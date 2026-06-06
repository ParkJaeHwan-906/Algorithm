package may.week4.codetree;

import java.io.*;
import java.util.*;

/*
  문제풀이 시간: 1:04:52 -> 틀림 -> 1:30:53 다음 방향을 구하기 전에 list.clear(); 를 해서 늘 방향이 상하좌우만 나옴.
  AI 사용 여부: △ 틀리고 사용.
  생각의 흐름: 원자를 어떻게 입력 받는 것이 좋을까? 한 칸에 여러 원자가 있을 수 있으니까 리스트 2차원 배열로..?
            처음 원자 객체를 만들 때는 위치도 저장했는데 배열에 위치가 저장이 되니까 지움.
 */
public class 원자충돌_박서희 {

    static int[] dx = {-1, -1, 0, 1, 1, 1, 0, -1};
    static int[] dy = {0, 1, 1, 1, 0, -1, -1, -1};

    static List<Atomic>[][] grid;
    static int N;

    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer st = new StringTokenizer(br.readLine());

        N = Integer.parseInt(st.nextToken());
        int M = Integer.parseInt(st.nextToken());
        int K = Integer.parseInt(st.nextToken());

        grid = new ArrayList[N][N];
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                grid[i][j] = new ArrayList<>();
            }
        }

        for (int i = 0; i < M; i++) {
            // 위치정보 x, y, 질량, 속도, 방향
            st = new StringTokenizer(br.readLine());
            int x = Integer.parseInt(st.nextToken()) - 1;
            int y = Integer.parseInt(st.nextToken()) - 1;
            int m = Integer.parseInt(st.nextToken());
            int v = Integer.parseInt(st.nextToken());
            int d = Integer.parseInt(st.nextToken());
            grid[x][y].add(new Atomic(m, v, d));
        }

        for (int i = 0; i < K; i++) {
            grid = move();
        }

        int answer = calculateTotalM();
        System.out.println(answer);
    }

    // 합쳐진 원자 질량의 5로 나누고, 합쳐진 속도를 원자의 개수로 나누고, 방향은 상하좌우, 대각선으로 이루어지면 상하좌우로, 아니면 대각선으로
    static List<Atomic>[][] move() {
        List<Atomic>[][] next = new ArrayList[N][N];
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                next[i][j] = new ArrayList<>();
            }
        }

        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                for (Atomic a : grid[i][j]) {
                    int nextX = (i + a.v * dx[a.dir]) % N;
                    if (nextX < 0) nextX += N;

                    int nextY = (j + a.v * dy[a.dir]) % N;
                    if (nextY < 0) nextY += N;

                    next[nextX][nextY].add(a);
                }
            }
        }

        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                if (next[i][j].size() <= 1) continue;
                int sumM = 0;
                int sumV = 0;
                int cnt = next[i][j].size();

                for (Atomic a : next[i][j]) {
                    sumM += a.m;
                    sumV += a.v;
                }
                int nextDir = calNextDir(next[i][j]);

                next[i][j].clear();

                if (sumM / 5 == 0) continue;

                int nextM = sumM / 5;
                int nextV = sumV / cnt;

                for (int aCnt = 0; aCnt < 4; aCnt++) {
                    next[i][j].add(new Atomic(nextM, nextV, aCnt * 2 + nextDir));
                }
            }
        }

        return next;
    }

    static int calculateTotalM() {
        int sumM = 0;
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                for (Atomic a : grid[i][j]) {
                    sumM += a.m;
                }
            }
        }
        return sumM;
    }

    // return 0 : 상하좌우, return 1: 대각선
    static int calNextDir(List<Atomic> atomics) {
        int even = 0;
        int odd = 0;
        for (Atomic a : atomics) {
            if (a.dir % 2 == 0) even++;
            else odd++;
        }
        return (even > 0 && odd > 0) ? 1 : 0;
    }

    static class Atomic {
        int m;
        int v;
        int dir;

        public Atomic(int m, int v, int dir) {
            this.m = m;
            this.v = v;
            this.dir = dir;
        }
    }
}
