package jun.week4.programmers.섬연결하기_박재환;

import java.util.PriorityQueue;

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
        System.out.println(sol.solution(n, costs));
      }
}

class Solution {
    class Connection implements Comparable<Connection> {
        int a, b;
        int cost;

        Connection(int a, int b, int cost) {
            this.a = a;
            this.b = b;
            this.cost = cost;
        }

        public int compareTo(Connection o) {
            return Integer.compare(this.cost, o.cost);
        }
    }

    int n;
    int[] parents;
    PriorityQueue<Connection> pq;
    public int solution(int n, int[][] costs) {
        set(n, costs);

        int edges = 0;
        int totalCost = 0;
        while(!pq.isEmpty()) {
            Connection conn = pq.poll();
            int a = conn.a, b = conn.b, cost = conn.cost;
            if(union(a, b)) {
                totalCost += cost;
                if(++edges == n - 1) break;
            }
        }

        return totalCost;
    }

    void set(int n, int[][] costs) {
        this.n = n;

        pq = new PriorityQueue<>();
        for(int[] conn : costs) {
            int a = conn[0], b = conn[1], cost = conn[2];
            pq.offer(new Connection(a, b, cost));
        }

        parents = new int[n];
        for(int i = 0; i < n; i++) parents[i] = i;
    }

    int find(int a) {
        if(parents[a] == a) return a;
        return parents[a] = find(parents[a]);
    }

    boolean union(int a, int b) {
        int pa =  find(a), pb = find(b);
        if(pa == pb) return false;
        parents[pa] = pb;
        return true;
    }
}
