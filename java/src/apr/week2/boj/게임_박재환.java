package apr.week2.boj;

import java.util.*;
import java.io.*;

public class 게임_박재환 {
    static BufferedReader br;
    public static void main(String[] args) throws IOException {
        br = new BufferedReader(new InputStreamReader(System.in));
        init();
        br.close();
    }
    static StringTokenizer st;
    static int n, m;
    static int[][] dangerBoard;
    static int[][] deathBoard;
    static void init() throws IOException {
        n = Integer.parseInt(br.readLine().trim());     // 위험한 구역 수
        dangerBoard = new int[501][501];
        for(int i = 0; i < n; i++) {
            st = new StringTokenizer(br.readLine().trim());
            int x1 = Integer.parseInt(st.nextToken());
            int y1 = Integer.parseInt(st.nextToken());
            int x2 = Integer.parseInt(st.nextToken());
            int y2 = Integer.parseInt(st.nextToken());
            fillBoard(x1, y1, x2, y2, dangerBoard);
        }
        m = Integer.parseInt(br.readLine().trim());     // 죽음의 구역 수
        deathBoard = new int[501][501];
        for(int i = 0; i < m; i++) {
            st = new StringTokenizer(br.readLine().trim());
            int x1 = Integer.parseInt(st.nextToken());
            int y1 = Integer.parseInt(st.nextToken());
            int x2 = Integer.parseInt(st.nextToken());
            int y2 = Integer.parseInt(st.nextToken());
            fillBoard(x1, y1, x2, y2, deathBoard);
        }

        System.out.println(solution());
    }

    static void fillBoard(int x1, int y1, int x2, int y2, int[][] board) {
        int minX = Math.min(x1, x2);
        int minY = Math.min(y1, y2);
        int maxX = Math.max(x1, x2);
        int maxY = Math.max(y1, y2);

        for(int x = minX; x <= maxX; x++) {
            for(int y = minY; y <= maxY; y++) board[x][y] = 1;
        }
    }

    static int solution() {
        int[] dx = {0, 1, 0, -1};
        int[] dy = {1, 0, -1, 0};

        Deque<int[]> q = new ArrayDeque<>();
        int[][] dist = new int[501][501];
        for(int x = 0; x <= 500; x++) Arrays.fill(dist[x], 987654321);
        q.offer(new int[] {0, 0, 0});
        dist[0][0] = 0;

        while(!q.isEmpty()) {
            int[] cur = q.poll();
            int x = cur[0];
            int y = cur[1];
            int hp = cur[2];

            if(x == 500 && y == 500) return hp;

            for(int dir = 0; dir < 4; dir++) {
                int nx = x + dx[dir];
                int ny = y + dy[dir];

                if(nx < 0 || ny < 0 || nx > 500 || ny > 500) continue;
                if(deathBoard[nx][ny] == 1) continue;

                int nextHp = hp + dangerBoard[nx][ny];

                if(dist[nx][ny] < nextHp) continue;
                dist[nx][ny] = nextHp;
                if(dangerBoard[nx][ny] == 1) q.offerLast(new int[] {nx, ny, nextHp});
                else q.offerFirst(new int[] {nx, ny, nextHp});
            }
        }

        return -1;
    }
}
