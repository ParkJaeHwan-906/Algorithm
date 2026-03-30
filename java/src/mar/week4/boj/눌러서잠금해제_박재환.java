package mar.week4.boj;

import java.util.*;
import java.io.*;

/**
 * 버튼은 1 ~ B 까지 있다. ( B : 1 ~ 11 )
 * 비밀번호는 최소 한 개 이상의 버튼 조합들로 이루어져 있다.
 * 어떤 버튼을 한 조합에서 사용하면, 다른 조합에서는 사용할 수 없다.
 */

public class 눌러서잠금해제_박재환 {
    static BufferedReader br;
    static StringBuilder sb;
    public static void main(String[] args) throws IOException {
        br = new BufferedReader(new InputStreamReader(System.in));
        sb = new StringBuilder();
        init();
        br.close();
        System.out.println(sb);
    }
    static void init() throws IOException {
        int n = Integer.parseInt(br.readLine().trim());
        findAllPasswordCombination();
        while(n-- > 0) {
            int b = Integer.parseInt(br.readLine().trim());

            long ans = (dp[b][b] * 2 - 1);

            sb.append(ans).append('\n');
        }
    }
    static long[][] dp;
    static long[][] combi;
    static void findAllPasswordCombination() {
        combi = new long[12][12];
        dp = new long[12][12];
        dp[1][1] = 1;
        dp[2][1] = 2;
        dp[2][2] = 3;

        for(int i=1; i<12; i++) {
            combi[i][0] = 1;
            combi[i][1] = i;
            for(int j=2; j<i+1; j++) {
                combi[i][j] = combi[i][j-1] * (i-j+1) / j;
            }
        }

        for(int i=3; i<12; i++) {
            for(int j=1; j<i; j++) {
                dp[i][j] = combi[i][j] * dp[j][j];
                dp[i][i] += dp[i][j];
            }
            dp[i][i] += 1;
        }
    }
}
