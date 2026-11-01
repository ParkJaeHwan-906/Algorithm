package oct.week4.leetcode.ShortestPathVisitingAllNodes_박재환;

import java.util.*;

public class ShortestPathVisitingAllNodes_박재환 {
    public static void main(String[] args) {
        Solution solution = new Solution();

        System.out.println(solution.shortestPathLength(new int[][]{
                {1, 2, 3}, {0}, {0}, {0}
        })); // 4
        System.out.println(solution.shortestPathLength(new int[][]{
                {1}, {0, 2, 4}, {1, 3, 4}, {2}, {1, 2}
        })); // 4
    }
}

class Solution {

    static final int INF = Integer.MAX_VALUE / 2;

    int n;
    int FULL;
    int[][] dists;
    int[][] costs;
    public int shortestPathLength(int[][] graph) {
        set(graph);
        int result = INF;
        for (int i = 0; i < n; i++) {
            result = Math.min(result, travel(i, 1 << i));
        }
        return result;
    }

    void set(int[][] graph) {
        n = graph.length;
        FULL = (1 << n) - 1;
        costs = new int[n][n];
        dists = new int[n][FULL + 1];
        for(int i = 0; i < n; i++) {
            Arrays.fill(costs[i], INF);
            Arrays.fill(dists[i], -1);
            costs[i][i] = 0;
            for (int next : graph[i]) {
                costs[i][next] = 1;
            }
        }
        for (int mid = 0; mid < n; mid++) {
            for (int start = 0; start < n; start++) {
                for (int end = 0; end < n; end++) {
                    costs[start][end] = Math.min(
                            costs[start][end],
                            costs[start][mid] + costs[mid][end]
                    );
                }
            }
        }
    }

    int travel(int cur, int state) {
        if(state == FULL)
            return 0;
        if(dists[cur][state] != -1)
            return dists[cur][state];
        dists[cur][state] = INF;
        for (int next = 0; next < n; next++) {
            if ((state & (1 << next)) != 0)
                continue;
            dists[cur][state] = Math.min(
                    dists[cur][state],
                    costs[cur][next]
                            + travel(next, state | (1 << next))
            );
        }
        return dists[cur][state];
    }
}
