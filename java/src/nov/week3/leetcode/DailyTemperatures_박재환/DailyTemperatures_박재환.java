package nov.week3.leetcode.DailyTemperatures_박재환;

import java.util.Arrays;

public class DailyTemperatures_박재환 {
    public static void main(String[] args) {
        Solution solution = new Solution();

        int[] temperatures1 = {73, 74, 75, 71, 69, 72, 76, 73};
        System.out.println(Arrays.toString(solution.dailyTemperatures(temperatures1))); // [1, 1, 4, 2, 1, 1, 0, 0]

        int[] temperatures2 = {30, 40, 50, 60};
        System.out.println(Arrays.toString(solution.dailyTemperatures(temperatures2))); // [1, 1, 1, 0]

        int[] temperatures3 = {30, 60, 90};
        System.out.println(Arrays.toString(solution.dailyTemperatures(temperatures3))); // [1, 1, 0]
    }
}

class Solution {
    public int[] dailyTemperatures(int[] temperatures) {
        int n = temperatures.length;
        int[] count = new int[n];
        for (int i = n - 2; i >= 0; i--) {
            int j = i + 1;
            while (temperatures[j] <= temperatures[i]) {
                if (count[j] == 0)
                    break;
                j += count[j];
            }

            if (temperatures[j] > temperatures[i])
                count[i] = j - i;
        }
        return count;
    }
}
