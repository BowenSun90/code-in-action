package com.alex.space.pojo;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @author Alex Created by Alex on 2017/12/29.
 */
@Getter
@Setter
@NoArgsConstructor
public class Person {

  /**
   * ID
   */
  private Integer personId;
  /**
   * 姓名
   */
  private String personName;
  /**
   * 年龄
   */
  private String personAge;
  /**
   * 性别
   */
  private String personSex;

  public Person(String personName, String personAge, String personSex) {
    this.personName = personName;
    this.personAge = personAge;
    this.personSex = personSex;
  }
}
