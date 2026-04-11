package apr.week1.codetree;

import java.util.*;
import java.io.*;

public class AI로봇청소기_박재환 {
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
	static int n, k, l;
	static int[][] board;
	static List<Robot> robots;
	static void init() throws IOException {
		st = new StringTokenizer(br.readLine().trim());
		n = Integer.parseInt(st.nextToken());
		k = Integer.parseInt(st.nextToken());
		l = Integer.parseInt(st.nextToken());
		
		board = new int[n][n];
		for(int x = 0; x < n; x++) {
			st = new StringTokenizer(br.readLine().trim());
			for(int y = 0; y < n; y++) {
				board[x][y] = Integer.parseInt(st.nextToken());
			}
		}
		
		robots = new ArrayList<>();
		for(int i = 0; i < k; i++) {
			st = new StringTokenizer(br.readLine().trim());
			int x = Integer.parseInt(st.nextToken()) - 1;
			int y = Integer.parseInt(st.nextToken()) - 1;
			Robot robot = new Robot(x, y);
			robots.add(robot);
		}
		
		solution();
	}
	
	static class Robot {
		int x, y;
		
		Robot(int x, int y) {
			this.x = x;
			this.y = y;
		}
	}
	static final int[] dx = {0, 1, 0, -1};
	static final int[] dy = {1, 0, -1, 0};
	static void solution() {
		while(l-- > 0) {
			move();
			clean();
			accDust();
			spreadDust();
			int score = getScore();
			sb.append(score).append('\n');
		}
	}
	static boolean[][] robotState;
	static void move() {
		// 현재 로봇의 위치
		robotState = new boolean[n][n];
		for(Robot r : robots) {
			robotState[r.x][r.y] = true; 
		}
		for(Robot r : robots) {
			moveRobot(r);
		}
	}
	static void moveRobot(Robot r) {
		// 현재 칸에 먼지가 있다면, 이동하지 않음 
		if(board[r.x][r.y] > 0) return;
		
		Queue<int[]> q = new ArrayDeque<>();
		boolean[][] visited = new boolean[n][n];
		// 초기 시작 위치
		robotState[r.x][r.y] = false;
		q.offer(new int[] {r.x, r.y, 0});
		visited[r.x][r.y] = true;
		
		int bestX = n + 1;
		int bestY = n + 1;
		int bestD = n * n + 7;
		
		while(!q.isEmpty()) {
			int[] cur = q.poll();
			int x = cur[0];
			int y = cur[1];
			int d = cur[2];
			
			if(d > bestD) continue;		// 가지치기
			if(board[x][y] > 0) {
				if(bestD > d) {
					bestX = x;
					bestY = y;
					bestD = d;
				} else if(bestD == d && (bestX > x || (bestX == x && bestY > y))) {
					bestX = x;
					bestY = y;
				}
				continue;
			}
			
			for(int dir = 0; dir < 4; dir++) {
				int nx = x + dx[dir];
				int ny = y + dy[dir];
				if(isNotBoard(nx, ny)) continue;		// 격자를 벗어나거나
				if(board[nx][ny] == -1) continue;		// 벽이 있거나
				if(visited[nx][ny]) continue;			// 이미 방문했거나
				if(robotState[nx][ny]) continue;		// 다른 청소기가 있거나
				visited[nx][ny] = true;
				q.offer(new int[] {nx, ny, d + 1});
			}
		}
		if(bestX == n + 1 && bestY == n + 1) {
			robotState[r.x][r.y] = true;
			return;
		}
		r.x = bestX;
		r.y = bestY;
		robotState[r.x][r.y] = true; 
	}
	// 오른쪽, 아래쪽, 왼쪽, 위쪽
	static final int[][] rdx = {{-1, 0, 1, 0}, {0, 0, 0, 1}, {-1, 0, 1, 0}, {0, 0, 0, -1}};
	static final int[][] rdy = {{0, 0, 0, 1}, {-1, 0, 1, 0}, {0, 0, 0, -1}, {-1, 0, 1, 0}};
	static void clean() {
		for(Robot r : robots) {
			int dir = dicisionDir(r);
			if(dir == -1) continue;
			cleanDust(dir, r);
		}
	}
	static int dicisionDir(Robot r) {
		int bestDir = -1;
		int bestDust = -1;
		
		for(int dir = 0; dir < 4; dir++) {
			int dust = 0;
			for(int i = 0; i < 4; i++) {
				int nx = r.x + rdx[dir][i];
				int ny = r.y + rdy[dir][i];
				if(isNotBoard(nx, ny)) continue;
				if(board[nx][ny] > 0) dust += Math.min(20, board[nx][ny]);
			}
			
			if(bestDust < dust) {
				bestDir = dir;
				bestDust = dust;
			}
		}
		return bestDir;
	}
	static void cleanDust(int dir, Robot r) {
		for(int i = 0; i < 4; i++) {
			int nx = r.x + rdx[dir][i];
			int ny = r.y + rdy[dir][i];
			if(isNotBoard(nx, ny)) continue;
			if (board[nx][ny] == -1) continue;
			board[nx][ny] = Math.max(0, board[nx][ny] - 20);
		}
	}
	static void accDust() {
		for(int x = 0; x < n; x++) {
			for(int y = 0; y < n; y++) {
				if(board[x][y] > 0) board[x][y] += 5;
			}
		}
	}
	static void spreadDust() {
		int[][] temp = new int[n][n];
		for(int x = 0; x < n; x++) {
			for(int y = 0; y < n; y++) {
				if(board[x][y] == 0) {
					int sum = nearDust(x, y);
					temp[x][y] += sum / 10;
				}
			}
		}
		computeBoard(temp);
	}
	static int nearDust(int x, int y) {
		int sum = 0;
		for(int dir = 0; dir < 4; dir++) {
			int nx = x + dx[dir];
			int ny = y + dy[dir];
			if(isNotBoard(nx, ny)) continue;
			if(board[nx][ny] > 0) sum += board[nx][ny];
		}
		return sum;
	}
	static void computeBoard(int[][] temp) {
		for(int x = 0; x < n; x++) {
			for(int y = 0; y < n; y++) {
				board[x][y] += temp[x][y];
			}
		}
	}
	static int getScore() {
		int sum = 0;
		for(int x = 0; x < n; x++) {
			for(int y = 0; y < n; y++) {
				if(board[x][y] > 0) sum += board[x][y];
			}
		}
		return sum;
	}
	// ===
	static boolean isNotBoard(int x, int y) {
		return x < 0 || y < 0 || x >= n || y >= n;
	}
}
