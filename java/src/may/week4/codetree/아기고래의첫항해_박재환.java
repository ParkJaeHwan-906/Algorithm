package may.week4.codetree;

import java.util.*;
import java.io.*;

/**
 * [풀이시간]
 * 00:52:48
 * AI 사용 여부 X
 */
public class 아기고래의첫항해_박재환 {
    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        init(br);
        br.close();
    }
    final static int[] dx = {-1, 1, 0, 0};
    final static int[] dy = {0, 0, -1, 1};

    static int n, r, c, d;
    static int[][] board;
    static void init(BufferedReader br) throws IOException {
        StringTokenizer st;

        st = new StringTokenizer(br.readLine().trim());
        n = Integer.parseInt(st.nextToken());
        r = Integer.parseInt(st.nextToken()) - 1;
        c = Integer.parseInt(st.nextToken()) - 1;
        d = Integer.parseInt(st.nextToken()) - 1;

        board = new int[n][n];
        for(int x = 0; x < n; x++) {
            st = new StringTokenizer(br.readLine().trim());
            for(int y = 0; y < n; y++) {
                board[x][y] = Integer.parseInt(st.nextToken());
            }
        }

        System.out.println(solution());
    }
    /**
     * N x N 크기의 격자
     * - 0 : 바다 (이동 가능)
     * - 1 : 암초 (이동 불가)
     *
     * (r, c) : 아기고래의 첫 위치
     * d : 아기고래의 첫 바라보는 방향 ( 1 : 상, 2 : 하, 3 : 좌, 4 : 우 )
     */
    static boolean[][] visited;
    static String solution() {
        StringBuilder sb = new StringBuilder();
        visited = new boolean[n][n];

        // 첫 시작 위치
        sb.append(String.format("%d %d\n", r + 1, c + 1));
        visited[r][c] = true;

        while(true) {
            // 1. 인접탐험
            // -> 인접한 방향으로 더 이상 이동할 수 없다면 -> 2단계 이동
            if(moveAdj()) {
                sb.append(String.format("%d %d\n", r + 1, c + 1));
                visited[r][c] = true;
                continue;
            }
            // 2. 가장 가까운 바다로 이동
            if(nearMove()) {
                sb.append(String.format("%d %d\n", r + 1, c + 1));
                visited[r][c] = true;
                continue;
            }
            break;
        }

        return sb.toString();
    }

    // ==== 1. 인접 탐험 ====
    static boolean moveAdj() {
        /**
         * 1. 현재 바라보는 방향으로 직진
         * 2. 좌회전 후 직진
         * 3. 우회전 후 직진
         * 4. 180도 화전 후 직진
         */

        // 1. 현재 바라보는 방향으로 직진
        if(canMove(r, c, d)) {
            r = r + dx[d];
            c = c + dy[d];
            return true;
        }

        // 2. 좌회전 후 직진
        if(canMove(r, c, counterClockWise(d))) {
            d = counterClockWise(d);
            r = r + dx[d];
            c = c + dy[d];
            return true;
        }

        // 3. 우회전 후 직진
        if(canMove(r, c, clockWise(d))) {
            d = clockWise(d);
            r = r + dx[d];
            c = c + dy[d];
            return true;
        }

        // 4. 180도 회전 후 직진
        if(canMove(r, c, oppositeDir(d))) {
            d = oppositeDir(d);
            r = r + dx[d];
            c = c + dy[d];
            return true;
        }

        return false;
    }
    static boolean canMove(int x, int y, int dir) {
        int nx = x + dx[dir];
        int ny = y + dy[dir];

        if(isNotBoard(nx, ny)) return false;
        if(visited[nx][ny]) return false;
        if(board[nx][ny] == 1) return false;

        return true;
    }
    // ( 1 : 상, 2 : 하, 3 : 좌, 4 : 우 )
    static int oppositeDir(int dir) {
        if(dir == 0) return 1;      // 상 -> 하
        if(dir == 1) return 0;      // 하 -> 상
        if(dir == 2) return 3;      // 좌 -> 우
        return 2;                   // 우 -> 좌
    }
    static int clockWise(int dir) {
        if(dir == 0) return 3;      // 상 -> 우
        if(dir == 1) return 2;      // 하 -> 좌
        if(dir == 2) return 0;      // 좌 -> 상
        return 1;                   // 우 -> 하
    }
    static int counterClockWise(int dir) {
        if(dir == 0) return 2;      // 상 -> 좌
        if(dir == 1) return 3;      // 하 -> 우
        if(dir == 2) return 1;      // 좌 -> 하
        return 0;                   // 우 -> 상
    }

    // ==== 2. 가장 가까운 바다로 이동 ====
    static boolean nearMove() {
        /**
         * 거리 : 상하좌우 인접한 칸 중 가장 가까운 거리
         *      - 암초는 지나갈 수 없지만, 이미 방문한 바다는 지나갈 수 있음
         * 행 번호가 가장 작고, 열 번호가 가장 작은 우선순위
         * 이동거리는 좌 하 우 상 순서로 선택한다.
         * 도착 후 바라보는 방향은 마지막 이동 방향으로 갱신된다.
         */
        int[] priorityDir = {2, 1, 3, 0};       // 좌 하 우 상 순서로 이동우선순위

        Queue<int[]> q = new ArrayDeque<>();
        boolean[][] tempVisited = new boolean[n][n];

        q.offer(new int[] {r, c, -1, 0});
        tempVisited[r][c] = true;

        // 가장 최적 위치
        int bestX = Integer.MAX_VALUE;
        int bestY = Integer.MAX_VALUE;
        int bestDist = Integer.MAX_VALUE;
        int bestDir = Integer.MAX_VALUE;

        while(!q.isEmpty()) {
            int[] cur = q.poll();
            int x = cur[0];
            int y = cur[1];
            int prevDir = cur[2];
            int dist = cur[3];
            if(board[x][y] == 0 && !visited[x][y]) {
                // 아직 방문하기 전의 바다라면
                if(bestDist > dist || (bestDist == dist && (bestX > x || (bestX == x && bestY > y)))) {
                    bestDist = dist;
                    bestX = x;
                    bestY = y;
                    bestDir = prevDir;
                }
                continue;
            }

            if(dist >= bestDist) continue;

            for(int dir : priorityDir) {
                int nx = x + dx[dir];
                int ny = y + dy[dir];

                if(isNotBoard(nx, ny)) continue;
                if(tempVisited[nx][ny]) continue;
                if(board[nx][ny] == 1) continue;

                q.offer(new int[] {nx, ny, dir, dist + 1});
                tempVisited[nx][ny] = true;
            }
        }

        if(bestX == Integer.MAX_VALUE && bestY == Integer.MAX_VALUE) return false;

        r = bestX;
        c = bestY;
        d = bestDir;

        return true;
    }

    static boolean isNotBoard(int x, int y) {
        return x < 0 || y < 0 || x >= n || y >= n;
    }
}
