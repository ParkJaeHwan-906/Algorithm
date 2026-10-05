package sep.week4.programmers.섬연결하기_박재환;

import java.util.*;

public class 섬연결하기_박재환 {
    public static void main(String[] args) {
        int n = 4;
        int[][] costs = {
                {0, 1, 1},
                {0, 2, 2},
                {1, 2, 5},
                {1, 3, 1},
                {2, 3, 8}
        };
        Solution sol = new Solution();
        System.out.print(sol.solution(n, costs));
    }

}

class Solution {
    class Edge implements Comparable<Edge> {
        int from, to;
        int cost;
        Edge(int from, int to, int cost) {
            this.from = from;
            this.to = to;
            this.cost = cost;
        }
        @Override
        public int compareTo(Edge o) {
            return Integer.compare(this.cost, o.cost);
        }
    }
    int[] parents;
    public int solution(int n, int[][] costs) {
        PriorityQueue<Edge> pq = new PriorityQueue<>();
        set(costs, pq);

        make(n);
        int totalCost = 0;
        int edges = 0;
        while(!pq.isEmpty()) {
            Edge e = pq.poll();
            if(union(e.from, e.to)) {
                totalCost += e.cost;
                if(++edges == n - 1) {
                    break;
                }
            }
        }
        return totalCost;
    }
    void set(int[][] costs, PriorityQueue<Edge> pq) {
        for(int[] c : costs) {
            int from = c[0];
            int to = c[1];
            int cost = c[2];
            pq.offer(new Edge(from, to, cost));
        }
    }
    // ==========================================
    // Union Find
    // ==========================================
    void make(int n) {
        parents = new int[n];
        for(int i = 0; i < n; i++) {
            parents[i] = i;
        }
    }
    int find(int i) {
        if(parents[i] == i) {
            return i;
        }
        return parents[i] = find(parents[i]);
    }
    boolean union(int a, int b) {
        int aRoot = find(a);
        int bRoot = find(b);
        if(aRoot == bRoot) {
            return false;
        }
        parents[bRoot] = aRoot;
        return true;
    }
}