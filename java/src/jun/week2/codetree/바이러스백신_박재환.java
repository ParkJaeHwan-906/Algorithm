package jun.week2.codetree;

import java.util.*;
import java.io.*;

/**
 * [풀이 시간]
 * 00:29:40
 * AI 사용 여부 X
 */
public class 바이러스백신_박재환 {
	public static void main(String[] args) throws IOException {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		init(br);
		br.close();
	}
	
	static class Hospital {
		int x, y;
		
		Hospital(int x, int y) {
			this.x = x;
			this.y = y;
		}
	}
	
	static int n, m;
	static int[][] board;
	static List<Hospital>hospitals;
	static int viruses;
	static void init(BufferedReader br) throws IOException {
		StringTokenizer st;
		
		st = new StringTokenizer(br.readLine().trim());
		n = Integer.parseInt(st.nextToken());
		m = Integer.parseInt(st.nextToken());
		
		board = new int[n][n];
		hospitals = new ArrayList<>();
		viruses = 0;
		for(int x = 0; x < n; x++) {
			st = new StringTokenizer(br.readLine().trim());
			for(int y = 0; y < n; y++) {
				/**
				 * 0 : 바이러스
				 * 1 : 벽
				 * 2 : 병원
				 */
				board[x][y] = Integer.parseInt(st.nextToken());
				if(board[x][y] == 2) hospitals.add(new Hospital(x, y));
				else if(board[x][y] == 0) viruses++;
			}
		}
		
		System.out.println(solution());
	}
	
	static int minTime;
	static int solution() {
		if(viruses == 0) return 0;
		
		minTime = Integer.MAX_VALUE;
		findCombi(0, 0, new int[m]);
		return minTime == Integer.MAX_VALUE ? -1 : minTime;
	}
	
	static void findCombi(int hid, int id, int[] combi) {
		if(id == m) {		// m 개의 병원을 선택 완료했을 때
			// 시뮬레이션
			int time = spreadVaccine(combi);
			minTime = Math.min(minTime, time);
			return;
		}
		
		if(hid == hospitals.size()) return;
		
		combi[id] = hid;
		findCombi(hid + 1, id + 1, combi);
		findCombi(hid + 1, id, combi);
	}
	
	static final int[] dx = {0, 1, 0, -1};
	static final int[] dy = {1, 0, -1, 0};
	static int spreadVaccine(int[] combi) {
		/**
		 * 바이러스는 모두 제거했지만 병원을 방문하지 않은 경우
		 */
		int time = 0;
		
		Queue<int[]> q = new ArrayDeque<>();
		boolean[][] visited = new boolean[n][n];
		
		for(int hid : combi) {
			Hospital hospital = hospitals.get(hid);
			q.offer(new int[] {hospital.x, hospital.y});
			visited[hospital.x][hospital.y] = true;
		}
		
		int removed = 0;
		while(!q.isEmpty()) {
			if(++time > minTime) break;		
			Queue<int[]> temp = new ArrayDeque<>();
			while(!q.isEmpty()) {
				int[] cur = q.poll();
				
				for(int dir = 0; dir < 4; dir++) {
					int nx = cur[0] + dx[dir];
					int ny = cur[1] + dy[dir];
					
					if(isNotBoard(nx, ny)) continue;
					if(visited[nx][ny]) continue;
					if(board[nx][ny] == 1) continue;
					
					temp.offer(new int[] {nx, ny});
					visited[nx][ny] = true;
					
					if(board[nx][ny] == 0) removed++;
				}
			}
			
			if(removed == viruses) return time;
			q = temp;
		}
		 
		return Integer.MAX_VALUE;
	}
	
	static boolean isNotBoard(int x, int y) {
		return x < 0 || y < 0 || x >= n || y >= n;
	}
}
