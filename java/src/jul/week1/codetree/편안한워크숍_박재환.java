package jul.week1.codetree;

import java.util.*;
import java.io.*;

/**
 * [풀이 시간]
 * 00:24:17
 * AI 사용 여부 X
 */
public class 편안한워크숍_박재환 {
    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        init(br);
        br.close();
    }
    static int n, k;
    static int[][] board;
    static void init(BufferedReader br) throws IOException {
        StringTokenizer st;

        st = new StringTokenizer(br.readLine().trim());
        n = Integer.parseInt(st.nextToken());
        k = Integer.parseInt(st.nextToken());

        board = new int[n][n];
        for(int x = 0; x < n; x++) {
            st = new StringTokenizer(br.readLine().trim());
            for(int y = 0; y < n; y++) board[x][y] = Integer.parseInt(st.nextToken());
        }

        System.out.println(solution());
    }

    static final int INF = Integer.MAX_VALUE;
    static int[] dx = {0, 1, 0, -1};
    static int[] dy = {1, 0, -1, 0};

    static int solution() {
        int[][][] dp = new int[n][n][k + 1];        // [x][y][현재까지 길이] : 현재 경로의 최대값
        for(int x = 0; x < n; x++) {
            for(int y = 0; y < n; y++) {
                Arrays.fill(dp[x][y], INF);
                dp[x][y][1] = 0;                    // 현재 칸에서 시작하는 경우 아직 높이 차 0
            }
        }

        for(int l = 1; l < k; l++) {
            for(int x = 0; x < n; x++) {
                for(int y = 0; y < n; y++) {
                    if(dp[x][y][l] == INF) continue;        // 아직 연결되지 않음

                    for(int dir = 0; dir < 4; dir++) {
                        int nx = x + dx[dir];
                        int ny = y + dy[dir];
                        if(isNotBoard(nx, ny)) continue;
                        if(board[nx][ny] <= board[x][y]) continue;      // 높이가 낮아지거나 같은 경우

                        // 조건 만족
                        int diff = board[nx][ny] - board[x][y];
                        int maxDiff = Math.max(diff, dp[x][y][l]);      // 현재 경로에서 최대 높이 차
                        dp[nx][ny][l + 1] = Math.min(dp[nx][ny][l + 1], maxDiff);    // 갱신
                    }
                }
            }
        }

        int min = INF;
        for(int x = 0; x < n; x++) {
            for(int y = 0; y < n; y++) min = Math.min(min, dp[x][y][k]);
        }
        return min == INF ? -1 : min;
    }

    static boolean isNotBoard(int x, int y) { return x < 0 || y < 0 || x >= n || y >= n; }
}
