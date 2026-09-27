package sep.week3.jungol;

import java.util.*;
import java.io.*;

public class 해밀턴순환회로_박재환 {
    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        init(br);
        br.close();
    }

    static final int INF = 987654321;

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
        System.out.print(solution());
    }

    static int full;
    static int[][] routes;
    static int solution() {
        set();
        return tsp(0, 1 << 0);
    }

    static void set() {
        full = (1 << n) - 1;
        routes = new int[n][full + 1];
        for(int i = 0; i < n; i++) {
            Arrays.fill(routes[i], -1);
        }
    }

    static int tsp(int cur, int state) {
        if(state == full) {                         // 모든 도시를 방문한 경우
            if(board[cur][0] == 0) {                // 현재 도시에서 시작점으로 돌아갈 수 없음
                return INF;
            }
            return board[cur][0];
        }

        if(routes[cur][state] != -1) {
            return routes[cur][state];
        }
        routes[cur][state] = INF;
        for(int next = 0; next < n; next++) {
            if(board[cur][next] == 0) {
                continue;
            }
            if((state & (1 << next)) != 0) {
                continue;
            }
            int nextState = state | (1 << next);
            int nextCost = tsp(next, nextState);
            if(nextCost == INF) {
                continue;
            }
            routes[cur][state] = Math.min(
                    routes[cur][state],
                    board[cur][next] + nextCost
            );
        }
        return routes[cur][state];
    }
}