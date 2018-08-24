package com.alex.space.eventtime

import com.alex.space.KafkaConsumerEventTime.Event
import org.apache.flink.streaming.api.functions.AssignerWithPeriodicWatermarks
import org.apache.flink.streaming.api.watermark.Watermark

/**
  * Assign timestamp and watermark
  *
  * @author Alex
  *         Created by Alex on 2018/8/23.
  */
class TimestampAndWatermarkAssigner extends AssignerWithPeriodicWatermarks[Event] {
  var currentMaxTimestamp = 0L

  val maxOutOfOrder = 10000L

  override def getCurrentWatermark: Watermark = new Watermark(currentMaxTimestamp - maxOutOfOrder)

  override def extractTimestamp(t: Event, l: Long): Long = {
    currentMaxTimestamp = t.timestamp max currentMaxTimestamp
    t.timestamp
  }
}
