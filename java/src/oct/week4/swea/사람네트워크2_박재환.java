package oct.week4.swea;

import java.util.*;
import java.io.*;

public class 사람네트워크2_박재환 {
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

    static int n;
    static int[][] board;
    static int init(BufferedReader br) throws IOException {
        StringTokenizer st = new StringTokenizer(br.readLine().trim());
        n = Integer.parseInt(st.nextToken());
        board = new int[n][n];
        for(int x = 0; x < n; x++) {
            for(int y = 0; y < n; y++) {
                board[x][y] = Integer.parseInt(st.nextToken());
                if(x != y && board[x][y] == 0)
                    board[x][y] = INF;
            }
        }
        return solution();
    }

    static int solution() {
        getAllDist();
        int minCC = INF;
        for(int i = 0; i < n; i++) {
            int sum = 0;
            for(int j = 0; j < n; j++) {
                if(board[i][j] == INF)
                    continue;
                sum += board[i][j];
            }
            minCC = Math.min(minCC, sum);
        }
        return minCC;
    }

    static void getAllDist() {
        for(int mid = 0; mid < n; mid++) {
            for(int start = 0; start < n; start++) {
                for (int end = 0; end < n; end++) {
                    board[start][end] = Math.min(
                            board[start][end],
                            board[start][mid] + board[mid][end]
                    );
                }
            }
        }
    }
}
