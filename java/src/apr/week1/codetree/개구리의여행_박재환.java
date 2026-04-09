package apr.week1.codetree;

import java.util.*;
import java.io.*;

public class 개구리의여행_박재환 {
	static BufferedReader br;
	static StringBuilder sb;
	public static void main(String[] args) throws IOException {
		br = new BufferedReader(new InputStreamReader(System.in));
		sb = new StringBuilder();
		init();
		br.close();
		System.out.println(sb);
	}
	static StringTokenizer st;
	static int n;
	static char[][] board;
	static void init() throws IOException {
		n = Integer.parseInt(br.readLine().trim());
		// 격자 정보
		board = new char[n][n];
		for(int x = 0; x < n; x++) {
			String line = br.readLine().trim();
			for(int y = 0; y < n; y++) {
				board[x][y] = line.charAt(y);
			}
		}
		// 여행 정보
		int q = Integer.parseInt(br.readLine().trim());
		for(int i = 0; i < q; i++) {
			st = new StringTokenizer(br.readLine().trim());
			int x1 = Integer.parseInt(st.nextToken()) - 1;
			int y1 = Integer.parseInt(st.nextToken()) - 1;
			int x2 = Integer.parseInt(st.nextToken()) - 1;
			int y2 = Integer.parseInt(st.nextToken()) - 1;
			int result = solution(x1, y1, x2, y2);
			sb.append(result).append('\n');
		}
	}
	static class State implements Comparable<State> {
		int x, y;
		int jump;
		int time;
		
		State(int x, int y, int jump, int time) {
			this.x = x;
			this.y = y;
			this.jump = jump;
			this.time = time;
		}
		public int compareTo(State o) {
			return Integer.compare(this.time, o.time);
		}
	}
	static final int INF = 987654321;
	static final int[] dx = {0, 1, 0, -1};
	static final int[] dy = {1, 0, -1, 0};
	static int solution(int sx, int sy, int ex, int ey) {
		PriorityQueue<State> pq = new PriorityQueue<>();
		int[][][] visited = new int[n][n][6];
		for(int x = 0; x < n; x++) {
			for(int y = 0; y < n; y++) Arrays.fill(visited[x][y], INF);
		}
		
		// 초기 설정 
		State start = new State(sx, sy, 1, 0);		// x, y, jump, time
		pq.offer(start);
		visited[sx][sy][1] = 0;
		
		while(!pq.isEmpty()) {
			State cur = pq.poll();
			if(cur.x == ex && cur.y == ey) return cur.time;
			if(visited[cur.x][cur.y][cur.jump] < cur.time) continue;
			/**
			 * 현재 위치에서 할 수 있는 행동
			 * 1. 이동
			 * 2. 점프력 증가
			 * 3. 점프력 감소
			 */
			
			// 1. 이동 
			for(int dir = 0; dir < 4; dir++) {
				State next = move(cur, dir);
				if(next == null) continue;
				// 이동이 가능하다면, 최적 루트인지 확인
				if(visited[next.x][next.y][next.jump] > next.time) {
					visited[next.x][next.y][next.jump] = next.time;
					pq.offer(next);
				}
			}
			
			// 2. 점프력 증가
			if(cur.jump < 5) {
				State increaseJumpState = increaseJump(cur);
				if(visited[increaseJumpState.x][increaseJumpState.y][increaseJumpState.jump] > increaseJumpState.time) {
					visited[increaseJumpState.x][increaseJumpState.y][increaseJumpState.jump] = increaseJumpState.time;
					pq.offer(increaseJumpState);
				}
			}
			
			// 3. 점프력 감소
			for(int jump = 1; jump < cur.jump; jump++) {
				State decreaseJumpState = decreaseJump(cur, jump);
				if(visited[decreaseJumpState.x][decreaseJumpState.y][decreaseJumpState.jump] > decreaseJumpState.time) {
					visited[decreaseJumpState.x][decreaseJumpState.y][decreaseJumpState.jump] = decreaseJumpState.time;
					pq.offer(decreaseJumpState);
				}
			}
		}
		int minTime = getMin(visited[ex][ey]);
		return minTime == INF ? -1 : minTime;
	}
	static State move(State cur, int dir) {
		/**
		 * 현재 방향으로 이동이 가능한지 확인 
		 * 1. 도착지가 . 인지
		 * 2. 이동경로에 # 는 없는지
		 */
		int x = cur.x;
		int y = cur.y;
		int jump = cur.jump;
		
		for(int i = 0; i < jump; i++) {
			int nx = x + dx[dir];
			int ny = y + dy[dir];
			if(isNotBoard(nx, ny)) return null;		// 격자 밖으로 나가는 경우
			if(board[nx][ny] == '#') return null;	// 천적이 사는 경우
			
			x = nx;
			y = ny;
		}
		
		// 경로상 문제가 없다면 
		return board[x][y] == '.' ? new State(x, y, jump, cur.time + 1) : null;	
	}
	static State increaseJump(State cur) {
		int jump = cur.jump;
		int time = cur.time;
		
		jump++;		// 점프력을 1 증가
		time += (jump * jump);
		
		return new State(cur.x, cur.y, jump, time);
	}
	static State decreaseJump(State cur, int jump) {
		return new State(cur.x, cur.y, jump, cur.time + 1);
	}
	static int getMin(int[] arr) {
		int min = INF;
		for(int i : arr) min = Math.min(i, min);
		return min;
	}
	 
	// ===
	static boolean isNotBoard(int x, int y) {
		return x < 0 || y < 0 || x >= n || y >= n;
	}
}
