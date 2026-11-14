package nov.week2.programmers.거스름돈_박재환;

public class 거스름돈_박재환 {
    public static void main(String[] args) {
        Solution solution = new Solution();
        System.out.println(solution.solution(5, new int[]{1, 2, 5}));
    }
}

class Solution {

    static final int MOD = 1_000_000_007;

    public int solution(int n, int[] money) {
        int[] caseArr = new int[n + 1];
        caseArr[0] = 1;
        for(int m : money) {
            for(int i = m; i <= n; i++) {
                if(caseArr[i] == 0)
                    caseArr[i] = caseArr[i - m];
                else
                    caseArr[i] += (caseArr[i - m] % MOD);
            }
        }
        return caseArr[n] % MOD;
    }
}
