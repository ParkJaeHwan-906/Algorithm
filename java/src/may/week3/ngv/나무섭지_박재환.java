package may.week3.ngv;

import java.util.*;
import java.io.*;

public class 나무섭지_박재환 {
    static BufferedReader br;
    public static void main(String[] args) throws IOException {
        br = new BufferedReader(new InputStreamReader(System.in));
        init();
        br.close();
    }
    /**
     * n x m 격자
     * - N : 남우
     * - D : 출구
     * - G : 유령
     * - # : 벽
     * - . : 빈 칸
     */
    static StringTokenizer st;
    static int n, m;
    static char[][] board;
    static void init() throws IOException {
        st = new StringTokenizer(br.readLine().trim());
        n = Integer.parseInt(st.nextToken());
        m = Integer.parseInt(st.nextToken());

        board = new char[n][m];
        for(int x = 0; x < n; x++) {
            String line = br.readLine().trim();
            for(int y = 0; y < m; y++) {
                board[x][y] = line.charAt(y);
            }
        }

        // --- board 확인
//        for(char[] arr : board) System.out.println(Arrays.toString(arr));
        // ---

        System.out.println(solution());
    }

    static class Loc {
        int x, y;

        Loc(int x, int y) {
            this.x = x;
            this.y = y;
        }
    }

    static Loc namu;
    static List<Loc> ghosts;
    static Loc exit;
    static String solution() {
        setLoc();
        int ghostTime = getGhostTime();
        int namuTime = getNamuTime(ghostTime);

        return ghostTime > namuTime ? "Yes" : "No";
    }

    static void setLoc() {
        ghosts = new ArrayList<>();
        for(int x = 0; x < n; x++) {
            for(int y = 0; y < m; y++) {
                if(board[x][y] == 'N') namu = new Loc(x, y);
                else if(board[x][y] == 'G') ghosts.add(new Loc(x, y));
                else if(board[x][y] == 'D') exit = new Loc(x, y);
            }
        }
    }

    static int getGhostTime() {
        if(ghosts.isEmpty()) return 0;

        int minTime = Integer.MAX_VALUE;
        for(Loc loc : ghosts) {
            int time = Math.abs(loc.x - exit.x) + Math.abs(loc.y - exit.y);
            minTime = Math.min(time, minTime);
        }

        return minTime;
    }

    static int getNamuTime(int ghostTime) {
        int[] dx = {0, 1, 0, -1};
        int[] dy = {1, 0, -1, 0};

        Queue<int[]> q = new ArrayDeque<>();
        boolean[][] visited = new boolean[n][m];

        q.offer(new int[] {namu.x, namu.y, 0});
        visited[namu.x][namu.y] = true;

        while(!q.isEmpty()) {
            int[] cur = q.poll();
            int x = cur[0];
            int y = cur[1];
            int time = cur[2];

            if(x == exit.x && y == exit.y) return time;

            if(time >= ghostTime) continue;

            for(int dir = 0; dir < 4; dir++) {
                int nx = x + dx[dir];
                int ny = y + dy[dir];

                if(isNotBoard(nx, ny) || visited[nx][ny]) continue;
                if(board[nx][ny] == '#') continue;

                q.offer(new int[] {nx, ny, time + 1});
            }
        }

        return Integer.MAX_VALUE;
    }

    static boolean isNotBoard(int x, int y) {
        return x < 0 || y < 0 || x >= n || y >= m;
    }
}
