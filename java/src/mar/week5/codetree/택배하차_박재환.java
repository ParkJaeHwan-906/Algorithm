package mar.week5.codetree;

import java.util.*;
import java.io.*;

public class 택배하차_박재환 {
	static BufferedReader br;
	public static void main(String[] args) throws IOException {
		br = new BufferedReader(new InputStreamReader(System.in));
		init();
		br.close();
	}
	static class Command {
		int id;
		int h, w;
		int inputLoc;
		
		Command(int id, int h, int w, int inputLoc) {
			this.id = id;
			this.h = h;
			this.w = w;
			this.inputLoc = inputLoc;
		}
	}
	static StringTokenizer st;
	static int n, m;
	static Queue<Command> cmdQ;
	static void init() throws IOException {
		st = new StringTokenizer(br.readLine().trim());
		n = Integer.parseInt(st.nextToken());
		m = Integer.parseInt(st.nextToken());
		
		cmdQ = new ArrayDeque<>();
		for(int i = 0; i < m; i++) {
			st = new StringTokenizer(br.readLine().trim());
			int id = Integer.parseInt(st.nextToken());
			int h = Integer.parseInt(st.nextToken());
			int w = Integer.parseInt(st.nextToken());
			int inputLoc = Integer.parseInt(st.nextToken()) - 1;
			
			Command command = new Command(id, h, w, inputLoc);
			cmdQ.offer(command);
		}
		
		System.out.println(solution());
	}
	static Set<Integer> removed;
	static String solution() {
		removed = new HashSet<Integer>();
		
		StringBuilder sb = new StringBuilder();
		
		while(!cmdQ.isEmpty()) {
			drop();
			if(cmdQ.isEmpty()) break;			
			int left = peekLeft();
			sb.append(left).append('\n');
			drop();
			if(cmdQ.isEmpty()) break;
			int right = peekRight();
			sb.append(right).append('\n');
			drop();
			if(cmdQ.isEmpty()) break;
		}
		
		return sb.toString();
	}
	static int[][] board;
	static void drop() {
		/**
		 * cmdQ 에 들어있는 모든 박스를 위치시킨다.
		 */
		board = new int[n][n];
		Queue<Command> temp = new ArrayDeque<>();
		
		while(!cmdQ.isEmpty()) {
			Command command = cmdQ.poll();
			if(removed.contains(command.id)) continue;
			int insertLoc = lowestLoc(command.inputLoc, command.w);
			put(insertLoc, command);
			temp.offer(command);
		}
		
		while(!temp.isEmpty()) cmdQ.offer(temp.poll());
	}
	static int peekLeft() {					
		/**
		 * 0 -> n 
		 * 방향 탐색
		 */
		boolean[][] checked = new boolean[n][n];
		int target = 107;
		for(int x = n - 1; x > -1; x--) {
			for(int y = 0; y < n; y++) {
				if(board[x][y] == 0) continue;
				if(checked[x][y]) break;
				List<int[]> points = sameType(x, y, checked);
				if(canPopLeft(board[x][y], points)) target = Math.min(target, board[x][y]);
				break;
			}
		}
		remove(target);
		return target;
	}
	static int peekRight() {					
		/**
		 * 0 <- n 
		 * 방향 탐색
		 */
		boolean[][] checked = new boolean[n][n];
		int target = 107;
		for(int x = n - 1; x > -1; x--) {
			for(int y = n - 1; y > 0; y--) {
				if(board[x][y] == 0) continue;
				if(checked[x][y]) break;
				List<int[]> points = sameType(x, y, checked);
				if(canPopRight(board[x][y], points)) target = Math.min(target, board[x][y]);
				break;
			}
		}
		remove(target);
		return target;
	}
	// ===================================
	static int lowestLoc(int inputLoc, int w) {
		int lowest = n;
		for(int y = inputLoc; y < inputLoc + w && y < n; y++) {
			int x = 0;
			while(x < n) {
				if(board[x][y] == 0) x++;
				else break;
			}
			lowest = Math.min(lowest, x - 1);
		}
		return lowest;
	}
	static void put(int insertLoc, Command cmd) {
		for(int x = insertLoc; x > insertLoc - cmd.h; x--) {
			for(int y = cmd.inputLoc; y < cmd.inputLoc + cmd.w; y++) board[x][y] = cmd.id;
		}
	}
	static final int[] dx = {0, 1, 0, -1};
	static final int[] dy = {1, 0, -1, 0};
	static List<int[]> sameType(int x, int y, boolean[][] checked) {
		int id = board[x][y];
		
		Queue<int[]> q = new ArrayDeque<>();
		List<int[]> points = new ArrayList<int[]>();
		
		q.offer(new int[] {x, y});
		checked[x][y] = true;
		
		while(!q.isEmpty()) {
			int[] cur = q.poll();
			points.add(cur);
			for(int dir = 0; dir < 4; dir++) {
				int nx = cur[0] + dx[dir];
				int ny = cur[1] + dy[dir];
				if(isNotBoard(nx, ny)) continue;
				if(checked[nx][ny] || board[nx][ny] != id) continue;
				
				checked[nx][ny] = true;
				q.offer(new int[] {nx, ny});
			}
		}
		return points;
	}
	static boolean isNotBoard(int x, int y) { return x < 0 || y < 0 || x >= n || y >= n; }
	static void remove(int target) {
		removed.add(target);
	}
	static boolean canPopLeft(int id, List<int[]> points) { 
		for(int[] point : points) {
			int x = point[0];
			int y = point[1];
			
			for(int j = y - 1; j >= 0; j--) {
				if(board[x][j] != id && board[x][j] != 0) return false;
			}
		}
		return true;
	}
	static boolean canPopRight(int id, List<int[]> points) { 
		for(int[] point : points) {
			int x = point[0];
			int y = point[1];
			
			for(int j = y + 1; j < n; j++) {
				if(board[x][j] != id && board[x][j] != 0) return false;
			}
		}
		return true;
	}
}
