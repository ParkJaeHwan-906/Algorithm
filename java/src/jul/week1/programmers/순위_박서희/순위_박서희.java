package jul.week1.programmers.순위_박서희;

/*
  문제풀이 시간: 30분+
  AI 사용 여부: O 트리를 직접 만들어야 하는 고민을 했는데 순위를 어떻게 정해야 하는지 모르겠어서 AI 사용했고, 플로이드 워셜을 써야 한다는 것을 확인.
 */
public class 순위_박서희 {
    public static void main(String[] args) {
        int n = 5;
        int[][] results = {{4, 3}, {4, 2}, {3, 2}, {1, 2}, {2, 5}};

        Solution solution = new Solution();
        int answer = solution.solution(n, results);
        System.out.println(answer);
    }
}

class Solution {
    public int solution(int n, int[][] results) {
        boolean[][] graph = new boolean[n + 1][n + 1];

        for (int[] r: results) {
            graph[r[0]][r[1]] = true;
        }

        for (int k = 1; k <= n; k++) {
            for (int i = 1; i <= n; i++) {
                for (int j = 1; j <= n; j++) {
                    if (graph[i][k] && graph[k][j]) graph[i][j] = true;
                }
            }
        }

        int answer = 0;
        for (int i = 1; i <= n; i++) {
            int count = 0;
            for (int j = 1; j <= n; j++) {
                if (i == j) continue;
                if (graph[i][j] || graph[j][i]) count++;
            }
            if (count == n - 1) answer++;
        }

        return answer;
    }
}
