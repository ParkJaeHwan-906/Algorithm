package mar.week3.boj;

import java.util.*;
import java.io.*;

public class 회사문화1_박재환 {
    static BufferedReader br;
    public static void main(String[] args) throws IOException {
        br = new BufferedReader(new InputStreamReader(System.in));
        init();
        br.close();
    }
    static StringTokenizer st;
    static int n, m;
    static int[] parents;
    static void init() throws IOException {
        st = new StringTokenizer(br.readLine().trim());
        n = Integer.parseInt(st.nextToken());
        m = Integer.parseInt(st.nextToken());

        parents = new int[n+1];
        st = new StringTokenizer(br.readLine().trim());
        for(int i=1; i<n+1;) parents[i++] = Integer.parseInt(st.nextToken());

        System.out.println(solution());
    }
    static int order;
    static int[] in, out;
    static long[] tree, lazy;
    static List<Integer>[] connections;
    static String solution() throws IOException {
        connections = new List[n+1];
        for(int i=0; i<n+1;) connections[i++] = new ArrayList<>();
        for(int i=2; i<n+1; i++) {
            int id = i;
            int pId = parents[id];
            connections[pId].add(id);
        }
        order = 0;
        in = new int[n+1];
        out = new int[n+1];
        makeBound(1);

        tree = new long[4*order];
        lazy = new long[4*order];
        while(m-- > 0) {
            st = new StringTokenizer(br.readLine().trim());
            int id = Integer.parseInt(st.nextToken());
            long w = Long.parseLong(st.nextToken());

            update(1, 1, n, in[id], out[id], w);
        }

        StringBuilder sb = new StringBuilder();
        for(int i=1; i<n+1; i++) {
            sb.append(query(1, 1, n, in[i])).append(' ');
        }
        return sb.toString();
    }
    static void makeBound(int cur) {
        in[cur] = ++order;
        for(int next : connections[cur]) makeBound(next);
        out[cur] = order;
    }
    static void push(int id, int l, int r) {
        if(lazy[id] == 0 || l == r) return;

        int mid = l + (r - l)/2;
        tree[2*id] += ((mid - l + 1) * lazy[id]);
        lazy[2*id] += lazy[id];
        tree[2*id+1] += ((r - mid) * lazy[id]);
        lazy[2*id+1] += lazy[id];

        lazy[id] = 0;
    }
    static long query(int id, int l, int r, int node) {
        if(node < l || node > r) return 0;
        push(id, l, r);
        if(l == r) return tree[id];
        int mid = l + (r - l)/2;
        return query(2*id, l, mid, node) + query(2*id+1, mid+1, r, node);
    }
    static void update(int id, int l, int r, int s, int e, long v) {
        if(r < s || l > e) return;
        if(l >= s && r <= e) {
            tree[id] += ((r - l + 1) * v);
            lazy[id] += v;
            return;
        }
        push(id, l, r);
        int mid = l + (r - l)/2;
        update(2*id, l, mid, s, e, v);
        update(2*id+1, mid+1, r, s, e, v);
        tree[id] = tree[2*id] + tree[2*id+1];
    }
}
