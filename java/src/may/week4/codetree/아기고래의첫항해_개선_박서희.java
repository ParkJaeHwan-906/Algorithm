package may.week4.codetree;

import java.io.*;
import java.util.*;

/*
    개선 사항: 1) 중복으로 사용되는 코드를 함수로 분리 moveWhalePos(), markVisitedSea()
             2) moveWhale 내에 있던 불필요한 bfs 코드 제거
             3) moveFar() 내에 if 조건문 개선
 */
public class 아기고래의첫항해_개선_박서희 {

    static int N;
    static int seaCount = 0, visitedSea = 0;
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
        int[] dMap = {3, 1, 0, 2};
        D = dMap[D];

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
        // 첫 출발 위치 visited로
        markVisitedSea();

        while (visitedSea < seaCount) {
            boolean canMoveNearby = false;

            // 1단계 (기존 방향, 좌회전, 우회전, 180도 회전)
            for (int d : new int[]{0, 1, 3, 2}) {
                int nX = whale.x + dx[(whale.d + d) % 4];
                int nY = whale.y + dy[(whale.d + d) % 4];

                if (!inRange(nX, nY) || grid[nX][nY] > 0) continue;
                canMoveNearby = true;
                moveWhalePos(nX, nY, (whale.d + d) % 4);
                markVisitedSea();
                break;
            }
            if (canMoveNearby) continue;

            // 2단계 - 상하좌우가 아닌 먼 바다 중 제일 가까운 곳으로 이동
            moveFar();
        }
    }

    // 상하좌우로 갈 수 없을 때 최단거리의 바다로 이동 -> 거리를 int[][] dis에 저장
    private static void moveFar() {
        int nextX = -1, nextY = -1, nextD = -1;
        int minDis = Integer.MAX_VALUE;

        int[][] dis = new int[N][N];
        for (int[] dd : dis) Arrays.fill(dd, -1);

        Queue<int[]> queue = new LinkedList<>();
        queue.offer(new int[]{whale.x, whale.y, whale.d});
        dis[whale.x][whale.y] = 1;

        while (!queue.isEmpty()) {
            int[] cur = queue.poll();
            int x = cur[0], y = cur[1], d = cur[2];

            if (grid[x][y] == 0) {
                if (dis[x][y] < minDis || (dis[x][y] == minDis && (x < nextX || (x == nextX && y < nextY)))) {
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

                queue.offer(new int[]{nx, ny, i});
                dis[nx][ny] = dis[x][y] + 1;
            }
        }

        if (minDis != Integer.MAX_VALUE) {
            moveWhalePos(nextX, nextY, nextD);
            markVisitedSea();
        }
    }

    private static void moveWhalePos(int x, int y, int d) {
        whale.x = x;
        whale.y = y;
        whale.d = d;
    }

    private static void markVisitedSea() {
        grid[whale.x][whale.y] = 2; // 방문한 바다는 2로 표시
        ans.append(whale.x + 1 + " " + (whale.y + 1) + "\n");
        visitedSea++;
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
