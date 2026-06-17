package jun.week3.codetree;

import java.util.*;
import java.io.*;

/**
 * [풀이 시간]
 * 01:18:24
 * AI 사용 여부 X
 */
public class 색깔트리_박재환 {
    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        init(br);
        br.close();
    }

    final static int ADD = 100;
    final static int UPDATE = 200;
    final static int QUERY_COLOR = 300;
    final static int QUERY_SCORE = 400;

    static Map<Integer, Node> idToNode;
    static void init(BufferedReader br) throws IOException {
        StringTokenizer st;
        StringBuilder sb = new StringBuilder();

        int q = Integer.parseInt(br.readLine().trim());

        idToNode = new HashMap<>();
        while(q-- > 0) {
            st = new StringTokenizer(br.readLine().trim());

            int type = Integer.parseInt(st.nextToken());
            if(type == ADD) { add(st); }
            else if(type == UPDATE) { update(st); }
            else if(type == QUERY_COLOR) {
                int result = getQueryColor(st);
                sb.append(result).append('\n');
            }
            else if(type == QUERY_SCORE) {
                long result = getQueryScore();
                sb.append(result).append('\n');
            }
        }
        System.out.println(sb);
    }

    static class Node {
        int mid;            // 고유 번호
        int pid;            // 부모 번호
        int color;          // 색깔
        int depth;          // 최대 깊이

        int colorBits;      // 현재 서브트리에 포함된 색 종류
        List<Node> childs;       // 자식 노드

        Node(int mid, int pid, int color, int depth) {
            this.mid = mid;
            this.pid = pid;
            this.color = color;
            this.depth = depth;

            this.colorBits = 0;
            childs = new ArrayList<>();

            addColor(color);
        }

        void addChild(Node node) {
            this.childs.add(node);
            addColor(node.color);
        }

        void addColor(int color) { this.colorBits |= (1 << color); }

        void resetColor(int color) {
            this.color = color;
            this.colorBits = (1 << color);
        }
    }

    static void add(StringTokenizer st) {
        int mid = Integer.parseInt(st.nextToken());
        int pid = Integer.parseInt(st.nextToken());
        int color = Integer.parseInt(st.nextToken());       // 1 : 빨, 2 : 주, 3 : 노, 4 : 초, 5 파
        int depth = Integer.parseInt(st.nextToken());

        Node node = new Node(mid, pid, color, depth);

        // root 노드라면 그냥 추가만
        if(node.pid == -1) {
            idToNode.put(mid, node);
            return;
        }

        // depth에 위배되지 않는지 확인하며 추가
        // root 노드까지 역으로 거슬러 올라가며 확인하기
        if(!canAdd(node)) return;

        // 추가 가능
        applyNewNode(node);
    }

    static boolean canAdd(Node node) {
        // 노드가 추가되는 경우
        // - 수평 확장 ( 트리 깊이에 영향 X )
        // - 수직 확장 ( 트리 깊이에 영향 O )

        int mDepth = 1;

        while(true) {
            Node parent = idToNode.get(node.pid);
            if(parent == null) break;       //  root 노드까지 탐색한 경우
            if(parent.depth < ++mDepth) return false;
            node = parent;
        }

        return true;
    }

    static void applyNewNode(Node node) {
        idToNode.put(node.mid, node);       // 현재 노드 추가

        // root 까지 child 노드에 추가
        // - childs 업데이트
        // - colorSet 업데이트
        int pid = node.pid;
        while(true) {
            Node parent = idToNode.get(pid);
            if(parent == null) break;
            parent.addChild(node);
            pid = parent.pid;
        }
    }

    static void update(StringTokenizer st) {
        int mid = Integer.parseInt(st.nextToken());
        int color = Integer.parseInt(st.nextToken());

        Node node = idToNode.get(mid);
        node.resetColor(color);
        for(Node child : node.childs) {
            child.resetColor(color);
        }

        propagationParent(idToNode.get(node.pid));
    }

    static void propagationParent(Node node) {
        while(node != null) {
            node.resetColor(node.color);
            for(Node child : node.childs) {
                node.addColor(child.color);
            }
            node = idToNode.get(node.pid);
        }
    }

    static int getQueryColor(StringTokenizer st) {
        int mid = Integer.parseInt(st.nextToken());
        Node node = idToNode.get(mid);
        return node.color;
    }

    static long getQueryScore() {
        long totalScore = 0;
        for(Node node : idToNode.values()) {
            int weight = Integer.bitCount(node.colorBits);
            totalScore += (weight * weight);
        }
        return totalScore;
    }
    // ===
//    static void printNodes() {
//        for(Node node : idToNode.values()) {
//            System.out.printf("mid = %d, pid = %d, color = %d, value = %d, depth = %d\n", node.mid, node.pid, node.color, Integer.bitCount(node.colorBits), node.depth);
//            for(Node child : node.childs) {
//                System.out.print(child.mid + " ");
//            }
//            System.out.println();
//        }
//    }
}
