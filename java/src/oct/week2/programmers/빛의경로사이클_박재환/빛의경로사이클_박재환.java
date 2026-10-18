package oct.week2.programmers.빛의경로사이클_박재환;

import java.util.*;

public class 빛의경로사이클_박재환 {
    public static void main(String[] args) {
        Solution sol = new Solution();
        System.out.println(
                Arrays.toString(
                        sol.solution(
                                new String[] {"SL", "LR"}
                        )
                )
        );
    }
}

class Solution {

    static final int[] dx = {0, 1, 0, -1};
    static final int[] dy = {1, 0, -1, 0};

    int n, m;
    boolean[][][] visited;
    public int[] solution(String[] grid) {
        set(grid);
        List<Integer> result = new ArrayList<>();
        for (int x = 0; x < n; x++) {
            for (int y = 0; y < m; y++) {
                for (int dir = 0; dir < 4; dir++) {
                    if (visited[x][y][dir])
                        continue;
                    int length = findCycle(grid, x, y, dir);
                    result.add(length);
                }
            }
        }
        result.sort(Integer::compareTo);
        int[] resultArr = new int[result.size()];
        for(int i = 0; i < result.size(); i++)
            resultArr[i] = result.get(i);
        return resultArr;
    }

    void set(String[] grid) {
        n = grid.length;
        m = grid[0].length();
        visited = new boolean[n][m][4];
    }

    int findCycle(String[] grid, int x, int y, int dir) {
        int count = 0;
        while (!visited[x][y][dir]) {
            visited[x][y][dir] = true;
            count++;
            char c = grid[x].charAt(y);
            if (c == 'L')
                dir = (dir + 3) % 4;
            else if (c == 'R')
                dir = (dir + 1) % 4;
            x = (x + dx[dir] + n) % n;
            y = (y + dy[dir] + m) % m;
        }
        return count;
    }
}