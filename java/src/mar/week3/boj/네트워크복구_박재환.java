package mar.week3.boj;

import java.util.*;
import java.io.*;

public class 네트워크복구_박재환 {
    static BufferedReader br;
    public static void main(String[] args) throws IOException {
        br = new BufferedReader(new InputStreamReader(System.in));
        init();
        br.close();
    }
    static StringTokenizer st;
    static int n, m;
    static List<int[]>[] connections;
    static void init() throws IOException {
        /**
         * 연결 : 양방향
         * 가중치가 존재
         */
        st = new StringTokenizer(br.readLine().trim());
        n = Integer.parseInt(st.nextToken());       // 컴퓨터 개수
        m = Integer.parseInt(st.nextToken());       // 회선 개수

        connections = new List[n+1];
        for(int i=0; i<n+1;) connections[i++] = new ArrayList<>();

        while(m-- > 0) {
            st = new StringTokenizer(br.readLine().trim());
            int from = Integer.parseInt(st.nextToken());
            int to = Integer.parseInt(st.nextToken());
            int cost = Integer.parseInt(st.nextToken());

            connections[from].add(new int[] {to, cost});
            connections[to].add(new int[] {from, cost});
        }

        int[] result = findMinCost();

        int count = 0;
        List<int[]> finalRoute = new ArrayList<>();
        for(int i=2; i<n+1; i++) {
            int from = i;
            int to = result[i];
            count++;
            finalRoute.add(new int[] {from, to});
        }

        StringBuilder sb = new StringBuilder();
        sb.append(count).append('\n');
        for(int[] arr : finalRoute) sb.append(arr[0]).append(' ').append(arr[1]).append('\n');

        System.out.println(sb);
    }
    static final int INF = 15 * 1000;
    static int[] findMinCost() {
        /**
         * 다익스트라 + 역추적
         * 최단 거리를 구한 뒤, 해당 거리를 구성하는 경로를 복원하는 것이 필요
         */
        PriorityQueue<int[]> pq = new PriorityQueue<>((a, b) -> Integer.compare(a[1], b[1]));
        int[] costDp = new int[n+1];
        int[] prevDp = new int[n+1];
        Arrays.fill(costDp, INF);

        costDp[1] = 0;      // 슈퍼 컴퓨터
        prevDp[1] = -1;     // 슈퍼 컴퓨터를 시작점으로 경로를 구함
        pq.offer(new int[] {1, 0});

        while(!pq.isEmpty()) {
            int[] cur = pq.poll();
            int from = cur[0];
            int accCost = cur[1];

            if(costDp[from] < accCost) continue;

            for(int[] next : connections[from]) {
                int to = next[0];
                int cost = next[1];

                if(costDp[from] != INF && costDp[to] > accCost + cost) {
                    costDp[to] = accCost + cost;
                    prevDp[to] = from;
                    pq.offer(new int[] {to, costDp[to]});
                }
            }
        }

        return prevDp;
    }
}
