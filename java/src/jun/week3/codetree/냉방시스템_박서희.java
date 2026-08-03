package jun.week3.codetree;

import java.io.*;
import java.util.*;

/*
  문제풀이 시간: 01:49:22
  AI 사용 여부: O mixedAir()에서 오른쪽과 아래방향만 따져서 중복이 안되게 계산.
               spreadAir()에서 반복문이 아닌 bfs를 쓰는 이유 -> 벽 뒤에는 바람이 안 퍼지게 하기 위함.
  생각의 흐름: 와..복잡하다..복잡하다..
 */
public class 냉방시스템_박서희 {

    static int n, m, k;
    static ArrayList<int[]> offices;
    static ArrayList<int[]> acs;

    static int[][] board;
    static boolean[][][] wall;
    static int[][] air;
    static int turn = 0;

    // 좌 상 우 하
    static int[] sdx = {0, -1, 0, 1};
    static int[] sdy = {-1, 0, 1, 0};
    static int[][] cdx = {{-1, 1}, {-1, -1}, {-1, 1}, {1, 1}};
    static int[][] cdy = {{-1, -1}, {-1, 1}, {1, 1}, {-1, 1}};

    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer st = new StringTokenizer(br.readLine());

        n = Integer.parseInt(st.nextToken());   // 격자 크기
        m = Integer.parseInt(st.nextToken());   // 벽의 개수
        k = Integer.parseInt(st.nextToken());   // 사무실의 시원함의 정도

        offices = new ArrayList<>();
        acs = new ArrayList<>();

        air = new int[n][n];
        board = new int[n][n];
        for (int i = 0; i < n; i++) {
            st = new StringTokenizer(br.readLine());
            for (int j = 0; j < n; j++) {
                board[i][j] = Integer.parseInt(st.nextToken());
                if (board[i][j] == 1) offices.add(new int[]{i, j});
                if (board[i][j] >= 2) acs.add(new int[]{i, j, board[i][j]});    // 좌 상 우 하
            }
        }

        wall = new boolean[n][n][2];
        for (int i = 0; i < m; i++) {
            st = new StringTokenizer(br.readLine());
            int r = Integer.parseInt(st.nextToken()) - 1;
            int c = Integer.parseInt(st.nextToken()) - 1;
            int type = Integer.parseInt(st.nextToken());    // 0 이면 윗쪽 벽 1 이면 왼쪽 벽
            wall[r][c][type] = true;
        }

        simulate();
    }

    static void simulate() {
        while (turn <= 100) {
            turn++;

            spreadAir();
            mixedAir();
            downAir();

            if (allOfficeOverK()) {
                System.out.println(turn);
                return;
            }
        }
        System.out.println(-1);
    }

    private static void spreadAir() {
        for (int[] ac : acs) {
            int r = ac[0], c = ac[1], dir = ac[2] - 2;

            bfs(r, c, dir);
        }
    }

    private static void bfs(int r, int c, int d) {
        boolean[][] visited = new boolean[n][n];
        Queue<int[]> queue = new LinkedList<>();

        int startR = r + sdx[d];
        int startC = c + sdy[d];

        queue.add(new int[]{startR, startC, 5});
        visited[startR][startC] = true;

        while (!queue.isEmpty()) {
            int[] cur = queue.poll();
            int cr = cur[0], cc = cur[1], amount = cur[2];

            air[cr][cc] += amount;

            if (amount == 1) continue;

            // 직진
            int nr = cr + sdx[d], nc = cc + sdy[d];
            if (inRange(nr, nc) && !visited[nr][nc]) {
                if (canMoveStraight(cr, cc, d)) {
                    visited[nr][nc] = true;
                    queue.add(new int[]{nr, nc, amount - 1});
                }
            }

            // 대각선
            for (int i = 0; i < 2; i++) {
                int nnr = cr + cdx[d][i];
                int nnc = cc + cdy[d][i];

                if (inRange(nnr, nnc) && !visited[nnr][nnc]) {
                    if (canMoveDiagonal(cr, cc, d, i)) {
                        visited[nnr][nnc] = true;
                        queue.add(new int[]{nnr, nnc, amount - 1});
                    }
                }
            }
        }
    }

    private static boolean canMoveStraight(int cr, int cc, int d) {
        if (d == 0) return !wall[cr][cc][1];
        if (d == 1) return !wall[cr][cc][0];
        if (d == 2) return !wall[cr][cc + 1][1];
        if (d == 3) return !wall[cr + 1][cc][0];
        return true;
    }

    private static boolean canMoveDiagonal(int cr, int cc, int d, int i) {
        if (d == 0) { // 좌
            if (i == 0) return !wall[cr][cc][0] && !wall[cr - 1][cc][1];    // 위로 갔다가 왼쪽
            else return !wall[cr + 1][cc][0] && !wall[cr + 1][cc][1];       // 아래로 갔다가 왼쪽
        }
        if (d == 1) { // 상
            if (i == 0) return !wall[cr][cc][1] && !wall[cr][cc - 1][0];    // 왼쪽으로 갔다가 위로
            else return !wall[cr][cc + 1][1] && !wall[cr][cc + 1][0];       // 오른쪽으로 갔다가 위로
        }
        if (d == 2) { // 우
            if (i == 0) return !wall[cr][cc][0] && !wall[cr - 1][cc + 1][1];// 위로 갔다가 오른쪽
            else return !wall[cr + 1][cc][0] && !wall[cr + 1][cc + 1][1];   // 아래로 갔다가 오른쪽
        }
        if (d == 3) { // 하
            if (i == 0) return !wall[cr][cc][1] && !wall[cr + 1][cc - 1][0];// 왼쪽으로 갔다가 아래
            else return !wall[cr][cc + 1][1] && !wall[cr + 1][cc + 1][0];   // 오른쪽으로 갔다가 아래
        }
        return true;
    }

    private static void mixedAir() {
        int[][] nextAir = new int[n][n];
        for (int i = 0; i < n; i++) {
            nextAir[i] = air[i].clone();
        }

        // 아래 오른쪽
        int[] dx = {1, 0};
        int[] dy = {0, 1};

        for (int r = 0; r < n; r++) {
            for (int c = 0; c < n; c++) {

                for (int d = 0; d < 2; d++) {
                    int nr = r + dx[d], nc = c + dy[d];
                    if (!inRange(nr, nc)) continue;
                    if (d == 0 && wall[nr][nc][0]) continue;    // 아래
                    if (d == 1 && wall[nr][nc][1]) continue;    // 오른쪽

                    int diff = Math.abs(air[r][c] - air[nr][nc]) / 4;
                    if (diff > 0) {
                        if (air[r][c] > air[nr][nc]) {
                            nextAir[r][c] -= diff;
                            nextAir[nr][nc] += diff;
                        } else {
                            nextAir[r][c] += diff;
                            nextAir[nr][nc] -= diff;
                        }
                    }
                }
            }
        }
        air = nextAir;
    }

    private static void downAir() {
        for (int i = 0; i < n; i++) {
            if (i == 0 || i == n - 1) {
                for (int j = 0; j < n; j++) {
                    if (air[i][j] > 0) air[i][j]--;
                }
            } else {
                if (air[i][0] > 0) air[i][0]--;
                if (air[i][n - 1] > 0) air[i][n - 1]--;
            }
        }
    }

    private static boolean allOfficeOverK() {
        for (int[] office : offices) {
            if (air[office[0]][office[1]] < k)
                return false;
        }
        return true;
    }

    private static boolean inRange(int x, int y) {
        return 0 <= x && x < n && 0 <= y && y < n;
    }
}
