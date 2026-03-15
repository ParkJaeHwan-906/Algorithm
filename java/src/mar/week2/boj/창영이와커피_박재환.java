package mar.week2.boj;

import java.util.*;
import java.io.*;

public class 창영이와커피_박재환 {
    static BufferedReader br;
    public static void main(String[] args) throws IOException {
        br = new BufferedReader(new InputStreamReader(System.in));
        init();
        br.close();
    }
    static StringTokenizer st;
    static int n, k;
    static int[] coffees;
    static void init() throws IOException {
        st = new StringTokenizer(br.readLine().trim());
        n = Integer.parseInt(st.nextToken());
        k = Integer.parseInt(st.nextToken());

        coffees = new int[n];
        st = new StringTokenizer(br.readLine().trim());
        for(int i=0; i<n;) coffees[i++] = Integer.parseInt(st.nextToken());

        /**
         * 정확히 K만큼의 카페인을 섭취하기 위해서 최소 몇 개의 커피를 마셔야하는가
         */
        int result = solution();
        System.out.println(result);
    }
    static final int INF = 107;
    static int solution() {
        int[] dp = new int[k+1];
        Arrays.fill(dp, INF);

        dp[0] = 0;
        for(int caffeine : coffees) {
            // caffeine : 현재 커피에서 얻을 수 있는 카페인
            // 딱 한 번씩만 사용 가능 -> 뒤에서부터 접근
            for(int i=k; i>=caffeine; i--) {
                dp[i] = Math.min(dp[i], dp[i-caffeine]+1);
            }
        }

        return dp[k] == INF ? -1 : dp[k];
    }
}
