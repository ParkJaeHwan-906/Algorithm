package mar.week4.codetree;

import java.util.*;
import java.io.*;

public class 코드트리등산게임_박재환 {
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
	static final int ADD = 200;
	static final int DEL = 300;
	static final int QUERY = 400;
	
	static class Command {
		int type;
		int id;
		int h;
		int n;
		int[] arr;

		// SET
		Command(int type, int n, int[] arr) {
			this.type = type;
			this.n = n;
			this.arr = arr;
		}
		// ADD / QUERY
		Command(int type, int h) {
			this.type = type;
			if(type == ADD) this.h = h;
			else if(type == QUERY) this.id = h;
		}
		// DEL
		Command(int type) {
			this.type = type;
		}
	}
	
	static StringTokenizer st;
	static Queue<Command> commands; 
	static List<Integer> mountains;
	static void init() throws IOException {
		commands = new ArrayDeque<>();

		mountains = new ArrayList<Integer>();

		int q = Integer.parseInt(br.readLine().trim());
		while(q-- > 0) {
			st = new StringTokenizer(br.readLine().trim());
			int cmd = Integer.parseInt(st.nextToken());
				
			if(cmd == SET) {
				n = Integer.parseInt(st.nextToken());
				int[] arr = new int[n];
				for(int i=0; i<n; i++) {
					arr[i] = Integer.parseInt(st.nextToken());
					mountains.add(arr[i]);
				}
				Command command = new Command(cmd, n, arr);
				commands.offer(command);
			}
			else if(cmd == ADD) {
				int h = Integer.parseInt(st.nextToken());
				mountains.add(h);
				Command command = new Command(cmd, h);
				commands.offer(command);
			}
			else if(cmd == DEL) {
				Command command = new Command(cmd);
				commands.offer(command);
			}
			else if(cmd == QUERY) {
				int id = Integer.parseInt(st.nextToken());
				Command command = new Command(cmd, id);
				commands.offer(command);
			}
		}
		

		solution();
	}
	static void solution() {
		while(!commands.isEmpty()) {
			Command cmd = commands.poll();
			
			if(cmd.type == SET) { set(cmd); }
			else if(cmd.type == ADD) { add(cmd); }
			else if(cmd.type == DEL) { del(); }
			else if(cmd.type == QUERY) {
				int result = query(cmd);
				sb.append(result).append('\n');
			}
		}
	}
	
	
	static int n;
	static Node[] tree;
	static Deque<Integer>[] history;
	static List<Integer> locLen;
	static void set(Command command) {
		flat();
		history = new Deque[order+1];
		for(int i=0; i<order+1; i++) history[i] = new ArrayDeque<Integer>();
		
		tree = new Node[4 * order];
		for(int i=0; i<tree.length; i++) tree[i] = new Node();
		
		// 세그먼트 트리를 사용해서 LIS 생성
		locLen = new ArrayList<Integer>();
		n = command.n;
		int[] arr = command.arr;
		mountains.clear();		// 새롭게 생성될때마다 현 상태 기록
		mountains.add(-1);
		locLen.add(0);
		for(int i : arr) {
			int id = heightToId.get(i);
			
			Node prev = query(1, 1, order, 1, id-1);
			Node next = new Node(prev.lis+1, Math.max(id,  prev.max));
			update(1, 1, order, id, id, next);
			history[id].push(next.lis);
			mountains.add(i);
			locLen.add(next.lis);
		}
	}
	/**
	 * 삽입 / 삭제는 항상 오른쪽 끝에서 일어난다
	 */
	static void add(Command command) {
		int h = command.h;
		int id = heightToId.get(h);
		
		Node prev = query(1, 1, order, 1, id-1);
		Node next = new Node(prev.lis+1, Math.max(id,  prev.max));
		update(1, 1, order, id, id, next);
		history[id].push(next.lis);
		mountains.add(h);
		locLen.add(next.lis);
	}
	static void del() {
		locLen.remove(locLen.size()-1);
		int removeH = mountains.remove(mountains.size()-1);
		int id = heightToId.get(removeH);
		
		history[id].pop();
		int newLis = history[id].isEmpty() ? 0 : history[id].peek();
		Node node = new Node(newLis, newLis == 0 ? -1 : id);
		update(1, 1, order, id, id, node);
	}
	static final int SCORE = 1_000_000;
	static int query(Command command) {
		/**
		 * 케이블카가 있는 산까지 LIS
		 * +
		 * 전체 LIS
		 */
		int id = command.id;
		int range1 = locLen.get(id);
		Node last = tree[1];
		int range2 = last.lis;
		return (range1 + range2 - 1) * SCORE + idToHeight.get(last.max);
	}
	// =========================================
	/**
	 * 세그먼트 트리 접근 
	 * - 총 입력되는 산의 개수를 미리 파악
	 */
	static int order;
	static Map<Integer, Integer> heightToId;
	static Map<Integer, Integer> idToHeight;
	static void flat() {
		List<Integer> temp = new ArrayList<>();
		for(int i=0; i<mountains.size(); i++) temp.add(mountains.get(i));
		temp.sort(Integer::compare);
		
		order = 0;
		heightToId = new HashMap<>();
		idToHeight = new HashMap<>();
		for(int i : temp) {
			if(heightToId.get(i) == null) {
				heightToId.put(i, ++order);
				idToHeight.put(order, i);
			}
		}
	}
	
	static class Node {
		int lis;
		int max;
		
		Node() {
			this.lis = 0;
			this.max = -1;
		}
		
		Node(int lis, int max) {
			this.lis = lis;
			this.max = max;
		}
	}
	
	static final Node DUMMY = new Node(0, -1);
	
	static Node query(int id, int l, int r, int s, int e) {
		if(r < s || l > e) return DUMMY;
		if(l >= s && r <= e) return tree[id];
		
		int mid = l + (r - l)/2;
		Node left = query(2*id, l, mid, s, e);
		Node right = query(2*id+1, mid+1, r, s, e);
		return best(left, right);
	}
	
	static void update(int id, int l, int r, int s, int e, Node node) {
		if(r < s || l > e) return;
		if(l >= s && r <= e) {
			tree[id] = node;
			return;
		}
		
		int mid = l + (r - l)/2;
		update(2*id, l, mid, s, e, node);
		update(2*id+1, mid+1, r, s, e, node);
		
		tree[id] = best(tree[2*id], tree[2*id+1]);
	}
	
	static Node best(Node a, Node b) {
		if(a.lis > b.lis) return a;
		if(a.lis < b.lis) return b;
		
		return a.max > b.max ? a : b;
	}
}
