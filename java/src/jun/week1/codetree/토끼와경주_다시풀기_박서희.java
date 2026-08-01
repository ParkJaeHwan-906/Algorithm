package jun.week1.codetree;

import java.io.*;
import java.util.*;

/*
  문제풀이 시간: 2시간+
  AI 사용 여부: O - 시간 초과 해결, 토끼 이동 수식 + AI를 많이 사용해서 다시 풀었습니다.
 */
public class 토끼와경주_다시풀기_박서희 {
    static final int READY = 100;
    static final int RUN = 200;
    static final int CHANGE = 300;
    static final int BEST = 400;

    static int N, M, P;
    static long totalSumScore = 0;
    static Map<Integer, Rabbit> rabbitMap = new HashMap<>();
    static PriorityQueue<Rabbit> pq = new PriorityQueue<>(
            (a, b) -> {
                if (a.cnt != b.cnt) return Integer.compare(a.cnt, b.cnt);
                if (a.r + a.c != b.r + b.c) return Integer.compare(a.r + a.c, b.r + b.c);
                if (a.r != b.r) return Integer.compare(a.r, b.r);
                if (a.c != b.c) return Integer.compare(a.c, b.c);
                return Integer.compare(a.id, b.id);
            }
    );

    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

        int Q = Integer.parseInt(br.readLine());
        long answer = 0;
        while (Q-- > 0) {
            StringTokenizer st = new StringTokenizer(br.readLine());
            int command = Integer.parseInt(st.nextToken());
            if (command == READY) {
                ready(st);
            } else if (command == RUN) {
                run(st);
            } else if (command == CHANGE) {
                change(st);
            } else if (command == BEST) {
                answer = getBestScore();
            }
        }
        System.out.println(answer);
    }

    static void ready(StringTokenizer st) {
        N = Integer.parseInt(st.nextToken());
        M = Integer.parseInt(st.nextToken());
        P = Integer.parseInt(st.nextToken());

        while (P-- > 0) {
            int id = Integer.parseInt(st.nextToken());
            int dist = Integer.parseInt(st.nextToken());
            Rabbit r = new Rabbit(id, dist);
            rabbitMap.put(id, r);
        }
    }

    static void run(StringTokenizer st) {
        int K = Integer.parseInt(st.nextToken());
        int S = Integer.parseInt(st.nextToken());

        PriorityQueue<Rabbit> pq2 = new PriorityQueue<>(
                (a, b) -> {
                    if (a.r + a.c != b.r + b.c) return Integer.compare(b.r + b.c, a.r + a.c);
                    if (a.r != b.r) return Integer.compare(b.r, a.r);
                    if (a.c != b.c) return Integer.compare(b.c, a.c);
                    return Integer.compare(b.id, a.id);
                }
        );
        Set<Integer> moveRabbitId = new HashSet<>();

        pq.clear();
        for (Rabbit r : rabbitMap.values()) pq.add(r);

        while (K-- > 0) {
            Rabbit targetRabbit = pq.poll();
            int targetR = -1, targetC = -1;

            for (int d = 0; d < 4; d++) {
                int curR = targetRabbit.r, curC = targetRabbit.c;
                if (d == 0 || d == 1) {
                    int mod = 2 * (N - 1);
                    int remain = targetRabbit.dist % mod;

                    if (d == 0) {
                        remain = (mod - remain + targetRabbit.r) % mod;
                    } else {
                        remain = (remain + targetRabbit.r) % mod;
                    }

                    if (remain < N) curR = remain;
                    else curR = mod - remain;

                } else {
                    int mod = 2 * (M - 1);
                    int remain = targetRabbit.dist % mod;

                    if (d == 2) {
                        remain = (mod - remain + targetRabbit.c) % mod;
                    } else {
                        remain = (remain + targetRabbit.c) % mod;
                    }

                    if (remain < M) curC = remain;
                    else curC = mod - remain;

                }

                if (targetR + targetC < curR + curC) {
                    targetR = curR;
                    targetC = curC;
                } else if (targetR + targetC == curR + curC) {
                    if (curR > targetR) {
                        targetR = curR;
                        targetC = curC;
                    }
                }
            }

            targetRabbit.r = targetR;
            targetRabbit.c = targetC;
            targetRabbit.cnt++;
            moveRabbitId.add(targetRabbit.id);

            totalSumScore += (targetR + targetC + 2);
            targetRabbit.score -= (targetR + targetC + 2);

            pq.add(targetRabbit);
        }

        for (Integer id : moveRabbitId) {
            pq2.add(rabbitMap.get(id));
        }

        Rabbit scoreRabbit = pq2.poll();
        scoreRabbit.score += S;
    }

    static void change(StringTokenizer st) {
        int id = Integer.parseInt(st.nextToken());
        int L = Integer.parseInt(st.nextToken());

        rabbitMap.get(id).dist *= L;
    }

    static long getBestScore() {
        long maxScore = 0;
        for (Rabbit r : rabbitMap.values()) {
            if (maxScore < r.score) maxScore = r.score;
        }
        return maxScore + totalSumScore;
    }

    static class Rabbit {
        int id, dist, cnt, r, c;
        long score;

        public Rabbit(int id, int dist) {
            this.id = id;
            this.dist = dist;
            this.cnt = 0;
            this.r = 0;
            this.c = 0;
            this.score = 0;
        }
    }
}
