package mar.week3.codetree;

import java.util.*;
import java.io.*;

public class 토끼와경주_박재환 {
    static BufferedReader br;
    static StringBuilder sb;
    public static void main(String[] args) throws IOException {
        br = new BufferedReader(new InputStreamReader(System.in));
        sb = new StringBuilder();
        init();
        br.close();
        System.out.println(sb);
    }
    static final int READY = 100;
    static final int START = 200;
    static final int CHANGE = 300;
    static final int PICK = 400;

    static StringTokenizer st;
    static void init() throws IOException {
        int q = Integer.parseInt(br.readLine().trim());
        while(q-- > 0) {
            st = new StringTokenizer(br.readLine().trim());
            int cmd = Integer.parseInt(st.nextToken());

            if(cmd == READY) { ready(); }
            else if(cmd == START) { start(); }
            else if(cmd == CHANGE) { change(); }
            else if(cmd == PICK) { sb.append(pick()); }
        }
    }
    static class Rabbit {
        int id;     // 고유 id
        int x, y;   // 현 위치
        int d;      // 이동 거리
        int jump;
        int score;

        Rabbit(int id, int x, int y, int d, int jump, int score) {
            this.id = id;
            this.x = x;
            this.y = y;
            this.d = d;
            this.jump = jump;
            this.score = score;
        }
    }
    static int n, m, p;
    static Map<Integer, Rabbit> rabbits;
    static PriorityQueue<Rabbit> rabbitPq;
    static long totalScore;
    static void ready() {
        totalScore = 0;

        n = Integer.parseInt(st.nextToken());
        m = Integer.parseInt(st.nextToken());
        p = Integer.parseInt(st.nextToken());

        rabbits = new HashMap<>();
        rabbitPq = new PriorityQueue<>((a, b) -> {          // 경주 진행 우선순위
            if(a.jump != b.jump) return Integer.compare(a.jump, b.jump);
            int aSum = a.x + a.y;
            int bSum = b.x + b.y;
            if(aSum != bSum) return Integer.compare(aSum, bSum);
            if(a.x != b.x) return Integer.compare(a.x, b.x);
            if(a.y != b.y) return Integer.compare(a.y, b.y);
            return Integer.compare(a.id, b.id);
        });

        for(int i=0; i<p; i++) {
            int id = Integer.parseInt(st.nextToken());
            int d = Integer.parseInt(st.nextToken());

            Rabbit rabbit = new Rabbit(id, 1, 1, d, 0, 0);        // 초기 위치는 모두 (1, 1)
            rabbits.put(id, rabbit);
            rabbitPq.offer(rabbit);
        }
    }
    static void start() {
        int k = Integer.parseInt(st.nextToken());
        int s = Integer.parseInt(st.nextToken());

        Set<Integer> set = new HashSet<>();
        while(k-- > 0) {
            int[][] next = new int[4][3];
            Rabbit rabbit = rabbitPq.poll();
            // 현재 토끼가 이동했을 때 4가지 방향의 좌표를 각각 구함
            int x = rabbit.x;
            int y = rabbit.y;

            int upX = move(x, -rabbit.d, n);
            int upY = y;
            next[0] = new int[]{upX, upY, upX + upY};

            int bottomX = move(x, rabbit.d, n);
            int bottomY = y;
            next[1] = new int[]{bottomX, bottomY, bottomX + bottomY};

            int leftX = x;
            int leftY = move(y, -rabbit.d, m);
            next[2] = new int[]{leftX, leftY, leftX + leftY};

            int rightX = x;
            int rightY = move(y, rabbit.d, m);
            next[3] = new int[]{rightX, rightY, rightX + rightY};

            Arrays.sort(next, (a, b) -> {
                if(a[2] != b[2]) return Integer.compare(b[2], a[2]);
                if(a[0] != b[0]) return Integer.compare(b[0], a[0]);
                return Integer.compare(b[1], a[1]);
            });

            rabbit.x = next[0][0];
            rabbit.y = next[0][1];
            rabbit.jump++;

            // 다른 토끼들의 점수 업데이트 -> 오래걸릴듯
            // => 반대로 생각 : 이동한 토끼만 점수를 잃는다.
            rabbit.score -= (rabbit.x + rabbit.y);
            totalScore += rabbit.x + rabbit.y;
            rabbitPq.offer(rabbit);
            set.add(rabbit.id);
        }

        Rabbit r = null;
        for(int i : set) {
            Rabbit rabbit = rabbits.get(i);
            if(r == null) {
                r = rabbit;
            } else {
                int sum1 = r.x + r.y;
                int sum2 = rabbit.x + rabbit.y;

                if(sum1 < sum2 ||
                        (sum1 == sum2 && r.x < rabbit.x) ||
                        (sum1 == sum2 && r.x == rabbit.x && r.y < rabbit.y) ||
                        (sum1 == sum2 && r.x == rabbit.x && r.y == rabbit.y && r.id < rabbit.id)) {
                    r = rabbit;
                }
            }
        }
        r.score += s;
    }

    static int move(int pos, int dist, int limit) {
        int cycle = 2 * (limit - 1);        // 사이클 주기
        dist %= cycle;

        int next = pos + dist;

        while (next < 1 || next > limit) {      // 격자밖이라면 반사처리
            if (next < 1) next = 2 - next;
            else next = 2 * limit - next;
        }

        return next;
    }

    static void change() {
        int id = Integer.parseInt(st.nextToken());
        int l = Integer.parseInt(st.nextToken());

        Rabbit r = rabbits.get(id);
        r.d *= l;
    }

    static long pick() {
        int max = -1;
        for(Rabbit r : rabbits.values()) {
            max = Math.max(r.score, max);
        }
        return max + totalScore;
    }
}
