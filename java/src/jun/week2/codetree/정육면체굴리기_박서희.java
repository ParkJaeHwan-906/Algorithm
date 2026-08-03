package jun.week2.codetree;

import java.io.*;
import java.util.*;

/*
  문제풀이 시간: 00:37:40
  AI 사용 여부: X
 */
public class 정육면체굴리기_박서희 {

    static int n, m;
    static int[][] board;
    static Dice dice;

    static final int[] dx = {0, 0, -1, 1};
    static final int[] dy = {1, -1, 0, 0};

    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer st = new StringTokenizer(br.readLine());

        n = Integer.parseInt(st.nextToken());
        m = Integer.parseInt(st.nextToken());
        int x = Integer.parseInt(st.nextToken());
        int y = Integer.parseInt(st.nextToken());
        int k = Integer.parseInt(st.nextToken());

        dice = new Dice(x, y);

        board = new int[n][m];
        for (int i = 0; i < n; i++) {
            st = new StringTokenizer(br.readLine());
            for (int j = 0; j < m; j++) {
                board[i][j] = Integer.parseInt(st.nextToken());
            }
        }

        StringBuilder sb = new StringBuilder();
        st = new StringTokenizer(br.readLine());
        while (k-- > 0) {
            int dir = Integer.parseInt(st.nextToken()) - 1;

            int nx = dice.x + dx[dir], ny = dice.y + dy[dir];
            if (!inRange(nx, ny)) continue;
            dice.x = nx;
            dice.y = ny;

            sb.append(moveDice(dir)).append("\n");
        }
        System.out.println(sb);
    }

    static int moveDice(int dir) {
        if (dir == 0) {         // 동
            int temp = dice.bottom;
            dice.bottom = dice.east;
            dice.east = dice.top;
            dice.top = dice.west;
            dice.west = temp;
        } else if (dir == 1) {  // 서
            int temp = dice.bottom;
            dice.bottom = dice.west;
            dice.west = dice.top;
            dice.top = dice.east;
            dice.east = temp;
        } else if (dir == 2) {  // 북
            int temp = dice.bottom;
            dice.bottom = dice.north;
            dice.north = dice.top;
            dice.top = dice.south;
            dice.south = temp;
        } else if (dir == 3) {  // 남
            int temp = dice.bottom;
            dice.bottom = dice.south;
            dice.south = dice.top;
            dice.top = dice.north;
            dice.north = temp;
        }


        if (board[dice.x][dice.y] == 0) {
            board[dice.x][dice.y] = dice.bottom;
        } else {
            dice.bottom = board[dice.x][dice.y];
            board[dice.x][dice.y] = 0;
        }
        return dice.top;
    }

    static boolean inRange(int x, int y) {
        return 0 <= x && x < n && 0 <= y && y < m;
    }

    static class Dice {
        int x, y;
        int top, bottom, west, east, north, south;

        public Dice(int x, int y) {
            this.x = x;
            this.y = y;
            this.top = 0;
            this.bottom = 0;
            this.west = 0;
            this.east = 0;
            this.north = 0;
            this.south = 0;
        }
    }
}
