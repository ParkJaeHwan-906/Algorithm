package nov.week2.swea;

import java.util.*;
import java.io.*;

public class 최소스패닝트리_박재환 {
    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringBuilder sb = new StringBuilder();
        int tc = Integer.parseInt(br.readLine().trim());
        for(int i = 1; i <= tc; i++) {
            sb.append('#').append(i).append(' ').append(init(br)).append('\n');
        }
        System.out.print(sb);
        br.close();
    }

    static int v, e;
    static long init(BufferedReader br) throws IOException {
        StringTokenizer st = new StringTokenizer(br.readLine().trim());
        v = Integer.parseInt(st.nextToken());
        e = Integer.parseInt(st.nextToken());
        PriorityQueue<int[]> pq = new PriorityQueue<>((a, b) -> Integer.compare(a[2], b[2]));
        for(int i = 0; i < e; i++) {
            st = new StringTokenizer(br.readLine().trim());
            int a = Integer.parseInt(st.nextToken());
            int b = Integer.parseInt(st.nextToken());
            int c = Integer.parseInt(st.nextToken());
            pq.offer(new int[]{a, b, c});
        }
        return solution(pq);
    }

    static int[] parents;
    static long solution(PriorityQueue<int[]> pq) {
        set();
        long totalCost = 0L;
        int edges = 0;
        while(!pq.isEmpty()) {
            int[] cur = pq.poll();
            int a = cur[0];
            int b = cur[1];
            int c = cur[2];
            if(union(a, b)) {
                totalCost += c;
                if(++edges == v - 1)
                    break;
            }
        }
        return totalCost;
    }

    static void set() {
        parents = new int[v + 1];
        for(int i = 0; i <= v; i++)
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
}
