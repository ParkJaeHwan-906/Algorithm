package apr.week2.codetree;

import java.util.*;
import java.io.*;

public class 아기고래의첫항해_박재환 {
    static BufferedReader br;
    static StringBuilder sb;
    public static void main(String[] args) throws IOException {
        br = new BufferedReader(new InputStreamReader(System.in));
        sb = new StringBuilder();
        init();
        br.close();
        System.out.println(sb);
    }
    static class Wale {
        int x, y;
        int dir;

        Wale(int x, int y, int dir) {
            this.x = x;
            this.y = y;
            this.dir = dir;
        }

        int rotateClockWise() {
            /**
             * 시계방향 회전
             */
            if(this.dir == 0) {     // 상 -> 우
                return 3;
            } else if(this.dir == 1) {  // 하 -> 좌
                return 2;
            } else if(this.dir == 2) {  // 좌 -> 상
                return 0;
            } else if(this.dir == 3) {  // 우 -> 하
                return 1;
            }
            return -1;
        }

        int rotateCounterClockWise() {
            /**
             * 반시계방향 회전
             */
            if(this.dir == 0) {     // 상 -> 좌
                return 2;
            } else if(this.dir == 1) {  // 하 -> 우
                return 3;
            } else if(this.dir == 2) {  // 좌 -> 하
                return 1;
            } else if(this.dir == 3) {  // 우 -> 상
                return 0;
            }
            return -1;
        }

        int rotateOpposite() {
            /**
             * 180도 회전
             */
            if(this.dir == 0) {     // 상 -> 하
                return 1;
            } else if(this.dir == 1) {  // 하 -> 상
                return 0;
            } else if(this.dir == 2) {  // 좌 -> 우
                return 3;
            } else if(this.dir == 3) {  // 우 -> 좌
                return 2;
            }
            return -1;
        }
    }
    // 상(0) 하(1) 좌(2) 우(3)
    static final int[] dx = {-1, 1, 0, 0};
    static final int[] dy = {0, 0, -1, 1};

    static StringTokenizer st;
    static int n, r, c, d;
    static int[][] board;
    static Wale wale;
    static void init() throws IOException {
        st = new StringTokenizer(br.readLine().trim());
        n = Integer.parseInt(st.nextToken());
        // 초기 고래 설정
        r = Integer.parseInt(st.nextToken()) - 1;
        c = Integer.parseInt(st.nextToken()) - 1;
        d = Integer.parseInt(st.nextToken()) - 1;

        wale = new Wale(r, c, d);
        board = new int[n][n];
        for(int x = 0; x < n; x++) {
            st = new StringTokenizer(br.readLine().trim());
            for(int y = 0; y < n; y++) {
                /**
                 * 0 : 바다
                 * 1 : 암초
                 */
                board[x][y] = Integer.parseInt(st.nextToken());
            }
        }

        solution();
    }
    static boolean[][] visited;
    static void solution() {
        visited = new boolean[n][n];
        sb.append(String.format("%d %d\n", wale.x + 1, wale.y + 1));
        visited[wale.x][wale.y] = true;
        while(true) {
            if(moveAdjGrid()) {
                sb.append(String.format("%d %d\n", wale.x + 1, wale.y + 1));
                continue;
            }
            if(moveNearGrid()) {
                sb.append(String.format("%d %d\n", wale.x + 1, wale.y + 1));
                continue;
            }
            break;
        }
    }
    /**
     * [인접 탐험]
     * 상 하 좌 우 인접한 칸 중, 아직 방문하지 않은 바다 칸을 방문
     * 1. 현재 바라보는 방향으로 직진
     * 2. 좌회전(90도 반시계 방향 회전) 후 직진
     * 3. 우회전(90도 시계 방향 회전) 후 직진
     * 4. 180도 회전 후 직진
     */
    static boolean moveAdjGrid() {
//        System.out.printf("[ADJ] 현재 위치 : (%d, %d)\n", wale.x, wale.y);
//        System.out.println("[직진]");
        // 1. 현재 바라보는 칸으로 직진
        if(canMove(wale, wale.dir)) {
            move(wale, wale.dir);
//            System.out.printf("[OK] 이동 : (%d, %d)\n", wale.x, wale.y);
            return true;
        }
//        System.out.println("[좌회전]");
        // 2. 좌회전 후 직진 
        int afterL = wale.rotateCounterClockWise();
        if(canMove(wale, afterL)) {
            move(wale, afterL);
//            System.out.printf("[OK] 이동 : (%d, %d)\n", wale.x, wale.y);
            return true;
        }
//        System.out.println("[우회전]");
        // 3. 우회전 후 직진
        int afterR = wale.rotateClockWise();
        if(canMove(wale, afterR)) {
            move(wale, afterR);
//            System.out.printf("[OK] 이동 : (%d, %d)\n", wale.x, wale.y);
            return true;
        }
//        System.out.println("[반대]");
        // 4. 반대 방향
        int afterOpposite = wale.rotateOpposite();
        if(canMove(wale, afterOpposite)) {
            move(wale, afterOpposite);
//            System.out.printf("[OK] 이동 : (%d, %d)\n", wale.x, wale.y);
            return true;
        }
        return false;
    }
    static boolean canMove(Wale wale, int dir) {
        int nx = wale.x + dx[dir];
        int ny = wale.y + dy[dir];
        if(isNotBoard(nx, ny)) return false;        // 격자 밖
        if(visited[nx][ny]) return false;           // 이미 방문
        if(board[nx][ny] == 1) return false;        // 산호

        return true;
    }
    static void move(Wale wale, int dir) {
        int nx = wale.x + dx[dir];
        int ny = wale.y + dy[dir];
        visited[nx][ny] = true;
        wale.dir = dir;
        wale.x = nx;
        wale.y = ny;
    }
    /**
     * [가장 가까운 바다로 이동]
     * 인접한 칸 모두 방문 가능한 바다가 없다면
     * -> 아직 방문하지 않은 바다 칸 중 현재 위치에서 가장 가까운 칸 찾아 이동
     *
     * - 거리 : 상 하 좌 우 인접한 칸을 이동한 횟수 (암초는 이동 블가) -> 이미 방문한 바다는 지나갈 수 있다.
     * - 가장 가까운 칸이 여러 개 -> 행 번호, 열 번호 가장 작은 칸
     * - 선택한 칸까지 최단 거리로 이동 (선택한 칸까지 거리가 1줄어드는 방향)
     *      -> 좌 하 우 상 순서로 이동
     * - 도착 후 바라보는 방향은 마지막 이동 방향으로 갱신
     */
    static class Node {
        int x, y;
        int d;

