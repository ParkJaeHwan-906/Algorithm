package sep.week3.codetree;

import java.util.*;
import java.io.*;

public class 색깔트리_박재환 {
    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        init(br);
        br.close();
    }

    static final int ADD = 100;
    static final int CHANGE = 200;
    static final int COLOR_QUERY = 300;
    static final int SCORE_QUERY = 400;

    static class Node {
        int id;                         // 고유 id
        int pId;                        // 부모 id
        int maxDepth;                   // 최대 깊이
        // ==========================================
        List<Node> childNodes;           // 서브트리
        int curHeight;
        int colorSet;
        Node(int id, int pId, int maxDepth, int colorSet) {
            this.id = id;
            this.pId = pId;
            this.maxDepth = maxDepth;
            this.childNodes = new ArrayList<>();
            this.curHeight = 1;                     // 초기 높이는 1
            this.colorSet = (1 << colorSet);
        }
    }

    static Map<Integer, Node> nodes;
    static void init(BufferedReader br) throws IOException {
        StringTokenizer st;
        StringBuilder sb = new StringBuilder();
        int q = Integer.parseInt(br.readLine().trim());
        nodes = new HashMap<>();
        while(q-- > 0) {
            st = new StringTokenizer(br.readLine().trim());
            int type = Integer.parseInt(st.nextToken());
            if(type == ADD) {
                add(st);
            } else if(type == CHANGE) {
                change(st);
            } else if(type == COLOR_QUERY) {

            } else if(type == SCORE_QUERY) {

            }
        }
    }

    static void add(StringTokenizer st) {
        int id = Integer.parseInt(st.nextToken());
        int pId = Integer.parseInt(st.nextToken());
        int color = Integer.parseInt(st.nextToken());
        int maxDepth = Integer.parseInt(st.nextToken());

        Node node = new Node(id, pId, maxDepth, color);
        if(node.pId == -1) {            // 루트 노드인 경우
            nodes.put(node.id, node);
            return;
        }

        // 루트 노드가 아닌 경우
        Node pNode = nodes.get(node.pId);
        if(!isPossible(pNode)) {            // 추가할 수 없음
            return;
        }
        // 노드 추가
        pNode.childNodes.add(node);
        nodes.put(node.id, node);
        // colorSet 업데이트
        Node cur = pNode;
        while (cur != null) {
            cur.colorSet |= node.colorSet;
            cur = nodes.get(cur.pId);
        }
    }

    static boolean isPossible(Node pNode) {
        Node cur = pNode;
        int depth = 2;          // 부모노드에서 시작 ( 부모 - 자식 )
        while(cur != null) {    // 루트까지 탐색
            if(depth > cur.maxDepth) {
                return false;
            }
            cur = nodes.get(cur.pId);
            depth++;
        }
        return true;
    }

    static void change(StringTokenizer st) {
        int id = Integer.parseInt(st.nextToken());
        int color = Integer.parseInt(st.nextToken());
        Node cur = nodes.get(id);

    }

    static void propagationNewColor(Node node, int color) {
        i
    }
}
