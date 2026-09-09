package sep.week1.codetree;

import java.util.*;
import java.io.*;

public class 아기바다거북의대모험_박재환 {
	public static void main(String[] args) throws IOException {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		init(br);
		br.close();
	}
	
	static final int[] dx = {0, 1, 0, -1};
	static final int[] dy = {1, 0, -1, 0};
	
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
	
	static class Volcano extends Loc{
		int limitP;
		int curP;
		Volcano(int x,int y, int limitP) {
			super(x, y);
			this.limitP = limitP;
			this.curP = 0;
		}
	}
	
	static class Turtle extends Loc {
		int id;
		boolean stone;
		int exitAt;
		Turtle(int id, int x, int y) {
			super(x, y);
			this.id = id;
			this.stone = false;
			this.exitAt = -1;
		}
	}
	
	static int n, m, k;
	static Loc end;
	static int[][] board;	
	static Map<Loc, Volcano> volcanos;
	static Map<Loc, Turtle> turtles;
	static Map<Integer, Turtle> idToTurtles;
	static void init(BufferedReader br) throws IOException {
		StringTokenizer st;
		
		st = new StringTokenizer(br.readLine().trim());
		n = Integer.parseInt(st.nextToken());			// 격자 크기
		m = Integer.parseInt(st.nextToken());			// 거북이 수
		k = Integer.parseInt(st.nextToken());			// 해저 화산 수
		end = new Loc(n - 1, n - 1);
		board = new int[n][n];
		for(int x = 0; x < n; x++) {
			st = new StringTokenizer(br.readLine().trim());
			for(int y = 0; y < n; y++) {
				/**
				 * 0 : 빈 공간
				 * 1 : 산호초
				 */
				board[x][y] = Integer.parseInt(st.nextToken());
			}
		}
		
		turtles = new HashMap<>();
		idToTurtles = new TreeMap<>((a, b) -> Integer.compare(a, b));		// id 순으로 정렬
		for(int i = 0; i < m; i++) {
			st = new StringTokenizer(br.readLine().trim());
			int x = Integer.parseInt(st.nextToken());
			int y = Integer.parseInt(st.nextToken());
			Turtle turtle = new Turtle(i + 1, x, y);
			turtles.put(new Loc(x, y), turtle);
			idToTurtles.put(i + 1, turtle);
		}
		
		volcanos = new HashMap<>();
		for(int i = 0; i < k; i++) {
			st = new StringTokenizer(br.readLine().trim());
			int x = Integer.parseInt(st.nextToken());
			int y = Integer.parseInt(st.nextToken());
			int p = Integer.parseInt(st.nextToken());
			volcanos.put(new Loc(x, y), new Volcano(x, y, p));
		}
		
		System.out.print(solution());
	}
	
	static String solution() {
		int time = 0;
		while(time++ < 100) {
			moveTurtles(time);
			increasePressure();
			explosionVolcanos();
		}
		return printResult();
	}
	
	// ========================================================
	// 바다 거북 이동
	// ========================================================
	static void moveTurtles(int time) {
		for(Turtle turtle : idToTurtles.values()) {
			if(turtle.stone) {			// 돌이 된 거북이는 움직이지 않음
				continue;	
			}
			
			if(turtle.exitAt != -1) {	// 이미 나간 거북이도 움직이지 않음
				continue;
			}
			
			NextLoc nextLoc = moveTurtle(turtle);
			if(!nextLoc.find) {		// 최단 경로가 존재하지 않음
				continue;
			}
				
			// 기존 위치와, 새 위치 swap 
			turtles.remove(new Loc(turtle.x, turtle.y));
			turtle.x = nextLoc.x;
			turtle.y = nextLoc.y;
			if(end.x == turtle.x && end.y == turtle.y) {
				turtle.exitAt = time;
				continue;
			}
			turtles.put(new Loc(nextLoc.x, nextLoc.y), turtle);
		}
	}
	
	static class Node extends Loc {
		Node prev;
		Node(int x, int y, Node prev) {
			super(x, y);
			this.prev = prev;
		}
	}
	
