package apr.week3.boj;

import java.util.*;
import java.io.*;

public class 외판원순회3_박재환 {
    static BufferedReader br;
    public static void main(String[] args) throws IOException {
        br = new BufferedReader(new InputStreamReader(System.in));
        init();
        br.close();
    }
    static class City {
        int x, y;

        City(int x, int y) {
            this.x = x;
            this.y = y;
        }
    }

    static StringTokenizer st;
    static int n;
    static City[] cities;
    static double[][] dists;
    static void init() throws IOException {
        n = Integer.parseInt(br.readLine().trim());
        cities = new City[n];
        for(int i = 0; i < n; i++) {
            st = new StringTokenizer(br.readLine().trim());
            int x = Integer.parseInt(st.nextToken());
            int y = Integer.parseInt(st.nextToken());
            cities[i] = new City(x, y);
        }

        System.out.println(solution());
    }

    static void makeDists() {
        dists = new double[n][n];
        for(int i = 0; i < n; i++) {
            for(int j = i + 1; j < n; j++) {
                long a = Math.abs(cities[i].x - cities[j].x);
                long b = Math.abs(cities[i].y - cities[j].y);
                double dist = Math.sqrt(Math.pow(a, 2) + Math.pow(b, 2));
                dists[i][j] = dist;
                dists[j][i] = dist;
            }
        }
    }

    static double solution() {
        makeDists();

        /**
         * [외판원 순회]
         * - 비트마스킹 사용
         *  - dp[visited][cur]
         *      : visited에 포함된 도시들을 방문했고, 현재 cur 도시에 있을 때 최소 이동 거리
         */
        int size = 1 << n;
        double INF = Double.MAX_VALUE;
        double[][] dp = new double[size][n];

        for(int i = 0; i < size; i++) {
            Arrays.fill(dp[i], INF);
        }

        dp[1][0] = 0;

        for(int visitied = 1; visitied < size; visitied++) {
            for(int cur = 0; cur < n; cur++) {
                if(dp[visitied][cur] == INF) continue;

                for(int next = 0; next < n; next++) {
                    if((visitied & (1 << next)) != 0) continue;

                    int nextVisited = visitied | (1 << next);
                    dp[nextVisited][next] = Math.min(
                            dp[nextVisited][next],
                            dp[visitied][cur] + dists[cur][next]
                    );
                }
            }
        }

        int FULL = size - 1;
        double result = INF;

        for(int last = 1; last < n; last++) {
            result = Math.min(result, dp[FULL][last] + dists[last][0]);
        }

        return result;
    }
}
