package jun.week3.programmers.파괴되지않은견물_박재환;

/**
 * AI 사용 여부 O
 */
public class 파괴되지않은건물_박재환 {
    public static void main(String[] args) {
        int[][] board = {
                {5,5,5,5,5},
                {5,5,5,5,5},
                {5,5,5,5,5},
                {5,5,5,5,5}
        };

        int[][] skill = {
                {1, 0, 0, 3, 4, 4},
                {1, 2, 0, 2, 3, 2},
                {2, 1, 0, 3, 1, 2},
                {1, 0, 1, 3, 3, 1}
        };

        Solution sol = new Solution();
        System.out.println(sol.solution(board, skill));
    }
}

class Solution {
    public int solution(int[][] board, int[][] skill) {
        /**
         * board 의 최대 크기 : 1000 x 1000 : 1,000,000
         * skill : 최대 250,000
         *
         * => board 전체를 변경하는 명령에 계속 들어오는 경우
         *      -> 250,000 x 1,000,000
         */
        int[][] temp = new int[board.length + 1][board[0].length + 1];
        for(int[] s : skill) {
            int type = s[0];
            int sx = s[1];
            int sy= s[2];
            int ex = s[3];
            int ey = s[4];
            int degree = s[5];

            checkAccBoard(temp, sx, sy, ex, ey, degree * (type == 1 ? -1 : 1));
        }

        applyAccBoard(temp);

        int result = 0;
        for(int x = 0; x < board.length; x++) {
            for(int y = 0; y < board[0].length; y++) {
                if(board[x][y] + temp[x][y] > 0) result++;
            }
        }
        return result;
    }

    void checkAccBoard(int[][] temp, int sx, int sy, int ex, int ey, int degree) {
        /**
         *           sy        ey
         *       ┌──────────────────────
         *   sx  │   S ■ ■ ■ │ X X X ...   ← 오른쪽으로 넘침
         *       │   ■ ■ ■ ■ │ X X X
         *   ex  │   ■ ■ ■ ■ │ X X X
         *       ├───────────┘
         *       │   X X X X   X X X ...    ← 아래로도 넘침
         *       │   X X X X   X X X
         */
        temp[sx][sy] += degree;
        temp[sx][ey + 1] -= degree;
        temp[ex + 1][sy] -= degree;
        temp[ex + 1][ey + 1] += degree;     // 중복으로 제거되는 부분에 대해서 처리
    }

    void applyAccBoard(int[][] temp) {
        // 가로 누적 ( > )
        for(int x = 0; x < temp.length - 1; x++) {
            for(int y = 1; y < temp[0].length - 1; y++) {
                temp[x][y] += temp[x][y - 1];
            }
        }
        // 세로 누적 ( v )
        for(int y = 0; y < temp[0].length - 1; y++) {
            for(int x = 1; x < temp.length - 1; x++) {
                temp[x][y] += temp[x - 1][y];
            }
        }
    }
}