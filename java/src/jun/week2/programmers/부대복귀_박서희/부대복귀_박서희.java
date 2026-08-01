package jun.week2.programmers.부대복귀_박서희;

import java.util.*;

/*
  문제풀이 시간: 00:31:50
  AI 사용 여부: X
 */
public class 부대복귀_박서희 {
    public static void main(String[] args) {
        int n = 5;
        int[][] roads = {{1, 2}, {1, 4}, {2, 4}, {2, 5}, {4, 5}};
        int[] sources = {1, 3, 5};
        int destination = 5;

        Solution solution = new Solution();
        int[] result = solution.solution(n, roads, sources, destination);
        System.out.println(Arrays.toString(result));
    }
}

class Solution {
    static int n;
    static final int INF = 1000002;
    static ArrayList<Integer>[] graph;
    static int[] dist;

    public int[] solution(int n, int[][] roads, int[] sources, int destination) {

        this.n = n;
        graph = new ArrayList[n + 1];
        for (int i = 0; i <= n; i++) graph[i] = new ArrayList<>();

        for (int i = 0; i < roads.length; i++) {
            int a = roads[i][0];
            int b = roads[i][1];
            graph[a].add(b);
            graph[b].add(a);
        }

        dijkstra(destination);

        int[] answer = new int[sources.length];
        for (int i = 0; i < sources.length; i++) {
            int source = sources[i];
            if (dist[source] == INF) answer[i] = -1;
            else answer[i] = dist[source];
        }

        return answer;
    }

    public void dijkstra(int destination) {
        dist = new int[n + 1];
        Arrays.fill(dist, INF);

        PriorityQueue<int[]> pq = new PriorityQueue<>(Comparator.comparingInt(a -> a[1]));

        dist[destination] = 0;
        pq.offer(new int[]{destination, 0});

        while (!pq.isEmpty()) {
            int[] cur = pq.poll();
            int curNode = cur[0];
            int curDist = cur[1];

            if (curDist > dist[curNode]) continue;

            for (Integer next : graph[curNode]) {
                if (dist[curNode] + 1 < dist[next]) {
                    dist[next] = dist[curNode] + 1;
                    pq.offer(new int[]{next, dist[next]});
                }
            }
        }
    }
}
