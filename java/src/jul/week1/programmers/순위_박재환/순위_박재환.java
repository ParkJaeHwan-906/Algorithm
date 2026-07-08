package jul.week1.programmers.순위_박재환;

import java.util.*;

/**
 * AI 사용 여부 O
 * => 처음에는 위상정렬로 접근
 * => DFS 로 완탐
 */
public class 순위_박재환 {
    public static void main(String[] args) {
        int n = 5;
        int[][] results = {
                {4, 2},
                {4, 3},
                {3, 2},
                {1, 2},
                {2, 5}
        };

        Solution solution = new Solution();
        System.out.println(solution.solution(n, results));
    }
}

class Solution {
    int n;
    List<Integer>[] wins;
    List<Integer>[] loses;

    public int solution(int n, int[][] results) {
        set(n, results);

        int result = 0;
        for(int i = 1; i <= n; i++) {
            int knownMatches = countReachable(i, wins, new boolean[n + 1])
                    + countReachable(i, loses, new boolean[n + 1]);
            if(knownMatches == n - 1) result++;
        }
        return result;
    }

    void set(int n, int[][] results) {
        this.n = n;
        this.wins = new List[n + 1];
        this.loses = new List[n + 1];
        for(int i = 0; i <= n; i++) {
            wins[i] = new ArrayList<>();
            loses[i] = new ArrayList<>();
        }
        for(int[] result : results) {
            int win = result[0];
            int lose = result[1];
            wins[win].add(lose);
            loses[lose].add(win);
        }
    }

    int countReachable(int id, List<Integer>[] graph, boolean[] checked) {
        int count = 0;
        for(int other : graph[id]) {
            if(checked[other]) continue;
            checked[other] = true;
            count += 1 + countReachable(other, graph, checked);
        }
        return count;
    }
}
