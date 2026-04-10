package apr.week1.codetree;

import java.util.*;
import java.io.*;

public class 색깔트리_박재환 {
	static BufferedReader br;
	static StringBuilder sb;
	public static void main(String[] args) throws IOException {
		br = new BufferedReader(new InputStreamReader(System.in));
		sb = new StringBuilder();
		init();
		br.close();
		System.out.println(sb);
	}
	static final int ADD = 100;
	static final int CHANGE = 200;
	static final int QUERY = 300;
	static final int SUM = 400;
	
	static StringTokenizer st;
	static Map<Integer, Node> idToNode;
	static void init() throws IOException {
		idToNode = new HashMap<>();
		
		int q = Integer.parseInt(br.readLine().trim());
		while(q-- > 0) {
			st = new StringTokenizer(br.readLine().trim());
			int cmd = Integer.parseInt(st.nextToken());
			
			if(cmd == ADD) {
				int id = Integer.parseInt(st.nextToken());
				int pId = Integer.parseInt(st.nextToken());
				int color = Integer.parseInt(st.nextToken());
				int maxDepth = Integer.parseInt(st.nextToken());
				add(id, pId, color, maxDepth);
				//printNode();
			}
			else if(cmd == CHANGE) {
				int id = Integer.parseInt(st.nextToken());
				int color= Integer.parseInt(st.nextToken());
				change(id, color);
			}
			else if(cmd == QUERY) {
				int id = Integer.parseInt(st.nextToken());
				int result = query(id);
				sb.append(result).append('\n');
			}
			else if(cmd == SUM) {
				long result = sum();
				sb.append(result).append('\n');
			}
		}
	}
	static class Node {
		int id;
		
		int pId;
		Node parent;
		
		int color;
		int colorMask;
		
		int maxDepth;
		
		List<Node> childs;
		
		Node(int id, int pId, Node parent, int color, int maxDepth) {
			this.id = id;
			
			this.pId = pId;
			this.parent = parent;
			
			this.color = color;
			this.colorMask = 1 << color;
			
			this.maxDepth = maxDepth;
			
			this.childs = new ArrayList<>();
		}
	}
	/**
	 * [노드 추가]
	 */
	static void add(int id, int pId, int color, int maxDepth) {
		Node parent = idToNode.get(pId);
		if(parent != null && !canAdd(parent)) return;		// 추가 못함
		// 추가 가능
		Node node = new Node(id, pId, parent, color, maxDepth);
		idToNode.put(id, node);
		if(parent == null) return;		// 루트 노드인 경우
		parent.childs.add(node);
		updateColorMask(parent, color);
	}
	static boolean canAdd(Node cur) {
		int depth = 2;		// 새로 생긴 노드와, 현재 자신 노드 포함 길이
		while(cur != null) {
			if(depth > cur.maxDepth) {
				return false;
			}
			depth++;
			cur = cur.parent;
		}
		return true;
	}
	static void updateColorMask(Node cur, int color) {
		while(cur != null) {
			int prev = cur.colorMask;
			cur.colorMask |= (1 << color);
			if(prev == cur.colorMask) break;
			cur = cur.parent;
		}
	}
	/** 
	 * [색깔 변경] : O(n)
	 */
	static void change(int id, int color) {
		Node cur = idToNode.get(id);
		
		changeSubTree(cur, color);
		changeParents(cur.parent, color);
	} 
	static void changeSubTree(Node cur, int color) {
		cur.color = color;
		int colorMask = 1 << color;
		
		for(Node next : cur.childs) {
			changeSubTree(next, color);
		}
		
		cur.colorMask = colorMask;
	}
	static void changeParents(Node cur, int color) {
		while(cur != null) {
			int colorMask = 1 << cur.color;
			
			for(Node chlid : cur.childs) {
				colorMask |= chlid.colorMask;
			}
			
			
			if(colorMask == cur.colorMask) break;
			
			cur.colorMask = colorMask;
			cur = cur.parent;
		}
	}
	/**
	 * [색쌀 조회] : O(1)
	 */
	static int query(int id) {
		Node node = idToNode.get(id);
		return node.color;
	}
	/**
	 * [점수 조회]
	 */
	static long sum() {
		long totalValue = 0;
		for(Node cur : idToNode.values()) {
			int value = Integer.bitCount(cur.colorMask);
			totalValue += (value * value);
		}
		return totalValue;
	}
	
	// ===
	static void printNode() {
		for(Map.Entry<Integer, Node> entry : idToNode.entrySet()) {
			Node cur = entry.getValue();
			System.out.printf("[%d] maxDepth : %d, curDepth : %d\n", entry.getKey(), cur.maxDepth);
			System.out.print("[Child]");
			for(Node n : cur.childs) System.out.print(n.id + " ");
			System.out.println();
		}
		System.out.println();
	}
}
