package sep.week2.codetree;

import java.util.*;
import java.io.*;

public class 개구리의여행_박재환 {
    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        init(br);
        br.close();
    }

    static final int INF = 987_654_321;

    static final int[] dx = {0, 1, 0, -1};
    static final int[] dy = {1, 0, -1, 0};

    static final char SAFE = '.';
    static final char SLIP = 'S';
    static final char DANGER = '#';

    static int n;
    static char[][] board;
    static void init(BufferedReader br) throws IOException {
        StringTokenizer st;
        StringBuilder sb = new StringBuilder();
        n = Integer.parseInt(br.readLine().trim());
        board = new char[n][n];
        for(int x = 0; x < n; x++) {
            String line = br.readLine().trim();
            for(int y = 0; y < n; y++) {
                board[x][y] = line.charAt(y);
            }
        }

        int q = Integer.parseInt(br.readLine().trim());
        while(q-- > 0) {
            st = new StringTokenizer(br.readLine().trim());
            int r1 = Integer.parseInt(st.nextToken()) - 1;
            int c1 = Integer.parseInt(st.nextToken()) - 1;
            int r2 = Integer.parseInt(st.nextToken()) - 1;
            int c2 = Integer.parseInt(st.nextToken()) - 1;
            sb.append(solution(r1, c1, r2, c2)).append('\n');
        }
        System.out.print(sb);
    }

    static class State implements Comparable<State> {
        int x, y;
        int jump;
        int time;
        State(int x, int y, int jump, int time) {
            this.x = x;
            this.y = y;
            this.jump = jump;
            this.time = time;
        }
        @Override
        public int compareTo(State s) {
            return Integer.compare(this.time, s.time);
        }
    }
    static int solution(int r1, int c1, int r2, int c2) {
        PriorityQueue<State> pq = new PriorityQueue<>();
        int[][][] visited = new int[n][n][6];
        for(int x = 0; x < n; x++) {
            for(int y = 0; y < n; y++) {
                Arrays.fill(visited[x][y], INF);
            }
        }

        State init = new State(r1, c1, 1, 0);       // 초기 상태
        visited[init.x][init.y][init.jump] = init.time;
        pq.offer(init);

        while(!pq.isEmpty()) {
            State cur = pq.poll();
            if(cur.x == r2 && cur.y == c2) {
                visited[cur.x][cur.y][cur.jump] = Math.min(visited[cur.x][cur.y][cur.jump], cur.time);
                continue;
            }

            // 1. 점프
            for(int dir = 0; dir < 4; dir++) {
                int nx = cur.x + dx[dir] * cur.jump;
                int ny = cur.y + dy[dir] * cur.jump;
                if(isNotBoard(nx, ny) || !isSafe(cur.x, cur.y, cur.jump, dir)) {
                    continue;
                }

                if(visited[nx][ny][cur.jump] > cur.time + 1) {
                    visited[nx][ny][cur.jump] = cur.time + 1;
                    pq.offer(new State(nx, ny, cur.jump, cur.time + 1));
                }
            }
            // 2. 점프 증가
            if(cur.jump < 5) {
                int nextJump = cur.jump + 1;
                if(visited[cur.x][cur.y][nextJump] > cur.time + (nextJump * nextJump)) {
                    visited[cur.x][cur.y][nextJump] = cur.time + (nextJump * nextJump);
                    pq.offer(new State(cur.x, cur.y, nextJump, cur.time + (nextJump * nextJump)));
                }
            }
            // 3. 점프 감소
            if(cur.jump > 1) {
                for(int nextJump = 1; nextJump < cur.jump; nextJump++) {
                    if(visited[cur.x][cur.y][nextJump] > cur.time + 1) {
                        visited[cur.x][cur.y][nextJump] = cur.time + 1;
                        pq.offer(new State(cur.x, cur.y, nextJump, cur.time + 1));
                    }
                }
            }
        }

        int minTime = INF;
        for(int i : visited[r2][c2]) {
            minTime = Math.min(minTime, i);
        }
        return minTime == INF ? -1 : minTime;
    }
    static boolean isNotBoard(int x, int y) {
        return x < 0 || y < 0 || x >= n || y >= n;
    }
    static boolean isSafe(int x, int y, int offset, int dir) {
        for(int i = 0; i < offset; i++) {
            x += dx[dir];
            y += dy[dir];
            if(board[x][y] == DANGER) {
                return false;
            }
        }
        return board[x][y] == SAFE;
    }
}
