package mar.week1.boj;

import java.util.*;
import java.io.*;

public class 탑_세그먼트트리_박재환 {
    static BufferedReader br;
    public static void main(String[] args) throws IOException {
        br = new BufferedReader(new InputStreamReader(System.in));
        init();
        br.close();
    }
    static StringTokenizer st;
    static int n;
    static int[] arr;
    static int[] result;
    static void init() throws IOException {
        n = Integer.parseInt(br.readLine().trim());

        arr = new int[n];
        result = new int[n];
        st = new StringTokenizer(br.readLine().trim());
        for(int i=0; i < n; i++) arr[i] = Integer.parseInt(st.nextToken());
        solution();
        StringBuilder sb = new StringBuilder();
        for(int i : result) sb.append(i).append(' ');
        System.out.println(sb);
    }
    static int[] tree;
    static void solution() {
        tree = new int[4*n];
        for(int i=0; i<n; i++) {
            int id = i == 0 ? -1 : query(1, 0, n-1, 0, i-1, arr[i]);
            result[i] = id + 1;
            update(1, 0, n-1, i, i, arr[i]);
        }
    }
    static void update(int id, int l, int r, int s, int e, int v) {
        if(r < s || l > e) return;
        if(l >= s && r <= e) {
            tree[id] = v;
            return;
        }

        int mid = l + (r-l)/2;
        update(2*id, l, mid, s, e, v);
        update(2*id+1, mid+1, r, s, e, v);
        tree[id] = Math.max(tree[2*id], tree[2*id+1]);
    }
    static int query(int id, int l, int r, int s, int e, int v) {
        if(r < s || l > e || tree[id] <= v) return -1;
        if(l == r) {
            return l;
        }

        int mid = l + (r-l)/2;

        int rId = query(2 * id + 1, mid + 1, r, s, e, v);
        if(rId != -1) return rId;
        int lId = query(2 * id, l, mid, s, e, v);
        return lId;
    }
}
