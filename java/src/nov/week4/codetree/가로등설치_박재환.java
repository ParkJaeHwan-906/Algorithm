package nov.week4.codetree;

import java.util.*;
import java.io.*;

public class 가로등설치_박재환 {
    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        System.out.print(init(br));
        br.close();
    }

    static final int INIT = 100;
    static final int ADD = 200;
    static final int DEL = 300;
    static final int CALC = 400;

    static class Light {
        int id;
        int loc;
        Light prev;
        Light next;
        Light(int id, int loc, Light prev, Light next) {
            this.id = id;
            this.loc = loc;
            this.prev = prev;
            this.next = next;
        }
    }

    static class Between implements Comparable<Between> {
        int lId, rId;
        int dist;
        Between(int lId, int rId, int dist) {
            this.lId = lId;
            this.rId = rId;
            this.dist = dist;
        }
        @Override
        public int compareTo(Between o) {
            if(o.dist != this.dist)
                return Integer.compare(o.dist, this.dist);
            return Integer.compare(lights.get(this.lId).loc, lights.get(o.lId).loc);
        }
    }

    static final Light DUMMY = new Light(-1, -1, null, null);

    static int n, m;
    static List<Light> lights;
    static Light first, last;
    static PriorityQueue<Between> pq;
    static String init(BufferedReader br) throws IOException {
        StringTokenizer st;
        StringBuilder sb = new StringBuilder();
        int q = Integer.parseInt(br.readLine().trim());
        while(q-- > 0) {
            st = new StringTokenizer(br.readLine().trim());
            int type = Integer.parseInt(st.nextToken());
            if(type == INIT) {
                initSolution(st);
            }
            else if(type == ADD) {
                add();
            }
            else if(type == DEL) {
                del(st);
            }
            else if(type == CALC) {
                sb.append(query()).append('\n');
            }
        }
        return sb.toString();
    }

    static void initSolution(StringTokenizer st) {
        n = Integer.parseInt(st.nextToken());
        m = Integer.parseInt(st.nextToken());

        lights = new ArrayList<>();
        pq = new PriorityQueue<>();

        lights.add(DUMMY);
        for(int i = 1; i <= m; i++) {
            int loc = Integer.parseInt(st.nextToken());
            Light cur = new Light(i, loc, null, null);
            if(i > 1) {
                Light prev = lights.get(i - 1);
                prev.next = cur;
                cur.prev = prev;

                int dist = cur.loc - prev.loc;
                pq.offer(new Between(prev.id, cur.id, dist));
            }
            if(i == 1)
                first = cur;
            if(i == m)
                last = cur;
            lights.add(cur);
        }
    }

    static void add() {
        lazyPropagation();

        if(pq.isEmpty()) {
            return;
        }

        Between b = pq.poll();
        Light left = lights.get(b.lId);
        Light right = lights.get(b.rId);

        int newLoc = (left.loc + right.loc + 1) / 2;
        Light light = new Light(++m, newLoc, left, right);
        left.next = light;
        right.prev = light;
        lights.add(light);
        pq.offer(new Between(left.id, light.id, light.loc - left.loc));
        pq.offer(new Between(light.id, right.id, right.loc - light.loc));
    }

    static void del(StringTokenizer st) {
        int id = Integer.parseInt(st.nextToken());
        Light cur = lights.get(id);
        Light left = cur.prev;
        Light right = cur.next;

        if(cur == first)
            first = right;
        if(cur == last)
            last = left;

        cur = DUMMY;
        lights.set(id, cur);

        if(left != null)
            left.next = right;
        if(right != null)
            right.prev = left;
        if(left != null && right != null) {
            pq.offer(new Between(left.id, right.id, right.loc - left.loc));
        }
    }

    static long query() {
        lazyPropagation();
        long left = 2L * (first.loc - 1);
        long right = 2L * (n - last.loc);
        int middle = pq.isEmpty()
                ? 0 : pq.peek().dist;
        return Math.max(Math.max(left, right), middle);
    }

    static void lazyPropagation() {
        while(!pq.isEmpty()) {
            Between b = pq.peek();
            Light left = lights.get(b.lId);
            Light right = lights.get(b.rId);
            if (left != DUMMY && right != DUMMY
                    && left.next == right && right.prev == left) {
                break;
            }
            pq.poll();
        }
    }
}
