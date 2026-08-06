package aug.week1.codetree;

import java.util.*;
import java.io.*;

public class 시공의돌풍_박재환 {
    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        init(br);
        br.close();
    }

    static int n, m, t;
    static int[][] board;
    static void init(BufferedReader br) throws IOException {
        StringTokenizer st;

        st = new StringTokenizer(br.readLine().trim());
        n = Integer.parseInt(st.nextToken());
        m = Integer.parseInt(st.nextToken());
        t = Integer.parseInt(st.nextToken());

        board = new int[n][m];
        for(int x = 0; x < n; x++) {
            st = new StringTokenizer(br.readLine().trim());
            for(int y = 0; y < m; y++) board[x][y] = Integer.parseInt(st.nextToken());
        }

        System.out.println(solution());
    }

    static int stormTop;
    static int solution() {
        findStormTop();
        while(t-- > 0) {        // t 초 동안 진행
            // 1. 먼지 확산
            spreadDust();
            // 2. 청소
            cleanDust();
        }
//        printState();
        return getResult();
    }

    static void findStormTop() {
        for(int x = 0; x < n; x++) {
            if(board[x][0] == -1) {
                stormTop = x;
                break;
            }
        }
    }

    static final int[] dx = {0, 1, 0, -1};
    static final int[] dy = {1, 0, -1, 0};
    static void spreadDust() {
        // 확산
        int[][] addDust = new int[n][m];
        for(int x = 0; x < n; x++) {
            for(int y = 0; y < m; y++) {
                if(board[x][y] > 0) {       // 먼지 확산
                    int spreadDust = board[x][y] / 5;
                    int count = 0;
                    for(int dir = 0; dir < 4; dir++) {
                        int nx = x + dx[dir];
                        int ny = y + dy[dir];
                        if(isNotBoard(nx, ny)) continue;        // 격자 밖
                        if(board[nx][ny] == -1) continue;       // 폭풍이 있는 칸
                        addDust[nx][ny] += spreadDust;
                        count++;
                    }
                    board[x][y] -= (spreadDust * count);
                }
            }
        }

        // 반영
        for(int x = 0; x < n; x++) {
            for(int y = 0; y < m; y++) board[x][y] += addDust[x][y];
        }
    }

    // 순환 방향 : 우 → 상 → 좌 → 하 (반시계)
    static final int[] ccwX = {0, -1, 0, 1};
    static final int[] ccwY = {1, 0, -1, 0};
    // 순환 방향 : 우 → 하 → 좌 → 상 (시계)
    static final int[] cwX = {0, 1, 0, -1};
    static final int[] cwY = {1, 0, -1, 0};

    static void cleanDust() {
        circulate(stormTop, 0, stormTop, ccwX, ccwY);               // 위쪽 : 반시계 방향
        circulate(stormTop + 1, stormTop + 1, n - 1, cwX, cwY);     // 아래쪽 : 시계 방향
    }

    static void circulate(int mx, int top, int bottom, int[] cdx, int[] cdy) {
        int prev = 0;                   // 돌풍 -> 0
        int x = mx, y = 0, dir = 0;

        while(true) {
            int nx = x + cdx[dir];
            int ny = y + cdy[dir];

            if(nx < top || nx > bottom || ny < 0 || ny >= m) {      // 구간 밖 → 방향 전환
                dir++;
                continue;
            }
            if(nx == mx && ny == 0) break;                          // 한바퀴 다 돌았을 때

            int cur = board[nx][ny];    // 이전 칸의 먼지를 밀어 넣고, 원래 값은 다음 칸으로 넘긴다
            board[nx][ny] = prev;
            prev = cur;

            x = nx;
            y = ny;
        }
    }

    static int getResult() {
        int sum = 0;
        for(int[] arr : board) {
            for(int i : arr) {
                if(i == -1) continue;
                sum += i;
            }
        }
        return sum;
    }

    static boolean isNotBoard(int x, int y) { return x < 0 || y < 0 || x >= n || y >= m; }

    static void printState() {
        for(int[] arr : board) System.out.println(Arrays.toString(arr));
    }
}
