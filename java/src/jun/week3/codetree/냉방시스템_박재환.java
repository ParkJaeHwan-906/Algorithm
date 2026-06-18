package jun.week3.codetree;

import java.util.*;
import java.io.*;

/**
 * [풀이 시간]
 * 01:17:42
 * AI 사용 여부 O
 * => 초기에는 dx, dy로 각 방향을 관리하려했음 -> 복잡도 증가 -> 디버깅 어려움
 *      => 각 에어컨마다 이동할 수 있는 방향은 3방향 밖에 없음 -> 분기처리
 *
 * => 벽을 세울 때 입력으로 주어지는 위치만 표기함 -> 인접한 칸에도 반영
 */
public class 냉방시스템_박재환 {
    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        init(br);
        br.close();
    }

    static final int LEFT = 0;
    static final int UP = 1;
    static final int RIGHT = 2;
    static final int DOWN = 3;

    static StringTokenizer st;
    static int n, m, k;
    static int[][] board;
    static int[][] chillBoard;
    static List<Aircon> aircons;
    static List<Office> offices;
    static boolean[][][] walls;
    static void init(BufferedReader br) throws IOException {
        st = new StringTokenizer(br.readLine().trim());
        n = Integer.parseInt(st.nextToken());
        m = Integer.parseInt(st.nextToken());
        k = Integer.parseInt(st.nextToken());

        board = new int[n][n];
        chillBoard = new int[n][n];
        aircons = new ArrayList<>();
        offices = new ArrayList<>();
        for(int x = 0; x < n; x++) {
            st = new StringTokenizer(br.readLine().trim());
            /**
             * 0 : 빈칸
             * 1 : 사무실
             * 2 : 에어컨 ( < )
             * 3 : 에어컨 ( ^ )
             * 4 : 에어컨 ( > )
             * 5 : 에어컨 ( v )
             */
            for(int y = 0; y < n; y++) {
                board[x][y] = Integer.parseInt(st.nextToken());
                if(board[x][y] >= 2) {
                    Aircon aircon = new Aircon(x, y, board[x][y]);
                    aircons.add(aircon);
                } else if(board[x][y] == 1) {
                    Office office = new Office(x, y);
                    offices.add(office);
                }
            }
        }

        walls = new boolean[n][n][4];
        for(int i = 0; i < m; i++) {
            st = new StringTokenizer(br.readLine().trim());
            int x = Integer.parseInt(st.nextToken()) - 1;
            int y = Integer.parseInt(st.nextToken()) - 1;
            int dir = Integer.parseInt(st.nextToken());

            if(dir == 0) {		// ^
                walls[x][y][UP] = true;
                if(x - 1 >= 0) walls[x - 1][y][DOWN] = true;
            } else {			// <
                walls[x][y][LEFT] = true;
                if(y - 1 >= 0) walls[x][y - 1][RIGHT] = true;
            }
        }

        System.out.println(solution());
    }
    static class Aircon {
        int x, y;
        int dir;

        Aircon(int x, int y, int dir) {
            this.x = x;
            this.y = y;
            this.dir = dir - 2;
        }
    }

    static class Office {
        int x, y;

        Office(int x, int y) {
            this.x = x;
            this.y = y;
        }
    }

    static int solution() {
        for(int time = 0; time < 100; time++) {
            int[][] newChillBoard = operateAircon();
            computeChill(newChillBoard);
            decreaseChill();
            if(checkOffice()) return time + 1;
        }
        return -1;
    }

    static int[][] operateAircon() {
        int[][] airconBoard = new int[n][n];
        for(Aircon a : aircons) {
            chilling(a, airconBoard);
        }
        return airconBoard;
    }
    static void chilling(Aircon a, int[][] airconBoard) {
        Queue<int[]> q = new ArrayDeque<>();
        boolean[][] visited = new boolean[n][n];

        int sx = a.x, sy = a.y;
        if(a.dir == LEFT) sy--;
        else if(a.dir == UP) sx--;
        else if(a.dir == RIGHT) sy++;
        else if(a.dir == DOWN) sx++;

        if(isNotBoard(sx, sy)) return;		// 바람 전파 불가

        q.offer(new int[] {sx, sy, 5});
        visited[sx][sy] = true;
        airconBoard[sx][sy] += 5;

        while(!q.isEmpty()) {
            int[] cur = q.poll();
            int x = cur[0];
            int y = cur[1];
            int p = cur[2];

            if(p == 1) continue;		// 더 이상 전파 X

            if(a.dir == LEFT) {
                // <
                if (y - 1 >= 0 && !walls[x][y][LEFT] && !visited[x][y - 1]) {
                    airconBoard[x][y - 1] += (p - 1);
                    visited[x][y - 1] = true;
                    q.offer(new int[] {x, y - 1, p - 1});
                }
                // ^ <
                if (y - 1 >= 0 && x - 1 >= 0 && !walls[x][y][UP] && !walls[x - 1][y][LEFT] && !visited[x - 1][y - 1]) {
                    airconBoard[x - 1][y - 1] += (p - 1);
                    visited[x - 1][y - 1] = true;
                    q.offer(new int[] {x - 1, y - 1, p - 1});
                }
                // v <
                if (y - 1 >= 0 && x + 1 < n && !walls[x][y][DOWN] && !walls[x + 1][y][LEFT] && !visited[x + 1][y - 1]) {
                    airconBoard[x + 1][y - 1] += (p - 1);
                    visited[x + 1][y - 1] = true;
                    q.offer(new int[] {x + 1, y - 1, p - 1});
                }
            }
            else if(a.dir == UP) {
                // ^
                if (x - 1 >= 0 && !walls[x][y][UP] && !visited[x - 1][y]) {
                    airconBoard[x - 1][y] += (p - 1);
                    visited[x - 1][y] = true;
                    q.offer(new int[] {x - 1, y, p - 1});
                }
                // < ^
                if (y - 1 >= 0 && x - 1 >= 0 && !walls[x][y][LEFT] && !walls[x][y - 1][UP] && !visited[x - 1][y - 1]) {
                    airconBoard[x - 1][y - 1] += (p - 1);
                    visited[x - 1][y - 1] = true;
                    q.offer(new int[] {x - 1, y - 1, p - 1});
                }
                // > ^
                if (y + 1 < n && x - 1 >= 0 && !walls[x][y][RIGHT] && !walls[x][y + 1][UP] && !visited[x - 1][y + 1]) {
                    airconBoard[x - 1][y + 1] += (p - 1);
                    visited[x - 1][y + 1] = true;
                    q.offer(new int[] {x - 1, y + 1, p - 1});
                }
            }
            else if(a.dir == RIGHT) {
                // >
                if (y + 1 < n && !walls[x][y][RIGHT] && !visited[x][y + 1]) {
                    airconBoard[x][y + 1] += (p - 1);
                    visited[x][y + 1] = true;
                    q.offer(new int[] {x, y + 1, p - 1});
                }
                // ^ >
                if (y + 1 < n && x - 1 >= 0 && !walls[x][y][UP] && !walls[x - 1][y][RIGHT] && !visited[x - 1][y + 1]) {
                    airconBoard[x - 1][y + 1] += (p - 1);
                    visited[x - 1][y + 1] = true;
                    q.offer(new int[] {x - 1, y + 1, p - 1});
                }
                // v >
                if (y + 1 < n && x + 1 < n && !walls[x][y][DOWN] && !walls[x + 1][y][RIGHT] && !visited[x + 1][y + 1]) {
                    airconBoard[x + 1][y + 1] += (p - 1);
                    visited[x + 1][y + 1] = true;
                    q.offer(new int[] {x + 1, y + 1, p - 1});
                }
            }
            else if(a.dir == DOWN) {
                // v
                if (x + 1 < n && !walls[x][y][DOWN] && !visited[x + 1][y]) {
                    airconBoard[x + 1][y] += (p - 1);
                    visited[x + 1][y] = true;
                    q.offer(new int[] {x + 1, y, p - 1});
                }
                // < v
                if (y - 1 >= 0 && x + 1 < n && !walls[x][y][LEFT] && !walls[x][y - 1][DOWN] && !visited[x + 1][y - 1]) {
                    airconBoard[x + 1][y - 1] += (p - 1);
                    visited[x + 1][y - 1] = true;
                    q.offer(new int[] {x + 1, y - 1, p - 1});
                }
                // > v
                if (y + 1 < n && x + 1 < n && !walls[x][y][RIGHT] && !walls[x][y + 1][DOWN] && !visited[x + 1][y + 1]) {
                    airconBoard[x + 1][y + 1] += (p - 1);
                    visited[x + 1][y + 1] = true;
                    q.offer(new int[] {x + 1, y + 1, p - 1});
                }
            }
        }
    }
    static void computeChill(int[][] arr) {
        for(int x = 0; x < n; x++) {
            for(int y = 0; y < n; y++) {
                chillBoard[x][y] += arr[x][y];
            }
        }

        int[][] temp = new int[n][n];
        for(int x = 0; x < n; x++) {
            for(int y = 0; y < n; y++) {
                // v > 만 확인
                if(y + 1 < n && !walls[x][y][RIGHT]) {
                    int a = chillBoard[x][y];
                    int b = chillBoard[x][y + 1];
                    int diff = Math.abs(a - b) / 4;
                    if(a > b) {
                        temp[x][y] -= diff;
                        temp[x][y + 1] += diff;
                    } else {
                        temp[x][y] += diff;
                        temp[x][y + 1] -= diff;
                    }
                }
                if(x + 1 < n && !walls[x][y][DOWN]) {
                    int a = chillBoard[x][y];
                    int b = chillBoard[x + 1][y];
                    int diff = Math.abs(a - b) / 4;
                    if(a > b) {
                        temp[x][y] -= diff;
                        temp[x + 1][y] += diff;
                    } else {
                        temp[x][y] += diff;
                        temp[x + 1][y] -= diff;
                    }
                }
            }
        }

        for(int x = 0; x < n; x++) {
            for(int y = 0; y < n; y++) {
                chillBoard[x][y] += temp[x][y];
            }
        }
    }
    static void decreaseChill() {
        for(int x = 0; x < n; x++) {
            chillBoard[x][0] = Math.max(0,  chillBoard[x][0] - 1);
            chillBoard[x][n - 1] = Math.max(0,  chillBoard[x][n - 1] - 1);
        }

        for(int y = 1; y < n - 1; y++) {
            chillBoard[0][y] = Math.max(0,  chillBoard[0][y] - 1);
            chillBoard[n - 1][y] = Math.max(0,  chillBoard[n - 1][y] - 1);
        }
    }
    static boolean checkOffice() {
        for(Office o : offices) {
            if(chillBoard[o.x][o.y] < k) return false;
        }
        return true;
    }
    // ====
    static boolean isNotBoard(int x, int y) {
        return x < 0 || y < 0 || x >= n || y >= n;
    }
}
