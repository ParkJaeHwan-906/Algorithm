package oct.week2.programmers.경주로건설_박재환;

import java.util.*;

public class 경주로건설_박재환 {
    public static void main(String[] args) {
        int[][][] boards = {
                {
                        {0,0,0},
                        {0,0,0},
                        {0,0,0}
                },
                {
                        {0,0,0,0,0,0,0,1},
                        {0,0,0,0,0,0,0,0},
                        {0,0,0,0,0,1,0,0},
                        {0,0,0,0,1,0,0,0},
                        {0,0,0,1,0,0,0,1},
                        {0,0,1,0,0,0,1,0},
                        {0,1,0,0,0,1,0,0},
                        {1,0,0,0,0,0,0,0}
                },
                {
                        {0,0,1,0},
                        {0,0,0,0},
                        {0,1,0,1},
                        {1,0,0,0}
                },
                {
                        {0,0,0,0,0,0},
                        {0,1,1,1,1,0},
                        {0,0,1,0,0,0},
                        {1,0,0,1,0,1},
                        {0,1,0,0,0,1},
                        {0,0,0,0,0,0}
                }
        };

        int[] expected = {900, 3800, 2100, 3200};

        for(int i = 0; i < boards.length; i++) {
            Solution solution = new Solution();
            int result = solution.solution(boards[i]);
            System.out.printf("테스트 %d | 예상: %d | 결과: %d | %s%n",
                    i + 1, expected[i], result, expected[i] == result ? "PASS" : "FAIL");
        }
    }
}

class Solution {

    static final int INF = Integer.MAX_VALUE / 2;

    static final int[] dx = {0, 1, 0, -1};
    static final int[] dy = {1, 0, -1, 0};

    int n;
    int[][][] costs;
    public int solution(int[][] board) {
        set(board);
        Arrays.fill(costs[0][0], 0);            // 시작 위치 - 초기 비용 0
        return findMinimumRoute(board);
    }

    void set(int[][] board) {
        this.n = board.length;
        this.costs = new int[n][n][4];
        for(int i = 0; i < n; i++)
            for(int j = 0; j < n; j++)
                Arrays.fill(costs[i][j], INF);
    }

    int findMinimumRoute(int[][] board) {
        PriorityQueue<int[]> pq = new PriorityQueue<>((a, b) -> {           // [x, y, dir, cost]
            return Integer.compare(a[3], b[3]);
        });
        for(int dir = 0; dir < 4; dir++)
            pq.offer(new int[] {0, 0, dir, 0});
        while(!pq.isEmpty()) {
            int[] cur = pq.poll();
            int x = cur[0];
            int y = cur[1];
            int prevDir = cur[2];
            int cost = cur[3];
            if(costs[x][y][prevDir] < cost)
                continue;
            if(x == n - 1 && y == n - 1) {
                return cost;
            }
            for(int dir = 0; dir < 4; dir++) {
                int nx = x + dx[dir];
                int ny = y + dy[dir];
                if(isNotBoard(nx, ny) || board[nx][ny] == 1)
                    continue;
                int nextCost = cost + (prevDir == dir ? 100 : (500 + 100));
                if(costs[nx][ny][dir] > nextCost) {
                    costs[nx][ny][dir] = nextCost;
                    pq.offer(new int[] {nx, ny, dir, costs[nx][ny][dir]});
                }
            }
        }
        return -1;
    }

    boolean isNotBoard(int x, int y) {
        return x < 0 || y < 0 || x >= n || y >= n;
    }
}