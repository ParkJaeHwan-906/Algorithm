package apr.week1.codetree;

import java.util.*;
import java.io.*;

public class 여왕개미_박재환 {
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
	
	static StringTokenizer st;
	static List<Home> homes;
	static void init() throws IOException {
		int q = Integer.parseInt(br.readLine().trim());
		
		while(q-- > 0) {
			st = new StringTokenizer(br.readLine().trim());
			int cmd = Integer.parseInt(st.nextToken());
			
			if(cmd == SET) { set(); }
			else if(cmd == ADD) {
				int x = Integer.parseInt(st.nextToken());
				add(x);
			}
			else if(cmd == DEL) {
				int id = Integer.parseInt(st.nextToken());
				del(id);
			}
			else if(cmd == QUERY) {
				int maxAnt = Integer.parseInt(st.nextToken());
				long result = query(maxAnt);
				sb.append(result).append('\n');
			}
		}
	}
	static class Home {
		int x;
		boolean removed;
		
		Home(int x) {
			this.x = x;
			this.removed = false;
		}
	}
	static void set() {
		homes = new ArrayList<>();
		int n = Integer.parseInt(st.nextToken());
		// 여왕 개미의 집
		Home queen = new Home(0);
		homes.add(queen);
		for(int i = 0; i < n; i++) {
			int x = Integer.parseInt(st.nextToken());
			Home home = new Home(x);
			homes.add(home);
		}
	}
	static void add(int x) {
		Home home = new Home(x);
		homes.add(home);
	}
	static void del(int id) {
		Home home = homes.get(id);
		home.removed = true;
	}
	static long query(int maxAnt) {
		int l = 0;
		int r = homes.get(homes.size() - 1).x;
		long min = Long.MAX_VALUE;
		while(l <= r) {
			int mid = l + (r - l) / 2;
			if(canCheck(mid, maxAnt)) {
				min = Math.min(min, mid);
				r = mid - 1;
			} else {
				l = mid + 1;
			}
		}
		
		return min;
	}
	static boolean canCheck(int mid, int r) {
		int ant = 0;
		int last = -1_000_000_007;
		
		for(int i = 1; i < homes.size(); i++) {
			Home cur = homes.get(i);
			if(cur.removed) continue;		// 철거된 집은 탐색할 필요 없음
			
			// 철거되지 않았다면, 현재 개미가 탐색 가능한지 확인
			if(cur.x - last > mid) {		// 현재 개미로 탐색이 불가하다면
				last = cur.x;
				if(++ant > r) return false;
			}
		}
		return ant <= r;
	}
}
