package apr.week1.boj;

import java.util.*;
import java.io.*;

public class 숫자고르기_박재환 {
    static BufferedReader br;
    public static void main(String[] args) throws IOException {
        br = new BufferedReader(new InputStreamReader(System.in));
        init();
        br.close();
    }
    static int n;
    static int[] arr;
    static TreeSet<Integer> maxSet;
    static void init() throws IOException {
        maxSet = new TreeSet<>();

        n = Integer.parseInt(br.readLine().trim());
        arr = new int[n + 1];
        for(int i = 0; i < n; i++) arr[i + 1] = Integer.parseInt(br.readLine().trim());

        for(int i = 1; i < n + 1; i++) {
            boolean[] visited = new boolean[n + 1];
            findMaxSize(i, i, visited);
        }

        StringBuilder sb = new StringBuilder();
        sb.append(maxSet.size()).append('\n');
        for(int i : maxSet) sb.append(i).append('\n');
        System.out.println(sb);
    }
    static void findMaxSize(int start, int cur, boolean[] visited) {
        visited[cur] = true;
        int next = arr[cur];
        if(!visited[next]) {
            findMaxSize(start, next, visited);
        } else {
            if(next == start) maxSet.add(start);
        }
    }
}
