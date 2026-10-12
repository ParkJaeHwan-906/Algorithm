package oct.week1.programmers.등굣길_박재환;

import java.util.Arrays;

public class 등굣길_박재환 {
    public static void main(String[] args) {
        int m = 4;
        int n = 3;
        int[][] puddles = {{2, 2}};
        Solution sol = new Solution();
        System.out.print(sol.solution(m,n,puddles));
    }
}

class Solution {
    int[][] board;
    boolean[][] disabled;
    public int solution(int m, int n, int[][] puddles) {
        set(n, m, puddles);
        board[1][1] = 1;
        for(int x = 1; x <= n; x++) {
            for(int y = 1; y <= m; y++) {
                if(disabled[x][y]) {
                    continue;
                }
                board[x][y] = board[x][y] + (board[x - 1][y] + board[x][y - 1]) % 1_000_000_007;
            }
        }
        return board[n][m];
    }

    void set(int n, int m, int[][] puddles) {
        board = new int[n + 1][m + 1];
        disabled = new boolean[n + 1][m + 1];
        for(int[] puddle : puddles){
            disabled[puddle[1]][puddle[0]] = true;
        }
    }
}