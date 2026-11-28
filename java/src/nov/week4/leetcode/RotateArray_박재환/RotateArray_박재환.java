package nov.week4.leetcode.RotateArray_박재환;

import java.util.Arrays;

public class RotateArray_박재환 {
    public static void main(String[] args) {
        Solution solution = new Solution();
        solution.rotate(new int[]{1, 2, 3, 4, 5, 6, 7}, 3);
        solution.rotate(new int[]{-1, -100, 3, 99}, 2);
    }
}

class Solution {
    public void rotate(int[] nums, int k) {
        int n = nums.length;
        int[] temp = new int[n];
        for(int i = 0; i < n; i++) {
            temp[(i + k) % n] = nums[i];
        }
        for (int i = 0; i < n; i++) {
            nums[i] = temp[i];
        }
        System.out.println(Arrays.toString(nums));
    }
}
