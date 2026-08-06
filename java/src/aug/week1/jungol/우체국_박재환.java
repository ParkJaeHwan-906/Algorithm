package aug.week1.jungol;

import java.util.*;
import java.io.*;

public class 우체국_박재환 {
    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        init(br);
        br.close();
    }

    static int v, p;
    static int[] homes;
    static int[] pre;
    static int[][] cost;
    static void init(BufferedReader br) throws IOException {
        StringTokenizer st = new StringTokenizer(br.readLine().trim());
        v = Integer.parseInt(st.nextToken());
        p = Integer.parseInt(st.nextToken());

        homes = new int[v + 1];
        pre = new int[v + 1];
        st = new StringTokenizer(br.readLine().trim());
        for(int i = 1; i <= v; i++) {
            homes[i] = Integer.parseInt(st.nextToken());
            pre[i] = pre[i - 1] + homes[i];
        }
        System.out.println(solution());
    }

    static final int INF = Integer.MAX_VALUE / 2;
    static int solution() {
        buildCost();

        int[][] dp = new int[p + 1][v + 1];     // dp[k][j] : 앞의 j개 마을을 우체국 k개로 담당한 최소 거리 합
        for(int[] row : dp) Arrays.fill(row, INF);
        dp[0][0] = 0;

        for(int k = 1; k <= p; k++) {                   // 사용할 우체국 개수
            for(int j = k; j <= v; j++) {               // 앞에서 부터 담당할 마을 개수
                for(int i = k - 1; i < j; i++) {        // 마지막 우체국의 담당 구간
                    if(dp[k - 1][i] == INF) continue;
                    dp[k][j] = Math.min(dp[k][j], dp[k - 1][i] + cost[i + 1][j]);
                }
            }
        }
        return dp[p][v];
    }

    // 구간 i~j를 우체국 하나가 담당하면 중앙값 마을에 짓는 것이 최소 → 누적합으로 O(1) 계산
    static void buildCost() {
        cost = new int[v + 1][v + 1];
        for(int i = 1; i <= v; i++) {
            for(int j = i; j <= v; j++) {
                int mid = (i + j) / 2;
                int left = homes[mid] * (mid - i + 1) - (pre[mid] - pre[i - 1]);        // 왼쪽에 있는 마을별로 우체국 까지 거리
                int right = (pre[j] - pre[mid]) - homes[mid] * (j - mid);               // 오른쪽에 있는 마을별로 우체국 까지 거리
                cost[i][j] = left + right;
            }
        }
    }
}
