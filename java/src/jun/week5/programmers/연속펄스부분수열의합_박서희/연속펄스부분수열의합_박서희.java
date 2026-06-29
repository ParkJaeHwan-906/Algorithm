package jun.week5.programmers.연속펄스부분수열의합_박서희;

/*
  문제풀이 시간: 00:18:39
  AI 사용 여부: x
 */
public class 연속펄스부분수열의합_박서희 {
    public static void main(String[] args) {
        int[] sequence = {2, 3, -6, 1, 3, -1, 2, 4};

        Solution solution = new Solution();
        long answer = solution.solution(sequence);
        System.out.println(answer);
    }
}

class Solution {
    static int[] arr1;
    static int[] arr2;
  
    public long solution(int[] sequence) {
        int n = sequence.length;
        long answer = 0;
        
        long[][] dp = new long[n][2];
        dp[0][0] = sequence[0];
        dp[0][1] = sequence[0] * -1;
        answer = Math.max(dp[0][0], dp[0][1]);
        for (int i = 1; i < n; i++) {
            if (i % 2 == 1) {
                dp[i][0] = Math.max(dp[i-1][0] + sequence[i] * -1, sequence[i] * -1);
                dp[i][1] = Math.max(dp[i-1][1] + sequence[i], sequence[i]);
            } else {
                dp[i][0] = Math.max(dp[i-1][0] + sequence[i], sequence[i]);
                dp[i][1] = Math.max(dp[i-1][1] + sequence[i] * -1, sequence[i] * -1);
            }
            answer = Math.max(answer, Math.max(dp[i][0], dp[i][1]));
        }
        return answer;
    }
}
