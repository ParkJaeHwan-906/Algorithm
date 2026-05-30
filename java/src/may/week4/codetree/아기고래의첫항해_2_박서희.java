package may.week4.codetree;

import java.io.*;
import java.util.*;

/*
    2단계의 바다 이동 시 bfs를 두 번 사용하여 방향 구하기(해설의 풀이에서 int[][] disFrom, disTo 총 2개가 필요한 부분이 이해가 어려웠음.)
    왜 고래 위치로부터 모든 거리를 구한 int[][] disFrom으로는 방향을 구할 수 없고 disTo로 구할 수 있을까?
    -> disFrom은 현재 고래 위치에서 어떤 바다가 제일 가까운지 찾기 위한 용도, 목적지 방향이든 엉뚱한 방향이든 고래에서 멀어지기만 하면 숫자 커짐.
    -> disTo는 목적지를 향해 제대로 전진하는 최단 경로 딱 하나만 찾을 수 있음. 목적지와 멀어지는 샛길들은 숫자가 커짐.(반면에 목적지를 향해 가면 작아짐.)
    제출 결과: bfs 두 번 돌리는거라 수행 시간은 더 길어짐.
 */
public class 아기고래의첫항해_2_박서희 {

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
        int targetX = -1, targetY = -1, nextD = -1;
        int minDis = Integer.MAX_VALUE;

        int[][] disFrom = bfs(whale.x, whale.y);
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                if (grid[i][j] == 0 && disFrom[i][j] != -1 && disFrom[i][j] < minDis) {
                    minDis = disFrom[i][j];
                    targetX = i;
                    targetY = j;
                }
            }
        }

        int[][] disTo = bfs(targetX, targetY);
        while (whale.x != targetX || whale.y != targetY) {
            for (int dir = 0; dir < 4; dir++) {
                int nx = whale.x + dx[dir], ny = whale.y + dy[dir];
                // grid[nx][ny] == 0, 2 일 때 지나갈 수 있음. 0은 아직 방문 안 한 바다, 2는 방문한 바다
                if (inRange(nx, ny) && grid[nx][ny] != 1 && disTo[nx][ny] == disTo[whale.x][whale.y] - 1) {
                    moveWhalePos(nx, ny, dir);
                    break;
                }
            }
        }

        markVisitedSea();
    }

    private static int[][] bfs(int startX, int startY) {
        int[][] dis = new int[N][N];
        for (int[] dd : dis) Arrays.fill(dd, -1);

        Queue<int[]> queue = new LinkedList<>();
        queue.offer(new int[]{startX, startY});
        dis[startX][startY] = 0;

        while (!queue.isEmpty()) {
            int[] cur = queue.poll();
            int x = cur[0], y = cur[1];

            for (int i = 0; i < 4; i++) {
                int nx = x + dx[i], ny = y + dy[i];

                if (!inRange(nx, ny)) continue;
                if (dis[nx][ny] != -1) continue;
                if (grid[nx][ny] == 1) continue;

                queue.offer(new int[]{nx, ny});
                dis[nx][ny] = dis[x][y] + 1;
            }
        }

        return dis;
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
