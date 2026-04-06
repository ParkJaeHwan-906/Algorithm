package apr.week1.codetree;

import java.util.*;
import java.io.*;

public class 메두사와전사들_박재환 {
	static BufferedReader br;
	static StringBuilder sb;
	public static void main(String[] args) throws IOException {
		br = new BufferedReader(new InputStreamReader(System.in));
		sb = new StringBuilder();
		init();
		br.close();
		System.out.println(sb);
	}
	static class Worrier {
		int x, y;
		boolean stone;
		boolean live;
		
		Worrier(int x, int y) {
			this.x = x;
			this.y = y;
			
			this.stone = false;
			this.live = true;
		}
	}
	static StringTokenizer st;
	static int n, m;
	static int[][] board;
	static int mx, my;			// 메두사 위치
	static int px, py;			// 공원 위치
	static Worrier[] worriers;
	static List<Integer>[][] worrierBoard;
	static void init() throws IOException {
		st = new StringTokenizer(br.readLine().trim());
		n = Integer.parseInt(st.nextToken());	
		m = Integer.parseInt(st.nextToken());	

		st = new StringTokenizer(br.readLine().trim());
		mx = Integer.parseInt(st.nextToken());	
		my = Integer.parseInt(st.nextToken());
		px = Integer.parseInt(st.nextToken());	
		py = Integer.parseInt(st.nextToken());
		
		worriers = new Worrier[m];
		worrierBoard = new List[n][n];
		for(int x = 0; x < n; x++) {
			for(int y = 0; y < n; y++) worrierBoard[x][y] = new ArrayList<Integer>();
		}
		st = new StringTokenizer(br.readLine().trim());
		for(int i = 0; i < m; i++) {
			int x = Integer.parseInt(st.nextToken());
			int y = Integer.parseInt(st.nextToken());
			Worrier worrier = new Worrier(x, y);
			worrierBoard[x][y].add(i);
			worriers[i] = worrier;
		}
		
		board = new int[n][n];
		for(int x = 0; x < n; x++) {
			st = new StringTokenizer(br.readLine().trim());
			for(int y = 0; y < n; y++) {
				board[x][y] = Integer.parseInt(st.nextToken());
				if(board[x][y] == 1) board[x][y] = -1;
			}
		}
		
		solution();
	}
	// 상 하 좌 우
	static final int[] dx = {-1, 1, 0, 0};
	static final int[] dy = {0, 0, -1, 1};
	static Deque<int[]> routeToPark;
	static int attackResult;
	static int stoneResult;
	static int moveResult;
	static void solution() {
		findRouteToPark();
		if(routeToPark == null) {
			sb.append(-1);
			return;
		}
		// 메두사 경로
//		validRoute();
		
		while(!routeToPark.isEmpty()) {
			moveResult = 0;
			attackResult = 0;
			int[] cur = routeToPark.pollFirst();						// 현재 메두사의 위치
			if(routeToPark.isEmpty()) {
				sb.append(0);
				break;
			}
			mx = cur[0];
			my = cur[1];
			if(!worrierBoard[mx][my].isEmpty()) {
				for(int id : worrierBoard[mx][my]) {
					worriers[id].live = false;
				}
				worrierBoard[mx][my].clear();
			}
			dicisionWatchDir(cur[0], cur[1]);		// 돌로 변하는 전사
			stoneWorrier();
			moveWorrier();
			sb.append(moveResult).append(' ').
			append(stoneResult).append(' ').
			append(attackResult).append('\n');
			resetWorrier();
		}
	}
	static List<Integer> stoneCand;
	static boolean[][] canWatch;
	static void dicisionWatchDir(int x, int y) {
		stoneCand = new ArrayList<Integer>();
		canWatch = new boolean[n][n];
		int bestDir = -1;
		for(int dir = 0; dir < 4; dir++) {
			WatchResult temp = null;
			if(dir == 0) temp = watchTop(x, y);
			else if(dir == 1) temp = watchBottom(x, y);
			else if(dir == 2) temp = watchLeft(x, y);
			else if(dir == 3) temp = watchRight(x, y);
			
			if(stoneCand.size() < temp.warriors.size()) {
				stoneCand = temp.warriors;
				canWatch = temp.finalWatch;
				bestDir = dir;
			}
		}
	}
	
	static void stoneWorrier() {
		stoneResult = stoneCand.size();
		for(int id : stoneCand) {
			Worrier w = worriers[id];
			if(!w.live) continue;
			w.stone = true;
		}
	}
	
