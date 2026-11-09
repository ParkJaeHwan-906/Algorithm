package nov.week1.programmers.합승택시요금_박재환;

import java.util.*;

public class 합승택시요금_박재환 {
    public static void main(String[] args) {
        Solution solution = new Solution();
        System.out.println(solution.solution(6, 4, 6, 2, new int[][]{
                {4, 1, 10}, {3, 5, 24}, {5, 6, 2},
                {3, 1, 41}, {5, 1, 24}, {4, 6, 50},
                {2, 4, 66}, {2, 3, 22}, {1, 6, 25}
        }));
        System.out.println(solution.solution(7, 3, 4, 1, new int[][]{
                {5, 7, 9}, {4, 6, 4}, {3, 6, 1},
                {3, 2, 3}, {2, 1, 6}
        }));
        System.out.println(solution.solution(6, 4, 5, 6, new int[][]{
                {2, 6, 6}, {6, 3, 7}, {4, 6, 7}, {6, 5, 11},
                {2, 5, 12}, {5, 3, 20}, {2, 4, 8}, {4, 3, 9}
        }));
    }
}

class Solution {

    static final int INF = Integer.MAX_VALUE / 2;

    int[][] dist;
    public int solution(int n, int s, int a, int b, int[][] fares) {
        set(n, fares);
        getMinCost(n);

        int compareCost = dist[s][a] + dist[s][b];
        for(int mid = 1; mid <= n; mid++) {
            if(dist[s][mid] == INF || dist[mid][a] == INF || dist[mid][b] == INF)
                continue;

            compareCost = Math.min(
                    compareCost,
                    dist[s][mid] + dist[mid][a] + dist[mid][b]
            );
        }
        return compareCost;
    }

    void set(int n, int[][] fares) {
        dist = new int[n + 1][n + 1];
        for(int i = 0; i <= n; i++) {
            Arrays.fill(dist[i], INF);
            dist[i][i] = 0;
        }
        for(int[] fare : fares) {
            dist[fare[0]][fare[1]] = fare[2];
            dist[fare[1]][fare[0]] = fare[2];
        }
    }

    void getMinCost(int n) {
        for(int mid = 1; mid <= n; mid++) {
            for(int s = 1; s <= n; s++) {
                for(int e = 1; e <= n; e++) {
                    dist[s][e] = Math.min(
                            dist[s][e],
                            dist[s][mid] + dist[mid][e]
                    );
                }
            }
        }
    }
}
