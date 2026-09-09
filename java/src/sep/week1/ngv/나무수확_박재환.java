package sep.week1.ngv;

import java.util.*;
import java.io.*;
public class 나무수확_박재환 {
    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        init(br);
        br.close();
    }
    static int n;
    static int[][] board;
    static void init(BufferedReader br) throws IOException {
        StringTokenizer st;

        n = Integer.parseInt(br.readLine().trim());
        board = new int[n + 1][n + 1];
        for(int x = 1; x <= n; x++) {
            st = new StringTokenizer(br.readLine().trim());
            for(int y = 1; y <= n; y++) {
                board[x][y] = Integer.parseInt(st.nextToken());
            }
        }
        System.out.println(solution());
    }

    static long solution() {
        long[][][] dp = new long[n + 1][n + 1][2];        //[x][y][현재까지 스프링쿨러 설치 O/X]

        for(int x = 1; x <= n; x++) {
            for(int y = 1; y <= n ; y++) {
                long notUsed = Math.max(dp[x - 1][y][0], dp[x][y - 1][0]);
                long used = Math.max(dp[x - 1][y][1], dp[x][y - 1][1]);
                dp[x][y][0] = notUsed + board[x][y];
                dp[x][y][1] = Math.max(used + board[x][y], notUsed + (board[x][y] + board[x][y]));
            }
        }
        return dp[n][n][1];
    }
}
