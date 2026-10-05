package sep.week4.programmers.등산코스정하기_박재환;

import java.util.*;

public class 등산코스정하기_박재환 {
    public static void main(String[] args) {
        int n = 6;
        int[][] paths = {{1, 2, 3}, {2, 3, 5}, {2, 4, 2}, {2, 5, 4}, {3, 4, 4}, {4, 5, 3}, {4, 6, 1}, {5, 6, 1}};
        int[] gates = {1, 3};
        int[] summits = {5};
        Solution sol = new Solution();
        System.out.println(Arrays.toString(sol.solution(n, paths, gates, summits)));
    }
}

class Solution {
    final int INF = Integer.MAX_VALUE;

    class Edge implements Comparable<Edge> {
        int to;
        int cost;
        Edge(int to, int cost) {
            this.to = to;
            this.cost = cost;
        }
        @Override
        public int compareTo(Edge o) {
            return Integer.compare(this.cost, o.cost);
        }
    }

    List<Edge>[] edges;
    public int[] solution(int n, int[][] paths, int[] gates, int[] summits) {
        set(n, paths);
        PriorityQueue<Edge> pq = new PriorityQueue<>();
        Set<Integer> endPoint = new HashSet<>();
        int[] intensity = new int[n + 1];
        Arrays.fill(intensity, INF);
        for(int gate : gates) {
            pq.offer(new Edge(gate, 0));
            intensity[gate] = 0;
        }
        for(int summit : summits) {
            endPoint.add(summit);
        }
        while(!pq.isEmpty()) {
            Edge cur = pq.poll();
            if(endPoint.contains(cur.to)) {       // 정산이라면 더 이상 방문하지 않음
                continue;
            }
            if(intensity[cur.to] < cur.cost) {
                continue;
            }
            for(Edge next : edges[cur.to]) {
                int nextIntensity = Math.max(cur.cost, next.cost);
                if(intensity[next.to] > nextIntensity) {
                    intensity[next.to] = nextIntensity;
                    pq.offer(new Edge(next.to, nextIntensity));
                }
            }
        }
        return getResult(intensity, summits);
    }

    void set(int n, int[][] paths) {
        edges = new List[n + 1];
        for(int i = 1; i <= n; i++) {
            edges[i] = new ArrayList<>();
        }
        for(int[] path : paths) {
            // 양방향 통행이 가능
            edges[path[0]].add(new Edge(path[1], path[2]));
            edges[path[1]].add(new Edge(path[0], path[2]));
        }
    }

    int[] getResult(int[] itensity, int[] summits) {
        int bestSummit = -1;
        int bestItensity = INF;
        Arrays.sort(summits);
        for(int summit : summits) {
            if(itensity[summit] < bestItensity) {
                bestSummit = summit;
                bestItensity = itensity[summit];
            }
        }
        return new int[] {bestSummit, bestItensity};
    }
}
