package may.week3.ngv;

import java.util.*;
import java.io.*;

public class 조립라인_박재환 {
    static BufferedReader br;
    public static void main(String[] args) throws IOException {
        br = new BufferedReader(new InputStreamReader(System.in));
        init();
        br.close();
    }
    static StringTokenizer st;
    static int n;
    static int[] beltA;
    static int[] beltB;
    static int[] aToB;
    static int[] bToA;
    static void init() throws IOException {
        n = Integer.parseInt(br.readLine().trim());
        beltA = new int[n + 1];
        beltB = new int[n + 1];
        aToB = new int[n + 1];
        bToA = new int[n + 1];

        for(int i = 1; i < n; i++) {
            st = new StringTokenizer(br.readLine().trim());
            beltA[i] = Integer.parseInt(st.nextToken());
            beltB[i] = Integer.parseInt(st.nextToken());
            aToB[i] = Integer.parseInt(st.nextToken());
            bToA[i] = Integer.parseInt(st.nextToken());
        }

        st = new StringTokenizer(br.readLine().trim());
        beltA[n] = Integer.parseInt(st.nextToken());
        beltB[n] = Integer.parseInt(st.nextToken());

        System.out.println(solution());
    }

    /**
     * 생산라인 A, B가 있다.
     *      - 각각 N개의 작업장이 있다.
     *
     * 각 컨베이어벨트 간 이동이 가능하다.
     */
    static int solution() {
        int[][] dp = new int[2][n + 1];

        dp[0][1] = beltA[1];
        dp[1][1] = beltB[1];
        for(int i = 2; i < n + 1; i++) {
            dp[0][i] = Math.min(
                    dp[0][i - 1] + beltA[i],
                    dp[1][i - 1] + bToA[i - 1] + beltA[i]
            );

            dp[1][i] = Math.min(
                    dp[1][i - 1] + beltB[i],
                    dp[0][i - 1] + aToB[i - 1] + beltB[i]
            );
        }

        return Math.min(dp[0][n], dp[1][n]);
    }
}
