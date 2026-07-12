package jul.week1.programmers.표편집_박재환;

import java.util.*;

/**
 * [풀이 시간]
 * 00:42:36
 * AI 사용 여부 X
 * => 처음에서 List 형태로 add(value, id) 함수를 사용해서 관리했으나, 연쇄적으로 삭제되는 경우 id가 틀어지는 현상이 있었음
 * => LinkedList형태로 관리 (CodeTree 가로등 설치) 키워드로 힌트를 제공받아 풀이함
 */
public class 표편집_박재환 {
    public static void main(String[] args) {
        int n = 8;
        int k = 2;
        String[] cmd = {"D 2", "C", "U 3", "C", "D 4", "C", "U 2", "Z", "Z"};
        Solution sol = new Solution();
        System.out.println(sol.solution(n, k, cmd));
    }
}

class Solution {

    class Node {
        int id;
        int prev;
        int next;

        Node(int id, int prev, int next) {
            this.id = id;
            this.prev = prev;
            this.next = next;
        }
    }

    int n, k;
    int[] prev;
    int[] next;
    boolean[] deleted;
    Deque<Node> history;
    public String solution(int n, int k, String[] cmd) {
        set(n, k);

        for (String s : cmd) {
            char type = s.charAt(0);

            if (type == 'U') {
                up(Integer.parseInt(s.substring(2)));
            } else if (type == 'D') {
                down(Integer.parseInt(s.substring(2)));
            } else if (type == 'C') {
                cut();
            } else if (type == 'Z') {
                rollBack();
            }
        }

        return getResult();
    }

    void set(int n, int k) {
        this.n = n;
        this.k = k;
        this.prev = new int[n];
        this.next = new int[n];
        this.deleted = new boolean[n];
        this.history = new ArrayDeque<>();

        for (int i = 0; i < n; i++) {
            prev[i] = i - 1;
            next[i] = i + 1;
        }
        next[n - 1] = -1;
    }

    void up(int count) {
        while (count-- > 0) k = prev[k];
    }

    void down(int count) {
        while (count-- > 0) k = next[k];
    }

    void cut() {
        int removed = k;
        int prevNode = prev[removed];
        int nextNode = next[removed];

        history.offerLast(new Node(removed, prevNode, nextNode));
        deleted[removed] = true;

        if (prevNode != -1) next[prevNode] = nextNode;
        if (nextNode != -1) prev[nextNode] = prevNode;

        k = (nextNode != -1) ? nextNode : prevNode;
    }

    void rollBack() {
        if (history.isEmpty()) return;

        Node restored = history.pollLast();
        deleted[restored.id] = false;

        if (restored.prev != -1) next[restored.prev] = restored.id;
        if (restored.next != -1) prev[restored.next] = restored.id;

        prev[restored.id] = restored.prev;
        next[restored.id] = restored.next;
    }

    String getResult() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < n; i++) sb.append(deleted[i] ? 'X' : 'O');
        return sb.toString();
    }
}
