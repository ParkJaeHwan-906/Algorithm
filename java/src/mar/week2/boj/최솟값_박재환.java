package mar.week2.boj;

import java.util.*;
import java.io.*;

public class 최솟값_박재환 {
    static BufferedReader br;
    public static void main(String[] args) throws IOException {
        br = new BufferedReader(new InputStreamReader(System.in));
        System.out.print(init());
        br.close();
    }
    static StringTokenizer st;
    static int n, m;
    static long[] arr;
    static long[] minTree;
    static String init() throws IOException {
        st = new StringTokenizer(br.readLine().trim());
        n = Integer.parseInt(st.nextToken());
        m = Integer.parseInt(st.nextToken());

        arr = new long[n+1];
        for(int i=1; i<n+1; i++) arr[i] = Integer.parseInt(br.readLine().trim());

        minTree = new long[4*n];
        buildTree(1, 1, n);
        StringBuilder sb = new StringBuilder();
        while(m-- > 0) {
            st = new StringTokenizer(br.readLine().trim());
            int a = Integer.parseInt(st.nextToken());
            int b = Integer.parseInt(st.nextToken());
            sb.append(query(1, 1, n, a, b)).append('\n');
        }
        return sb.toString();
    }
    static long buildTree(int id, int l, int r) {
        if(l == r) return minTree[id] = arr[l];

        int mid = l + (r - l)/2;
        return  minTree[id] = Math.min(
                buildTree(2*id, l, mid),
                buildTree(2*id+1, mid+1, r)
        );
    }
    static final long MAX = 1_000_000_000;
    static long query(int id, int l, int r, int s, int e) {
        if(r < s || l > e) return MAX;
        if(l >= s && r <= e) return minTree[id];

        int mid = l + (r - l)/2;
        return Math.min(
                query(2*id, l, mid, s, e),
                query(2*id+1, mid+1, r, s, e)
        );
    }
}
