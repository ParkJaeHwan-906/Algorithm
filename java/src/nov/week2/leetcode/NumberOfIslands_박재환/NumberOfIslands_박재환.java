package nov.week2.leetcode.NumberOfIslands_박재환;

import java.util.*;

public class NumberOfIslands_박재환 {
    public static void main(String[] args) {
        Solution solution = new Solution();

        char[][] grid1 = {
            {'1', '1', '1', '1', '0'},
            {'1', '1', '0', '1', '0'},
            {'1', '1', '0', '0', '0'},
            {'0', '0', '0', '0', '0'}
        };
        System.out.println(solution.numIslands(grid1)); // 1

        char[][] grid2 = {
            {'1', '1', '0', '0', '0'},
            {'1', '1', '0', '0', '0'},
            {'0', '0', '1', '0', '0'},
            {'0', '0', '0', '1', '1'}
        };
        System.out.println(solution.numIslands(grid2)); // 3
    }
}


class Solution {

    static final int[] dx = {0, 1, 0, -1};
    static final int[] dy = {1, 0, -1, 0};

    int n, m;
    public int numIslands(char[][] grid) {
        set(grid);
        boolean[][] visited = new boolean[n][m];
        int count = 0;
        for(int x = 0; x < n; x++) {
            for(int y = 0; y < m; y++) {
                if(grid[x][y] == '1' && !visited[x][y]) {
                    count++;
                    checkAdjLand(x, y, grid, visited);
                }
            }
        }
        return count;
    }

    void checkAdjLand(int x, int y, char[][] grid, boolean[][] visited) {
        Queue<int[]> q = new ArrayDeque<>();

        q.offer(new int[] {x, y});
        visited[x][y] = true;

        while(!q.isEmpty()) {
            int[] cur = q.poll();
            for(int dir = 0; dir < 4; dir++) {
                int nx = cur[0] + dx[dir];
                int ny = cur[1] + dy[dir];
                if(nx < 0 || ny < 0 || nx >= n || ny >= m)
                    continue;
                if(visited[nx][ny] || grid[nx][ny] == '0')
                    continue;
                visited[nx][ny] = true;
                q.offer(new int[] {nx, ny});
            }
        }
    }

    void set(char[][] grid) {
        n = grid.length;
        m = grid[0].length;
    }
}
