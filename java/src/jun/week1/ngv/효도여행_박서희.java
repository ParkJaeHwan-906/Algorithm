package jun.week1.ngv;

import java.io.*;
import java.util.*;

/*
  문제풀이 시간: 시간 초과나서 수정.
  AI 사용 여부: O 시간을 줄이는 발상을 하기가 어려웠음.
              S는 변하지 않기 때문에 dfs할 때 S를 기준으로 dp를 진행함. LCS에서는 이전 행의 dp만 필요한 것을 이용했어야 했음.
 */
public class 효도여행_박서희 {

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

        dfs(0, new int[S.length() + 1]);

        System.out.println(answer);
    }

    static void dfs(int cur, int[] prev) {
        boolean isLeafNode = true;
        visited[cur] = 1;

        for (Edge e : graph.get(cur)) {
            if (visited[e.u] == 0) {
                isLeafNode = false;

                int[] next = new int[S.length() + 1];
                char nextChar = e.s.charAt(0);

                for (int i = 1; i <= S.length(); i++) {
                    if (nextChar == S.charAt(i - 1)) {
                        next[i] = prev[i - 1] + 1;
                    } else {
                        next[i] = Math.max(next[i - 1], prev[i]);
                    }
                }
                dfs(e.u, next);
            }
        }

        if (isLeafNode) {
            answer = Math.max(answer, prev[S.length()]);
        }

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
