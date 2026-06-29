package jun.week4.programmers.길찾기게임_박재환;

import java.util.*;

/**
 * AI 사용 여부 O
 * -> 초기에 생각했던 접근 방식이 너무 복잡했었음
 *      y 값으로 트리의 Level을 판단 : TreeMap을 사용해서 TreeMap<Integer, Node> 로 관리
 *      x 으로 삽입될 위치 탐색 List<Node>로 관리 + BinarySearch로 삽입 위치 탐색
 *
 * => AI : 그렇게 풀어도 되긴하는데, 굳이..?
 * => Node(id, x, y, LeftNode, RightNode) 로 관리 -> 정렬 -> 트리 생성
 */
public class 길찾기게임_박재환 {
    public static void main(String[] args) {
        int[][] nodeinfo = {
                {5, 3},
                {11, 5},
                {13, 3},
                {3, 5},
                {6, 1},
                {1, 3},
                {8, 6},
                {7, 2},
                {2, 2}
        };

        Solution sol = new Solution();
        int[][] result = sol.solution(nodeinfo);

        for(int[] arr : result) {
            System.out.println(Arrays.toString(arr));
        }
    }
}

class Solution {
    /**
     * 모든 노드는 다른 x 값을 갖는다.
     * 같은 레벨에 있는 노드는 같은 y 좌표를 갖는다.
     * 자식 노드의 y값은 항상 부모 노드보다 작다.
     * 임의의 노드 v의 왼쪽 서브트리에 있는 모든 노드의 x 값은 v의 x값보다 작다.
     * 임의의 노드 v의 오른쪽 서브트리에 있는 모든 노드의 x값은 v의 x값보다 크다.
     */
    class Node implements Comparable<Node> {
        int id;
        int x, y;
        Node left, right;

        Node(int id, int x, int y) {
            this.id = id;
            this.x = x;
            this.y = y;

        }

        public int compareTo(Node o) {
            // y 내림차순 (Level 별)
            if(this.y != o.y) return Integer.compare(o.y, this.y);
            // x 오름차순 (왼쪽 > 오른쪽)
            return Integer.compare(this.x, o.x);
        }
    }
    Node[] nodes;
    List<Integer> preList;
    List<Integer> postList;
    public int[][] solution(int[][] nodeinfo) {
        set(nodeinfo);
        Node root = makeTree();

        preList = new ArrayList<>();
        preOrder(root);
        postList = new ArrayList<>();
        postOrder(root);

        int[][] answer = new int[2][nodeinfo.length];
        for(int i = 0; i < nodeinfo.length; i++) answer[0][i] = preList.get(i);
        for(int i = 0; i < nodeinfo.length; i++) answer[1][i] = postList.get(i);
        return answer;
    }

    void set(int[][] nodeinfo) {
        nodes = new Node[nodeinfo.length];
        for(int i = 0; i < nodeinfo.length; i++) {
            int x = nodeinfo[i][0];
            int y = nodeinfo[i][1];
            nodes[i] = new Node(i + 1, x, y);
        }
    }

    Node makeTree() {
        Arrays.sort(nodes);

        Node root = nodes[0];
        for(int i = 1; i < nodes.length; i++) {
            add(root, nodes[i]);
        }

        return root;
    }

    void add(Node p, Node c) {
        if(p.x > c.x) {
            if(p.left == null) {
                p.left = c;
            } else {
                add(p.left, c);
            }
        } else {
            if(p.right == null) {
                p.right = c;
            } else {
                add(p.right, c);
            }
        }
    }

    void preOrder(Node cur) {
        // 현재 add
        preList.add(cur.id);
        // 왼쪽 먼저 탐색
        if(cur.left != null) preOrder(cur.left);
        // 오른쪽 탐색
        if(cur.right != null) preOrder(cur.right);
    }

    void postOrder(Node cur) {
        // 왼쪽 끝까지 탐색
        if(cur.left != null) postOrder(cur.left);
        // 오른쪽 끝까지 탐색
        if(cur.right != null) postOrder(cur.right);
        // 현재 add
        postList.add(cur.id);
    }
}
