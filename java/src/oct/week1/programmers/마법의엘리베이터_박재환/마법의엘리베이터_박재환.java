package oct.week1.programmers.마법의엘리베이터_박재환;

import java.util.*;

public class 마법의엘리베이터_박재환 {
    public static void main(String[] args) {
        int storey = 16;
        Solution sol = new Solution();
        System.out.print(sol.solution(storey));
    }
}

class Solution {
    static final int INF = Integer.MAX_VALUE / 2;
    public int solution(int storey) {
        String str = new StringBuilder(String.valueOf(storey)).reverse().toString();
        int n = str.length();
        int[][] dp = new int[n + 1][2];
        for (int i = 0; i <= n; i++) {
            Arrays.fill(dp[i], INF);
        }
        dp[0][0] = 0;
        for (int i = 0; i < n; i++) {
            int num = str.charAt(i) - '0';
            dp[i + 1][0] = Math.min(
                    dp[i + 1][0],
                    dp[i][0] + num
            );
            dp[i + 1][1] = Math.min(
                    dp[i + 1][1],
                    dp[i][0] + (10 - num)
            );
            int cur = num + 1;
            dp[i + 1][0] = Math.min(
                    dp[i + 1][0],
                    dp[i][1] + cur
            );
            dp[i + 1][1] = Math.min(
                    dp[i + 1][1],
                    dp[i][1] + (10 - cur)
            );
        }
        return Math.min(
                dp[n][0],
                dp[n][1] + 1
        );
    }
}