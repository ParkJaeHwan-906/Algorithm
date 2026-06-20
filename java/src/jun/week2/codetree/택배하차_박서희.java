package jun.week2.codetree;

import java.io.*;
import java.util.*;

/*
  문제풀이 시간: 02:04:43
  AI 사용 여부: O 디버깅할 때 사용, 택배를 하차하고 내려갈 택배를 내려야 하는데 idx 순서대로 돌리는 것이 아닌 택배 행이 큰 것부터 내려야 함.
 */
public class 택배하차_박서희 {

    static int N, M;
    static int[][] board;
    static Box[] boxes;

    static int count = 0;
    static StringBuilder sb = new StringBuilder();

    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer st = new StringTokenizer(br.readLine());

        N = Integer.parseInt(st.nextToken());
        M = Integer.parseInt(st.nextToken());

        board = new int[N][N];
        boxes = new Box[M];
        for (int i = 0; i < M; i++) {
            st = new StringTokenizer(br.readLine());
            inputBox(st, i);
        }
        Arrays.sort(boxes, Comparator.comparingInt(a -> a.k));

        while (count < M) {
            out(true);
            out(false);
        }
        System.out.println(sb);
    }

    static void out(boolean isLeft) {
        int k = -1;
        for (int i = 0; i < M; i++) {
            if (boxes[i].isOut) continue;
            if (isLeft && canLeftOut(i)) {
                k = i;
                break;
            }
            if (!isLeft && canRightOut(i)) {
                k = i;
                break;
            }
        }
        if (k != -1) {
            count++;
            sb.append(boxes[k].k).append("\n");
            boxes[k].isOut = true;
            clearBoard(k);
            Arrays.sort(boxes, (a, b) -> {
                if (a.r != b.r) return Integer.compare(b.r, a.r);
                return Integer.compare(a.c, b.c);
            });
            for (int i = 0; i < M; i++) {
                if (boxes[i].isOut) continue;
                downBox(i);
            }
            Arrays.sort(boxes, Comparator.comparingInt(a -> a.k));
        }
    }

    static private boolean canLeftOut(int idx) {
        int r = boxes[idx].r, c = boxes[idx].c, h = boxes[idx].h, w = boxes[idx].w;
        for (int i = r; i < r + h; i++) {
            for (int j = 0; j < c; j++) {
                if (board[i][j] != 0) return false;
            }
        }
        return true;
    }

    static private boolean canRightOut(int idx) {
        int r = boxes[idx].r, c = boxes[idx].c, h = boxes[idx].h, w = boxes[idx].w;
        for (int i = r; i < r + h; i++) {
            for (int j = c + w; j < N; j++) {
                if (board[i][j] != 0) return false;
            }
        }
        return true;
    }

    static void inputBox(StringTokenizer st, int idx) {
        int k = Integer.parseInt(st.nextToken());
        int h = Integer.parseInt(st.nextToken());
        int w = Integer.parseInt(st.nextToken());
        int c = Integer.parseInt(st.nextToken()) - 1;
        boxes[idx] = new Box(k, h, w, c);
        fillBoard(idx);
        downBox(idx);
    }

    private static void downBox(int idx) {
        int r = boxes[idx].r, c = boxes[idx].c, h = boxes[idx].h, w = boxes[idx].w;

        int boxBottom = r + h - 1;
        int moveCnt = 0;

        while (boxBottom + moveCnt + 1 < N) {
            int nextRow = boxBottom + moveCnt + 1;
            boolean canDown = true;

            for (int i = c; i < c + w; i++) {
                if (board[nextRow][i] != 0) {
                    canDown = false;
                    break;
                }
            }

            if (!canDown)
                break;
            moveCnt++;
        }

        if (moveCnt > 0) {
            clearBoard(idx);
            boxes[idx].r += moveCnt;
            fillBoard(idx);
        }
    }

    private static void fillBoard(int idx) {
        int r = boxes[idx].r, c = boxes[idx].c, h = boxes[idx].h, w = boxes[idx].w;

        for (int i = r; i < r + h; i++) {
            for (int j = c; j < c + w; j++) {
                board[i][j] = boxes[idx].k;
            }
        }
    }

    private static void clearBoard(int idx) {
        int r = boxes[idx].r, c = boxes[idx].c, h = boxes[idx].h, w = boxes[idx].w;

        for (int i = r; i < r + h; i++) {
            for (int j = c; j < c + w; j++) {
                board[i][j] = 0;
            }
        }
    }

    static class Box {
        int k;
        int h, w;
        int r, c;
        boolean isOut;

        public Box(int k, int h, int w, int c) {
            this.k = k;
            this.h = h;
            this.w = w;
            this.r = 0;
            this.c = c;
            this.isOut = false;
        }
    }
}
