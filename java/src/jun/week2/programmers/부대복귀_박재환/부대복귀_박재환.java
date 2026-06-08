package jun.week2.programmers.부대복귀_박재환;

import java.util.*;

/**
 * [풀이 시간]
 * 00:25:00
 * AI 사용 여부 O
 * => 다익스트라를 sources 마다 실행 -> destination 에서 1회 실행으로 모든 목적지로의 거리 구함
 */
public class 부대복귀_박재환 {

    public static void main(String[] args) {
        int n = 3;
        int[][] roads = {{1, 2}, {2, 3}};
        int[] sources = {2, 3};
        int destination = 1;

        Solution solution = new Solution();
        System.out.print(Arrays.toString(solution.solution(n, roads, sources, destination)));
    }
}

class Solution {
    /**
     * 부대가 위치한 지역을 포함한 각 지역은 고유 id로 표현된다.
     * 두 지역간 이동에는 1 시간이 걸린다.
     * 임무 수행 후 부대원은 지도 정보를 이용해 최단시간에 부대로 복귀하고자한다.
     * 임무 시작 때와 다르게 되돌아오는 경로가 사라져 복귀가 불가능할 수 있다.
     */
    int n;
    List<Integer>[] connections;
    public int[] solution(int n, int[][] roads, int[] sources, int destination) {
        set(n, roads, sources, destination);

        int[] dist = findMinDist(destination);
        int[] result = new int[sources.length];
        for(int i = 0; i < sources.length; i++) {
            result[i] = dist[sources[i]] == Integer.MAX_VALUE ? -1 : dist[sources[i]];
        }

        return result;
    }

    void set(int n, int[][] roads, int[] sources, int destination) {
        this.n = n;
        connections = new List[n + 1];
        for(int i = 0; i < n + 1; i++) connections[i] = new ArrayList<>();
        for(int[] connection : roads) {
            int a = connection[0];
            int b = connection[1];
            connections[a].add(b);
            connections[b].add(a);
        }
    }

    int[] findMinDist(int s) {
        PriorityQueue<int[]> pq = new PriorityQueue<>((a, b) -> Integer.compare(a[1], b[1]));
        int[] dist = new int[n + 1];
        Arrays.fill(dist, Integer.MAX_VALUE);

        dist[s] = 0;
        pq.offer(new int[]{s, 0});

        while(!pq.isEmpty()) {
            int[] cur = pq.poll();

            if(dist[cur[0]] < cur[1]) continue;

            for(int next : connections[cur[0]]) {
                if(dist[next] > cur[1] + 1) {
                    dist[next] = cur[1] + 1;
                    pq.offer(new int[] {next, dist[next]});
                }
            }
        }
        return dist;
    }
}