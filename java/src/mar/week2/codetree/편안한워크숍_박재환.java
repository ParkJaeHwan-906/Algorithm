package mar.week2.codetree;

import java.util.*;
import java.io.*;

public class 편안한워크숍_박재환 {
    static BufferedReader br;
    public static void main(String[] args) throws IOException {
        br = new BufferedReader(new InputStreamReader(System.in));
        init();
        br.close();
    }
    /**
     * N x N -> 각 칸은 높이
     * 인접한 높이의 차들 간 최대값이 최소가 되는 등산로 찾기
     *
     * 1. 등산로는 어느 칸에서 시작해도 상관 없다.
     * 2. 이동은 상하좌우, 이동할 때마다 높이가 높아져야한다.
     * 3. 등산로의 길이는 K 이상이어야한다.
     */
    static final int INF = Integer.MAX_VALUE;

    static StringTokenizer st;
    static int n, k;
    static int[][] board;
    static int[][][] dp;
    static void init() throws IOException {
        st = new StringTokenizer(br.readLine().trim());
        n = Integer.parseInt(st.nextToken());
        k = Integer.parseInt(st.nextToken());

        board = new int[n][n];
        for(int x=0; x<n; x++) {
            st = new StringTokenizer(br.readLine().trim());
            for(int y=0; y<n; y++) board[x][y] = Integer.parseInt(st.nextToken());
        }

        dp = new int[n][n][k+1];
        for(int x=0; x<n; x++){
            for(int y=0; y<n; y++){
                Arrays.fill(dp[x][y], INF);
                dp[x][y][1] = 0;
            }
        }
        findBestRoute();
        int answer = INF;
        for(int x=0;x<n;x++){
            for(int y=0;y<n;y++){
                answer = Math.min(answer, dp[x][y][k]);
            }
        }
        System.out.println(answer == INF ? -1 : answer);
    }

    static int[] dx = {0,1,0,-1};
    static int[] dy = {1,0,-1,0};
    static void findBestRoute() {
        for(int l=1; l<k; l++) {
            for(int x=0; x<n; x++){
                for(int y=0; y<n; y++){
                    if(dp[x][y][l] == INF) continue;

                    for(int dir=0; dir<4; dir++) {
                        int nx = x + dx[dir];
                        int ny = y + dy[dir];
                        if(nx < 0 || ny < 0 || nx >= n || ny >= n) continue;
                        if(board[x][y] >= board[nx][ny]) continue;

                        int diff = board[nx][ny] - board[x][y];
                        int next = Math.max(dp[x][y][l], diff);     // 다음 위치 후보 값 (현 위치까지 최대 높이 차, 현 -> 다음 높이 차)
                        dp[nx][ny][l+1] =
                                Math.min(dp[nx][ny][l+1], next);        // 갱신
                    }
                }
            }
        }
    }
}
