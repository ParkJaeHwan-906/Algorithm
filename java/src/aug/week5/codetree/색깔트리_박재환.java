package aug.week5.codetree;

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
    static final int QUERY = 300;
    static final int SCORE = 400;

    static class Node {
        int mId;
        int pId;
        int color;
        int maxDepth;
        List<Node> subTrees;
        Node(int mId, int pId, int color, int maxDepth) {
            this.mId = mId;
            this.pId = pId;
            this.color = color;
            this.maxDepth = maxDepth;
            this.subTrees = new ArrayList<>();
        }
    }

    static Map<Integer, Node> nodes;
    static void init(BufferedReader br) throws IOException {
        StringTokenizer st;
        nodes = new HashMap<>();
        StringBuilder sb = new StringBuilder();

        int q = Integer.parseInt(br.readLine().trim());
        while(q-- > 0) {
            st = new StringTokenizer(br.readLine().trim());
            int type = Integer.parseInt(st.nextToken());
            if(type == ADD) { add(st); }
            else if(type == CHANGE) { change(st); }
            else if(type == QUERY) {
                int result= query(st);
                sb.append(result).append("\n");
            }
            else if(type == SCORE) {
                long result= score();
                sb.append(result).append("\n");
            }
        }
        System.out.println(sb);
    }

    // ===============================================
    //  Node 추가
    // ===============================================
    static void add(StringTokenizer st) {
        int mId = Integer.parseInt(st.nextToken());
        int pId = Integer.parseInt(st.nextToken());
        int color = Integer.parseInt(st.nextToken());
        int maxDepth = Integer.parseInt(st.nextToken());
        Node node = new Node(mId, pId, color, maxDepth);

        if(node.pId == -1) {            // 루트 노드인 경우 바로 넣음
            nodes.put(node.mId, node);
            return;
        }

        if (canAdd(node.pId)) {   // 바텀업으로 모두 조건을 만족하는지 확인
            nodes.put(node.mId, node);
            nodes.get(node.pId).subTrees.add(node);
        }

    }
    static boolean canAdd(int mId) {
        // mId 노드에 node 를 추가할 수 있는지 확인
        Node cur = nodes.get(mId);
        int dist = 1;
        while(cur != null) {
            if(dist + 1 > cur.maxDepth) {       // maxDepth 위반
                return false;
            }
            cur = nodes.get(cur.pId);
            dist++;
        }
        return true;
    }

    // ===============================================
    //  color 변경
    // ===============================================
    static void change(StringTokenizer st) {
        int mId = Integer.parseInt(st.nextToken());
        int color = Integer.parseInt(st.nextToken());
        Node cur = nodes.get(mId);
        changeColorSubTrees(cur, color);
    }
    static void changeColorSubTrees(Node node, int color) {
        node.color = color;
        for(Node child : node.subTrees) {
            changeColorSubTrees(child, color);
        }
    }

    // ===============================================
    //  color 조회
    // ===============================================
    static int query(StringTokenizer st) {
        int mId = Integer.parseInt(st.nextToken());
        return nodes.get(mId).color;
    }
    // ===============================================
    //  가치 조회
    // ===============================================
    static long score() {
        long score = 0;
        for(Node node : nodes.values()) {
            int colors = getValue(node, 0);
            int bits = Integer.bitCount(colors);
            score += (bits * bits);
        }
        return score;
    }
    static int getValue(Node node, int colors) {
        colors |= (1 << node.color);
        for(Node child : node.subTrees) {
            colors |= getValue(child, colors);
        }
        return colors;
    }

    // ===============================================
    //  공통
    // ===============================================
    static void printNodes() {
        for(Node node : nodes.values()) {
            System.out.printf("mid : %d, pId : %d, color : %d\n", node.mId, node.pId, node.color);
            for(Node child : node.subTrees) {
                System.out.print(child.mId + " ");
            }
            System.out.println();
        }
    }
}
