package nov.week3.programmers.순위_박재환;

import java.util.*;

public class 순위_박재환 {
    public static void main(String[] args) {
        int n = 5;
        int[][] results = {{4, 3}, {4, 2}, {3, 2}, {1, 2}, {2, 5}};

        Solution solution = new Solution();
        System.out.println(solution.solution(n, results));
    }
}

class Solution {

    List<Integer>[] connections;
    List<Integer>[] reverseConnections;
    public int solution(int n, int[][] results) {
        set(n, results);

        int answer = 0;
        for(int i = 1; i <= n; i++) {
            int win = win(i, new boolean[n + 1]) - 1;
            int lose = lose(i, new boolean[n + 1]) - 1;
            if(win + lose == n - 1)
                answer++;
        }
        return answer;
    }

    int win(int cur, boolean[] checked) {
        int count = 1;
        checked[cur] = true;
        for(int next : connections[cur]) {
            if(checked[next])
                continue;
            count += win(next, checked);
        }
        return count;
    }

    int lose(int cur, boolean[] checked) {
        int count = 1;
        checked[cur] = true;
        for(int next : reverseConnections[cur]) {
            if(checked[next])
                continue;
            count += lose(next, checked);
        }
        return count;
    }

    void set(int n, int[][] results) {
        connections = new List[n + 1];
        reverseConnections = new List[n + 1];
        for(int i = 1; i <= n; i++) {
            connections[i] = new ArrayList<>();
            reverseConnections[i] = new ArrayList<>();
        }
        for(int[] result : results) {
            int win = result[0];
            int lose = result[1];
            connections[win].add(lose);
            reverseConnections[lose].add(win);
        }
    }
}
