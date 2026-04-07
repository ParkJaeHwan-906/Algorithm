package apr.week1.boj;

import java.util.*;
import java.io.*;

public class 비밀모임_박재환 {
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
    static int n, m;
    static int k;
    static int[] start;
    static int[] sum;
    static List<int[]>[] connections;
    static void init() throws IOException {
        int tc = Integer.parseInt(br.readLine().trim());
        while(tc-- > 0) {
            st = new StringTokenizer(br.readLine().trim());
            n = Integer.parseInt(st.nextToken());       // 방의 개수
            m = Integer.parseInt(st.nextToken());       // 통로의 개수

            connections = new List[n + 1];
            for(int i = 0; i < n + 1; i++) connections[i] = new ArrayList<>();
            for(int i = 0; i < m; i++) {
                st = new StringTokenizer(br.readLine().trim());
                int from = Integer.parseInt(st.nextToken());
                int to = Integer.parseInt(st.nextToken());
                int dist = Integer.parseInt(st.nextToken());
                // 양방향으로 연결
                connections[from].add(new int[] {to, dist});
                connections[to].add(new int[] {from, dist});
            }

            k = Integer.parseInt(br.readLine().trim());     // 모임에 참여하는 친구 수
            start = new int[k];
            st = new StringTokenizer(br.readLine().trim());
            for(int i = 0; i < k; i++) start[i] = Integer.parseInt(st.nextToken());

            sum = new int[n + 1];
            for(int s : start) {
                int[] dist = findNearRoom(s);
                for(int i = 1; i < n + 1; i++) sum[i] += dist[i];
            }

            int room = n + 5;
            int dist = INF;
            for(int i = 1; i < n + 1; i++) {
                if(dist > sum[i]) {
                    dist = sum[i];
                    room = i;
                }
            }
            sb.append(room).append('\n');
        }
    }
    static final int INF = 100 * 1000 + 7;
    static int[] findNearRoom(int s) {
        PriorityQueue<int[]> pq = new PriorityQueue<>((a, b) -> Integer.compare(a[1], b[1]));
        int[] dp = new int[n + 1];
        Arrays.fill(dp, INF);

        // 출발 위치 설정
        dp[s] = 0;
        pq.offer(new int[] {s, 0});

        // 최단 경로
        while(!pq.isEmpty()) {
            int[] cur = pq.poll();
            int from = cur[0];
            int accDist = cur[1];

            if(dp[from] < accDist) continue;

            for(int[] connection : connections[from]) {
                int to = connection[0];
                int dist = connection[1];

                if(dp[to] > dp[from] + dist) {
                    dp[to] = dp[from] + dist;
                    pq.offer(new int[] {to, dp[to]});
                }
            }
        }
        return dp;
    }
}
