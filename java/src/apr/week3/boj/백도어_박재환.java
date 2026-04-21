package apr.week3.boj;

import java.util.*;
import java.io.*;

public class 백도어_박재환 {
    static BufferedReader br;
    public static void main(String[] args) throws IOException {
        br = new BufferedReader(new InputStreamReader(System.in));
        init();
        br.close();
    }
    static class Edge implements Comparable<Edge> {
        int to;
        long cost;

        Edge(int to, long cost) {
            this.to = to;
            this.cost = cost;
        }

        public int compareTo(Edge o) {
            return Long.compare(this.cost, o.cost);
        }
    }
    static StringTokenizer st;
    static int n, m;
    static boolean[] visible;
    static List<Edge>[] connections;
    static void init() throws IOException {
        st = new StringTokenizer(br.readLine().trim());
        n = Integer.parseInt(st.nextToken());       // 분기점의 수
        m = Integer.parseInt(st.nextToken());       // 분기점들을 잇는 길의 수

        visible = new boolean[n];
        st = new StringTokenizer(br.readLine().trim());
        for(int i = 0; i < n; i++) {
            visible[i] = Integer.parseInt(st.nextToken()) == 1;     // 1 : 보임, 0 안보임
        }

        connections = new List[n];
        for(int i = 0; i < n; i++) connections[i] = new ArrayList<>();
        for(int i = 0; i < m; i++) {
            st = new StringTokenizer(br.readLine().trim());
            int from = Integer.parseInt(st.nextToken());
            int to = Integer.parseInt(st.nextToken());
            long cost = Long.parseLong(st.nextToken());

            connections[from].add(new Edge(to, cost));
            connections[to].add(new Edge(from, cost));
        }

        System.out.println(solution());
    }

    static final long INF = Long.MAX_VALUE;
    static long solution() {
        /**
         * 출발 : 0
         * 도착 : n - 1
         *
         * visible[i] == true : 지나갈 수 없음
         */

        PriorityQueue<Edge> pq = new PriorityQueue<>();
        long[] costs = new long[n];
        Arrays.fill(costs, INF);

        pq.offer(new Edge(0, 0));
        costs[0] = 0;

        while(!pq.isEmpty()) {
            Edge cur = pq.poll();
            int from = cur.to;
            long accCost = cur.cost;

            if(costs[from] < accCost) continue;
            for(Edge connection : connections[from]) {
                int to = connection.to;
                long cost = connection.cost;
                if(to != n - 1 && visible[to]) continue;
                if(costs[to] > accCost + cost) {
                    costs[to] = accCost + cost;
                    pq.offer(new Edge(to, costs[to]));
                }
            }
        }

        return costs[n - 1] == INF ? -1 : costs[n - 1];
    }
}
