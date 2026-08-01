package jul.week1.codetree;

import java.io.*;
import java.util.*;

/*
  문제풀이 시간: 틈틈이 푼거라 시간 모름..1시간?
  AI 사용 여부: O
  생각의 흐름: 이전에 푼 토끼 문제랑 비슷해서 왕복 거리(제자리 위치)로 나머지 구하는 부분은 생각했지만 AI를 사용했다.
 */
public class 생명과학부랩인턴_박서희 {
    static int n, m, k;
    static int[] dx = {-1, 1, 0, 0};
    static int[] dy = {0, 0, 1, -1};

    static int answer = 0;
    static ArrayList<Mold> molds = new ArrayList<>();
    static Mold[][] board;

    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer st = new StringTokenizer(br.readLine());

        n = Integer.parseInt(st.nextToken());
        m = Integer.parseInt(st.nextToken());
        k = Integer.parseInt(st.nextToken());

        board = new Mold[n + 1][m + 1];

        for (int i = 0; i < k; i++) {
            st = new StringTokenizer(br.readLine());
            int x = Integer.parseInt(st.nextToken());
            int y = Integer.parseInt(st.nextToken());
            int s = Integer.parseInt(st.nextToken());
            int d = Integer.parseInt(st.nextToken()) - 1;
            int b = Integer.parseInt(st.nextToken());

            Mold mold = new Mold(x, y, s, d, b);
            molds.add(mold);
            board[x][y] = mold;
        }

        simulate();

        System.out.println(answer);
        br.close();
    }

    static void simulate() {
        for (int col = 1; col <= m; col++) {
            catchMold(col);
            moveMolds();
        }
    }

    static void catchMold(int col) {
        for (int row = 1; row <= n; row++) {
            if (board[row][col] != null) {
                Mold target = board[row][col];
                answer += target.b;
                molds.remove(target);
                board[row][col] = null;
                break;
            }
        }
    }

    static void moveMolds() {
        Mold[][] nextBoard = new Mold[n + 1][m + 1];

        for (Mold mold : molds) {
            mold.move();
            // 다른 곰팡이가 있으면 크기 비교
            if (nextBoard[mold.x][mold.y] != null) {
                if (nextBoard[mold.x][mold.y].b < mold.b) {
                    nextBoard[mold.x][mold.y] = mold;
                }
            } else {
                nextBoard[mold.x][mold.y] = mold;
            }
        }

        molds.clear();
        for (int i = 1; i <= n; i++) {
            for (int j = 1; j <= m; j++) {
                if (nextBoard[i][j] != null) {
                    molds.add(nextBoard[i][j]);
                }
            }
        }

        board = nextBoard;
    }

    static class Mold {
        int x, y, s, d, b;

        public Mold(int x, int y, int s, int d, int b) {
            this.x = x;
            this.y = y;
            this.s = s;
            this.d = d;
            this.b = b;
        }

        public void move() {
            int cycle = (d == 0 || d == 1) ? (n - 1) * 2 : (m - 1) * 2;
            // 왕복 거리로 나누기
            int realS = s % cycle;

            for (int i = 0; i < realS; i++) {
                // 위 아래 이동인 경우
                if (d == 0 || d == 1) {
                    if (x == 1 && d == 0) d = 1;      // 위쪽 벽에서 아래로
                    else if (x == n && d == 1) d = 0; // 아래쪽 벽에서 위로
                    x += dx[d];
                }
                // 좌 우 이동인 경우
                else {
                    if (y == 1 && d == 3) d = 2;      // 왼쪽 벽에서 오른쪽으로
                    else if (y == m && d == 2) d = 3; // 오른쪽 벽에서 왼쪽으로
                    y += dy[d];
                }
            }
        }
    }
}
