package mar.week4.boj;

import java.util.*;
import java.io.*;

public class 수도배관공사_박재환 {
    static BufferedReader br;
    public static void main(String[] args) throws IOException {
        br = new BufferedReader(new InputStreamReader(System.in));
        init();
        br.close();
    }
    static StringTokenizer st;
    static int d, p;
    static Pipe[] pipes;
    static void init() throws IOException {
        st = new StringTokenizer(br.readLine().trim());
        d = Integer.parseInt(st.nextToken());
        p = Integer.parseInt(st.nextToken());

        pipes = new Pipe[p];
        for(int i = 0; i < p; i++) {
            st = new StringTokenizer(br.readLine().trim());
            int l = Integer.parseInt(st.nextToken());
            int v = Integer.parseInt(st.nextToken());
            pipes[i] = new Pipe(l, v);
        }

        System.out.println(solution());
    }
    static class Pipe {
        int l;      // 길이
        int v;      // 용량

        Pipe(int l, int v) {
            this.l = l;
            this.v = v;
        }
    }
    static long INF = Long.MAX_VALUE;
    static long solution() {
        long[] dp = new long[d + 1];        // [i] : i 의 길이일 때, 만들 수 있는 최대 용량

        dp[0] = INF;
        for(Pipe p : pipes) {
            for(int i = d; i >= p.l; i--) {
                if(dp[i - p.l] == 0) continue;
                dp[i] = Math.max(dp[i], Math.min(dp[i - p.l], p.v));
            }
        }
        return dp[d];
    }
}
