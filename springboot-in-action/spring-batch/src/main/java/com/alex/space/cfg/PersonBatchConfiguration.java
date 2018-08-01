package com.alex.space.cfg;

import com.alex.space.pojo.Person;
import com.alex.space.processor.PersonItemProcessor;
import javax.sql.DataSource;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecutionListener;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.BeanPropertyItemSqlParameterSourceProvider;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.core.JdbcTemplate;

/**
 * @author Alex Created by Alex on 2017/12/29.
 */
@Configuration
@EnableBatchProcessing
public class PersonBatchConfiguration {

  private static final String PERSON_INSERT =
      "INSERT INTO Person (personName, personAge,personSex) VALUES (:personName, :personAge,:personSex)";

  //读数据
  @Bean
  public ItemReader<Person> reader() {
    FlatFileItemReader<Person> reader = new FlatFileItemReader<>();
    //加载外部文件数据 文件类型:CSV
    reader.setResource(new ClassPathResource("sample-data.csv"));
    reader.setLineMapper(new DefaultLineMapper<Person>() {{
      setLineTokenizer(new DelimitedLineTokenizer() {{
        setNames(new String[]{"personName", "personAge", "personSex"});
      }});
      setFieldSetMapper(new BeanWrapperFieldSetMapper<Person>() {{
        setTargetType(Person.class);
      }});
    }});
    return reader;
  }

  //处理数据
  @Bean
  public PersonItemProcessor processor() {
    return new PersonItemProcessor();
  }

  //写数据
  @Bean
  public ItemWriter<Person> writer(@Qualifier("dataSource") DataSource dataSource) {
    JdbcBatchItemWriter<Person> writer = new JdbcBatchItemWriter<>();
    writer.setItemSqlParameterSourceProvider(new BeanPropertyItemSqlParameterSourceProvider<>());
    writer.setSql(PERSON_INSERT);
    writer.setDataSource(dataSource);
    return writer;
  }

  //job step
  @Bean
  public Job importUserJob(JobBuilderFactory jobs, @Qualifier("step1") Step s1,
      JobExecutionListener listener) {
    return jobs.get("importUserJob")
        .incrementer(new RunIdIncrementer())
        .listener(listener)
        .flow(s1)
        .end()
        .build();
  }

  //job step
  @Bean
  public Step step1(StepBuilderFactory stepBuilderFactory,
      ItemReader<Person> reader, ItemWriter<Person> writer,
      ItemProcessor<Person, Person> processor) {
    return stepBuilderFactory.get("step1")
        .<Person, Person>chunk(10)
        .reader(reader)
        .processor(processor)
        .writer(writer)
        .build();
  }

  @Bean
  public JdbcTemplate jdbcTemplate(@Qualifier("dataSource") DataSource dataSource) {
    return new JdbcTemplate(dataSource);
  }
}
