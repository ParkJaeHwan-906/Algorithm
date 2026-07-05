package jun.week5.programmers.도둑질_박재환;

/**
 * AI 사용 여부 O
 * => 처음에는 int[][] dp 로 [집 id][훔침여부] 로 작성
 * => 시간초과 => 1차원 배열로 변경
 */
public class 도둑질_박재환 {
    public static void main(String[] args) {
        int[] money = {1, 2, 3, 1};
        Solution sol = new Solution();
        System.out.println(sol.solution(money));
    }
}

class Solution {
    public int solution(int[] money) {
        int n = money.length;

        int exclude = robFirstExcluded(money, n);
        int include = robFirstIncluded(money, n);

        return Math.max(exclude, include);
    }

    private int robFirstIncluded(int[] money, int n) {
        int[] dp = new int[n];

        dp[0] = money[0];
        dp[1] = Math.max(money[0], money[1]);

        for (int i = 2; i < n - 1; i++) { // 마지막 집 제외
            dp[i] = Math.max(dp[i - 1], dp[i - 2] + money[i]);
        }

        return dp[n - 2];
    }

    private int robFirstExcluded(int[] money, int n) {
        int[] dp = new int[n];

        dp[0] = 0;
        dp[1] = money[1];

        for (int i = 2; i < n; i++) {
            dp[i] = Math.max(dp[i - 1], dp[i - 2] + money[i]);
        }

        return dp[n - 1];
    }
}
