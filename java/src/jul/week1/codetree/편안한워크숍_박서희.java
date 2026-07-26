package jul.week1.codetree;

import java.io.*;
import java.util.*;

/*
  문제풀이 시간: 1시간 넘게
  AI 사용 여부: O
  어려운거 다 모였네.. 이진탐색, dp, dfs
 */
public class 편안한워크숍_박서희 {
    static int N, K;
    static int[][] mountains;
    static int[][] dp;
    static int[] dx = {-1, 1, 0, 0};
    static int[] dy = {0, 0, -1, 1};

    static int answer = -1;

    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer st = new StringTokenizer(br.readLine());

        N = Integer.parseInt(st.nextToken());
        K = Integer.parseInt(st.nextToken());

        mountains = new int[N][N];
        int maxHeight = 0, minHeight = Integer.MAX_VALUE;
        for (int i = 0; i < N; i++) {
            st = new StringTokenizer(br.readLine());
            for (int j = 0; j < N; j++) {
                mountains[i][j] = Integer.parseInt(st.nextToken());
                maxHeight = Math.max(maxHeight, mountains[i][j]);
                minHeight = Math.min(minHeight, mountains[i][j]);
            }
        }

        binarySearch(0, maxHeight - minHeight);
        System.out.println(answer);

    }

    static void binarySearch(int lo, int hi) {
        int mid = lo + (hi - lo) / 2;

        while (mid <= hi) {
            dp = new int[N][N];

            for (int i = 0; i < N; i++) {
                for (int j = 0; j < N; j++) {
                    dp[i][j] = dfs(i, j, mid);
                }
            }

            int temp = 0;
            for (int i = 0; i < N; i++) {
                for (int j = 0; j < N; j++) {
                    temp = Math.max(temp, dp[i][j]);
                }
            }

            if (temp >= K) {
                answer = mid;
                hi = mid - 1;
            } else {
                lo = mid + 1;
            }
            mid = lo + (hi - lo) / 2;
        }
    }

    static int dfs(int i, int j, int mid) {
        if (dp[i][j] != 0) return dp[i][j];
        int max = 0;
        for (int d = 0; d < 4; d++) {
            int nx = i + dx[d];
            int ny = j + dy[d];
            if (!inRange(nx, ny)) continue;
            if (mountains[nx][ny] <= mountains[i][j]) continue;
            if (mountains[nx][ny] - mountains[i][j] > mid) continue;
            if (dp[nx][ny] == 0) dp[nx][ny] = dfs(nx, ny, mid);
            max = Math.max(max, dp[nx][ny]);
        }
        dp[i][j] = max + 1;
        return dp[i][j];
    }


    static boolean inRange(int x, int y) {
        return 0 <= x && x < N && 0 <= y && y < N;
    }
}
