package oct.week3.programmers.미로탈출_박재환;

import java.util.*;

public class 미로탈출_박재환 {
    public static void main(String[] args) {
        Solution solution = new Solution();

        int result1 = solution.solution(
                3, 1, 3,
                new int[][]{{1, 2, 2}, {3, 2, 3}},
                new int[]{2}
        );
        System.out.println("예제 1: " + result1 + " (기대값: 5)");

        int result2 = solution.solution(
                4, 1, 4,
                new int[][]{{1, 2, 1}, {3, 2, 1}, {2, 4, 1}},
                new int[]{2, 3}
        );
        System.out.println("예제 2: " + result2 + " (기대값: 4)");
    }
}

class Solution {

    static final int INF = Integer.MAX_VALUE / 2;

    class Edge {
        int to;
        int cost;
        Edge(int to, int cost) {
            this.to = to;
            this.cost = cost;
        }
    }

    class Node {
        int cur;
        int cost;
        int trap;
        Node(int cur, int cost, int trap) {
            this.cur = cur;
            this.cost = cost;
            this.trap = trap;
        }
    }

    int n;
    int[] isTrap;
    List<Edge>[] connections;
    List<Edge>[] reverseConnections;
    public int solution(int n, int start, int end, int[][] roads, int[] traps) {
        set(n, roads, traps);
        PriorityQueue<Node> pq = new PriorityQueue<>((a, b) -> Integer.compare(a.cost, b.cost));
        int[][] costs = new int[n + 1][(1 << traps.length)];
        for(int i = 0; i <= n; i++)
            Arrays.fill(costs[i], INF);
        costs[start][0] = 0;
        pq.offer(new Node(start, 0, 0));
        while(!pq.isEmpty()) {
            Node node = pq.poll();
            if(costs[node.cur][node.trap] < node.cost)
                continue;
            if(node.cur == end) {
                return node.cost;
            }
            boolean curTrapActive = (node.trap & isTrap[node.cur]) != 0;
            for(Edge edge : connections[node.cur]) {
                boolean nextTrapActive = (node.trap & isTrap[edge.to]) != 0;
                boolean reversed = curTrapActive ^ nextTrapActive;
                if(reversed)
                    continue;
                int nextState = node.trap ^ isTrap[edge.to];
                int nextCost = node.cost + edge.cost;
                if(costs[edge.to][nextState] > nextCost) {
                    costs[edge.to][nextState] = nextCost;
                    pq.offer(new Node(edge.to, nextCost, nextState));
                }
            }
            for(Edge edge : reverseConnections[node.cur]) {
                boolean nextTrapActive = (node.trap & isTrap[edge.to]) != 0;
                boolean reversed = curTrapActive ^ nextTrapActive;
                if(!reversed)
                    continue;
                int nextState = node.trap ^ isTrap[edge.to];
                int nextCost = node.cost + edge.cost;
                if(costs[edge.to][nextState] > nextCost) {
                    costs[edge.to][nextState] = nextCost;
                    pq.offer(new Node(edge.to, nextCost, nextState));
                }
            }
        }
        return 0;
    }

    void set(int n, int[][] roads, int[] traps) {
        this.n = n;
        isTrap = new int[n + 1];
        for(int i = 0; i < traps.length; i++)
            isTrap[traps[i]] = (1 << i);
        connections = new List[n + 1];
        reverseConnections = new List[n + 1];
        for(int i = 0; i <= n; i++) {
            connections[i] = new ArrayList<>();
            reverseConnections[i] = new ArrayList<>();
        }
        for(int[] road : roads) {
            int from = road[0];
            int to = road[1];
            int cost = road[2];
            connections[from].add(new Edge(to, cost));
            reverseConnections[to].add(new Edge(from, cost));
        }
    }
}
