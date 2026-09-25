package sep.week3.jungol;

import java.util.*;
import java.io.*;

public class 미로만들기_박재환 {
    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        init(br);
        br.close();
    }

    static final int[] dx = {0, 1, 0, -1};
    static final int[] dy = {1, 0, -1, 0};

    static int n;
    static int[][] board;
    static void init(BufferedReader br) throws IOException {
        n = Integer.parseInt(br.readLine().trim());
        board = new int[n][n];
        for(int x = 0; x < n; x++) {
            String line = br.readLine().trim();
            for(int y = 0; y < n; y++) {
                board[x][y] = line.charAt(y) - '0';
            }
        }
        System.out.print(solution());
    }

    static int solution() {
        ArrayDeque<int[]> q = new ArrayDeque<>();
        boolean[][] visited = new boolean[n][n];
        q.offerFirst(new int[] {0, 0, 0});      // [x, y, cost]
        visited[0][0] = true;
        while(!q.isEmpty()) {
            int[] cur = q.pollFirst();
            if(cur[0] == n - 1 && cur[1] == n - 1) {
                return cur[2];
            }
            for(int d = 0; d < 4; d++) {
                int nx = cur[0] + dx[d];
                int ny = cur[1] + dy[d];
                if(nx < 0 || ny < 0 || nx >= n || ny >= n) {
                    continue;
                }
                if(visited[nx][ny]) {
                    continue;
                }
                visited[nx][ny] = true;
                if(board[nx][ny] == 0) {
                    q.offerLast(new int[] {nx, ny, cur[2] + 1});
                } else if(board[nx][ny] == 1) {
                    q.offerFirst(new int[] {nx, ny, cur[2]});
                }
            }
        }
        return -1;
    }
}
