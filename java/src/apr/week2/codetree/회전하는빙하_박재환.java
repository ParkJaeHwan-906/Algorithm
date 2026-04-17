package apr.week2.codetree;

import java.util.*;
import java.io.*;

public class 회전하는빙하_박재환 {
	static BufferedReader br;
	public static void main(String[] args) throws IOException {
		br = new BufferedReader(new InputStreamReader(System.in));
		init();
		br.close();
	}
	static StringTokenizer st;
	static int n, q;
	static int size;
	static int[][] board;
	static Queue<Integer> cmds;
	static void init() throws IOException {
		st = new StringTokenizer(br.readLine().trim());
		n = Integer.parseInt(st.nextToken());
		q = Integer.parseInt(st.nextToken());
		size = getSize(n);
		board = new int[size][size];
		for(int x = 0; x < size; x++) {
			st = new StringTokenizer(br.readLine().trim());
			for(int y = 0; y < size; y++) {
				board[x][y] = Integer.parseInt(st.nextToken());
			}
		}
		
		cmds = new ArrayDeque<>();
		st = new StringTokenizer(br.readLine().trim());
		for(int i = 0; i < q; i++) {
			int l = Integer.parseInt(st.nextToken());
			cmds.offer(l);
		}
		
		solution();
		System.out.println(String.format("%d\n%d", getIce(), getMax()));
	}
	
	static void solution() {
		while(!cmds.isEmpty()) {
			int l = cmds.poll();
			rotateGrids(l);
			melt();
		}
	}
	
	static void rotateGrids(int l) {
		int lSize = getSize(l);
		for(int x = 0; x < size; x += lSize) {
			for(int y = 0; y < size; y += lSize) {
				rotateGrid(x, y, lSize);
			}
		}
	}
	
	static void rotateGrid(int x, int y, int lSize) {
		int[][] temp = new int[lSize][lSize];
		for(int i = x; i < x + lSize; i++) {
			for(int j = y; j < y + lSize; j++) {
				temp[i - x][j - y] = board[i][j];
			}
		}
		
		// 구역별로 회전 
		int half = lSize / 2;
		
		// 1 > 2
		for(int i = 0; i < half; i++) {
			for(int j = 0; j < half; j++) {
				board[i + x][j + y + half] = temp[i][j];
			}
		}
		
		// 2 > 4
		for(int i = 0; i < half; i++) {
			for(int j = half; j < lSize; j++) {
				board[i + x + half][j + y] = temp[i][j];
			}
		}
		
		// 3 > 1
		for(int i = half; i < lSize; i++) {
			for(int j = 0; j < half; j++) {
				board[i + x - half][j + y] = temp[i][j];
			}
		}
		
		// 4 > 3
		for(int i = half; i < lSize; i++) {
			for(int j = half; j < lSize; j++) {
				board[i + x][j + y - half] = temp[i][j];
			}
		}
	}
	static final int[] dx = {0, 1, 0, -1};
	static final int[] dy = {1, 0, -1, 0};
	static void melt() {
		boolean[][] isMelt = new boolean[size][size];
		for(int x = 0; x < size; x++) {
			for(int y = 0; y < size; y++) {
				if(board[x][y] == 0) continue;
				
				int ice = 0;
				for(int dir = 0; dir < 4; dir++) {
					int nx = x + dx[dir];
					int ny = y + dy[dir];
					if(nx < 0 || ny < 0 || nx >= size || ny >= size) continue;
					if(board[nx][ny] > 0) ice++;
				}
				
				if(ice < 3) isMelt[x][y] = true; 
			}
		}
		
		
		for(int x = 0; x < size; x++) {
			for(int y = 0; y < size; y++) {
				if(isMelt[x][y]) board[x][y]--;
			}
		}
	}
	
	static long getIce() {
		long totalIce = 0;
		for(int x = 0; x < size; x++) {
			for(int y = 0; y < size; y++) {
				totalIce += board[x][y];
			}
		}
		return totalIce;
	}
	
	static long getMax() {
		long max = 0;
		boolean[][] visited = new boolean[size][size];
		
		for(int x = 0; x < size; x++) {
			for(int y = 0; y < size; y++) {
				if(visited[x][y] || board[x][y] == 0) continue;
				max = Math.max(grouping(x, y, visited), max);
			}
		}
		return max;
	}
	static long grouping(int x, int y, boolean[][] visited) {
		Queue<int[]> q = new ArrayDeque<int[]>();
		
		q.offer(new int[] {x, y});
		visited[x][y] = true;
		long group = 1;
		
		while(!q.isEmpty()) {
			int[] cur = q.poll();
			for(int dir = 0; dir < 4; dir++) {
				int nx = cur[0] + dx[dir];
				int ny = cur[1] + dy[dir];
				if(nx < 0 || ny < 0 || nx >= size || ny >= size) continue;
				if(board[nx][ny] == 0) continue;
				if(visited[nx][ny]) continue;
				
				visited[nx][ny] = true;
				q.offer(new int[] {nx, ny});
				group++;
			}
		}
		
		return group;
	}
	static int getSize(int i) {
		int size = 1;
		for(int j = 0; j < i; j++) {
			size *= 2;
		}
		return size;
	}
}
