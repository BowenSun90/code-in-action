package com.alex.space.java.interview.string;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import lombok.extern.slf4j.Slf4j;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

/**
 * @author Alex Created by Alex on 2019/3/13.
 */
@Slf4j
public class Solution {

  public static void main(String[] args) {
    Solution solution = new Solution();

//    log.info("{}", solution.lengthOfLongestSubstring("abcadcae"));
//    log.info("{}", solution.lengthOfLongestSubstring1("abcadcae"));
//    log.info("{}", solution.lengthOfLongestSubstring2("abcadcae"));
//
//    log.info("{}", solution.multiply("123", "456"));
//    log.info("{}", solution.multiply1("123", "456"));
//
//    log.info("{}", solution.reverseWords("hello   world !"));
//    log.info("{}", solution.reverseWords1("hello   world !"));
//
//    log.info("{}", solution.twoSum(new int[]{1, 2, 3}, 4));
//
//    log.info("{}", solution.threeSum(new int[]{1, 2, 3, -3, -2, -5}));
//    log.info("{}", solution.threeSum(new int[]{-1, -1, -1, 1}));
//    log.info("{}", solution.threeSum(new int[]{-1, 0, 1, 2, -1, -4}));
//
//    int[][] grid = Generator.randomGrid(10, 12);
//    log.info("{}", solution.maxAreaOfIsland(grid));

//    log.info("{}", solution.search(new int[]{4, 5, 6, 7, 8, 0, 1, 2}, 2));
//    log.info("{}", solution.search(new int[]{1}, 1));
//    log.info("{}", solution.search(new int[]{1, 3}, 2));

//    log.info("{}", solution.search1(new int[]{}, 1));
//    log.info("{}", solution.search1(new int[]{1}, 1));
//    log.info("{}", solution.search1(new int[]{1, 3}, 2));
//    log.info("{}", solution.search1(new int[]{4, 5, 6, 7, 0, 1, 2}, 0));

//    log.info("{}", solution.findMaxConsecutiveOnes(new int[]{1, 0, 1, 1, 0, 1}));

//    log.info("{}", solution.findKthLargest(new int[]{4, 1, 6, 3, 7, 5, 8, 9, 2}, 3));
//    log.info("{}", solution.findKthLargest(new int[]{1}, 1));
//    log.info("{}", solution.findKthLargest(new int[]{1, 2}, 2));

//    log.info("{}", solution.findKthLargest1(new int[]{4, 1, 6, 3, 7, 5, 8, 9, 2}, 4));
//    log.info("{}", solution.findKthLargest1(new int[]{1}, 1));
//    log.info("{}", solution.findKthLargest1(new int[]{1, 2}, 2));
    log.info("{}", solution.findKthLargest1(new int[]{3, 3, 3, 3, 4, 3, 3, 3, 3}, 5));

//    int[] nums = new int[]{4, 1, 6, 3, 7, 5, 8, 9, 2};
//    solution.quickSort1(nums);
//    log.info("{}", nums);

  }

  // 无重复最长子字符串

  public int lengthOfLongestSubstring(String s) {
    if (s == null || s.length() == 0) {
      return 0;
    }

    HashMap<Character, Integer> pos = new HashMap<>(16);

    int len = 1, i = 0, j = 0, last = i;
    for (; i < s.length(); i++) {

      for (; j < s.length(); j++) {

        Character ch = s.charAt(j);
        if (pos.containsKey(ch)) {
          len = Math.max(len, j - i + 1);
          i = pos.get(ch);

//          for (int k = last; k <= i; k++) {
//            pos.remove(s.charAt(k));
//          }
          pos.put(ch, j);
          last = i + 1;
          j++;
          break;
        } else {
          pos.put(ch, j);
        }

      }
    }

    return Math.max(len, pos.size());
  }

  public int lengthOfLongestSubstring1(String s) {
    if (s == null || s.length() == 0) {
      return 0;
    }

    int n = s.length();
    Set<Character> set = new HashSet<>();
    int max = 0, i = 0, j = 0;
    while (i < n && j < n) {
      if (!set.contains(s.charAt(j))) {
        set.add(s.charAt(j++));
        max = Math.max(max, j - i);
      } else {
        set.remove(s.charAt(i++));
      }
    }
    return max;
  }

