package sep.week1.codetree;

import java.util.*;
import java.io.*;

public class AI로봇청소기_박재환 {
	public static void main(String[] args) throws IOException {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		init(br);
		br.close();
	}
	
	static final int[] dx = {0, 1, 0, -1};
	static final int[] dy = {1, 0, -1, 0};
	
	static class RobotCleaner {
		int id;
		int x, y;
		RobotCleaner(int id, int x, int y) {
			this.id = id;
			this.x = x;
			this.y = y;
		}
	}
	
	static int n, k, l;
	static int[][] board;
	static List<RobotCleaner> robotCleaners;
	static int[][] robotCleanerBoard;
	static void init(BufferedReader br) throws IOException {
		StringTokenizer st;
		
		st = new StringTokenizer(br.readLine().trim());
		n = Integer.parseInt(st.nextToken());		// 격자 크기
		k = Integer.parseInt(st.nextToken());		// 청소기 수
		l = Integer.parseInt(st.nextToken());		// 테스트 횟수
		
		board = new int[n][n];
		robotCleanerBoard = new int[n][n];
		for(int x = 0; x < n; x++) {
			st = new StringTokenizer(br.readLine().trim());
			for(int y = 0; y < n; y++) {
				/**
				 * 1 ~ 100 : 먼지
				 * 0 : 빈칸 
				 * -1 : 물건
				 */
				board[x][y] = Integer.parseInt(st.nextToken());
			}
		}
		
		robotCleaners = new ArrayList<>();
		for(int id = 1; id <= k; id++) {
			st = new StringTokenizer(br.readLine().trim());
			int x = Integer.parseInt(st.nextToken()) - 1;
			int y = Integer.parseInt(st.nextToken()) - 1;
			RobotCleaner robotCleaner = new RobotCleaner(id, x, y);
			robotCleaners.add(robotCleaner);
			robotCleanerBoard[x][y] = id;
		}
		
		System.out.print(soltion());
	}
	
	static String soltion() {
		StringBuilder sb = new StringBuilder();
		while(l-- > 0) {
			moveRobotCleaners();
			cleanRobotCleaners();
			accDust();
			sb.append(spreadDust()).append('\n');
		}
		return sb.toString();
	}
	
