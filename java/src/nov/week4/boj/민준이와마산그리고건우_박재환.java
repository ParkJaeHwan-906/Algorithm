package nov.week4.boj;

import java.util.*;
import java.io.*;

public class 민준이와마산그리고건우_박재환 {
    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        System.out.print(init(br));
        br.close();
    }

    static final int INF = Integer.MAX_VALUE / 2;

    static int v, e, p;
    static List<int[]>[] connections;
    static String init(BufferedReader br) throws IOException {
        StringTokenizer st;
        st = new StringTokenizer(br.readLine().trim());
        v = Integer.parseInt(st.nextToken());
        e = Integer.parseInt(st.nextToken());
        p = Integer.parseInt(st.nextToken());
        connections = new List[v + 1];
        for(int i = 1; i <= v; i++)
            connections[i] = new ArrayList<>();
        for(int i = 0; i < e; i++) {
            st = new StringTokenizer(br.readLine().trim());
            int a = Integer.parseInt(st.nextToken());
            int b = Integer.parseInt(st.nextToken());
            int c = Integer.parseInt(st.nextToken());
            connections[a].add(new int[] {b, c});
            connections[b].add(new int[] {a, c});
        }

        return solution() ? "SAVE HIM" : "GOOD BYE";
    }

    static boolean solution() {
        int[] costs = new int[v + 1];
        List<Integer>[] prev = new List[v + 1];
        for(int i = 1; i <= v; i++)
            prev[i] = new ArrayList<>();

        PriorityQueue<int[]> pq = new PriorityQueue<>((a, b) -> Integer.compare(a[1], b[1]));
        Arrays.fill(costs, INF);

        pq.offer(new int[] {1, 0});
        costs[1] = 0;

        while(!pq.isEmpty()) {
            int[] cur = pq.poll();
            int loc = cur[0];
            int cost = cur[1];

            if(costs[loc] < cost)
                continue;

            for(int[] nextArr : connections[loc]) {
                int nextLoc = nextArr[0];
                int nextCost = nextArr[1];

                if(costs[nextLoc] > cost + nextCost) {
                    costs[nextLoc] = cost + nextCost;
                    prev[nextLoc].clear();
                    prev[nextLoc].add(loc);
                    pq.offer(new int[] {nextLoc, costs[nextLoc]});
                } else if(costs[nextLoc] == cost + nextCost) {
                    prev[nextLoc].add(loc);
                }
            }
        }

        return isAvailable(v, prev);
    }

    static boolean isAvailable(int cur, List<Integer>[] prev) {
        if(cur == p)
            return true;

        for(int i : prev[cur]) {
            if(isAvailable(i, prev))
                return true;
        }
        return false;
    }
}
