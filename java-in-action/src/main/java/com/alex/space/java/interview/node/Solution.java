package com.alex.space.java.interview.node;

import com.alex.space.java.interview.util.Generator;
import java.util.Comparator;
import java.util.PriorityQueue;
import lombok.extern.slf4j.Slf4j;

/**
 * @author Alex Created by Alex on 2019/3/18.
 */
@Slf4j
public class Solution {

  public static void main(String[] args) {
    Solution solution = new Solution();

    ListNode list1 = Generator.randomList(5, true);
    ListNode list2 = Generator.randomList(5, true);
    ListNode list3 = Generator.randomList(5, false);
    ListNode cycleList = Generator.cycleList(5);

//    log.info("{}", solution.mergeTwoLists(list1, list2));
//    log.info("{}", solution.reverseList(list1));
//    log.info("{}", solution.reverseList1(list1));

//    log.info("{}", solution.sortList(list3));

//    log.info("{}", solution.detectCycle(list1));
//    log.info("{}", solution.detectCycle(cycleList));

    log.info("{}", solution.mergeKLists(new ListNode[]{list1, list2, list3}));

  }

  // 合并两个有序链表

  public ListNode mergeTwoLists(ListNode l1, ListNode l2) {
    if (l1 == null) {
      return l2;
    }
    if (l2 == null) {
      return l1;
    }

    ListNode root;
    if (l1.val < l2.val) {
      root = l1;
      root.next = mergeTwoLists(l1.next, l2);
    } else {
      root = l2;
      root.next = mergeTwoLists(l1, l2.next);
    }

    return root;
  }

  // 反转链表

  public ListNode reverseList(ListNode head) {

    ListNode prev = null;
    ListNode cur = head;
    ListNode newHead = null;
    while (cur != null) {
      ListNode next = cur.next;
      if (next == null) {
        newHead = cur;
      }
      cur.next = prev;
      prev = cur;
      cur = next;
    }

    return newHead;
  }

  public ListNode reverseList1(ListNode head) {
    if (head == null || head.next == null) {
      return head;
    } else {
      ListNode node = reverseList1(head.next);
      head.next.next = head;
      head.next = null;
      return node;
    }
  }

  // 链表排序

  public ListNode sortList(ListNode head) {
    if (head == null) {
      return head;
    }

    return mergeSort(head);
  }

  private ListNode mergeSort(ListNode head) {
    if (head.next == null) {
      return head;
    }
    ListNode slow = head, fast = head, pre = null;
    while (fast != null && fast.next != null) {
      pre = slow;
      slow = slow.next;
      fast = fast.next.next;
    }
    pre.next = null;
    ListNode l = mergeSort(head);
    ListNode r = mergeSort(slow);
    return mergeTwoLists(l, r);
  }

  // 环形链表 II

  public ListNode detectCycle(ListNode head) {
    if (head == null || head.next == null) {
      return null;
    }

    ListNode node = hasCycle(head);

    if (node != null) {
      ListNode p = head;
      while (!p.equals(node)) {
        node = node.next;
        p = p.next;
      }
      return p;
    }

    return null;
  }

  public ListNode hasCycle(ListNode head) {
    ListNode slow = head, fast = head;
    while (fast != null && fast.next != null) {
      slow = slow.next;
      fast = fast.next.next;
      if (slow.equals(fast)) {
        return slow;
      }
    }

    return null;
  }

  // 合并K个排序链表

  public ListNode mergeKLists(ListNode[] lists) {
    if (lists == null || lists.length == 0) {
      return null;
    }

    int length = lists.length;
    int interval = 1;
    while (interval < length) {
      for (int i = 0; i < length - interval; i += interval * 2) {
        lists[i] = mergeTwoLists(lists[i], lists[i + interval]);
        lists[i + interval] = null;
      }
      interval *= 2;
    }
    return lists[0];
  }

  public ListNode mergeKLists1(ListNode[] lists) {
    ListNode result = new ListNode(0);
    ListNode point = result;
    PriorityQueue<ListNode> q = new PriorityQueue<>(valComparator);
    for (ListNode node : lists) {
      if (node != null) {
        q.add(node);
      }
    }

    while (!q.isEmpty()) {
      ListNode cur = q.poll();
      point.next = new ListNode(cur.val);
      point = point.next;
      cur = cur.next;
      if (cur != null) {
        q.add(cur);
      }
    }
    return result.next;
  }

  private static Comparator<ListNode> valComparator = (o1, o2) -> (int) (o1.val - o2.val);

}
