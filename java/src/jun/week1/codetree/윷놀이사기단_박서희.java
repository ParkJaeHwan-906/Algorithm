package jun.week1.codetree;

import java.util.*;

/*
  문제풀이 시간: ?
  AI 사용 여부: O 보드판을 옮기는 과정에서 사용.
  생각의 흐름: 보드판만 잘 옮겼으면 무난한 문제.
 */
public class 윷놀이사기단_박서희 {

    static Node[] board = new Node[33];
    static int[] horses = new int[4];
    static int[] dice = new int[10];

    static int answer = 0;

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        for (int i = 0; i < 10; i++) {
            dice[i] = sc.nextInt();
        }

        // board 판 옮기기
        init();
        // 백트래킹
        dfs(0, 0);

        System.out.println(answer);
    }

    static void dfs(int curDice, int sum) {
        // 기저 조건
        if (curDice == 10) {
            answer = Math.max(answer, sum);
            return;
        }

        for (int i = 0; i < 4; i++) {
            int originPos = horses[i];
            if (originPos == 21) continue;

            int move = dice[curDice];
            int nextPos = originPos;

            if (board[nextPos].shortcut != -1) {
                nextPos = board[nextPos].shortcut;
                move--;
            }

            while (move > 0) {
                nextPos = board[nextPos].next;
                move--;
                if (nextPos == 21) break;
            }


            boolean canMove = true;
            for (int j = 0; j < 4; j++) {
                if (i != j && nextPos != 21) {
                    if (horses[j] == nextPos) canMove = false;
                }
            }

            if (!canMove) continue;

            horses[i] = nextPos;
            dfs(curDice + 1, sum + board[nextPos].score);
            horses[i] = originPos;
        }
    }

    static void init() {
        for (int i = 0; i < 21; i++) {
            if (i == 5) board[i] = new Node(10, 6, 22);
            else if (i == 10) board[i] = new Node(20, 11, 25);
            else if (i == 15) board[i] = new Node(30, 16, 27);
            else board[i] = new Node(i * 2, i + 1);
        }

        // 도착 : 21
        board[21] = new Node(0, -1);

        board[22] = new Node(13, 23);
        board[23] = new Node(16, 24);
        board[24] = new Node(19, 30);

        board[25] = new Node(22, 26);
        board[26] = new Node(24, 30);

        board[27] = new Node(28, 28);
        board[28] = new Node(27, 29);
        board[29] = new Node(26, 30);

        board[30] = new Node(25, 31);
        board[31] = new Node(30, 32);
        board[32] = new Node(35, 20);
    }

    static class Node {
        int score, next, shortcut;

        public Node(int score, int next) {
            this.score = score;
            this.next = next;
            this.shortcut = -1;
        }

        public Node(int score, int next, int shortcut) {
            this.score = score;
            this.next = next;
            this.shortcut = shortcut;
        }
    }
}