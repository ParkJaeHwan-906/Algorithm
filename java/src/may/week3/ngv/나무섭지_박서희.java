package may.week3.ngv;

import java.io.*;
import java.util.*;

public class 나무섭지_박서희 {

    static List<int[]> ghosts = new ArrayList<>();
    static int n, m;
    static int startX, startY, exitX, exitY;
    static int[][] grid;

    static int[] dx = {-1, 0, 1, 0};
    static int[] dy = {0, 1, 0, -1};

    // 유령보다 남우의 출구까지의 거리가 짧으면 됨.
    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer st = new StringTokenizer(br.readLine());

        n = Integer.parseInt(st.nextToken());
        m = Integer.parseInt(st.nextToken());

        grid = new int[n][m];

        for (int i = 0; i < n; i++) {
            String temp = br.readLine();
            for (int j = 0; j < m; j++) {
                char c = temp.charAt(j);
                if (c == 'N') {
                    startX = i;
                    startY = j;
                } else if (c == 'G') {
                    ghosts.add(new int[]{i, j});
                } else if (c == 'D') {
                    exitX = i;
                    exitY = j;
                } else if (c == '#') {
                    grid[i][j] = 1;
                }
            }
        }

        int namwooDis = bfs();

        int minGhostDis = calculateGhostDis();

        System.out.println(namwooDis < minGhostDis ? "Yes" : "No");

    }

    static int bfs() {
        Queue<int[]> queue = new LinkedList<>();
        int[][] visited = new int[n][m];

        queue.add(new int[]{startX, startY, 0});
        visited[startX][startY] = 1;

        while (!queue.isEmpty()) {

            int[] cur = queue.poll();
            int curX = cur[0];
            int curY = cur[1];
            int d = cur[2];

            if (curX == exitX && curY == exitY)
                return d;

            for (int i = 0; i < 4; i++) {
                int nx = curX + dx[i];
                int ny = curY + dy[i];

                if (!inRange(nx, ny))
                    continue;
                if (visited[nx][ny] == 1)
                    continue;
                if (grid[nx][ny] == 1)
                    continue;

                queue.add(new int[]{nx, ny, d + 1});
                visited[nx][ny] = 1;
            }
        }
        return Integer.MAX_VALUE;
    }

    static int calculateGhostDis() {
        int minDis = Integer.MAX_VALUE;
        for (int[] ghost : ghosts) {
            minDis = Math.min(minDis, Math.abs(ghost[0] - exitX) + Math.abs(ghost[1] - exitY));
        }
        return minDis;
    }

    static boolean inRange(int x, int y) {
        return 0 <= x && x < n && 0 <= y && y < m;
    }
}
