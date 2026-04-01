package mar.week5.boj;

import java.util.*;
import java.io.*;

public class 미확인도착지_박재환 {
    static BufferedReader br;
    static StringBuilder sb;
    public static void main(String[] args) throws IOException {
        br = new BufferedReader(new InputStreamReader(System.in));
        sb = new StringBuilder();
        init();
        br.close();
        System.out.println(sb);
    }
    static StringTokenizer st;
    static int n, m, t;
    static int s, g, h;
    static List<int[]>[] connections;
    static int[] cand;
    static int ghCost;
    static void init() throws IOException {
        int tc = Integer.parseInt(br.readLine().trim());
        while(tc-- > 0) {
            st = new StringTokenizer(br.readLine().trim());
            n = Integer.parseInt(st.nextToken());       // 도시 수
            m = Integer.parseInt(st.nextToken());       // 간선 수
            t = Integer.parseInt(st.nextToken());       // 목적지 후보

            st = new StringTokenizer(br.readLine().trim());
            s = Integer.parseInt(st.nextToken());       // 출발지
            g = Integer.parseInt(st.nextToken());       // 필수 경로 a
            h = Integer.parseInt(st.nextToken());       // 필수 경로 b

            connections = new List[n+1];
            for(int i = 0; i < n + 1;) connections[i++] = new ArrayList<>();
            for(int i = 0; i < m; i++) {
                st = new StringTokenizer(br.readLine().trim());
                int from = Integer.parseInt(st.nextToken());
                int to = Integer.parseInt(st.nextToken());
                int cost = Integer.parseInt(st.nextToken());

                connections[from].add(new int[] {to, cost});
                connections[to].add(new int[] {from, cost});

                if ((from == g && to == h) || (from == h && to == g)) {
                    ghCost = cost;
                }
            }

            cand = new int[t];
            for(int i = 0; i < t; i++) cand[i] = Integer.parseInt(br.readLine().trim());

            solution();
            sb.append('\n');
        }
    }
    static void solution() {
        int[] distS = findShortestRoute(s);
        int[] distG = findShortestRoute(g);
        int[] distH = findShortestRoute(h);

        Arrays.sort(cand);
        for(int end : cand) {
            int route1 = distS[g] + ghCost + distH[end];
            int route2 = distS[h] + ghCost + distG[end];

            if(distS[end] == route1 || distS[end] == route2) sb.append(end).append(' ');
        }
    }
    static final int INF = 1_000_000_009;
    static int[] findShortestRoute(int start) {
        int[] dist = new int[n + 1];

        PriorityQueue<int[]> pq = new PriorityQueue<>((a, b) -> Integer.compare(a[1], b[1]));
        Arrays.fill(dist, INF);

        pq.offer(new int[] {start, 0});
        dist[start] = 0;

        while(!pq.isEmpty()) {
            int[] cur = pq.poll();
            int from = cur[0];
            int acc = cur[1];

            if(dist[from] < acc) continue;

            for(int[] connection : connections[from]) {
                int to = connection[0];
                int cost = connection[1];

                if(dist[to] > acc + cost) {
                    dist[to] = acc + cost;
                    pq.offer(new int[] {to, dist[to]});
                }
            }
        }
        return dist;
    }
}
