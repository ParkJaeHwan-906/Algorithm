package sep.week3.jungol;

import java.util.*;
import java.io.*;

public class 구간의합1_박재환 {
    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        init(br);
        br.close();
    }

    static int n, m;
    static int[] arr;
    static long[] tree;
    static void init(BufferedReader br) throws IOException {
        StringTokenizer st;
        StringBuilder sb = new StringBuilder();
        n = Integer.parseInt(br.readLine().trim());
        arr = new int[n];
        st = new StringTokenizer(br.readLine().trim());
        for(int i = 0; i < n; i++) {
            arr[i] = Integer.parseInt(st.nextToken());
        }

        setTree();
        m = Integer.parseInt(br.readLine().trim());
        for(int i = 0; i < m; i++) {
            st = new StringTokenizer(br.readLine().trim());
            int type = Integer.parseInt(st.nextToken());
            if(type == 1) {
                int idx = Integer.parseInt(st.nextToken()) - 1;
                int nd = Integer.parseInt(st.nextToken());
                arr[idx] = nd;
                update(1, 0, n - 1, idx, nd);
            } else if(type == 2) {
                int s = Integer.parseInt(st.nextToken()) - 1;
                int e = Integer.parseInt(st.nextToken()) - 1;
                sb.append(query(1, 0, n - 1, s, e)).append('\n');
            }
        }
        System.out.print(sb);
    }

    static void setTree() {
        tree = new long[4 * n];
        for(int i = 0; i < n; i++) {
            update(1, 0, n - 1, i, arr[i]);
        }
    }

    static void update(int id, int l, int r, int target, int value) {
        if(target < l || target > r) {
            return;
        }
        if(l == r) {
            tree[id] = value;
            return;
        }
        int mid = l + (r - l) / 2;
        update(2 * id, l, mid, target, value);
        update(2 * id + 1, mid + 1, r, target, value);
        tree[id] = tree[2 * id] + tree[2 * id + 1];
    }

    static long query(int id, int l, int r, int s, int e) {
        if(e < l || s > r) {
            return 0L;
        }
        if(s <= l && e >= r) {
            return tree[id];
        }
        int mid = l + (r - l) / 2;
        return query(2 * id, l, mid, s, e) +  query(2 * id + 1, mid + 1, r, s, e);
    }
}
