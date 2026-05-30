package may.week3.codetree;

import java.io.*;
import java.util.*;

public class 전투로봇_박서희 {

    static int n;
    static int[][] grid;
    static int[] dx = {-1, 0, 1, 0};
    static int[] dy = {0, 1, 0, -1};

    // x, y, Lv
    static int[] robot = new int[3];
    static int monsterCnt = 0;
    static ArrayList<int[]> monsters = new ArrayList<>();

    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        int ans = 0;

        n = Integer.parseInt(br.readLine());
        grid = new int[n][n];

        StringTokenizer st;
        for (int i = 0; i < n; i++) {
            st = new StringTokenizer(br.readLine());
            for (int j = 0; j < n; j++) {
                grid[i][j] = Integer.parseInt(st.nextToken());
                if (grid[i][j] == 9) {
                    robot[0] = i;
                    robot[1] = j;
                    robot[2] = 2;
                    grid[i][j] = 0;
                } else if (grid[i][j] != 0) {
                    monsters.add(new int[] {i, j, grid[i][j], 0});
                }
            }
        }

        while(true){
            int[] targetMonster = removeMonster();
            if (targetMonster[0] == Integer.MAX_VALUE)
                break;

            monsterCnt++;
            monsters.get(targetMonster[1])[3] = 1;
            grid[robot[0]][robot[1]] = 0;
            robot[0] = monsters.get(targetMonster[1])[0];
            robot[1] = monsters.get(targetMonster[1])[1];
            upgradeRobotLv();

            ans+=targetMonster[0];
        }

        System.out.println(ans);

    }
    // 없앨 몬스터 찾기 return: 이동거리, 몬스터 idx
    static int[] removeMonster() {
        int[] dis = new int[2];
        dis[0] = Integer.MAX_VALUE;
        dis[1] = Integer.MAX_VALUE;

        for (int i = 0; i < monsters.size(); i++) {
            if (monsters.get(i)[3] == 1 || monsters.get(i)[2] >= robot[2])
                continue;

            int monsterDis = bfs(monsters.get(i)[0], monsters.get(i)[1]);
            if (monsterDis < dis[0]) {
                dis[0] = monsterDis;
                dis[1] = i;
            }
        }

        return dis;
    }

    static int bfs(int monsterX, int monsterY) {
        int[][] visited = new int[n][n];
        Queue<int[]> queue = new LinkedList<>();

        queue.add(new int[] {robot[0], robot[1], 0});
        visited[robot[0]][robot[1]] = 1;

        while(!queue.isEmpty()) {
            int[] cur = queue.poll();
            int curX = cur[0];
            int curY = cur[1];
            int dis = cur[2];

            if (curX == monsterX && curY == monsterY)
                return dis;

            for (int i = 0; i < 4; i++) {
                int nx = curX + dx[i];
                int ny = curY + dy[i];

                if (!inRange(nx, ny))
                    continue;
                if (visited[nx][ny] == 1)
                    continue;
                if (grid[nx][ny] > robot[2])
                    continue;

                queue.add(new int[] {nx, ny, dis+1});
                visited[nx][ny] = 1;
            }
        }
        return Integer.MAX_VALUE;
    }

    static void upgradeRobotLv() {
        if (monsterCnt == robot[2]) {
            robot[2]++;
            monsterCnt = 0;
        }
    }

    static boolean inRange(int x, int y) {
        return 0 <= x && x < n && 0 <= y && y < n;
    }
}
