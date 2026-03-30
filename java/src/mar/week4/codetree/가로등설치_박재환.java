package mar.week4.codetree;

import java.util.*;
import java.io.*;

public class 가로등설치_박재환 {
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
	/**
	 * 가로등 사이의 거리를 기준으로 가로등을 설치한다.
	 * 두 가로등 사이에 가로등을 설치한다. -> Double Linked List
	 */
	static StringTokenizer st;
	static void init() throws IOException {
		int q = Integer.parseInt(br.readLine().trim());
		
		while(q-- > 0) {
			st = new StringTokenizer(br.readLine().trim());
			int cmd = Integer.parseInt(st.nextToken());
			if(cmd == SET) { set(); }
			else if(cmd == ADD) { add(); }
			else if(cmd == DEL) { del(); }
			else if(cmd == QUERY) {
				int result = query();
				sb.append(result).append('\n');
			}
//			printInfo();
		}
	}
	static class Lamp {
		int id;
		int loc;
		Lamp prev;
		Lamp next;
		boolean del;
		
		Lamp(int id, int loc) {
			this.id = id;
			this.loc = loc;
			
			this.prev = null;
			this.next = null;
			this.del = false;
		}
		
		void init() {
			this.prev = null;
			this.next = null;
		}
	}
	static class State implements Comparable<State> {
		Lamp a;
		Lamp b;
		int dist;
		
		State(Lamp a, Lamp b, int dist) {
			this.a = a;
			this.b = b;
			this.dist = dist;
		}
		
		public int compareTo(State o) {
			if(this.dist != o.dist) return Integer.compare(o.dist, this.dist);
			return Integer.compare(this.a.loc, o.a.loc);
		}
	}
	static int n, m;
	static int lampId;
	static Lamp first, last;
	static List<Lamp> lamps;
	static PriorityQueue<State> pq;
	static void set() {
		n = Integer.parseInt(st.nextToken());
		m = Integer.parseInt(st.nextToken());
		
		// 1-based 로 시작
		pq = new PriorityQueue<>();
		lamps = new ArrayList<>();
		lamps.add(null);
		
		lampId = 0;
		for(int i=1; i<m+1; i++) {
			int loc = Integer.parseInt(st.nextToken());
			Lamp lamp = new Lamp(++lampId, loc);
			if(i == 1) {
				lamp.prev = null;
			} else {
				Lamp prev = lamps.get(i-1);
				lamp.prev = prev;
				prev.next = lamp;
				
				int dist = lamp.loc - prev.loc;
				State state = new State(prev, lamp, dist);
				pq.offer(state);
			}
			lamps.add(lamp);
		}
		
		first = lamps.get(1);
		last = lamps.get(lampId);
	}
	
	static void add() {
		/**
		 * 인접한 가로등 중 가장 거리가 먼, 좌쵸 값이 작은 가로등 쌍의 위치 사이에 추가
		 */
		while(!pq.isEmpty()) {
			/**
			 * 유효성 검사
			 * 해당 쌍이 유효한 쌍인지
			 */
			State state = pq.peek();
			Lamp a = state.a;
			Lamp b = state.b;
			
			if(a.next == b && b.prev == a && !a.del && !b.del) break;
			
			pq.poll();
		}
		
		State s = pq.poll();
		// 새로운 램프
		int newLoc = (s.a.loc + s.b.loc + 1) / 2;		// 올림처리
		Lamp newL = new Lamp(++lampId, newLoc);
		// 연결 갱신
		newL.prev = s.a;
		newL.next = s.b;
		
		s.a.next = newL;
		s.b.prev = newL;
		// newL 와 a, b 거리 새로 생성
		lamps.add(newL);
		pq.offer(new State(s.a, newL, (newL.loc - s.a.loc)));
		pq.offer(new State(newL, s.b, (s.b.loc - newL.loc)));
	}
	
	static void del() {
		int tId = Integer.parseInt(st.nextToken());
		Lamp lamp = lamps.get(tId);
		lamp.del = true;
		 
		Lamp a = lamp.prev;
		Lamp b = lamp.next;
		if(a != null) a.next = b;
		if(b != null) b.prev = a;
			
		if(a != null && b != null) pq.offer(new State(a, b, (b.loc - a.loc)));
		
		if(lamp == first) first = b;
		if(lamp == last) last = a;
		
		lamp.init();
	}
	
	static int query() {
		int result = 0;
		/**
		 * 가능한 최대 전력 후보
		 * 1. 1 ~ 가장 첫 위치의 가로등
		 * 2. 가장 마지막 위치의 가로등 ~ N
		 * 3. 가장 거리가 먼 가로등 쌍
		 */
		result = Math.max(result, 2 * (first.loc - 1));
		result = Math.max(result, 2 * (n - last.loc));
		while(!pq.isEmpty()) {
			/**
			 * 유효성 검사
			 * 해당 쌍이 유효한 쌍인지
			 */
			State state = pq.peek();
			Lamp a = state.a;
			Lamp b = state.b;
			
			if(a.next == b && b.prev == a && !a.del && !b.del) break;
			
			pq.poll();
		}
		int maxDist= pq.isEmpty() ? 0 : pq.peek().dist;
		result = Math.max(result, maxDist);
		return result;
	}
	// =============================================
	static void printInfo() {
		System.out.println("[가로등]");
		for(int i=1; i<lamps.size(); i++) {
			if(lamps.get(i).del) continue;
			Lamp l = lamps.get(i);
			System.out.printf("id : %d, loc : %d, prev : %d, next : %d\n", l.id, l.loc, 
					l.prev == null ? -1 : l.prev.id, l.next == null ? -1 : l.next.id);
		}
	}
}
