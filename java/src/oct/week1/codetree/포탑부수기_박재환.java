package oct.week1.codetree;

import java.util.*; 
import java.io.*;

public class 포탑부수기_박재환 {
	public static void main(String[] args) throws IOException {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		init(br);
		br.close();
	}
	
	static final int[] dx = {0, 1, 0, -1, 1, 1, -1, -1};
	static final int[] dy = {1, 0, -1, 0, 1, -1, 1, -1};

	static class Tower {
		int id;
		int x, y;
		int power;
		int lastAttack;
		Tower(int id, int x, int y, int power, int lastAttack) {
			this.id = id;
			this.x = x;
			this.y = y;
			this.power = power;
			this.lastAttack = lastAttack;
		}
	}
	static int n, m, k;
	static Tower[][] board;
	static void init(BufferedReader br) throws IOException {
		StringTokenizer st;
		st = new StringTokenizer(br.readLine().trim());
		n = Integer.parseInt(st.nextToken());
		m = Integer.parseInt(st.nextToken());
		k = Integer.parseInt(st.nextToken());
		int towerId = 0;
		board = new Tower[n][m];
		for(int x = 0; x < n; x++) {
			st = new StringTokenizer(br.readLine().trim());
			for(int y = 0; y < m; y++) {
				board[x][y] = new Tower(++towerId, x, y, Integer.parseInt(st.nextToken()), 0);
				if(board[x][y].power == 0) {							// 이미 부서진 포탑
					continue;
				}
			}
		}
		System.out.print(solution());
	}
	static PriorityQueue<Tower> attackTower;
	static PriorityQueue<Tower> targetTower;
	static Set<Tower> skippedTower;
	static int solution() {
		attackTower = new PriorityQueue<>((a, b) -> {
			if(a.power != b.power) {
				return Integer.compare(a.power,  b.power);
			}
			if(a.lastAttack != b.lastAttack) {
				return Integer.compare(b.lastAttack, a.lastAttack);
			}
			if((a.x + a.y) != (b.x + b.y)) {
				return Integer.compare((b.x + b.y), (a.x + a.y));
			}
			return Integer.compare(b.y, a.y);
		});
		targetTower = new PriorityQueue<>((a, b) -> {
			if(a.power != b.power) {
				return Integer.compare(b.power,  a.power);
			}
			if(a.lastAttack != b.lastAttack) {
				return Integer.compare(a.lastAttack, b.lastAttack);
			}
			if((a.x + a.y) != (b.x + b.y)) {
				return Integer.compare((a.x + a.y), (b.x + b.y));
			}
			return Integer.compare(a.y, b.y);
		});
		skippedTower = new HashSet<>();
		int time = 0;
		while(k-- > 0) {
			++time;
			// 0. 현재 활성화된 타워 기록 
			ready();
			// 1. 공격자 선정
			Tower attack = pickAttacker(time);
			// 2 - 1. 타겟 선정
			Tower target = pickTarget(attack);
			// 2 - 2. 공격
			attack(attack, target);
			// 3. 정비
			if(!maintenance()) {
				break;
			}
		}
		return getResult();
	}
	
	static void ready() {
		attackTower.clear();
		targetTower.clear();
		skippedTower.clear();
		for(int x = 0; x < n; x++) {
			for(int y = 0; y < m; y++) {
				if(board[x][y].power == 0) {
					continue;
				}
				attackTower.offer(board[x][y]);
				targetTower.offer(board[x][y]);
			}
		}
	}
	
	static Tower pickAttacker(int time) {
		Tower attack = attackTower.poll();
		attack.lastAttack = time;
		attack.power += (n + m);
		return attack;
	}
	
	static Tower pickTarget(Tower attack) {
		while(!targetTower.isEmpty() && targetTower.peek() == attack) {
			targetTower.poll();
		}
		return targetTower.poll();
	}
	
	static void attack(Tower attack, Tower target) {
		skippedTower.add(attack);
		skippedTower.add(target);
		if(laser(attack, target)) {
			return;
		}
		canon(attack, target);
	}
	
	static class Node {
		int x, y;
		Node prev;
		Node(int x, int y, Node prev) {
			this.x = x;
			this.y = y;
			this.prev = prev;
		}
	}
	
	static boolean laser(Tower attack, Tower target) {
		boolean[][] visited = new boolean[n][m];
		Queue<Node> q = new ArrayDeque<>();
		q.offer(new Node(attack.x, attack.y, null));
		visited[attack.x][attack.y] = true;
		while(!q.isEmpty()) {
			Node cur = q.poll();
			if(cur.x == target.x && cur.y == target.y) {
				applyLaser(cur, attack);
				return true;
			}
			for(int dir = 0; dir < 4; dir++) {
				int nx = (cur.x + dx[dir] + n) % n;
				int ny = (cur.y + dy[dir] + m) % m;
				if(board[nx][ny].power == 0 || visited[nx][ny]) {
					continue;
				}
				visited[nx][ny] = true;
				q.offer(new Node(nx, ny, cur));
			}
		}
		return false;
	}
	
	static void applyLaser(Node cur, Tower attack) {
		board[cur.x][cur.y].power = Math.max(0, board[cur.x][cur.y].power - attack.power);
		cur = cur.prev;
		while(cur.prev != null) {
			board[cur.x][cur.y].power = Math.max(0, board[cur.x][cur.y].power - (attack.power / 2));
			skippedTower.add(board[cur.x][cur.y]);
			cur = cur.prev;
		}
	}
	
	static void canon(Tower attack, Tower target) {
		target.power = Math.max(0, target.power - attack.power);
		for(int dir = 0; dir < 8; dir++) {
			int nx = (target.x + dx[dir] + n) % n;
			int ny = (target.y + dy[dir] + m) % m;
			if(board[nx][ny].power == 0 || board[nx][ny] == attack) {
				continue;
			}
			board[nx][ny].power = Math.max(0, board[nx][ny].power - (attack.power / 2));
			skippedTower.add(board[nx][ny]);
		}
	}
	
	static boolean maintenance() {
		int activedTower = 0;
		for(int x = 0; x < n; x++) {
			for(int y = 0; y < m; y++) {
				if(board[x][y].power == 0) {
					continue;
				}
				activedTower++;
				if(skippedTower.contains(board[x][y])) {
					continue;
				}
				board[x][y].power++;
			}
		}
		return !(activedTower == 1);
	}
	
	static int getResult() {
		int max = 0;
		for(int x = 0; x < n; x++) {
			for(int y = 0; y < m; y++) {
				max = Math.max(max, board[x][y].power);
			}
		}
		return max;
	}
}
