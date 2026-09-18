package sep.week2.jungol;

import java.util.*;
import java.io.*;

public class 해밀턴순환회로2_박재환 {
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
        board = new int[n][n];
        for(int x = 0; x < n; x++) {
            st = new StringTokenizer(br.readLine().trim());
            for(int y = 0; y < n; y++) {
                board[x][y] = Integer.parseInt(st.nextToken());
            }
        }
        System.out.println(solution());
    }

    static int FULL;
    static int minCost;
    static int[][] dp;
    static int solution() {
        FULL = (1 << n) - 1;
        minCost = Integer.MAX_VALUE;
        dp = new int[FULL + 1][n];          // [state][cur]
        for(int x = 0; x < FULL + 1; x++) {
            Arrays.fill(dp[x], -1);
        }
        return calcCostToTSP(0, 1);
    }

    static int calcCostToTSP(int cur, int state) {
        if(state == FULL) {
            return board[cur][0] == 0
                    ? Integer.MAX_VALUE : board[cur][0];
        }

        if(dp[state][cur] != -1) {
            return dp[state][cur];
        }

        int minCost = Integer.MAX_VALUE;
        for(int next = 0; next < n; next++) {
            if ((state & (1 << next)) != 0) {
                continue;
            }

            if (board[cur][next] == 0) {
                continue;
            }

            int nextCost = calcCostToTSP(next, state | (1 << next));
            if(nextCost == Integer.MAX_VALUE) {
                continue;
            }
            minCost = Math.min(minCost, board[cur][next] + nextCost);
        }
        return dp[state][cur] = minCost;
    }
}
