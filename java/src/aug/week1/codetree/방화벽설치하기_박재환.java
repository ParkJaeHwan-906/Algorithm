package aug.week1.codetree;

import java.util.*;
import java.io.*;

public class 방화벽설치하기_박재환 {
    /**
     * n x m 크기의 격자
     * 불 : (상 하 좌 우) 이동 가능, 방화벽은 뚫을 수 없음
     */
    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        init(br);
        br.close();
    }

    static int n, m;
    static int[][] board;
    static void init(BufferedReader br) throws IOException {
        StringTokenizer st;

        st = new StringTokenizer(br.readLine().trim());
        n = Integer.parseInt(st.nextToken());
        m = Integer.parseInt(st.nextToken());

        board = new int[n][m];
        for(int x = 0; x < n; x++) {
            st = new StringTokenizer(br.readLine().trim());
            /**
             * 0 : 빈 칸
             * 1 : 방화벽
             * 2 : 불
             */
            for(int y = 0; y < m;) board[x][y++] = Integer.parseInt(st.nextToken());
        }

        System.out.println(solution());
    }

    static int maxArea;
    static int solution() {
        /**
         * 추가로 방화벽 3개를 설치해서, 불이 퍼지지 않는 영역의 크기 최댓값
         */
        maxArea = 0;
        installNewFirewall(0, 0);
        return maxArea;
    }
    static void installNewFirewall(int loc, int installed) {
        if(installed == 3) {        // 3개의 방화벽 모두 설치 완료
            int area = getSafeArea();
            maxArea = Math.max(maxArea, area);
            return;
        }

        for (int pos = loc; pos < n * m; pos++) {
            int x = pos / m;
            int y = pos % m;

            if (board[x][y] != 0) continue;

            board[x][y] = 1;
            installNewFirewall(pos + 1, installed + 1);
            board[x][y] = 0;
        }
    }

    static int getSafeArea() {
        int[][] temp = copyBoard();
        spreadFire(temp);
        int area = 0;
        for(int x = 0; x < n; x++) {
            for(int y = 0; y < m; y++) {
                if(temp[x][y] == 0) area++;
            }
        }
        return area;
    }

    static final int[] dx = {0, 1, 0, -1};
    static final int[] dy = {1, 0, -1, 0};
    static void spreadFire(int[][] temp) {
        Queue<int[]> q = new ArrayDeque<>();
        for(int x = 0; x < n; x++) {
            for(int y = 0; y < m; y++) {
                if(temp[x][y] == 2) q.offer(new int[] {x, y});
            }
        }

        while(!q.isEmpty()) {
            int[] cur = q.poll();
            for(int dir = 0; dir < 4; dir++) {
                int nx = cur[0] + dx[dir];
                int ny = cur[1] + dy[dir];
                if(isNotBoard(nx, ny)) continue;        // 격자 밖
                if(temp[nx][ny] == 1) continue;         // 방화벽
                if(temp[nx][ny] == 2) continue;         // 중복 탐색 방지

                temp[nx][ny] = 2;
                q.offer(new int[] {nx, ny});
            }
        }
    }

    static boolean isNotBoard(int x, int y) { return x < 0 || y < 0 || x >= n || y >= m; }

    static int[][] copyBoard() {
        int[][] temp = new int[n][m];
        for(int x = 0; x < n; x++) {
            for(int y = 0; y < m; y++) temp[x][y] = board[x][y];
        }
        return temp;
    }
}
