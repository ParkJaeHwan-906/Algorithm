package apr.week1.codetree;

import java.util.*;
import java.io.*;

public class 민트초코우유_박재환 {
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
	static int n, t;
	static char[][] foodBoard;
	static int[][] believeBoard;
	static void init() throws IOException {
		st = new StringTokenizer(br.readLine().trim());
		n = Integer.parseInt(st.nextToken());
		t = Integer.parseInt(st.nextToken());
		
		foodBoard = new char[n][n];
		for(int x = 0; x < n; x++) {
			String line = br.readLine().trim();
			for(int y = 0; y < n; y++) foodBoard[x][y] = line.charAt(y);
		}
		
		believeBoard = new int[n][n];
		for(int x = 0; x < n; x++) {
			st = new StringTokenizer(br.readLine().trim());
			for(int y = 0; y < n; y++) believeBoard[x][y] = Integer.parseInt(st.nextToken());
		}
		
		solution();
	}
	static final int MINT = 1 << 1;
	static final int CHOCO = 1 << 2;
	static final int MILK = 1 << 3;
	static class Student {
		int food;
		int believe;
		
		Student(int food, int believe) {
			this.food = food;
			this.believe = believe;
		}
	}
	static Student[][] studentBoard;
	static PriorityQueue<Ref> refs;
	static void solution() {
		makeStudentBoard();
		while(t-- > 0) {
			/**
			 * [아침 + 점심]
			 * 아침에 모든 학생 + 1 을 하지 않고,
			 * 그룹 형성 이후, 대표자 + 그룹 크기
			 */
			makeGroup();
			// ===
			printRef();
			printState();
			//===
			/**
			 * [저녁]
			 */
			propagation();
			// ===
			printState();
			// ===
			sb.append(dayResult()).append('\n');
		}
	}
	static void makeStudentBoard() {
		studentBoard = new Student[n][n];
		
		for(int x = 0; x < n; x++) {
			for(int y = 0; y < n; y++) {
				char food = foodBoard[x][y];
				int believe = believeBoard[x][y];
				int foodBit = food == 'T' ? MINT : food == 'C' ? CHOCO : MILK;
				Student student = new Student(foodBit, believe);
				studentBoard[x][y] = student;
			}
		}
	}
	static void makeGroup() {
		refs = new PriorityQueue<>();
		boolean[][] visited = new boolean[n][n];
		for(int x = 0; x < n; x++) {
			for(int y = 0; y < n; y++) {
				if(visited[x][y]) continue;
				Ref ref = findSameGroup(x, y, visited);
				refs.offer(ref);
			}
		}
	}
	static class Ref implements Comparable<Ref> {
		int x, y;
		int food;
		Student student;
		
		Ref(int x, int y, int food, Student student) {
			this.x = x;
			this.y = y;
			this.food = food;
			this.student = student;
		}
		
		public int compareTo(Ref o) {
			int aFoodBit = Integer.bitCount(this.food);
			int bFoodBit = Integer.bitCount(o.food);
			// 음식 1 > 음식 2 > 음식 3
			if(aFoodBit != bFoodBit) return Integer.compare(aFoodBit, bFoodBit);
			// 신앙심 큰 순
			if(this.student.believe != o.student.believe) return Integer.compare(o.student.believe, this.student.believe);
			if(this.x != o.x) return Integer.compare(this.x, o.x);
			return Integer.compare(this.y, o.y);
		}
	}
	static final int[] dx = {-1, 1, 0, 0};
	static final int[] dy = {0, 0, -1, 1};
	static Ref findSameGroup(int x, int y, boolean[][] visited) {
		Queue<int[]> q = new ArrayDeque<>();
		
		int food = studentBoard[x][y].food;
		
		q.offer(new int[] {x, y});
		visited[x][y] = true;
		
		int bestX = n + 1, bestY = n + 1;
		int bestBelieve = 0;
		int size = 0;
		while(!q.isEmpty()) {
			int[] cur = q.poll();
			Student curStudent = studentBoard[cur[0]][cur[1]];
			if(bestBelieve < curStudent.believe ||
					(bestBelieve == curStudent.believe && (bestX > cur[0] || 
							(bestX == cur[0] && bestY > cur[1])))) {
				bestX = cur[0];
				bestY = cur[1];
				bestBelieve = curStudent.believe;
			}
			size++;
			for(int dir = 0; dir < 4; dir++) {
				int nx = cur[0] + dx[dir];
				int ny = cur[1] + dy[dir];
				if(isNotBoard(nx, ny) || visited[nx][ny]) continue;
				if(studentBoard[nx][ny].food != food) continue;
				visited[nx][ny] = true;
				q.offer(new int[] {nx, ny});
			}
		}
		
		Student refStudent = studentBoard[bestX][bestY];
		refStudent.believe += size;
		return new Ref(bestX, bestY, food, refStudent);
	}
	static void propagation() {
		boolean[][] defense = new boolean[n][n];
		while(!refs.isEmpty()) {
			Ref ref = refs.poll();
			Student refStudent = ref.student;
			int x = ref.x;
			int y = ref.y;
			int food = ref.food;
			// 방어 상태라면 전파를 하지 않는다. 
			if(defense[x][y]) continue;
			
			// 전파 가능
			int power = refStudent.believe - 1;		// 간절함
			int dir = refStudent.believe % 4;
			
			while(true) {
				int nx = x + dx[dir];
				int ny = y + dy[dir];
				if(isNotBoard(nx, ny)) break;
				Student target = studentBoard[nx][ny];
				if(target.food == refStudent.food) {
					// 전파자 이동
					x = nx;
					y = ny;
					continue;
				}
				defense[nx][ny] = true;
				if(target.believe < power) {		// 강한 전파
					power -= (target.believe + 1);
					target.believe++;
					target.food = food;
				} else {		// 약힌 전파
					target.believe += power;
					power = 0;
					target.food |= food;
				}
				// 간절함이 0 이 되면 전파 종료
				if(power == 0) break;
				// 전파자 이동
				x = nx;
				y = ny;
			}
			
			refStudent.believe = 1;
		}
	}
	static String dayResult() {
		int mcml = 0;
		int mc = 0;
		int mml = 0;
		int cml = 0;
		int ml = 0;
		int c = 0;
		int m = 0;
		
		for(int x = 0; x < n; x++) {
			for(int y = 0; y < n; y++) {
				Student s = studentBoard[x][y];
				if(s.food == (MINT | CHOCO | MILK)) mcml += s.believe;
				else if(s.food == (MINT | CHOCO)) mc += s.believe;
				else if(s.food == (MINT | MILK)) mml += s.believe;
				else if(s.food == (MILK | CHOCO)) cml += s.believe;
				else if(s.food == (MILK)) ml += s.believe;
				else if(s.food == (CHOCO)) c += s.believe;
				else if(s.food == (MINT)) m += s.believe;
			}
		}
		return String.format("%d %d %d %d %d %d %d", mcml, mc, mml, cml, ml, c, m);
	}
	// ===
	static boolean isNotBoard(int x, int y) {
		return x < 0 || y < 0 || x >= n || y >= n;
	}
	// ===
	static void printRef() {
		for(Ref ref : refs) {
			System.out.printf("(%d, %d) - food : %d - believe : %d\n", ref.x + 1, ref.y + 1, Integer.bitCount(ref.food), ref.student.believe);
		}
		System.out.println();
	}
	static void printState() {
		for(int x = 0; x < n; x++) {
			for(int y = 0; y < n; y++) {
				System.out.print(studentBoard[x][y].believe+" ");
			}
			System.out.println();
		}
	}
}
