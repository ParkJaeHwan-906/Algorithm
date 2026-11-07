package nov.week1.programmers.정수삼각형_박재환;

import java.util.*;

public class 정수삼각형_박재환 {
    public static void main(String[] args) {
        Solution solution = new Solution();
        int[][] triangle = {
                {7},
                {3, 8},
                {8, 1, 0},
                {2, 7, 4, 4},
                {4, 5, 2, 6, 5}
        };
        System.out.println(solution.solution(triangle));
    }
}

class Solution {
    int[][] dp;
    public int solution(int[][] triangle) {
        dp = new int[triangle.length][];
        for (int i = 0; i < triangle.length; i++) {
            dp[i] = new int[triangle[i].length];
            Arrays.fill(dp[i], -1);
        }

        return research(0, 0, triangle);
    }

    int research(int depth, int id, int[][] triangle) {
        if(depth == triangle.length - 1)
            return dp[depth][id] = triangle[depth][id];
        if(dp[depth][id] != -1)
            return dp[depth][id];
        int max = research(depth + 1, id, triangle);
        if(id + 1 < triangle[depth + 1].length)
            max = Math.max(max, research(depth + 1, id + 1, triangle));

        return dp[depth][id] = triangle[depth][id] + max;
    }
}
