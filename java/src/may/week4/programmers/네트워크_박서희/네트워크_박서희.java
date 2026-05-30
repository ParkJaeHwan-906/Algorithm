package may.week4.programmers.네트워크_박서희;

/*
  문제풀이 시간: 정확히 안 쟀는데 한 10~15분
  AI 사용 여부: X
 */
public class 네트워크_박서희 {
    public static void main(String[] args) {
        int n = 3;
        int[][] computers = {{1, 1, 0}, {1, 1, 0}, {0, 0, 1}};

        Solution solution = new Solution();
        int result = solution.solution(n, computers);
        System.out.println(result);
    }

}

class Solution {
    static int[] visited;
    static int[][] connect;
    static int N;

    public int solution(int n, int[][] computers) {
        int answer = 0;

        visited = new int[n];
        connect = computers;
        N = n;

        for (int i = 0; i < n; i++) {
            if (visited[i] == 0) {
                dfs(i);
                answer++;
            }
        }
        return answer;
    }

    public void dfs(int cur) {
        visited[cur] = 1;
        for (int i = 0; i < N; i++) {
            if (visited[i] == 0 && connect[cur][i] == 1) {
                dfs(i);
            }
        }
    }
}
