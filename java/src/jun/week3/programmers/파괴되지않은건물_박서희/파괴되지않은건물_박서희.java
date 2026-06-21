package jun.week3.programmers.파괴되지않은건물_박서희;

/*
  문제풀이 시간:
  AI 사용 여부: O 일일이 계산하기에는 시간초과날 것 같고, 그 외 방법을 모르겠어서 사용. 2D 펜윅트리도 아닐거 같고..
              Imos 알고리즘이란 것이 있다고 함. 내가 푼 건 거의 없어서 문제풀이 시간 안 적음.
 */
public class 파괴되지않은건물_박서희 {
    public static void main(String[] args) {
        int[][] board = {
                {5, 5, 5, 5, 5},
                {5, 5, 5, 5, 5},
                {5, 5, 5, 5, 5},
                {5, 5, 5, 5, 5}
        };

        int[][] skill = {
                {1, 0, 0, 3, 4, 4},
                {1, 2, 0, 2, 3, 2},
                {2, 1, 0, 3, 1, 2},
                {1, 0, 1, 3, 3, 1}
        };

        Solution solution = new Solution();
        int result = solution.solution(board, skill);
        System.out.println(result);
    }
}

class Solution {
    public int solution(int[][] board, int[][] skill) {
        int n = board.length;
        int m = board[0].length;

        int[][] temp = new int[n + 1][m + 1];

        for (int[] s: skill) {
            int type = s[0], r1 = s[1], c1 = s[2], r2 = s[3], c2 = s[4];
            int degree = s[5];
            if (type == 1) degree = -degree;

            temp[r1][c1] += degree;
            temp[r1][c2 + 1] -= degree;
            temp[r2 + 1][c1] -= degree;
            temp[r2 + 1][c2 + 1] += degree;
        }

        for (int i = 0; i <= n; i++) {
            for (int j = 1; j <= m; j++) {
                temp[i][j] += temp[i][j - 1];
            }
        }

        for (int j = 0; j <= m; j++) {
            for (int i = 1; i <= n; i++) {
                temp[i][j] += temp[i - 1][j];
            }
        }

        int answer = 0;
        for (int x = 0; x < n; x++) {
            for (int y = 0; y < m; y++) {
                if (board[x][y] + temp[x][y] > 0) {
                    answer++;
                }
            }
        }
        return answer;
    }
}
