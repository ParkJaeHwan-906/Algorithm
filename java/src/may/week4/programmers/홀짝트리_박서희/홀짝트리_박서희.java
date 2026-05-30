package may.week4.programmers.홀짝트리_박서희;

import java.util.*;

/*
  문제풀이 시간: 1:08:45
  AI 사용 여부: O -> 감을 못 잡겠어서 사용. maybeNormalTree == 1, maybeReverseTree == 1 일 때만 가능하다는 걸 알게 됨.
  생각의 흐름: 처음엔 dfs로 트리끼리 분리를 하고 거기서 반복문을 돌렸는데 시간 초과가 났다. 그래서 dfs하면서 바로 트리 수를 세는 걸로 바꿨다.
 */
public class 홀짝트리_박서희 {
    public static void main(String[] args) {
        int[] nodes = {11, 9, 3, 2, 4, 6};
        int[][] edges = {{9, 11}, {2, 3}, {6, 3}, {3, 4}};

        Solution solution = new Solution();
        int[] result = solution.solution(nodes, edges); //5
        System.out.println(Arrays.toString(result));
    }

}

/*
    홀수 노드: 노드의 번호가 홀수이며 자식의 노드의 개수가 홀수인 노드짝
    짝수 노드: 노드의 번호가 짝수이며 자식의 노드의 개수가 짝수인 노드
    역홀수 노드: 노드의 번호가 홀수이며 자식 노드의 개수가 짝수인 노드
    역짝수 노드: 노드의 번호가 짝수이며 자식의 노드의 개수가 홀수인 노드
    return: 홀짝 트리가 도리 수 있는 트리의 개수, 역홀짝 트리가 될 수 있는 노드
 */
class Solution {
    int[] nodes;
    int[] visited;
    int[] degree;

    Map<Integer, Integer> nodeMap = new HashMap<>();
    List<List<Integer>> graph = new ArrayList<>();

    int totalNormalTree = 0;
    int totalReverseTree = 0;

    int maybeNormalTree = 0;
    int maybeReverseTree = 0;

    public int[] solution(int[] nodes, int[][] edges) {
        this.nodes = nodes;
        this.degree = new int[nodes.length];

        for (int i = 0; i < nodes.length; i++) {
            nodeMap.put(nodes[i], i); // 노드값으로 인덱스 조회
            graph.add(new ArrayList<>());
        }

        for (int i = 0; i < edges.length; i++) {

            int idxA = nodeMap.get(edges[i][0]);
            int idxB = nodeMap.get(edges[i][1]);

            graph.get(idxA).add(idxB);
            graph.get(idxB).add(idxA);

            degree[idxA]++;
            degree[idxB]++;
        }

        visited = new int[nodes.length];

        for (int i = 0; i < nodes.length; i++) {
            if (visited[i] == 1) continue;

            maybeNormalTree = 0;
            maybeReverseTree = 0;

            visited[i] = 1;
            dfs(i);

            // 루트 노드가 되면 한 개의 노드만 자식의 수가 달라지게 됨.
            if (maybeNormalTree == 1) totalNormalTree++;
            if (maybeReverseTree == 1) totalReverseTree++;
        }
        return new int[] {totalNormalTree, totalReverseTree};
    }

    public void dfs(int curIdx) {
        int nodeValue = nodes[curIdx];
        int d = degree[curIdx];

        if (nodeValue % 2 == d % 2) {
            maybeNormalTree++;
        } else {
            maybeReverseTree++;
        }

        for (int childIdx : graph.get(curIdx)) {
            if (visited[childIdx] == 1) continue;

            visited[childIdx] = 1;
            dfs(childIdx);
        }
    }
}
