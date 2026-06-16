package jun.week3.codetree;

import java.util.*;
import java.io.*;

/**
 * [풀이 시간]
 * 00:32:02
 * AI 사용 여부 X
 */
public class 개구리의여행_박재환 {
	public static void main(String[] args) throws IOException {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		init(br);
		br.close();
	}
	
	static int n;
	static char[][] board;
	static void init(BufferedReader br) throws IOException {
		StringTokenizer st;
		StringBuilder sb = new StringBuilder();
		
		n = Integer.parseInt(br.readLine().trim());
		board = new char[n][n];
		for(int x = 0; x < n; x++) {
			String line = br.readLine().trim();
			for(int y = 0; y < n; y++) board[x][y] = line.charAt(y);
		}
		
		int q = Integer.parseInt(br.readLine().trim());
		while(q-- > 0) {
			// 0 - based 치환
			st = new StringTokenizer(br.readLine().trim());
			int sx = Integer.parseInt(st.nextToken()) - 1;
			int sy = Integer.parseInt(st.nextToken()) - 1;
			int ex = Integer.parseInt(st.nextToken()) - 1;
			int ey = Integer.parseInt(st.nextToken()) - 1;
			
			int result = solution(sx, sy, ex, ey);
			sb.append(result).append('\n');
		}
		
		System.out.println(sb);
	}
	static final int INF = 50 * 50 + 7;
	static final int[] dx = {0, 1, 0, -1};
	static final int[] dy = {1, 0, -1, 0};
	static int solution(int sx, int sy, int ex, int ey) {
		PriorityQueue<int[]> q = new PriorityQueue<>((a, b) -> Integer.compare(a[3], b[3]));
		int[][][] visited = new int[n][n][6];
		for(int x = 0; x < n; x++) {
			for(int y = 0; y < n; y++) Arrays.fill(visited[x][y], INF);
		}
		
		q.offer(new int[] {sx, sy, 1, 0});		// {x, y, jump, time}
		visited[sx][sy][1] = 0;
		
		while(!q.isEmpty()) {
			int[] cur = q.poll();
			int x = cur[0];
			int y = cur[1];
			int jump = cur[2];
			int time = cur[3];
			
			if(visited[x][y][jump] < time) continue;
			
			// 점프 
			for(int dir = 0; dir < 4; dir++) {
				int nx = x + dx[dir] * jump;
				int ny = y + dy[dir] * jump;
				
				if(isNotBoard(nx, ny)) continue;		// 격자를 벗어나는 경우
				if(board[nx][ny] == 'S') continue;		// 미끄러운 돌인 경우 
				if(isDanger(x, y, dir, jump)) continue;	// 천적이 있는 경우
				
				if(visited[nx][ny][jump] > time + 1) {
					visited[nx][ny][jump] = time + 1;
					q.offer(new int[] {nx, ny, jump, visited[nx][ny][jump]});
				}
			}
			
			// 점프 증가 ( 1만큼만 증가 )
			if(jump < 5) {
				int newJump = jump + 1;
				int extraTime = newJump * newJump;
				
				if(visited[x][y][newJump] > time + extraTime) {
					visited[x][y][newJump] = time + extraTime;
					q.offer(new int[] {x, y, newJump, visited[x][y][newJump]});
				}
			}

			// 점프 감소 ( 1 ~ jump - 1 )
			for(int newJump = 1; newJump < jump; newJump++) {
				if(visited[x][y][newJump] > time + 1) {
					visited[x][y][newJump] = time + 1;
					q.offer(new int[] {x, y, newJump, visited[x][y][newJump]});
				}
			}
		}
		
		return getMinTime(visited[ex][ey]);
	}
	
	static int getMinTime(int[] cand) {
		int min = INF;
		for(int i : cand) min = Math.min(min, i);
		return min == INF ? -1 : min;
	}
	
	static boolean isDanger(int x, int y, int dir, int jump) {
		for(int i = 0; i < jump; i++) {
			int nx = x + dx[dir];
			int ny = y + dy[dir];
			
			if(board[nx][ny] == '#') return true;
			
			x = nx;
			y = ny;
		}
		return false;
	}
	
	static boolean isNotBoard(int x, int y) {
		return x < 0 || y < 0 || x >= n || y >= n;
	}
}
