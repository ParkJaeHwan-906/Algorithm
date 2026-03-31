package mar.week5.boj;

import java.util.*;
import java.io.*;

public class 계단수_박재환 {
    static BufferedReader br;
    public static void main(String[] args) throws IOException {
        br = new BufferedReader(new InputStreamReader(System.in));
        init();
        br.close();
    }
    static void init() throws IOException {
        int n = Integer.parseInt(br.readLine().trim());
        System.out.println(solution(n));
    }
    static final int MOD = 1_000_000_000;
    static long solution(int n) {
        /**
         * [i][j][mask]
         * i : i 길이
         * j : 숫자 사용 ( 0 ~ 9 )
         * mask : 사용한 숫자
         */
        int[][][] dp = new int[n+1][10][1 << 10];
        /**
         * 길이가 1일 때
         */
        for(int j = 1; j < 10; j++) {
            dp[1][j][1 << j] = 1;
        }
        for(int i = 1; i < n; i++) {
            for (int j = 0; j < 10; j++) {
                for (int mask = 0; mask < (1 << 10); mask++) {
                    if (dp[i][j][mask] == 0) continue;
                    /**
                     * dp[i][j][mask]
                     * i : 길이가 i
                     * j : 마지막으로 사용한 수
                     * mask : 지금까지 사용한 숫자의 집합
                     */
                    if(j - 1 >= 0) {
                        int next = mask | (1 << (j - 1));
                        dp[i + 1][j - 1][next] =
                                (dp[i + 1][j - 1][next] + dp[i][j][mask]) % MOD;
                    }

                    if(j + 1 < 10) {
                        int next = mask | (1 << (j + 1));
                        dp[i + 1][j + 1][next] =
                                (dp[i + 1][j + 1][next] + dp[i][j][mask]) % MOD;
                    }
                }
            }
        }

        int full = (1 << 10) - 1;
        long answer = 0;
        for(int j = 0; j < 10; j++) {
            answer += (dp[n][j][full]) % MOD;
        }

        return answer;
    }
}
