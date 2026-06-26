package jun.week4.codetree;

import java.util.*;
import java.io.*;

/**
 * [풀이 시간]
 * 01:47:25
 * AI 사용 여부 O
 * => 기울어진 사각형을 만드는 로직까지는 어렵지 않았음
 *      => 이후 경계를 구분하는 로직 아이디어가 떠오르지 않음 => 각 꼭짓점을 기준으로 규칙 적용
 */
public class 종전_박재환 {
    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        init(br);
        br.close();
    }

    static int n;
    static int[][] board;
    static void init(BufferedReader br) throws IOException {
        StringTokenizer st;

        n = Integer.parseInt(br.readLine().trim());
        board = new int[n][n];
        for(int x = 0; x < n; x++) {
            st = new StringTokenizer(br.readLine().trim());
            for(int y = 0; y < n; y++) board[x][y] = Integer.parseInt(st.nextToken());
        }

        System.out.println(solution());
    }

    static final int[] dx = {-1, -1, 1, 1};
    static final int[] dy = {1, -1, -1, 1};

    static int answer;
    static int solution() {
        answer = Integer.MAX_VALUE;
        /**
         * 기울어진 직사각형 만들기
         */
         for(int x = 0; x < n; x++) {
             for(int y = 0; y < n; y++) {
                 boolean[][] tiltSquare = new boolean[n][n];
                 tiltSquare[x][y] = true;
                makeTiltSquare(tiltSquare, x, y, x, y, 0, 0, 0);
             }
         }
         return answer;
    }

    static void makeTiltSquare(boolean[][] tiltSquare, int sx, int sy, int x, int y, int dir, int a, int b) {
        // 기울어진 직사각형 완성
        if(dir == 4) {
            int diff = compareNative(tiltSquare, sx, sy, a, b);
            answer = Math.min(answer, diff);
            return;
        }
        boolean[][] temp = copyBoard(tiltSquare);
        if(dir == 0) {
            while(true) {
                int nx = x + dx[dir];
                int ny = y + dy[dir];

                if(isNotBoard(nx, ny)) break;

                a++;
                temp[nx][ny] = true;
                makeTiltSquare(temp, sx, sy, nx, ny, dir + 1, a, b);
                x = nx;
                y = ny;
            }
        } else if(dir == 1) {
            while(true) {
                int nx = x + dx[dir];
                int ny = y + dy[dir];

                if(isNotBoard(nx, ny)) break;

                b++;
                temp[nx][ny] = true;
                makeTiltSquare(temp, sx, sy, nx, ny, dir + 1, a, b);
                x = nx;
                y = ny;
            }
        } else if(dir == 2) {
            for(int i = 0; i < a; i++) {
                int nx = x + dx[dir];
                int ny = y + dy[dir];
                if(isNotBoard(nx, ny)) return;
                temp[nx][ny] = true;
                x = nx;
                y = ny;
            }
            makeTiltSquare(temp, sx, sy, x, y, dir + 1, a, b);
        } else if(dir == 3) {
            for(int i = 0; i < b; i++) {
                int nx = x + dx[dir];
                int ny = y + dy[dir];
                if(isNotBoard(nx, ny)) return;
                temp[nx][ny] = true;
                x = nx;
                y = ny;
            }
            makeTiltSquare(temp, sx, sy, x, y, dir + 1, a, b);
        }
    }

    static int compareNative(boolean[][] arr, int sx, int sy, int a, int b) {
        boolean[][] temp = copyBoard(arr);
        checkInside(temp);

        int[] people = new int[5];

        int bX = sx, bY = sy;
        int rX = sx - a, rY = sy + a;
        int tX = sx - a - b, tY = sy + a - b;
        int lX = sx - b, lY = sy - b;

        for(int x = 0; x < n; x++) {
            int l = -1, r = -1;

            // 1번 부족의 현재 행의 경계 찾기
            for(int y = 0; y < n; y++) {
                if(temp[x][y]) {
                    if(l == -1) l = y;
                    r = y;
                }
            }

            if(l != -1) {       // 현재 행에 1번 부족이 존재
                // 1번 부족
                for(int y = l; y <= r; y++) people[0] += board[x][y];

                // 2번 부족 / 4번 부족
                for(int y = 0; y < l; y++) {
                    if(x < lX) people[1] += board[x][y];
                    else people[3] += board[x][y];
                }

                // 3번 부족 / 5번 부족
                for(int y = r + 1; y < n; y++) {
                    if(x <= rX) people[2] += board[x][y];
                    else people[4] += board[x][y];
                }
            } else {        // 경계가 없는 경우
                if(x < tX) {
                    for(int y = 0; y < n; y++) {
                        if(y <= tY) people[1] += board[x][y];
                        else people[2] += board[x][y];
                    }
                } else {
                    for(int y = 0; y < n; y++) {
                        if(y < bY) people[3] += board[x][y];
                        else people[4] += board[x][y];
                    }
                }
            }
        }

        return getDiff(people);
    }

    static void checkInside(boolean[][] arr) {
        // 1번 부족 : 경계선 + 내부
        for(int x = 0; x < n; x++) {
            int l = -1, r = -1;

            for(int y = 0; y < n; y++) {
                if(arr[x][y]) {
                    if(l == -1) l = y;
                    r = y;
                }
            }

            if(l == -1) continue;
            for(int y = l; y <= r; y++) arr[x][y] = true;
        }
    }

    static boolean isNotBoard(int x, int y) {
        return x < 0 || y < 0 || x >= n || y >= n;
    }

    static boolean[][] copyBoard(boolean[][] board) {
        boolean[][] copyBoard = new boolean[n][n];
        for(int x = 0; x < n; x++) {
            for(int y = 0; y < n; y++) copyBoard[x][y] = board[x][y];
        }
        return copyBoard;
    }

    static int getDiff(int[] arr) {
        int max = 0, min = Integer.MAX_VALUE;
        for(int i : arr) {
            max = Math.max(max, i);
            min = Math.min(min, i);
        }
        return max - min;
    }
}
