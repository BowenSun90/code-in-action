package com.alex.space.eventtime

import org.apache.flink.streaming.api.windowing.triggers.Trigger.TriggerContext
import org.apache.flink.streaming.api.windowing.triggers.{Trigger, TriggerResult}
import org.apache.flink.streaming.api.windowing.windows.TimeWindow

/**
  * @author Alex
  *         Created by Alex on 2018/8/24.
  */
class SessionWindowTrigger extends Trigger[Object, TimeWindow] {

  override def onElement(element: Object, timestamp: Long, window: TimeWindow, ctx: TriggerContext): TriggerResult = {
    println("window.maxTimestamp: " + window.maxTimestamp)
    println("ctx.getCurrentWatermark:" + ctx.getCurrentWatermark)
    if (window.maxTimestamp <= ctx.getCurrentWatermark) {
      TriggerResult.FIRE
    } else {
      ctx.registerEventTimeTimer(window.maxTimestamp)
      TriggerResult.CONTINUE
    }
  }

  override def onEventTime(time: Long, window: TimeWindow, ctx: TriggerContext): TriggerResult = {
    println("time: " + time)
    println("window.maxTimestamp: " + window.maxTimestamp)
    if (time == window.maxTimestamp) TriggerResult.FIRE else TriggerResult.CONTINUE
  }

  override def onProcessingTime(time: Long, window: TimeWindow, ctx: TriggerContext): TriggerResult = TriggerResult.CONTINUE

  override def clear(window: TimeWindow, ctx: TriggerContext): Unit = ctx.deleteEventTimeTimer(window.maxTimestamp)

  override def onMerge(window: TimeWindow, ctx: Trigger.OnMergeContext): Unit = super.onMerge(window, ctx)

  override def canMerge: Boolean = super.canMerge

  override def toString: String = "SessionWindowTrigger()"
  
}