	static void moveWorrier() {
		for(Worrier w : worriers) {
			if(!w.live || w.stone) continue;
			
			// 1차 이동 
			int[] first = moveFirst(w.x, w.y);
			if(first == null) continue;
			moveResult++;
			if(first[0] == mx && first[1] == my) {
				attackResult++;
				w.live = false;
				continue;
			}
			// 2차 이동
			int[] second = moveSecond(first[0], first[1]);
			if(second == null) {
				w.x = first[0];
				w.y = first[1];
				continue;
			}
			moveResult++;
			if(second[0] == mx && second[1] == my) {
				attackResult++;
				w.live = false;
				continue;
			}
			w.x = second[0];
			w.y = second[1];
		}
	}
	static int[] moveFirst(int x, int y) {
		// 상 하 좌 우
		int originDist = getDist(mx, my, x, y);
		for(int dir = 0; dir < 4; dir++) {
			int nx = x + dx[dir];
			int ny = y + dy[dir];
			if(isNotBoard(nx, ny)) continue;
			if(canWatch[nx][ny]) continue;
			int dist = getDist(mx, my, nx, ny);
			if(originDist > dist) return new int[] {nx, ny};
		}
		return null;
	}
	static int[] moveSecond(int x, int y) {
		// 좌 우 상 하
		int[] dx = {0, 0, -1, 1};
		int[] dy = {-1, 1, 0, 0};
		int originDist = getDist(mx, my, x, y);
		for(int dir = 0; dir < 4; dir++) {
			int nx = x + dx[dir];
			int ny = y + dy[dir];
			if(isNotBoard(nx, ny)) continue;
			if(canWatch[nx][ny]) continue;
			int dist = getDist(mx, my, nx, ny);
			if(originDist > dist) return new int[] {nx, ny};
		}
		return null;
	}

	static void resetWorrier() {
		for(int x = 0; x < n; x++) {
			for(int y = 0; y < n; y++) worrierBoard[x][y].clear();
		}
		for(int i = 0; i < m; i++) {
			Worrier w = worriers[i];
			if(!w.live) continue;
			w.stone = false;
			worrierBoard[w.x][w.y].add(i);
		}
	}
	// ===
	static class Node {
		int x, y;
		Node prev;
		
		Node(int x, int y, Node prev) {
			this.x = x;
			this.y = y;
			this.prev = prev;
		}
	}
	static class WatchResult {
	    List<Integer> warriors;
	    boolean[][] finalWatch;

	    WatchResult(List<Integer> warriors, boolean[][] finalWatch) {
	        this.warriors = warriors;
	        this.finalWatch = finalWatch;
	    }
	}
	static void findRouteToPark() {
		Queue<Node> q = new ArrayDeque<>();
		boolean[][] visited = new boolean[n][n];
		
		Node init = new Node(mx, my, null);
		q.offer(init);
		visited[mx][my] = true;
		
		while(!q.isEmpty()) {
			Node cur = q.poll();
			if(cur.x == px && cur.y == py) {		// 공원 도착
				recoverRoute(cur);
				return;
			}
			for(int dir = 0; dir < 4; dir++) {
				int nx = cur.x + dx[dir];
				int ny = cur.y + dy[dir];
				if(isNotBoard(nx, ny)) continue;
				if(board[nx][ny] == -1) continue;
				if(visited[nx][ny]) continue;
				visited[nx][ny] = true;
				Node next = new Node(nx, ny, cur);
				q.offer(next);
			}
		}
	}
	static void recoverRoute(Node cur) {
		Deque<int[]> dq = new ArrayDeque<>();
		
		while(cur.prev != null) {
			dq.offerFirst(new int[] {cur.x, cur.y});
			cur = cur.prev;
		}
		
		routeToPark = dq;
	}
	static boolean isNotBoard(int x, int y) {
		return x < 0 || y < 0 || x >= n || y >= n;
	}
	static WatchResult watchTop(int x, int y) {
		boolean[][] canWatch = new boolean[n][n];
		int depth = 0;
		for(int i = x - 1; i >= 0; i--) {
			++depth;
			canWatch[i][y] = true;
			// 왼쪽
			int j = y;
			for(int d = 0; d < depth && --j >= 0; d++) {
				canWatch[i][j] = true;	
			}
			// 오른쪽
			j = y;
			for(int d = 0; d < depth && ++j < n; d++) {
				canWatch[i][j] = true;	
			}
		}
		
		List<Integer> stone = new ArrayList<>();
		for(int i = x - 1; i >= 0; i--) {
			for(int j = 0; j < n; j++) {
				if(canWatch[i][j] && !worrierBoard[i][j].isEmpty()) {
					for(int id : worrierBoard[i][j]) {
						if(!worriers[id].live) continue;
						stone.add(id);
					}
					applyShadowTop(i, j, canWatch);
				}
			}
		}
		return new WatchResult(stone, canWatch);
	}
	
