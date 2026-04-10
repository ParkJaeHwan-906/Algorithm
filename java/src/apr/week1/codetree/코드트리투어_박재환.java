package apr.week1.codetree;

import java.util.*;
import java.io.*;

public class 코드트리투어_박재환 {
	static BufferedReader br;
	static StringBuilder sb;
	public static void main(String[] args) throws IOException {
		br = new BufferedReader(new InputStreamReader(System.in));
		sb = new StringBuilder();
		init();
		br.close();
		System.out.println(sb);
	}
	static final int BUILD = 100;
	static final int CREATE = 200;
	static final int DELETE = 300;
	static final int SELL = 400;
	static final int CHANGE = 500;
	
	static class Travel implements Comparable<Travel> {
		int id;
		int revenue;
		int dest;
		int cost;
		boolean removed;
		
		Travel(int id, int revenue, int dest, int cost) {
			this.id = id;
			this.revenue = revenue;
			this.dest = dest;
			this.cost = cost;
			
			this.removed = false;
		}
		
		public int compareTo(Travel o) {
			int aProfit = this.revenue - this.cost;
			int bProfit = o.revenue - o.cost;
			if(aProfit != bProfit) return Integer.compare(bProfit, aProfit);
			return Integer.compare(this.id, o.id);
		}
	}
	
	static StringTokenizer st;
	static int n, m;
	static int[] dist;
	static List<int[]>[] connections;
	static PriorityQueue<Travel> travels;
	static Map<Integer, Travel> idToTravels;
	static void init() throws IOException {
		travels = new PriorityQueue<>();
		idToTravels = new HashMap<>();
		int q = Integer.parseInt(br.readLine().trim());
		
		while(q-- > 0) {
			st = new StringTokenizer(br.readLine().trim());
			int cmd = Integer.parseInt(st.nextToken());
			
			if(cmd == BUILD) { build(); }
			else if(cmd == CREATE) { create(); }
			else if(cmd == DELETE) { 
				int id = Integer.parseInt(st.nextToken());
				delete(id); 
			}
			else if(cmd == SELL) { 
				int result = sell(); 
				delete(result);
				sb.append(result).append('\n');
			}
			else if(cmd == CHANGE) {
				int start = Integer.parseInt(st.nextToken());
				change(start);
			}
		}
	}
	static void build() {
		n = Integer.parseInt(st.nextToken());
		m = Integer.parseInt(st.nextToken());
		
		connections = new List[n];		// 0 ~ n - 1
		for(int i = 0; i < n; i++) connections[i] = new ArrayList<>();
		
		for(int i = 0; i < m; i++) {
			int from = Integer.parseInt(st.nextToken());
			int to = Integer.parseInt(st.nextToken());
			int weight = Integer.parseInt(st.nextToken());
			connections[from].add(new int[] {to, weight});
			connections[to].add(new int[] {from, weight});
		}
		// 초기에는 출발지가 0
		findShortestRoute(0);
	}
	static void create() {
		int id = Integer.parseInt(st.nextToken());
		int revenue = Integer.parseInt(st.nextToken());
		int dest = Integer.parseInt(st.nextToken());
		int cost = dist[dest];
		
		Travel travel = new Travel(id, revenue, dest, cost);
		idToTravels.put(id, travel);
		travels.offer(travel);
	}
	static void delete(int id) {
		idToTravels.remove(id);
	}
	static int sell() {
		Travel taret = null;
		while(!travels.isEmpty()) {
			Travel travel = travels.peek();
			if(idToTravels.get(travel.id) == null) {
				travels.poll();
				continue;
			}
			
			if(travel.cost == INF || (travel.revenue < travel.cost)) return -1;
			
			// 판매할 수 있는 상품
			taret = travels.poll();
			break;
		}
		return taret == null ? -1 : taret.id;
	}
	static void change(int start) {
		findShortestRoute(start);
		
		PriorityQueue<Travel> temp = new PriorityQueue<>();
		while(!travels.isEmpty()) {
			Travel travel = travels.poll();
			int newCost = dist[travel.dest];
			travel.cost = newCost;
			temp.offer(travel);
		}
		travels = temp;
	}
	// ===
	static final int INF = 987654321;
	static void findShortestRoute(int start) {
		PriorityQueue<int[]> pq = new PriorityQueue<int[]>((a, b) -> Integer.compare(a[1], b[1]));
		dist = new int[n];
		Arrays.fill(dist, INF);
		
		pq.offer(new int[] {start, 0});
		dist[start] = 0;
		
		while(!pq.isEmpty()) {
			int[] cur = pq.poll();
			int from = cur[0];
			int accCost = cur[1];
			
			if(dist[from] < accCost) continue;
			
			for(int[] connection : connections[from]) {
				int to = connection[0];
				int cost = connection[1];
				
				if(dist[to] > dist[from] + cost) {
					dist[to] = dist[from] + cost;
					pq.offer(new int[] {to, dist[to]});
				}
			}
		}
	}
}
