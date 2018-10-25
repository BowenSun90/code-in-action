package com.alex.space.java.interview.trietree;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

/**
 * Trie树，即字典树，又称单词查找树或键树 <p> 典型应用是用于统计和排序大量的字符串，经常被搜索引擎系统用于文本词频统计 <p> 最大限度地减少无谓的字符串比较，查询效率比哈希表高 <p>
 * 核心思想是空间换时间。利用字符串的公共前缀来降低查询时间的开销以达到提高效率的目的。
 *
 * @author Alex Created by Alex on 2018/10/25.
 */
@Getter
@Setter
@Slf4j
public class TireTree {

  /**
   * 字典树根节点
   */
  private Node root;

  public TireTree() {
    root = new Node();
  }

  /**
   * 添加单词
   */
  public void add(String word) {
    internalAdd(word, root);
  }

  /**
   * 查找单词
   */
  public boolean find(String word) {
    return internalFind(word, root);
  }

  /**
   * 统计前缀单词数
   */
  public int count(String prefix) {
    // 处理特殊情况
    if (null == prefix || prefix.trim().isEmpty()) {
      return root.getCount();
    }
    // 从根结点往下匹配
    return internalCount(prefix, root);
  }

  /**
   * 打印字典树
   */
  public void print() {
    printNode(root, 0);
  }

  /**
   * 添加字串
   */
  private void internalAdd(String word, Node node) {

    // 计数
    node.incrCount();

    String value = node.getValue();
    if (null != value) {

      // 寻找公共前缀
      String commonPrefix = "";
      for (int i = 0; i < word.length(); i++) {
        if (value.length() > i && word.charAt(i) == value.charAt(i)) {
          commonPrefix += word.charAt(i);
        } else {
          break;
        }
      }

      // 找到公共前缀
      if (commonPrefix.length() > 0) {
        if (commonPrefix.length() == value.length() && commonPrefix.length() == word.length()) {
          // 与之前的词重复
        } else if (commonPrefix.length() == value.length() && commonPrefix.length() < word
            .length()) {
          // 剩余的串
          String wordLeft = word.substring(commonPrefix.length());
          // 剩余的串去子节点中继续找
          searchChild(wordLeft, node);
        } else if (commonPrefix.length() < value.length()) {
          // 节点裂变
          Node splitNode = new Node(true, node.getCount(), commonPrefix);
          // 处理裂变节点的父关系
          splitNode.setParent(node.getParent());
          splitNode.getParent().addChild(commonPrefix, splitNode);
          node.getParent().removeChild(node.getValue());
          node.decrCount();
          // 节点裂变后的剩余字串
          String strLeft = value.substring(commonPrefix.length());
          node.setValue(strLeft);
          splitNode.addChild(strLeft, node);
          // 单词裂变后的剩余字串
          if (commonPrefix.length() < word.length()) {
            splitNode.setWord(false);
            String wordLeft = word.substring(commonPrefix.length());
            Node leftNode = new Node(true, 1, wordLeft);
            splitNode.addChild(wordLeft, leftNode);
          }
        }
      } else {
        // 没有共同前缀，直接添加节点
        Node newNode = new Node(true, 1, word);
        node.addChild(word, newNode);
      }
    } else {
      // 根结点
      if (node.getChilds().size() > 0) {
        searchChild(word, node);
      } else {
        Node newNode = new Node(true, 1, word);
        node.addChild(word, newNode);
      }
    }
  }

  /**
   * 在子节点中添加字串
   */
  private void searchChild(String wordLeft, Node node) {
    boolean isFind = false;
    if (node.getChilds().size() > 0) {
      // 遍历孩子
      for (String childKey : node.getChilds().keySet()) {
        Node childNode = node.getChilds().get(childKey);
        // 首字母相同，则在该子节点继续添加字串
        if (wordLeft.charAt(0) == childNode.getValue().charAt(0)) {
          isFind = true;
          internalAdd(wordLeft, childNode);
          break;
        }
      }
    }
    // 没有首字母相同的孩子，则将其变为子节点
    if (!isFind) {
      Node newNode = new Node(true, 1, wordLeft);
      node.addChild(wordLeft, newNode);
    }
  }

  /**
   * 在节点中查找字串
   */
  private boolean internalFind(String word, Node node) {
    boolean isMatch = true;
    String wordLeft = word;
    String str = node.getValue();
    if (null != str) {
      // 字串与单词不匹配
      if (word.indexOf(str) != 0) {
        isMatch = false;
      } else {
        // 匹配，则计算剩余单词
        wordLeft = word.substring(str.length());
      }
    }
    // 如果匹配
    if (isMatch) {
      // 如果还有剩余单词长度
      if (wordLeft.length() > 0) {
        // 遍历孩子继续找
        for (String key : node.getChilds().keySet()) {
          Node childNode = node.getChilds().get(key);
          boolean isChildFind = internalFind(wordLeft, childNode);
          if (isChildFind) {
            return true;
          }
        }
        return false;
      } else {
        // 没有剩余单词长度，说明已经匹配完毕，直接返回节点是否为单词
        return node.isWord();
      }
    }
    return false;
  }

  /**
   * 统计子节点字串单词数
   */
  private int countChild(String prefix, Node node) {
    // 遍历孩子
    for (String key : node.getChilds().keySet()) {
      Node childNode = node.getChilds().get(key);
      // 匹配子节点
      int childCount = internalCount(prefix, childNode);
      if (childCount != 0) {
        return childCount;
      }
    }
    return 0;
  }

  /**
   * 统计字串单词数
   */
  private int internalCount(String prefix, Node node) {
    String value = node.getValue();
    // 非根结点
    if (null != value) {
      // 前缀与字串不匹配
      if (prefix.indexOf(value) != 0 && value.indexOf(prefix) != 0) {
        return 0;
        // 前缀匹配字串，且前缀较短
      } else if (value.indexOf(prefix) == 0) {
        // 找到目标节点，返回单词数
        return node.getCount();
        // 前缀匹配字串，且字串较短
      } else if (prefix.indexOf(value) == 0) {
        // 剩余字串继续匹配子节点
        String prefixLeft = prefix.substring(value.length());
        if (prefixLeft.length() > 0) {
          return countChild(prefixLeft, node);
        }
      }
    } else {
      // 根结点，直接找其子孙
      return countChild(prefix, node);
    }
    return 0;
  }

  /**
   * 打印节点
   */
  private void printNode(Node node, int layer) {
    // 层级递进
    // 打印
    log.info(node.toString());
    // 递归打印子节点
    for (String str : node.getChilds().keySet()) {
      Node child = node.getChilds().get(str);
      printNode(child, layer + 1);
    }
  }

}