	static class NextLoc extends Loc {
		boolean find;
		NextLoc(int x, int y, boolean find) {
			super(x, y);
			this.find = find;
		}
	}
	
	static NextLoc moveTurtle(Turtle turtle) {
		Queue<Node> q = new ArrayDeque<>();
		boolean[][] visited = new boolean[n][n];
		
		q.offer(new Node(turtle.x, turtle.y, null));
		visited[turtle.x][turtle.y] = true;
		
		while(!q.isEmpty()) {
			Node cur = q.poll();
			if(cur.x == end.x && cur.y == end.y) {														// 안식처에 도착한 경우
				Loc nextLoc = findNextLoc(cur);
				return new NextLoc(nextLoc.x, nextLoc.y, true);
			}
			for(int dir = 0; dir < 4; dir++) {
				int nx = cur.x + dx[dir];
				int ny = cur.y + dy[dir];
				if(isNotBoard(nx, ny) || visited[nx][ny] || board[nx][ny] == 1) {						// 격자를 벗어나거나, 이미 방문한 경우, 산호초인 경우 
					continue;
				}
				if(turtles.containsKey(new Loc(nx, ny))) {		// 다른 거북이가 막고있는 경우
					continue;
				}
				q.offer(new Node(nx, ny, cur));
				visited[nx][ny] = true;
			}
		}
		return new NextLoc(-1, -1, false);
	}
	
	static Loc findNextLoc(Node cur) {
		while(cur.prev.prev != null) {
			cur = cur.prev;
		}
		return new Loc(cur.x, cur.y);
	}
	// ========================================================
	// 압력 증가
	// ========================================================
	static void increasePressure() {
		for(Volcano volcano : volcanos.values()) {
			volcano.curP += 10;
		}
	}
	// ========================================================
	// 화산 분출
	// ========================================================
	static void explosionVolcanos() {
		Set<Loc> alreadyExplosion = new HashSet<>();
		int[][] accPressure = new int[n][n];
		while(true) {
			boolean explosion = false;
			for(Volcano volcano : volcanos.values()) {
				if(alreadyExplosion.contains(new Loc(volcano.x, volcano.y))) {		// 이미 폭발한 화산은 더 이상 폭발하지 않음
					continue;
				}
				if(volcano.curP + accPressure[volcano.x][volcano.y] < volcano.limitP) {	// 분출 조건이 되지 않는 경우
					continue;
				}
				
				explosion = true;
				explosionVolcano(volcano, accPressure);
				alreadyExplosion.add(new Loc(volcano.x, volcano.y));
				
			}
			if(!explosion) {		// 폭발이 더 이상 일어나지 않는다면 종료
				break;
			}
		}
		
		// 돌이 된 거북이 처리
		turtleStone(accPressure);
		// 이번 턴에 폭발한 화선 0 처리
		for(Loc loc : alreadyExplosion) {
			volcanos.get(loc).curP = 0;
		}
	}
	
	static void explosionVolcano(Volcano volcano, int[][] accPressure) {
		for(int dir = 0; dir < 4; dir++) {
			int p = volcano.limitP;
			int x = volcano.x;
			int y = volcano.y;
			while(!isNotBoard(x, y) && board[x][y] == 0) {
				accPressure[x][y] += p;
				x += dx[dir];
				y += dy[dir];
				p /= 2;
			}
		}
		accPressure[volcano.x][volcano.y] /= 4;
	}
	
	static void turtleStone(int[][] accPressure) {
		for(Turtle turtle : turtles.values()) {
			if(turtle.stone || turtle.exitAt != -1) {
				continue;
			}
			if(accPressure[turtle.x][turtle.y] >= 20) {		// 돌이 되는 경우 
				turtle.stone = true;
			}
		}
	}
	
	
	// ========================================================
	// 공통 
	// ========================================================
	static boolean isNotBoard(int x, int y) {
		return x < 0 || y < 0 || x >= n || y >= n;
	}
	static String printResult() {
		StringBuilder sb = new StringBuilder();
		for(Turtle turtle : idToTurtles.values()) {
			sb.append(turtle.exitAt).append('\n');
		}
		return sb.toString();
	}
}
 