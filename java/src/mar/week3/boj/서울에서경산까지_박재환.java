package mar.week3.boj;

import java.util.*;
import java.io.*;

public class 서울에서경산까지_박재환 {
    static BufferedReader br;
    public static void main(String[] args) throws IOException {
        br = new BufferedReader(new InputStreamReader(System.in));
        init();
        br.close();
    }
    static class Range {
        int walk;
        int walkV;
        int bike;
        int bikeV;

        Range(int walk, int walkV, int bike, int bikeV) {
            this.walk = walk;
            this.walkV = walkV;
            this.bike = bike;
            this.bikeV = bikeV;
        }
    }
    static StringTokenizer st;
    static int n, k;
    static Range[] ranges;
    static void init() throws IOException {
        st = new StringTokenizer(br.readLine().trim());
        n = Integer.parseInt(st.nextToken());
        k = Integer.parseInt(st.nextToken());

        ranges = new Range[n];
        for(int i=0; i<n; i++) {
            st = new StringTokenizer(br.readLine().trim());
            int walk = Integer.parseInt(st.nextToken());
            int walkV = Integer.parseInt(st.nextToken());
            int bike = Integer.parseInt(st.nextToken());
            int bikeV = Integer.parseInt(st.nextToken());
            Range range = new Range(walk, walkV, bike, bikeV);
            ranges[i] = range;
        }

        System.out.println(solution());
    }
    static final int MIN = -1_000_000_00;
    static long solution() {
        long[][] dp = new long[n+1][k+1];     // [i][j] : i개의 구간을 지나고, j의 시간을 썼을 때의 최대 값
        for(int i=0; i<n+1; i++) Arrays.fill(dp[i], MIN);

        dp[0][0] = 0;
        for(int i=1; i<n+1; i++) {
            /**
             * 이동 방법은 2가디
             * - 도보
             * - 자전거
             */
            Range r = ranges[i-1];

            for(int j=0; j<k+1; j++) {
                if(dp[i-1][j] == MIN) continue;

                if(j + r.walk <= k) dp[i][j + r.walk] = Math.max(dp[i][j + r.walk], dp[i-1][j] + r.walkV);

                if(j + r.bike <= k) dp[i][j + r.bike] = Math.max(dp[i][j + r.bike], dp[i-1][j] + r.bikeV);
            }

        }

        long maxV = MIN;
        for(long v : dp[n]) maxV = Math.max(maxV, v);
        return maxV;
    }
}
