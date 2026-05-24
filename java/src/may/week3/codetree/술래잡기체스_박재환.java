package may.week3.codetree;

import java.util.*;
import java.io.*;

public class 술래잡기체스_박재환 {
    static BufferedReader br;
    public static void main(String[] args) throws IOException {
        br = new BufferedReader(new InputStreamReader(System.in));
        init();
        br.close();
    }

    static class Horse {
        int x, y;
        int dir;

        Horse(int x, int y, int dir) {
            this.x = x;
            this.y = y;
            this.dir = dir;
        }

        void turn() {
            dir = (dir + 1) % 8;
        }
    }

    static class Thief extends Horse {
        int id;
        boolean caught;

        Thief(int id, int x, int y, int dir) {
            super(x, y, dir);
            this.id = id;
            this.caught = false;
        }

        Thief(Thief other) {
            // 복사용
            super(other.x, other.y, other.dir);
            this.id = other.id;
            this.caught = other.caught;
        }
    }

    static class Tagger extends Horse {
        Tagger(int x, int y, int dir) {
            super(x, y, dir);
        }

        Tagger(Tagger other) {
            // 복사용
            super(other.x, other.y, other.dir);
        }
    }

    static final int[] dx = {-1, -1, 0, 1, 1, 1, 0, -1};
    static final int[] dy = {0, -1, -1, -1, 0, 1, 1, 1};

    static StringTokenizer st;
    static int[][] board;
    static Thief[] thieves;
    static void init() throws IOException {
        board = new int[4][4];
        thieves = new Thief[17];

        for(int x = 0; x < 4; x++) {
            st = new StringTokenizer(br.readLine().trim());
            for(int y = 0; y < 4; y++) {
                int id = Integer.parseInt(st.nextToken());
                int dir = Integer.parseInt(st.nextToken()) - 1;

                Thief thief = new Thief(id, x, y, dir);
                board[x][y] = id;
                thieves[id] = thief;
            }
        }

        solution();
    }

    /**
     * 술래 말 하나만 사용해 도둑을 잡는다.
     *  - 도둑을 잡을 때마다 잡은 도둑말의 방향을 갖게 된다. (상하좌우 대각선, 총 8방향)
     *
     *  1. 초기에는 (0, 0)에 있는 도둑을 잡으며 시작합니다.
     *  2. 도둑은 번호작 작은 순서대로, 본인이 가지고 있는 이동방향대로 이동합니다.
     *      한 칸씩 이동하며, 빈 칸 혹은 다른 도둑이 있는 칸으로 이동할 수 있습니다. (겪자 밖, 술래 위치X)
     *      이동 불가할 때, 이동 가능할 때까지 45도 반시계 회전하며 이동가능한 부분을 탐색합니다.
     *      이동할 수 없다면 이동하지 않습니다.
     *      해당 칸에 다른 도둑이 있다면 해당 말과 위치를 바꾼다.
     * 3. 도둑 이동이 모두 끝나면 술래가 이동한다. 이동 가능한 방향의 어느 칸이나 이동할 수 있다.
     *      한 번에 여러 칸도 이동할 수 있다. 지나치는 도둑은 잡지 않는다.
     */
    static int maxScore;
    static void solution() {
        int firstId = board[0][0];
        Thief first = thieves[firstId];

        Tagger tagger = new Tagger(0, 0, first.dir);
        int score = firstId;

        first.caught = true;
        board[0][0] = 0;

        findAllCombi(board, thieves, tagger, score);

        System.out.println(maxScore);
    }

    static void findAllCombi(int[][] board, Thief[] thieves, Tagger tagger, int score) {
        maxScore = Math.max(maxScore, score);

        moveThief(board, thieves, tagger);

        for(int dist = 1; dist < 4; dist++) {
            int nx = tagger.x + dx[tagger.dir] * dist;
            int ny = tagger.y + dy[tagger.dir] * dist;

            if (isNotBoard(nx, ny)) break;      // 격자를 벗어나는 경우
            if (board[nx][ny] == 0) continue;   // 빈 칸인 경우 -> 점수 획득 X

            int[][] nextBoard = copyBoard(board);
            Thief[] nextThieves = copyThieves(thieves);

            int targetId = nextBoard[nx][ny];
            Thief target = nextThieves[targetId];

            nextBoard[nx][ny] = 0;
            target.caught = true;

            Tagger nextTagger = new Tagger(nx, ny, target.dir);

            findAllCombi(nextBoard, nextThieves, nextTagger, score + targetId);
        }
    }

    static void moveThief(int[][] board, Thief[] thieves, Tagger tagger) {
        for (int id = 1; id < 17; id++) {
            Thief thief = thieves[id];
            if (thief.caught) continue;

            // 낮은 id로 부터 순차적이로 이동
            boolean move = false;
            for (int turn = 0; turn < 8; turn++) {
                if (canMove(thief, tagger)) {
                    move = true;
                    break;
                }
                thief.turn();
            }

            if (!move) continue;

            // 이동할 새로운 위치
            int nx = thief.x + dx[thief.dir];
            int ny = thief.y + dy[thief.dir];

            int targetId = board[nx][ny];

            // 다른 도둑이 있는 경우
            if (targetId > 0) {
                // 둘의 위치 교환
                Thief other = thieves[targetId];
                thieves[targetId].x = thief.x;
                thieves[targetId].y = thief.y;
            }

            board[thief.x][thief.y] = targetId;
            board[nx][ny] = thief.id;
            thief.x = nx;
            thief.y = ny;
        }
    }

    static boolean canMove(Thief thief, Tagger tagger) {
        int nx = thief.x + dx[thief.dir];
        int ny = thief.y + dy[thief.dir];

        if(isNotBoard(nx, ny)) return false;                    // 격자를 벗어나는 경우
        if(nx == tagger.x && ny == tagger.y) return false;      // 술래의 위치인 경우

        return true;
    }

    static int[][] copyBoard(int[][] board) {
        int[][] copied = new int[4][4];
        for (int i = 0; i < 4; i++) {
            copied[i] = board[i].clone();
        }
        return copied;
    }

    static Thief[] copyThieves(Thief[] thieves) {
        Thief[] copied = new Thief[17];
        for (int i = 1; i <= 16; i++) {
            copied[i] = new Thief(thieves[i]);
        }
        return copied;
    }

    static boolean isNotBoard(int x, int y) {
        return x < 0 || y < 0 || x >= 4 || y >= 4;
    }
}
