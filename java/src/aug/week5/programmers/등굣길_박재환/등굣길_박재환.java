package aug.week5.programmers.등굣길_박재환;

import java.util.ArrayDeque;
import java.util.Queue;

public class 등굣길_박재환 {
    public static void main(String[] args) {
        int m = 4;
        int n = 3;
        int[][] puddles = {{2, 2}};
        Solution sol = new Solution();
        System.out.println(sol.solution(m, n, puddles) == 4);
    }
}

class Solution {

    // 오른쪽, 아래쪽으로만 이동 가능
    final int[] dx = {1, 0};
    final int[] dy = {0, 1};

    int[][] board;
    boolean[][] isPuddle;
    public int solution(int m, int n, int[][] puddles) {
        set(n, m, puddles);
        board[0][0] = 1;
        for(int x = 0; x < n; x++) {
            for(int y = 0; y < m; y++) {
                if(isPuddle[x][y]) {
                    board[x][y] = 0;
                    continue;
                }

                if(x == 0 && y == 0) {
                    continue;
                }

                long count = 0;
                if(x > 0) {
                    count += board[x - 1][y];
                }
                if(y > 0) {
                    count += board[x][y - 1];
                }
                board[x][y] = (int) count % 1_000_000_007;
            }
        }
        return board[n - 1][m - 1];
    }

    void set(int n, int m, int[][] puddles) {
        board = new int[n][m];
        isPuddle = new boolean[n][m];
        for(int[] puddle : puddles) {
            int x = puddle[0] - 1;
            int y = puddle[1] - 1;
            isPuddle[y][x] = true;
        }
    }
}