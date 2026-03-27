package mar.week4.codetree;

import java.util.*;
import java.io.*;

public class 꼬리잡기놀이_박재환 {
	static BufferedReader br;
	public static void main(String[] args) throws IOException {
		br = new BufferedReader(new InputStreamReader(System.in));
		init();
		br.close();
	}
	static StringTokenizer st;
	static int n, m, k;
	static int[][] board;
	static void init() throws IOException {
		st = new StringTokenizer(br.readLine().trim());
		n = Integer.parseInt(st.nextToken());			// 격자 크기
		m = Integer.parseInt(st.nextToken());			// 팀 개수
		k = Integer.parseInt(st.nextToken());			// 라운드 수
		
		board = new int[n][n];
		for(int x = 0; x < n; x++) {
			st = new StringTokenizer(br.readLine().trim());			
			for(int y = 0; y < n; y++) {
				board[x][y] = Integer.parseInt(st.nextToken());
			}
		}
		
		solution();
	}
	static int score;
	static List<List<int[]>> teams;
	static void solution() {
		teams = new ArrayList<List<int[]>>();
		score = 0;
		
		setTeam();
		
		for(int i = 0; i < k; i++) {
			move();
			shoot(i);
		}
		System.out.println(score);
	}
	static void setTeam() {
		boolean[][] checked = new boolean[n][n];
		
		for(int x = 0; x < n; x++) {
			for(int y = 0; y < n; y++) {
				if(board[x][y] != 1 || checked[x][y]) continue;
				
				List<int[]> team = new ArrayList<int[]>();
				findTeam(x, y, checked, team);
				teams.add(team);
			}
		}
		
		// 이후 격자에서 사람의 존재만 표시
		for(List<int[]> team : teams) {
			for(int[] loc : team) {
				board[loc[0]][loc[1]] = 1;
			}
		}
	}
	static void move() {
//		System.out.println("[MOVE]");
		for(List<int[]> team : teams) {
			int[] next = forward(team.get(0), team.get(1));
			
			// 꼬리의 위치만 변경
			int[] tail = team.remove(team.size() - 1);
			board[tail[0]][tail[1]] = 4;
			
			team.add(0, next);		// 새로운 머리 위치
			board[next[0]][next[1]] = 1;
		}
		
	}
	static final int[] dx = {0, -1, 0 ,1};
	static final int[] dy = {1, 0, -1 ,0};
	static void findTeam(int x, int y, boolean[][] checked, List<int[]> team) {
//		System.out.println("[FINDTEAM]");
		checked[x][y] = true;
		if(board[x][y] != 4) {
			team.add(new int[] {x, y});
		}
		
		for(int dir = 0; dir < 4; dir++) {
			int nx = x + dx[dir];
			int ny = y + dy[dir];
			
			if(isNotBoard(nx, ny)) continue;
			if(board[nx][ny] == 0) continue;		
			
			/**
			 * 1 2
			 * 3 2
			 * 이런 케이스가 있기 때문에, 해당 조건문이 필요
			 */
			if((board[x][y] == 1 && board[nx][ny] == 2) ||
					(board[x][y] != 1 && !checked[nx][ny])) {
				findTeam(nx, ny, checked, team);
			}
		}
	}
	static int[] forward(int[] curHead, int[] curBody) {
		// 1. 머리를 먼저 움직인다. 
		for(int dir = 0; dir < 4; dir++) {
			int nx = curHead[0] + dx[dir];
			int ny = curHead[1] + dy[dir];
			
			if(isNotBoard(nx, ny)) continue;
			if(board[nx][ny] == 0) continue;
			if(curBody[0] == nx && curBody[1] == ny) continue;
			
			return new int[] {nx, ny};
		}
		return null;
	}
	static void shoot(int r) {
//		System.out.print("[SHOOT] ");
		int currentRound = r % (4 * n);
	    int dir = currentRound / n;  // 0:우, 1:상, 2:좌, 3:하
	    int line = currentRound % n; // 해당 방향에서 몇 번째 줄인지
		
	    if (dir == 0) { // 왼쪽 -> 오른쪽 (행: line, 열: 0 ~ n-1)
	        for (int y = 0; y < n; y++) {
	            if (board[line][y] == 1) { // 사람(머리/몸통/꼬리 통합 1)을 만난 경우
	                swapHtoT(line, y);
	                return; // 한 명만 맞으므로 바로 종료
	            }
	        }
	    } 
	    else if (dir == 1) { // 아래 -> 위 (행: n-1 ~ 0, 열: line)
	        for (int x = n - 1; x >= 0; x--) {
	            if (board[x][line] == 1) {
	                swapHtoT(x, line);
	                return;
	            }
	        }
	    } 
	    else if (dir == 2) { // 오른쪽 -> 왼쪽 (행: n-1-line, 열: n-1 ~ 0)
	        int row = n - 1 - line;
	        for (int y = n - 1; y >= 0; y--) {
	            if (board[row][y] == 1) {
	                swapHtoT(row, y);
	                return;
	            }
	        }
	    } 
	    else if (dir == 3) { // 위 -> 아래 (행: 0 ~ n-1, 열: n-1-line)
	        int col = n - 1 - line;
	        for (int x = 0; x < n; x++) {
	            if (board[x][col] == 1) {
	                swapHtoT(x, col);
	                return;
	            }
	        }
	    }
	}
	static void swapHtoT(int x, int y) {
		for(List<int[]> team : teams) {
		    for(int i = 0; i < team.size(); i++) {
		        int[] pos = team.get(i);
		        if(pos[0] == x && pos[1] == y) {

		            // 점수
		            score += (i + 1) * (i + 1);

		            // 방향 뒤집기
		            Collections.reverse(team);

		            return;
		        }
		    }
		}
	}
	// ========================================================
	static boolean isNotBoard(int x, int y) {
		return x < 0 || y < 0 || x >= n || y >= n;
	}
}
