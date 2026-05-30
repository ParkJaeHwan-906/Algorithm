package may.week4.programmers.네트워크_박재환;

import java.util.*;

/**
 * [풀이 시간]
 * 00:16:34
 * AI 사용 여부 X
 */
public class 네트워크_박재환 {
    public static void main(String[] args) {
        int n = 3;
        int[][] computers = {{1, 1, 0}, {1, 1, 0}, {0, 0, 1}};

        Solution solution = new Solution();
        System.out.println(solution.solution(n, computers));
    }
}

class Solution {
    void init(int n, int[][] computers) {
        this.n = n;
        this.computers = computers;
    }

    int n;
    int[][] computers;
    int[] parents;
    public int solution(int n, int[][] computers) {
        init(n, computers);
        make();
        for(int i = 0; i < n; i++) {
            for(int j = 0; j < n; j++) {
                if(computers[i][j] == 1) union(i, j);
            }
        }

        Set<Integer> set = new HashSet<>();
        for(int i = 0; i < n; i++) {
            set.add(find(i));
        }

        return set.size();
    }

    void make() {
        parents = new int[n];
        for(int i = 0; i < n; i++) parents[i] = i;
    }

    int find(int i) {
        if(parents[i] == i) return i;
        return parents[i] = find(parents[i]);
    }

    void union(int i, int j) {
        int rootI = find(i);
        int rootJ = find(j);

        if(rootI == rootJ) return;

        parents[rootJ] = rootI;
    }
}