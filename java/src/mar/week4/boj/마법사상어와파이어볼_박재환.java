package mar.week4.boj;

import java.util.*;
import java.io.*;

public class 마법사상어와파이어볼_박재환 {
    static BufferedReader br;
    public static void main(String[] args) throws IOException {
        br = new BufferedReader(new InputStreamReader(System.in));
        init();
        br.close();
    }
    static final int[] dx = {-1, -1, 0, 1, 1, 1, 0, -1};
    static final int[] dy = {0, 1, 1, 1, 0, -1, -1, -1};

    static StringTokenizer st;
    static int n, m, k;
    static Queue<FireBall> fireBalls;
    static void init() throws IOException {
        st = new StringTokenizer(br.readLine().trim());
        n = Integer.parseInt(st.nextToken());
        m = Integer.parseInt(st.nextToken());
        k = Integer.parseInt(st.nextToken());

        fireBalls = new ArrayDeque<>();
        for(int i=0; i<m; i++) {
            st = new StringTokenizer(br.readLine().trim());
            int x = Integer.parseInt(st.nextToken())-1;
            int y = Integer.parseInt(st.nextToken())-1;
            int m = Integer.parseInt(st.nextToken());
            int s = Integer.parseInt(st.nextToken());
            int d = Integer.parseInt(st.nextToken());
            FireBall fireBall = new FireBall(x, y, m, d, s);
            fireBalls.offer(fireBall);
        }

        while(k-- > 0) {
            moveAllFireBall();
            divideFireBall();
        }

        int sum = 0;
        while(!fireBalls.isEmpty()) sum += fireBalls.poll().m;
        System.out.println(sum);
     }
    /**
     * 이동은 한 번에 일어난다.
     * 이동이 끝난 후, 같은 칸에 있는 파이어볼은 합쳔진다.
     * 파이어볼은 4개로 나누어진다.
     * - 나누어진 파이어볼 질량 : 합쳐진 파이어볼 질량 / 5
     * - 나누어진 파이어볼 속력 : 합쳐진 파이어볼 속력 / 합쳐진 파이어볼 개수
     * - 나누어진 파이어볼 방향 : 합쳐진 파이어볼 방향이 모두 홀수라면 [0, 2, 4, 6], 아니라면 [1, 3, 5, 7]
     * 질량이 0이되면 사라진다.
     */
    static class FireBall {
        int x, y;
        int m;          // 질량
        int d;          // 방향
        int s;          // 속도

        FireBall(int x, int y, int m, int d, int s) {
            this.x = x;
            this.y = y;
            this.m = m;
            this.d = d;
            this.s = s;
        }

        void move() {
            // 격자 밖으로 나가는 경우에 반대쪽과 이어져 있음
            int moveCycle = s % n;      // 제자리로 돌아오는 경우 제외
            int nx = (x + dx[d] * moveCycle + n) % n;
            int ny = (y + dy[d] * moveCycle + n) % n;
            this.x = nx;
            this.y = ny;
        }
    }

    static final int KEY = 57;
    static Map<Integer, FireBall> afterMove;
    static Map<Integer, Set<Integer>> afterMoveDir;
    static Map<Integer, Integer> afterMoveCount;
    static void moveAllFireBall() {
        afterMove = new HashMap<>();
        afterMoveDir = new HashMap<>();
        afterMoveCount = new HashMap<>();

        while(!fireBalls.isEmpty()) {
            FireBall fireBall = fireBalls.poll();
            fireBall.move();
            int key = fireBall.x * KEY + fireBall.y;
            FireBall other = afterMove.get(key);
            if(other == null) {
                afterMove.put(key, fireBall);
                afterMoveDir.computeIfAbsent(key, k -> new HashSet<>()).add(fireBall.d);
                afterMoveCount.put(key, afterMoveCount.getOrDefault(key, 0) + 1);
            } else {
                // 기존 위치에 파이어볼이 있다면
                // 질량 합치고, 속력 합치고
                // 부가 정보 : 합쳐진 파이어볼 개수, 합쳐진 방향 종류(홀수, 짝수)
                other.m += fireBall.m;
                other.s += fireBall.s;
                afterMoveDir.computeIfAbsent(key, k -> new HashSet<>()).add(fireBall.d);
                afterMoveCount.put(key, afterMoveCount.getOrDefault(key, 0) + 1);
            }
        }
    }
    static final int[] oddDir = {0, 2, 4, 6};
    static final int[] evenDir = {1, 3, 5, 7};
    static void divideFireBall() {
        for(int key : afterMove.keySet()) {
            // 분할하고자 하는 파이어볼
            FireBall fireBall = afterMove.get(key);
            int conquerCount = afterMoveCount.get(key);
            Set<Integer> conquerDir = afterMoveDir.get(key);
            if(conquerCount == 1) {
                fireBalls.offer(fireBall);
                continue;
            }
            // 4 개의 파이어볼로 나누어진다.
            int newM = fireBall.m / 5;      // 새로운 질량
            if(newM == 0)  continue;      // 질량이 0 인 파이어볼은 소멸된다.
            int newS = fireBall.s / conquerCount;   // 새로운 속도

            // 방향
            if(isAllSameDir(conquerDir)) {      // 모두 홀수
                for(int d : oddDir) {
                    FireBall newF = new FireBall(fireBall.x, fireBall.y, newM, d, newS);
                    fireBalls.offer(newF);
                }
            } else {    // 그 외
                for(int d : evenDir) {
                    FireBall newF = new FireBall(fireBall.x, fireBall.y, newM, d, newS);
                    fireBalls.offer(newF);
                }
            }
        }
    }
    static boolean isAllSameDir(Set<Integer> s) {
        int even = 0;
        int odd = 0;
        for(int i : s) {
            if(i % 2 == 0) even++;
            else odd++;
        }
        return (even == 0) || (odd == 0);
    }
}
