package com.alex.space

import java.util.Properties

import org.apache.flink.streaming.api.scala.{StreamExecutionEnvironment, _}
import org.apache.flink.streaming.api.windowing.time.Time
import org.apache.flink.streaming.api.{CheckpointingMode, TimeCharacteristic}
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaConsumer09
import org.apache.flink.streaming.util.serialization.SimpleStringSchema

/**
  * Kafka consumer processing time stream
  *
  * @author Alex
  *         Created by Alex on 2018/8/22.
  */
object KafkaConsumerProcessing {

  val zookeeper_host = "localhost:2181"
  val kafka_broker = "localhost:9092"

  def main(args: Array[String]): Unit = {
    val env = StreamExecutionEnvironment.getExecutionEnvironment

    // set time characteristic
    env.setStreamTimeCharacteristic(TimeCharacteristic.ProcessingTime)

    // set checkpoint
    env.enableCheckpointing(1000)
    env.getCheckpointConfig.setCheckpointingMode(CheckpointingMode.EXACTLY_ONCE)

    // set parallelism
    env.setParallelism(3)

    // set kafka consumer
    val kafkaProps = new Properties()
    kafkaProps.setProperty("zookeeper.connect", zookeeper_host)
    kafkaProps.setProperty("bootstrap.servers", kafka_broker)

    val input_topic = "test"

    // kafka输入： 空格分隔的随机长度字符串 例：xxxx XXXX XXX
    val stream = env.addSource(
      new FlinkKafkaConsumer09[String](input_topic, new SimpleStringSchema(), kafkaProps)
    )
      .flatMap(_.split(" "))
      .flatMap(_.toCharArray.toString.toUpperCase)
      .map((_, 1))

    var windowType = "1"
    if (args.length > 0) {
      windowType = args(0)
    }

    windowType match {
      case "1" =>
        // Tumbling windows
        val tumbling = stream
          .keyBy(0)
          .timeWindow(Time.seconds(10))
          .sum(1)
          .map(x => ("tumbling", x._1, x._2))

        tumbling.print()
      case "2" =>
        // Sliding windows
        val sliding = stream
          .keyBy(0)
          .timeWindow(Time.seconds(10), Time.seconds(2))
          .sum(1)
          .map(x => ("sliding", x._1, x._2))

        sliding.print()
      case "3" =>
      // Session windows

      case "4" =>
      // Global windows

      case _ =>
        println("exit")
    }

    env.execute()

  }
}
