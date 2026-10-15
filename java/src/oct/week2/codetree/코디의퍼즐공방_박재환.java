package oct.week2.codetree;

import java.util.*;
import java.io.*;

public class 코디의퍼즐공방_박재환 {
    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        init(br);
        br.close();
    }

    static final int[] dx = {-1, 0, 1, 0};
    static final int[] dy = {0, 1, 0, -1};

    static final int SET = 100;
    static final int ADD = 200;
    static final int DEL = 300;
    static final int ADD_SPARE = 400;
    static final int MATCH = 500;

    /**
     * 0 : 오목
     * 1 : 직선
     * 2 : 볼록
     */
    static class Puzzle {
        int[] shape;
        Puzzle(int[] shape) {
            this.shape = shape;
        }
        void rotateClockWise() {
            int[] temp = new int[4];
            for(int i = 0; i < 4; i++) {
                temp[(i + 1) % 4] = this.shape[i];
            }
            this.shape = temp;
        }
        int need(int dir) {
            if(shape[dir] == 1)
                return 1;
            else if(shape[dir] == 0)
                return 2;
            return 0;
        }
    }

    static int n;
    static Puzzle[][] board;
    static int[] sparePuzzles;
    static int[] holes;
    static void init(BufferedReader br) throws IOException {
        StringTokenizer st;
        StringBuilder sb = new StringBuilder();
        int q = Integer.parseInt(br.readLine().trim());
        while(q-- > 0) {
            st = new StringTokenizer(br.readLine().trim());
            int type = Integer.parseInt(st.nextToken());
            if(type == SET)
                set(st);
            else if(type == ADD)
                add(st);
            else if(type == DEL)
                del(st);
            else if(type == ADD_SPARE)
                addSpare(st);
            else if(type == MATCH)
                sb.append(match()).append('\n');
        }
        System.out.print(sb);
    }

    static void set(StringTokenizer st) {
        sparePuzzles = new int[81];
        holes = new int[81];
        n = Integer.parseInt(st.nextToken());
        board = new Puzzle[n][n];
    }

    static void add(StringTokenizer st) {
        int y = Integer.parseInt(st.nextToken()) - 1;
        int x = Integer.parseInt(st.nextToken()) - 1;
        int u = Integer.parseInt(st.nextToken());
        int r = Integer.parseInt(st.nextToken());
        int d = Integer.parseInt(st.nextToken());
        int l = Integer.parseInt(st.nextToken());

        int prev = getNeed(y, x);
        if(prev != -1)
            holes[prev]--;
        board[y][x] = new Puzzle(new int[] {u, r, d, l});
        for(int dir = 0; dir < 4; dir++) {
            int nx = y + dx[dir];
            int ny = x + dy[dir];
            if(isNotBoard(nx, ny))
                continue;
            if(board[nx][ny] != null)
                continue;
            int need = getNeed(nx, ny);
            if(need != -1)
                holes[need]++;
        }
    }

    static void del(StringTokenizer st) {
        int y = Integer.parseInt(st.nextToken()) - 1;
        int x = Integer.parseInt(st.nextToken()) - 1;
        for(int dir = 0; dir < 4; dir++) {
            int nx = y + dx[dir];
            int ny = x + dy[dir];
            if(isNotBoard(nx, ny))
                continue;
            if(board[nx][ny] != null)
                continue;
            int need = getNeed(nx, ny);
            if(need != -1)
                holes[need]--;
        }
        board[y][x] = null;
        int prev = getNeed(y, x);
        if(prev != -1)
            holes[prev]++;
    }

    static void addSpare(StringTokenizer st) {
        int u = Integer.parseInt(st.nextToken());
        int r = Integer.parseInt(st.nextToken());
        int d = Integer.parseInt(st.nextToken());
        int l = Integer.parseInt(st.nextToken());
        Puzzle puzzle = new Puzzle(new int[] {u, r, d, l});
        boolean[] visited = new boolean[81];
        for(int i = 0; i < 4; i++) {
            int key = encode(puzzle.shape);
            if(!visited[key]){
                visited[key] = true;
                sparePuzzles[key]++;
            }
            puzzle.rotateClockWise();
        }
    }

    static long match() {
        long total = 0L;
        for(int i = 0; i < 81; i++) {
            total += (long) holes[i] * sparePuzzles[i];
        }
        return total;
    }

    static int getNeed(int x, int y) {
        int[] needs = new int[4];
        for(int dir = 0; dir < 4; dir++) {
            int nx = x + dx[dir];
            int ny = y + dy[dir];
            if(isNotBoard(nx, ny))            // 테두리와 닿아있는 경우에는 직선이 필요
                needs[dir] = 1;
            else if(board[nx][ny] == null)    // 빈 칸인 경우 아무거나 가능
                return -1;
            else                               // 블록인 경우
                needs[dir] = board[nx][ny].need((dir + 2) % 4);


        }
        return encode(needs);
    }

    static boolean isNotBoard(int x, int y) {
        return x < 0 || y < 0 || x >= n || y >= n;
    }

    static int encode(int[] shape) {
        int key = 0;
        for (int i = 0; i < 4; i++) {
            key = key * 3 + shape[i];
        }
        return key;
    }
}
