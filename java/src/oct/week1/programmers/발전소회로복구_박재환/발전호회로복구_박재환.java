package oct.week1.programmers.발전소회로복구_박재환;

import java.util.*;

public class 발전호회로복구_박재환 {
    public static void main(String[] args) {
        int h = 3;
        String[] grid = {".#.##..", ".#..##.", ".......", "##.###.", ".@.#...", "...#..."};
        int[][] panels = {{2, 3, 4}, {2, 5, 6}, {1, 1, 1}, {3, 6, 3}};
        int[][] seqs = {{3, 2}, {1, 2}, {4, 1}, {4, 3}};
        Solution sol = new Solution();
        System.out.print(sol.solution(h, grid, panels, seqs));
    }
}

class Solution {
    static final int INF = Integer.MAX_VALUE / 2;
    static final int[] dx = {1, 0, -1, 0};
    static final int[] dy = {0, 1, 0, -1};

    int FULL;
    int h, n, m, k;
    int[][] panels;
    char[][] board;
    int[] preceding;
    int[][] dist;
    int[][] memo;
    public int solution(int h, String[] grid, int[][] panels, int[][] seqs) {
        set(h, grid, panels, seqs);
        return findBestRoute(0, 0);
    }

    void set(int h, String[] grid, int[][] panels, int[][] seqs) {
        this.h = h;
        n = grid.length;
        m = grid[0].length();
        board = new char[n][m];
        for(int x = 0; x < n; x++) {
            for(int y = 0; y < m; y++) {
                board[x][y] = grid[x].charAt(y);
            }
        }
        k = panels.length;
        this.panels = new int[k][3];
        for (int i = 0; i < k; i++) {
            this.panels[i][0] = panels[i][0] - 1;
            this.panels[i][1] = panels[i][1] - 1;
            this.panels[i][2] = panels[i][2] - 1;
        }
        preceding = new int[k];         // 선행조건
        for(int[] seq : seqs) {
            int a = seq[0] - 1;
            int b = seq[1] - 1;
            preceding[b] |= (1 << a);
        }

        dist = new int[k][k];           //[i][j] : i 에서 j 로의 최단 거리
        for(int startPId = 0; startPId < k; startPId++) {
            findMinDist(startPId);
        }
        FULL = (1 << k) - 1;
        memo = new int[1 << k][k];      // [i][j] : i 상태(방문)일 때, j 위치에 있음
        for(int[] row : memo) {
            Arrays.fill(row, -1);
        }
    }

    void findMinDist(int startPId) {
        int[][][] arr = new int[h][n][m];
        for(int f = 0; f < h; f++) {
            for(int x = 0; x < n; x++) {
                Arrays.fill(arr[f][x], -1);
            }
        }
        int sf = panels[startPId][0];
        int sx = panels[startPId][1];
        int sy = panels[startPId][2];
        Queue<int[]> q = new ArrayDeque<>();
        q.offer(new int[] {sf, sx, sy});
        arr[sf][sx][sy] = 0;
        while(!q.isEmpty()) {
            int[] cur = q.poll();
            int f = cur[0];
            int x = cur[1];
            int y = cur[2];
            for(int dir = 0; dir < 4; dir++) {              // 현재 격자 내 이동
                int nx = x + dx[dir];
                int ny = y + dy[dir];
                if(isNotBoard(nx, ny)) {
                    continue;
                }
                if(board[nx][ny] == '#') {
                    continue;
                }
                if(arr[f][nx][ny] != -1) {
                    continue;
                }
                arr[f][nx][ny] = arr[f][x][y] + 1;
                q.offer(new int[] {f, nx, ny});
            }
            if(board[x][y] == '@') {
                if(f > 0 && arr[f - 1][x][y] == -1) {
                    arr[f - 1][x][y] = arr[f][x][y] + 1;
                    q.offer(new int[] {f - 1, x, y});
                }
                if(f + 1 < h && arr[f + 1][x][y] == -1) {
                    arr[f + 1][x][y] = arr[f][x][y] + 1;
                    q.offer(new int[] {f + 1, x, y});
                }
            }
        }
        for(int next = 0; next < k; next++) {
            int f = panels[next][0];
            int x = panels[next][1];
            int y = panels[next][2];
            dist[startPId][next] = arr[f][x][y] == -1
                    ? INF : arr[f][x][y];
        }
    }

    int findBestRoute(int state, int cur) {
        if(state == FULL) {
            return 0;
        }
        if(memo[state][cur] != -1) {
            return memo[state][cur];
        }
        int result = INF;
        for(int next = 0; next < k; next++) {
            if((state & (1 << next)) != 0) {
                continue;
            }
            if((state & preceding[next]) != preceding[next]) {
                continue;
            }
            int nextState = state | (1 << next);
            int cost = dist[cur][next]
                    + findBestRoute(nextState, next);
            result = Math.min(result, cost);
        }
        return memo[state][cur] = result;
    }

    boolean isNotBoard(int x, int y) {
        return x < 0 || y < 0 || x >= n || y >= m;
    }
}