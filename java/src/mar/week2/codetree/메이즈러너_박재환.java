package mar.week2.codetree;

import java.util.*;
import java.io.*;

public class 메이즈러너_박재환 {
    static BufferedReader br;
    public static void main(String[] args) throws IOException {
        br = new BufferedReader(new InputStreamReader(System.in));
        init();
        br.close();
    }
    /**
     * N x N 격자 (1,1)
     * - 빈 칸
     * - 벽
     * - 출구
     *
     * 참가자
     * - 모든 참가자는 동시에 움직임
     * - 멘헤튼 거리 사용
     * - 상하좌우 이동 가능
     *      - 이동하는 칸은 출구로 가까워져야한다.
     * - 움직일 수 없다면 움직이지 않음
     * - 한 칸에 다수가 있을 수 있음
     *
     * 미로 회전
     * - 한 명 이상의 참가자와 출구를 포함한 가장 작은 정사각형을 잡는다.
     * - 선택된 정사각형은 시계방향으로 90도 회전한다. (초함된 벽은 내구도가 1씩 깎인다.)
     */
    static class Exit {
        int x, y;
        Exit(int x, int y) { this.x = x; this.y = y; }
    }
    static class Player {
        int id;
        int x, y;
        boolean exit;
        Player(int id, int x, int y) { this.id = id; this.x = x; this.y =y; this.exit = false; }
    }
    static StringTokenizer st;
    static int n, m, k;
    static int[][] board;
    static Player[] players;
    static Exit exit;
    static void init() throws IOException {
        st = new StringTokenizer(br.readLine().trim());
        n = Integer.parseInt(st.nextToken());       // 격자 크기
        m = Integer.parseInt(st.nextToken());       // 참가자 수
        k = Integer.parseInt(st.nextToken());       // 턴 수

        board = new int[n][n];
        for(int x=0; x<n; x++) {
            st = new StringTokenizer(br.readLine().trim());
            for(int y=0; y<n; y++) board[x][y] = Integer.parseInt(st.nextToken());

        }

        players = new Player[m+1];
        for(int i=1; i<m+1; i++) {
            st = new StringTokenizer(br.readLine().trim());
            int x = Integer.parseInt(st.nextToken())-1;
            int y = Integer.parseInt(st.nextToken())-1;
            players[i] = new Player(i, x, y);
        }

        st = new StringTokenizer(br.readLine().trim());
        int x = Integer.parseInt(st.nextToken())-1;
        int y = Integer.parseInt(st.nextToken())-1;
        exit = new Exit(x, y);

        solution();
        System.out.printf("%d\n%d %d", moveCount, exit.x+1, exit.y+1);
    }
    static int moveCount;
    static void solution() {
        moveCount = 0;

        while(k-- > 0) {
            // 1. 참가자들 움직이기
            movePlayers();
            if(allExit()) break;
            // 2. 미로 회전
            findSmallestSquare();
        }
    }
    static int[] dx = {1,-1,0,0};
    static int[] dy = {0,0,1,-1};
    static void movePlayers() {
        for(int i=1; i<m+1; i++) {
            Player p = players[i];
            if(p.exit) continue;
            if(nextGrid(p)) moveCount++;
            if(p.x == exit.x && p.y == exit.y) p.exit = true;
        }
    }
    static boolean nextGrid(Player p) {
        int x = p.x;
        int y = p.y;
        int curDist = distFromExit(x, y);
        for(int dir=0; dir<4; dir++) {
            int nx = x + dx[dir];
            int ny = y + dy[dir];
            if(isNotBoard(nx, ny)) continue;
            if(board[nx][ny] > 0) continue;
            int nextDist = distFromExit(nx, ny);
            if(nextDist < curDist) {
                p.x = nx;
                p.y = ny;
                return true;
            }
        }
        return false;
    }
    static int distFromExit(int x, int y) {
        return Math.abs(exit.x - x) + Math.abs(exit.y - y);
    }
    static void findSmallestSquare() {
        int bestDist = Integer.MAX_VALUE;
        int bestX = Integer.MAX_VALUE, bestY = Integer.MAX_VALUE;
        for(int i=1; i<m+1; i++) {
            Player p = players[i];
            if(p.exit) continue;
            int size = Math.max(Math.abs(p.x - exit.x), Math.abs(p.y - exit.y)) + 1;

            int sx = Math.max(p.x, exit.x) - size + 1;
            int sy = Math.max(p.y, exit.y) - size + 1;

            if (sx < 0) sx = 0;
            if (sy < 0) sy = 0;

            if (bestDist > size ||
                    (bestDist == size && bestX > sx) ||
                    (bestDist == size && bestX == sx && bestY > sy)) {
                bestDist = size;
                bestX = sx;
                bestY = sy;
            }
        }
        rotateBoard(bestX, bestY, bestDist);
    }
    static void rotateBoard(int x, int y, int dist){
        int[][] origin = new int[dist][dist];
        for(int i=0;i<dist;i++){
            for(int j=0;j<dist;j++){
                /**
                 * 벽인경우 내구도 감소
                 */
                origin[i][j] = board[x+i][y+j] > 0 ?
                        board[x+i][y+j]-1 :
                        board[x+i][y+j];
            }
        }

        int[][] temp = new int[dist][dist];
        for(int i=0;i<dist;i++){
            for(int j=0;j<dist;j++){
                temp[i][j] = origin[dist-j-1][i];
            }
        }
        for(int i=0;i<dist;i++){
            for(int j=0;j<dist;j++){
                board[x+i][y+j] = temp[i][j];
            }
        }
        for(int i=1;i<=m;i++){
            Player p = players[i];
            if(p.exit) continue;
            // 회전 영역에 포함되어 있는 경우
            if(p.x>=x && p.x<x+dist && p.y>=y && p.y<y+dist){
                int lx = p.x-x;
                int ly = p.y-y;

                int rx = ly;
                int ry = dist-lx-1;

                p.x = x+rx;
                p.y = y+ry;
            }
        }

        if(exit.x>=x && exit.x<x+dist &&
                exit.y>=y && exit.y<y+dist){

            int lx = exit.x-x;
            int ly = exit.y-y;

            int rx = ly;
            int ry = dist-lx-1;

            exit.x = x+rx;
            exit.y = y+ry;
        }
    }
    static boolean isNotBoard(int x, int y) {
        return x < 0 || y < 0 || x >= n || y >= n;
    }
    static boolean allExit() {
        for(int i=1;i<=m;i++){
            if(!players[i].exit) return false;
        }
        return true;
    }
}