  public int lengthOfLongestSubstring2(String s) {
    if (s == null || s.length() == 0) {
      return 0;
    }

    int n = s.length(), max = 0;
    Map<Character, Integer> map = new HashMap<>(16);
    for (int j = 0, i = 0; j < n; j++) {
      if (map.containsKey(s.charAt(j))) {
        i = Math.max(map.get(s.charAt(j)), i);
      }
      max = Math.max(max, j - i + 1);
      map.put(s.charAt(j), j + 1);
    }
    return max;
  }

  // 字符串相乘

  public String multiply(String num1, String num2) {

    int l1 = num1.length(), l2 = num2.length();
    if (l1 == 0 || l2 == 0) {
      return "";
    }

    char[] res = new char[l1 + l2];
    for (int i = 0; i < l1 + l2; i++) {
      res[i] = '0';
    }

    for (int i = l1 - 1; i >= 0; i--) {
      int add = 0;
      for (int j = l2 - 1; j >= 0; j--) {
        int mul = (num1.charAt(i) - '0') * (num2.charAt(j) - '0');
        int sum = res[i + j + 1] + add + mul % 10 - '0';
        res[i + j + 1] = (char) ('0' + sum % 10);
        add = mul / 10 + sum / 10;
      }
      res[i] += add;
    }
    for (int i = 0; i < l1 + l2; i++) {
      if (res[i] != '0') {
        return String.valueOf(res).substring(i);
      }
    }
    return "0";
  }

  public String multiply1(String num1, String num2) {
    char[] value = new char[num1.length() + num2.length()];
    for (int i = num1.length() - 1; i >= 0; i--) {
      for (int j = num2.length() - 1; j >= 0; j--) {
        value[i + j + 1] += (num1.charAt(i) - '0') * (num2.charAt(j) - '0');
      }
    }
    // 处理进位
    int carry = 0;
    for (int i = value.length - 1; i >= 0; i--) {
      value[i] += carry;
      carry = value[i] / 10;
      value[i] %= 10;
    }
    // 处理前端的 0
    int beginIndex = 0;
    while (beginIndex < value.length - 1 && value[beginIndex] == 0) {
      beginIndex++;
    }
    for (int i = beginIndex; i < value.length; i++) {
      value[i] += '0';
    }
    return new String(value, beginIndex, value.length - beginIndex);
  }

  // 翻转字符串里的单词

  public String reverseWords(String s) {
    s = s.trim();
    if (s.length() == 0 || (!s.contains(" "))) {
      return s;
    }

    StringBuilder stringBuilder = new StringBuilder();
    String[] strings = s.split("\\s+");
    for (int i = strings.length - 1; i >= 0; i--) {
      stringBuilder.append(strings[i]).append(" ");
    }
    return stringBuilder.toString().trim();
  }

  public String reverseWords1(String s) {
    s = s.trim();
    if (s.length() == 0 || (!s.contains(" "))) {
      return s;
    }

    String[] strings = spiltString(s);
    StringBuffer sb = new StringBuffer();
    for (int i = strings.length - 1; i >= 0; i--) {
      sb.append(strings[i]).append(" ");
    }

    return sb.toString().trim();
  }

  public String[] spiltString(String str) {
    List<String> list = new LinkedList<>();
    char[] s = str.toCharArray();
    String tmpStr = "";
    for (int i = 0; i < s.length; i++) {
      if (s[i] != ' ') {
        tmpStr += s[i];
      } else {
        list.add(tmpStr);
        tmpStr = "";
        while (s[i] == ' ') {
          i++;
          if (i >= s.length) {
            break;
          }
        }
        i--;
      }
    }
    list.add(tmpStr);
    return list.toArray(new String[list.size()]);
  }

  // 两数之和

