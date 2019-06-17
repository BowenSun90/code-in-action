package com.alex.space.quartz;

import static org.quartz.JobBuilder.newJob;
import static org.quartz.SimpleScheduleBuilder.simpleSchedule;
import static org.quartz.TriggerBuilder.newTrigger;

import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.Trigger;
import org.quartz.impl.StdSchedulerFactory;

/**
 * @author Alex Created by Alex on 2019/6/17.
 */
public class QuartzTest {

  public static void main(String[] args) {
    try {
      // 获取一个调度程序的实例
      Scheduler scheduler = StdSchedulerFactory.getDefaultScheduler();
      System.out.println(scheduler.getSchedulerName() + " - " + scheduler.getSchedulerInstanceId());

      // 定义一个 job，并绑定到 HelloJob.class
      // 这里并不会马上创建一个 HelloJob 实例，实例创建是在 scheduler 安排任务触发执行时创建的
      JobDetail job = newJob(HelloJob.class)
          .withIdentity("job1", "group1")
          .build();

      // 声明一个触发器
      // schedule.start() 方法开始调用的时候执行，每间隔2秒执行一次
      Trigger trigger = newTrigger()
          .withIdentity("trigger1", "group1")
          .startNow()
          .withSchedule(
              simpleSchedule().withIntervalInSeconds(3).repeatForever()
          )
          .build();

      // 安排执行任务
      scheduler.scheduleJob(job, trigger);

      // 启动任务调度程序（内部机制是线程的启动）
      scheduler.start();

      Thread.sleep(30000);

      // 关闭任务调度程序
      scheduler.shutdown();

    } catch (SchedulerException e) {
      e.printStackTrace();
    } catch (InterruptedException e) {
      e.printStackTrace();
    }


  }
}