	// ========================================
	// 청소기 이동 
	// ========================================
	static void moveRobotCleaners() {
		for(RobotCleaner robotCleaner : robotCleaners) {
			int[] nextState = moveRobotCleaner(robotCleaner);
			if(nextState == null) {		// 이동할 수 없는 경우
				continue;
			}
			// 새 상태로 갱신
			robotCleanerBoard[robotCleaner.x][robotCleaner.y] = 0;
			robotCleaner.x = nextState[0];
			robotCleaner.y = nextState[1];
			robotCleanerBoard[robotCleaner.x][robotCleaner.y] = robotCleaner.id;
		}
	}
	static int[] moveRobotCleaner(RobotCleaner robotCleaner) {
		Queue<int[]> q = new ArrayDeque<>();		// [x, y, dist]
		boolean[][] visited = new boolean[n][n];
		
		q.offer(new int[] {robotCleaner.x, robotCleaner.y, 0});
		visited[robotCleaner.x][robotCleaner.y] = true;
		
		int bestDist = Integer.MAX_VALUE;
		int bestX = Integer.MAX_VALUE;
		int bestY = Integer.MAX_VALUE;
		while(!q.isEmpty()) {
			int[] cur = q.poll();
			int curX = cur[0];
			int curY = cur[1];
			int dist = cur[2];
			
			if(board[curX][curY] > 0) {		// 먼지가 있는 칸이라면
				if(bestDist > dist
						|| (bestDist == dist && bestX > curX)
						|| (bestDist == dist && bestX == curX && bestY > curY)) {
					bestDist = dist;
					bestX = curX;
					bestY = curY;
				}
				continue;
			}
			
			if(bestDist <= dist) {
				continue;
			}
			
			for(int d = 0; d < 4; d++) {
				int nx = curX + dx[d];
				int ny = curY + dy[d];
				if(isNotBoard(nx, ny) || board[nx][ny] == -1 || visited[nx][ny]) {		// 격자 밖 또는 벽
					continue;
				}
				if(robotCleanerBoard[nx][ny] > 0) {					// 다른 청소기가 있음
					continue;
				}
				
				visited[nx][ny] = true;
				q.offer(new int[] {nx, ny, dist + 1});
			}
		}
		return bestDist == Integer.MAX_VALUE
				? null : new int[] {bestX, bestY};
	}	
	// =============================================
	// 청소 
	// =============================================
	static void cleanRobotCleaners() {
		for(RobotCleaner robotCleaner : robotCleaners) {
			int cleanDir = findCleanDir(robotCleaner);
			if(cleanDir == -1) {		// 청소할 위치가 없음
				continue;
			}
			cleanRobotCleaner(robotCleaner, cleanDir);
		}
	}
	static int findCleanDir(RobotCleaner robotCleaner) {
		int totalDust = board[robotCleaner.x][robotCleaner.y];
		for(int dir = 0; dir < 4; dir++) {
			int nx = robotCleaner.x + dx[dir];
			int ny = robotCleaner.y + dy[dir];
			if(isNotBoard(nx, ny) || board[nx][ny] <= 0) {		// 격자 밖 또는 먼지가 없는 경우
				continue;
			}
			totalDust += Math.min(20, board[nx][ny]);
		}
		
		int bestDir = -1;
		int bestDust = 0;
		for(int dir = 0; dir < 4; dir++) {
			int tempDust = totalDust;
			int exceptDir = (dir + 2) % 4;
			int nx = robotCleaner.x + dx[exceptDir];
			int ny = robotCleaner.y + dy[exceptDir];
			if(!isNotBoard(nx, ny) && board[nx][ny] > 0) {
				tempDust -= Math.min(20, board[nx][ny]);
			}
			if(bestDust < tempDust) {
				bestDir = dir;
				bestDust = tempDust;
			}
		}
		return bestDir;
	}
	static void cleanRobotCleaner(RobotCleaner robotCleaner, int cleanDir) {
		int exceptDir = (cleanDir + 2) % 4;
		board[robotCleaner.x][robotCleaner.y] -= Math.min(20, board[robotCleaner.x][robotCleaner.y]);
		for(int dir = 0; dir < 4; dir++) {
			if(dir == exceptDir) {
				continue;
			}
			int nx = robotCleaner.x + dx[dir];
			int ny = robotCleaner.y + dy[dir];
			if(isNotBoard(nx, ny) || board[nx][ny] <= 0) {		// 격자 밖 또는 먼지가 없는 경우
				continue;
			}
			board[nx][ny] -= Math.min(20, board[nx][ny]);
		}
	}
	// =============================================
	// 먼지 축적 
	// =============================================
	static void accDust() {
		for(int x = 0; x < n; x++) {
			for(int y = 0; y < n; y++) {
				board[x][y] += (board[x][y] > 0)
						? 5 : 0;
			}
		}
	}
	// =============================================
	// 먼지 확산 
	// =============================================
	static int spreadDust() {
		int[][] temp = new int[n][n];
		for(int x = 0; x < n; x++) {
			for(int y = 0; y < n; y++) {
				if(board[x][y] != 0) {
					continue;
				}
				
				// 깨끗한 칸인경우 -> 상하좌우 먼지 합 구하기
				int adjDust = 0;
				for(int dir = 0; dir < 4; dir++) {
					int nx = x + dx[dir];
					int ny = y + dy[dir];
					if(isNotBoard(nx, ny) || board[nx][ny] <= 0) {
						continue;
					}
					adjDust += board[nx][ny];
				}
				temp[x][y] += (adjDust / 10);
			}
		}
		return integrateBoard(temp);
	}
	static int integrateBoard(int[][] temp) {
		int totalDust = 0;
		for(int x = 0; x < n; x++) {
			for(int y = 0; y < n; y++) {
				board[x][y] += temp[x][y];
				if(board[x][y] > 0) {
					totalDust += board[x][y];
				}
			}
		}
		return totalDust;
	}
	// =============================================
	// 공통 
	// =============================================
	static boolean isNotBoard(int x, int y) {
		return x < 0 || y < 0 || x >= n || y >= n;
	}
}
