package jul.week1.codetree;

import java.util.*;
import java.io.*;

/*
  문제풀이 시간: 중간중간 시간날 때 푼거라서 시간 X
  AI 사용 여부: O Travel에 거리와 이익을 저장하려니까 로직이 어려워져서 Travel 객체와 dist[] 배열 분리.
 */
public class 코드트리투어_박서희 {
    static StringBuilder sb = new StringBuilder();
    static final int MAX_ID = 30002;

    static int n, m;
    static ArrayList<int[]>[] edges;
    static int[] dijkstraDist;
    static int startNode = 0;

    static PriorityQueue<Travel> pq = new PriorityQueue<>();
    static Map<Integer, Travel> productMap = new HashMap<>();
    static boolean[] isCancelled = new boolean[MAX_ID];

    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

        int Q = Integer.parseInt(br.readLine());
        while (Q-- > 0) {
            StringTokenizer st = new StringTokenizer(br.readLine());
            int command = Integer.parseInt(st.nextToken());

            if (command == 100) {
                build(st);
            } else if (command == 200) {
                create(st);
            } else if (command == 300) {
                cancel(st);
            } else if (command == 400) {
                sell();
            } else if (command == 500) {
                change(st);
            }
        }

        System.out.print(sb.toString());
        br.close();
    }

    public static void build(StringTokenizer st) {
        n = Integer.parseInt(st.nextToken());
        m = Integer.parseInt(st.nextToken());

        dijkstraDist = new int[n];
        edges = new ArrayList[n];
        for (int i = 0; i < n; i++) {
            edges[i] = new ArrayList<>();
        }

        for (int i = 0; i < m; i++) {
            int u = Integer.parseInt(st.nextToken());
            int v = Integer.parseInt(st.nextToken());
            int w = Integer.parseInt(st.nextToken());
            edges[u].add(new int[]{v, w});
            edges[v].add(new int[]{u, w});
        }

        dijkstra(startNode);
    }

    public static void create(StringTokenizer st) {
        int id = Integer.parseInt(st.nextToken());
        int revenue = Integer.parseInt(st.nextToken());
        int dest = Integer.parseInt(st.nextToken());

        Travel t = new Travel(id, revenue, dest);
        productMap.put(id, t);
        pq.add(t);
    }

    public static void cancel(StringTokenizer st) {
        int id = Integer.parseInt(st.nextToken());
        if (productMap.containsKey(id)) {
            isCancelled[id] = true;
            productMap.remove(id);
        }
    }

    public static void sell() {
        while (!pq.isEmpty()) {
            Travel current = pq.peek();

            if (isCancelled[current.id]) {
                pq.poll();
                continue;
            }

            if (current.getBenefit() < 0) break;

            pq.poll();
            productMap.remove(current.id);
            sb.append(current.id).append("\n");
            return;
        }

        sb.append(-1).append("\n");
    }


    public static void change(StringTokenizer st) {
        startNode = Integer.parseInt(st.nextToken());

        dijkstra(startNode);

        PriorityQueue<Travel> nextPQ = new PriorityQueue<>();
        while (!pq.isEmpty()) {
            Travel t = pq.poll();
            if (!isCancelled[t.id]) {
                nextPQ.add(t);
            }
        }
        pq = nextPQ;
    }

    public static void dijkstra(int start) {
        Arrays.fill(dijkstraDist, Integer.MAX_VALUE);
        PriorityQueue<int[]> pqueue = new PriorityQueue<>(Comparator.comparingInt(o -> o[1]));

        dijkstraDist[start] = 0;
        pqueue.add(new int[]{start, 0});

        while (!pqueue.isEmpty()) {
            int[] cur = pqueue.poll();
            int now = cur[0], weight = cur[1];

            if (weight > dijkstraDist[now]) continue;

            for (int[] next : edges[now]) {
                if (dijkstraDist[next[0]] > dijkstraDist[now] + next[1]) {
                    dijkstraDist[next[0]] = dijkstraDist[now] + next[1];
                    pqueue.add(new int[]{next[0], dijkstraDist[next[0]]});
                }
            }
        }
    }

    static class Travel implements Comparable<Travel> {
        int id;
        int revenue;
        int destination;

        public Travel(int id, int revenue, int destination) {
            this.id = id;
            this.revenue = revenue;
            this.destination = destination;
        }

        public int getBenefit() {
            if (dijkstraDist[this.destination] == Integer.MAX_VALUE) return -1;
            return this.revenue - dijkstraDist[this.destination];
        }

        @Override
        public int compareTo(Travel o) {
            int myBenefit = this.getBenefit();
            int otherBenefit = o.getBenefit();

            if (myBenefit != otherBenefit)
                return Integer.compare(otherBenefit, myBenefit);
            return Integer.compare(this.id, o.id);
        }
    }
}
