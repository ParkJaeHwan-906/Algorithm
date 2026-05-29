package jun.week1.codetree;

import java.util.*;
import java.io.*;

public class 시공의돌풍_박재환 {
    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        init(br);
        br.close();
    }
    static int n, m, t;
    static int[][] board;
    static void init(BufferedReader br) throws IOException {
        StringTokenizer st;
        st = new StringTokenizer(br.readLine().trim());
        n = Integer.parseInt(st.nextToken());
        m = Integer.parseInt(st.nextToken());
        t = Integer.parseInt(st.nextToken());

        board = new int[n][m];
        for(int i = 0; i < n; i++) {
            st = new StringTokenizer(br.readLine().trim());
            for(int j = 0; j < m; j++) board[i][j] = Integer.parseInt(st.nextToken());
        }
        solution();
    }
    static void setStorm() {
        storm = new ArrayList<>();
        for(int i = 0; i < n; i++) {
            for(int j = 0; j < m; j++) {
                if(board[i][j] == -1) storm.add(new int[] {i, j});
            }
        }
    }

    static List<int[]> storm;
    static void solution() {
        setStorm();

        while(t-- > 0) {
            // 1. 먼지 확산

            // 2. 청소
        }
    }

    static int[] dx = {0, 1, 0, -1};
    static int[] dy = {1, 0, -1, 0};
    static void spreadDusts() {
        int[][] lazyBoard = new int[n][m];

        for(int i = 0; i < n; i++) {
            for(int j = 0; j < m; j++) {
                if(board[i][j] == -1) continue;


            }
        }
    }
    static int spreadDust(int x, int y, int[][] lazyBoard) {
        int spread = 0;
        int mod = board[x][y] /
        for(int dir = 0; dir < 4; dir++) {
            int nx = x + dx[dir];
            int ny = y + dy[dir];

            if(isNotBoard(nx, ny)) continue;
            if(board[nx][ny] == -1) continue;


        }
    }

    static boolean isNotBoard(int x, int y) {
        return x < 0 || y < 0 || x >= n || y >= m;
    }
}
