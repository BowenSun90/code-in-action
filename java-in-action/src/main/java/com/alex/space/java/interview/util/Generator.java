package com.alex.space.java.interview.util;

import com.alex.space.java.interview.node.ListNode;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;
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
        result[i][j] = ThreadLocalRandom.current().nextInt(20) % 2 == 0 ?
            1 : 0;
      }
    }

    return result;
  }

  public static int[][] randomGrid(int size) {
    int[][] result = new int[size][size];
    for (int i = 0; i < size; i++) {
      for (int j = 0; j < size; j++) {
        result[i][j] = ThreadLocalRandom.current().nextInt(100) % 10 == 0 ?
            1 : 0;
        result[j][i] = result[i][j];
      }
    }

    for (int i = 0; i < size; i++) {
      result[i][i] = 1;
    }

    return result;
  }

  public static ListNode randomList(int size, boolean inOrder) {
    Random random = new Random();

    int[] nums = new int[size * 10];

    for (int i = 0; i < nums.length; i++) {

      if (inOrder) {
        nums[i] = i + 1;
      } else {
        nums[i] = random.nextInt((i + 5) * 5);
      }
    }

    int idx = random.nextInt(10);
    ListNode root = new ListNode(0);
    ListNode tmp = root;

    for (int i = 0; i < size; i++) {
      idx += random.nextInt(5) + 1;
      tmp.next = new ListNode(nums[idx]);
      tmp = tmp.next;
    }

    return root.next;
  }

  public static ListNode cycleList(int size) {
    Random random = new Random();
    ListNode root = new ListNode(0);
    int point = random.nextInt(size);

    Set<Integer> set = new HashSet<>();
    ListNode pointNode = null;
    ListNode tmp = root;
    for (int i = 0; i < size; i++) {
      int t = random.nextInt(i * 3 + 5);
      while (set.contains(t)) {
        t = random.nextInt(i * 3 + 5);
      }
      set.add(t);
      ListNode node = new ListNode(t);
      tmp.next = node;
      tmp = node;
      if (i == point) {
        pointNode = node;
      }
    }

    tmp.next = pointNode;

    return root.next;
  }

  public static int[] randomArray(int size) {

    return randomArray(size, false);
  }

  public static int[] randomArray(int size, boolean allowDup) {
    return randomArray(size, allowDup, false);
  }

  public static int[] randomArray(int size, boolean allowDup, boolean allowNegative) {
    int[] result = new int[size];

    Set<Integer> set = new HashSet<>();

    for (int i = 0; i < size; i++) {
      int t = ThreadLocalRandom.current().nextInt(i + 10);
      if (allowNegative) {
        t = t * (ThreadLocalRandom.current().nextInt(10) % 3 == 0 ? -1 : 1);
      }
      if (!allowDup && set.contains(t)) {
        t = ThreadLocalRandom.current().nextInt(i + 20);
      }
      set.add(t);
      result[i] = t;
    }

    return result;
  }

  public static List<List<Integer>> randomTriangle(int deep) {
    List<List<Integer>> res = new ArrayList<>();

    for (int i = 1; i <= deep; i++) {
      List<Integer> list = new ArrayList<>();
      for (int j = 0; j < i; j++) {
        list.add(ThreadLocalRandom.current().nextInt(10));
      }
      res.add(list);
    }

    return res;
  }

}
