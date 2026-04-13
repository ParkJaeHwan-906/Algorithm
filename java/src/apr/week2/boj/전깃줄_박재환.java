package apr.week2.boj;

import java.util.*;
import java.io.*;

public class 전깃줄_박재환 {
    static BufferedReader br;
    public static void main(String[] args) throws IOException {
        br = new BufferedReader(new InputStreamReader(System.in));
        init();
        br.close();
    }
    static class Wire {
        int from;
        int to;

        Wire(int from, int to) {
            this.from = from;
            this.to = to;
        }
    }
    static StringTokenizer st;
    static Wire[] connections;
    static void init() throws IOException {
        int m = Integer.parseInt(br.readLine().trim());
        connections = new Wire[m];
        for(int i = 0; i < m; i++) {
            st = new StringTokenizer(br.readLine().trim());
            int from = Integer.parseInt(st.nextToken());
            int to = Integer.parseInt(st.nextToken());

            connections[i] = new Wire(from, to);
        }

        int result = solution(m);
        System.out.println(m - result);
    }
    static int solution(int m) {
        Arrays.sort(connections, (a, b) -> Integer.compare(a.from, b.from));

        int[] dp = new int[m];
        Arrays.fill(dp, 1);

        int lis = 1;
        for(int i = 0; i < m; i++) {
            for(int j = 0; j < i; j++) {
                if(connections[j].to < connections[i].to) {
                    dp[i] = Math.max(dp[i], dp[j] + 1);
                }
                lis = Math.max(lis, dp[i]);
            }
        }
        return lis;
    }
}
