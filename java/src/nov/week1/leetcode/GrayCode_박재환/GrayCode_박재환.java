package nov.week1.leetcode.GrayCode_박재환;

import java.util.*;

public class GrayCode_박재환 {
    public static void main(String[] args) {
        Solution solution = new Solution();

        System.out.println("Example 1: " + solution.grayCode(2) + " (expected: [0, 1, 3, 2])");
        System.out.println("Example 2: " + solution.grayCode(1) + " (expected: [0, 1])");
    }
}

class Solution {
    public List<Integer> grayCode(int n) {
        List<Integer> result = new ArrayList<>();
        for(int i = 0; i < (1 << n); i++) {
            result.add(i ^ (i >> 1));
        }
        return result;
    }
}
