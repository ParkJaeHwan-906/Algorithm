package mar.week1.boj;

import java.util.*;
import java.io.*;

public class 트리_박재환 {
    static BufferedReader br;
    static StringBuilder sb;
    public static void main(String[] args) throws IOException {
        br = new BufferedReader(new InputStreamReader(System.in));
        sb = new StringBuilder();
        init();
        br.close();
        System.out.println(sb);
    }
    static StringTokenizer st;
    static int n, m;
    static List<Integer>[] connections;
    static boolean[] visited;
    static boolean isTree;
    static void init() throws IOException {
        int tc = 1;
        while(true) {
            st = new StringTokenizer(br.readLine().trim());
            n = Integer.parseInt(st.nextToken());       // 정점의 개수
            m = Integer.parseInt(st.nextToken());       // 간선의 개수
            if(n==0 && m==0) break;

            connections = new List[n+1];
            for(int i=0; i<n+1; i++) connections[i] = new ArrayList<>();

            for(int i=0; i<m; i++) {
                st = new StringTokenizer(br.readLine().trim());
                int from = Integer.parseInt(st.nextToken());
                int to = Integer.parseInt(st.nextToken());
                connections[from].add(to);
                connections[to].add(from);
            }

            visited = new boolean[n+1];
            int tree = 0;
            for(int i=1; i<n+1; i++) {
                if(visited[i]) continue;
                isTree = true;
                findTree(i, -1);
                if(isTree) tree++;
            }

            sb.append("Case ").append(tc++).append(": ").append(getStr(tree)).append('\n');
        }
    }
    static String getStr(int tree) {
        if(tree == 0) return "No trees.";
        if(tree == 1) return "There is one tree.";
        return String.format("A forest of %d trees.", tree);
    }
    static void findTree(int cur, int prev) {
        visited[cur] = true;
        for(int next : connections[cur]) {
            if(next == prev) continue;
            if(visited[next]) {
                isTree = false;
                continue;
            }
            findTree(next, cur);
        }
    }
}
