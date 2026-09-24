package sep.week3.jungol;

import java.util.*;
import java.io.*;

public class 등수찾기_박재환 {
    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        init(br);
        br.close();
    }

    static int n, m, x;
    static List<Integer>[] forward;
    static List<Integer>[] reverse;
    static void init(BufferedReader br) throws IOException {
        StringTokenizer st;
        st = new StringTokenizer(br.readLine().trim());
        n = Integer.parseInt(st.nextToken());
        m = Integer.parseInt(st.nextToken());
        x = Integer.parseInt(st.nextToken());
        forward = new ArrayList[n + 1];
        reverse = new ArrayList[n + 1];
        for (int i = 1; i <= n; i++) {
            forward[i] = new ArrayList<>();
            reverse[i] = new ArrayList<>();
        }
        for (int i = 0; i < m; i++) {
            st = new StringTokenizer(br.readLine().trim());
            int win = Integer.parseInt(st.nextToken());
            int lose = Integer.parseInt(st.nextToken());
            forward[win].add(lose);
            reverse[lose].add(win);
        }
        int[] result = solution();
        System.out.printf("%d %d", result[0], result[1]);
    }

    static int[] solution() {
        boolean[] visited = new boolean[n + 1];
        int lower = dfs(x, forward, visited);
        visited = new boolean[n + 1];
        int higher = dfs(x, reverse, visited);
        return new int[] {higher + 1, n - lower};
    }

    static int dfs(int cur, List<Integer>[] graph, boolean[] visited) {
        visited[cur] = true;
        int count = 0;
        for (int next : graph[cur]) {
            if (visited[next]) {
                continue;
            }
            count++;
            count += dfs(next, graph, visited);
        }
        return count;
    }
}