package jun.week5.codetree;

import java.io.*;
import java.util.*;

/*
  문제풀이 시간: 00:16:54
  AI 사용 여부: X
  생각의 흐름: 뭔가 DP 냄새가 난다..
 */
public class 동전프로모션_박서희 {
    static final int INF = 100_000;

    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer st = new StringTokenizer(br.readLine());

        int n = Integer.parseInt(st.nextToken());
        int m = Integer.parseInt(st.nextToken());

        ArrayList<Integer> aList = new ArrayList<>();
        ArrayList<Integer> bList = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            st = new StringTokenizer(br.readLine());
            char type = st.nextToken().charAt(0);
            int value = Integer.parseInt(st.nextToken());
            if (type == 'A') aList.add(value);
            if (type == 'B') bList.add(value);
        }

        int[] dp = new int[m + 1];
        Arrays.fill(dp, INF);
        dp[0] = 0;

        for (int b : bList) {
            for (int i = m; i >= b; i--) {
                dp[i] = Math.min(dp[i], dp[i - b] + 1);
            }
        }

        for (int a : aList) {
            for (int i = a; i <= m; i++) {
                dp[i] = Math.min(dp[i], dp[i - a] + 1);
            }
        }
        System.out.println(dp[m] >= INF ? -1 : dp[m]);
    }
}
