import java.io.*;
import java.util.*;

public class 술래잡기체스_박서희 {

    static int[] dx = {-1, -1, 0, 1, 1, 1, 0, -1};
    static int[] dy = {0, -1, -1, -1, 0, 1, 1, 1};

    // 보드에는 도둑말의 숫자만 저장함.
    static int[][] board = new int[4][4];
    // 게임 말 저장 0번 술래말 1~16번 도둑말
    static Piece[] pieces = new Piece[17];

    static int answer = 0;

    public static void main(String[] args) throws IOException {
        // 보드판 입력 받고 술래말이 0,0 도둑말 잡기
        int firstScore = init();
        // 도둑말 전체 이동, 술래말이 도둑말 잡기(반복)
        dfs(board, pieces, firstScore);

        System.out.println(answer);
    }

    static int init() throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

        for (int i = 0; i < 4; i++) {
            StringTokenizer st = new StringTokenizer(br.readLine());
            for (int j = 0; j < 4; j++) {
                int n = Integer.parseInt(st.nextToken());
                int dir = Integer.parseInt(st.nextToken()) - 1;
                board[i][j] = n;
                pieces[n] = new Piece(n, i, j, dir);
            }
        }
        pieces[0] = new Piece(0, 0, 0, pieces[board[0][0]].dir);

        int caughtThief = board[0][0];
        pieces[caughtThief].n = -1;
        board[0][0] = 0;

        return caughtThief;
    }

    static void dfs(int[][] curBoard, Piece[] curPieces, int score) {
        answer = Math.max(answer, score);

        int[][] nextBoard = copyBoard(curBoard);
        Piece[] nextPieces = copyPieces(curPieces);

        moveThieves(nextBoard, nextPieces);

        int itX = nextPieces[0].x;
        int itY = nextPieces[0].y;
        int itDir = nextPieces[0].dir;

        for (int i = 1; i <= 3; i++) {
            int nX = itX + i * dx[itDir];
            int nY = itY + i * dy[itDir];

            if (inRange(nX, nY) && nextBoard[nX][nY] > 0) {
                int targetThief = nextBoard[nX][nY];

                int[][] tempBoard = copyBoard(nextBoard);
                Piece[] tempPieces = copyPieces(nextPieces);

                tempBoard[itX][itY] = -1;
                tempBoard[nX][nY] = 0;
                tempPieces[targetThief].n = -1;

                tempPieces[0].x = nX;
                tempPieces[0].y = nY;
                tempPieces[0].dir = tempPieces[targetThief].dir;

                dfs(tempBoard, tempPieces, score + targetThief);
            }
        }

    }

    static void moveThieves(int[][] curBoard, Piece[] curPieces) {
        // 도둑말은 1번 부터
        for (int i = 1; i < curPieces.length; i++) {
            // 잡힌 말이라면 패스
            if (curPieces[i].n == -1) continue;

            int curN = i;
            int curX = curPieces[i].x;
            int curY = curPieces[i].y;
            int curDir = curPieces[i].dir;

            int nDir = curDir;
            int nX = curX;
            int nY = curY;


            for (int d = 0; d < 8; d++) {
                nDir = (curDir + d) % 8;
                nX = curX + dx[nDir];
                nY = curY + dy[nDir];

                if (inRange(nX, nY)) {
                    if (!(nX == curPieces[0].x && nY == curPieces[0].y)) {
                        break;
                    }
                }

            }

            int changeN = curBoard[nX][nY];
            curBoard[curX][curY] = changeN;
            if (changeN > 0) {
                curPieces[changeN].x = curX;
                curPieces[changeN].y = curY;
            }
            curBoard[nX][nY] = curN;
            curPieces[curN].x = nX;
            curPieces[curN].y = nY;
            curPieces[curN].dir = nDir;
        }
    }

    static int[][] copyBoard(int[][] original) {
        int[][] result = new int[4][4];
        for (int i = 0; i < 4; i++) {
            System.arraycopy(original[i], 0, result[i], 0, 4);
        }
        return result;
    }

    static Piece[] copyPieces(Piece[] original) {
        Piece[] result = new Piece[17];
        for (int i = 0; i < 17; i++) {
            if (original[i] != null) {
                result[i] = new Piece(original[i].n, original[i].x, original[i].y, original[i].dir);
            }
        }
        return result;
    }

    static boolean inRange(int x, int y) {
        return 0 <= x && x < 4 && 0 <= y && y < 4;
    }

    // n: 술래말 0, 도둑말 1 ~ 16, 잡힌 말(빈칸) -1
    static class Piece {
        int n;
        int x;
        int y;
        int dir;

        public Piece(int n, int x, int y, int dir) {
            this.n = n;
            this.x = x;
            this.y = y;
            this.dir = dir;
        }
    }
}
