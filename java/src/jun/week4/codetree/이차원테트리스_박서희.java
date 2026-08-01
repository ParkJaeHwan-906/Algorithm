package jun.week4.codetree;

import java.io.*;
import java.util.*;


/*
  문제풀이 시간: 1시간 +
  AI 사용 여부: O 로직 꼬여서 AI 도움으로 품.
 */
public class 이차원테트리스_박서희 {
    static int answer = 0;

    static int[][] yellow = new int[6][4];
    static int[][] red = new int[6][4];

    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        int k = Integer.parseInt(br.readLine());

        while (k-- > 0) {
            StringTokenizer st = new StringTokenizer(br.readLine());
            int t = Integer.parseInt(st.nextToken());
            int r = Integer.parseInt(st.nextToken());
            int c = Integer.parseInt(st.nextToken());

            simulate(t, r, c);
        }
        System.out.println(answer);
        int cnt = countBoard(yellow) + countBoard(red);
        System.out.println(cnt);
    }

    private static int countBoard(int[][] board) {
        int cnt = 0;
        for (int i = 0; i < 6; i++) {
            for (int j = 0; j < 4; j++) {
                if (board[i][j] == 1) cnt++;
            }
        }
        return cnt;
    }


    public static void simulate(int t, int r, int c) {
        dropBlock(yellow, t, c);
        checkAndClear(yellow);
        checkLight(yellow);

        int nt = t;
        int nc = 3 - r;

        if (t == 2) {
            nt = 3;
            nc = 3 - r;
        } else if (t == 3) {
            nt = 2;
            nc = 3 - (r + 1);
        }

        dropBlock(red, nt, nc);
        checkAndClear(red);
        checkLight(red);
    }

    private static void dropBlock(int[][] board, int t, int c) {
        int row = 0;

        if (t == 1) {
            while (row + 1 < 6 && board[row + 1][c] == 0) {
                row++;
            }
            board[row][c] = 1;
        } else if (t == 2) {
            while (row + 1 < 6 && board[row + 1][c] == 0 && board[row + 1][c + 1] == 0) {
                row++;
            }
            board[row][c] = 1;
            board[row][c + 1] = 1;
        } else if (t == 3) {
            while (row + 2 < 6 && board[row + 2][c] == 0) {
                row++;
            }
            board[row][c] = 1;
            board[row + 1][c] = 1;
        }
    }

    private static void checkAndClear(int[][] board) {
        for (int i = 5; i >= 2; i--) {
            boolean isFull = true;
            for (int j = 0; j < 4; j++) {
                if (board[i][j] == 0) {
                    isFull = false;
                    break;
                }
            }

            if (isFull) {
                answer++;
                shiftDown(board, i);
                i++;
            }
        }
    }

    private static void shiftDown(int[][] board, int targetRow) {
        for (int r = targetRow; r > 0; r--) {
            for (int c = 0; c < 4; c++) {
                board[r][c] = board[r - 1][c];
            }
        }

        for (int c = 0; c < 4; c++) {
            board[0][c] = 0;
        }
    }

    private static void checkLight(int[][] board) {
        int linesToRemove = 0;

        boolean hasBlockInZero = false;
        for (int j = 0; j < 4; j++) {
            if (board[0][j] == 1) {
                hasBlockInZero = true;
                break;
            }
        }

        boolean hasBlockInOne = false;
        for (int j = 0; j < 4; j++) {
            if (board[1][j] == 1) {
                hasBlockInOne = true;
                break;
            }
        }

        if (hasBlockInZero) {
            linesToRemove = 2;
        } else if (hasBlockInOne) {
            linesToRemove = 1;
        }

        for (int k = 0; k < linesToRemove; k++) {
            shiftDown(board, 5);
        }
    }
}
