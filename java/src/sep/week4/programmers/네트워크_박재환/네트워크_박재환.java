package sep.week4.programmers.네트워크_박재환;

import java.io.*;
import java.util.*;

public class 네트워크_박재환 {
    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        init(br);
        br.close();
    }

    static void init(BufferedReader br) throws IOException {
        int n = Integer.parseInt(br.readLine().trim());
        int[][] computers = new int[n][n];
        for(int i = 0; i < n; i++) {
            String line = br.readLine().trim();
            for(int j = 0; j < n; j++) {
                computers[i][j] = line.charAt(j) - '0';
            }
        }
        Solution solution = new Solution();
        System.out.print(solution.solution(n, computers));
    }
}

class Solution {
    public int solution(int n, int[][] computers) {
        set(n);
        for(int i = 0; i < n; i++) {
            for(int j = 0; j < n; j++) {
                if(i == j || computers[i][j] == 0) {
                    continue;
                }
                union(i, j);
            }
        }
        Set<Integer> networks = new HashSet<>();
        for(int i = 0; i < n; i++) {
            networks.add(find(i));
        }

        return networks.size();
    }

    int[] parents;
    void set(int n) {
        parents = new int[n];
        for(int i = 0; i < n; i++) {
            parents[i] = i;
        }
    }
    int find(int i) {
        if(parents[i] == i) {
            return i;
        }
        return parents[i] = find(parents[i]);
    }
    void union(int a, int b) {
        int aRoot = find(a);
        int bRoot = find(b);
        if(aRoot == bRoot) {
            return;
        }
        parents[aRoot] = bRoot;
    }
}