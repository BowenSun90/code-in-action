package com.alex.space.java.interview.dp;

import com.alex.space.java.interview.util.Generator;
import java.util.List;
import lombok.extern.slf4j.Slf4j;

/**
 * @author Alex Created by Alex on 2019/3/19.
 */
@Slf4j
public class Solution {

  public static void main(String[] args) {
    Solution solution = new Solution();

//    int[] array = Generator.randomArray(5, false);
//    log.info("{} - {}", array, solution.maxProfit(array));

//    log.info("{} - {}", array, solution.maxProfit1(array));

//    log.info("{} - {}", array, solution.maxProfit2(array));

//    int[][] grid = Generator.randomGrid(5, 5);

//    log.info("{}", solution.maximalSquare(grid));

//    int[] array2 = Generator.randomArray(8, false, true);
//    log.info("{} - {}", array2, solution.maxSubArray(array2));
//    log.info("{}", solution.maxSubArray(new int[]{-2, 1, -3, 4, -1, 2, 1, -5, 4}));

    List<List<Integer>> triangle = Generator.randomTriangle(1);
    log.info("{} - {}", triangle, solution.minimumTotal(triangle));

  }

  // 买卖股票的最佳时机 I

  public int maxProfit(int[] prices) {

    int min = Integer.MAX_VALUE;
    int res = 0;
    for (int p : prices) {
      if (p < min) {
        min = p;
      } else {
        res = Math.max(res, p - min);
      }
    }
    return res;
  }

  // 买卖股票的最佳时机 II

  public int maxProfit1(int[] prices) {
    int res = 0;
    if (prices.length == 0) {
      return res;
    }
    int low = prices[0];
    int high = prices[0];
    int i = 0, len = prices.length;
    while (i < len - 1) {
      while (i < len - 1 && prices[i] >= prices[i + 1]) {
        i++;
      }
      low = prices[i];
      while (i < len - 1 && prices[i] <= prices[i + 1]) {
        i++;
      }
      high = prices[i];
      res += high - low;
    }

    return res;
  }

  // 买卖股票的最佳时机 III

  public int maxProfit2(int[] prices) {
    if (prices.length == 0) {
      return 0;
    }

    int fstBuy = Integer.MIN_VALUE, fstSell = 0;
    int secBuy = Integer.MIN_VALUE, secSell = 0;
    for (int p : prices) {
      fstBuy = Math.max(fstBuy, -p);
      fstSell = Math.max(fstSell, fstBuy + p);
      secBuy = Math.max(secBuy, fstSell - p);
      secSell = Math.max(secSell, secBuy + p);
    }
    return secSell;

  }

  // 最大正方形

  public int maximalSquare(int[][] matrix) {
    if (matrix == null || matrix.length == 0) {
      return 0;
    }

    int res = 0;
    int[][] dp = new int[matrix.length][matrix[0].length];
    boolean flag = false;
    for (int i = 0; i < matrix.length; i++) {
      dp[i][0] = matrix[i][0];
      if (!flag && matrix[0][i] == 1) {
        flag = true;
      }
    }
    for (int i = 0; i < matrix[0].length; i++) {
      dp[0][i] = matrix[0][i];
      if (!flag && matrix[0][i] == 1) {
        flag = true;
      }
    }
    if (flag) {
      res = 1;
    }

    for (int i = 1; i < matrix.length; i++) {
      for (int j = 1; j < matrix[0].length; j++) {
        if (matrix[i][j] == 1) {
          dp[i][j] = Math.min(Math.min(dp[i][j - 1], dp[i - 1][j]), dp[i - 1][j - 1]) + 1;
        }
        res = Math.max(res, dp[i][j]);
      }
    }

    return res * res;
  }

  // 最大子序和

  public int maxSubArray(int[] nums) {

    int[] dp = new int[nums.length];
    dp[0] = nums[0];
    int res = dp[0];

    for (int i = 1; i < nums.length; i++) {

      dp[i] = Math.max(dp[i - 1] + nums[i], nums[i]);
      res = Math.max(res, dp[i]);
    }

    return res;
  }

  // 三角形最小路径和

  public int minimumTotal(List<List<Integer>> triangle) {
    if (triangle == null || triangle.get(0) == null
        || triangle.get(0).size() == 0) {
      return 0;
    }

    if (triangle.size() == 1) {
      return triangle.get(0).get(0);
    }

    int res = Integer.MAX_VALUE;

    for (int i = 1; i < triangle.size(); i++) {
      for (int j = 0; j < triangle.get(i).size(); j++) {
        int t = triangle.get(i).get(j);
        if (j == 0) {
          triangle.get(i).set(j, triangle.get(i - 1).get(0) + t);
        } else if (j == triangle.get(i).size() - 1) {
          triangle.get(i).set(j, triangle.get(i - 1).get(j - 1) + t);
        } else {
          triangle.get(i).set(j,
              Math.min(triangle.get(i - 1).get(j - 1), triangle.get(i - 1).get(j)) + t);
        }

        if (i == triangle.size() - 1) {
          res = Math.min(res, triangle.get(i).get(j));
        }
      }
    }

    return res;
  }

}
