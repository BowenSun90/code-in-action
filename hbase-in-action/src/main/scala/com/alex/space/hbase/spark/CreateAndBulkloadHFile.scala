package com.alex.space.hbase.spark

import java.util.Date

import org.apache.hadoop.conf.Configuration
import org.apache.hadoop.fs.Path
import org.apache.hadoop.hbase.client.{ConnectionFactory, HTable, Table}
import org.apache.hadoop.hbase.io.ImmutableBytesWritable
import org.apache.hadoop.hbase.mapreduce.{HFileOutputFormat2, LoadIncrementalHFiles}
import org.apache.hadoop.hbase.util.Bytes
import org.apache.hadoop.hbase.{HBaseConfiguration, KeyValue, TableName}
import org.apache.hadoop.mapreduce.Job
import org.apache.spark.{SparkConf, SparkContext}

/**
  * Spark创建HFile
  * Bulkload导入HBase table
  *
  * @author Alex
  *         Created by Alex on 2018/10/26.
  */
object CreateAndBulkloadHFile {

  def main(args: Array[String]): Unit = {
    // Step1 生成HFile
    // HFile要生成在HDFS上
    // 本地调试，HBase Bulkload会失败
    val sc = new SparkContext(new SparkConf().setMaster("local[*]").setAppName("DumpFile"))

    val hadoopConf = new Configuration()

    val tableName = "bulk_load_table_1"
    val cf = "d"
    val date = new Date().getTime

    val srcRdd = sc.parallelize(
      Array(
        // rowkey,family,qualifier,value
        (Bytes.toBytes("rowkey1"), Bytes.toBytes(cf), Bytes.toBytes("c1"), Bytes.toBytes("v1")),
        (Bytes.toBytes("rowkey2"), Bytes.toBytes(cf), Bytes.toBytes("c2"), Bytes.toBytes("v2")),
        (Bytes.toBytes("rowkey1"), Bytes.toBytes(cf), Bytes.toBytes("c1"), Bytes.toBytes("v3")),
        (Bytes.toBytes("rowkey3"), Bytes.toBytes(cf), Bytes.toBytes("c2"), Bytes.toBytes("v4"))
      ))

    val rdd = srcRdd
      .map(x => {
        // 转换成HFile需要的格式
        (new ImmutableBytesWritable(x._1), new KeyValue(x._1, x._2, x._3, date, x._4))
      })

    // 生成的HFile的临时保存路径
    val stagingFolder = "data/hbase/hfile/"
    rdd.saveAsNewAPIHadoopFile(stagingFolder,
      classOf[ImmutableBytesWritable],
      classOf[KeyValue],
      classOf[HFileOutputFormat2],
      hadoopConf)

    // Step2 HFile导入到Hbase
    //  hbase conf
    val hbaseConf = HBaseConfiguration.create()
    // dev：环境变量设置HADOOP_USER_NAME
    hbaseConf.set("HADOOP_USER_NAME", "hadoop")
    hbaseConf.set("hbase.zookeeper.quorum", "172.23.4.138,172.23.4.139,172.23.4.140")
    hbaseConf.set("hbase.zookeeper.property.clientPort", "2181")
    hbaseConf.set("hbase.rootdir", "hdfs://namenode01.td.com/hbase")
    hbaseConf.set("fs.defaultFS", "hdfs://namenode01.td.com")
    hbaseConf.set("hbase.scanner.timeout.period", "60000")
    val load = new LoadIncrementalHFiles(hbaseConf)

    val conn = ConnectionFactory.createConnection(hbaseConf)
    val table: Table = conn.getTable(TableName.valueOf(tableName))

    try {
      val regionLocator = conn.getRegionLocator(TableName.valueOf(tableName))
      val job = Job.getInstance(hbaseConf)
      job.setJobName("LoadFile")
      // 此处最重要，需要设置文件输出的key，要生成HFile，所以OutputKey要用ImmutableBytesWritable
      job.setMapOutputKeyClass(classOf[ImmutableBytesWritable])
      job.setMapOutputValueClass(classOf[KeyValue])

      // 配置HFileOutputFormat2的信息
      HFileOutputFormat2.configureIncrementalLoad(job, table, regionLocator)

      // bulkload后会删除hfile，要做好备份
      val hdfsPath = "/bulkload/test1"
      load.doBulkLoad(new Path(hdfsPath), table.asInstanceOf[HTable])

    } finally {
      table.close()
      conn.close()
    }

  }

}
