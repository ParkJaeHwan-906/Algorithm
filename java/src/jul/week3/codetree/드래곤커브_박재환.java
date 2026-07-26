package jul.week3.codetree;

import java.util.*;
import java.io.*;

public class 드래곤커브_박재환 {
    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        init(br);
        br.close();
    }

    static class DragonCurve {
        int x, y;       // 시작점
        int dir;        // 시작 방향
        int g;          // 차수

        List<Integer> commands;

        DragonCurve(int x, int y, int dir, int g) {
            this.x = x;
            this.y = y;
            this.dir = dir;
            this.g = g;

            commands = new ArrayList<>();
        }

        void copy() {
            int lastId = commands.size() - 1;

            // n - 1차 드래곤 커브의 방향을 역순으로 확인
            for (int i = lastId; i >= 0; i--) {
                int dir = commands.get(i);
                commands.add((dir + 1) % 4);
            }
        }
    }
    // 우 상 좌 하
    static final int[] dx = {0, -1, 0, 1};
    static final int[] dy = {1, 0, -1, 0};

    static int[][] board;
    static void init(BufferedReader br) throws IOException {
        StringTokenizer st;

        board = new int[101][101];
        int n = Integer.parseInt(br.readLine().trim());
        for(int i = 0; i < n; i++) {
            st = new StringTokenizer(br.readLine().trim());
            int x = Integer.parseInt(st.nextToken());
            int y = Integer.parseInt(st.nextToken());
            int dir = Integer.parseInt(st.nextToken());
            int g = Integer.parseInt(st.nextToken());
            DragonCurve dragonCurve = new DragonCurve(x, y, dir, g);
            solution(dragonCurve);
            applyBoard(dragonCurve);
        }

        System.out.println(countSquares());
    }

    static void solution(DragonCurve dragonCurve) {
        // 0차 드래곤 커브
        dragonCurve.commands.add(dragonCurve.dir);
        for (int i = 0; i < dragonCurve.g; i++) {
            dragonCurve.copy();
        }
    }

    static void applyBoard(DragonCurve dragonCurve) {
        int x = dragonCurve.x;
        int y = dragonCurve.y;
        board[x][y] = 1;

        for (int dir : dragonCurve.commands) {
            x += dx[dir];
            y += dy[dir];

            board[x][y] = 1;
        }
    }

    static int countSquares() {
        int count = 0;
        for (int x = 0; x < 100; x++) {
            for (int y = 0; y < 100; y++) {
                if (board[x][y] == 1
                        && board[x + 1][y] == 1
                        && board[x][y + 1] == 1
                        && board[x + 1][y + 1] == 1) {
                    count++;
                }
            }
        }
        return count;
    }
}
