package oct.week3.programmers.코딩테스트공부_박재환;

import java.util.*;

public class 코딩테스트공부_박재환 {
    public static void main(String[] args) {
        Solution solution = new Solution();

        int result1 = solution.solution(
                10, 10,
                new int[][]{{10, 15, 2, 1, 2}, {20, 20, 3, 3, 4}}
        );
        System.out.println("예제 1: " + result1 + " (기대값: 15)");

        int result2 = solution.solution(
                0, 0,
                new int[][]{
                        {0, 0, 2, 1, 2},
                        {4, 5, 3, 1, 2},
                        {4, 11, 4, 0, 2},
                        {10, 4, 0, 4, 2}
                }
        );
        System.out.println("예제 2: " + result2 + " (기대값: 13)");
    }
}

class Solution {

    static final int INF = Integer.MAX_VALUE / 2;

    class Problem {
        int alpReq;
        int copReq;
        int alpRes;
        int copRes;
        int cost;
        Problem(int alpReq, int copReq, int alpRes, int copRes, int cost) {
            this.alpReq = alpReq;
            this.copReq = copReq;
            this.alpRes = alpRes;
            this.copRes = copRes;
            this.cost = cost;
        }
    }

    int alp, cop;
    int maxAlp, maxCop;
    Problem[] problems;
    public int solution(int alp, int cop, int[][] problems) {
        set(alp, cop, problems);
        if(alp >= maxAlp && cop >= maxCop)
            return 0;
        alp = Math.min(alp, maxAlp);
        cop = Math.min(cop, maxCop);
        int[][] needs = new int[maxAlp + 1][maxCop + 1];
        for(int i = 0; i <= maxAlp; i++)
            Arrays.fill(needs[i], INF);
        needs[alp][cop] = 0;                // 현재 내 상태
        for(int a = alp; a <= maxAlp; a++) {
            for(int c = cop; c <= maxCop; c++) {
                if(a + 1 < maxAlp + 1)
                    needs[a + 1][c] = Math.min(needs[a + 1][c], needs[a][c] + 1);
                if(c + 1 < maxCop + 1)
                    needs[a][c + 1] = Math.min(needs[a][c + 1], needs[a][c] + 1);
                for(Problem problem : this.problems) {
                    if(problem.alpReq > a || problem.copReq > c)
                        continue;
                    int nextAlp = Math.min(maxAlp, a + problem.alpRes);
                    int nextCop = Math.min(maxCop, c + problem.copRes);
                    needs[nextAlp][nextCop] = Math.min(
                            needs[nextAlp][nextCop],
                            needs[a][c] + problem.cost
                    );
                }
            }
        }
        return needs[maxAlp][maxCop];
    }

    void set(int alp, int cop, int[][] problems) {
        this.alp = alp;
        this.cop = cop;
        this.maxAlp = 0;
        this.maxCop = 0;
        this.problems = new Problem[problems.length];
        for(int i = 0; i < problems.length; i++) {
            this.problems[i] = new Problem(
                    problems[i][0],
                    problems[i][1],
                    problems[i][2],
                    problems[i][3],
                    problems[i][4]
            );
            this.maxAlp = Math.max(this.maxAlp, this.problems[i].alpReq);
            this.maxCop = Math.max(this.maxCop, this.problems[i].copReq);
        }
    }
}
