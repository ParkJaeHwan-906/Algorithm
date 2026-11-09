package nov.week1.programmers.가장먼노드_박재환;

import java.util.*;

public class 가장먼노드_박재환 {
    public static void main(String[] args) {
        Solution solution = new Solution();
        int[][] vertex = {
                {3, 6},
                {4, 3},
                {3, 2},
                {1, 3},
                {1, 2},
                {2, 4},
                {5, 2}
        };
        System.out.println(solution.solution(6, vertex));
    }
}

class Solution {
    static final int INF = Integer.MAX_VALUE / 2;
    List<Integer>[] connections;
    public int solution(int n, int[][] edge) {
        set(n, edge);
        PriorityQueue<int[]> pq = new PriorityQueue<>(
                (a, b) -> Integer.compare(a[1], b[1])
        );
        int[] dist = new int[n + 1];
        Arrays.fill(dist, INF);

        pq.offer(new int[] {1, 0});
        dist[1] = 0;

        while(!pq.isEmpty()) {
            int[] cur = pq.poll();
            for(int next : connections[cur[0]]) {
                if(dist[next] > cur[1] + 1) {
                    dist[next] = cur[1] + 1;
                    pq.offer(new int[] {next, dist[next]});
                }
            }
        }

        int farDist = 0;
        int count = 0;
        for(int i = 1; i <= n; i++) {
            if(farDist == dist[i])
                count++;
            else if(farDist < dist[i]) {
                farDist = dist[i];
                count = 1;
            }
        }
        return count;
    }

    void set(int n, int[][] edge) {
        connections = new List[n + 1];
        for(int i = 1; i <= n;)
            connections[i++] = new ArrayList<>();

        for(int[] conn : edge) {
            int from = conn[0];
            int to = conn[1];
            connections[from].add(to);
            connections[to].add(from);
        }
    }
}
