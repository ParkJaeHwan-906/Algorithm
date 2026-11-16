package nov.week2.programmers.완전범죄_박재환;

import java.util.*;

public class 완전범죄_박재환 {
    public static void main(String[] args) {
        Solution solution = new Solution();
        int[][] info1 = {{1, 2}, {2, 3}, {2, 1}};
        int[][] info2 = {{3, 3}, {3, 3}};

        System.out.println(solution.solution(info1, 4, 4));
        System.out.println(solution.solution(info1, 1, 7));
        System.out.println(solution.solution(info2, 7, 1));
        System.out.println(solution.solution(info2, 6, 1));
    }
}

class Solution {
    public int solution(int[][] info, int n, int m) {
        int[][] dp = new int[info.length + 1][m];
        for(int i = 0; i <= info.length; i++)
            Arrays.fill(dp[i], Integer.MAX_VALUE / 2);
        dp[0][0] = 0;

        for(int i = 1; i <= info.length; i++) {
            int a = info[i - 1][0];
            int b = info[i - 1][1];
            for(int j = 0; j < m; j++) {
                dp[i][j] = Math.min(dp[i][j], dp[i - 1][j] + a);
                if(j + b < m) {
                    dp[i][j + b] = Math.min(dp[i][j + b], dp[i - 1][j]);
                }
            }
        }

        int minValue = n;
        for(int i : dp[info.length])
            minValue = Math.min(minValue, i);
        return minValue >= n ? -1 : minValue;
    }
}
