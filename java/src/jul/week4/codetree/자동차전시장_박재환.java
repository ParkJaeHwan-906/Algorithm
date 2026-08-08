package jul.week4.codetree;

import java.util.*;
import java.io.*;

public class 자동차전시장_박재환 {
    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        init(br);
        br.close();
    }

    static int n, m, k;
    static List<Integer>[] connections;
    static void init(BufferedReader br) throws IOException {
        StringTokenizer st;

        st = new StringTokenizer(br.readLine().trim());
        n = Integer.parseInt(st.nextToken());
        m = Integer.parseInt(st.nextToken());
        k = Integer.parseInt(st.nextToken());

        connections = new List[n + 1];
        for(int i = 0; i < n + 1; i++) connections[i] = new ArrayList<>();
        for(int i = 0; i < m; i++) {
            st = new StringTokenizer(br.readLine().trim());
            int a = Integer.parseInt(st.nextToken());
            int b = Integer.parseInt(st.nextToken());
            connections[a].add(b);
        }

        int[] max = new int[n + 1];
        Arrays.fill(max, -1);
        st = new StringTokenizer(br.readLine().trim());
        for(int i = 0; i < k; i++) {
            int s = Integer.parseInt(st.nextToken());
            findMinRoute(max, s);
        }

        int result = Integer.MAX_VALUE;
        for(int i = 1; i <= n; i++) result = Math.min(result, max[i]);
        System.out.println(result == Integer.MAX_VALUE ? -1 : result);
    }

    static void findMinRoute(int[] max, int s) {
        PriorityQueue<int[]> pq = new PriorityQueue<>((a, b) -> Integer.compare(a[1], b[1]));
        int[] dist = new int[n + 1];
        Arrays.fill(dist, Integer.MAX_VALUE);

        pq.offer(new int[] {s, 0});
        dist[s] = 0;

        while(!pq.isEmpty()) {
            int[] cur = pq.poll();
            int curL = cur[0];
            int curD = cur[1];
            if(dist[curL] < curD) continue;
            for(int i : connections[curL]) {
                if(dist[i] > curD + 1) {
                    dist[i] = curD + 1;
                    pq.offer(new int[] {i, dist[i]});
                }
            }
        }

        for(int i = 1; i < n + 1; i++) {
            max[i] = Math.max(max[i], dist[i]);
        }
    }
}
