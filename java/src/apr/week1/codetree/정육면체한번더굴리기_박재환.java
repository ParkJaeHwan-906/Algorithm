package apr.week1.codetree;

import java.util.*;
import java.io.*;

public class 정육면체한번더굴리기_박재환 {
	static BufferedReader br;
	public static void main(String[] args) throws IOException {
		br = new BufferedReader(new InputStreamReader(System.in));
		init();
		br.close();
	}
	static StringTokenizer st;
	static int n, m;
	static int[][] board;
	static void init() throws IOException {
		st = new StringTokenizer(br.readLine().trim());
		n = Integer.parseInt(st.nextToken());
		m = Integer.parseInt(st.nextToken());
		
		board = new int[n][n];
		for(int x = 0; x < n; x++) {
			st = new StringTokenizer(br.readLine().trim());
			for(int y = 0; y < n; y++) board[x][y] = Integer.parseInt(st.nextToken());
		}
		
		solution();
	}
	static final int[] dx = {0, 1, 0 ,-1};
	static final int[] dy = {1, 0, -1, 0};
	static void solution() {
		Dice dice = new Dice();
		int totalScore = 0;
		int diceX = 0, diceY = 0;		// 초기 위치는 (0, 0)
		int diceDir = 0;				// 초기 방향은 오른쪽
		for(int i = 0; i < m;) {
			// 1. 주사위 굴리기
			if(diceDir == 0) {
				if(canMove(diceX, diceY, diceDir)) {
					dice.rollRight();
					diceX = diceX + dx[diceDir];
					diceY = diceY + dy[diceDir];
				}
				else {
					diceDir = (diceDir + 2) % 4;
					continue;
				}
			}
			else if(diceDir == 1) {
				if(canMove(diceX, diceY, diceDir)) {
					dice.rollDown();
					diceX = diceX + dx[diceDir];
					diceY = diceY + dy[diceDir];
				}
				else {
					diceDir = (diceDir + 2) % 4;
					continue;
				}
			}
			else if(diceDir == 2) {
				if(canMove(diceX, diceY, diceDir)) {
					dice.rollLeft();
					diceX = diceX + dx[diceDir];
					diceY = diceY + dy[diceDir];
				}
				else {
					diceDir = (diceDir + 2) % 4;
					continue;
				}
			}
			else if(diceDir == 3) {
				if(canMove(diceX, diceY, diceDir)) {
					dice.rollUp();
					diceX = diceX + dx[diceDir];
					diceY = diceY + dy[diceDir];
				}
				else {
					diceDir = (diceDir + 2) % 4;
					continue;
				}
			}
			// 2. 점수 합산 
			int score = board[diceX][diceY];
			totalScore += findSameScore(diceX, diceY, score);
			// 3. 방향 전환
			if(dice.body > score) { diceDir = (diceDir + 1) % 4; }
			else if(dice.body < score) { diceDir = (diceDir - 1 + 4) % 4; }
			i++;
		}
		
		System.out.println(totalScore);
	}
	static boolean canMove(int x, int y, int dir) {
		int nx = x + dx[dir];
		int ny = y + dy[dir];
		return !(nx < 0 || ny < 0 || nx >= n || ny >= n);
	}
	static int findSameScore(int x, int y, int score) {
		Queue<int[]> q = new ArrayDeque<int[]>();
		boolean[][] visited = new boolean[n][n];
		
		int sum = 0;
		q.offer(new int[] {x, y});
		visited[x][y] = true;
		
		while(!q.isEmpty()) {
			int[] cur = q.poll();
			sum += board[cur[0]][cur[1]];
			
			for(int dir = 0; dir < 4; dir++) {
				if(!canMove(cur[0], cur[1], dir)) continue;
				int nx = cur[0] + dx[dir];
				int ny = cur[1] + dy[dir];
				if(visited[nx][ny]) continue;
				if(board[nx][ny] != score) continue;
				
				visited[nx][ny] = true;
				q.offer(new int[] {nx, ny});
			}
		}
		return sum;
	}
	static class Dice {
		int head;
		int chest;
		int body;
		int left;
		int right;
		int leg;
		
		Dice() {
			this.head = 1;
			this.chest = 2;
			this.left = 3;
			this.right = 4;
			this.body = 6;
			this.leg = 5;
		}
		
		void rollRight() {
			int tempH = this.head;
			int tempL = this.left;
			int tempB = this.body;
			int tempR = this.right;
			
			this.left = tempH;
			this.body = tempL;
			this.right = tempB;
			this.head = tempR;
		}
		
		void rollLeft() {
			int tempH = this.head;
			int tempL = this.left;
			int tempB = this.body;
			int tempR = this.right;
			
			this.head = tempL;
			this.left = tempB;
			this.body = tempR;
			this.right = tempH;
		}
		
		void rollDown() {
			int tempH = this.head;
			int tempC = this.chest;
			int tempB = this.body;
			int tempL = this.leg;
			
			this.chest = tempH;
			this.body = tempC;
			this.leg = tempB;
			this.head = tempL;
		}
		
		void rollUp() {
			int tempH = this.head;
			int tempC = this.chest;
			int tempB = this.body;;
			int tempL = this.leg;
			
			this.head = tempC;
			this.chest = tempB;
			this.body = tempL;
			this.leg = tempH;
		}
	}
}
