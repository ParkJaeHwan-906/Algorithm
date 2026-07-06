package jul.week1.codetree;

import java.util.*;
import java.io.*;

/**
 * [풀이 시간]
 * 00:44:03
 * AI 사용 여부 X
 */
public class 코드트리투어_박재환 {
    public static void main(String[] args) throws IOException{
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        init(br);
        br.close();
    }

    static final int SET = 100;
    static final int ADD = 200;
    static final int DEL = 300;
    static final int QUERY = 400;
    static final int RESET = 500;

    static class Trip implements Comparable<Trip> {
        int id;
        int revenue;
        int dest;

        int cost;

        boolean del;

        Trip(int id, int revenue, int dest, int cost) {
            this.id = id;
            this.revenue = revenue;
            this.dest = dest;

            this.cost = cost;

            this.del = false;
        }

        int getProfit() {
            return this.revenue - this.cost;
        }

        public int compareTo(Trip o) {
            int oR = o.getProfit();
            int tR = this.getProfit();

            if(oR != tR) return Integer.compare(oR, tR);
            return Integer.compare(this.id, o.id);
        }
    }

    static int n, m;
    static List<int[]>[] connections;
    static int[] costArr;
    static Map<Integer, Trip> trips;
    static PriorityQueue<Trip> priorityTrips;
    static void init(BufferedReader br) throws IOException {
        StringTokenizer st;
        trips = new HashMap<>();
        priorityTrips = new PriorityQueue<>();
        StringBuilder sb = new StringBuilder();

        int q = Integer.parseInt(br.readLine().trim());
        while(q-- > 0) {
            st = new StringTokenizer(br.readLine().trim());
            int type = Integer.parseInt(st.nextToken());
            if(type == SET) { set(st); }
            else if(type == ADD) { add(st); }
            else if(type == DEL) { del(st); }
            else if(type == QUERY) {
                int result = query();
                sb.append(result).append('\n');
            }
            else if(type == RESET) { reset(st); }
        }

        System.out.println(sb);
    }

    static void set(StringTokenizer st) {
        n = Integer.parseInt(st.nextToken());
        m = Integer.parseInt(st.nextToken());

        connections = new List[n];      // 0 ~ (n - 1)
        for(int i = 0; i < n;) connections[i++] = new ArrayList<>();

        for(int i = 0; i < m; i++) {
            int a = Integer.parseInt(st.nextToken());
            int b = Integer.parseInt(st.nextToken());
            int cost = Integer.parseInt(st.nextToken());

            connections[a].add(new int[] {b, cost});
            connections[b].add(new int[] {a, cost});
        }

        getCostArr(0);      // 시작 위치는 0
    }

    static void add(StringTokenizer st) {
        int id = Integer.parseInt(st.nextToken());
        int revenue = Integer.parseInt(st.nextToken());
        int dest = Integer.parseInt(st.nextToken());
        Trip trip = new Trip(id, revenue, dest, costArr[dest]);
        trips.put(id, trip);
        priorityTrips.add(trip);
    }

    static void del(StringTokenizer st) {
        int id = Integer.parseInt(st.nextToken());
        if(!trips.containsKey(id)) return;
        Trip trip = trips.get(id);
        trips.remove(id);
        trip.del = true;
    }

    static int query() {
        while(!priorityTrips.isEmpty() && (priorityTrips.peek().del || priorityTrips.peek().getProfit() < 0)) priorityTrips.poll();

        int id = priorityTrips.isEmpty() ? -1 : priorityTrips.poll().id;
        if(id != -1) trips.remove(id);

        return id;
    }

    static void reset(StringTokenizer st) {
        int s = Integer.parseInt(st.nextToken());
        getCostArr(s);

        priorityTrips.clear();
        for(Trip t : trips.values()) {
            t.cost = costArr[t.dest];
            priorityTrips.add(t);
        }
    }

    static final int INF = Integer.MAX_VALUE;

    static void getCostArr(int s) {
        costArr = new int[n];
        Arrays.fill(costArr, INF);
        PriorityQueue<int[]> pq = new PriorityQueue<>((a, b) -> Integer.compare(a[1], b[1]));

        costArr[s] = 0;
        pq.offer(new int[] {s, 0});
        while(!pq.isEmpty()) {
            int[] cur = pq.poll();
            int from = cur[0];
            int accCost = cur[1];

            if(costArr[from] < accCost) continue;

            for(int[] conn : connections[from]) {
                int to = conn[0];
                int cost = conn[1];

                if(costArr[to] > accCost + cost) {
                    costArr[to] = accCost + cost;
                    pq.offer(new int[] {to, costArr[to]});
                }
            }
        }
    }
}
