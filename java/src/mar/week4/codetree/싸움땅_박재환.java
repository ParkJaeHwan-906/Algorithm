package mar.week4.codetree;

import java.util.*;
import java.io.*;

public class 싸움땅_박재환 {
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
	static int n, m, k;
	static PriorityQueue<Integer>[][] gunBoard;
	static int[][] playerBoard;
	static Player[] players;
	static void init() throws IOException {
		st = new StringTokenizer(br.readLine().trim());
		n = Integer.parseInt(st.nextToken());
		m = Integer.parseInt(st.nextToken());
		k = Integer.parseInt(st.nextToken());
		
		gunBoard = new PriorityQueue[n][n];
		for(int x=0; x<n; x++) {			
			st = new StringTokenizer(br.readLine().trim());
			for(int y=0; y<n; y++) {
				gunBoard[x][y] = new PriorityQueue<>((a, b) -> Integer.compare(b, a));
				int gun = Integer.parseInt(st.nextToken());
				if(gun == 0) continue;
				gunBoard[x][y].offer(gun);
			}
		}
		
		players = new Player[m+1];
		playerBoard = new int[n][n];
		for(int i=1; i<m+1; i++) {
			st = new StringTokenizer(br.readLine().trim());
			int x = Integer.parseInt(st.nextToken())-1;
			int y = Integer.parseInt(st.nextToken())-1;
			int d = Integer.parseInt(st.nextToken());
			int s = Integer.parseInt(st.nextToken());
			
			Player player = new Player(i, x, y, d, s);
			players[i] = player;
			
			playerBoard[x][y] = i;
		}
		
		play();
		
	}
	static final int[] dx = {-1, 0, 1, 0};
	static final int[] dy = {0, 1, 0, -1};
	static class Player {
		int id;
		int x, y;
		int dir;
		int power;
		int gun;
		int point;
		
		Player(int id, int x, int y, int dir, int power) {
			this.id = id;
			this.x = x;
			this.y = y;
			this.dir = dir;
			this.power = power;
			
			this.gun = 0;
			this.point = 0;
		}
		
		void move(int limit) {
			int nx = x + dx[dir];
			int ny = y + dy[dir];
			
			if(nx < 0 || ny < 0 || nx >= limit || ny >= limit) {
				// 격자를 벗어나는 경우, 방향을 바꾸어 이동
				this.dir = (this.dir + 2) % 4;
				nx = x + dx[dir];
				ny = y + dy[dir];
			}
			
			this.x = nx;
			this.y = ny;
		}
		
		void loseMove(int limit, int[][] playerBoard, PriorityQueue<Integer>[][] gunBoard) {
			// 1. 현 위치에 총을 떨군다.
			gunBoard[x][y].offer(this.gun);
			this.gun = 0;
			// 2. 이동한다. 
			int nx = x + dx[dir];
			int ny = y + dy[dir];
			
			if(nx < 0 || ny < 0 || nx >= limit || ny >= limit || playerBoard[nx][ny] != 0) {
				// 격자를 벗어나는 경우, 이미 다른 플레이어가 있는 경우
				// 시계방향으로 회전하며 빈 칸을 찾음
				for(int i = 1; i < 4; i++) {
					this.dir = (this.dir + 1) % 4;
					nx = x + dx[dir];
					ny = y + dy[dir];
					if(nx < 0 || ny < 0 || nx >= limit || ny >= limit || playerBoard[nx][ny] != 0) continue;
					break;
				}
			}
			
			this.x = nx;
			this.y = ny;
		}
		
		void win(int p) {
			this.point += p;
		}
	}
	
	static void play() {
		while(k-- > 0) {
			// 1. 플레이어 순차 이동
			movePlayer();
//			printInfo();
		}
		getAnswer();
	}
	
	static void movePlayer() {
		for(int i=1; i<m+1; i++) {
			Player p = players[i];
			playerBoard[p.x][p.y] = 0;
			p.move(n);
			// 현재 이동하려는 칸에 이미 다른 플레이어가 있는지
			if(playerBoard[p.x][p.y] == 0) {				// 빈 칸
				// 총을 주움 
				getGun(p);
			} else {	// 플레이어와 마주침
				Player prevP = players[playerBoard[p.x][p.y]];
				int prevPw = prevP.power + prevP.gun;
				int pPw = p.power + p.gun;
				// 기존에 있던 사람이 이동하는 경우
				if(prevPw < pPw || (prevPw == pPw && prevP.power < p.power)) {
					prevP.loseMove(n, playerBoard, gunBoard);
					playerBoard[prevP.x][prevP.y] = prevP.id;
					getGun(prevP);
					getGun(p);
					p.win(pPw - prevPw);
				} else {
					p.loseMove(n, playerBoard, gunBoard);
					getGun(p);
					getGun(prevP);
					prevP.win(prevPw - pPw);
				}
			}
			playerBoard[p.x][p.y] = p.id;
		}
	}
	static void getGun(Player p) {
		if(!gunBoard[p.x][p.y].isEmpty() && gunBoard[p.x][p.y].peek() > p.gun) {
			int change = gunBoard[p.x][p.y].poll();
			int temp = p.gun;
			p.gun = change;
			if(temp > 0) gunBoard[p.x][p.y].offer(temp);
		}
	}
	static void getAnswer() {
		for(int i = 1; i < m + 1; i++) sb.append(players[i].point).append(' ');
		sb.append('\n');
	}
	// ========================================
	static void printInfo() {
		System.out.println("[플레이어]");
		for(int[] arr : playerBoard) System.out.println(Arrays.toString(arr));
		System.out.println();
		System.out.println("[상세정보]");
		for(int i=1; i<m+1; i++) {
			Player p = players[i];
			System.out.printf("id : %d, power : %d, gun : %d\n", p.id, p.power, p.gun);
		}
		System.out.println();
		System.out.println("[총]");
		for(int x=0; x<n; x++) {
			for(int y=0; y<n; y++) {
				if(gunBoard[x][y].isEmpty()) continue;
				System.out.printf("(%d, %d) : ", x, y);
				for(int i : gunBoard[x][y]) System.out.print(i + " ");
				System.out.println();
			}
		}
		System.out.println();
	}
}
