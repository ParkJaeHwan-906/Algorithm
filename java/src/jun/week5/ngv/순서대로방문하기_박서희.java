package jun.week5.ngv;

import java.io.*;
import java.util.*;

/*
  문제풀이 시간: 00:25:14
  AI 사용 여부: X
 */
public class 순서대로방문하기_박서희 {

    static int n, m;
    static int[][] grid;
    static int[][] points;
    static boolean[][] visited;

    static int answer = 0;
    static final int[] dx = {-1, 0, 1, 0};
    static final int[] dy = {0, 1, 0, -1};

    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer st = new StringTokenizer(br.readLine());

        n = Integer.parseInt(st.nextToken());
        m = Integer.parseInt(st.nextToken());

        grid = new int[n][n];
        visited = new boolean[n][n];
        points = new int[m][2];

        for (int i = 0; i < n; i++) {
            st = new StringTokenizer(br.readLine());
            for (int j = 0; j < n; j++) {
                grid[i][j] = Integer.parseInt(st.nextToken());
            }
        }

        for (int i = 0; i < m; i++) {
            st = new StringTokenizer(br.readLine());
            int x = Integer.parseInt(st.nextToken()) - 1;
            int y = Integer.parseInt(st.nextToken()) - 1;
            points[i][0] = x;
            points[i][1] = y;
        }

        dfs(points[0][0], points[0][1], 1);
        System.out.println(answer);
    }

    static void dfs(int sx, int sy, int endIdx) {
        visited[sx][sy] = true;

        int ex = points[endIdx][0], ey = points[endIdx][1];
        if (sx == ex && sy == ey) {
            if (endIdx == m - 1) {
                answer++;
            } else {
                dfs(ex, ey, endIdx + 1);
            }
            visited[sx][sy] = false;
            return;
        }

        for (int i = 0; i < 4; i++) {
            int nx = sx + dx[i], ny = sy + dy[i];

            if (!inRange(nx, ny)) continue;
            if (visited[nx][ny]) continue;
            if (grid[nx][ny] == 1) continue;

            dfs(nx, ny, endIdx);
        }

        visited[sx][sy] = false;
    }

    static boolean inRange(int x, int y) {
        return 0 <= x && x < n && 0 <= y && y < n;
    }
}
