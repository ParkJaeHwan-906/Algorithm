package jun.week1.ngv;

import java.io.*;
import java.util.*;

/*
  문제풀이 시간: 36분 -> 제출했는데 시간초과남.
  AI 사용 여부: O LCS 몰라서 찾아봄.
 */
public class 효도여행_시간초과_박서희 {

    static String S;
    static List<List<Edge>> graph = new ArrayList<>();
    static int[] visited;

    static int answer = 0;

    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

        StringTokenizer st = new StringTokenizer(br.readLine());
        int N = Integer.parseInt(st.nextToken());
        int M = Integer.parseInt(st.nextToken());

        visited = new int[N];
        for (int i = 0; i < N; i++) {
            graph.add(new ArrayList<>());
        }

        S = br.readLine();

        for (int i = 0; i < N - 1; i++) {
            st = new StringTokenizer(br.readLine());
            int u = Integer.parseInt(st.nextToken()) - 1;
            int v = Integer.parseInt(st.nextToken()) - 1;
            String s = st.nextToken();
            graph.get(u).add(new Edge(v, s));
            graph.get(v).add(new Edge(u, s));
        }

        dfs(0, new StringBuilder());

        System.out.println(answer);
    }

    static void dfs(int cur, StringBuilder sb) {
        boolean isLeafNode = true;
        visited[cur] = 1;

        for (Edge e : graph.get(cur)) {
            if (visited[e.u] == 0) {
                isLeafNode = false;

                sb.append(e.s);
                dfs(e.u, sb);
                sb.setLength(sb.length() - 1);
            }
        }

        if (isLeafNode) {
            String T = sb.toString();
            answer = Math.max(answer, getLcsLength(T));
        }

    }

    static int getLcsLength(String T) {
        int[][] dp = new int[T.length() + 1][S.length() + 1];

        for (int i = 1; i <= T.length(); i++) {
            for (int j = 1; j <= S.length(); j++) {
                if (T.charAt(i - 1) == S.charAt(j - 1)) {
                    dp[i][j] = dp[i - 1][j - 1] + 1;
                } else {
                    dp[i][j] = Math.max(dp[i][j - 1], dp[i - 1][j]);
                }
            }
        }

        return dp[T.length()][S.length()];
    }


    static class Edge {
        int u;
        String s;

        public Edge(int u, String s) {
            this.u = u;
            this.s = s;
        }
    }
}
