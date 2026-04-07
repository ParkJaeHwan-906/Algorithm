package apr.week1.codetree;

import java.util.*;
import java.io.*;

public class 고대문명유적탐사_박재환 {
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
	static int k, m;
	static int[][] board;
	static Queue<Integer> wall;
	static void init() throws IOException {
		st = new StringTokenizer(br.readLine().trim());
		k = Integer.parseInt(st.nextToken());		// 턴 수 
		m = Integer.parseInt(st.nextToken());		// 대기 블럭 수
		
		// 격자 입력
		board = new int[5][5];
		for(int x = 0; x < 5; x++) {
			st = new StringTokenizer(br.readLine().trim());
			for(int y = 0; y < 5; y++) {
				board[x][y] = Integer.parseInt(st.nextToken());
			}
		}
		
		// 대기 블럭 압력
		st = new StringTokenizer(br.readLine().trim());
		wall = new ArrayDeque<>();
		for(int i = 0; i < m; i++) wall.offer(Integer.parseInt(st.nextToken()));
		
		solution();
	}
	
	static void solution() {
		for(int i = 0; i < k; i++) {
			// 시작 전에도 확인 
			int preScore = originBoardGain();
			pickGrid();
			// ===
//			System.out.printf("[중심점] : (%d, %d), 회전 : %d 번\n", bestX, bestY, bestDegree);
//			printTempGrid(bestBoard);
//			printGainLoc(bestGain);
			//===
			int middleScore = take();
			int afterScore = originBoardGain();
			if(preScore + middleScore + afterScore == 0) break;
			sb.append(preScore + middleScore + afterScore).append(' ');
		}
	}
	