  public int[] twoSum(int[] nums, int target) {
    int[] res = new int[2];

    if (nums == null || nums.length < 2) {
      return res;
    }

    Map<Integer, Integer> map = new HashMap<>();

    for (int i = 0; i < nums.length; i++) {
      if (map.containsKey(target - nums[i])) {
        res[0] = i;
        res[1] = map.get(target - nums[i]);
        break;
      }
      map.put(nums[i], i);
    }

    return res;

  }

  // 三数之和

  public List<List<Integer>> threeSum(int[] nums) {
    Arrays.sort(nums);

    List<List<Integer>> result = new ArrayList<>();
    for (int i = 0; i < nums.length - 1; i++) {
      if (i > 0 && nums[i] == nums[i - 1]) {
        continue;
      }
      int j = i + 1;
      int k = nums.length - 1;
      while (j < k) {
        if (nums[j] + nums[k] + nums[i] > 0) {
          k = move(nums, k, -1);
        } else if (nums[j] + nums[k] + nums[i] < 0) {
          j = move(nums, j, 1);
        } else {
          result.add(Arrays.asList(nums[j], nums[k], nums[i]));
          j = move(nums, j, 1);
          k = move(nums, k, -1);
        }
      }
    }
    return result;

  }

  private int move(int[] nums, int offset, int step) {
    int newOffset = offset + step;
    while (newOffset > 0 && newOffset < nums.length - 1) {
      if (nums[offset] != nums[newOffset]) {
        break;
      }
      newOffset += step;
    }

    return newOffset;
  }

  // 岛屿的最大面积

  public int maxAreaOfIsland(int[][] grid) {
    int max = 0;
    for (int i = 0; i < grid.length; i++) {
      for (int j = 0; j < grid[0].length; j++) {
        if (grid[i][j] == 1) {
          int num = deepSearch(grid, i, j);
          max = Math.max(num, max);
        }
      }
    }
    return max;
  }

  public int deepSearch(int[][] grid, int i, int j) {
    if (i >= 0 && i < grid.length && j >= 0 && j < grid[0].length && grid[i][j] == 1) {
      grid[i][j] = 0;
      return 1
          + deepSearch(grid, i - 1, j)
          + deepSearch(grid, i + 1, j)
          + deepSearch(grid, i, j - 1)
          + deepSearch(grid, i, j + 1);
    } else {
      return 0;
    }
  }

  // 搜索旋转排序数组

  public int search(int[] nums, int target) {
    if (nums.length == 0) {
      return -1;
    }

    int k = 0;
    while (k < nums.length - 1 && nums[k] < nums[k + 1]) {
      k++;
    }

    if (nums[0] <= target && nums[k] >= target) {
      return binarySearch(nums, 0, k, target);
    } else if (k < nums.length - 1 && nums[k + 1] <= target && nums[nums.length - 1] >= target) {
      return binarySearch(nums, k + 1, nums.length - 1, target);
    }
    return -1;
  }

  public int binarySearch(int[] nums, int i, int j, int target) {
    if (i > j) {
      return -1;
    }

    if (target == nums[i]) {
      return i;
    } else if (target == nums[j]) {
      return j;
    } else if (target > nums[i] && target < nums[j]) {

      int k = (i + j) / 2;
      if (k == i) {
        return -1;
      }
      if (target == nums[k]) {
        return k;
      } else if (target < nums[k]) {
        return binarySearch(nums, i, k, target);
      } else {
        return binarySearch(nums, k, j, target);
      }
    }

    return -1;
  }

  public int search1(int[] nums, int target) {
    if (nums.length == 0) {
      return -1;
    }

    int i = 0, j = nums.length - 1;
    while (i <= j) {
      int mid = (i + j) / 2;
      if (nums[mid] == target) {
        return mid;
      }
      if (nums[mid] >= nums[i]) {
        if (nums[i] <= target && target < nums[mid]) {
          j = mid - 1;
        } else {
          i = mid + 1;
        }
      } else {
        if (nums[mid] < target && target <= nums[j]) {
          i = mid + 1;
        } else {
          j = mid == 0 ? mid : mid - 1;
        }
      }
    }
    return -1;
  }

