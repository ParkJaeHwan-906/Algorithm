package may.week4.programmers.사라지는발판_박서희;

/*
  문제풀이 시간: 0:59:41
  AI 사용 여부: O
  생각의 흐름: 보드의 크기가 5X5이하여서 dfs로 완탐을 돌리면 되겠다고 생각을 했다.
            게임이 끝나는 지점의 결과를 리턴하면서 바텀업 방식으로 풀면 될 것이라 생각했는데 식 세우기가 어려워서 AI에게 물어봤다.
 */
public class 사라지는발판_박서희 {
    public static void main(String[] args) {
        int[][] board = {{1, 1, 1}, {1, 1, 1}, {1, 1, 1}};
        int[] aloc = {1, 0};
        int[] bloc = {1, 2};

        Solution solution = new Solution();
        int result = solution.solution(board, aloc, bloc); //5
        System.out.println(result);
    }
}

class Solution {
    static int[][] board;

    int[] dx = {-1, 0, 1, 0};
    int[] dy = {0, -1, 0, 1};

    public int solution(int[][] board, int[] aloc, int[] bloc) {
        this.board = board;
        return dfs(board, aloc[0], aloc[1], bloc[0], bloc[1]);
    }

    // return 현재 플레이어가 최선으로 이동하는 횟수
    int dfs(int[][] board, int myX, int myY, int opX, int opY) {
        if (isFinished(board, myX, myY)) {
            return 0;
        }

        int maxTurn = 0;
        int minTurn = Integer.MAX_VALUE;
        boolean canWin = false;

        for (int i = 0; i < 4; i++) {
            int nextX = myX + dx[i];
            int nextY = myY + dy[i];

            if (!inRange(nextX, nextY)) continue;
            board[myX][myY] = 0;
            int nextResult = dfs(board, opX, opY, nextX, nextY);
            board[myX][myY] = 1;

            // 홀수면 내가 졌고, 짝수면 내가 이김. 예를 들어 nextResult = 2 이면 상대 한번 나 한번으로 내가 최종적으로 움직여서 이긴 것.
            if (nextResult % 2 == 0) {
                canWin = true;
                minTurn = Math.min(minTurn, nextResult + 1);
            } else {
                maxTurn = Math.max(maxTurn, nextResult + 1);
            }
        }
        return canWin ? minTurn : maxTurn;
    }

    boolean isFinished(int[][] board, int myX, int myY) {
        if (board[myX][myY] == 0) return true;
        int cnt = 0;
        for (int i = 0; i < 4; i++) {
            int nextX = myX + dx[i];
            int nextY = myY + dy[i];
            if (!inRange(nextX, nextY) || board[nextX][nextY] == 0) continue;
            cnt++;
        }
        return cnt > 0 ? false : true;
    }

    boolean inRange(int x, int y) {
        return 0 <= x && x < board.length && 0 <= y && y < board[0].length;
    }

}

