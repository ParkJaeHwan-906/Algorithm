package jun.week2.codetree;

import java.util.*;
import java.io.*;

/**
 * [풀이 시간]
 * 00:34:35
 * AI 사용 여부 X
 */
public class 정육면체굴리기_박재환 {
    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        init(br);
        br.close();
    }
    static class Dice {
        int x, y;
        int top;
        int east, west, south, north;
        int bottom;

        Dice(int x, int y) {
            this.x = x;
            this.y = y;

            this.top = 0;

            this.east = 0;      // 동
            this.west = 0;      // 서
            this.north = 0;     // 남
            this.south = 0;     // 북

            this.bottom = 0;
        }

        void rollEast(int x, int y) {
            int tempEast = this.east;
            int tempWest = this.west;
            int tempTop = this.top;
            int tempBottom = this.bottom;

            this.east = tempTop;
            this.bottom = tempEast;
            this.west = tempBottom;
            this.top = tempWest;

            updateLoc(x, y);
        }

        void rollWest(int x, int y) {
            int tempEast = this.east;
            int tempWest = this.west;
            int tempTop = this.top;
            int tempBottom = this.bottom;

            this.west = tempTop;
            this.bottom = tempWest;
            this.east = tempBottom;
            this.top = tempEast;

            updateLoc(x, y);
        }

        void rollNorth(int x, int y) {
            int tempTop = this.top;
            int tempNorth = this.north;
            int tempBottom = this.bottom;
            int tempSouth = this.south;

            this.north = tempTop;
            this.bottom = tempNorth;
            this.south = tempBottom;
            this.top = tempSouth;

            updateLoc(x, y);
        }

        void rollSouth(int x, int y) {
            int tempTop = this.top;
            int tempNorth = this.north;
            int tempBottom = this.bottom;
            int tempSouth = this.south;

            this.south = tempTop;
            this.bottom = tempSouth;
            this.north = tempBottom;
            this.top = tempNorth;

            updateLoc(x, y);
        }

        void updateLoc(int x, int y) {
            this.x = x;
            this.y = y;
        }
    }

    static int n, m, k;
    static Dice dice;
    static int[][] board;
    static int[] roll;
    static void init(BufferedReader br) throws IOException {
        StringTokenizer st;

        st = new StringTokenizer(br.readLine().trim());
        n = Integer.parseInt(st.nextToken());
        m = Integer.parseInt(st.nextToken());

        int diceX = Integer.parseInt(st.nextToken());
        int diceY = Integer.parseInt(st.nextToken());
        dice = new Dice(diceX, diceY);

        k = Integer.parseInt(st.nextToken());

        board = new int[n][m];
        for(int x = 0; x < n; x++) {
            st = new StringTokenizer(br.readLine().trim());
            for(int y = 0; y < m; y++) board[x][y] = Integer.parseInt(st.nextToken());
        }

        roll = new int[k];
        st = new StringTokenizer(br.readLine().trim());
        for(int i = 0; i < k; i++) roll[i] = Integer.parseInt(st.nextToken());

        System.out.println(solution());
    }

    static int[] dx = {0, 0, 0, -1, 1};
    static int[] dy = {0, 1, -1, 0, 0};
    static String solution() {
        StringBuilder sb = new StringBuilder();
        for(int dir : roll) {
            /**
             * 1 : 동
             * 2 : 서
             * 3 : 북
             * 4 : 남
             */
            int nx = dice.x + dx[dir];
            int ny = dice.y + dy[dir];
            if(isNotBoard(nx, ny)) continue;

            if(dir == 1) {
                dice.rollEast(nx, ny);
            } else if(dir == 2) {
                dice.rollWest(nx, ny);
            } else if(dir == 3) {
                dice.rollNorth(nx, ny);
            } else if(dir == 4) {
                dice.rollSouth(nx, ny);
            }
            compareBottomToGrid();
            sb.append(dice.top).append('\n');
        }

        return sb.toString();
    }

    static void compareBottomToGrid() {
        int gridNum = board[dice.x][dice.y];
        int diceNum = dice.bottom;

        if(gridNum == 0) {
            board[dice.x][dice.y] = diceNum;
        } else {
            dice.bottom = gridNum;
            board[dice.x][dice.y] = 0;
        }
    }

    static boolean isNotBoard(int x, int y) {
        return x < 0 || y < 0 || x >= n || y >= m;
    }
}
