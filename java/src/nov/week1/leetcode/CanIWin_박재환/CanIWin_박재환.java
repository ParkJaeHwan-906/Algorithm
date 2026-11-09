package nov.week1.leetcode.CanIWin_박재환;

import java.util.Arrays;

public class CanIWin_박재환 {
    public static void main(String[] args) {
        Solution solution = new Solution();

        System.out.println(solution.canIWin(10, 11)); // false
        System.out.println(solution.canIWin(10, 0));  // true
        System.out.println(solution.canIWin(10, 1));  // true
    }
}

class Solution {

    int maxChoosableInteger;
    int desiredTotal;
    int[] dp;
    public boolean canIWin(int maxChoosableInteger, int desiredTotal) {
        set(maxChoosableInteger, desiredTotal);
        if (desiredTotal <= 0)
            return true;

        if (maxChoosableInteger * (maxChoosableInteger + 1) / 2 < desiredTotal)
            return false;
        return numberOfCases(0, 0);
    }

    void set(int maxChoosableInteger, int desiredTotal) {
        this.maxChoosableInteger = maxChoosableInteger;
        this.desiredTotal = desiredTotal;
        this.dp = new int[1 << (maxChoosableInteger + 1)];
        Arrays.fill(dp, -1);
    }

    boolean numberOfCases(int mask, int total) {
        if(dp[mask] != -1)
            return dp[mask] == 1;
        for(int i = 1; i <= maxChoosableInteger; i++) {
            if((mask & (1 << i)) != 0)
                continue;
            if(total + i >= desiredTotal) {
                dp[mask] = 1;
                return true;
            }

            if(!numberOfCases(mask | (1 << i), total+i)) {
                dp[mask] = 1;
                return true;
            }
        }
        dp[mask] = 0;
        return false;
    }
}
