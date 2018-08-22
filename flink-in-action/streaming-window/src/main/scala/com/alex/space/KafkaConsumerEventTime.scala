package com.alex.space

import java.util.Properties

import org.apache.flink.streaming.api.scala.{StreamExecutionEnvironment, _}
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

    val input_topic = "test"

    val stream = env.addSource(
      new FlinkKafkaConsumer09[String](input_topic, new SimpleStringSchema(), kafkaProps)
    )
      .flatMap(_.split(" "))
      .map((_, 1))

    // Time windows
    val tumbling = stream
      .keyBy(0)
      .timeWindow(Time.seconds(10))
      .sum(1)

    tumbling.print()

//    val sliding = stream
//      .keyBy(0)
//      .timeWindow(Time.seconds(10), Time.seconds(5))
//      .sum(1)
//
//    sliding.print()


    env.execute()

  }
}
