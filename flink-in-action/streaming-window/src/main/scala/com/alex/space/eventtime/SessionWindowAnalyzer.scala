package com.alex.space.eventtime

import com.alex.space.KafkaConsumerEventTime.{AnalyzeResult, Event}
import org.apache.flink.streaming.api.scala.function.RichWindowFunction
import org.apache.flink.streaming.api.windowing.windows.TimeWindow
import org.apache.flink.util.Collector

/**
  * Analyze session window content
  *
  * @author Alex
  *         Created by Alex on 2018/8/23.
  */
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
