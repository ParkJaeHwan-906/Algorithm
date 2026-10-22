package oct.week3.swea;

import java.util.*;
import java.io.*;

public class 보급로_박재환 {
    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringBuilder sb = new StringBuilder();
        int t = Integer.parseInt(br.readLine().trim());
        for(int tc = 1; tc <= t; tc++) {
            sb.append('#').append(tc).append(' ')
                    .append(init(br)).append('\n');
        }
        System.out.print(sb);
        br.close();
    }

    static final int INF = Integer.MAX_VALUE / 2;
    static final int[] dx = {0, 1, 0, -1};
    static final int[] dy = {1, 0, -1, 0};

    static int n;
    static int[][] board;
    static int init(BufferedReader br) throws IOException {
        n = Integer.parseInt(br.readLine().trim());
        board = new int[n][n];
        for(int x = 0; x < n; x++) {
            String line = br.readLine().trim();
            for(int y = 0; y < n; y++)
                board[x][y] = line.charAt(y) - '0';
        }
        return solution();
    }

    static int solution() {
        PriorityQueue<int[]> pq = new PriorityQueue<>((a, b) -> {       // [x, y, cost]
            return Integer.compare(a[2], b[2]);
        });
        int[][] costs = new int[n][n];
        for(int x = 0; x < n; x++)
            Arrays.fill(costs[x], INF);
        pq.offer(new int[] {0, 0, 0});
        costs[0][0] = 0;
        while(!pq.isEmpty()) {
            int[] cur = pq.poll();
            if(cur[0] == n - 1 && cur[1] == n - 1)
                return cur[2];
            if(cur[2] > costs[cur[0]][cur[1]])
                continue;
            for(int dir = 0; dir < 4; dir++) {
                int nx = cur[0] + dx[dir];
                int ny = cur[1] + dy[dir];
                if(isNotBoard(nx, ny))
                    continue;
                if(costs[nx][ny] > cur[2] + board[nx][ny]) {
                    costs[nx][ny] = cur[2] + board[nx][ny];
                    pq.offer(new int[] {nx, ny, costs[nx][ny]});
                }
            }
        }
        return -1;
    }

    static boolean isNotBoard(int x, int y) {
        return x < 0 || y < 0 || x >= n || y >= n;
    }
}
