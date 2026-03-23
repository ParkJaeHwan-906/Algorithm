package mar.week3.codetree;

import java.util.*;
import java.io.*;

public class 산타의선물공장_박재환 {
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
	static final int POLL = 200;
	static final int DEL = 300;
	static final int FIND = 400;
	static final int BROKEN = 500;
	
	static StringTokenizer st;
	static void init() throws IOException {
		int q = Integer.parseInt(br.readLine().trim());
		while(q-- > 0) {
			st = new StringTokenizer(br.readLine().trim());
			int cmd = Integer.parseInt(st.nextToken());
			
			if(cmd == SET) { set(); }
			else if(cmd == POLL) {
				long result = poll();
				sb.append(result).append('\n');
			}
			else if(cmd == DEL) {
				int result = del();
				sb.append(result).append('\n');
			}
			else if(cmd == FIND) {
				int result = find();
				sb.append(result).append('\n');
			}
			else if(cmd == BROKEN) {
				int result = broken();
				sb.append(result).append('\n');
			}
            printBelt();
		}
	}
	static class Node {
		int id;
		int w;
		Node prev;
		Node next;

		Node(int id, int w) {
			this.id = id;
			this.w = w;
			this.prev = null;
			this.next= null;
		}
		
		void init() {
			this.prev = null;
			this.next= null;
		}
	}
	static class Belt {
		Node head;
		Node tail;
        int size;
		boolean broken;
		
		Belt() {
			this.head = null;
			this.tail = null;
            this.size = 0;
			this.broken = false;
		}
		/**
 		 * 벨트의 뒷쪽에 물건을 추가하는 경우
 		 * head 가 null -> 벨트에 물건이 없음
		 */
		void addLast(Node node) {
			if(head == null) {
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
		/**
		 * 벨트의 맨 앞 원소를 반환 
		 * - 벨트가 비어있는 경우 
		 * - 벨트에 하나의 워소만 있는 경우
		 * - 여러 원소가 있는 경우 
		 */
		Node pollFirst() {
			if(head == null) return null;
			Node node = head;
			if(head.next == null) {
				head = null;
				tail = null;
			} else {
				Node nextHead = head.next;
				nextHead.prev = null;
				head = nextHead;
			}
			node.init();
            size--;
			return node;
		}
        Node pollLast() {
            if(head == null) return null;
            Node node = tail;
            if(tail.prev == null) {
                head = null;
                tail = null;
            } else {
                Node newTail = tail.prev;
                newTail.next = null;
                tail = newTail;
            }
            node.init();
            size--;
            return node;
        }
        void removeNode(Node node) {
            if(node == head) pollFirst();
            else if(node == tail) pollLast();
            else {
                if(node.prev != null) node.prev.next = node.next;
                if(node.next != null) node.next.prev = node.prev;

                node.init();
                size--;
            }
        }
	}
	static int n, m;
	static Node[] nodes;
	static Map<Integer, Node> idToNode;
	static Map<Integer, Integer> idToId;
	static Belt[] belts;
	static void set() {
		n = Integer.parseInt(st.nextToken());		// 선물의 개수
		m = Integer.parseInt(st.nextToken());		// 벨트의 개수
		
		int[] ids = new int[n];
		int[] ws = new int[n];
		for(int i=0; i<n; i++) ids[i] = Integer.parseInt(st.nextToken());
		for(int i=0; i<n; i++) ws[i] = Integer.parseInt(st.nextToken());
		
		nodes = new Node[n];
        idToNode = new HashMap<>();
        idToId = new HashMap<>();
		for(int i=0; i<n; i++) {
			Node node = new Node(ids[i], ws[i]);
			nodes[i] = node;
		}
		
		belts = new Belt[m];
		for(int i=0; i<m; i++) belts[i] = new Belt();
		
		for(int bId=0; bId<m; bId++) {
			for(int nId = bId * (n / m); nId < bId * (n / m) + (n / m); nId++) {
				belts[bId].addLast(nodes[nId]);
                idToNode.put(nodes[nId].id, nodes[nId]);
                idToId.put(nodes[nId].id, bId);
			}
		}
	}
	static long poll() {
		int w = Integer.parseInt(st.nextToken());
        long  sum = 0;
		for(int bId = 0; bId < m; bId++) {
			Belt b = belts[bId];
            if(b.broken) continue;
			if(b.head == null) continue;
			Node node = b.pollFirst();
			if(node.w <= w) {
				sum += node.w;
                idToNode.remove(node.id);
                idToId.remove(node.id);
			}
			else b.addLast(node);
		}
		return sum;
	}
	static int del() {
		int nId = Integer.parseInt(st.nextToken());
        Node cur = idToNode.get(nId);
		if(cur == null) return -1;

        Belt b = belts[idToId.get(nId)];

        b.removeNode(cur);

        cur.init();
        idToNode.remove(cur.id);
        idToId.remove(cur.id);
		return nId;
	}
	static int find() {
		int nId = Integer.parseInt(st.nextToken());
        Node cur = idToNode.get(nId);
        if(cur == null) return -1;

		Belt b = belts[idToId.get(nId)];
        if (b.head != cur) {
            Node oldHead = b.head;
            Node oldTail = b.tail;
            Node newHead = cur;
            Node newTail = cur.prev;

            // 앞부분 끊기
            newHead.prev = null;
            newTail.next = null;

            // 뒤에 붙이기
            oldTail.next = oldHead;
            oldHead.prev = oldTail;

            // head / tail 갱신
            b.head = newHead;
            b.tail = newTail;
        }

		return idToId.get(nId) + 1;			// 0-based 를 1-based 로 보정
	}
	static int broken() {
		int bId = Integer.parseInt(st.nextToken()) - 1;
		if(belts[bId].broken) return -1;

        Belt b = belts[bId];
        b.broken = true;

        for (int i = 1; i < m; i++) {
            int nb = (bId + i) % m;
            if(belts[nb].broken) continue;
            Belt target = belts[nb];
            while(b.size > 0) {
                Node node = b.pollFirst();
                target.addLast(node);
                idToId.put(node.id, nb);
            }
            break;
        }

        return bId + 1;
	}
    //------------------------------
    static void printBelt() {
        for(int i=0; i<m; i++ ) {
            System.out.printf("[BELT] %d\n", (i+1));
            Node cur= belts[i].head;
            while(cur != null) {
                System.out.printf("[NODE] id : %d, w : %d\n", cur.id, cur.w);
                cur = cur.next;
            }
            System.out.println();
        }
    }
}
