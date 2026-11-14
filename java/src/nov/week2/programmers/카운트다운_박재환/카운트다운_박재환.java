package nov.week2.programmers.카운트다운_박재환;

import java.util.*;

public class 카운트다운_박재환 {
    public static void main(String[] args) {
        Solution solution = new Solution();
        System.out.println(Arrays.toString(solution.solution(21)));
        System.out.println(Arrays.toString(solution.solution(58)));
    }
}


class Solution {

    static final int INF = Integer.MAX_VALUE / 2;

    public int[] solution(int target) {
        int[][] dp = new int[target + 1][4];
        for(int i = 1; i <= target; i++)
            Arrays.fill(dp[i], INF);
        List<int[]> scores = getScores();
        for(int[] score : scores) {
            for(int i = score[0]; i <= target; i++) {
                if(dp[i - score[0]][0] == INF)
                    continue;

                int[] cand = dp[i - score[0]].clone();
                cand[score[1]]++;

                dp[i] = compare(dp[i], cand);
            }
        }

        int total = 0;
        for (int count : dp[target])
            total += count;

        return new int[] {
                total,
                dp[target][0] + dp[target][3]
        };
    }

    List<int[]> getScores() {
        List<int[]> scores = new ArrayList<>();
        for (int i = 1; i <= 20; i++) {
            scores.add(new int[]{i, 0});
            scores.add(new int[]{i * 2, 1});
            scores.add(new int[]{i * 3, 2});
        }
        scores.add(new int[]{50, 3});
        return scores;
    }

    int[] compare(int[] origin, int[] cand) {
        if (origin[0] == INF)
            return cand;

        int originCount = 0;
        int candCount = 0;
        for(int i = 0; i < 4; i++) {
            originCount += origin[i];
            candCount += cand[i];
        }

        if(originCount > candCount)
            return cand;

        return originCount == candCount
                ? (origin[0] + origin[3]) < (cand[0] + cand[3])
                ? cand : origin
                : origin;
    }
}
