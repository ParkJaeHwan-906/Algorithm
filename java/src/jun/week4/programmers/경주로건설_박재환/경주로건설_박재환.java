package jun.week4.programmers.경주로건설_박재환;

import java.util.*;

/**
 * AI 사용 여부 O
 * => BFS 에서 초기 값 설정을 visited[0][1][0] = visited[1][0][1] = 100 으로 설정했었음
 *      => 해당 위치가 벽인 경우를 필터링 하지 못해서 정답 틀림
 *          => 초기 위치 visited[0][0][0] = visited[0][0][1] = 0 으로 설정해서 BFS 실행
 */
public class 경주로건설_박재환 {
    public static void main(String[] args) {
        int[][] board = {
                {0, 0, 0},
                {0, 0, 0},
                {0, 0, 0}
        };

        Solution sol = new Solution();
        System.out.println(sol.solution(board));
    }
}

class Solution {
    /**
     * n x n
     * - 0 : 빈 칸
     * - 1 : 벽
     *
     * [입력] : 0-based
     *
     * [경주로 건설]
     * (0, 0) -> (n - 1, n - 1)
     * 중간에 끊기지 않도록 경주로를 건설한다.
     * {상, 하, 좌, 우} 인접한 두 빈칸을 연결하여 건설할 수 있다.
     *
     * - 인접한 두 빈 칸: 직선도로 (100원)
     * - 두 직선 도로가 서로 직각으로 만나는 지점: 코너 (500원)
     *
     * 최소 비용으로 경주로 건설
     */
    final int INF = Integer.MAX_VALUE;

    int n;
    int[][] board;
    public int solution(int[][] board) {
        set(board);
        return findMinCostTrack();
    }

    void set(int[][] board) {
        this.n = board.length;
        this.board = board;
    }

    int[] dx = {0, 1, 0, -1};
    int[] dy = {1, 0, -1, 0};
    int findMinCostTrack() {
        Queue<int[]> q = new ArrayDeque<>();
        int[][][] visited = new int[n][n][4];       // 방향에 따라서 구분
        for(int x = 0; x < n; x++) {
            for(int y = 0; y < n; y++) Arrays.fill(visited[x][y], INF);
        }

        // 초기 위치 설정
        // 초기에는 오른쪽, 아래쪽 으로만 확장 가능
        visited[0][0][0] = visited[0][0][1] = 0;

        q.offer(new int[] {0, 0, 0, visited[0][0][0]});
        q.offer(new int[] {0, 0, 1, visited[0][0][1]});

        while(!q.isEmpty()) {
            int[] cur = q.poll();
            int x = cur[0];
            int y = cur[1];
            int prevDir = cur[2];
            int cost = cur[3];

            if(visited[x][y][prevDir] < cost) continue;
            if(x == n - 1 && y == n - 1) {
                visited[x][y][prevDir] = Math.min(cost, visited[x][y][prevDir]);
                continue;
            }

            for(int dir = 0; dir < 4; dir++) {
                int nx = x + dx[dir];
                int ny = y + dy[dir];

                if(isNotBoard(nx, ny)) continue;
                if(board[nx][ny] == 1) continue;

                int nextCost = cost + (dir == prevDir ? 100 : 600);

                if(visited[nx][ny][dir] > nextCost) {
                    visited[nx][ny][dir] = nextCost;
                    q.offer(new int[] {nx, ny, dir, visited[nx][ny][dir]});
                }
            }
        }
        return findMinCost(visited[n - 1][n - 1]);
    }

    boolean isNotBoard(int x, int y) {
        return x < 0 || y < 0 || x >= n || y >= n;
    }

    int findMinCost(int[] arr) {
        int min = INF;
        for(int i : arr) min = Math.min(min, i);
        return min;
    }
}
