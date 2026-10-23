package oct.week3.swea;

import java.util.*;
import java.io.*;

public class 최적경로_박재환 {
    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringBuilder sb = new StringBuilder();
        int t = Integer.parseInt(br.readLine().trim());
        for(int tc = 1; tc <= t; tc++)
            sb.append('#').append(tc).append(' ')
                            .append(init(br)).append('\n');
        System.out.print(sb);
        br.close();
    }

    static final int INF = Integer.MAX_VALUE / 2;

    static class Loc {
        int x, y;
        Loc(int x, int y) {
            this.x = x;
            this.y = y;
        }
    }

    static int n;
    static Loc[] locs;
    static int[][] board;
    static int init(BufferedReader br) throws IOException {
        StringTokenizer st;
        n = Integer.parseInt(br.readLine().trim()) + 2;
        locs = new Loc[n];
        st = new StringTokenizer(br.readLine().trim());
        for(int i = 0; i < n; i++) {
            locs[i] = new Loc(
                    Integer.parseInt(st.nextToken()),
                    Integer.parseInt(st.nextToken())
            );
        }
        board = new int[n][n];
        for(int i = 0; i < n; i++) {
            for(int j = 0; j < n; j++)
                board[i][j] = Math.abs(locs[i].x - locs[j].x)
                        + Math.abs(locs[i].y - locs[j].y);
        }
        return solution();
    }

    static int FULL;
    static int solution() {
        FULL = ((1 << n) - 1) ^ (1 << 1);
        int[][] costs = new int[n][1 << n];
        for(int i = 0; i < n; i++)
            Arrays.fill(costs[i], -1);
        return findBestRoute(0, (1 << 0), costs);
    }

    static int findBestRoute(int cur, int state, int[][] costs) {
        if(state == FULL)
            return board[cur][1];
        if(costs[cur][state] != -1)
            return costs[cur][state];
        costs[cur][state] = INF;
        for(int next = 2; next < n; next++) {
            if(((1 << next) & state) != 0)
                continue;
            costs[cur][state] = Math.min(
                    costs[cur][state],
                    board[cur][next] + findBestRoute(next, state | (1 << next), costs)
            );
        }
        return costs[cur][state];
    }
}
