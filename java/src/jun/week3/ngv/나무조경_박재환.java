package jun.week3.ngv;

import java.util.*;
import java.io.*;

/**
 * [풀이 시간]
 * 00:28:24
 * AI 사용 여부 X
 */
public class 나무조경_박재환 {
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
            for(int y = 0; y < n; y++) board[x][y] = Integer.parseInt(st.nextToken());
        }

        System.out.println(solution());
    }

    static int max;
    static int solution() {
        if(n == 2) {
            /**
             * 2 x 2 격자의 경우 4가지 묶음 경우가 나올 수 없음
             */
            int total = 0;
            for(int x = 0; x < n; x++) {
                for(int y = 0; y < n; y++) total += board[x][y];
            }
            return total;
        }

        max = 0;
        getMaxCombi(0, 0, new boolean[n][n], 0);
        return max;
    }

    static int[] dx = {1, 0};
    static int[] dy = {0, 1};
    static void getMaxCombi(int bundleId, int id, boolean[][] checked, int total) {
        if(bundleId == 4) {
            max = Math.max(total, max);
            return;
        }

        if(id >= n * n) return;

        /**
         * >, v 방향으로만 확인해도 될듯?
         */
        for(int i = id; i < n * n; i++) {
            int x = i / n;
            int y = i % n;

            if (checked[x][y]) continue;

            checked[x][y] = true;
            for (int dir = 0; dir < 2; dir++) {
                int nx = x + dx[dir];
                int ny = y + dy[dir];

                if (isNotBoard(nx, ny)) continue;
                if (checked[nx][ny]) continue;

                checked[nx][ny] = true;
                getMaxCombi(bundleId + 1, i + 1, checked, total + board[x][y] + board[nx][ny]);
                checked[nx][ny] = false;
            }
            checked[x][y] = false;
        }
    }

    static boolean isNotBoard(int x, int y) {
        return x < 0 || y < 0 || x >= n || y >= n;
    }
}
