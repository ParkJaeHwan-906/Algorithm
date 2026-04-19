package apr.week2.boj;

import java.util.*;
import java.io.*;

public class 주식회사승범이네_박재환 {
    static BufferedReader br;
    static StringBuilder sb;
    public static void main(String[] args) throws IOException {
        br = new BufferedReader(new InputStreamReader(System.in));
        sb = new StringBuilder();
        init();
        br.close();
        System.out.print(sb);
    }
    static final int UPDATE = 1;
    static final int QUERY = 2;

    static StringTokenizer st;
    static int n, m;
    static List<Integer>[] connections;
    static int root;
    static int[] in;
    static int[] out;
    static long[] tree;
    static long[] lazy;
    static void init() throws IOException {
        st = new StringTokenizer(br.readLine().trim());
        n = Integer.parseInt(st.nextToken());       // 직원 수
        m = Integer.parseInt(st.nextToken());       // 명령 수

        connections = new List[n];
        for(int i = 0; i < n; i++) connections[i] = new ArrayList<>();
        st = new StringTokenizer(br.readLine().trim());
        for(int i = 0; i < n; i++) {
            int parent = Integer.parseInt(st.nextToken());
            if(parent == -1) root = i;
            else connections[parent - 1].add(i);
        }

        flat();
        tree = new long[4 * n];
        lazy = new long[4 * n];
        while(m-- > 0) {
            st = new StringTokenizer(br.readLine().trim());
            int cmd = Integer.parseInt(st.nextToken());

            if(cmd == UPDATE) {
                int i = Integer.parseInt(st.nextToken()) - 1;
                long w = Long.parseLong(st.nextToken());
                update(1, 1, order, in[i], out[i], w);
            } else if(cmd == QUERY) {
                int i = Integer.parseInt(st.nextToken()) - 1;
                long result = query(1, 1, order, in[i], in[i]);
                sb.append(result).append('\n');
            }
        }
    }
    static int order;
    static void flat() {
        order = 0;
        in = new int[n];
        out = new int[n];

        makeInOut(root);
    }
    static void makeInOut(int cur) {
        in[cur] = ++order;

        for(int next : connections[cur]) {
            makeInOut(next);
        }

        out[cur] = order;
    }

    static void push(int id, int l, int r) {
        if(lazy[id] == 0 || l == r) return;

        long temp = lazy[id];
        int mid = l + (r - l) / 2;

        tree[2 * id] += (temp * (mid - l + 1));
        lazy[2 * id] += temp;
        tree[2 * id + 1] += (temp * (r - mid));
        lazy[2 * id + 1] += temp;

        lazy[id] = 0;
    }

    static void update(int id, int l, int r, int s, int e, long v) {
        if(e < l || s > r) return;
        if(s <= l && e >= r) {
            tree[id] += (v * (r - l + 1));
            lazy[id] += v;
            return;
        }
        push(id, l, r);
        int mid = l + (r - l) / 2;
        update(2 * id, l, mid, s, e, v);
        update(2 * id + 1, mid + 1, r, s, e, v);
        tree[id] = tree[2 * id] + tree[2 * id + 1];
    }

    static long query(int id, int l, int r, int s, int e) {
        if(e < l || s > r) return 0;
        push(id, l, r);
        if(s <= l && e >= r) return tree[id];
        int mid = l + (r - l) / 2;
        return query(2 * id, l, mid, s, e)
                + query(2 * id + 1, mid + 1, r, s, e);
    }
}