        Node(int x, int y, int d) {
            this.x = x;
            this.y = y;
            this.d = d;
        }
    }
    static boolean moveNearGrid() {
//        System.out.printf("[NEAR] 현재 위치 : (%d, %d)\n", wale.x, wale.y);
        Queue<Node> q = new ArrayDeque<>();
        boolean[][] temp = new boolean[n][n];

        int x = wale.x;
        int y = wale.y;

        q.offer(new Node(x, y, 0));
        temp[x][y] = true;

        // 다음 이동 위치 -> 행 열 가장 작은 위치
        int bestX = Integer.MAX_VALUE;
        int bestY = Integer.MAX_VALUE;
        int bestD = Integer.MAX_VALUE;

        while (!q.isEmpty()) {
            Node cur = q.poll();
            // 현 위치가 방문한 적 없는 바다 칸이라면
            if (!visited[cur.x][cur.y] && board[cur.x][cur.y] == 0) {
                if (bestD > cur.d) {
                    bestX = cur.x;
                    bestY = cur.y;
                    bestD = cur.d;
                } else if (bestD == cur.d && (bestX > cur.x || (bestX == cur.x && bestY > cur.y))) {
                    bestX = cur.x;
                    bestY = cur.y;
                }
                continue;
            }

            for (int dir = 0; dir < 4; dir++) {
                int nx = cur.x + dx[dir];
                int ny = cur.y + dy[dir];
                if (isNotBoard(nx, ny)) continue;
                if (temp[nx][ny]) continue;
                if (board[nx][ny] == 1) continue;

                temp[nx][ny] = true;
                q.offer(new Node(nx, ny,cur.d + 1));
            }
        }

        if(bestX == Integer.MAX_VALUE && bestY == Integer.MAX_VALUE) return false;

        moveToBestLoc(bestX, bestY);
        return true;
    }
    static void moveToBestLoc(int bestX, int bestY) {
        // 좌 하 우 상
        int[] order = {2, 1, 3, 0};
        Queue<int[]> q = new ArrayDeque<>();
        boolean[][] temp = new boolean[n][n];

        int x = wale.x;
        int y = wale.y;

        q.offer(new int[] {x, y, wale.dir});
        temp[x][y] = true;

        while(!q.isEmpty()) {
            int[] cur = q.poll();
            if(cur[0] == bestX && cur[1] == bestY) {
                wale.x = cur[0];
                wale.y = cur[1];
                wale.dir = cur[2];
                visited[wale.x][wale.y] = true;
                break;
            }
            for(int d = 0; d < 4; d++) {
                int dir = order[d];
                int nx = cur[0] + dx[dir];
                int ny = cur[1] + dy[dir];
                if(isNotBoard(nx, ny)) continue;
                if(temp[nx][ny]) continue;
                if(board[nx][ny] == 1) continue;

                temp[nx][ny] = true;
                q.offer(new int[] {nx, ny, dir});
            }
        }
    }
    // ===
    static boolean isNotBoard(int x, int y) {
        return x < 0 || y < 0 || x>= n || y >=n;
    }
}
