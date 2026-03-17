package mar.week3.codetree;

import java.util.*;
import java.io.*;

public class 개구리의여행_박재환 {
    static BufferedReader br;
    static StringBuilder sb;
    public static void main(String[] args) throws IOException {
        br = new BufferedReader(new InputStreamReader(System.in));
        sb = new StringBuilder();
        init();
        br.close();
        System.out.println(sb);
    }
    static StringTokenizer st;
    static int n;
    static int[][] board;
    static void init() throws IOException {
        n = Integer.parseInt(br.readLine().trim());
        board = new int[n][n];

        for(int x=0; x<n; x++) {
            /**
             * 안전한 돌 : .
             * 미끄러운 돌: S
             * 천적이 사는 돌: #
             */
            String line = br.readLine().trim();
            for(int y=0; y<n; y++) board[x][y] = line.charAt(y);
        }

        int q = Integer.parseInt(br.readLine().trim());
        while(q-- > 0) {
            st = new StringTokenizer(br.readLine().trim());
            int result = travel();
            sb.append(result).append('\n');
        }
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

        public int compareTo(State o) {
            return Integer.compare(this.time, o.time);
        }
    }
    static final int INF = 987654321;

    static int[] dx = {0,1,0,-1};
    static int[] dy = {1,0,-1,0};
    static int sx, sy, ex, ey;
    static int travel() {
        sx = Integer.parseInt(st.nextToken())-1;
        sy = Integer.parseInt(st.nextToken())-1;
        ex = Integer.parseInt(st.nextToken())-1;
        ey = Integer.parseInt(st.nextToken())-1;

        PriorityQueue<State> q = new PriorityQueue<>();
        int[][][] visited = new int[n][n][6];       // 현재 칸에, 현재 점프력을 가지고 방문한 이력이 있는지
        for(int x=0; x<n; x++) {
            for(int y=0; y<n; y++) Arrays.fill(visited[x][y], INF);
        }

        q.offer(new State(sx, sy, 1, 0));
        visited[sx][sy][1] = 0;

        while(!q.isEmpty()) {
            State cur = q.poll();
            if(cur.x == ex && cur.y == ey) return cur.time;
            if(visited[cur.x][cur.y][cur.jump] < cur.time) continue;
            // 1. 점프
            for(int dir=0; dir<4; dir++) {
                int nx = cur.x + dx[dir] * cur.jump;
                int ny = cur.y + dy[dir] * cur.jump;
                if(nx < 0 || ny < 0 || nx >= n || ny >= n) continue;
                if(visited[nx][ny][cur.jump] <= cur.time + 1) continue;
                if(board[nx][ny] == 'S' || board[nx][ny] == '#') continue;
                if(!isSafe(cur.x, cur.y, cur.jump, dir)) continue;

                visited[nx][ny][cur.jump] = cur.time + 1;
                q.offer(new State(nx, ny, cur.jump, visited[nx][ny][cur.jump]));
            }
            // 2. 점프력 증가
            if(cur.jump < 5) {
                int nJump = cur.jump + 1;
                int nTime = cur.time + (nJump * nJump);
                if(visited[cur.x][cur.y][nJump] > nTime) {
                    visited[cur.x][cur.y][nJump] = nTime;
                    q.offer(new State(cur.x, cur.y, nJump, visited[cur.x][cur.y][nJump]));
                }
            }
            // 3. 점프력 감소
            if(cur.jump > 1) {
                int nTime = cur.time + 1;
                for(int i=cur.jump-1; i>=1; i--) {
                    if(visited[cur.x][cur.y][i] > nTime) {
                        visited[cur.x][cur.y][i] = nTime;
                        q.offer(new State(cur.x, cur.y, i, visited[cur.x][cur.y][i]));
                    }
                }
            }
        }

        return -1;
    }
    static boolean isSafe(int x, int y, int jump, int dir) {
        for(int i=1;i<=jump;i++) {
            int nx = x + dx[dir]*i;
            int ny = y + dy[dir]*i;

            if(board[nx][ny] == '#') return false;
        }
        return true;
    }
}
