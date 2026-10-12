package oct.week1.codetree;

import java.util.*;
import java.io.*;

public class 메이즈러너_박재환 {
	public static void main(String[] args) throws IOException {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		init(br);
		br.close();
	}
	
	static final int[] dx = {1, -1, 0, 0};
	static final int[] dy = {0, 0, 1, -1};
	
	static class Loc {
		int x, y;
		Loc(int x, int y) {
			this.x = x;
			this.y = y;
		}
		@Override
		public boolean equals(Object o) {
			if(o == this) {
				return true;
			}
			if(!(o instanceof Loc)) {
				return false;
			}
			Loc oLoc = (Loc) o;
			return this.x == oLoc.x && this.y == oLoc.y;
		}
		@Override
		public int hashCode() {
			return Objects.hash(this.x, this.y);
		}
	}
	static class Runner extends Loc {
		boolean exit;
		Runner(int x, int y) {
			super(x, y);
			this.exit = false;
		}
	}
	
	static int n, m, k;
	static int[][] board;
	static Map<Loc, List<Runner>> runners;
	static Loc exit;
	static void init(BufferedReader br) throws IOException {
		StringTokenizer st;
		st = new StringTokenizer(br.readLine().trim());
		n = Integer.parseInt(st.nextToken());
		m = Integer.parseInt(st.nextToken());
		k = Integer.parseInt(st.nextToken());
		board = new int[n][n];
		for(int x = 0; x < n; x++) {
			st = new StringTokenizer(br.readLine().trim());
			for(int y = 0; y < n; y++) {
				/**
				 * 0 : 빈칸
				 * 1 ~ 9 : 벽
				 */
				board[x][y] = Integer.parseInt(st.nextToken());
			}
		}
		runners = new HashMap<>();
		for(int i = 0; i < m; i++) {
			st = new StringTokenizer(br.readLine().trim());
			int x = Integer.parseInt(st.nextToken()) - 1;
			int y = Integer.parseInt(st.nextToken()) - 1;
			runners.computeIfAbsent(new Loc(x, y), k -> new ArrayList<>()).add(new Runner(x, y));
		}
		st = new StringTokenizer(br.readLine().trim());
		int x = Integer.parseInt(st.nextToken()) - 1;
		int y = Integer.parseInt(st.nextToken()) - 1;
		exit = new Loc(x, y);
		System.out.print(solution());
	}
	
	static int exitRunners;
	static int totalMoveDistance;
	static String solution() {
		exitRunners = 0;
		totalMoveDistance = 0;
		while(k-- > 0) {
			moveRunners();
			if(exitRunners == m) {
				break;
			}
			rotateMaze();
		}
		return String.format("%d\n%d %d", totalMoveDistance, exit.x + 1, exit.y + 1);
	}
	
