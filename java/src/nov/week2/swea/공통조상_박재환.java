package nov.week2.swea;

import java.util.*;
import java.io.*;

public class 공통조상_박재환 {
    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringBuilder sb = new StringBuilder();
        int tc = Integer.parseInt(br.readLine().trim());
        for(int i = 1; i <= tc; i++) {
            sb.append('#').append(i).append(' ').append(init(br)).append('\n');
        }
        System.out.print(sb);
        br.close();
    }

    static int v, e;
    static int findA, findB;
    static int[] parent;
    static List<Integer>[] graph;
    static String init(BufferedReader br) throws IOException {
        StringTokenizer st = new StringTokenizer(br.readLine().trim());
        v = Integer.parseInt(st.nextToken());
        e = Integer.parseInt(st.nextToken());
        findA = Integer.parseInt(st.nextToken());
        findB = Integer.parseInt(st.nextToken());
        parent = new int[v + 1];
        graph = new List[v + 1];
        for(int i = 0; i <= v; i++) {
            graph[i] = new ArrayList<>();
        }
        st = new StringTokenizer(br.readLine().trim());
        for(int i = 0; i < e; i++) {
            int a = Integer.parseInt(st.nextToken());
            int b = Integer.parseInt(st.nextToken());
            parent[b] = a;                                  // 부모 -> 자식 순으로 주어짐
            graph[a].add(b);
        }
        return solution();
    }

    static String solution() {
        int commonParent = findCommonParent();
        int totalSubTree = countSubTree(commonParent);
        return String.format("%d %d", commonParent, totalSubTree);
    }

    static int findCommonParent() {
        int a = findA, b = findB;
        boolean[] visited = new boolean[v + 1];
        while(a != 0) {
            visited[a] = true;
            a = parent[a];
        }

        while(b != 0) {
            if(visited[b])
                return b;
            b = parent[b];
        }
        return -1;
    }

    static int countSubTree(int cur) {
        int count = 1;
        for(int next : graph[cur]) {
            count += countSubTree(next);
        }
        return count;
    }
}
