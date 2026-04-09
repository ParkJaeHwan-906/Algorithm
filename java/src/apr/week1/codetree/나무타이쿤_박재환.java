package apr.week1.codetree;

import java.util.*;
import java.io.*;

public class 나무타이쿤_박재환 {
	static BufferedReader br;
	public static void main(String[] args) throws IOException {
		br = new BufferedReader(new InputStreamReader(System.in));
		init();
		br.close();
	}
	static StringTokenizer st;
	static int n, m;
	static int[][] board;
	static Queue<Command> cmds;
	static void init() throws IOException {
		st = new StringTokenizer(br.readLine().trim());
		n = Integer.parseInt(st.nextToken());
		m = Integer.parseInt(st.nextToken());
		
		board = new int[n][n];
		for(int x = 0; x < n; x++) {
			st = new StringTokenizer(br.readLine().trim());
			for(int y = 0; y < n; y++) {
				board[x][y] = Integer.parseInt(st.nextToken());
			}
		}
		
		cmds = new ArrayDeque<>();
		for(int i = 0; i < m; i++) {
			st = new StringTokenizer(br.readLine().trim());
			int d = Integer.parseInt(st.nextToken()) - 1;
			int p = Integer.parseInt(st.nextToken());
			cmds.offer(new Command(d, p));
		}
		
		System.out.println(solution());
	}
	static class Command {
		int d;		// 이동 방향 
		int p;		// 이동 칸 수 
		Command(int d, int p) {
			this.d = d;
			this.p = p;
		}
	}
	static class Point {
		int x, y;
		
		Point(int x, int y) {
			this.x = x;
			this.y = y;
		}
	}
	static Queue<Point> 영양제;
	static boolean[][] injected;
	static int solution() {
		setInit();
		while(m-- > 0) {
			Command cmd = cmds.poll();
			move(cmd);
			grow();
			checkCross();
			add영양제();
		}
		return getSum();
	}
	static void setInit() {
		영양제 = new ArrayDeque<>();
		for(int x = n - 1; x > n - 3; x--) {
			for(int y = 0; y < 2; y++) {
				영양제.offer(new Point(x, y));
			}
		}
		// ===
		//print영양제();
	}
	static final int[] dx = {0, -1, -1, -1, 0, 1, 1, 1};
	static final int[] dy = {1, 1, 0, -1, -1, -1, 0, 1};
	static void move(Command cmd) {
		int dir = cmd.d;
		int p = cmd.p;
//		System.out.printf("[COMMAND] dir : %d, p : %d\n", dir, p);
		for(Point pt : 영양제) {
			int x = pt.x;
			int y = pt.y;
//			System.out.printf("[이동 가중치] (%d, %d)\n", dx[dir] * p, dy[dir] * p);
			int nx = ((x + dx[dir] * p) + n) % n;
			int ny = ((y + dy[dir] * p) + n) % n;
//			System.out.printf("[NEW LOC] (%d, %d) -> (%d, %d)\n", x , y, nx, ny);
			pt.x = nx;
			pt.y = ny;
		}
		
		// ===
		//print영양제();
	}
	static void grow() {
		injected = new boolean[n][n];
		for(Point p : 영양제) {
			int x = p.x;
			int y = p.y;
			board[x][y]++;
			injected[x][y] = true;
		}
		// ===
		//printBoard();
	}
	static final int[] crossDx = {1, 1, -1, -1};
	static final int[] crossDy = {1, -1, 1, -1};
	static void checkCross() {
		while(!영양제.isEmpty()) {
			Point p = 영양제.poll();
			int x = p.x;
			int y = p.y;
			
			for(int dir = 0; dir < 4; dir++) {
				int nx = x + crossDx[dir];
				int ny = y + crossDy[dir];
				if(isNotBoard(nx, ny)) continue;
				if(board[nx][ny] > 0) board[x][y]++;
			}
		}
		// ===
		//printBoard();
	}
	static void add영양제() {
		for(int x = 0; x < n; x++) {
			for(int y = 0; y < n; y++) {
				if(injected[x][y]) continue;
				if(board[x][y] < 2) continue;
				board[x][y] -= 2;
				영양제.offer(new Point(x, y));
			}
		}
	}
	static int getSum() {
		int sum = 0;
		for(int x = 0; x < n; x++) {
			for(int y = 0; y < n; y++) {
				sum += board[x][y];
			}
		}
		return sum;
	}
	// ===
	static boolean isNotBoard(int x, int y) {
		return x < 0 || y < 0 || x >= n || y >= n;
	}
	// ===
	static void print영양제() {
		for(Point p : 영양제) {
			System.out.printf("(%d, %d)\n", p.x, p.y);
		}
		System.out.println();
	}
	static void printBoard() {
		for(int[] arr : board) System.out.println(Arrays.toString(arr));
		System.out.println();
	}
}
