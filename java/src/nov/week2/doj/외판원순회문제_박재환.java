package nov.week2.doj;

import java.util.*;
import java.io.*;

public class 외판원순회문제_박재환 {
    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringBuilder sb = new StringBuilder();
        int tc = Integer.parseInt(br.readLine().trim());
        for(int i = 0; i < tc; i++) {
            sb.append(init(br)).append('\n');
        }
        System.out.print(sb);
        br.close();
    }

    static final long INF = Long.MAX_VALUE / 2;

    static int n;
    static int[][] board;
    static String init(BufferedReader br) throws IOException {
        StringTokenizer st;

        n = Integer.parseInt(br.readLine().trim());
        board = new int[n][n];
        for(int x = 0; x < n; x++) {
            st = new StringTokenizer(br.readLine().trim());
            for(int y = 0; y < n; y++)
                board[x][y] = Integer.parseInt(st.nextToken());
        }
        return solution();
    }

    static int FULL;
    static long[][] dp;
    static int[][] next;
    static String solution() {
        FULL = (1 << n) - 1;
        dp = new long[n][FULL + 1];
        next = new int[n][FULL + 1];
        for(int i = 0; i < n; i++) {
            Arrays.fill(dp[i], -1);
            Arrays.fill(next[i], -1);
        }
        long resultCost = findRoute(0, (1 << 0));
        if(resultCost == INF) {
            return "-1";
        }
        return String.format("%d\n%s", resultCost, recoverRoute());
    }

    static String recoverRoute() {
        StringBuilder sb = new StringBuilder();
        int cur = 0;
        int state = (1 << 0);
        while(state != FULL) {
            sb.append((cur + 1)).append(' ');
            int nextLoc = next[cur][state];
            cur = nextLoc;
            state |= (1 << nextLoc);
        }
        sb.append((cur + 1)).append(' ');
        return sb.toString();
    }

    static long findRoute(int cur, int state) {
        if(state == FULL) {
            if(board[cur][0] != -1) {
                return board[cur][0];
            }
            return INF;
        }

        if(dp[cur][state] != -1) {
            return dp[cur][state];
        }

        dp[cur][state] = INF;
        for(int i = 0; i < n; i++) {
            if(((1 << i) & state) != 0)
                continue;
            if(board[cur][i] != -1) {
                int nextState = (1 << i) | state;
                long nextCost = findRoute(i, nextState) + board[cur][i];
                if(dp[cur][state] > nextCost) {
                    dp[cur][state] = nextCost;
                    next[cur][state] = i;
                }
            }
        }
        return dp[cur][state];
    }
}
