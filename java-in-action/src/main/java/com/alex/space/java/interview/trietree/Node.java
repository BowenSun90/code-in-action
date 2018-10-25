package com.alex.space.java.interview.trietree;

import java.util.HashMap;
import java.util.Map;
import lombok.Getter;
import lombok.Setter;

/**
 * 字典树的节点
 *
 * @author Alex Created by Alex on 2018/10/25.
 */
@Getter
@Setter
public class Node {

  /**
   * 是否是单词
   */
  private boolean isWord;
  /**
   * 单词计数
   */
  private int count;
  /**
   * 字串
   */
  private String value;
  /**
   * 子节点
   */
  private Map<String, Node> childs;
  /**
   * 父节点
   */
  private Node parent;

  public Node() {
    childs = new HashMap<String, Node>();
  }

  public Node(boolean isWord, int count, String str) {
    this();
    this.isWord = isWord;
    this.count = count;
    this.value = str;
  }

  /**
   * 添加子节点
   */
  public void addChild(String key, Node node) {
    childs.put(key, node);
    node.parent = this;
  }

  /**
   * 移除字节点
   */
  public void removeChild(String key) {
    childs.remove(key);
  }

  /**
   * 子节点数+1
   */
  public void incrCount() {
    this.count++;
  }

  /**
   * 子节点数+1
   */
  public void decrCount() {
    this.count++;
  }

  @Override
  public String toString() {
    return "value : " + value + ", isWord : " + isWord + ", count : " + count;
  }

}
