package com.alex.space

import java.util.Properties

import com.alex.space.eventtime.{SessionWindowAnalyzer, TimestampAndWatermarkAssigner}
import org.apache.flink.streaming.api.scala.{StreamExecutionEnvironment, _}
import org.apache.flink.streaming.api.windowing.assigners.EventTimeSessionWindows
import org.apache.flink.streaming.api.windowing.time.Time
import org.apache.flink.streaming.api.{CheckpointingMode, TimeCharacteristic}
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaConsumer09
import org.apache.flink.streaming.util.serialization.SimpleStringSchema

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
    env.enableCheckpointing(1000)
    env.getCheckpointConfig.setCheckpointingMode(CheckpointingMode.EXACTLY_ONCE)

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

  case class AnalyzeResult(user: String, windowStart: Long, windowEnd: Long, windowSize: Int, totalSize: Int, detail: String)


}
