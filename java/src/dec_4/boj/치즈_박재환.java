package dec_4.boj;

import java.util.*;
import java.io.*;

public class 치즈_박재환 {
    static BufferedReader br;
    public static void main(String[] args) throws IOException {
        br = new BufferedReader(new InputStreamReader(System.in));
        init();
        br.close();
    }
    /**
     *  N x M 격자가 있음 (N : 세로, M : 가로)
     *  4변 중, 2변 이상이 샐내 온도의 공기와 접촉하면 녹는다.
     */
    static StringTokenizer st;
    static int n, m;
    static int[][] map;
    static int cheese;
    static void init() throws IOException {
        cheese = 0;
        st = new StringTokenizer(br.readLine().trim());
        n = Integer.parseInt(st.nextToken());
        m = Integer.parseInt(st.nextToken());
        map = new int[n][m];
        for(int x=0; x<n; x++) {
            st = new StringTokenizer(br.readLine().trim());
            for(int y=0; y<m; y++) {
                map[x][y] = Integer.parseInt(st.nextToken());
                if(map[x][y] == 1) cheese++;
            }
        }
        System.out.println(meltCheese());
    }

    static int meltCheese() {
        int[] dx = {0,1,0,-1};
        int[] dy = {1,0,-1,0};

        int time = 0;

        while(cheese > 0) {
            time++;
            Queue<int[]> q = new ArrayDeque<>();
            int[][] cheeseStatus = new int[n][m];       // 각 턴에 치즈와 맞닿는 면 계산
            boolean[][] visited = new boolean[n][m];
            // 모눈종이의 맨 가장자리에는 치즈가 놓이지 않는다.
            q.offer(new int[] {0,0});

            while(!q.isEmpty()) {
                int[] cur = q.poll();
                int curX = cur[0];
                int curY = cur[1];

                for(int dir=0; dir<4; dir++) {
                    int nx = curX + dx[dir];
                    int ny = curY + dy[dir];
                    if(nx < 0 || ny < 0 || nx >= n || ny >= m) continue;
                    if(visited[nx][ny]) continue;
                    // 치즈라면 더 이상 탐색 X
                    if(map[nx][ny] == 1) cheeseStatus[nx][ny]++;
                    else if(map[nx][ny] == 0) {
                        visited[nx][ny] = true;
                        q.offer(new int[] {nx, ny});
                    }
                }
            }

            for(int x=0; x<n; x++) {
                for(int y=0; y<m; y++) {
                    if(cheeseStatus[x][y] > 1) {
                        map[x][y] = 0;
                        cheese--;
                    }
                }
            }
        }
        return time;
    }
}
