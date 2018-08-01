package com.alex.space.listener;

import com.alex.space.pojo.Person;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.listener.JobExecutionListenerSupport;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

/**
 * @author Alex Created by Alex on 2017/12/29.
 */
@Slf4j
@Component
public class JobCompletionNotificationListener extends JobExecutionListenerSupport {

  private static final String PERSON_SQL = "SELECT personName, personAge,personSex FROM Person";

  private JdbcTemplate jdbcTemplate;

  @Autowired
  public JobCompletionNotificationListener(JdbcTemplate jdbcTemplate) {
    if (this.jdbcTemplate == null) {
      this.jdbcTemplate = jdbcTemplate;
    }
  }

  @Override
  public void beforeJob(JobExecution jobExecution) {
    // TODO Auto-generated method stub
    super.beforeJob(jobExecution);
  }

  @Override
  public void afterJob(JobExecution jobExecution) {
    if (jobExecution.getStatus() == BatchStatus.COMPLETED) {
      log.info("!!! JOB 执行完成!");
      List<Person> results = jdbcTemplate.query(PERSON_SQL,
          (rs, row) -> new Person(rs.getString(1), rs.getString(2), rs.getString(3)));
      log.info("入库条数---------" + results.size());
      for (Person person : results) {
        log.info("新增 <" + person.getPersonName() + "> 成功!!!!!");
      }

    }
  }

}
