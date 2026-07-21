package jul.week2.jungol;

import java.util.*;
import java.io.*;

public class 경로찾기_박재환 {
    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        init(br);
        br.close();
    }

    static int n, t;
    static int[][] board;
    static void init(BufferedReader br) throws IOException {
        StringTokenizer st;

        st = new StringTokenizer(br.readLine().trim());
        n = Integer.parseInt(st.nextToken());
        t = Integer.parseInt(st.nextToken());

        board = new int[n][n];
        for(int x = 0; x < n; x++) {
            st = new StringTokenizer(br.readLine().trim());
            /**
             * 0 : 건물
             * -1 : 출발점
             * 2 : 도착점
             */
            for(int y = 0; y < n;) board[x][y++] = Integer.parseInt(st.nextToken());
        }

        System.out.println(solution());
    }
    static final int INF = Integer.MAX_VALUE;
    static final int[] dx = {0, 1, 0, -1};
    static final int[] dy = {1, 0, -1, 0};
    static int solution() {
        int[][] locs = set();
        int[] start = locs[0];
        int[] end = locs[1];

        int[][] dp = new int[n][n];
        for(int x = 0; x < n; x++) Arrays.fill(dp[x], INF);

        Queue<int[]> q = new ArrayDeque<>();
        q.offer(new int[] {start[0], start[1], 0, 0});
        dp[start[0]][start[1]] = 0;

        while(!q.isEmpty()) {
            int[] cur = q.poll();
            int x = cur[0];
            int y = cur[1];
            int acc = cur[2];
            int step = cur[3];
            if(x == end[0] && y == end[1]) {
                if(dp[x][y] > acc) dp[x][y] = acc;
                continue;
            }
            if(dp[x][y] < acc || step >= t) continue;
            for(int dir = 0; dir < 4; dir++) {
                int nx = x + dx[dir];
                int ny = y + dy[dir];

                if(nx < 0 || ny < 0 || nx >= n || ny >= n) continue;
                if(board[nx][ny] == -1) continue;
                if(dp[nx][ny] > board[nx][ny] + acc) {
                    dp[nx][ny] = board[nx][ny] + acc;
                    q.offer(new int[] {nx, ny, dp[nx][ny], step + 1});
                }
            }
        }

        return dp[end[0]][end[1]] == INF ? -1 : dp[end[0]][end[1]];
    }

    static int[][] set() {
        int[][] result = new int[2][];
        for(int x = 0; x < n; x++) {
            for(int y = 0; y < n; y++) {
                if(board[x][y] == -1) result[0] = new int[] {x, y};
                else if(board[x][y] == -2) result[1] = new int[] {x, y};
                else if(board[x][y] == 0) board[x][y] = -1;
            }
        }
        board[result[0][0]][result[0][1]] = 0;
        board[result[1][0]][result[1][1]] = 0;
        return result;
    }
}