	static WatchResult watchBottom(int x, int y) {
		boolean[][] canWatch = new boolean[n][n];
		int depth = 0;
		for(int i = x + 1; i < n; i++) {
			++depth;
			canWatch[i][y] = true;
			// 왼쪽
			int j = y;
			for(int d = 0; d < depth && --j >= 0; d++) {
				canWatch[i][j] = true;
			}
			// 오른쪽
			j = y;
			for(int d = 0; d < depth && ++j < n; d++) {
				canWatch[i][j] = true;	
			}
		}
		
		List<Integer> stone = new ArrayList<>();
		for(int i = x + 1; i < n; i++) {
			for(int j = 0; j < n; j++) {
				if(canWatch[i][j] && !worrierBoard[i][j].isEmpty()) {
					for(int id : worrierBoard[i][j]) {
						if(!worriers[id].live) continue;
						stone.add(id);
					}
					applyShadowBottom(i, j, canWatch);
				}
			}
		}
		return new WatchResult(stone, canWatch);
	}

	
	static WatchResult watchLeft(int x, int y) {
		boolean[][] canWatch = new boolean[n][n];
		int depth = 0;
		for(int i = y - 1; i >= 0; i--) {
			++depth;
			canWatch[x][i] = true;
			// 왼쪽
			int j = x;
			for (int d = 0; d < depth && --j >= 0; d++) {
				canWatch[j][i] = true;
			}
			// 오른쪽
			j = x;
			for (int d = 0; d < depth && ++j < n; d++) {
				canWatch[j][i] = true;
			}
		}
		
		List<Integer> stone = new ArrayList<>();
		for(int i = y - 1; i >= 0; i--) {
			for(int j = 0; j < n; j++) {
				if(canWatch[j][i] && !worrierBoard[j][i].isEmpty()) {
					for(int id : worrierBoard[j][i]) {
						if(!worriers[id].live) continue;
						stone.add(id);
					}
					applyShadowLeft(j, i, canWatch);
				}
			}
		}
		return new WatchResult(stone, canWatch);
	}

	
	static WatchResult watchRight(int x, int y) {
		boolean[][] canWatch = new boolean[n][n];
		int depth = 0;
		for(int i = y + 1; i < n; i++) {
			++depth;
			canWatch[x][i] = true;
			// 왼쪽
			int j = x;
			for (int d = 0; d < depth && --j >= 0; d++) {
				canWatch[j][i] = true;
			}
			// 오른쪽
			j = x;
			for (int d = 0; d < depth && ++j < n; d++) {
				canWatch[j][i] = true;
			}
		}
		
		List<Integer> stone = new ArrayList<>();
		for(int i = y + 1; i < n; i++) {
			for(int j = 0; j < n; j++) {
				if(canWatch[j][i] && !worrierBoard[j][i].isEmpty()) {
					for(int id : worrierBoard[j][i]) {
						if(!worriers[id].live) continue;
						stone.add(id);
					}
					applyShadowRight(j, i, canWatch);
				}
			}
		}
		return new WatchResult(stone, canWatch);
	}
	
	// 위쪽 그림자
	static void applyShadowTop(int x, int y, boolean[][] canWatch) {
	    for (int i = x - 1; i >= 0; i--) {
	        int dist = x - i;
	        if (y == my) { // 메두사와 같은 열 (직선)
	            canWatch[i][y] = false;
	        } else if (y < my) { // 왼쪽 대각선 방향
	            for (int j = Math.max(0, y - dist); j <= y; j++) canWatch[i][j] = false;
	        } else { // 오른쪽 대각선 방향
	            for (int j = y; j <= Math.min(n - 1, y + dist); j++) canWatch[i][j] = false;
	        }
	    }
	}

	// 아래쪽 그림자
	static void applyShadowBottom(int x, int y, boolean[][] canWatch) {
	    for (int i = x + 1; i < n; i++) {
	        int dist = i - x;
	        if (y == my) {
	        	canWatch[i][y] = false;
	        } else if (y < my) {
	            for (int j = Math.max(0, y - dist); j <= y; j++) canWatch[i][j] = false;
	        } else {
	            for (int j = y; j <= Math.min(n - 1, y + dist); j++) canWatch[i][j] = false;
	        }
	    }
	}

	// 왼쪽 그림자
	static void applyShadowLeft(int x, int y, boolean[][] canWatch) {
	    for (int j = y - 1; j >= 0; j--) {
	        int dist = y - j;
	        if (x == mx) {
	        	canWatch[x][j] = false;
	        } else if (x < mx) {
	            for (int i = Math.max(0, x - dist); i <= x; i++) canWatch[i][j] = false;
	        } else {
	            for (int i = x; i <= Math.min(n - 1, x + dist); i++) canWatch[i][j] = false;
	        }
	    }
	}

	// 오른쪽 그림자
	static void applyShadowRight(int x, int y, boolean[][] canWatch) {
	    for (int j = y + 1; j < n; j++) {
	        int dist = j - y;
	        if (x == mx) {
	        	canWatch[x][j] = false;
	        } else if (x < mx) {
	            for (int i = Math.max(0, x - dist); i <= x; i++) canWatch[i][j] = false;
	        } else {
	            for (int i = x; i <= Math.min(n - 1, x + dist); i++) canWatch[i][j] = false;
	        }
	    }
	}

	static int getDist(int x1, int y1, int x2, int y2) {
		return Math.abs(x1 - x2) + Math.abs(y1 - y2);
 	}
}
