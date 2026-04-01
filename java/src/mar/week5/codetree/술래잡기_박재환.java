package mar.week5.codetree;

import java.util.*;
import java.io.*;

public class 술래잡기_박재환 {
    static BufferedReader br;
    static StringBuilder sb;
    public static void main(String[] args) throws IOException {
        br = new BufferedReader(new InputStreamReader(System.in));
        sb = new StringBuilder();
        init();
        br.close();
        System.out.println(sb);
    }

    static final int[] dx = {0, 1, 0, -1};
    static final int[] dy = {1, 0, -1, 0};

    static class Runner {
        int x, y;
        int dir;
        boolean live;

        Runner(int x, int y, int dir) {
            this.x = x;
            this.y = y;
            this.dir = dir;
            this.live = true;
        }
    }

    static StringTokenizer st;
    static int n, m, h, k;
    static Runner[] runners;
    static int[][] treeBoard;

    static void init() throws IOException {
        st = new StringTokenizer(br.readLine().trim());
        n = Integer.parseInt(st.nextToken());       // 격자 크기
        m = Integer.parseInt(st.nextToken());       // 도망자 수
        h = Integer.parseInt(st.nextToken());       // 나무 수
        k = Integer.parseInt(st.nextToken());       // 턴 수

        runners = new Runner[m];
        for (int i = 0; i < m; i++) {
            st = new StringTokenizer(br.readLine().trim());
            int x = Integer.parseInt(st.nextToken()) - 1;
            int y = Integer.parseInt(st.nextToken()) - 1;
            int dir = Integer.parseInt(st.nextToken());
            dir = (dir == 1) ? 0 : 1;
            Runner runner = new Runner(x, y, dir);
            runners[i] = runner;
        }

        treeBoard = new int[n][n];
        for (int i = 0; i < h; i++) {
            st = new StringTokenizer(br.readLine().trim());
            int x = Integer.parseInt(st.nextToken()) - 1;
            int y = Integer.parseInt(st.nextToken()) - 1;
            treeBoard[x][y] = 1;
        }
        solution();
    }
    static boolean isGo;
    static int cId;
    static void solution() {
        int total = 0;

        chaser();
        isGo = true;
        cId = 0;

        // 술래의 초기 위치
        int cx = n / 2, cy = n / 2;

        for(int i = 1; i < k + 1; i++) {
            // 1. 도망자 이동
            moveRunner(cx, cy);
            // 2. 술래 이동
            int[] cur = moveChaser();
            int[] next = peekNextChaser();
            int[] dir = {next[0] - cur[0], next[1] - cur[1]};

            // 3. 시야 획인
            int caught = checkAround(cur, dir);
            total += i * caught;
            cx = cur[0];
            cy = cur[1];
        }

        sb.append(total);
    }
    /**
     * [술래 경로 생성]
     * - 항상 정가운데 위치
     */
    static List<int[]> go, come;        // go : 중앙 -> 바깥, come : 바깥 -> 중앙
    static void chaser() {
        go = new ArrayList<>();
        come = new ArrayList<>();
        traceCome();
        Collections.reverse(go);

        go.remove(0);
        come.remove(0);
    }

    static int[][] deltaDir = {{1, 0}, {0, 1}, {-1, 0}, {0, -1}};
    static void traceCome() {
        // V > ^ <
        Queue<int[]> q = new ArrayDeque<>();
        boolean[][] visited = new boolean[n][n];

        q.offer(new int[]{0, 0, 0});
        visited[0][0] = true;

        while (!q.isEmpty()) {
            int[] cur = q.poll();
            int x = cur[0];
            int y = cur[1];
            int dir = cur[2];
            go.add(new int[] {x, y});
            come.add(new int[] {x, y});
            int nx = x + deltaDir[dir][0];
            int ny = y + deltaDir[dir][1];
            if(isNotBoard(nx, ny) || visited[nx][ny]) {     // 방향 전환
                dir = (dir + 1) % 4;
                nx = x + deltaDir[dir][0];
                ny = y + deltaDir[dir][1];
            }
            if(isNotBoard(nx, ny) || visited[nx][ny]) continue;

            visited[nx][ny] = true;
            q.offer(new int[] {nx, ny, dir});
        }
    }

    /**
     * 도망자
     */
    static void moveRunner(int cx, int cy) {
        for(Runner r : runners) {
            if(!r.live) continue;

            int dist = Math.abs(r.x - cx) + Math.abs(r.y - cy);
            if(dist > 3) continue;

            int nx = r.x + dx[r.dir];
            int ny = r.y + dy[r.dir];
            if(isNotBoard(nx, ny)) {
                r.dir = (r.dir + 2) % 4;
                nx = r.x + dx[r.dir];
                ny = r.y + dy[r.dir];
            }
            if(nx == cx && ny == cy) continue;
            r.x = nx;
            r.y = ny;
        }
    }

    static int[] moveChaser() {
        if(isGo && go.size() == cId) {
            isGo = false;
            cId = 0;
        }
        else if(!isGo && come.size() == cId) {
            isGo = true;
            cId = 0;
        }
        return isGo ? go.get(cId++) : come.get(cId++);
    }
    static List<Integer>[][] runnerMap;
    static int checkAround(int[] chaser, int[] nDir) {
        makeRunnerMap();
        int cx = chaser[0];
        int cy = chaser[1];

        int catchCount = 0;
        for(int i = 0; i < 3; i++) {
            int nx = cx + nDir[0] * i;
            int ny = cy + nDir[1] * i;
            if(isNotBoard(nx, ny)) break;
            if(treeBoard[nx][ny] == 1) continue;
            if(runnerMap[nx][ny].isEmpty()) continue;

            for(int id : runnerMap[nx][ny]) {
                runners[id].live = false;
                catchCount++;
            }
        }

        return catchCount;
    }
    static void makeRunnerMap() {
        runnerMap = new ArrayList[n][n];
        for(int i = 0; i < n; i++) {
            for(int j = 0; j < n; j++) {
                runnerMap[i][j] = new ArrayList<>();
            }
        }

        for(int i = 0; i < m; i++) {
            Runner r = runners[i];
            if(!r.live) continue;
            runnerMap[r.x][r.y].add(i);
        }
    }

    static int[] peekNextChaser() {
        if (isGo) {
            if (cId == go.size()) {
                return come.get(0);
            }
            return go.get(cId);
        } else {
            if (cId == come.size()) {
                return go.get(0);
            }
            return come.get(cId);
        }
    }
    // ====================================================
    static boolean isNotBoard(int x, int y) {
        return x < 0 || y < 0 || x >= n || y >= n;
    }
}
