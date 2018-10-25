package com.alex.space.java.interview.trietree;

import lombok.extern.slf4j.Slf4j;

/**
 * @author Alex Created by Alex on 2018/10/25.
 */
@Slf4j
public class Client {

  public static void main(String[] args) {
    TireTree tireTree = new TireTree();

    tireTree.add("version");
    tireTree.add("encoding");
    tireTree.add("encode");
    tireTree.add("appender");
    tireTree.add("apple");
    tireTree.add("append");
    tireTree.add("java");
    tireTree.add("javascript");
    tireTree.add("com");
    tireTree.add("app");
    tireTree.add("come");
    tireTree.add("alex");
    tireTree.add("space");
    tireTree.add("value");
    tireTree.add("coming");

    tireTree.print();

    boolean isFind = tireTree.find("com");
    log.info("find inside : " + isFind);

    int count = tireTree.count("com");
    log.info("count prefix inter : " + count);


  }
}
