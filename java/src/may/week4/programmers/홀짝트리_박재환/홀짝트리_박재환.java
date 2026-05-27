package may.week4.programmers.홀짝트리_박재환;

import java.util.*;

/**
 * [풀이시간]
 * 00:57:00
 * AI 사용 여부 O
 */
public class 홀짝트리_박재환 {
    public static void main(String[] args) {
        int[] nodes = {11, 9, 3, 2, 4, 6};
        int[][] edges = {{9, 11}, {2, 3}, {6, 3}, {3, 4}};

        Solution solution = new Solution();
        int[] result = solution.solution(nodes, edges);
        System.out.println(Arrays.toString(result));
    }
}

class Solution {
    /**
     * 루트노드가 설정되지 않은 1개 이상의 트리가 있다.
     * - 모든 노드들은 서로 다른 번호를 갖는다.
     *      - 홀수 노드 : 노드의 번호가 홀수, 자식 노드 개수도 홀수
     *      - 짝수 노드 : 노드 번호 짝수, 자식 노드 개수도 짝수
     *      - 역홀수 노드 : 노드 번호 홀수, 자식 노드 개수 짝수
     *      - 역짝수 노드 : 노드 번호 짝수, 자식 노드 개수 홀수
     *
     * -> 각 트리에 대해 루트 노드를 설정했을 때 홀짝 트리와 역홀짝 트리 개수를 구하려한다.
     * - 홀짝 트리 : 홀수 노드와 짝수 노드로만 이루어짐
     * - 역홀짝 트리 : 역홀수 노드와 역짝수 노드로만 이루어짐
     */
    void init(int[] nodes, int[][] edges) {
        this.nodes = nodes;
        this.edges = edges;

        nodeMap = new HashMap<>();
        for(int i : nodes) nodeMap.put(i, new ArrayList<>());

        for(int[] conn : edges) {
            int a = conn[0];
            int b = conn[1];
            nodeMap.get(a).add(b);
            nodeMap.get(b).add(a);
        }

        checked = new HashSet<>();
        resultEvenOdd = resultReverseEvenOdd = 0;
    }

    int[] nodes;
    int[][] edges;
    Map<Integer, List<Integer>> nodeMap;
    Set<Integer> checked;
    int resultEvenOdd, resultReverseEvenOdd;
    public int[] solution(int[] nodes, int[][] edges) {
        init(nodes, edges);

        // 트리를 분리한다. -> 각 트리 분리 후 노드별로 루트노드 설정 후 탐색?
        for(int i : nodes) {
            if(checked.add(i)) {        // 아직 처리되지 않은 노드 -> 트리 구성이 되지 않음
                Set<Integer> group  = new HashSet<>();
                makeGroup(i, -1, group);

                // group 를 사용해서 각 node를 루트노드로 설정했을 때 조건 탐색
                setRootNodes(group);
            }
        }
        return new int[] {resultEvenOdd, resultReverseEvenOdd};
    }

    void makeGroup(int cur, int prev, Set<Integer> groups) {
        checked.add(cur);
        groups.add(cur);

        for(int i : nodeMap.get(cur)) {
            if(i == prev) continue;     // 사이클 방지
            if(checked.contains(i)) continue;       // 중복 방지
            makeGroup(i, cur, groups);
        }
    }

    /**
     * [AI 활용]
     * - 홀짝 트리와 역홀짝 트리 판별 기준을 이해
     * - 루트 노드와 비루트 노드의 자식 수 차이, degree 기준 후보 노드가 정확히 1개여야 하는 이유를 정리
     *
     * ---
     *
     * 결론
     * 각 i 가 루트노드로 있을 때, {홀짝, 역홀짝} 조건을 만족하는 노드를 구합니다.
     * 해당 후보가 루트가 된다면, 나머지 후보들의 degree 값이 변하게 됩니다.
     *
     * 즉, 후보가 정확하게 1개인 항목에 대해서만 {홀짝, 역홀짝} 트리를 생성할 수 있습니다.
     */
    void setRootNodes(Set<Integer> group) {
        int evenOdd = 0;
        int reverseEvenOdd = 0;

        for(int i : group) {
            if((i % 2 == 0 && nodeMap.get(i).size() % 2 == 0) ||
                    (i % 2 == 1 && nodeMap.get(i).size() % 2 == 1)) evenOdd++;
            else if((i % 2 == 0 && nodeMap.get(i).size() % 2 == 1) ||
                        (i % 2 == 1 && nodeMap.get(i).size() % 2 == 0)) reverseEvenOdd++;
        }

        if(evenOdd == 1) resultEvenOdd++;
        if(reverseEvenOdd == 1) resultReverseEvenOdd++;
    }
}