package mar.week5.codetree;

import java.util.*;
import java.io.*;

public class 예술성_박재환 {
	static BufferedReader br;
	public static void main(String[] args) throws IOException {
		br = new BufferedReader(new InputStreamReader(System.in));
		init();
		br.close();
	}
	static StringTokenizer st;
	static int n;
	static int[][] board;
	static void init() throws IOException {
		n = Integer.parseInt(br.readLine().trim());
		board = new int[n][n];
		for(int x = 0; x < n; x++) {
			st = new StringTokenizer(br.readLine().trim());
			for(int y = 0; y < n; y++) board[x][y] = Integer.parseInt(st.nextToken());
		}
		
		System.out.println(solution());
	}
	static long solution() {
		// 1. 초기 예술점수 
		long totalScore = getArtScore();
//		System.out.println(totalScore);
		
		for(int i = 0; i < 3; i++) {
			turn();
			long score = getArtScore();
			totalScore += score;
//			System.out.println(score);
		}
		return totalScore;
	}
	static long getArtScore() {
		// 그룹 찾기
		findGroups();
		
//		printGroup();
		
		return findPairGroup();
	}
	static class Group {
		int id;
		int size;
		List<int[]> outline;
		
		Group(int id, int size, List<int[]> outline) {
			this.id = id;
			this.size = size;
			this.outline = outline;
		}
	}
	static int gId;
	static Map<Integer, Group> groups;
	static int[][] checked;
	static void findGroups() {
		gId = 0;
		groups = new HashMap<>();
		checked = new int[n][n];
		
		for(int x = 0; x < n; x++) {
			for(int y = 0; y < n; y++) {
				if(checked[x][y] != 0) continue;
				Group group = findGroup(x, y, checked, ++gId);
				groups.put(gId, group);
			}
		}
	}
	static final int[] dx = {0, 1, 0, -1};
	static final int[] dy = {1, 0, -1, 0};
	static Group findGroup(int x, int y, int[][] checked, int gId) {
		int id = board[x][y];
		int size = 1;
		
		Queue<int[]> q = new ArrayDeque<int[]>();
		List<int[]> outline = new ArrayList<int[]>();
		q.offer(new int[] {x, y});
		checked[x][y] = gId;
		
		while(!q.isEmpty()) {
			int[] cur = q.poll();
			boolean include = true;
			for(int dir = 0; dir < 4; dir++) {
				int nx = cur[0] + dx[dir];
				int ny = cur[1] + dy[dir];
				if(isNotBoard(nx, ny)) continue;
				if(board[nx][ny] != id) {
					include = false;
					continue;
				}
				if(checked[nx][ny] != 0) continue;
				
				checked[nx][ny] = gId;
				q.offer(new int[] {nx, ny});
				size++;
			}
			
			if(!include) outline.add(cur);
		}
		return new Group(id, size, outline);
	}
	static long findPairGroup() {
		boolean[][] pair = new boolean[gId+1][gId+1];
		long totalScore = 0;
		for(Map.Entry<Integer, Group> entry : groups.entrySet()) {
			Map<Integer, Integer> adj = new HashMap<>();
			Group g = entry.getValue();
			for(int[] cur : g.outline) {
				int x = cur[0];
				int y =cur[1];
				
				for(int dir = 0; dir < 4; dir++) {
					int nx = x + dx[dir];
					int ny = y + dy[dir];
					
					if(isNotBoard(nx, ny)) continue;
					if(checked[x][y] == checked[nx][ny]) continue;
					
					adj.put(checked[nx][ny], adj.getOrDefault(checked[nx][ny], 0) + 1);
				}
			}
			
			for(int oId : adj.keySet()) {
				if(pair[entry.getKey()][oId] || pair[oId][entry.getKey()]) continue;
				
				int aId = g.id;
				int aSize = g.size;
				int bId = groups.get(oId).id;
				int bSize = groups.get(oId).size;
				int adjCnt = adj.get(oId);
				long score = (aSize + bSize) * aId * bId * adjCnt;
				totalScore += score;
				
				pair[entry.getKey()][oId] = true; 
				pair[oId][entry.getKey()] = true;
			}
		}
		return totalScore;
	}
	static void turn() {
		int[][] temp = new int[n][n];
		crossTurn(temp);
//		for(int[] arr : temp) {
//			System.out.println(Arrays.toString(arr));
//		}
		otherTurn(temp);
//		for(int[] arr : temp) {
//			System.out.println(Arrays.toString(arr));
//		}
		board = temp;
	}
	static void crossTurn(int[][] temp) {
		// 중심
		int mid = n / 2;
		
		for(int i = 0; i < n; i++) {
			temp[i][mid] = board[mid][n - 1 - i];
			temp[mid][i] = board[i][mid];
			
		}
	}
	static void otherTurn(int[][] temp) {
		int mid = n / 2;
		
		// 0 ~ mid - 1
		int[][] leftTop = new int[mid][mid];
		for(int x = 0; x < mid; x++) {
			for(int y = 0; y < mid; y++) {
				leftTop[x][y] = board[x][y];
			}
		}
		leftTop = turnClockWise(leftTop, mid);
		for(int x = 0; x < mid; x++) {
			for(int y = 0; y < mid; y++) {
				temp[x][y] = leftTop[x][y];
			}
		}
		
		// mid + 1 ~ n
		int[][] rightTop = new int[mid][mid];
		for(int x = 0; x < mid; x++) {
			for(int y = mid + 1; y < n; y++) {
				rightTop[x][y - mid - 1] = board[x][y];
			}
		}
		rightTop = turnClockWise(rightTop, mid);
		for(int x = 0; x < mid; x++) {
			for(int y = mid + 1; y < n; y++) {
				temp[x][y] = rightTop[x][y - mid - 1];
			}
		}
		
		// mid + 1 ~ n
		int[][] rightDown = new int[mid][mid];
		for(int x = mid + 1; x < n; x++) {
			for(int y = mid + 1; y < n; y++) {
				rightDown[x - mid - 1][y - mid - 1] = board[x][y];
			}
		}
		rightDown = turnClockWise(rightDown, mid);
		for(int x = mid + 1; x < n; x++) {
			for(int y = mid + 1; y < n; y++) {
				temp[x][y] = rightDown[x - mid - 1][y - mid - 1];
			}
		}
		
		// 0 ~ mid - 1
		int[][] leftDown = new int[mid][mid];
		for(int x = mid + 1; x < n; x++) {
			for(int y = 0; y < mid; y++) {
				leftDown[x - mid - 1][y] = board[x][y];
			}
		}
		leftDown = turnClockWise(leftDown, mid);
		for(int x = mid + 1; x < n; x++) {
			for(int y = 0; y < mid; y++) {
				temp[x][y] = leftDown[x - mid - 1][y];
			}
		}
	}
	static int[][] turnClockWise(int[][] arr, int size) {
		int[][] temp = new int[size][size];
		
		for(int x = 0; x < size; x++) {
			for(int y = 0; y < size; y++) {
				temp[y][size - 1 - x] = arr[x][y];
			}
		}
		return temp;
	}
	// =================================================================
	static boolean isNotBoard(int x, int y) { return x < 0 || y < 0 || x >= n || y >= n; }
	static void printGroup() {
		for(Group g : groups.values()) {
			System.out.printf("id : %d, size : %d\n outline : ", g.id, g.size);
			for(int[] arr : g.outline) {
				System.out.print(Arrays.toString(arr)+ " ");
			}
			System.out.println();
		}
	}
}
