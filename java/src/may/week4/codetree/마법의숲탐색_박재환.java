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
        int id;
        int inputRow;
        int exitDir;

        Input(int id, int inputRow, int exitDir) {
            this.id = id;
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

        board = new int[r + 3][c];
        exitBoard = new boolean[r + 3][c];
        inputs = new ArrayDeque<>();
        for(int i = 0; i < k; i++) {
            st = new StringTokenizer(br.readLine().trim());
            int id = i + 1;
            int inputRow = Integer.parseInt(st.nextToken()) - 1;
            int exitDir = Integer.parseInt(st.nextToken());
            Input input = new Input(id, inputRow, exitDir);
            inputs.offer(input);
        }

        System.out.println(solution());
    }

    static int solution() {
        int result = 0;
        while(!inputs.isEmpty()) {
            // 골렘 내리기
            Input input = inputs.poll();
            Golem golem = fall(input);
            if(golem == null) {
                // 초기화
                board = new int[r + 3][c];
                exitBoard = new boolean[r + 3][c];
                continue;
            }
            
            // 정령이동
            result += moveElemental(golem);
        }
        return result;
    }

    static class Golem {
        int id;
        int x, y;       // 중심 좌표 위치
        List<int[]> locs;
        int exitDir;    // 탈출구 위치 -> dx, dy 로 계산 가능

        Golem(int id, int x, int y, int exitDir) {
            this.id = id;
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
    static Golem fall(Input input) {
        /**
         * 골렘은 십자 형태를 가지고 있음
         * inputRow : 중앙 열 좌표
         * exitDir : 탈출구 좌표
         *
         * 1. 아래로 떨구기
         * 2. 서쪽 회전 후 떨구기
         * 3. 동쪽 회전 후 떨구기
         */
        Golem golem = new Golem(input.id, 1, input.inputRow, input.exitDir);
        while (true) {
            if (canDown(golem.x, golem.y)) {
                golem.updateLoc(golem.x + 1, golem.y);
                continue;
            }
            if (canLeft(golem.x, golem.y)) {
                golem.updateLoc(golem.x + 1, golem.y - 1);
                golem.rotateLeft();
                continue;
            }
            if (canRight(golem.x, golem.y)) {
                golem.updateLoc(golem.x + 1, golem.y + 1);
                golem.rotateRight();
                continue;
            }
            break;
        }

        if(!isValidLoc(golem)) return null;
        fixFall(golem);
        return golem;
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
            board[loc[0]][loc[1]] = golem.id;
        }
        int ex = golem.x + dx[golem.exitDir];
        int ey = golem.y + dy[golem.exitDir];
        exitBoard[ex][ey] = true;
    }

    static boolean canDown(int x, int y) {
        int[][] nextLoc = {
                {x, y},
                {x + 1, y},
                {x + 2, y},
                {x + 1, y - 1},
                {x + 1, y + 1}
        };

        for (int[] loc : nextLoc) {
            int nx = loc[0];
            int ny = loc[1];
            if (isNotBoard(nx, ny) || board[nx][ny] != 0) return false;
        }
        return true;
    }

    static boolean canLeft(int x, int y) {
        int[][] nextLoc = {
                // 왼쪽
                {x, y},
                {x, y - 1},
                {x, y - 2},
                {x - 1, y - 1},
                {x + 1, y - 1},
                // 아래 -> (x + 1, y - 1) 기준
        };

        for (int[] loc : nextLoc) {
            int nx = loc[0];
            int ny = loc[1];
            if (isNotBoard(nx, ny) || board[nx][ny] != 0) return false;
        }
        return canDown(x, y - 1);
    }

    static boolean canRight(int x, int y) {
        int[][] nextLoc = {
                // 오른쪽
                {x, y},
                {x, y + 1},
                {x, y + 2},
                {x - 1, y + 1},
                {x + 1, y + 1},
                // 아래 -> (x + 1, y + 1)
        };

        for (int[] loc : nextLoc) {
            int nx = loc[0];
            int ny = loc[1];
            if (isNotBoard(nx, ny) || board[nx][ny] != 0) return false;
        }
        return canDown(x, y + 1);
    }

    static int moveElemental(Golem golem) {
        int x = golem.x, y = golem.y;

        Queue<int[]> q = new ArrayDeque<>();
        boolean[][] visited = new boolean[r + 3][c];

        q.offer(new int[] {x, y});
        visited[x][y] = true;

        int maxX = x;
        while(!q.isEmpty()) {
            int[] cur = q.poll();
            maxX = Math.max(cur[0], maxX);
            int prev = board[cur[0]][cur[1]];
            for(int dir = 0; dir < 4; dir++) {
                int nx = cur[0] + dx[dir];
                int ny = cur[1] + dy[dir];

                if(isNotBoard(nx, ny)) continue;
                if(visited[nx][ny]) continue;
                if(board[nx][ny] == 0) continue;

                // 이동은 두 가지 경우
                // 1. 같은 골렘 내에서 이동
                int next = board[nx][ny];
                if(prev == next) {
                    q.offer(new int[] {nx, ny});
                    visited[nx][ny] = true;
                } else if(prev != next && exitBoard[cur[0]][cur[1]]) {
                    q.offer(new int[] {nx, ny});
                    visited[nx][ny] = true;
                }
            }
        }
        return maxX - 2;
    }

    static boolean isValidLoc(Golem golem) {
        for(int[] loc : golem.locs) {
            int x = loc[0];
            if(x < 3) return false;
        }
        return true;
    }

    static boolean isNotBoard(int x, int y) {
        return x < 0 || y < 0 || x >= (r + 3) || y >= c;
    }
}