package com.alex.space.java.interview.util;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

/**
 * @author Alex Created by Alex on 2019/3/13.
 */
public class Generator {

  private final static String chars = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890";

  public static String randomString() {
    int len = ThreadLocalRandom.current().nextInt(20);
    StringBuilder sb = new StringBuilder();
    for (int i = 0; i < len; i++) {
      sb.append(chars.charAt(ThreadLocalRandom.current().nextInt(chars.length())));
    }

    return sb.toString();
  }

  public static List<String> randomStringList(int size) {
    List<String> list = new ArrayList<>(size);

    for (int i = 0; i < size; i++) {
      list.add(randomString());
    }

    return list;
  }

  public static int[][] randomGrid(int h, int w) {
    int[][] result = new int[h][w];
    for (int i = 0; i < h; i++) {
      for (int j = 0; j < w; j++) {
        result[i][j] = ThreadLocalRandom.current().nextInt(100) % 5 == 0 ?
            1 : 0;
      }
    }

    return result;
  }

}
