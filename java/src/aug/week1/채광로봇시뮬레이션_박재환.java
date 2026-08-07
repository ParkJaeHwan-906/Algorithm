package aug.week1;

import java.io.*;
import java.util.*;

public class 채광로봇시뮬레이션_박재환 {
    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        init(br);
        br.close();
    }
    static int n, t;
    static long[][] board;
    static long[][][] dp;
    static void init(BufferedReader br) throws IOException {
        StringTokenizer st;

        st = new StringTokenizer(br.readLine().trim());
        n = Integer.parseInt(st.nextToken());
        t = Integer.parseInt(st.nextToken());

        board = new long[n][n];

        for (int x = 0; x < n; x++) {
            st = new StringTokenizer(br.readLine().trim());
            for (int y = 0; y < n; y++) board[x][y] = Long.parseLong(st.nextToken());
        }

        System.out.println(solution());
    }
    static final long NEG_INF = Long.MIN_VALUE / 4;

    // 오른쪽, 아래쪽
    static final int[] dx = {0, 1};
    static final int[] dy = {1, 0};
    static long maxGain;
    static long solution() {
        dp = new long[n][n][2];

        // Java의 long 배열 기본값은 0이므로,
        // 도달하지 못한 상태와 실제 점수 0을 구분하기 위해 초기화해야 한다.
        for (int x = 0; x < n; x++) {
            for (int y = 0; y < n; y++) {
                Arrays.fill(dp[x][y], NEG_INF);
            }
        }

        setNormal();
        setReverse();

        // 시간 역행을 사용하지 않는 경우와 사용한 경우 중 최댓값
        return Math.max(
                dp[n - 1][n - 1][0],
                dp[n - 1][n - 1][1]
        );
    }

    static void setNormal() {
        dp[0][0][0] = board[0][0];

        for (int x = 0; x < n; x++) {
            for (int y = 0; y < n; y++) {
                if (x == 0 && y == 0) continue;

                long prev = NEG_INF;
                if (x > 0) {
                    prev = Math.max(prev, dp[x - 1][y][0]);
                }
                if (y > 0) {
                    prev = Math.max(prev, dp[x][y - 1][0]);
                }
                if (prev != NEG_INF) {
                    dp[x][y][0] = prev + board[x][y];
                }
            }
        }
    }

    static void setReverse() {
        for (int x = 0; x < n; x++) {
            for (int y = 0; y < n; y++) {
                long prev = NEG_INF;
                if (x > 0) {
                    prev = Math.max(prev, dp[x - 1][y][1]);
                }
                if (y > 0) {
                    prev = Math.max(prev, dp[x][y - 1][1]);
                }
                if (prev != NEG_INF) {
                    dp[x][y][1] = prev + board[x][y];
                }
                maxGain = NEG_INF;

                findMaxGain(x, y, 0, board[x][y]);

                if (maxGain != NEG_INF) {
                    dp[x][y][1] = Math.max(dp[x][y][1], dp[x][y][0] + maxGain);
                }
            }
        }
    }

    static void findMaxGain(int x, int y, int moveCount, long value) {
        if (moveCount == t) {
            maxGain = Math.max(maxGain, value);
            return;
        }

        for (int dir = 0; dir < 2; dir++) {
            int nx = x + dx[dir];
            int ny = y + dy[dir];

            if (!isBoard(nx, ny)) continue;

            findMaxGain(nx, ny, moveCount + 1, value + board[nx][ny]);
        }
    }

    static boolean isBoard(int x, int y) { return x >= 0 && x < n && y >= 0 && y < n; }
}