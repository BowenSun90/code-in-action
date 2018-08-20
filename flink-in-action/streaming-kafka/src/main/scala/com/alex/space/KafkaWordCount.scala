package com.alex.space

import org.apache.flink.api.common.restartstrategy.RestartStrategies
import org.apache.flink.api.common.serialization.SimpleStringSchema
import org.apache.flink.api.java.utils.ParameterTool
import org.apache.flink.streaming.api.scala._
import org.apache.flink.streaming.connectors.kafka.{FlinkKafkaConsumer011, FlinkKafkaProducer011}

/**
  * Flink integrate Kakfa consumer and producer
  *
  * @author alex
  */
object KafkaWordCount {

  def main(args: Array[String]) {
    // parse input arguments
    val params = ParameterTool.fromArgs(args)

    if (params.getNumberOfParameters < 4) {
      println("Missing parameters!\n"
        + "Usage: Kafka --input-topic <topic> --output-topic <topic> "
        + "--bootstrap.servers <kafka brokers> "
        + "--zookeeper.connect <zk quorum> --group.id <some id> [--prefix <prefix>]")
      return
    }

    val prefix = params.get("prefix", "")

    val env = StreamExecutionEnvironment.getExecutionEnvironment

    // set restart strategy
    env.getConfig.setRestartStrategy(RestartStrategies.fixedDelayRestart(4, 1000))

    // create a checkpoint every 5 seconds
    env.enableCheckpointing(5000)

    // make parameters available in the web interface
    env.getConfig.setGlobalJobParameters(params)

    // create a Kafka streaming source consumer for Kafka 0.11.x
    val kafkaConsumer = new FlinkKafkaConsumer011(
      params.getRequired("input-topic"),
      new SimpleStringSchema,
      params.getProperties
    )

    val messageStream = env.addSource(kafkaConsumer)
      //      .map(prefix + _)
      .flatMap(_.split("\\W+"))
      .map((_, 1))
      .keyBy(0)
      .sum(1)
      .map(x => x._1 + "\t" + x._2)

    // create a Kafka producer for Kafka 0.11.x
    val kafkaProducer = new FlinkKafkaProducer011(
      params.getRequired("output-topic"),
      new SimpleStringSchema,
      params.getProperties
    )

    messageStream.addSink(kafkaProducer)

    env.execute("Kafka WordCount example")


  }

}
