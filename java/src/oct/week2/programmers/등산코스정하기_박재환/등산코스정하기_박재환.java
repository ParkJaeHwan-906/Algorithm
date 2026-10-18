package oct.week2.programmers.등산코스정하기_박재환;

import java.util.*;

public class 등산코스정하기_박재환 {
    public static void main(String[] args) {
        int[] n = {6, 7, 7, 5};
        int[][][] paths = {
                {{1, 2, 3}, {2, 3, 5}, {2, 4, 2}, {2, 5, 4}, {3, 4, 4}, {4, 5, 3}, {4, 6, 1}, {5, 6, 1}},
                {{1, 4, 4}, {1, 6, 1}, {1, 7, 3}, {2, 5, 2}, {3, 7, 4}, {5, 6, 6}},
                {{1, 2, 5}, {1, 4, 1}, {2, 3, 1}, {2, 6, 7}, {4, 5, 1}, {5, 6, 1}, {6, 7, 1}},
                {{1, 3, 10}, {1, 4, 20}, {2, 3, 4}, {2, 4, 6}, {3, 5, 20}, {4, 5, 6}}
        };
        int[][] gates = {
                {1, 3},
                {1},
                {3, 7},
                {1, 2}
        };
        int[][] summits = {
                {5},
                {2, 3, 4},
                {1, 5},
                {5}
        };
        int[][] expected = {
                {5, 3},
                {3, 4},
                {5, 1},
                {5, 6}
        };

        for(int i = 0; i < n.length; i++) {
            Solution solution = new Solution();
            int[] result = solution.solution(n[i], paths[i], gates[i], summits[i]);
            System.out.println("Test " + (i + 1) + " : " + Arrays.toString(result)
                    + " / " + (Arrays.equals(result, expected[i]) ? "PASS" : "FAIL"));
        }
    }
}

class Solution {

    static final int INF = Integer.MAX_VALUE;

    int n;
    int[] intensities;
    List<int[]>[] connections;
    Set<Integer> summitSet;
    public int[] solution(int n, int[][] paths, int[] gates, int[] summits) {
        set(n, paths, summits);
        PriorityQueue<int[]> pq = new PriorityQueue<>((a, b) -> {           // [cur, intensity]
            return Integer.compare(a[1], b[1]);
        });
        for(int gate : gates) {
            intensities[gate] = 0;
            pq.offer(new int[] {gate, 0});
        }
        while(!pq.isEmpty()) {
            int[] cur = pq.poll();
            int curLoc = cur[0];
            int curIntensity = cur[1];
            if(summitSet.contains(curLoc))
                continue;
            if(intensities[curLoc] < curIntensity)
                continue;
            for(int[] conn : connections[curLoc]) {
                int next = conn[0];
                int nextIntensity = Math.max(curIntensity, conn[1]);
                if(intensities[next] > nextIntensity) {
                    intensities[next] = nextIntensity;
                    pq.offer(new int[] {next, intensities[next]});
                }
            }
        }

        return getResult(summits);
    }

    void set(int n, int[][] paths, int[] summits) {
        intensities = new int[n + 1];
        Arrays.fill(intensities, INF);
        connections = new List[n + 1];
        for(int i = 0; i < n + 1; i++)
            connections[i] = new ArrayList<>();
        for(int[] path : paths) {
            connections[path[0]].add(new int[] {path[1], path[2]});
            connections[path[1]].add(new int[] {path[0], path[2]});
        }
        summitSet = new HashSet<>();
        for(int i : summits)
            summitSet.add(i);
    }

    int[] getResult(int[] summits) {
        int bestSummit = INF;
        int bestIntensity = INF;
        for(int i : summits) {
            if(bestIntensity > intensities[i]
                    || (bestIntensity == intensities[i] && bestSummit > i)) {
                bestSummit = i;
                bestIntensity = intensities[i];
            }
        }
        return new int[] {bestSummit, bestIntensity};
    }
}