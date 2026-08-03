package jun.week5.programmers.도둑질_박서희;

/*
  문제풀이 시간: 00:19:06
  AI 사용 여부: x
 */
public class 도둑질_박서희 {
    public static void main(String[] args) {
        int[] money = {1, 2, 3, 1};

        Solution solution = new Solution();
        int result = solution.solution(money);
        System.out.println(result);
    }
}

class Solution {
    public int solution(int[] money) {
        int n = money.length;
        // 0 열엔 첫번째 값 포함, 1 열에는 첫번째 값 포함 x
        int[][] dp = new int[n][2];
        dp[0][0] = money[0];
        dp[1][0] = dp[0][0];
        dp[1][1] = money[1];
        for (int i = 2; i < n - 1; i++) {
            dp[i][0] = Math.max(dp[i - 2][0] + money[i], dp[i - 1][0]);
            dp[i][1] = Math.max(dp[i - 2][1] + money[i], dp[i - 1][1]);
        }
        dp[n - 1][0] = dp[n - 2][0];
        dp[n - 1][1] = Math.max(dp[n - 3][1] + money[n - 1], dp[n - 2][1]);
        return Math.max(dp[n - 1][0], dp[n - 1][1]);
    }
}
