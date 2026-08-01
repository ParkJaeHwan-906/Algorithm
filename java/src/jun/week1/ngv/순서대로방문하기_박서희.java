package jun.week1.ngv;

import java.io.*;
import java.util.*;

/*
  문제풀이 시간: 0:29:40
  AI 사용 여부: X
 */
public class 순서대로방문하기_박서희 {

    static int n;
    static int[][] grid;
    static ArrayList<int[]> points = new ArrayList<>();
    static int answer = 0;

    static int[] dx = {-1, 0, 1, 0};
    static int[] dy = {0, -1, 0, 1};

    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer st = new StringTokenizer(br.readLine());

        n = Integer.parseInt(st.nextToken());
        int m = Integer.parseInt(st.nextToken());

        grid = new int[n][n];

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
            points.add(new int[] {x, y});
        }

        int[][] visited = new int[n][n];
        dfs(points.get(0)[0], points.get(0)[1], 1, visited);
        System.out.println(answer);

    }

    static void dfs(int startX, int startY, int endIdx, int[][] visited) {
        visited[startX][startY] = 1;

        int endX = points.get(endIdx)[0], endY = points.get(endIdx)[1];

        if (startX == endX && startY == endY) {
            if (points.size() - 1 == endIdx) {
                answer++;
                visited[startX][startY] = 0;
                return;
            }
            // 다음 점으로 가기
            dfs(endX, endY, endIdx + 1, visited);
            visited[startX][startY] = 0;
            return;
        }


        for (int i = 0; i < 4; i++) {
            int nx = startX + dx[i], ny = startY + dy[i];
            if (!inRange(nx, ny)) continue;
            if (visited[nx][ny] == 1) continue;
            if (grid[nx][ny] == 1) continue;
            dfs(nx, ny, endIdx, visited);
        }

        visited[startX][startY] = 0;

    }

    static boolean inRange(int x, int y) {
        return 0 <= x && x < n && 0 <= y && y < n;
    }
}
