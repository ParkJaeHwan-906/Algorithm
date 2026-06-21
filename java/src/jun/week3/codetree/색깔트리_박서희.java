package jun.week3.codetree;

import java.io.*;
import java.util.*;

/*
  문제풀이 시간: 01:01:59
  AI 사용 여부: O 자식 노드를 어떤 식으로 저장해야 할지 찾아봄. Node 안에 List<Integer> children으로 하면 됨.
  수행시간이 2834ms라서 최적화 시도해보면 좋을듯함.
 */
public class 색깔트리_박서희 {
    static Map<Integer, Node> nodeMap = new HashMap<>();
    static List<Integer> rootNodes = new ArrayList<>();

    static StringBuilder sb = new StringBuilder();
    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        int Q = Integer.parseInt(br.readLine());

        while (Q-- > 0) {
            StringTokenizer st = new StringTokenizer(br.readLine());
            int command = Integer.parseInt(st.nextToken());

            switch (command) {
                case 100: addNode(st); break;
                case 200: changeNode(st); break;
                case 300: checkNode(st); break;
                case 400: getScore(); break;
            }
        }

        System.out.print(sb);
    }

    static void addNode(StringTokenizer st) {
        int m = Integer.parseInt(st.nextToken());
        int p = Integer.parseInt(st.nextToken());
        int color = Integer.parseInt(st.nextToken());
        int maxDepth = Integer.parseInt(st.nextToken());

        // 노드 추가 가능한지 체크
        if (p != -1) {
            int curDepth = 2;
            int parentId = p;

            while (parentId != -1) {
                if (nodeMap.get(parentId).maxDepth < curDepth) {
                    return;
                }
                curDepth++;
                parentId = nodeMap.get(parentId).p;
            }
        }

        // 노드 추가
        Node node = new Node(m, p , color, maxDepth);
        nodeMap.put(m, node);
        if (p == -1) rootNodes.add(m);
        else nodeMap.get(p).children.add(m);
    }

    static void changeNode(StringTokenizer st) {
        int m = Integer.parseInt(st.nextToken());
        int color = Integer.parseInt(st.nextToken());
        nodeMap.get(m).color = color;
        changeChildrenColor(m, color);
    }

    static void checkNode(StringTokenizer st) {
        int m = Integer.parseInt(st.nextToken());
        sb.append(nodeMap.get(m).color).append("\n");
    }

    static void getScore() {
        int score = 0;
        for (int rootId : rootNodes) {
            score += treeDp(rootId)[0];
        }
        sb.append(score).append("\n");
    }

    private static int[] treeDp(int curId) {
        int bit = 1 << nodeMap.get(curId).color;
        int sumChildren = 0;

        for (Integer childrenId: nodeMap.get(curId).children) {
            int[] childrenTreeDp = treeDp(childrenId);
            sumChildren += childrenTreeDp[0];
            bit |= childrenTreeDp[1];
        }
        int curValue = Integer.bitCount(bit);
        sumChildren += curValue * curValue;
        return new int[] {sumChildren, bit};
    }

    private static void changeChildrenColor(int m, int color) {
        nodeMap.get(m).color = color;
        for (Integer childrenId: nodeMap.get(m).children) {
            changeChildrenColor(childrenId, color);
        }
    }

    static class Node{
        int m, p, color, maxDepth;
        List<Integer> children;

        public Node(int m, int p, int color, int maxDepth) {
            this.m = m;
            this.p = p;
            this.color = color;
            this.maxDepth = maxDepth;
            this.children = new ArrayList<>();
        }
    }
}
