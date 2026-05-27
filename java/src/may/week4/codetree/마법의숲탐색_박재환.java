package may.week4.codetree;

import java.util.*;
import java.io.*;

public class 마법의숲탐색_박재환 {
    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        init(br);
        br.close();
    }

    static class Input {
        int inputRow;
        int exitDir;

        Input(int inputRow, int exitDir) {
            this.inputRow = inputRow;
            this.exitDir = exitDir;
        }
    }
    /**
     * K명의 정령
     * - 골렘을 타고 숲을 탐색
     *      - 골렘은 십자 모양 구조
     *      - 중앙을 제외한 4칸 중 한 칸은 골렘의 출구
     *      - 탑승은 어디든 상관 X, 탈출은 출구로만 가능
     */
    static int r, c, k;            // r x c 격자 (1, 1) ~ (r, c), k명의 정령
    static int[][] board;
    static boolean[][] exitBoard;
    static Queue<Input> inputs;
    static void init(BufferedReader br) throws IOException {
        StringTokenizer st;
        st = new StringTokenizer(br.readLine().trim());
        r = Integer.parseInt(st.nextToken());
        c = Integer.parseInt(st.nextToken());
        k = Integer.parseInt(st.nextToken());

        board = new int[r][c];
        exitBoard = new boolean[r][c];
        inputs = new ArrayDeque<>();
        for(int i = 0; i < k; i++) {
            st = new StringTokenizer(br.readLine().trim());
            int inputRow = Integer.parseInt(st.nextToken()) - 1;
            int exitDir = Integer.parseInt(st.nextToken());
            Input input = new Input(inputRow, exitDir);
            inputs.offer(input);
        }

        solution();
    }

    static void solution() {
        while(!inputs.isEmpty()) {
            Input input = inputs.poll();
            fall(input);

            // ===
            for(int[] arr : board) System.out.println(Arrays.toString(arr));
            System.out.println();
            // ===
        }
    }

    static class Golem {
        int x, y;       // 중심 좌표 위치
        List<int[]> locs;
        int exitDir;    // 탈출구 위치 -> dx, dy 로 계산 가능

        Golem(int x, int y, int exitDir) {
            locs = new ArrayList<>();
            updateLoc(x, y);
            this.exitDir = exitDir;
        }

        void updateLoc(int x, int y) {
            locs.clear();

            this.x = x;
            this.y = y;

            locs.add(new int[] {x, y});
            locs.add(new int[] {x - 1, y});
            locs.add(new int[] {x, y - 1});
            locs.add(new int[] {x + 1, y});
            locs.add(new int[] {x, y + 1});
        }

        void rotateLeft() {         // 서쪽 방향으로 회전
            if(exitDir == 0) exitDir = 3;
            else if(exitDir == 1) exitDir = 0;
            else if(exitDir == 2) exitDir = 1;
            else exitDir = 2;
        }

        void rotateRight() {        // 동쪽 방향으로 회전
            if(exitDir == 0) exitDir = 1;
            else if(exitDir == 1) exitDir = 2;
            else if(exitDir == 2) exitDir = 3;
            else exitDir = 0;
        }
    }

    // 북 동 남 서
    static int[] dx = {-1, 0, 1, 0};
    static int[] dy = {0, 1, 0 ,-1};
    static void fall(Input input) {
        /**
         * 골렘은 십자 형태를 가지고 있음
         * inputRow : 중앙 열 좌표
         * exitDir : 탈출구 좌표
         *
         * 1. 아래로 떨구기
         * 2. 서쪽 회전 후 떨구기
         * 3. 동쪽 회전 후 떨구기
         */
        Golem golem = new Golem(0, input.inputRow, input.exitDir);
        while(true) {
            // 1. 아래로 떨구기
            if(canFall(golem.x, golem.y)) {
                int nx = golem.x + 1;
                int ny = golem.y;
                golem.updateLoc(nx, ny);
                continue;
            }
            // 2. 서쪽으로 돌려서 떨구기
            if(canFall(golem.x, golem.y - 1)) {
                int nx = golem.x + 1;
                int ny = golem.y - 1;
                golem.updateLoc(nx, ny);
                golem.rotateLeft();
                continue;
            }
            // 3. 동쪽으로 돌려서 떨구기
            if(canFall(golem.x, golem.y + 1)) {
                int nx = golem.x + 1;
                int ny = golem.y + 1;
                golem.updateLoc(nx, ny);
                golem.rotateRight();
                continue;
            }
            break;
        }

        fixFall(golem);
    }

    static void fixFall(Golem golem) {
        boolean isOk = true;
        for(int[] loc : golem.locs) {
            if(isNotBoard(loc[0], loc[1]) ||
                board[loc[0]][loc[1]] != 0) {
                isOk = false;
                break;
            }
        }

        if(!isOk) return;

        for(int[] loc : golem.locs) {
            board[loc[0]][loc[1]] = 1;
        }
    }

    static boolean canFall(int x, int y) {
        List<int[]> temp = new ArrayList<>();
        temp.add(new int[] {x, y});
        temp.add(new int[] {x - 1, y});
        temp.add(new int[] {x, y - 1});
        temp.add(new int[] {x + 1, y});
        temp.add(new int[] {x, y + 1});

        for(int[] loc : temp) {
            // 한 칸 아래로 이동
            int nx = loc[0] + 1;

            if(isNotBoard(nx, loc[1])) return false;        // 격자 밖
            if(board[nx][loc[1]] != 0) return true;         // 다른 골렘이 존재
        }

        return true;
    }

    static boolean isNotBoard(int x, int y) {
        return x < 0 || y < 0 || x >= r || y >= c;
    }
}
