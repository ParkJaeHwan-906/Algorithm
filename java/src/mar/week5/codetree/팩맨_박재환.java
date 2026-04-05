package mar.week5.codetree;

import java.util.*;
import java.io.*;

public class 팩맨_박재환 {
	static BufferedReader br;
	public static void main(String[] args) throws IOException {
		br = new BufferedReader(new InputStreamReader(System.in));
		init();
		br.close();
	}
	static final int[] pdx = {-1, 0, 1, 0};
	static final int[] pdy = {0, -1, 0, 1};
	static class PackMan {
		int x, y;
		
		PackMan(int x, int y) {
			this.x = x;
			this.y = y;
		}
	}
	static final int[] mdx = {-1, -1, 0, 1, 1, 1, 0, -1};
	static final int[] mdy = {0, -1, -1, -1, 0, 1, 1, 1};
	static class Monster {
		int x, y;
		int dir;
		boolean live;
		
		Monster(int x, int y, int dir) {
			this.x = x;
			this.y = y;
			this.dir = dir;
			
			this.live = true;
		}
		
		Monster cloneSelf() {
			return new Monster(this.x, this.y, this.dir);
		}
		
		void turnCounterClockWise() {
			this.dir = (this.dir + 1) % 8;
		}
	}
	
	static StringTokenizer st;
	static int m, t;
	static PackMan packMan;
	static Queue<Monster> monsters;
	static int[][] deathBoard;
	static void init() throws IOException {
		monsters = new ArrayDeque<>();
		deathBoard = new int[4][4];
		
		st = new StringTokenizer(br.readLine().trim());
		m = Integer.parseInt(st.nextToken());		// 몬스터 수
		t = Integer.parseInt(st.nextToken());		// 턴 수
		
		// 1. 팩맨 초기 위치
		st = new StringTokenizer(br.readLine().trim());
		int px = Integer.parseInt(st.nextToken()) - 1;
		int py = Integer.parseInt(st.nextToken()) - 1;
		packMan = new PackMan(px, py);
		
		// 2. 몬스터 초기 위치
		for(int i = 0; i < m; i++) {
			st = new StringTokenizer(br.readLine().trim());
			int mx = Integer.parseInt(st.nextToken()) -1;
			int my = Integer.parseInt(st.nextToken()) - 1;
			int mdir = Integer.parseInt(st.nextToken()) - 1;
			Monster monster = new Monster(mx, my, mdir);
			monsters.offer(monster);
		}
		
		System.out.println(solution());
	}
	
