import java.io.*;
import java.util.*;

/*
  문제풀이 시간: 50분 정도 풀다가 잘 모르겠어서 AI에게 핵심 로직 물어보고 구현.
  AI 사용 여부: O
  설명: 트리DP. 서브트리의 개수로 점화식을 세워야 하는데 이 부분이 어려웠음. 거리의 합을 long으로 둬야 했던 문제.
 */
public class 거리합구하기_박서희 {

    static int N;
    static ArrayList<ArrayList<int[]>> edges = new ArrayList<>();
    static int[] subtree;
    static long[] dp;

    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringBuilder answer = new StringBuilder();

        N = Integer.parseInt(br.readLine());

        subtree = new int[N + 1];
        dp = new long[N + 1];

        for (int i = 0; i <= N; i++) {
            edges.add(new ArrayList<>());
        }

        for (int i = 0; i < N - 1; i++) {
            StringTokenizer st = new StringTokenizer(br.readLine());
            int x = Integer.parseInt(st.nextToken());
            int y = Integer.parseInt(st.nextToken());
            int t = Integer.parseInt(st.nextToken());
            edges.get(x).add(new int[]{y, t});
            edges.get(y).add(new int[]{x, t});
        }

        initDfs(1, 0, 0);

        dpDfs(1, 0);

        for (int i = 1; i <= N; i++) {
            answer.append(dp[i]).append("\n");
        }
        System.out.println(answer.toString());
    }

    static void initDfs(int cur, int parent, long dist) {
        subtree[cur] = 1;
        dp[1] += dist;

        for (int[] child : edges.get(cur)) {
            if (child[0] == parent) continue;
            initDfs(child[0], cur, dist + child[1]);
            subtree[cur] += subtree[child[0]];
        }
    }

    static void dpDfs(int cur, int parent) {
        for (int[] child : edges.get(cur)) {
            if (child[0] == parent) continue;

            long childSubtree = subtree[child[0]];
            long weight = child[1];

            // 자식 노드 밑에 있는 노드들이랑은 거리가 가까워서 뺴주고, 부모 노드와 가까운 노드들은 거리가 멀어지므로 더해줌.
            dp[child[0]] = dp[cur] - childSubtree * weight + (N - childSubtree) * weight;
            dpDfs(child[0], cur);
        }
    }
}
