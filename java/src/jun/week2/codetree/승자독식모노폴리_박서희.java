package jun.week2.codetree;

import java.io.*;
import java.util.*;

/*
  문제풀이 시간: 01:54:07
  AI 사용 여부: O 풀다가 로직이 복잡해서 사용함.
 */
public class 승자독식모노폴리_박서희 {

    static final int[] dx = {-1, 1, 0, 0};
    static final int[] dy = {0, 0, -1, 1};

    static int n, m, k;
    static int[][][] board;
    static Player[] players;
    static int turn = 0;

    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer st = new StringTokenizer(br.readLine());

        n = Integer.parseInt(st.nextToken());
        m = Integer.parseInt(st.nextToken());
        k = Integer.parseInt(st.nextToken());

        players = new Player[m + 1];
        board = new int[n][n][2];
        for (int i = 0; i < n; i++) {
            st = new StringTokenizer(br.readLine());
            for (int j = 0; j < n; j++) {
                int id = Integer.parseInt(st.nextToken());
                if (id > 0) {
                    board[i][j][0] = id;
                    board[i][j][1] = k;
                    players[id] = new Player(id, i, j);
                }
            }
        }

        st = new StringTokenizer(br.readLine());
        for (int i = 1; i <= m; i++) {
            players[i].dir = Integer.parseInt(st.nextToken()) - 1;
        }

        // 위 아래 왼쪽 오른쪽을 향할 때의 우선순위
        for (int i = 1; i <= m; i++) {
            for (int d = 0; d < 4; d++) {
                st = new StringTokenizer(br.readLine());
                for (int l = 0; l < 4; l++) {
                    players[i].priority[d][l] = Integer.parseInt(st.nextToken()) - 1;
                }
            }
        }

        simulate();

    }

    static void simulate() {
        while (turn < 1000) {
            turn++;

            int[][] nextPos = new int[m + 1][3];
            for (int i = 1; i <= m; i++) {
                Player p = players[i];
                if (p == null) continue;

                int nextX = -1, nextY = -1, nextDir = -1;
                boolean hasEmpty = false;

                for (int j = 0; j < 4; j++) {
                    int d = p.priority[p.dir][j];
                    int nx = p.x + dx[d], ny = p.y + dy[d];

                    if (!inRange(nx, ny)) continue;
                    if (board[nx][ny][0] == 0) {
                        nextX = nx;
                        nextY = ny;
                        nextDir = d;
                        hasEmpty = true;
                        break;
                    }
                }

                if (!hasEmpty) {
                    for (int l = 0; l < 4; l++) {
                        int d = p.priority[p.dir][l];
                        int nx = p.x + dx[d], ny = p.y + dy[d];

                        if (!inRange(nx, ny)) continue;
                        if (board[nx][ny][0] == p.id) {
                            nextX = nx;
                            nextY = ny;
                            nextDir = d;
                            break;
                        }
                    }
                }

                nextPos[i][0] = nextX;
                nextPos[i][1] = nextY;
                nextPos[i][2] = nextDir;
            }

            int[][] tempMap = new int[n][n];

            for (int i = 1; i <= m; i++) {
                if (players[i] == null) continue;

                int nx = nextPos[i][0], ny = nextPos[i][1], ndir = nextPos[i][2];

                if (tempMap[nx][ny] > 0) players[i] = null;
                else {
                    tempMap[nx][ny] = i;
                    players[i].x = nx;
                    players[i].y = ny;
                    players[i].dir = ndir;
                }
            }

            downK();

            for (int i = 1; i <= m; i++) {
                if (players[i] == null) continue;

                board[players[i].x][players[i].y][0] = i;
                board[players[i].x][players[i].y][1] = k;
            }

            if (isOnlyOne()) {
                System.out.println(turn);
                return;
            }
        }

        System.out.println(-1);
    }

    static void downK() {
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                if (board[i][j][1] > 0) {
                    board[i][j][1]--;
                    if (board[i][j][1] == 0)
                        board[i][j][0] = 0;
                }
            }
        }
    }

    static boolean isOnlyOne() {
        for (int i = 2; i <= m; i++) {
            if (players[i] != null)
                return false;
        }
        return true;
    }

    static boolean inRange(int x, int y) {
        return 0 <= x && x < n && 0 <= y && y < n;
    }

    static class Player {
        int id;
        int x, y, dir;
        int[][] priority = new int[4][4];

        public Player(int id, int x, int y) {
            this.id = id;
            this.x = x;
            this.y = y;
        }
    }
}