	static int solution() {
		while(t-- > 0) {
			cloneMonster();
			moveMonsters();
			movePackMan();
			removeDeathMonster();
			bornMonster();
			// ==
//			printState();
		}
		
		int monsterCount = 0;
		while(!monsters.isEmpty()) {
			Monster m = monsters.poll();
			if(m.live) monsterCount++;
		}
		return monsterCount;
	}
	/**
	 * [몬스터 복제 시도]
	 * - 몬스터의 현 위치에, 자신과 같은 방향을 갖는 몬스터 복제를 시도한다.
	 */
	static Queue<Monster> eggs;
	static void cloneMonster() {
		eggs = new ArrayDeque<>();
		for(Monster m : monsters) {
			if(!m.live) continue;
			Monster newMonster = m.cloneSelf();
			eggs.offer(newMonster);
		}
	}
	/**
	 * [몬스터 이동]
	 * - 현재 자신이 가진 방향대로 한 칸 이동한다.
	 * - 이동하려는 칸에 시체가 있거나, 팩맨이 있거나, 격자를 벗어나는 경우 : 반시계 방향으로 45 도 회전한다. ( 이동 가능할 때 까지 )
	 * - 만약 이동가능한 곳이 없다면 이동하지 않는다.
	 */
	static List<Monster>[][] monsterBoard;
	static void moveMonsters() {
		monsterBoard = new List[4][4];
		for(int x = 0; x < 4; x++) {
			for(int y = 0; y < 4; y++) monsterBoard[x][y] = new ArrayList<>();
		}
		
		Queue<Monster> temp = new ArrayDeque<>();
		while(!monsters.isEmpty()) {
			Monster m = monsters.poll();
			if(!m.live) continue;		// 죽은 몬스터는 처리하지 않음
			
			moveMonster(m);
			monsterBoard[m.x][m.y].add(m);
			temp.offer(m);
		}
		
		monsters = temp;
	}
	static void moveMonster(Monster m) {
		for(int i = 0; i < 8; i++) {		// 8번을 돌면 결국 같은 방향을 바라보게 됨
			int nx = m.x + mdx[m.dir];
			int ny = m.y + mdy[m.dir];
			if(isNotBoard(nx, ny) || deathBoard[nx][ny] > 0 ||
					(nx == packMan.x && ny == packMan.y)) {
				m.turnCounterClockWise();
				continue;
			}
			
			// 이동 가능
			m.x = nx;
			m.y = ny;
			break;
		}
	}
	/**
	 * [팩맨 이동]
	 * - 총 3 칸을 이동한다.
	 * - 각 이동마나 상 하 좌 우 선택한다. 
	 * - 몬스터를 가장 많이 먹을 수 있는 방향으로 이동한다. ( 같은 조건이 여러개 : 상 좌 하 우 우선순위 )
	 * - 몬스터를 먹으면 그 자리에 시체를 남긴다. ( 알은 먹지 않는다. )
	 * - 시작 칸의 몬스터는 포함하지 않는다.
	 */
	static int maxMonsterCount;
	static List<Monster> removedMonster;
	static int npx, npy;
	static void movePackMan() {
		maxMonsterCount = -1;
		removedMonster = new ArrayList<>();
		// DFS 로 탐색
		int[][] visited = new int[4][4];
		visited[packMan.x][packMan.y]++;
		findMaxMonsterRoute(packMan.x, packMan.y, 0, new ArrayList<Monster>(), visited);
		eatMonster();
	}
	static void findMaxMonsterRoute(int x, int y, int moved, List<Monster> monsterList, int[][] visited) {
		if(moved == 3) {		// 모두 움직인 경우
			if(maxMonsterCount < monsterList.size()) {
				removedMonster = monsterList;
				maxMonsterCount = monsterList.size();
				npx = x;
				npy = y;
			}
			return;
		}
		
		for(int dir = 0; dir < 4; dir++) {
//			System.out.printf("MOVE : %d, DIR : %d\n", moved, dir);
			int nx = x + pdx[dir];
			int ny = y + pdy[dir];
			if(isNotBoard(nx, ny)) continue;
			// 이동가능 
			List<Monster> list = monsterBoard[nx][ny];
			if(visited[nx][ny] == 0) {
				visited[nx][ny]++;
				List<Monster> nextList = computeList(monsterList, list);
				findMaxMonsterRoute(nx, ny, moved + 1, nextList, visited);
			} else {
				visited[nx][ny]++;
				findMaxMonsterRoute(nx, ny, moved + 1, monsterList, visited);
			}
			visited[nx][ny]--;
		}
	}
	static void eatMonster() {
//		System.out.println("[먹을 수 있는 몬스터의 수] " + removedMonster.size());
		for(Monster m : removedMonster) {
			m.live = false;
			deathBoard[m.x][m.y] = 3; 
		}
		packMan.x = npx;
		packMan.y = npy;
	}
	/**
	 * [몬스터 시체 소멸]
	 * - 시체는 2턴 동안 유지된다.
	 */
	static void removeDeathMonster() {
		for(int x = 0; x < 4; x++) {
			for(int y = 0; y < 4; y++) {
				if(deathBoard[x][y] == 0) continue;
				deathBoard[x][y]--;
			}
		}
	}
	/**
	 * [몬스터 복제 완성]
	 * - 얼 형태였던 몬스터가 부화한다.
	 */
	static void bornMonster() {
		while(!eggs.isEmpty()) {
			Monster monster = eggs.poll();
			monsters.offer(monster);
		}
	}
	
	// ===
	static boolean isNotBoard(int x, int y) {
		return x < 0 || y < 0 || x >= 4 || y >= 4;
	}
	static List<Monster> computeList(List<Monster> a, List<Monster> b) {
		List<Monster> temp = new ArrayList<>();
		for(Monster m : a) temp.add(m);
		for(Monster m : b) temp.add(m);
		return temp;
	}
	static void printState() {
		System.out.printf("[팩맨] (%d, %d)\n", packMan.x, packMan.y);
		System.out.println();
		System.out.println("[몬스터]");
		for(Monster m : monsters) {
			System.out.printf("(%d, %d) - %d\n", m.x, m.y, m.dir);
		}
		System.out.println();
		
	}
}
