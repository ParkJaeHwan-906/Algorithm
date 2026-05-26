package may.week4.programmers.사라지는발판_박재환;

import java.util.*;

/**
 * [풀이시간]
 * 00:49:29
 * AI 사용 여부 O
 */
public class 사라지는발판_박재환 {
    public static void main(String[] args) {
        int[][] board = {{1, 1, 1}, {1, 1, 1}, {1, 1, 1}};
        int[] aloc = {1, 0};
        int[] bloc = {1, 2};

        Solution solution = new Solution();
        System.out.println(solution.solution(board, aloc, bloc));
    }
}

class Solution {
    /**
     * A, B 총 두 플레이어가 있음
     *
     * Board
     * - 발판 O - 캐릭터가 서있을 수 있음
     * - 발판 X
     *
     * 발판은 그 위에 있던 캐릭터가 다른 곳으로 이동하여 다른 발판을 밟음과 동시에 사라짐
     * - 상 하 좌 우 이동
     *
     * 승/패 결정
     * - 움직일 차례인데 캐릭터의 상하좌우 주변 4칸이 모두 발판이 없거나, 보드 밖이라 이동할 수 없는 경우 : 패배
     * - 두 캐릭터가 같은 발판 위에 있을 때, 상대 플레이어의 캐릭터가 다른 발판으로 이동하여 자신의 캐릭터가 서있던 발판이 사라지게 되는 경우 : 패배
     *      - 같은 위치에 서 있을 수 있음
     */
    void init(int[][] board) {
        this.board = board;
        this.r = board.length;
        this.c = board[0].length;
    }
    int[][] board;
    int r, c;
    public int solution(int[][] board, int[] aloc, int[] bloc) {
        init(board);
        return Math.abs(game(board, aloc, 0, bloc, 0));
    }

    int[] dx = {0, 1, 0, -1};
    int[] dy = {1, 0, -1, 0};
    int game(int[][] board, int[] aloc, int aMove, int[] bloc, int bMove) {
        boolean turnA = aMove == bMove;
        int[] cur = turnA ? aloc : bloc;

        if(board[cur[0]][cur[1]] == 0) {
            // 현재 있는 칸에 발판이 없는 경우 -> 패배
            return 0;
        }

        boolean canMove = false;
        /**
         * [AI 사용]
         * 어떤 값을 사용해서 재귀적으로 호출해야하고, 그 값을 현재 경우에 어떻게 이용할 수 있는지
         */
        int win = Integer.MAX_VALUE;
        int lose = 0;

        for(int dir = 0; dir < 4; dir++) {
            int nx = cur[0] + dx[dir];
            int ny = cur[1] + dy[dir];

            if(isNotBoard(nx, ny)) continue;
            if(board[nx][ny] == 0) continue;

            canMove = true;
            board[cur[0]][cur[1]] = 0;      // 이동하면 현재 위치 발판은 사라짐

            int next;       // 다음 턴 플레이어의 결과를 가져옴
            if(turnA) next = game(board, new int[] {nx, ny}, aMove + 1, bloc, bMove);
            else next = game(board, aloc, aMove, new int[] {nx, ny}, bMove + 1);

            board[cur[0]][cur[1]] = 1;

            // 다음턴의 플레이어가 진 경우 -> 현재 플레이어 승
            if(next <= 0) win = Math.min(win, -next + 1);
            // 다음턴의 플레이어가 이긴 경우 -> 현재 플레이어 패
            else lose = Math.max(lose, next + 1);
        }

        if(!canMove) return 0;

        if(win != Integer.MAX_VALUE) return win;
        return -lose;
    }

    boolean isNotBoard(int x, int y) {
        return x < 0 || y < 0 || x >= r || y >= c;
    }
}