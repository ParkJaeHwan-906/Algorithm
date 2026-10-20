package oct.week3.codetree;

import java.util.*;
import java.io.*;

public class 외판원순회_박재환 {
    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        System.out.print(init(br));
        br.close();
    }

    static final int INF = Integer.MAX_VALUE / 2;

    static int n;
    static int[][] connections;
    static int init(BufferedReader br) throws IOException {
        StringTokenizer st;
        n = Integer.parseInt(br.readLine().trim());
        connections = new int[n][n];
        for(int x = 0; x < n; x++) {
            st = new StringTokenizer(br.readLine().trim());
            for(int y = 0; y < n; y++)
                connections[x][y] = Integer.parseInt(st.nextToken());
        }
        return solution();
    }

    static int FULL;
    static int[][] costs;
    static int solution() {
        FULL = (1 << n) - 1;
        costs = new int[n][FULL + 1];
        for(int i = 0; i < n; i++)
            Arrays.fill(costs[i], -1);
        return findBestRoute(0, 1);
    }

    static int findBestRoute(int cur, int state) {
        if(state == FULL) {
            if(connections[cur][0] != 0) {
                return connections[cur][0];
            }
            return INF;
        }
        if(costs[cur][state] != -1)
            return costs[cur][state];

        costs[cur][state] = INF;
        for(int next = 0; next < n; next++) {
            if(connections[cur][next] == 0)
                continue;
            if(((1 << next) & state) != 0)
                continue;
            int nextCost = findBestRoute(next, state | (1 << next));
            if(nextCost == INF)
                continue;
            costs[cur][state] = Math.min(
                    costs[cur][state],
                    connections[cur][next] + nextCost
            );
        }
        return costs[cur][state];
    }
}
