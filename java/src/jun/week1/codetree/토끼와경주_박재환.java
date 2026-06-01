package jun.week1.codetree;

import java.util.*;
import java.io.*;

/**
 * [풀이 시간]
 * 01:37:46
 * AI 사용 여부 O
 * -> 이전 코드 대비 수행 시간이 2배정도 느렸음 -> START 에서 현재 코드는 매번 PQ를 생성
 *      -> 이전 코드에서는 전역 PQ로 매번 생성하지 않음
 *
 * -> move() 메서드에서 반사 로직 시뮬레이션이 아닌 상수 시간으로 수식 세우는 데 사용
 */
public class 토끼와경주_박재환 {
    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        System.out.println(init(br));
        br.close();
    }
    static final int SET = 100;
    static final int START = 200;
    static final int CHANGE = 300;
    static final int QUERY = 400;

    static int n, m, p;
    static Map<Integer, Rabbit> idToRabbit;
    static long totalScore;
    static String init(BufferedReader br) throws IOException {
        StringTokenizer st;
        StringBuilder sb = new StringBuilder();
        int q = Integer.parseInt(br.readLine().trim());
        while(q-- > 0) {
            st = new StringTokenizer(br.readLine().trim());
            int type = Integer.parseInt(st.nextToken());
            if(type == SET) { set(st); }
            else if(type == START) { start(st); }
            else if(type == CHANGE) { change(st); }
            else if(type == QUERY) {
                long result = query();
                sb.append(result);
            }
        }
        return sb.toString();
    }
    /**
     * N x M
     * - 토끼 ( 처음 토끼들은 전부 (1, 1) 에 위치 )
     *  - 고유 번호
     *  - 이동 거리
     *
     * [경주진행]
     * 우선순위가 높은 토끼를 뽑아 멀리 보내주는 것을 K 번 반복
     * 1. 총 점프 횟수가 작은순
     * 2. 행 + 열 이 작은순
     * 3. 행 번호가 작은 토끼
     * 4. 열 번호가 작은 토끼
     * 5. pid 가 작은 토끼
     *
     * 토끼 이동
     * - 상하좌우 각각 이동했을 때 위치
     *  - 격자를 벗어나는 경우 방향을 바꿔 한 칸 이동
     *  - 그 중
     *      1. 행 + 열 큰 칸
     *      2. 행 큰 칸
     *      3. 열 큰 칸
     *  - 나머지 토끼들은 r + c 만큼 점수 얻음
     *
     * K 턴 이후
     * 1. 행 + 열 큰 순서
     * 2. 행 큰 순서
     * 3. 열 큰 순서
     * 4. pid 큰 순서
     * 를 콜라 S 를 더함
     */
    static class Rabbit {
        int pid;
        int x, y;
        int d;

        int score;
        int jumpCount;

        Rabbit(int pid, int x, int y, int d, int score, int jumpCount) {
            this.pid = pid;
            this.x = x;
            this.y = y;
            this.d = d;
            this.score = score;
            this.jumpCount = jumpCount;
        }
    }

    static void set(StringTokenizer st) {
        totalScore = 0;

        n = Integer.parseInt(st.nextToken());
        m = Integer.parseInt(st.nextToken());
        p = Integer.parseInt(st.nextToken());

        idToRabbit = new HashMap<>();
        for(int i = 0; i < p; i++) {
            int pid = Integer.parseInt(st.nextToken());
            int d = Integer.parseInt(st.nextToken());

            Rabbit rabbit = new Rabbit(pid, 1, 1, d, 0, 0);
            idToRabbit.put(pid, rabbit);
        }
    }

    static int[] dx = {0, 1, 0, -1};
    static int[] dy = {1, 0, -1, 0};
    static void start(StringTokenizer st) {
        PriorityQueue<Rabbit> pq = new PriorityQueue<>((a, b) -> {
            if(a.jumpCount != b.jumpCount) return Integer.compare(a.jumpCount, b.jumpCount);
            if((a.x + a.y) != (b.x + b.y)) return Integer.compare((a.x + a.y), (b.x + b.y));
            if(a.x != b.x) return Integer.compare(a.x, b.x);
            if(a.y != b.y) return Integer.compare(a.y, b.y);
            return Integer.compare(a.pid, b.pid);
        });

        for(Rabbit rabbit : idToRabbit.values()) {
            pq.offer(rabbit);
        }

        int k = Integer.parseInt(st.nextToken());
        int s = Integer.parseInt(st.nextToken());

        Set<Rabbit> bonusRabbit = new HashSet<>();
        while(k-- > 0) {
            // 움직일 대상
            Rabbit rabbit = pq.poll();

            // 상 하 좌 우 각각 이동하는 경우를 모두 구함
            Next top = move(rabbit.x, rabbit.y, 3, rabbit.d);
            Next bottom = move(rabbit.x, rabbit.y, 0, rabbit.d);
            Next left = move(rabbit.x, rabbit.y, 2, rabbit.d);
            Next right = move(rabbit.x, rabbit.y, 1, rabbit.d);

            PriorityQueue<Next> temp = new PriorityQueue<>((a, b) -> {
                if((a.x + a.y) != (b.x + b.y)) return Integer.compare((b.x + b.y), (a.x + a.y));
                if(a.x != b.x) return Integer.compare(b.x, a.x);
                return Integer.compare(b.y, a.y);
            });

            temp.offer(top);
            temp.offer(bottom);
            temp.offer(left);
            temp.offer(right);

            Next next = temp.poll();
            rabbit.x = next.x;
            rabbit.y = next.y;
            rabbit.jumpCount++;
            rabbit.score -= (rabbit.x + rabbit.y);
            totalScore += (rabbit.x + rabbit.y);
            bonusRabbit.add(rabbit);
            pq.offer(rabbit);
        }

        int bestPid = -1;
        int bestSum = 0, bestX = 0, bestY = 0;
        for(Rabbit rabbit : bonusRabbit) {
            if(bestSum < (rabbit.x + rabbit.y) ||
                    (bestSum == (rabbit.x + rabbit.y) && (bestX < rabbit.x ||
                            (bestX == rabbit.x && bestY < rabbit.y))) ||
                    (bestSum == (rabbit.x + rabbit.y) && bestX == rabbit.x && rabbit.y == bestY && bestPid < rabbit.pid)) {
                bestPid = rabbit.pid;
                bestSum = rabbit.x + rabbit.y;
                bestX = rabbit.x;
                bestY = rabbit.y;
            }
        }

        idToRabbit.get(bestPid).score += s;
    }

    static class Next {
        int x, y;

        Next(int x, int y) {
            this.x = x;
            this.y = y;
        }
    }

    static Next move(int x, int y, int dir, int d) {
        if(dir == 0) return new Next(x, nextLoc(y, d, m));     // 우
        if(dir == 1) return new Next(nextLoc(x, d, n), y);     // 하
        if(dir == 2) return new Next(x, nextLoc(y, -d, m));     // 좌
        return new Next(nextLoc(x, -d, n), y);                  // 상
    }

    static int nextLoc(int loc, int d, int limit) {
        int cycle = 2 * (limit - 1);
        d %= cycle;

        int next = loc + d;

        while(next < 1 || next > limit) {
            if(next < 1) next = 2 - next;
            else next = 2 * limit - next;
        }

        return next;
    }

    static void change(StringTokenizer st) {
        int pid = Integer.parseInt(st.nextToken());
        int l = Integer.parseInt(st.nextToken());

        idToRabbit.get(pid).d *= l;
    }

    static long query() {
        int bestScore = Integer.MIN_VALUE;
        for(Rabbit rabbit : idToRabbit.values()) {
            if(rabbit.score > bestScore) bestScore = rabbit.score;
        }
        return bestScore + totalScore;
    }
}
