package com.alex.space.hbase.spark

import java.util.Date

import org.apache.hadoop.fs.Path
import org.apache.hadoop.hbase.client.{ConnectionFactory, HTable, Table}
import org.apache.hadoop.hbase.io.ImmutableBytesWritable
import org.apache.hadoop.hbase.mapreduce.{HFileOutputFormat2, LoadIncrementalHFiles}
import org.apache.hadoop.hbase.util.Bytes
import org.apache.hadoop.hbase.{HBaseConfiguration, KeyValue, TableName}
import org.apache.hadoop.mapreduce.Job
import org.apache.spark.{SparkConf, SparkContext}

/**
  * Spark创建并
  *
  * @author Alex
  *         Created by Alex on 2018/10/26.
  */
object CreateAndBulkloadHFile {

  def main(args: Array[String]): Unit = {
    // 生成HFile
    val sc = new SparkContext(new SparkConf().setMaster("local[*]").setAppName("DumpFile"))
    val cf = "d"
    val conf = HBaseConfiguration.create()

    val date = new Date().getTime

    val srcRdd = sc.parallelize(
      Array(
        // rowkey,family,qualifier,value
        (Bytes.toBytes("1"), Bytes.toBytes(cf), Bytes.toBytes("c1"), Bytes.toBytes("v1")),
        (Bytes.toBytes("1"), Bytes.toBytes(cf), Bytes.toBytes("c2"), Bytes.toBytes("v2")),
        (Bytes.toBytes("2"), Bytes.toBytes(cf), Bytes.toBytes("c1"), Bytes.toBytes("v3")),
        (Bytes.toBytes("2"), Bytes.toBytes(cf), Bytes.toBytes("c2"), Bytes.toBytes("v4"))
      ))

    val rdd = srcRdd.map(x => {
      // 转换成HFile需要的格式
      (new ImmutableBytesWritable(x._1), new KeyValue(x._1, x._2, x._3, date, x._4))
    })

    // 生成的HFile的临时保存路径
    val stagingFolder = "data/hbase/hfile/"
    rdd.saveAsNewAPIHadoopFile(stagingFolder,
      classOf[ImmutableBytesWritable],
      classOf[KeyValue],
      classOf[HFileOutputFormat2],
      conf)

    // HFile导入到Hbase
    val load = new LoadIncrementalHFiles(conf)
    val tableName = "test_blukload"

    val conn = ConnectionFactory.createConnection(conf)
    val table: Table = conn.getTable(TableName.valueOf(tableName))
    try {

      val regionLocator = conn.getRegionLocator(TableName.valueOf(tableName))
      val job = Job.getInstance(conf)
      job.setJobName("DumpFile")
      //此处最重要,需要设置文件输出的key,因为我们要生成HFil,所以outkey要用ImmutableBytesWritable
      job.setMapOutputKeyClass(classOf[ImmutableBytesWritable])
      job.setMapOutputValueClass(classOf[KeyValue])

      //配置HFileOutputFormat2的信息
      HFileOutputFormat2.configureIncrementalLoad(job, table, regionLocator)
      load.doBulkLoad(new Path(stagingFolder), table.asInstanceOf[HTable])

    } finally {
      table.close()
      conn.close()
    }

  }
}
