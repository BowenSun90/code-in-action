package com.alex.space.java.interview.node;

/**
 * @author Alex Created by Alex on 2019/3/18.
 */
public class ListNode {

  public int val;
  public ListNode next;

  public ListNode(int x) {
    val = x;
  }

  @Override
  public String toString() {

    Solution solution = new Solution();
    ListNode node = solution.hasCycle(this);
    if (node == null) {
      return val + (next == null ? "" : "->" + next.toString());
    } else {
      StringBuilder sb = new StringBuilder();
      ListNode tmp = this;
      while (tmp != node) {
        sb.append(tmp.val);
        sb.append("->");
        tmp = tmp.next;
      }
      sb.append("(");
      sb.append(tmp.val);
      sb.append("->");
      tmp = tmp.next;
      while (tmp != node) {
        sb.append(tmp.val);
        sb.append("->");
        tmp = tmp.next;
      }
      sb.append(tmp.val);
      sb.append(")");
      return sb.toString();
    }

  }

}
