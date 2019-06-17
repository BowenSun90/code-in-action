package com.alex.space.quartz;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

/**
 * @author Alex Created by Alex on 2019/6/17.
 */
public class HelloJob implements Job {

  public void execute(final JobExecutionContext jobExecutionContext) throws JobExecutionException {
    System.out.println(System.currentTimeMillis() + " - helloJob 任务执行");
  }
}
