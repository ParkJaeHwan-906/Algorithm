package may.week4.codetree;

import java.io.*;
import java.util.*;

/*
  문제풀이 시간: 1:26:04
  AI 사용 여부: X
  생각의 흐름: 제출했다가 메모리 초과를 맛봤다. 어떤 승객을 태울지 매번 bfs를 하는 것이 아니라 택시 좌표 기준으로 모든 승객까지 한번만 조사하는걸로 수정했다.
            그리고 승객이나 목적지에 벽 때문에 아예 못 가는 경우도 생각했어야 헀다.
 */
public class 자율주행전기차_박서희 {

    static int n, m, c;
    static int[][] grid;
    static ArrayList<int[]> people = new ArrayList<>();
    static int taxiX, taxiY;

    static int[] dx = {-1, 0, 1, 0};
    static int[] dy = {0, 1, 0, -1};

    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

        StringTokenizer st = new StringTokenizer(br.readLine());
        n = Integer.parseInt(st.nextToken());
        m = Integer.parseInt(st.nextToken());
        c = Integer.parseInt(st.nextToken());

        grid = new int[n][n];
        for (int i = 0; i < n; i++) {
            st = new StringTokenizer(br.readLine());
            for (int j = 0; j < n; j++) {
                grid[i][j] = Integer.parseInt(st.nextToken());
            }
        }

        st = new StringTokenizer(br.readLine());
        taxiX = Integer.parseInt(st.nextToken()) - 1;
        taxiY = Integer.parseInt(st.nextToken()) - 1;

        for (int i = 0; i < m; i++) {
            st = new StringTokenizer(br.readLine());
            int startX = Integer.parseInt(st.nextToken()) - 1;
            int startY = Integer.parseInt(st.nextToken()) - 1;
            int endX = Integer.parseInt(st.nextToken()) - 1;
            int endY = Integer.parseInt(st.nextToken()) - 1;
            people.add(new int[]{startX, startY, endX, endY, 0});
        }

        people.sort((a, b) -> a[0] != b[0] ? a[0] - b[0] : a[1] - b[1]);

        int answer = simulate();
        System.out.println(answer);
    }

    static int simulate() {
        for (int i = 0; i < m; i++) {
            int[] target = findTargetIdx();
            if (target[0] == -1) return -1;

            // 승객까지 이동
            int idx = target[0];
            people.get(idx)[4] = 1;
            c -= target[1];
            if (c < 0) return -1;

            // 승객의 목적지까지 이동
            int targetMoveDis = calDis(people.get(idx)[0], people.get(idx)[1], people.get(idx)[2], people.get(idx)[3]);
            if (targetMoveDis == -1) return -1;
            c -= targetMoveDis;
            if (c < 0) return -1;

            // 충전
            taxiX = people.get(idx)[2];
            taxiY = people.get(idx)[3];
            c += targetMoveDis * 2;
        }
        return c;
    }

    // 승객 idx와 승객까지의 거리 반환
    static int[] findTargetIdx() {
        int[][] dis = new int[n][n];
        for (int[] d : dis) Arrays.fill(d, -1);

        Queue<int[]> q = new LinkedList<>();
        q.add(new int[]{taxiX, taxiY});
        dis[taxiX][taxiY] = 0;

        while (!q.isEmpty()) {
            int[] cur = q.poll();
            int curX = cur[0];
            int curY = cur[1];

            for (int d = 0; d < 4; d++) {
                int nX = curX + dx[d];
                int nY = curY + dy[d];

                if (!inRange(nX, nY) || grid[nX][nY] == 1 || dis[nX][nY] != -1) continue;

                q.add(new int[]{nX, nY});
                dis[nX][nY] = dis[curX][curY] + 1;
            }
        }

        int minDis = Integer.MAX_VALUE;
        int idx = -1;

        for (int i = 0; i < people.size(); i++) {
            int[] p = people.get(i);
            if (p[4] == 1) continue;

            int curDis = dis[p[0]][p[1]];
            if (curDis == -1) continue;

            if (curDis < minDis) {
                minDis = curDis;
                idx = i;
            }
        }

        return new int[]{idx, minDis};
    }

    static int calDis(int startX, int startY, int endX, int endY) {
        Queue<int[]> q = new LinkedList<>();
        int[][] visited = new int[n][n];

        q.add(new int[]{startX, startY, 0});
        visited[startX][startY] = 1;

        while (!q.isEmpty()) {
            int[] cur = q.poll();
            int curX = cur[0];
            int curY = cur[1];
            int curDis = cur[2];

            if (curX == endX && curY == endY)
                return curDis;

            for (int d = 0; d < 4; d++) {
                int nX = curX + dx[d];
                int nY = curY + dy[d];

                if (!inRange(nX, nY) || visited[nX][nY] == 1 || grid[nX][nY] == 1) continue;

                q.add(new int[]{nX, nY, curDis + 1});
                visited[nX][nY] = 1;
            }
        }
        return -1;
    }

    static boolean inRange(int x, int y) {
        return 0 <= x && x < n && 0 <= y && y < n;
    }
}
