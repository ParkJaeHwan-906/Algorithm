package oct.week4.doj;

import java.util.*;
import java.io.*;

public class 외판원순회문제_박재환 {
    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        int t = Integer.parseInt(br.readLine().trim());
        while(t-- > 0)
            init(br);
        br.close();
    }

    static final long INF = Long.MAX_VALUE / 2;

    static int n;
    static long[][] board;
    static void init(BufferedReader br) throws IOException {
        StringTokenizer st;
        n = Integer.parseInt(br.readLine().trim());
        board = new long[n + 1][n + 1];
        for(int x = 1; x <= n; x++) {
            st = new StringTokenizer(br.readLine().trim());
            for(int y = 1; y <= n; y++) {
                board[x][y] = Long.parseLong(st.nextToken());
                if(board[x][y] == -1) board[x][y] = INF;
            }
        }
        System.out.print(solution());
    }

    static int FULL;
    static long[][] costs;
    static int[][] nextNode;
    static String solution() {
        StringBuilder sb = new StringBuilder();
        FULL = ((1 << (n + 1)) - 1) ^ (1 << 0);
        costs = new long[n + 1][FULL + 1];
        nextNode = new int[n + 1][FULL + 1];
        for(int i = 0; i <= n; i++)
            Arrays.fill(costs[i], -1);
        long resultCost = findRoute(1, (1 << 1));
        sb.append(resultCost).append('\n');
        if(resultCost == INF)
            return "-1\n";
        return sb.append(recoverRoute()).append('\n').toString();
    }

    static String recoverRoute() {
        StringBuilder sb = new StringBuilder();
        int cur = 1;
        int state = (1 << 1);
        while(state != FULL) {
            sb.append(cur).append(' ');
            cur = nextNode[cur][state];
            state |= (1 << cur);
        }
        sb.append(cur);
        return sb.toString();
    }

    static long findRoute(int cur, int state) {
        if(state == FULL) {
            if(board[cur][1] != INF)
                return board[cur][1];
            return INF;
        }
        if(costs[cur][state] != -1)
            return costs[cur][state];
        costs[cur][state] = INF;
        for(int next = 1; next <= n; next++) {
            if(board[cur][next] == INF)
                continue;
            if(((1 << next) & state) != 0)
                continue;
            long newCost = board[cur][next] + findRoute(next, state | (1 << next));
            if(costs[cur][state] > newCost) {
                costs[cur][state] = newCost;
                nextNode[cur][state] = next;
            }
        }
        return costs[cur][state];
    }
}
