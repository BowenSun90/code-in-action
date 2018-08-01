package com.alex.space.processor;

import com.alex.space.pojo.Person;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;

/**
 * @author Alex Created by Alex on 2017/12/29.
 */
@Slf4j
public class PersonItemProcessor implements ItemProcessor<Person, Person> {

  private static final String GET_PRODUCT = "SELECT * FROM Person WHERE personName = ?";

  @Autowired
  private JdbcTemplate jdbcTemplate;

  @Override
  public Person process(Person person) {
    List<Person> personList = jdbcTemplate
        .query(GET_PRODUCT, new Object[]{person.getPersonName()}, (resultSet, rowNum) -> {
          Person p = new Person();
          p.setPersonName(resultSet.getString(1));
          p.setPersonAge(resultSet.getString(2));
          p.setPersonSex(resultSet.getString(3));
          return p;
        });

    if (personList.size() > 0) {
      log.info("该数据已录入!!!");
    }

    String sex;
    if (person.getPersonSex().equals("0")) {
      sex = "男";
    } else {
      sex = "女";
    }
    log.info("转换 (性别：" + person.getPersonSex() + ") 为 (" + sex + ")");
    final Person transformedPerson = new Person(person.getPersonName(), person.getPersonAge(), sex);
    log.info("转换 (" + person + ") 为 (" + transformedPerson + ")");

    return transformedPerson;
  }


}