	// ============================================================
	// Runner 이동
	// ============================================================
	static void moveRunners() {
		Map<Loc, List<Runner>> tempList = new HashMap<>();
		for(List<Runner> runnerList : runners.values()) {
			for(Runner runner : runnerList) {
				if(runner.exit) {
					continue;		
				}
				Loc nextLoc = moveRunner(runner);
				if(nextLoc == null) {
					tempList.computeIfAbsent(new Loc(runner.x, runner.y), k -> new ArrayList<>()).add(runner);
					continue;
				}
				runner.x = nextLoc.x;
				runner.y = nextLoc.y;
				totalMoveDistance++;
				if(runner.x == exit.x && runner.y == exit.y) {
					runner.exit = true;
					exitRunners++;
					continue;
				}
				tempList.computeIfAbsent(new Loc(runner.x, runner.y), k -> new ArrayList<>()).add(runner);
			}
		}
		runners = tempList;
	}
	static Loc moveRunner(Runner runner) {
		int originDist = getDist(runner.x, runner.y, exit.x, exit.y);
		Loc nextLoc = null;
		for(int dir = 0; dir < 4; dir++) {
			int nx = runner.x + dx[dir];
			int ny = runner.y + dy[dir];
			if(isNotBoard(nx, ny) || board[nx][ny] > 0) {
				continue;
			}
			int newDist = getDist(nx, ny, exit.x, exit.y);
			if(newDist < originDist) {
				nextLoc = new Loc(nx, ny);
				break;
			}
		}
		return nextLoc;
	}
	// ============================================================
	// 미로 이동
	// ============================================================
	static class Square extends Loc {
		int l;
		Square(int x, int y, int l) {
			super(x, y);
			this.l = l;
		}
	}
	static void rotateMaze() {
		Square square = findSquare();
		rotateBoard(square);
		rotateRunner(square);
		rotateExit(square);
	}
	static void rotateBoard(Square square) {
		int[][] temp = new int[square.l][square.l];
		for(int i = square.x; i < square.x + square.l; i++) {
			for(int j = square.y; j < square.y + square.l; j++) {
				temp[i - square.x][j - square.y] = board[i][j] > 0
						? board[i][j] - 1 : board[i][j]; 
			}
		}
		
		for(int i = 0; i < square.l; i++) {
			for(int j = 0; j < square.l; j++) {
				board[j + square.x][square.l - i - 1 + square.y] = temp[i][j]; 
			}
		}
	}
	static void rotateRunner(Square square) {
		List<Runner>[][] temp = new List[square.l][square.l];
		for(int i = 0; i < square.l; i++) {
			for(int j = 0; j < square.l; j++) {
				temp[i][j] = new ArrayList<>();
			}
		}
		for(int i = square.x; i < square.x + square.l; i++) {
			for(int j = square.y; j < square.y + square.l; j++) {
				if(runners.containsKey(new Loc(i, j))) {
					temp[i - square.x][j - square.y] = runners.get(new Loc(i, j));
					runners.remove(new Loc(i, j));
				}
			}
		}
		List<Runner>[][] afterRotate = new List[square.l][square.l];
		for(int i = 0; i < square.l; i++) {
			for(int j = 0; j < square.l; j++) {
				afterRotate[j][square.l - i - 1] = temp[i][j];  
			}
		}
		for(int i = 0; i < square.l; i++) {
			for(int j = 0; j < square.l; j++) {
				if(afterRotate[i][j].isEmpty()) {
					continue;
				}
				for(Runner runner : afterRotate[i][j]) {
					runner.x = i + square.x;
					runner.y = j + square.y;
				}
				runners.put(new Loc(i + square.x, j + square.y), afterRotate[i][j]);
			}
		}
	}
	static void rotateExit(Square square) {
		int[][] exitBoard = new int[n][n];
		exitBoard[exit.x][exit.y] = -1;
		int[][] temp = new int[square.l][square.l];
		for(int i = square.x; i < square.x + square.l; i++) {
			for(int j = square.y; j < square.y + square.l; j++) {
				temp[i - square.x][j - square.y] = exitBoard[i][j];
			}
		}
		
		for(int i = 0; i < square.l; i++) {
			for(int j = 0; j < square.l; j++) {
				exitBoard[j + square.x][square.l - i - 1 + square.y] = temp[i][j]; 
			}
		}
		
		for(int i = 0; i < n; i++) {
			for(int j = 0; j < n; j++) {
				if(exitBoard[i][j] == -1) {
					exit = new Loc(i, j);
					return;
				}
			}
		}
		
	}
	static Square findSquare() {
		for(int l = 2; l <= n; l++) {
			for(int x = 0; x <= n - l; x++) {
				for(int y = 0; y <= n - l; y++) {
					if(includeRunnerAndExit(x, y, l)) {
						return new Square(x, y, l);
					}
				}
			}
		}
		return null;
	}
	static boolean includeRunnerAndExit(int x, int y, int l) {
		boolean isExit = false;
		boolean isRunner = false;
		for(int i = x; i < x + l; i++) {
			for(int j = y; j < y + l; j++) {
				if(runners.containsKey(new Loc(i, j)) && !runners.get(new Loc(i, j)).isEmpty()) {
					isRunner = true;
				}
				if(exit.x == i && exit.y == j) {
					isExit = true;
				}
			}
		}
		return isExit && isRunner;
	}
	// ============================================================
	// 공용
	// ============================================================
	static int getDist(int x1, int y1, int x2, int y2) {
		return Math.abs(x1 - x2) + Math.abs(y1 - y2);
	}
	static boolean isNotBoard(int x, int y) {
		return x < 0 || y < 0 || x >= n ||y >= n;
	}
}
