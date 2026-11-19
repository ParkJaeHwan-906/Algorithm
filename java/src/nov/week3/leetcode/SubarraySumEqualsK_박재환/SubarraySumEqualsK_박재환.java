package nov.week3.leetcode.SubarraySumEqualsK_박재환;

import java.util.*;

public class SubarraySumEqualsK_박재환 {
    public static void main(String[] args) {
        Solution solution = new Solution();

        int[] nums1 = {1, 1, 1};
        System.out.println(solution.subarraySum(nums1, 2)); // 2

        int[] nums2 = {1, 2, 3};
        System.out.println(solution.subarraySum(nums2, 3)); // 2
    }
}

class Solution {
    public int subarraySum(int[] nums, int k) {
        int n = nums.length;
        int[] accSum = new int[n];
        accSum[0] = nums[0];
        for (int i = 1; i < n; i++)
            accSum[i] = accSum[i - 1] + nums[i];

        Map<Integer, Integer> rightSumCounts = new HashMap<>();
        int count = 0;
        for (int left = n - 1; left >= 0; left--) {
            int newRightSum = accSum[left];
            int previousCount = rightSumCounts.getOrDefault(newRightSum, 0);
            rightSumCounts.put(newRightSum, previousCount + 1);

            int beforeLeft = (left == 0) ? 0 : accSum[left - 1];
            int target = k + beforeLeft;
            count += rightSumCounts.getOrDefault(target, 0);
        }
        return count;
    }
}
