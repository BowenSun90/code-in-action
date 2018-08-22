package com.alex.space

import org.apache.flink.streaming.api.TimeCharacteristic
import org.apache.flink.streaming.api.functions.AssignerWithPeriodicWatermarks
import org.apache.flink.streaming.api.scala._
import org.apache.flink.streaming.api.watermark.Watermark
import org.apache.flink.streaming.api.windowing.time.Time

/**
  * @author Alex
  *         Created by Alex on 2018/8/19.
  */
object SocketEventTimeWindow {


  def main(args: Array[String]): Unit = {
    // the host and the port to connect to
    var hostname: String = "localhost"
    var port: Int = 9000

    // get the execution environment
    val env: StreamExecutionEnvironment = StreamExecutionEnvironment.getExecutionEnvironment

    // set event-time
    env.setStreamTimeCharacteristic(TimeCharacteristic.EventTime)

    // get input data by connecting to the socket
    val text = env.socketTextStream(hostname, port, '\n')
      .assignTimestampsAndWatermarks(new TimestampExtractor)

    // parse the data, group it, window it, and aggregate the counts
    val windowCounts = text
      .flatMap { w => w.split(",")(0) }
      .map { w => (w, 1) }
      .keyBy(0)
      .timeWindow(Time.seconds(10), Time.seconds(5))
      .sum(1)

    // print the results with a single thread, rather than in parallel
    windowCounts.print().setParallelism(1)

    env.execute("Socket Window WordCount")

  }

  class TimestampExtractor extends AssignerWithPeriodicWatermarks[String] with Serializable {

    override def extractTimestamp(e: String, prevElementTimestamp: Long) = {
      e.split(",")(1).toLong
    }

    override def getCurrentWatermark(): Watermark = {
      new Watermark(System.currentTimeMillis)
    }
  }

}
