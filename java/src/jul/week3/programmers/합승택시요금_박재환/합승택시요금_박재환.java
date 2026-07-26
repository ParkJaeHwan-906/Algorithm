package jul.week3.programmers.합승택시요금_박재환;

import java.util.*;

public class 합승택시요금_박재환 {
    public static void main(String[] args) {
        Solution solution = new Solution();

        int[][] fares1 = {
                {4, 1, 10}, {3, 5, 24}, {5, 6, 2},
                {3, 1, 41}, {5, 1, 24}, {4, 6, 50},
                {2, 4, 66}, {2, 3, 22}, {1, 6, 25}
        };
        System.out.println("테스트 1: " + solution.solution(6, 4, 6, 2, fares1)
                + " (기댓값: 82)");

        int[][] fares2 = {
                {5, 7, 9}, {4, 6, 4}, {3, 6, 1},
                {3, 2, 3}, {2, 1, 6}
        };
        System.out.println("테스트 2: " + solution.solution(7, 3, 4, 1, fares2)
                + " (기댓값: 14)");

        int[][] fares3 = {
                {2, 6, 6}, {6, 3, 7}, {4, 6, 7}, {6, 5, 11},
                {2, 5, 12}, {5, 3, 20}, {2, 4, 8}, {4, 3, 9}
        };
        System.out.println("테스트 3: " + solution.solution(6, 4, 5, 6, fares3)
                + " (기댓값: 18)");

    }
}

class Solution {
    int[][] connections;
    public int solution(int n, int s, int a, int b, int[][] fares) {
        set(n, fares);
        findAllCost(n);
        return getMinCost(n, s, a, b);
    }
    static final int INF = 20_000_001;
    void set(int n, int[][] fares) {
        connections = new int[n + 1][n + 1];
        for(int i = 0; i < n + 1; i++) {
            Arrays.fill(connections[i], INF);
            connections[i][i] = 0;
        }
        for(int[] connection : fares) {
            int a = connection[0];
            int b = connection[1];
            int cost = connection[2];
            connections[a][b] = cost;
            connections[b][a] = cost;
        }
    }


    void findAllCost(int n) {
        for(int mid = 1; mid < n + 1; mid++) {
            for(int start = 1; start < n + 1; start++) {
                for(int end = 1; end < n + 1; end++) {
                    connections[start][end] = Math.min(
                            connections[start][end],
                            connections[start][mid] + connections[mid][end]
                    );
                }
            }
        }
    }

    int getMinCost(int n, int s, int a, int b) {
        /**
         * 두 가지 경우
         * - 혼자 타고 가는 경우
         * - 중간에 내려서 가는 경우
         */
        int minCost = connections[s][a] + connections[s][b];
        for(int mid = 1; mid < n + 1; mid++) {
            int candCost = connections[s][mid] + connections[mid][a] + connections[mid][b];
            minCost = Math.min(candCost, minCost);
        }
        return minCost;
    }
}
