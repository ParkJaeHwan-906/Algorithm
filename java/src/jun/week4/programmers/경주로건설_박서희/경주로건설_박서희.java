package jun.week4.programmers.경주로건설_박서희;

import java.util.*;

/*
  문제풀이 시간: 00:40:27
  AI 사용 여부: O 코너를 돌 때 500만 더했는데 코너+직진도로라 600을 더해야 한다는 것을 몰랐다.
 */
public class 경주로건설_박서희 {
    public static void main(String[] args) {
        int[][] board = {{0, 0, 1, 0}, {0, 0, 0, 0}, {0, 1, 0, 1}, {1, 0, 0, 0}};

        Solution solution = new Solution();
        int answer = solution.solution(board);
        System.out.println(answer);
    }
}

class Solution {
    static final int INF = 50000000;
    static final int[] dx = {0, 1, 0, -1};
    static final int[] dy = {1, 0, -1, 0};

    static int N;
    static int[][] board;
    static int[][][] dp;

    public int solution(int[][] board) {
        this.board = board;
        this.N = board.length;
        dp = new int[N][N][4];

        int answer = INF;
        dijkstra();
        for (int i = 0; i < 4; i++) answer = Math.min(answer, dp[N - 1][N - 1][i]);
        return answer;
    }

    public void dijkstra() {
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                Arrays.fill(dp[i][j], INF);
            }
        }

        PriorityQueue<int[]> pq = new PriorityQueue<>(Comparator.comparingInt(a -> a[3]));
        pq.add(new int[]{0, 0, -1, 0});
        for (int i = 0; i < 4; i++) {
            dp[0][0][i] = 0;
        }

        while (!pq.isEmpty()) {
            int cur[] = pq.poll();
            int x = cur[0], y = cur[1], dir = cur[2], cost = cur[3];


            for (int d = 0; d < 4; d++) {
                int nx = x + dx[d], ny = y + dy[d], ndir = d;

                if (!inRange(nx, ny)) continue;
                if (board[nx][ny] == 1) continue;

                if (dir == ndir || dir == -1) {
                    if (cost + 100 < dp[nx][ny][ndir]) {
                        dp[nx][ny][ndir] = cost + 100;
                        pq.add(new int[]{nx, ny, ndir, cost + 100});
                    }
                } else {
                    if (cost + 500 + 100 < dp[nx][ny][ndir]) {
                        dp[nx][ny][ndir] = cost + 500 + 100;
                        pq.add(new int[]{nx, ny, ndir, cost + 500 + 100});
                    }
                }
            }
        }
    }

    private boolean inRange(int x, int y) {
        return 0 <= x && x < N && 0 <= y && y < N;
    }
}
