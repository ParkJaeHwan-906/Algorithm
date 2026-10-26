package oct.week3.swea;

import java.util.*;
import java.io.*;

public class 트리바꾸기_박재환 {
    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringBuilder sb = new StringBuilder();
        int t = Integer.parseInt(br.readLine().trim());
        for(int i = 1; i <= t; i++) {
            sb.append(init(br)).append('\n');
        }
        System.out.print(sb);
        br.close();
    }

    static int n;
    static List<Integer>[] connections;
    static int[] edges;
    static int init(BufferedReader br) throws IOException {
        StringTokenizer st;
        n = Integer.parseInt(br.readLine().trim());
        connections = new List[n + 1];
        edges = new int[n + 1];
        for(int i = 0; i <= n; i++)
            connections[i] = new ArrayList<>();
        for(int i = 0; i < n - 1; i++) {
            st = new StringTokenizer(br.readLine().trim());
            int a = Integer.parseInt(st.nextToken());
            int b = Integer.parseInt(st.nextToken());
            connections[a].add(b);
            connections[b].add(a);
            edges[a]++;
            edges[b]++;
        }
        return n == 1
                ? 0 : solution();
    }

    static int solution() {
        int totalCount = 0;
        for(int i : edges) {
            if(i > 2) {
                totalCount += (i - 2);
            }
        }
        return totalCount;
    }
}
