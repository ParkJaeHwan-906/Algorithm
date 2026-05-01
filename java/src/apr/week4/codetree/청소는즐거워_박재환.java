package apr.week4.codetree;

import java.util.*;
import java.io.*;

public class 청소는즐거워_박재환 {
    static BufferedReader br;
    public static void main(String[] args) throws IOException {
        br = new BufferedReader(new InputStreamReader(System.in));
        init();
        br.close();
    }

    static int parseInt(String s) {
        return Integer.parseInt(s);
    }

    static StringTokenizer st;
    static int n;
    static int[][] board;
    static void init() throws IOException {
        n = parseInt(br.readLine().trim());

        board = new int[n][n];
        for(int i = 0; i < n; i++) {
            st = new StringTokenizer(br.readLine());
            for(int j = 0; j < n ; j++) {
                board[i][j] = parseInt(st.nextToken());
            }
        }

        int result = solution();
        System.out.println(result);
    }
    static int totalDust;
    static List<int[]> moveSeq;
    static int solution() {
        getMoveSeq();
        totalDust = 0;
        for(int i = 0; i < moveSeq.size() - 1; i++) {
            int[] cur = moveSeq.get(i);
            int[] next = moveSeq.get(i + 1);

            int dx = next[0] - cur[0];
            int dy = next[1] - cur[1];

            int dir = getDir(dx, dy);
            spreadDust(next[0], next[1], dir);
        }
        return totalDust;
    }

    static void getMoveSeq() {
        // 좌 하 우 상 -> 우 하 좌 상
        int[] dx = {0, 1, 0, -1};
        int[] dy = {1, 0, -1, 0};

        moveSeq = new ArrayList<>();
        Queue<int[]> q = new ArrayDeque<>();
        boolean[][] visited = new boolean[n][n];

        int endX = n / 2;
        int endY = n / 2;

        q.offer(new int[] {0, 0});
        visited[0][0] = true;
        int dir = 0;
        while(!q.isEmpty()) {
            int[] cur = q.poll();
            int x = cur[0];
            int y = cur[1];
            moveSeq.add(cur);

            if(endX == x && endY == y) break;

            int nx = x + dx[dir];
            int ny = y + dy[dir];
            if(isNotBoard(nx, ny) || visited[nx][ny]) {
                dir = (dir + 1) % 4;
                nx = x + dx[dir];
                ny = y + dy[dir];
            }
            q.offer(new int[] {nx, ny});
            visited[nx][ny] = true;
        }
        Collections.reverse(moveSeq);
    }

    static int[][][] spread = {
            { {-1, 1}, {1, 1}, {-2, 0}, {2, 0}, {-1, 0}, {1, 0}, {-1, -1}, {1, -1}, {0, -2} }, // 좌
            { {-1, -1}, {-1, 1}, {0, -2}, {0, 2}, {0, -1}, {0, 1}, {1, -1}, {1, 1}, {2, 0} }, // 하
            { {-1, -1}, {1, -1}, {-2, 0}, {2, 0}, {-1, 0}, {1, 0}, {-1, 1}, {1, 1}, {0, 2} }, // 우
            { {1, -1}, {1, 1}, {0, -2}, {0, 2}, {0, -1}, {0, 1}, {-1, -1}, {-1, 1}, {-2, 0} }  // 상
    };
    static int[] percent = {1, 1, 2, 2, 7, 7, 10, 10, 5};
    static int[][] alpha = {
            {0, -1},   // 좌
            {1, 0},    // 하
            {0, 1},    // 우
            {-1, 0}    // 상
    };
    static void spreadDust(int x, int y, int dir) {
        int dust = board[x][y];
        if(dust == 0) return;       // 퍼뜨릴 먼지 X

        board[x][y] = 0;
        int used = 0;

        for(int i = 0; i < 9; i++) {
            int nx = x + spread[dir][i][0];
            int ny = y + spread[dir][i][1];
            int amount = dust * percent[i] / 100;
            used += amount;

            if(isNotBoard(nx, ny)) totalDust += amount;
            else board[nx][ny] += amount;
        }

        int nx = x + alpha[dir][0];
        int ny = y + alpha[dir][1];
        int remain = dust - used;

        if(isNotBoard(nx, ny)) totalDust += remain;
        else board[nx][ny] += remain;
    }


    static boolean isNotBoard(int x, int y) {
        return x < 0 || y < 0 || x >= n || y >= n;
    }
    static int getDir(int dx, int dy) {
        if(dx == 0 && dy == -1) return 0;       // 좌
        if(dx == 1 && dy == 0) return 1;        // 하
        if(dx == 0 && dy == 1) return 2;        // 우
        return 3;                              // 상
    }
}