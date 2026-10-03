package sep.week4.programmers.합승택시요금_박재환;

import java.util.*;

public class 합승택시요금_박재환 {
    public static void main(String[] args) {
        int n = 6;
        int s = 4;
        int a = 6;
        int b = 2;
        int[][] fares = {{4, 1, 10}, {3, 5, 24}, {5, 6, 2}, {3, 1, 41}, {5, 1, 24}, {4, 6, 50}, {2, 4, 66}, {2, 3, 22}, {1, 6, 25}};
        Solution solution = new Solution();
        System.out.print(solution.solution(n, s, a, b, fares));
    }
}

class Solution {
    static final int INF = 100_000 * 200 + 7;
    int[][] fareBoard;
    public int solution(int n, int s, int a, int b, int[][] fares) {
        getAllFares(n, fares);

        int minCost = fareBoard[s - 1][a - 1] + fareBoard[s - 1][b - 1];        // 각자 따로 가는 경우
        for(int mid = 0; mid < n; mid++) {
            minCost = Math.min(
                    minCost,
                    fareBoard[s - 1][mid] + fareBoard[mid][a - 1] + fareBoard[mid][b - 1]
            );
        }
        return minCost;
    }

    void getAllFares(int n, int[][] fares) {
        fareBoard = new int[n][n];
        for(int i = 0; i < n; i++) {
            Arrays.fill(fareBoard[i], INF);
            fareBoard[i][i] = 0;
        }

        for(int[] fare : fares) {
            int a = fare[0] - 1;
            int b = fare[1] - 1;
            int v = fare[2];
            fareBoard[a][b] = v;
            fareBoard[b][a] = v;
        }

        for(int mid = 0; mid < n; mid++) {
            for(int start = 0; start < n; start++) {
                for(int end = 0; end < n; end++) {
                    fareBoard[start][end] = Math.min(
                            fareBoard[start][end],
                            fareBoard[start][mid] + fareBoard[mid][end]
                    );
                }
            }
        }
    }
}