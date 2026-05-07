package may.week1.programmers;

import java.util.*;

public class 등굣길_박재환 {
    public static void main(String[] args) {
        int m = 4;
        int n = 3;
        int[][] puddles = {{2, 2}};

        Solution solution = new Solution();
        System.out.println(solution.solution(m, n, puddles));
    }
}

class Solution {
    /**
     * m x n : (1, 1) -> (m, n)
     * 집 : (1, 1)
     * 학교 : (m, n)
     */
    final int MOD = 1_000_000_007;
    int m, n;
    int[][] board;

    public int solution(int m, int n, int[][] puddles) {
        init(m, n, puddles);

        int[][] count = new int[m + 1][n + 1];
        count[1][1] = 1;

        for(int x = 1; x < m + 1; x++) {
            for(int y = 1; y < n + 1; y++) {
                if(x == 1 && y == 1) continue;
                if(board[x][y] == -1) {
                    count[x][y] = 0;
                    continue;
                }

                count[x][y] = (count[x - 1][y] + count[x][y - 1]) % MOD;
            }
        }

        return count[m][n];
    }

    void init(int m, int n, int[][] puddles) {
        this.m = m;
        this.n = n;
        board = new int[m + 1][n + 1];
        for (int[] puddle : puddles) {
            int x = puddle[0];
            int y = puddle[1];
            board[x][y] = -1;
        }
    }
}
