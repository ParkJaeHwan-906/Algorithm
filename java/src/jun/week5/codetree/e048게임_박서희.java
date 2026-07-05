package jun.week5.codetree;

import java.io.*;
import java.util.*;

/*
  문제풀이 시간: 00:42:18
  AI 사용 여부: X
 */
public class e048게임_박서희 {
    static int answer = 0;
    static int n;

    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        n = Integer.parseInt(br.readLine());

        int[][] board = new int[n][n];
        for (int i = 0; i < n; i++) {
            StringTokenizer st = new StringTokenizer(br.readLine());
            for (int j = 0; j < n; j++) {
                board[i][j] = Integer.parseInt(st.nextToken());
            }
        }

        dfs(0, board);
        System.out.println(answer);
    }

    public static void dfs(int depth, int[][] board) {
        if (depth == 5) {
            answer = Math.max(answer, checkMax(board));
            return;
        }

        for (int d = 0; d < 4; d++) {
            int[][] nextBoard = new int[n][n];
            for (int i = 0; i < n; i++) {
                nextBoard[i] = Arrays.copyOf(board[i], n);
            }
            nextBoard = moveBoard(d, nextBoard);
            dfs(depth + 1, nextBoard);
        }
    }

    private static int[][] moveBoard(int dir, int[][] board) {
        if (dir == 0) { // 위로 밀기
            for (int j = 0; j < n; j++) {
                Deque<int[]> deque = new ArrayDeque<>();
                for (int i = 0; i < n; i++) {
                    if (board[i][j] == 0) continue;
                    moveBlock(deque, board[i][j]);
                }
                int idx = 0;
                while (!deque.isEmpty()) {
                    board[idx++][j] = deque.pollFirst()[0];
                }
                while (idx < n) {
                    board[idx++][j] = 0;
                }
            }
        } else if (dir == 1) {  // 오른쪽으로 밀기
            for (int i = 0; i < n; i++) {
                Deque<int[]> deque = new ArrayDeque<>();
                for (int j = n - 1; j >= 0; j--) {
                    if (board[i][j] == 0) continue;
                    moveBlock(deque, board[i][j]);
                }
                int jdx = n - 1;
                while (!deque.isEmpty()) {
                    board[i][jdx--] = deque.pollFirst()[0];
                }
                while (jdx >= 0) {
                    board[i][jdx--] = 0;
                }
            }
        } else if (dir == 2) {  // 아래쪽
            for (int j = 0; j < n; j++) {
                Deque<int[]> deque = new ArrayDeque<>();
                for (int i = n - 1; i >= 0; i--) {
                    if (board[i][j] == 0) continue;
                    moveBlock(deque, board[i][j]);
                }
                int idx = n - 1;
                while (!deque.isEmpty()) {
                    board[idx--][j] = deque.pollFirst()[0];
                }
                while (idx >= 0) {
                    board[idx--][j] = 0;
                }
            }
        } else if (dir == 3) {  // 왼쪽
            for (int i = 0; i < n; i++) {
                Deque<int[]> deque = new ArrayDeque<>();
                for (int j = 0; j < n; j++) {
                    if (board[i][j] == 0) continue;
                    moveBlock(deque, board[i][j]);
                }
                int jdx = 0;
                while (!deque.isEmpty()) {
                    board[i][jdx++] = deque.pollFirst()[0];
                }
                while (jdx < n) {
                    board[i][jdx++] = 0;
                }
            }
        }
        return board;
    }

    private static void moveBlock(Deque<int[]> deque, int boardValue) {
        if (deque.isEmpty()) {
            deque.addLast(new int[]{boardValue, 0});
        } else {
            int[] last = deque.peekLast();
            if (last[1] == 0 && last[0] == boardValue) {
                deque.pollLast();
                deque.addLast(new int[]{boardValue * 2, 1});
            } else {
                deque.addLast(new int[]{boardValue, 0});
            }
        }
    }

    private static int checkMax(int[][] board) {
        int result = 0;
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                result = Math.max(result, board[i][j]);
            }
        }
        return result;
    }
}
