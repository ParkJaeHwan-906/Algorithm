package apr.week2.boj;

import java.util.*;
import java.io.*;

public class 도시건설_박재환 {
    static BufferedReader br;
    public static void main(String[] args) throws IOException {
        br = new BufferedReader(new InputStreamReader(System.in));
        init();
        br.close();
    }
    static StringTokenizer st;
    static int n, m;
    static PriorityQueue<int[]> pq;
    static long maxCost;
    static void init() throws IOException {
        pq = new PriorityQueue<>((a, b) -> Integer.compare(a[2], b[2]));

        st = new StringTokenizer(br.readLine().trim());
        n = Integer.parseInt(st.nextToken());
        m = Integer.parseInt(st.nextToken());

        for(int i = 0; i < m; i++) {
            st = new StringTokenizer(br.readLine().trim());
            int a = Integer.parseInt(st.nextToken());
            int b = Integer.parseInt(st.nextToken());
            int cost = Integer.parseInt(st.nextToken());

            pq.offer(new int[] {a, b, cost});
            maxCost += cost;
        }

        long minCost = solution();
        if(minCost == -1) {
            System.out.println(-1);
            return;
        }
        System.out.println((maxCost - minCost));
    }

    static int[] parents;
    static long solution() {
        make();
        long minCost = 0;
        int edge = 0;
        while(!pq.isEmpty()) {
            int[] cur = pq.poll();
            int a = cur[0];
            int b = cur[1];
            int cost = cur[2];

            if(union(a, b)) {
                minCost += cost;
                if(++edge == n - 1) break;
            }
        }
        return edge == n - 1 ? minCost : -1;
    }
    static int[] rank;
    static void make() {
        parents = new int[n + 1];
        rank = new int[n + 1];
        for(int i = 0; i < n + 1; i++) parents[i] = i;
    }

    static int find(int a) {
        if(parents[a] == a) return a;
        return parents[a] = find(parents[a]);
    }

    static boolean union(int a, int b) {
        int rootA = find(a);
        int rootB = find(b);
        if(rootA == rootB) return false;

        if(rank[rootA] > rank[rootB]) {
            parents[rootB] =  rootA;
        } else if(rank[rootB] > rank[rootA]) {
            parents[rootA] = rootB;
        } else {
            parents[rootB] = rootA;
            rank[rootA]++;
        }
        return true;
    }
}
