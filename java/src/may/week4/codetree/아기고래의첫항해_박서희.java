package may.week4.codetree;

import java.io.*;
import java.util.*;

/*
  문제풀이 시간: 4시간+
  AI 사용 여부: X
  생각의 흐름: 쉬운 문제라 생각했는데 방향 추적 방법을 잘못 생각해서 헤맸다. bfs를 할 때 거리를 dis[][] 배열에 저장했었다.
            그래서 목적지까지의 거리가 6이면, 목적지의 상하좌우 중 거리가 5인 칸에서 방향 우선순위를 적용하면 될거라 생각했는데 오산이었다ㅜ
            어느 방향으로 우선순위를 지키면서 왔는지 dis[][] 만으로 알 수가 없다. bfs 돌면서 배열에 방향을 가지고 다녔어야 했다.
            해설을 보니까 해설은 dis[][] 배열을 2개 썼던데 그 방법도 한번 시도해봐야겠다.
 */
public class 아기고래의첫항해_박서희 {

    static int N;
    static int seaCount = 0;
    static int[][] grid;
    static Whale whale;
    static StringBuilder ans = new StringBuilder();

    // 좌 하 우 상
    static int[] dx = {0, 1, 0, -1};
    static int[] dy = {-1, 0, 1, 0};

    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

        StringTokenizer st = new StringTokenizer(br.readLine());
        N = Integer.parseInt(st.nextToken());
        int R = Integer.parseInt(st.nextToken()) - 1;
        int C = Integer.parseInt(st.nextToken()) - 1;
        // 상 하 좌 우 dx, dy 기준으로 {3, 1, 0, 2}
        int D = Integer.parseInt(st.nextToken()) - 1;
        if (D == 0) D = 3;
        else if (D == 2) D = 0;
        else if (D == 3) D = 2;

        whale = new Whale(R, C, D);

        grid = new int[N][N];
        for (int i = 0; i < N; i++) {
            st = new StringTokenizer(br.readLine());
            for (int j = 0; j < N; j++) {
                grid[i][j] = Integer.parseInt(st.nextToken());
                if (grid[i][j] == 0) seaCount++;
            }
        }
        moveWhale();
        System.out.print(ans.toString());
    }

    private static void moveWhale() {
        int visitedSea = 0;

        Queue<int[]> q = new LinkedList<>();
        q.add(new int[]{whale.x, whale.y, whale.d});

        while (!q.isEmpty()) {
            int[] cur = q.poll();
            whale.x = cur[0];
            whale.y = cur[1];
            whale.d = cur[2];

            if (grid[whale.x][whale.y] == 0) {
                visitedSea++;
                ans.append((whale.x + 1) + " " + (whale.y + 1) + "\n");
                grid[whale.x][whale.y] = 2; // 방문한 바다는 2로
                if (visitedSea == seaCount)
                    break;
            }

            boolean canMoveNearby = false;

            // 현재 방향 좌회전 우회전 정반대
            for (int d : new int[]{0, 1, 3, 2}) {
                int nX = whale.x + dx[(whale.d + d) % 4];
                int nY = whale.y + dy[(whale.d + d) % 4];

                if (!inRange(nX, nY) || grid[nX][nY] > 0) continue;
                canMoveNearby = true;
                q.add(new int[]{nX, nY, (whale.d + d) % 4});
                break;
            }

            if (canMoveNearby) continue;
            int[] moveFar = moveFar();
            q.add(moveFar);
        }

    }

    // 상하좌우로 갈 수 없을 때 최단거리의 바다로 이동
    private static int[] moveFar() {
        int nextX = -1, nextY = -1, nextD = -1;
        int minDis = Integer.MAX_VALUE;

        int[][] dis = new int[N][N];
        for (int[] dd: dis) Arrays.fill(dd, -1);

        Queue<int[]> queue = new LinkedList<>();
        queue.offer(new int[] {whale.x, whale.y, whale.d});
        dis[whale.x][whale.y] = 1;

        while(!queue.isEmpty()) {
            int[] cur = queue.poll();
            int x = cur[0], y = cur[1], d = cur[2];

            if (grid[x][y] == 0 && dis[x][y] < minDis) {
                minDis = dis[x][y];
                nextX = x;
                nextY = y;
                nextD = d;
            } else if (grid[x][y] == 0 && dis[x][y] == minDis) {
                if (x < nextX || (x == nextX && y < nextY)) {
                    minDis = dis[x][y];
                    nextX = x;
                    nextY = y;
                    nextD = d;
                }
            }

            for (int i = 0; i < 4; i++) {
                int nx = x + dx[i], ny = y + dy[i];

                if (!inRange(nx, ny)) continue;
                if (dis[nx][ny] != -1) continue;
                if (grid[nx][ny] == 1) continue;

                queue.offer(new int[] {nx, ny, i});
                dis[nx][ny] = dis[x][y] + 1;
            }
        }

        return new int[]{nextX, nextY, nextD};
    }


    private static boolean inRange(int x, int y) {
        return 0 <= x && x < N && 0 <= y && y < N;
    }

    static class Whale {
        int x, y, d;

        public Whale(int x, int y, int d) {
            this.x = x;
            this.y = y;
            this.d = d;
        }
    }
}
