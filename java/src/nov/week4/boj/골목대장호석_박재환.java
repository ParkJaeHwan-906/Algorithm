package nov.week4.boj;

import java.util.*;
import java.io.*;

public class 골목대장호석_박재환 {
    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        System.out.print(init(br));
        br.close();
    }

    static int n, m;
    static long budget;
    static List<int[]>[] connections;
    static int start, end;
    static int init(BufferedReader br) throws IOException {
        StringTokenizer st;
        st = new StringTokenizer(br.readLine().trim());
        n = Integer.parseInt(st.nextToken());
        m = Integer.parseInt(st.nextToken());
        start = Integer.parseInt(st.nextToken());
        end = Integer.parseInt(st.nextToken());
        budget = Long.parseLong(st.nextToken());

        connections = new List[n + 1];
        for(int i = 1; i <= n; i++)
            connections[i] = new ArrayList<>();
        for(int i = 0; i < m; i++) {
            st = new StringTokenizer(br.readLine().trim());
            int a = Integer.parseInt(st.nextToken());
            int b = Integer.parseInt(st.nextToken());
            int c = Integer.parseInt(st.nextToken());
            connections[a].add(new int[]{b, c});
            connections[b].add(new int[]{a, c});
        }
        return solution();
    }

    static class State implements Comparable<State> {
        int cur;
        long acc;
        int max;
        State(int cur, long acc, int max) {
            this.cur = cur;
            this.acc = acc;
            this.max = max;
        }

        @Override
        public int compareTo(State o) {
            return Integer.compare(this.max, o.max);
        }
    }

    static int solution() {
        PriorityQueue<State> pq = new PriorityQueue<>();
        int[] state = new int[n + 1];
        Arrays.fill(state, Integer.MAX_VALUE);

        state[start] = 0;
        pq.offer(new State(start, 0L, 0));

        while(!pq.isEmpty()) {
            State cur = pq.poll();
            if(cur.cur == end) {
                return cur.max;
            }
            for(int[] next : connections[cur.cur]) {
                if(state[next[0]] > Math.max(cur.max, next[1])) {
                    if(cur.acc + next[1] <= budget) {
                        state[next[0]] = Math.max(cur.max, next[1]);
                        pq.offer(new State(next[0], cur.acc + next[1], Math.max(cur.max, next[1])));
                    }
                }
            }
        }
        return -1;
    }
}
