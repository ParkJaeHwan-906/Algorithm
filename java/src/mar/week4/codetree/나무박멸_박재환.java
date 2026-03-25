package mar.week4.codetree;

import java.util.*;
import java.io.*;

public class 나무박멸_박재환 {
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
	static int n, m, k, c;
	static int[][] board;
	static int[][] kBoard;
	static void init() throws IOException {
		st = new StringTokenizer(br.readLine().trim());
		n = Integer.parseInt(st.nextToken());
		m = Integer.parseInt(st.nextToken());
		k = Integer.parseInt(st.nextToken());
		c = Integer.parseInt(st.nextToken());
		
		kBoard = new int[n][n];		// 제초제
		board = new int[n][n];
		for(int x=0; x<n; x++) {
			st = new StringTokenizer(br.readLine().trim());
			/**
			 * -1 : 벽 
			 * 0 : 빈 칸
			 */
			for(int y=0; y<n; y++) board[x][y] = Integer.parseInt(st.nextToken());
		}
		
		int totalKilled = 0;
		while(m-- > 0) {
			// 1. 나무 성장
			grow();
//			printBoard();
			propagation();
//			printBoard();
			int killed = killTree();
			totalKilled += killed;
//			printBoard();
//			System.out.println("===============");
		}
		
		sb.append(totalKilled);
	}
	static List<int[]> trees;
	static void grow() {
		trees = new ArrayList<>();
		for(int x=0; x<n; x++) {
			for(int y=0; y<n; y++) {
				if(board[x][y] > 0) {
					int near = nearTree(x,  y);
					board[x][y] += near;
					trees.add(new int[] {x, y});
				}
			}
		}
	}
	static final int[] dx = {0, 1, 0 ,-1, 1, 1, -1, -1};
	static final int[] dy = {1, 0, -1, 0, 1, -1, 1, -1};
	static int nearTree(int x, int y) {
		int tree = 0;
		for(int dir = 0; dir < 4; dir++) {
			int nx = x + dx[dir];
			int ny = y + dy[dir];
			if(isNotBoard(nx, ny)) continue;
			if(board[nx][ny] > 0) tree++;
		}
		return tree;
	}
	static void propagation() {
		int[][] next = new int[n][n];		// 번식으로 새롭게 생기는 나무
		for(int[] tree : trees) {
			int x = tree[0];
			int y = tree[1];
			
			List<int[]> cand = new ArrayList<>();		// 번식 가능한 칸 기록
			// 상하좌우로 번식 가능
			// 벽 또는 제초제 또는 나무가 있으면 번식 불가 -> 빈 칸으로만 번식 가능
			for(int dir = 0; dir < 4; dir++) {
				int nx = x + dx[dir];
				int ny = y + dy[dir];
				if(isNotBoard(nx, ny)) continue;
				if(board[nx][ny] != 0) continue;
				if(kBoard[nx][ny] > 0) continue;
				 
				cand.add(new int[] {nx, ny});
			}
			if(cand.isEmpty()) continue;
			int size = board[x][y] / cand.size();
			for(int[] loc : cand) {
				int x1 = loc[0];
				int y1 = loc[1];
				next[x1][y1] += size;
			}
		}
		
		// 지연 갱신
		for(int x=0; x<n; x++) {
			for(int y=0; y<n; y++) {
				board[x][y] += next[x][y];
			}
		}
	}
	static int killTree() {
		int bestC = 0;
		int bestX = 25;
		int bestY = 25;
		int[][] temp = new int[n][n];		// [x][y] : (x, y) 칸에 제초제를 뿌렸을 때, 죽일 수 있는 나무의 수
		for(int x=0; x<n; x++) {
			for(int y=0; y<n; y++) {
				if(board[x][y] == 0 || board[x][y] == -1) continue;
				int c = getDeathTree(x, y); 
				if(bestC < c) {
					bestC = c;
					bestX = x;
					bestY = y;
				} else if(bestC == c && (bestX > x || (bestX == x && bestY > y))) {
					bestX = x;
					bestY = y;
				}
				temp[x][y] = c;
			}
		}
		
//		for(int[] arr : temp) System.out.println(Arrays.toString(arr));
//		System.out.printf("제초제 살포 : (%d, %d)\n", bestX, bestY);
//		System.out.println();
		
		updateKBoard();
		
		// 뿌릴 수 없을 수도 있음 
		if(bestC > 0) spreadKill(bestX, bestY);
		
		return bestC;
	}
	static int getDeathTree(int x, int y) {
		int total = board[x][y];
		for(int dir = 4; dir < 8; dir++) {
			int nx = x;
			int ny = y;
			int move = 0;
			while(move++ < k) {
				nx += dx[dir];
				ny += dy[dir];
				if(isNotBoard(nx, ny)) break;
				if(board[nx][ny] < 1) break;
				total += board[nx][ny];
			}
		}
		return total;
	}
	static void spreadKill(int x, int y) {
		/**
		 * 제초제 살포
		 */
		board[x][y] = 0;
		kBoard[x][y] = c;
		for(int dir = 4; dir < 8; dir++) {
			int nx = x;
			int ny = y;
			int move = 0;
			while(move++ < k) {
				nx += dx[dir];
				ny += dy[dir];
				if(isNotBoard(nx, ny)) break;
				if(board[nx][ny] < 1) {
					if(board[nx][ny] == 0) {		// 빈칸
						kBoard[nx][ny] = c;
					}
					break;
				}
				// 나무 죽음
				board[nx][ny] = 0;
				// 제초제 갱신
				kBoard[nx][ny] = c;
			}
		}
	}
	static void updateKBoard() {
		for(int x=0; x<n; x++) {
			for(int y=0; y<n; y++) {
				if(kBoard[x][y] == 0) continue;
				kBoard[x][y]--;
			}
		}
	}
	// ===========================================
	static boolean isNotBoard(int x, int y) {
		return x < 0 || y < 0 || x >= n || y >= n;
	}
	
	static void printBoard() {
		for(int[] arr : board) System.out.println(Arrays.toString(arr));
		System.out.println();
	} 
}
