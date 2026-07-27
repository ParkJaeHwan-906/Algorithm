package jul.week3.programmers.공이동시뮬레이션_박서희;

/*
  문제풀이 시간: 1시간+
  AI 사용 여부: O
  n과 m이 10^9로 엄청 크기 때문에 x, y에서 역추적으로 구하는 방법을 생각해냈다.
  하지만 그 뒤로는 어떻게 해야하는 지 막막해서 AI를 사용함. 헷갈릴 때는 그림을 그려보자.
 */
public class 공이동시뮬레이션_박서희 {
    public static void main(String[] args) {
        Solution solution = new Solution();
        int n = 2, m = 2;
        int x = 0, y = 0;
        int[][] queries = {{2, 1}, {0, 1}, {1, 1}, {0, 1}, {2, 1}};

        long answer = solution.solution(n, m, x, y, queries);
        System.out.println("answer = " + answer);
    }
}

class Solution {

    public long solution(int n, int m, int x, int y, int[][] queries) {
        int r1 = x, r2 = x, c1 = y, c2 = y;

        for (int i = queries.length - 1; i >= 0; i--) {
            int command = queries[i][0];
            int move = queries[i][1];

            if (command == 0) {
                if (c1 != 0) c1 += move;
                c2 = Math.min(c2 + move, m - 1);
            } else if (command == 1) {
                if (c2 != m - 1) c2 -= move;
                c1 = Math.max(c1 - move, 0);
            } else if (command == 2) {
                if (r1 != 0) r1 += move;
                r2 = Math.min(r2 + move, n - 1);
            } else if (command == 3) {
                if (r2 != n - 1) r2 -= move;
                r1 = Math.max(r1 - move, 0);
            }

            if (r2 < r1 || c2 < c1 || r1 < 0 || r2 >= n || c1 < 0 || c2 >= m) {
                return 0;
            }
        }

        long answer = (long) (r2 + 1 - r1) * (long) (c2 + 1 - c1);
        return answer;
    }
}
