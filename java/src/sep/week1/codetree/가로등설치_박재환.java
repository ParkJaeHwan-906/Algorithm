package sep.week1.codetree;

import java.util.*;
import java.io.*;

public class 가로등설치_박재환 {
    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        init(br);
        br.close();
    }

    static class Lamp {
        int loc;        // 설치 위치
        Lamp prev;
        Lamp next;
        Lamp(int loc, Lamp prev, Lamp next) {
            this.loc = loc;
            this.prev = prev;
            this.next = next;
        }
    }

    static class Between implements Comparable<Between> {
        Lamp a;
        Lamp b;
        final int dist;
        final int start;
        Between(Lamp a, Lamp b) {
            this.a = a;
            this.b = b;
            this.dist = b.loc - a.loc;
            this.start = a.loc;
        }
        @Override
        public int compareTo(Between o) {
            if(this.dist != o.dist) return Integer.compare(o.dist, this.dist);
            return Integer.compare(this.start, o.start);
        }
    }

    static final int SET = 100;
    static final int ADD = 200;
    static final int DEL = 300;
    static final int QUERY = 400;

    static final Lamp DUMMY = new Lamp(-1, null, null);

    static int n, m;
    static Lamp first, last;
    static List<Lamp> lamps;
    static PriorityQueue<Between> betweens;
    static void init(BufferedReader br) throws IOException {
        StringTokenizer st;
        StringBuilder sb = new StringBuilder();
        int q = Integer.parseInt(br.readLine().trim());
        while(q-- > 0) {
            st = new StringTokenizer(br.readLine().trim());
            int type = Integer.parseInt(st.nextToken());
            if(type == SET) {
                set(st);
            }
            else if(type == ADD) {
                add();
            }
            else if(type == DEL) {
                del(st);
            }
            else if(type == QUERY) {
                long result = query();
                sb.append(result).append('\n');
            }
        }
        System.out.print(sb);
    }

    static void set(StringTokenizer st) {
        n = Integer.parseInt(st.nextToken());
        m = Integer.parseInt(st.nextToken());
        lamps = new ArrayList<>();
        betweens = new PriorityQueue<>();
        lamps.add(DUMMY);               // 1 - based;
        first = null;
        last = null;
        for(int i = 1; i <= m; i++) {
            int loc = Integer.parseInt(st.nextToken());
            Lamp lamp = new Lamp(loc, last, null);
            if(last == null) {
                first = lamp;
            } else {
                last.next = lamp;
                betweens.offer(new Between(last, lamp));
            }
            last = lamp;
            lamps.add(lamp);
        }
    }

    static void add() {
        lazyPropagation();
        Between between = betweens.poll();      // 가로등을 새롭게 설치할 구간
        int newLoc = between.a.loc + between.dist / 2 + between.dist % 2;
        Lamp newLamp = new Lamp(newLoc, between.a, between.b);
        between.a.next = newLamp;
        between.b.prev = newLamp;
        lamps.add(newLamp);
        betweens.offer(new Between(between.a, newLamp));
        betweens.offer(new Between(newLamp, between.b));
    }

    static void del(StringTokenizer st) {
        int id = Integer.parseInt(st.nextToken());
        Lamp lamp = lamps.get(id);
        Lamp prev = lamp.prev;
        Lamp next = lamp.next;

        if(prev == null) first = next;
        if(next == null) last = prev;

        // lamp 삭제 처리
        lamp.loc = -1;
        lamp.prev = null;
        lamp.next = null;

        if(next != null) {
            next.prev = prev;
        }
        if(prev != null) {
            prev.next = next;
        }

        if(next != null && prev != null) {
            betweens.offer(new Between(prev, next));
        }
    }

    static long query() {
        lazyPropagation();
        long left = 2L * (first.loc - 1);
        long right = 2L * (n - last.loc);
        int middle = betweens.isEmpty()
                ? 0 : betweens.peek().dist;
        return Math.max(Math.max(left, right), middle);
    }

    // =============================================
    // 공통
    // =============================================
    static void lazyPropagation() {
        while(!betweens.isEmpty()) {        // 최신 정보가 아닌 데이터 삭제
            Between between = betweens.peek();
            Lamp prev = between.a;
            Lamp next = between.b;
            if(prev.next != next || next.prev != prev) {        // 서로의 연결 정보가 올바르지 않다면
                betweens.poll();
                continue;
            }
            break;
        }
    }
}
