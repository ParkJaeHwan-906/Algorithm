package mar.week3.codetree;

import java.util.*;
import java.io.*;

public class 산타의선물공장2_박재환 {
    static BufferedReader br;
    static StringBuilder sb;

    public static void main(String[] args) throws IOException {
        br = new BufferedReader(new InputStreamReader(System.in));
        sb = new StringBuilder();
        init();
        br.close();
        System.out.println(sb);
    }

    static final int SET = 100;
    static final int MOVE = 200;
    static final int CHANGE = 300;
    static final int MOD = 400;
    static final int GIFT_QUERY = 500;
    static final int BELT_QUERY = 600;

    static StringTokenizer st;

    static void init() throws IOException {
        int q = Integer.parseInt(br.readLine().trim());
        while (q-- > 0) {
            st = new StringTokenizer(br.readLine().trim());
            int cmd = Integer.parseInt(st.nextToken());

            if (cmd == SET) set();
            else if (cmd == MOVE) {
                int result = move();
                sb.append(result).append('\n');
            }
            else if (cmd == CHANGE) {
                int result = change();
                sb.append(result).append('\n');
            } else if (cmd == MOD) {
                int result = mod();
                sb.append(result).append('\n');
            } else if (cmd == GIFT_QUERY) {
                int result = giftQuery();
                sb.append(result).append('\n');
            } else if (cmd == BELT_QUERY) {
                int result = beltQuery();
                sb.append(result).append('\n');
            }
//            printBelt();
//            System.out.println("_________________________");
        }
    }

    /**
     * n 개의 벨트 설치
     * m 개의 물건 -> m 개의 선물 위치가 공백을 사이에 두고 주어진다.
     */
    static class Node {
        int id;
        Node prev, next;

        Node(int id) {
            this.id = id;
        }

        void init() {
            prev = null;
            next = null;
        }
    }

    static class Belt {
        Node head;
        Node tail;
        int size;

        Belt() {
            this.head = null;
            this.tail = null;
            this.size = 0;
        }

        void addLast(Node node) {
            if (head == null) {
                head = node;
            } else {
                tail.next = node;
                node.prev = tail;
            }
            tail = node;
            size++;
        }

        void addFirst(Node node) {
            if(head == null) {
                tail = node;
            } else {
                head.prev = node;
                node.next = head;
            }
            head = node;
            size++;
        }

        Node pollFirst() {
            if(size == 0) return null;

            Node node = head;
            if(size == 1) {
                head = null;
                tail = null;
            } else {
                Node next = node.next;
                next.prev = null;
                head = next;
            }

            size--;
            node.init();
            return node;
        }
    }

    static int n, m;
    static Node[] nodes;
    static Belt[] belts;
    static void set() {
        n = Integer.parseInt(st.nextToken());
        m = Integer.parseInt(st.nextToken());

        belts = new Belt[n + 1];
        nodes = new Node[m + 1];

        // 노드 생성
        for (int i = 1; i < m + 1; i++) nodes[i] = new Node(i);
        // 벨트 생성
        for (int i = 1; i < n + 1; i++) belts[i] = new Belt();

        for (int i = 1; i < m + 1; i++) {
            int bId = Integer.parseInt(st.nextToken());
            Node node = nodes[i];
            Belt belt = belts[bId];
            belt.addLast(node);
        }
    }

    /**
     * a 벨트에서 b 벨트로 옮긴다.
     *
     * a 벨트의 tail 을 b 벨트의 head 와 연결
     * a 벨트의 head 를 b head 로 변경
     */
    static int move() {
        int a = Integer.parseInt(st.nextToken());
        int b = Integer.parseInt(st.nextToken());

        Belt aBelt = belts[a];
        Belt bBelt = belts[b];

        if(aBelt.size == 0) return bBelt.size;

        Node aHead = aBelt.head;
        Node aTail = aBelt.tail;
        // Belt b가 비어있는 경우 -> head, tail 통으로 교체
        if(bBelt.size == 0) {
            bBelt.head = aHead;
            bBelt.tail = aTail;
        } else {        // 값이 있는 경우 bHead 와 aTail 연결
            bBelt.head.prev = aTail;
            aTail.next = bBelt.head;
            bBelt.head = aHead;
        }
        aBelt.head = aBelt.tail = null;
        bBelt.size += aBelt.size;
        aBelt.size = 0;

        return bBelt.size;
    }

    /**
     * a, b 벨트의 맨 앞에 있는 물건을 교체한다.
     * b 벨트의 물건 개수를 출력한다.
     * <p>
     * 둘 중 선물이 존재하지 않는다면 옮기기만 한다. (교체 X)
     */
    static int change() {
        int a = Integer.parseInt(st.nextToken());
        int b = Integer.parseInt(st.nextToken());

        Belt aBelt = belts[a];
        Belt bBelt = belts[b];

        Node aFirst = aBelt.pollFirst();
        Node bFirst = bBelt.pollFirst();

        if(aFirst != null) bBelt.addFirst(aFirst);
        if(bFirst != null) aBelt.addFirst(bFirst);

        return bBelt.size;
    }
    /**
     * a -> b 로 선물을 절반 옮긴다.
     * 만약 a 개수가 1개인 경우는 옮기지 않는다.
     * 옮긴 뒤 b 벨트에 있는 개수를 출력한다.
     */
    static int mod() {
        int a = Integer.parseInt(st.nextToken());
        int b = Integer.parseInt(st.nextToken());

        Belt aBelt = belts[a];
        Belt bBelt = belts[b];

        if(aBelt.size <= 1) return bBelt.size;

        int size = aBelt.size;

        Node first = aBelt.head;
        Node last = aBelt.head;
        for(int i=1; i<size/2; i++) last = last.next;

        // aBelt 갱신
        Node next = last.next;
        aBelt.head = next;
        next.prev = null;

        last.next = null;
        // bBelt 에 붙임 (앞에 삽입)
        if(bBelt.size == 0) {
            bBelt.tail = last;
        } else {
            bBelt.head.prev = last;
            last.next = bBelt.head;
        }
        bBelt.head = first;

        aBelt.size -= (size/2);
        bBelt.size += (size/2);

        return bBelt.size;
    }
    static int giftQuery() {
        int pId = Integer.parseInt(st.nextToken());
        Node node = nodes[pId];

        int prev = node.prev == null ? -1 : node.prev.id;
        int next = node.next == null ? -1 : node.next.id;
        return prev + 2 * next;
    }
    static int beltQuery() {
        int bId = Integer.parseInt(st.nextToken());
        Belt belt = belts[bId];

        int fist = belt.size == 0 ? -1 : belt.head.id;
        int last = belt.size == 0 ? -1 : belt.tail.id;
        int size = belt.size;

        return fist + 2 * last + 3 * size;
    }

    //-------------------------------------------
    static void printBelt() {
        // 벨트 확인
        for (int i = 1; i < n + 1; i++) {
            StringBuilder sbb = new StringBuilder();
            Node cur = belts[i].head;
            while (cur != null) {
                sbb.append(cur.id).append(' ');
                cur = cur.next;
            }
            System.out.printf("[BELT] %d : %s\n", i, sbb.toString());
        }
    }
}
