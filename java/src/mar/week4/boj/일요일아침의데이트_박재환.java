package mar.week4.boj;

import java.util.*;
import java.io.*;

public class 일요일아침의데이트_박재환 {
    static BufferedReader br;
    public static void main(String[] args) throws IOException {
        br = new BufferedReader(new InputStreamReader(System.in));
        init();
        br.close();
    }
    /**
     * 쓰레기를 통과해서 지나가는 것 X
     * 쓰레기를 따라 옆을 지나가는 것 X
     */
    static StringTokenizer st;
    static int n, m;
    static char[][] board;
    static void init() throws IOException {
        st = new StringTokenizer(br.readLine().trim());
        n = Integer.parseInt(st.nextToken());
        m = Integer.parseInt(st.nextToken());

        board = new char[n][m];
        for(int x=0; x<n; x++) {
            String line = br.readLine().trim();
            for(int y=0; y<m; y++) board[x][y] = line.charAt(y);
        }

        int[] result = solution();
        System.out.printf("%d %d", result[0], result[1]);
    }
    static class State implements Comparable<State> {
        int x, y;
        int passBy, nextBy;
        int totalMove;

        State(int x, int y, int passBy, int nextBy, int totalMove) {
            this.x = x;
            this.y = y;
            this.passBy = passBy;
            this.nextBy = nextBy;
            this.totalMove = totalMove;
        }

        public int compareTo(State o) {
            if(o.passBy == this.passBy) return Integer.compare(this.nextBy, o.nextBy);
            return Integer.compare(this.passBy, o.passBy);
        }
    }
    static final int[] dx = {0,1,0,-1};
    static final int[] dy = {1,0,-1,0};

    static int[] start;
    static int[] end;
    static final int INF = 50 * 50 + 7;
    static int[] solution() {
        findStartEnd();
        /**
         * S -> F
         * 쓰레기로 차있는 칸을 되도록 적게 지나가는 것
         * 만약 여러개라면, 옆을 지나가는 칸의 개수 최소
         *
         * => 가장 먼저 도착하는 것 X
         * => 특정 조건을 만족하는 최단
         */
        PriorityQueue<State> q = new PriorityQueue<>();
        int[][] visitedP = new int[n][m];
        int[][] visitedN = new int[n][m];
        for(int x=0; x<n; x++) {
            Arrays.fill(visitedP[x], INF);
            Arrays.fill(visitedN[x], INF);
        }
        // 시작칸은 포함 X
        q.offer(new State(start[0], start[1], 0, 0, 1));
        visitedP[start[0]][start[1]] = 0;        // 쓰레기 칸을 지나친 개수만
        visitedN[start[0]][start[1]] = 0;        // 쓰레기 칸 옆을 지나친 개수만

        int bestP = INF;
        int bestN = INF;
        while(!q.isEmpty()) {
            State cur = q.poll();

            if(cur.x == end[0] && cur.y == end[1]) {     // 목적지
                if(bestP > cur.passBy) {
                    bestP = cur.passBy;
                    bestN = cur.nextBy;
                } else if(bestP == cur.passBy && bestN > cur.nextBy) {
                    bestN = cur.nextBy;
                }
                continue;
            }

            if(visitedP[cur.x][cur.y] < cur.passBy) continue;
            if(visitedP[cur.x][cur.y] == cur.passBy &&
                    visitedN[cur.x][cur.y] < cur.nextBy) continue;

            for(int dir=0; dir<4; dir++) {
                int nx = cur.x + dx[dir];
                int ny = cur.y + dy[dir];
                if(nx < 0 || ny < 0 || nx >= n || ny >= m) continue;

                // 1. 다음 칸이 쓰레기 인지
                if(board[nx][ny] == 'g') {
                    if(visitedP[nx][ny] > cur.passBy + 1 ||
                            (visitedP[nx][ny] == cur.passBy + 1 && visitedN[nx][ny] > cur.nextBy)) {
                        q.offer(new State(nx, ny, cur.passBy + 1, cur.nextBy, cur.totalMove + 1));
                        visitedP[nx][ny] = cur.passBy + 1;
                        visitedN[nx][ny] = cur.nextBy;
                    }
                }
                // 2. 인접한 구역에 쓰레기가 있는지
                else if(board[nx][ny] == '.') {
                    int g = findG(nx, ny) ? 1 : 0;
                    if(visitedP[nx][ny] > cur.passBy ||
                            (visitedP[nx][ny] == cur.passBy && visitedN[nx][ny] > cur.nextBy + g)) {
                        q.offer(new State(nx, ny, cur.passBy, cur.nextBy + g, cur.totalMove + 1));
                        visitedP[nx][ny] = cur.passBy;
                        visitedN[nx][ny] = cur.nextBy + g;
                    }
                }
                // 3. 목적지
                else if(board[nx][ny] == 'F') {
                    if(visitedP[nx][ny] >= cur.passBy ||
                            (visitedP[nx][ny] == cur.passBy && visitedN[nx][ny] > cur.nextBy)) {
                        q.offer(new State(nx, ny, cur.passBy, cur.nextBy, cur.totalMove + 1));
                        visitedP[nx][ny] = cur.passBy;
                        visitedN[nx][ny] = cur.nextBy;
                    }
                }
            }
        }

        return new int[] {bestP, bestN};
    }
    static void findStartEnd() {
        for(int x=0; x<n; x++) {
            for(int y=0; y<m; y++) {
                if(board[x][y] == 'S') start = new int[] {x, y};
                else if(board[x][y] == 'F') end = new int[] {x, y};

                if(start != null && end != null) return;
            }
        }
    }
    static boolean findG(int x, int y) {
        for(int dir=0; dir<4; dir++) {
            int nx = x + dx[dir];
            int ny = y + dy[dir];
            if(nx < 0 || ny < 0 || nx >= n || ny >= m) continue;
            if(board[nx][ny] == 'g') return true;
        }
        return false;
    }
}
