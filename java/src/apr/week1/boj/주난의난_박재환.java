package apr.week1.boj;

import java.util.*;
import java.io.*;

public class 주난의난_박재환 {
    static BufferedReader br;
    public static void main(String[] args) throws IOException {
        br = new BufferedReader(new InputStreamReader(System.in));
        init();
        br.close();
    }
    static class Point {
        int x, y;

        Point(int x, int y) {
            this.x = x;
            this.y = y;
        }
    }
    static StringTokenizer st;
    static int n, m;
    static Point junan;
    static Point criminal;
    static char[][] board;
    static void init() throws IOException {
        st = new StringTokenizer(br.readLine().trim());
        n = Integer.parseInt(st.nextToken());
        m = Integer.parseInt(st.nextToken());

        st = new StringTokenizer(br.readLine().trim());
        int jx = Integer.parseInt(st.nextToken()) - 1;
        int jy = Integer.parseInt(st.nextToken()) - 1;
        junan = new Point(jx, jy);
        int cx = Integer.parseInt(st.nextToken()) -1;
        int cy = Integer.parseInt(st.nextToken()) -1;
        criminal = new Point(cx, cy);

        board = new char[n][m];
        for(int x = 0; x < n; x++) {
            String line = br.readLine().trim();
            for(int y = 0; y < m; y++) board[x][y] = line.charAt(y);
        }

        System.out.println(solution());
    }
    static final int[] dx = {0, 1, 0, -1};
    static final int[] dy = {1, 0, -1, 0};
    static int solution() {
        Deque<int[]> q = new ArrayDeque<>();
        int[][] visited = new int[n][m];
        for(int x = 0; x < n; x++) Arrays.fill(visited[x], 987654321);

        q.offerLast(new int[] {junan.x, junan.y});
        visited[junan.x][junan.y] = 0;

        while(!q.isEmpty()) {
            int[] cur = q.pollFirst();

            for(int dir = 0; dir < 4; dir++) {
                int nx = cur[0] + dx[dir];
                int ny = cur[1] + dy[dir];

                if(nx < 0 || ny < 0 || nx >= n || ny >= m) continue;

                int w = board[nx][ny] == '0' ? 0 : 1;

                if(visited[nx][ny] > visited[cur[0]][cur[1]] + w) {
                    visited[nx][ny] = visited[cur[0]][cur[1]] + w;
                    if(w == 0) q.offerFirst(new int[] {nx, ny});
                    else q.offerLast(new int[] {nx, ny});
                }
            }
        }
        return visited[criminal.x][criminal.y];
    }
}
