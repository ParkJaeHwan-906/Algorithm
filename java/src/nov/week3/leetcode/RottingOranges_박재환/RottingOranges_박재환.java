package nov.week3.leetcode.RottingOranges_박재환;

import java.util.*;

public class RottingOranges_박재환 {
    public static void main(String[] args) {
        Solution solution = new Solution();

        int[][] grid1 = {{2, 1, 1}, {1, 1, 0}, {0, 1, 1}};
        System.out.println(solution.orangesRotting(grid1)); // 4

        int[][] grid2 = {{2, 1, 1}, {0, 1, 1}, {1, 0, 1}};
        System.out.println(solution.orangesRotting(grid2)); // -1

        int[][] grid3 = {{0, 2}};
        System.out.println(solution.orangesRotting(grid3)); // 0
    }
}

class Solution {
    public int orangesRotting(int[][] grid) {
        int n = grid.length, m = grid[0].length;
        int orange = 0;
        Queue<int[]> q = new ArrayDeque<>();
        for(int x = 0; x < n; x++) {
            for(int y = 0; y < m; y++) {
                if(grid[x][y] == 1)
                    orange++;
                else if(grid[x][y] == 2)
                    q.offer(new int[] {x, y});
            }
        }
        if(orange == 0)
            return 0;
        int[] dx = {0, 1, 0, -1};
        int[] dy = {1, 0, -1, 0};
        int time = 0;
        while(!q.isEmpty() && orange > 0) {
            time++;
            Queue<int[]> temp = new ArrayDeque<>();
            while(!q.isEmpty() && orange > 0) {
                int[] cur = q.poll();
                for(int dir = 0; dir < 4; dir++) {
                    int nx = cur[0] + dx[dir];
                    int ny = cur[1] + dy[dir];
                    if(nx < 0 || ny < 0 || nx >= n || ny >= m)
                        continue;
                    if(grid[nx][ny] == 1) {
                        temp.offer(new int[] {nx, ny});
                        grid[nx][ny] = 2;
                        orange--;
                    }
                }
            }
            q = temp;
        }
        return orange == 0 ? time : -1;
    }
}
