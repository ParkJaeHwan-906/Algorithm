package jun.week4.codetree;

import java.util.*;
import java.io.*;

/**
 * [풀이 시간]
 * 00:53:21
 * AI 사용 여부 O
 * -> tilt 함수 내부에서 두 구슬의 위치가 같을 때 확인하는 부분에서 출구(#) 에 동시 도착하는 부분을 처음에 필터링하지 못했음
 *      -> 출구에 동시 도착하는 경우에도 하나의 구슬을 뒤로 보냈기 때문에 틀렸음
 *          -> 빨간 구슬이 출구에 도착하는 경우 좌표 보정을 하지 않고 다음 함수로 보내서 동시 탈출 여부를 판단함
 */
public class 두개의사탕_박재환 {
    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        init(br);
        br.close();
    }

    /**
     * n x m 크기의 격자
     * - 빨간 사탕 1개
     * - 파란 사탕 1개
     * - 장애물
     * - 출구 1개
     *
     * [사탕 빼기]
     * - 상자를 {상, 하, 좌, 우} 로 기울여서 뺄 수 있음
     *     -> 기울어진 방향으로 사탕은 장애물 혹은 다른 사탕에 부딪히기 전까지 미끄러진다.
     *         -> 사탕은 동시에 미끄러지나?
     *         -> 같은 방향으로 미끄러지기 때문에 고려하지 않아도 될듯
     * - 빨간 사탕이 먼저 나와야한다. (동시 X)
     */
    static class Loc {
        int x, y;
        Loc(int x, int y) {
            this.x = x;
            this.y = y;
        }
    }

    static class Candy extends Loc {
        Candy(int x, int y) {
            super(x, y);
        }
    }

    static class Exit extends Loc {
        Exit(int x, int y) {
            super(x, y);
        }
    }

    static int n, m;
    static char[][] board;
    static Exit exit;
    static void init(BufferedReader br) throws IOException {
        StringTokenizer st;

        st = new StringTokenizer(br.readLine());
        n = Integer.parseInt(st.nextToken());
        m = Integer.parseInt(st.nextToken());

        Candy red = null, blue = null;
        board = new char[n][m];
        for(int x = 0; x < n; x++) {
            String line = br.readLine().trim();
            for(int y = 0; y < m; y++) {
                board[x][y] = line.charAt(y);

                if(board[x][y] == 'O') exit = new Exit(x,y);
                else if(board[x][y] == 'B') blue = new Candy(x,y);
                else if(board[x][y] == 'R') red = new Candy(x,y);
            }
        }
        System.out.println(solution(red, blue));
    }

    static int minTilt;
    static int solution(Candy red, Candy blue) {
        minTilt = Integer.MAX_VALUE;
        tilt(0, -1, -1, red, blue);
        return minTilt == Integer.MAX_VALUE ? -1 : minTilt;
    }

    static int[] dx = {0, 1, 0, -1};
    static int[] dy = {1, 0, -1, 0};
    static void tilt(int tiltCount, int prevDir, int returnDir, Candy red, Candy blue) {
        // 종료 조건
        if(tiltCount > 10) return;
        // - 파란색이 먼저 빠지는 경우
        // - 동시에 빠지는 경우
        if((!isExit(red.x, red.y) && isExit(blue.x, blue.y)) ||
                (isExit(red.x, red.y) && isExit(blue.x, blue.y))) return;

        // 빨간색만 빠지는 경우
        if(isExit(red.x, red.y)) {
            minTilt = Math.min(minTilt, tiltCount);
            return;
        }

        // 기존 minTilt보다 값이 큰 경우
        if(minTilt <=  tiltCount) return;

        for(int dir = 0; dir < 4; dir++) {
            if(dir == prevDir || dir == returnDir) continue;        // 의미 없는 이동 방지, 무한루프 방지

            Candy newRed = moveCandy(red, dir);
            Candy newBlue = moveCandy(blue, dir);

            // 둘 다 움직일 수 없어서 의미 없는 경우
            if(isSame(newRed, red) && isSame(newBlue, blue)) continue;

            // 둘 다 같은 위치에 있는 경우 (구슬끼리 충돌)
            if(isSame(newRed, newBlue)) {
                if(isExit(newRed.x, newRed.y)) {        // 빨간 구슬이 출구에 도착하는 경우 -> 동시 도착은 다음 함수 호출에서 확인
                    tilt(tiltCount + 1, dir, (dir + 2) % 4, newRed, newBlue);
                    continue;
                }
                int redMoveDist = Math.abs(red.x - newRed.x) + Math.abs(red.y - newRed.y);
                int blueMoveDist = Math.abs(blue.x - newBlue.x) + Math.abs(blue.y - newBlue.y);

                if(redMoveDist < blueMoveDist) {
                    newBlue.x -= dx[dir];
                    newBlue.y -= dy[dir];
                } else {
                    newRed.x -= dx[dir];
                    newRed.y -= dy[dir];
                }
            }

            tilt(tiltCount + 1, dir, (dir + 2) % 4, newRed, newBlue);
        }

    }

    static Candy moveCandy(Candy candy, int dir) {
        int x = candy.x, y = candy.y;
        while(true) {
            int nx = x + dx[dir];
            int ny = y + dy[dir];

            if(isNotBoard(nx, ny)) break;       // 격자 밖을 벗어나는 경우
            if(board[nx][ny] == '#') break;     // 장애물에 막히는 경우

            x = nx;
            y = ny;
            if(board[x][y] == 'O') break;       //  출구인 경우 즉시 종료
        }
        return new Candy(x, y);
    }

    static boolean isSame(Candy a, Candy b) {
        return a.x == b.x && a.y == b.y;
    }

    static boolean isNotBoard(int x, int y) {
        return x < 0 || y < 0 || x >= n || y >= m;
    }

    static boolean isExit(int x, int y) {
        return exit.x == x && exit.y == y;
    }
}
