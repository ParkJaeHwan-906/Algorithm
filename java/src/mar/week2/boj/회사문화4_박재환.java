package mar.week2.boj;

import java.util.*;
import java.io.*;

public class 회사문화4_박재환 {
    static BufferedReader br;
    public static void main(String[] args) throws IOException {
        br = new BufferedReader(new InputStreamReader(System.in));
        init();
        br.close();
    }
    static StringTokenizer st;
    static int n, m;
    static int[] parents;
    static List<Integer>[] connections;
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
    static int[] treeDown;
    static int[] treeUp;
    static int[] lazy;
    static String solution() throws IOException {
        order = 0;
        in = new int[n+1];
        out = new int[n+1];

        getConnections();
        convertId(1, -1);

        treeDown = new int[4*n];
        treeUp = new int[4*n];
        lazy = new int[4*n];
        boolean down = true;
        StringBuilder sb = new StringBuilder();
        while(m-- > 0) {
            st = new StringTokenizer(br.readLine().trim());
            int cmd = Integer.parseInt(st.nextToken());

            if(cmd == 1) {
                int i = Integer.parseInt(st.nextToken());
                int w = Integer.parseInt(st.nextToken());
                if(down) update(1, 1, n, in[i], out[i], w);
                else update(1, 1, n, in[i], w);
            } else if(cmd == 2) {
                int i = Integer.parseInt(st.nextToken());
                sb.append(query(1, 1, n, in[i], in[i])
                        + query2(1, 1, n, in[i], out[i])).append('\n');
            } else if(cmd == 3) {
                down = !down;
            }
        }
        return sb.toString();
    }
    static void getConnections() {
        connections = new List[n+1];
        for(int i=1; i<n+1;) connections[i++] = new ArrayList<>();
        // 1번은 항상 사장
        for(int cur=2; cur<n+1; cur++) {
            int prev = parents[cur];
            connections[prev].add(cur);
        }
    }
    static void convertId(int cur, int prev) {
        in[cur] = ++order;
        for(int next : connections[cur]) {
            if(next == prev) continue;
            convertId(next, cur);
        }
        out[cur] = order;
    }
    static void update(int id, int l, int r, int s, int e, int v) {
        push(id, l, r);
        if(r < s || l > e) return;
        if(l >= s && r <= e) {
            treeDown[id] += (r-l+1) * v;
            lazy[id] += v;
            return;
        }
        int mid = l + (r - l)/2;
        update(2*id, l, mid, s, e, v);
        update(2*id+1, mid+1, r, s, e, v);
        treeDown[id] = treeDown[2*id] + treeDown[2*id+1];
    }
    static void update(int id, int l, int r, int node, int v) {
        if(r < node || l > node) return;
        if(l == r) {
            treeUp[id] += v;
            return;
        }
        int mid = l + (r - l)/2;
        update(2*id, l, mid, node, v);
        update(2*id+1, mid+1, r, node, v);
        treeUp[id] = treeUp[2*id] + treeUp[2*id+1];
    }
    static int query(int id, int l ,int r, int s, int e) {
        push(id, l, r);
        if(r < s || l > e) return 0;
        if(l >= s && r <= e) return treeDown[id];
        int mid = l + (r - l)/2;
        return query(2*id, l, mid, s, e) + query(2*id+1, mid+1, r, s, e);
    }
    static int query2(int id, int l ,int r, int s, int e) {
        if(r < s || l > e) return 0;
        if(l >= s && r <= e) return treeUp[id];
        int mid = l + (r - l)/2;
        return query2(2*id, l, mid, s, e) + query2(2*id+1, mid+1, r, s, e);
    }
    static void push(int id, int l, int r) {
        if(lazy[id] == 0 || (l == r)) return;

        int temp = lazy[id];
        int mid = l + (r - l)/2;
        treeDown[2*id] += (mid-l+1) * temp;
        lazy[2*id] += temp;
        treeDown[2*id+1] += (r-mid) * temp;
        lazy[2*id+1] += temp;
        lazy[id] = 0;
    }
}