  // 最大连续1的个数

  public int findMaxConsecutiveOnes(int[] nums) {
    int max = 0;
    int tmp = 0;

    for (int i = 0; i < nums.length; i++) {
      if (nums[i] == 1) {
        tmp++;
      } else {
        max = Math.max(max, tmp);
        tmp = 0;
      }
    }

    max = Math.max(max, tmp);

    return max;
  }

  // 最大增长子序列

  public int findLengthOfLCIS(int[] nums) {
    if (nums.length == 0) {
      return 0;
    }

    int max = 0;
    int tmp = 1;

    for (int i = 1; i < nums.length; i++) {
      if (nums[i] > nums[i - 1]) {
        tmp++;
      } else {
        max = Math.max(max, tmp);
        tmp = 1;
      }
    }

    max = Math.max(max, tmp);
    return max;
  }

  // 数组中的第K个最大元素

  public int findKthLargest(int[] nums, int k) {

    for (int i = 0; i < nums.length; i++) {
      if (i == k) {
        log.info("{}", nums);
        return nums[k - 1];
      }

      for (int j = i + 1; j < nums.length; j++) {

        log.info("{}, i:{}, j:{}", nums, i, j);
        if (nums[i] < nums[j]) {
          int temp = nums[i];
          nums[i] = nums[j];
          nums[j] = temp;
        }
      }
    }

    if (nums.length == k) {
      return nums[nums.length - 1];
    }

    return -1;
  }

  public int findKthLargest1(int[] nums, int k) {

    quickSort(nums, 0, nums.length - 1, k);

    return nums[k - 1];
  }

  private void quickSort(int[] nums, int i, int j, int k) {

    if (i >= j) {
      return;
    }

    int mid = findMid1(nums, i, j);
    if (mid > k - 1) {
      quickSort(nums, i, mid - 1, k);
    } else if (mid < k - 1) {
      quickSort(nums, mid + 1, j, k - mid);
    }

  }

  // 快速排序 降序

  public void quickSort(int[] nums) {

    deepSort(nums, 0, nums.length - 1);

  }

  private void deepSort(int[] nums, int i, int j) {

    if (i >= j) {
      return;
    }

    int mid = findMid(nums, i, j);
    deepSort(nums, i, mid - 1);
    deepSort(nums, mid + 1, j);

  }

  private int findMid(int[] nums, int i, int j) {
    int k = j;
    while (i < j) {
      while (i < j && nums[i] <= nums[k]) {
        i++;
      }
      while (i < j && nums[j] >= nums[k]) {
        j--;
      }

      int tmp = nums[i];
      nums[i] = nums[j];
      nums[j] = tmp;
    }

    int tmp = nums[i];
    nums[i] = nums[k];
    nums[k] = tmp;

    return i;

  }

  // 快速排序 升序

  public void quickSort1(int[] nums) {

    deepSort1(nums, 0, nums.length - 1);

  }

  private void deepSort1(int[] nums, int i, int j) {

    if (i >= j) {
      return;
    }

    int mid = findMid1(nums, i, j);
    deepSort1(nums, i, mid - 1);
    deepSort1(nums, mid + 1, j);

  }

  private int findMid1(int[] nums, int i, int j) {
    int r = (int) (j - (j - i) * Math.random());
    int tmp = nums[r];
    nums[r] = nums[j];
    nums[j] = tmp;

    int k = j;
    while (i < j) {
      while (i < j && nums[i] >= nums[k]) {
        i++;
      }
      while (i < j && nums[j] < nums[k]) {
        j--;
      }

      tmp = nums[i];
      nums[i] = nums[j];
      nums[j] = tmp;
    }

    tmp = nums[i];
    nums[i] = nums[k];
    nums[k] = tmp;

    return i;

  }

  // 快速排序 优化

  public void quickSort2(int[] nums) {

    // 采用随机选择基准值的方法，或第一个点、中间点、最后一个点，选取中间值作为基准

    throw new NotImplementedException();
  }


}
