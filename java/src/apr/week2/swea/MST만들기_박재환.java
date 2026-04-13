package apr.week2.swea;

import java.util.*;
import java.io.*;

public class MST만들기_박재환 {
    static BufferedReader br;
    static StringBuilder sb;
    public static void main(String[] args) throws IOException {
        br = new BufferedReader(new InputStreamReader(System.in));
        sb = new StringBuilder();
        int t = Integer.parseInt(br.readLine().trim());
        for(int i = 0; i < t; i++) {
            long[] result = init();
            sb.append(result[0]).append(' ').append(result[1]).append('\n');
        }
        br.close();
        System.out.println(sb);
    }
    static StringTokenizer st;
    static int n, m;
    static long[] edges;
    static long[] init() throws IOException {
        n = Integer.parseInt(br.readLine().trim());
        m = (n * (n - 1)) / 2;

        edges = new long[m];
        st = new StringTokenizer(br.readLine().trim());
        for(int i = 0; i < m; i++) {
            edges[i] = Long.parseLong(st.nextToken());
        }
        Arrays.sort(edges);
        long min = minMst();
        long max = maxMst();
        return new long[] {min, max};
    }
    static long maxMst() {
        long sum = 0;
        for (int i = 0; i < n - 1; i++) {
            int idx = i * (i + 1) / 2;
            sum += edges[idx];
        }
        return sum;
    }
    static long minMst() {
        long sum = 0;
        for(int i = 0; i < n - 1; i++) {
            sum += edges[i];
        }
        return sum;
    }
}
