package sep.week4.programmers.표편집_박재환;

import java.util.*;

public class 표편집_박재환 {
    public static void main(String[] args) {
        int n = 8;
        int k = 2;
        String[] cmd = {"D 2","C","U 3","C","D 4","C","U 2","Z","Z"};
        Solution sol = new Solution();
        System.out.print(sol.solution(n,k,cmd));
    }
}

class Solution {
    class Node {
        int id;
        Node prev;
        Node next;
        boolean deleted;
        Node(int id, Node prev, Node next) {
            this.id = id;
            this.prev = prev;
            this.next = next;
            this.deleted = false;
        }
    }

    Node[] nodes;
    Deque<Integer> logs;
    int cusor;
    public String solution(int n, int k, String[] cmds) {
        set(n, k);
        for(String cmd : cmds) {
            char type = cmd.charAt(0);
            if(type == 'U') {
                int x = Integer.parseInt(cmd.substring(2));
                up(x);
            }
            else if(type == 'D') {
                int x = Integer.parseInt(cmd.substring(2));
                down(x);
            }
            else if(type == 'C') {
                delete();
            }
            else if(type == 'Z') {
                rollBack();
            }
        }
        StringBuilder sb = new StringBuilder();
        for(Node node : nodes) {
            if(node.deleted) {
                sb.append('X');
            } else {
                sb.append('O');
            }
        }
        return sb.toString();
    }
    void set(int n, int k) {
        cusor = k;
        nodes = new Node[n];
        logs = new ArrayDeque<>();
        for(int i = 0; i < n; i++) {
            nodes[i] = new Node(i, null, null);
            if(i == 0) {
                continue;
            }
            nodes[i].prev = nodes[i - 1];
            nodes[i - 1].next = nodes[i];
        }
    }
    void up(int x) {
        Node cur = nodes[cusor];
        while(x-- > 0) {
            cur = cur.prev;
        }
        cusor = cur.id;
    }
    void down(int x) {
        Node cur = nodes[cusor];
        while(x-- > 0) {
            cur = cur.next;
        }
        cusor = cur.id;
    }
    void delete() {
        Node cur = nodes[cusor];
        Node prev = cur.prev;
        Node next = cur.next;
        if(prev != null) {
            prev.next = next;
        }
        if(next != null) {
            next.prev = prev;
        }
        if(next == null) {
            cusor = prev.id;
        } else {
            cusor = next.id;
        }
        cur.deleted = true;
        logs.offerLast(cur.id);
    }
    void rollBack() {
        int id = logs.pollLast();
        Node cur = nodes[id];
        Node prev = cur.prev;
        Node next = cur.next;
        if(prev != null) {
            prev.next = cur;
        }
        if(next != null) {
            next.prev = cur;
        }
        cur.deleted = false;
    }
}