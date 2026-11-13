package nov.week2.leetcode.PathWithMinimumEffort_박재환.ShortestPathtoGetAllKeys_박재환;

import java.util.*;

public class ShortestPathtoGetAllKeys_박재환 {
    public static void main(String[] args) {
        String[] grid1 = {"@.a..", "###.#", "b.A.B"};
        String[] grid2 = {"@..aA", "..B#.", "....b"};
        String[] grid3 = {"@Aa"};

        Solution solution = new Solution();
        System.out.println(solution.shortestPathAllKeys(grid1));
        System.out.println(solution.shortestPathAllKeys(grid2));
        System.out.println(solution.shortestPathAllKeys(grid3));
    }
}

class Solution {

    static final int INF = Integer.MAX_VALUE / 2;
    static final int[] dx = {0, 1, 0, -1};
    static final int[] dy = {1, 0, -1, 0};

    static int FULL;

    static int n, m;
    static int sx, sy;
    static char[][] board;
    public int shortestPathAllKeys(String[] grid) {
        set(grid);
        Queue<int[]> q = new ArrayDeque<>();        // [x, y, state]
        int[][][] dp = new int[n][m][FULL + 1];
        for(int x = 0; x < n; x++) {
            for(int y = 0; y < m; y++)
                Arrays.fill(dp[x][y], INF);
        }

        q.offer(new int[] {sx, sy, 0});
        dp[sx][sy][0] = 0;

        while(!q.isEmpty()) {
            int[] cur = q.poll();
            int x = cur[0];
            int y = cur[1];
            int state = cur[2];
            if(state == FULL) {
                return dp[x][y][state];
            }
            for(int dir = 0; dir < 4; dir++) {
                int nx = x + dx[dir];
                int ny = y + dy[dir];
                if(isNotBoard(nx, ny))
                    continue;

                if(board[nx][ny] >= 'a' && board[nx][ny] <= 'z') {
                    int nextState = state | (1 << (board[nx][ny] - 'a'));
                    if(dp[nx][ny][nextState] > dp[x][y][state] + 1) {
                        dp[nx][ny][nextState] = dp[x][y][state] + 1;
                        q.offer(new int[] {nx, ny, nextState});
                    }
                } else if(board[nx][ny] >= 'A' && board[nx][ny] <= 'Z') {
                    if(((1 << board[nx][ny] - 'A') & state) != 0) {     // 열쇠를 가지고 있음, 이동 가능
                        if(dp[nx][ny][state] > dp[x][y][state] + 1) {
                            dp[nx][ny][state] = dp[x][y][state] + 1;
                            q.offer(new int[] {nx, ny, state});
                        }
                    }
                } else if(board[nx][ny] == '.' || board[nx][ny] == '@') {
                    if(dp[nx][ny][state] > dp[x][y][state] + 1) {
                        dp[nx][ny][state] = dp[x][y][state] + 1;
                        q.offer(new int[] {nx, ny, state});
                    }
                }
            }
        }
        return -1;
    }

    boolean isNotBoard(int x, int y) {
        return x < 0 || y < 0 || x >= n || y >= m;
    }

    void set(String[] grid) {
        n = grid.length;
        m = grid[0].length();

        FULL = 0;
        board = new char[n][m];
        for(int x = 0; x < n; x++) {
            for(int y = 0; y < m; y++) {
                board[x][y] = grid[x].charAt(y);
                if(board[x][y] >= 'a' && board[x][y] <= 'z') {
                    FULL |= (1 << (board[x][y] - 'a'));
                } else if(board[x][y] == '@') {
                    sx = x;
                    sy = y;
                }
            }
        }
    }
}
