package jun.week2.ngv;

import java.io.*;
import java.util.*;

/*
  문제풀이 시간: 00:36:25
  AI 사용 여부: O 문제 이해가 잘 안 돼서 사용.
 */
public class 출퇴근길_박서희 {
    static int n, m;
    static int S, T;

    static ArrayList<ArrayList<Integer>> graph;
    static ArrayList<ArrayList<Integer>> reverseGraph;

    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer st = new StringTokenizer(br.readLine());

        n = Integer.parseInt(st.nextToken());
        m = Integer.parseInt(st.nextToken());

        graph = new ArrayList<>();
        reverseGraph = new ArrayList<>();

        for (int i = 0; i <= n; i++) {
            graph.add(new ArrayList<>());
            reverseGraph.add(new ArrayList<>());
        }

        for (int i = 0; i < m; i++) {
            st = new StringTokenizer(br.readLine());
            int a = Integer.parseInt(st.nextToken());
            int b = Integer.parseInt(st.nextToken());
            graph.get(a).add(b);
            reverseGraph.get(b).add(a);
        }

        st = new StringTokenizer(br.readLine());
        S = Integer.parseInt(st.nextToken());
        T = Integer.parseInt(st.nextToken());

        int result = solution();
        System.out.println(result);
    }

    static int solution() {
        // 1. S -> X
        Set<Integer> set1 = bfs(S, T, false);
        // 2. X -> T
        Set<Integer> set2 = bfs(T, 0, true);
        // 3. T -> X
        Set<Integer> set3 = bfs(T, S, false);
        // 4. X -> S
        Set<Integer> set4 = bfs(S, 0, true);

        set1.retainAll(set2);
        set1.retainAll(set3);
        set1.retainAll(set4);

        set1.remove(S);
        set1.remove(T);

        return set1.size();
    }

    static Set<Integer> bfs(int cur, int end, boolean isReverse) {
        Set<Integer> set = new HashSet<>();
        Queue<Integer> queue = new LinkedList<>();

        set.add(cur);
        queue.add(cur);

        while (!queue.isEmpty()) {
            int now = queue.poll();

            if (now == end) continue;

            if (!isReverse) {
                for (Integer next : graph.get(now)) {
                    if (set.contains(next)) continue;
                    set.add(next);
                    queue.add(next);
                }
            } else {
                for (Integer next : reverseGraph.get(now)) {
                    if (set.contains(next)) continue;
                    set.add(next);
                    queue.add(next);
                }
            }
        }

        return set;
    }
}
