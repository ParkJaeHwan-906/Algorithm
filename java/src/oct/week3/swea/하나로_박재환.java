package oct.week3.swea;

import java.util.*;
import java.io.*;

public class 하나로_박재환 {
    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringBuilder sb = new StringBuilder();
        int t = Integer.parseInt(br.readLine().trim());
        for(int tc = 1; tc <= t; tc++)
            sb.append('#').append(tc).append(' ')
                    .append(init(br)).append('\n');
        System.out.print(sb);
        br.close();
    }

    static class Loc {
        double x, y;
        Loc(double x, double y) {
            this.x = x;
            this.y = y;
        }
    }

    static int n;
    static Loc[] locs;
    static double tax;
    static long init(BufferedReader br) throws IOException {
        StringTokenizer st;
        n = Integer.parseInt(br.readLine().trim());
        locs = new Loc[n];
        st = new StringTokenizer(br.readLine().trim());
        for(int i = 0; i < n; i++) {
            locs[i] = new Loc(Double.parseDouble(st.nextToken()), 0);
        }
        st = new StringTokenizer(br.readLine().trim());
        for(int i = 0; i < n; i++) {
            locs[i].y = Double.parseDouble(st.nextToken());
        }
        tax = Double.parseDouble(br.readLine().trim());
        return (long) Math.round(solution());
    }

    static class Connection implements Comparable<Connection> {
        int a, b;
        double dist;
        Connection(int a, int b, double dist) {
            this.a = a;
            this.b = b;
            this.dist = dist;
        }
        @Override
        public int compareTo(Connection o) {
            return Double.compare(this.dist, o.dist);
        }
    }

    static int[] parents;
    static double solution() {
        PriorityQueue<Connection> pq = new PriorityQueue<>();
        for(int i = 0; i < n; i++) {
            for(int j = i + 1; j < n; j++) {
                pq.offer(new Connection(i, j, getDist(locs[i], locs[j])));
            }
        }
        make();
        int edges = 0;
        double totalDist = 0;
        while(!pq.isEmpty()) {
            Connection conn = pq.poll();
            if(union(conn.a, conn.b)) {
                totalDist += conn.dist;
                if(++edges == n - 1)
                    break;
            }
        }
        return tax * totalDist;
    }

    static void make() {
        parents = new int[n];
        for(int i = 0; i < n; i++)
            parents[i] = i;
    }

    static int find(int a) {
        if(parents[a] == a)
            return a;
        return parents[a] = find(parents[a]);
    }

    static boolean union(int a, int b) {
        int rootA = find(a);
        int rootB = find(b);
        if(rootA == rootB)
            return false;
        parents[rootA] = rootB;
        return true;
    }

    static double getDist(Loc a, Loc b) {
        return Math.pow((a.x - b.x), 2) + Math.pow((a.y - b.y), 2);
    }
}
