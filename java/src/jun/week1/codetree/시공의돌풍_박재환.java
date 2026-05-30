package jun.week1.codetree;

import java.util.*;
import java.io.*;

/**
 * [풀이 시간]
 * 00:51:27
 * AI 사용 여부 X
 */
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
        for(int i = 0; i < n; i++) {
            st = new StringTokenizer(br.readLine().trim());
            for(int j = 0; j < m; j++) board[i][j] = Integer.parseInt(st.nextToken());
        }
        System.out.println(solution());
    }
    static void setStorm() {
        storm = new ArrayList<>();
        for(int i = 0; i < n; i++) {
            for(int j = 0; j < m; j++) {
                if(board[i][j] == -1) storm.add(new int[] {i, j});
            }
        }
    }

    static List<int[]> storm;
    static int solution() {
        setStorm();

        while(t-- > 0) {
            // 1. 먼지 확산
            spreadDusts();
            // ===
//            for(int[] arr : board) System.out.println(Arrays.toString(arr));
//            System.out.println();
            // ===
            // 2. 청소
            clean();
            // ===
//            for(int[] arr : board) System.out.println(Arrays.toString(arr));
//            System.out.println();
            // ===
        }
        return totalDust();
    }

    static int[] dx = {0, 1, 0, -1};
    static int[] dy = {1, 0, -1, 0};
    static void spreadDusts() {
        int[][] lazyBoard = new int[n][m];

        for(int i = 0; i < n; i++) {
            for(int j = 0; j < m; j++) {
                if(board[i][j] == -1) continue;     // 폭풍이 있는 칸이라면 확산하지 않음

                int spread = spreadDust(i, j, lazyBoard);
                board[i][j] -= spread;
            }
        }

        // lazy 반영
        for(int i = 0; i < n; i++) {
            for(int j = 0; j < m; j++) {
                board[i][j] += lazyBoard[i][j];
            }
        }
    }

    static int spreadDust(int x, int y, int[][] lazyBoard) {
        int spread = 0;
        int mod = board[x][y] / 5;
        for(int dir = 0; dir < 4; dir++) {
            int nx = x + dx[dir];
            int ny = y + dy[dir];

            if(isNotBoard(nx, ny)) continue;
            if(board[nx][ny] == -1) continue;

            lazyBoard[nx][ny] += mod;
            spread += mod;
        }
        return spread;
    }

    static void clean() {
        /**
         * 폭풍
         * - 위 : 반시계
         * - 아래 : 시계
         * 격자 전체 범위가 회전 ? 아니면 가장자리만 회전..?
         */
        counterClockWise(storm.get(0)[0], storm.get(0)[1]);
        clockwise(storm.get(1)[0], storm.get(1)[1]);
        // 다시 돌풍 위치 설정
        for(int[] loc : storm) {
            board[loc[0]][loc[1]] = -1;
        }
    }
    static int[] counterClockWiseDx = {0, -1, 0, 1};
    static int[] counterClockWiseDy = {1, 0, -1, 0};
    static void counterClockWise(int x, int y) {
        // 반시계 방향 : > ^ < v
        int dir = 0;
        int prevX = x, prevY = y;
        int prevV = 0;
        while(true) {
            int nextX = prevX + counterClockWiseDx[dir];
            int nextY = prevY + counterClockWiseDy[dir];

            if(isNotBoard(nextX, nextY)) {
                dir = (dir + 1) % 4;
                continue;
            }

            if(nextX == x && nextY == y) break;

            int temp =  board[nextX][nextY];
            board[nextX][nextY] = prevV;
            prevV = temp;
            prevX = nextX;
            prevY = nextY;
        }
    }
    static int[] clockWiseDx = {0, 1, 0, -1};
    static int[] clockWiseDy = {1, 0, -1, 0};
    static void clockwise(int x, int y) {
        // 시계 방향 : > v < ^
        int dir = 0;
        int prevX = x, prevY = y;
        int prevV = 0;
        while(true) {
            int nextX = prevX + clockWiseDx[dir];
            int nextY = prevY + clockWiseDy[dir];

            if(isNotBoard(nextX, nextY)) {
                dir = (dir + 1) % 4;
                continue;
            }

            if(nextX == x && nextY == y) break;

            int temp =  board[nextX][nextY];
            board[nextX][nextY] = prevV;
            prevV = temp;
            prevX = nextX;
            prevY = nextY;
        }
    }

    static int totalDust() {
        int total = 0;
        for(int i = 0; i < n; i++) {
            for(int j = 0; j < m; j++) {
                if(board[i][j] == -1) continue;
                total += board[i][j];
            }
        }
        return total;
    }

    static boolean isNotBoard(int x, int y) {
        return x < 0 || y < 0 || x >= n || y >= m;
    }
}
