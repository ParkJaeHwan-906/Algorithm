package oct.week2.programmers.동굴탐험_박재환;

import java.util.*;

public class 동굴탐험_박재환 {
    public static void main(String[] args) {
        int[] n = {9, 9, 9};
        int[][][] path = {
                {{0, 1}, {0, 3}, {0, 7}, {8, 1}, {3, 6}, {1, 2}, {4, 7}, {7, 5}},
                {{8, 1}, {0, 1}, {1, 2}, {0, 7}, {4, 7}, {0, 3}, {7, 5}, {3, 6}},
                {{0, 1}, {0, 3}, {0, 7}, {8, 1}, {3, 6}, {1, 2}, {4, 7}, {7, 5}}
        };
        int[][][] order = {
                {{8, 5}, {6, 7}, {4, 1}},
                {{4, 1}, {5, 2}},
                {{4, 1}, {8, 7}, {6, 5}}
        };
        boolean[] result = {true, true, false};

        for (int i = 0; i < n.length; i++) {
            boolean answer = new Solution().solution(n[i], path[i], order[i]);
            System.out.println("예제 " + (i + 1) + ": " + answer + " / 정답: " + result[i]);
        }
    }
}

class Solution {
    int n;
    List<Integer>[] connections;
    List<Integer>[] graph;
    int[] preorders;
    public boolean solution(int n, int[][] path, int[][] order) {
        set(n, path, order);
        Queue<Integer> q = new ArrayDeque<>();
        for(int i = 0; i < n; i++) {
            if(preorders[i] == 0)
                q.offer(i);
        }
        int visited = 0;
        while(!q.isEmpty()) {
            int cur = q.poll();
            visited++;
            for(int conn : graph[cur]) {
                if(--preorders[conn] == 0)
                    q.offer(conn);
            }
        }
        return visited == n;
    }

    void set(int n, int[][] path, int[][] order) {
        this.n = n;
        this.connections = new List[n];
        for(int i = 0; i < n; i++)
            connections[i] = new ArrayList<>();
        for(int[] conn : path) {
            int a = conn[0];
            int b = conn[1];
            connections[a].add(b);
            connections[b].add(a);
        }
        preorders = new int[n];
        setPreorders();
        for(int[] ref : order) {
            graph[ref[0]].add(ref[1]);
            preorders[ref[1]]++;
        }
    }

    void setPreorders() {
        graph = new List[n];
        for(int i = 0; i < n; i++)
            graph[i] = new ArrayList<>();

        Queue<Integer> q = new ArrayDeque<>();
        boolean[] visited = new boolean[n];
        q.offer(0);
        visited[0] = true;
        while(!q.isEmpty()) {
            int cur = q.poll();
            for(int conn : connections[cur]) {
                if(visited[conn])
                    continue;
                visited[conn] = true;
                preorders[conn]++;
                q.offer(conn);
                graph[cur].add(conn);
            }
        }
    }
}
