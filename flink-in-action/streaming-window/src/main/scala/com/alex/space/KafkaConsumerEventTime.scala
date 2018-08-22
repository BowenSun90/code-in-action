package com.alex.space

import java.util.Properties

import org.apache.flink.streaming.api.TimeCharacteristic
import org.apache.flink.streaming.api.functions.AssignerWithPeriodicWatermarks
import org.apache.flink.streaming.api.scala.function.RichWindowFunction
import org.apache.flink.streaming.api.scala.{StreamExecutionEnvironment, _}
import org.apache.flink.streaming.api.watermark.Watermark
import org.apache.flink.streaming.api.windowing.assigners.EventTimeSessionWindows
import org.apache.flink.streaming.api.windowing.time.Time
import org.apache.flink.streaming.api.windowing.windows.TimeWindow
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaConsumer09
import org.apache.flink.streaming.util.serialization.SimpleStringSchema
import org.apache.flink.util.Collector

/**
  *
  * @author Alex
  *         Created by Alex on 2018/8/22.
  */
object KafkaConsumerEventTime {

  val zookeeper_host = "localhost:2181"
  val kafka_broker = "localhost:9092"

  def main(args: Array[String]): Unit = {
    val env = StreamExecutionEnvironment.getExecutionEnvironment

    // set time characteristic
    env.setStreamTimeCharacteristic(TimeCharacteristic.EventTime)

    // set checkpoint
    //    env.enableCheckpointing(2000)
    //    env.getCheckpointConfig.setCheckpointingMode(CheckpointingMode.EXACTLY_ONCE)

    // set kafka consumer
    val kafkaProps = new Properties()
    kafkaProps.setProperty("zookeeper.connect", zookeeper_host)
    kafkaProps.setProperty("bootstrap.servers", kafka_broker)

    // set parallelism
    env.setParallelism(2)

    val input_topic = "test"

    // kafka输入： timestamp \t user \t message
    val stream = env.addSource(
      new FlinkKafkaConsumer09[String](input_topic, new SimpleStringSchema(), kafkaProps)
    )
      .map(x => {
        val param = x.split("\t")
        Event(param(0).toLong, param(1), param(2))
      })

    val session = stream
      .assignTimestampsAndWatermarks(new TimestampAndWatermarkAssigner)
      .keyBy(_.user)
      .window(EventTimeSessionWindows.withGap(Time.seconds(5)))
      .apply(new SessionWindowAnalyzer)

    session.print()

    env.execute()

  }

  case class Event(timestamp: Long, user: String, msg: String)

  class TimestampAndWatermarkAssigner extends AssignerWithPeriodicWatermarks[Event] {
    var currentMaxTimestamp = 0L

    val maxOutOfOrder = 10000L

    override def getCurrentWatermark: Watermark = new Watermark(currentMaxTimestamp - maxOutOfOrder)

    override def extractTimestamp(t: Event, l: Long): Long = {
      currentMaxTimestamp = t.timestamp max currentMaxTimestamp
      t.timestamp
    }
  }


  case class AnalyzeResult(user: String, windowStart: Long, windowEnd: Long, windowSize: Int, totalSize: Int, detail: String)

  class SessionWindowAnalyzer extends RichWindowFunction[Event, AnalyzeResult, String, TimeWindow] {
    var size = 0

    override def apply(key: String, window: TimeWindow, input: Iterable[Event], out: Collector[AnalyzeResult]): Unit = {
      val list = input.toList.sortBy(_.timestamp).map(_.msg).mkString("\t")

      val window_start = window.getStart
      val window_end = window.getEnd
      val window_size = input.size

      size = size + window_size

      out.collect(AnalyzeResult(key, window_start, window_end, window_size, size, list))
    }
  }

}
