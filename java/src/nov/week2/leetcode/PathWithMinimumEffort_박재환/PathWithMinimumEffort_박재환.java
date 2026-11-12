package nov.week2.leetcode.PathWithMinimumEffort_박재환;

import java.util.*;

public class PathWithMinimumEffort_박재환 {
    public static void main(String[] args) {
        int[][] heights1 = {
                {1, 2, 2},
                {3, 8, 2},
                {5, 3, 5}
        };
        int[][] heights2 = {
                {1, 2, 3},
                {3, 8, 4},
                {5, 3, 5}
        };
        int[][] heights3 = {
                {1, 2, 1, 1, 1},
                {1, 2, 1, 2, 1},
                {1, 2, 1, 2, 1},
                {1, 2, 1, 2, 1},
                {1, 1, 1, 2, 1}
        };

        Solution solution = new Solution();
        System.out.println(solution.minimumEffortPath(heights1));
        System.out.println(solution.minimumEffortPath(heights2));
        System.out.println(solution.minimumEffortPath(heights3));
    }
}

class Solution {

    static final int INF = Integer.MAX_VALUE / 2;

    static final int[] dx = {0, 1, 0, -1};
    static final int[] dy = {1, 0, -1, 0};

    int n, m;
    public int minimumEffortPath(int[][] heights) {
        set(heights);

        PriorityQueue<int[]> pq = new PriorityQueue<>((a, b) -> Integer.compare(a[2], b[2]));
        int[][] efforts = new int[n][m];
        for(int i = 0; i < n; i++)
            Arrays.fill(efforts[i], INF);

        efforts[0][0] = 0;
        pq.offer(new int[] {0, 0, 0});
        while(!pq.isEmpty()) {
            int[] cur = pq.poll();
            int x = cur[0];
            int y = cur[1];
            int effort = cur[2];

            if(x == n - 1 && y == m - 1) {
                efforts[x][y] = effort;
                break;
            }

            if(efforts[x][y] < effort)
                continue;

            for(int dir = 0; dir < 4; dir++) {
                int nx = x + dx[dir];
                int ny = y + dy[dir];
                if(isNotBoard(nx, ny))
                    continue;
                int diff = Math.abs(heights[nx][ny] - heights[x][y]);
                int nextEffort = Math.max(effort, diff);
                if (nextEffort < efforts[nx][ny]) {
                    efforts[nx][ny] = nextEffort;
                    pq.offer(new int[] {nx, ny, nextEffort});
                }
            }
        }
        return efforts[n - 1][m - 1];
    }

    boolean isNotBoard(int x, int y) {
        return x < 0 || y < 0 || x >= n || y >= m;
    }

    void set(int[][] heights) {
        n = heights.length;
        m = heights[0].length;
    }
}