	/**
	 * [격자 선택] O
	 * 3 x 3 격자를 선택한다.
	 */
	static int bestX, bestY, bestDegree;
	static List<int[]> bestGain;
	static int[][] bestBoard;
	static void pickGrid() {
		bestX = 10;
		bestY = 10;
		bestDegree = 0;
		bestGain = new ArrayList<int[]>();
		bestBoard = new int[5][5];
		
		for(int x = 1; x < 4; x++) {
			for(int y = 1; y < 4; y++) {
				int[][] grid = makeGrid(x, y);
				//===
//				print3Grid(grid);
				//===
				simulation(grid, x, y);
			}
		}
	}
	static int[][] makeGrid(int x, int y) {
		/**
		 * 중심 (x, y) 를 기준으로 3 x 3 격자를 만든다. 
		 */
		int[][] temp = new int[3][3];
		for(int i = x - 1; i <= x + 1; i++) {
			for(int j = y - 1; j <= y + 1; j++) {
				temp[i - (x - 1)][j - (y - 1)] = board[i][j];
			}
		}
		return temp;
	}
	/**
	 * [회전] O
	 * 선택된 3 x 3 격자를 순차적으로 90 180 270 회전시킨다.
	 */
	static void simulation(int[][] arr, int x, int y) {
		// 회전시킨 격자를 반영한 임시 전체 board 를 생성한다. 
		int[][] temp = new int[5][5];
		// clone
		for(int i = 0; i < 5; i ++) {
			for(int j = 0; j < 5; j++) {
				temp[i][j] = board[i][j];
			}
		}
//		System.out.printf("[중심점] : (%d, %d)\n", x, y);
		// 회전 ( 3 번 가능 )
		for(int i = 0; i < 3; i++) {
//			print3Grid(arr);
			arr = rotate90(arr);
//			print3Grid(arr);
			computeGrid(temp, arr, x, y);
			//===
//			printTempGrid(temp);
			//===
			// 회전된 상태로 얻을 수 있는 최대 유물
			List<int[]> gain = expectedGain(temp);
			// ===
//			printTempGrid(temp);
//			printGainLoc(gain);
			// ===
			if(bestGain.size() < gain.size()) {
				bestX = x;
				bestY = y;
				bestDegree = i;
				bestGain = gain;
				bestBoard = copyBoard(temp);;
			} else if(bestGain.size() == gain.size() && 
					(bestDegree > i)) {
				bestX = x;
				bestY = y;
				bestDegree = i;
				bestGain = gain;
				bestBoard = copyBoard(temp);;
			} else if(bestGain.size() == gain.size() && 
					(bestDegree == i) &&
					(bestY > y || (bestY == y && bestX > x))) {
				bestX = x;
				bestY = y;
				bestDegree = i;
				bestGain = gain;
				bestBoard = copyBoard(temp);
			}
				
		}
	}
	static int[][] rotate90(int[][] arr) {
		int[][] temp = new int[3][3];
		for(int x = 0; x < 3; x++) {
			for(int y = 0; y < 3; y++) {
				temp[y][3 - 1 - x] = arr[x][y];
			}
		}
		return temp;
	}
	static void computeGrid(int[][] origin, int[][] temp, int x, int y) {
		for(int i = x - 1; i <= x + 1; i++) {
			for(int j = y - 1; j <= y + 1; j++) {
				origin[i][j] = temp[i - (x - 1)][j - (y - 1)];
			}
		}
	}
	/**
	 * [얻을 수 있는 유물 확인]
	 */
	static List<int[]> expectedGain(int[][] arr) {
		List<int[]> gain = new ArrayList<int[]>();
		boolean[][] visited = new boolean[5][5];
		for(int x = 0; x < 5; x++) {
			for(int y = 0; y < 5; y++) {
				if(visited[x][y]) continue;
				// 현위치를 기점으로 동일한 블록이 얼마나 있는지 확인
				List<int[]> list = findSameGroup(arr, visited, x, y);
				if(list.size() < 3) continue;
				gain.addAll(list);
			}
		}
		return gain;
	} 
	static final int[] dx = {0, 1, 0, -1};
	static final int[] dy = {1, 0, -1, 0};
	static List<int[]> findSameGroup(int[][] arr, boolean[][] visited, int x, int y) {
		Queue<int[]> q = new ArrayDeque<int[]>();
		List<int[]> list = new ArrayList<>();
		int standard = arr[x][y];
		q.offer(new int[] {x, y});
		visited[x][y] = true;
		
		while(!q.isEmpty()) {
			int[] cur = q.poll();
			list.add(cur);
			
			for(int dir = 0; dir < 4; dir++) {
				int nx = cur[0] + dx[dir];
				int ny = cur[1] + dy[dir];
				if(isNotBoard(nx, ny)) continue;
				if(arr[nx][ny] != standard) continue;
				if(visited[nx][ny]) continue;
				
				visited[nx][ny] = true;
				q.offer(new int[] {nx, ny});
			}
		}
		return list;
	}
	static int take() {
		board = bestBoard;
		bestGain.sort((a, b) -> {
			/**
			 * 열 번호가 작고 > 행 번호가 큰
			 */
			if(a[1] == b[1]) return Integer.compare(b[0], a[0]);
			return Integer.compare(a[1], b[1]);
		});
		
		// 벽에 있는 유물로, 빈 칸 채우기
		for(int[] loc : bestGain) {
			int x = loc[0]; 
			int y = loc[1]; 
			board[x][y] = wall.poll();
		}
		
//		printTempGrid(board);
		return bestGain.size();
	}
	/**
	 * 회전시키지 않은 board 에서 얻을 수 있는 유물
	 */
	static int originBoardGain() {
		List<int[]> gain = new ArrayList<int[]>();
		
		while(true) {
			List<int[]> list = expectedGain(board);
			if(list.isEmpty()) break;
			originTake(list);
			gain.addAll(list);
		}
		
		return gain.size();
	}
	static void originTake(List<int[]> list) {
		list.sort((a, b) -> {
			/**
			 * 열 번호가 작고 > 행 번호가 큰
			 */
			if(a[1] == b[1]) return Integer.compare(b[0], a[0]);
			return Integer.compare(a[1], b[1]);
		});
		
		// 벽에 있는 유물로, 빈 칸 채우기
		for(int[] loc : list) {
			int x = loc[0]; 
			int y = loc[1]; 
			board[x][y] = wall.poll();
		}
	}
	// ===
	static boolean isNotBoard(int x, int y) {
		return x < 0 || y < 0 || x >= 5 || y >= 5;
	}
	static int[][] copyBoard(int[][] arr) {
		int[][] temp = new int[5][5];
		for(int x = 0; x < 5; x++) {
			for(int y = 0; y < 5; y++) temp[x][y] = arr[x][y];
		}
		return temp;
	}
	
	static void print3Grid(int[][] arr) {
		for(int[] a : arr) System.out.println(Arrays.toString(a));
		System.out.println();
	}
	static void printTempGrid(int[][] arr) {
		for(int[] a : arr) System.out.println(Arrays.toString(a));
		System.out.println();
	}
	static void printGainLoc(List<int[]> list) {
		for(int[] arr : list) {
			System.out.println(Arrays.toString(arr));
		}
		System.out.println();
	}
}
