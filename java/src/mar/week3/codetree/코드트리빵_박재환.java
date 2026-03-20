package mar.week3.codetree;

import java.util.*;
import java.io.*;

public class 코드트리빵_박재환 {
    static BufferedReader br;
    public static void main(String[] args) throws IOException {
        br = new BufferedReader(new InputStreamReader(System.in));
        init();
        br.close();
    }
    /**
     * m 명의 사람
     * - 1번 사람은 1분, 2번 사람은 2분, ... m번 사람은 m분에 출발
     *
     * 출발 시간 전에는 격자 밖에 있다.
     * -> 목표로 하는 편의점은 모두 다르다.
     * 
     * [이동 방법]
     * 1. 본인이 가고싶은 편의점 방향을 향해 1칸 이동 (최단 거리로 움직인다.)
     * 		- 여러개인 경우 상 좌 우 하 순으로 움직인다.
     * 2. 편의점에 도착한다면 멈춘다. 다른 사람들은 이때부터 해당 칸을 지나갈 수 없다.
     * 3. 현재 시간이 t분일 때, t <= m 만족 시, t번 사람은 자신이 가고싶은 편의점과 가장 가까운 베이스 캠프 이동
     * 		여러개라면 행 열 작은 순 (이동 시간 소요 X)
     * 		다른 사람이 못지나감
     */
    static class Store {
    	int x, y;
    	
    	Store(int x, int y) {
    		this.x = x;
    		this.y = y;
    	}
    }
    static StringTokenizer st;
    static int n, m;
    static int[][] board;
    static Store[] stores;
    static void init() throws IOException {
    	st = new StringTokenizer(br.readLine().trim());
    	n = Integer.parseInt(st.nextToken());
    	m = Integer.parseInt(st.nextToken());
    	
    	board = new int[n][n];
    	for(int x=0; x<n; x++) {
    		st = new StringTokenizer(br.readLine().trim());
    		/**
    		 * 0 : 빈 공간
    		 * 1 : 베이스 캠프
    		 */
    		for(int y=0; y<n; y++) board[x][y] = Integer.parseInt(st.nextToken());
    	}
    	
    	stores = new Store[m+1];
    	for(int i=1; i<m+1; i++) {
    		st = new StringTokenizer(br.readLine().trim());
            int x = Integer.parseInt(st.nextToken()) - 1;
            int y = Integer.parseInt(st.nextToken()) - 1;
            Store store = new Store(x, y);
            stores[i] = store;
        }
        solution();
    }
    static Queue<int[]> persons;
    static boolean[][] blocked;
    static List<int[]> reservationBlock;
    static int arrived;
    static void solution() {
    	arrived = 0;
    	persons = new ArrayDeque<int[]>();
        reservationBlock = new ArrayList<int[]>();
    	blocked = new boolean[n][n];
    	
    	int time = 0;
    	while(arrived < m) {
    		time++;
            movePerson();
            processReservation();
            reservationBlock.clear();
            if(time <= m) addPerson(time);
            processReservation();
            reservationBlock.clear();
        }
    	System.out.println(time);
    }
    static final int[] dx = {-1,0,0,1};
    static final int[] dy = {0,-1,1,0};
    static void movePerson() {
    	if(persons.isEmpty()) return;
    	
    	// 움직일 수 있는 사람이 있다면
    	// BFS 로 돌려도 되는지 모르겠음
    	Queue<int[]> temp = new ArrayDeque<int[]>();
    	while(!persons.isEmpty()) {
    		int[] person = persons.poll();
    		int id = person[0];
    		int x = person[1];
    		int y = person[2];
    		int[] next = findShortestRoute(id, x, y);
    		// 위치 이동
    		person[1] = next[0];
    		person[2] = next[1];
    		
    		if(stores[id].x == person[1] && stores[id].y == person[2]) {		// 가게에 도착
                reservationBlock.add(new int[] {person[1], person[2]});
    			arrived++;
    			continue;
    		}
    		temp.offer(person);
    	}
    	
    	persons = temp;
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
    static int[] findShortestRoute(int id, int x, int y) {
    	Queue<Node> q = new ArrayDeque<>();
    	boolean[][] visited = new boolean[n][n];
    	
    	q.offer(new Node(x, y, null));
    	visited[x][y] = true;

    	Store s = stores[id];		// 선호하는 편의점
    	while(!q.isEmpty()) {
    		Node cur = q.poll();
    		if(cur.x == s.x && cur.y == s.y) {
    			return recoverRoute(cur);
    		}
    		
    		for(int dir=0; dir<4; dir++) {
    			int nx = cur.x + dx[dir];
    			int ny = cur.y + dy[dir];
    			if(nx < 0 || ny < 0 || nx >= n || ny >= n) continue;
    			if(visited[nx][ny] || blocked[nx][ny]) continue;
    			
    			visited[nx][ny] = true;
    			q.offer(new Node(nx, ny, cur));
    		}
    	}
    	return null;
    }
    static int[] recoverRoute(Node cur) {
        while(cur.prev != null && cur.prev.prev != null) {
            cur = cur.prev;
        }
    	return new int[] {cur.x, cur.y};
    }
    static void addPerson(int time) {
    	Queue<int[]> q = new ArrayDeque<>();
    	boolean[][] visited = new boolean[n][n];
    	Store s = stores[time];
    	
    	q.offer(new int[] {s.x, s.y, 0});
    	visited[s.x][s.y] = true;
    	
    	int bx = 20, by = 20, bd = 500;
    	while(!q.isEmpty()) {
    		int[] cur = q.poll();
    		int x = cur[0];
    		int y = cur[1];
    		int d = cur[2];

            if(board[x][y] == 1) {
                if(bd > d) {
                    bx = x;
                    by = y;
                    bd = d;
                } else if(bd == d && (bx > x || (bx == x && by > y))) {
                    bx = x;
                    by = y;
                }
            }
    		
    		if(bd < d) continue;
    		for(int dir=0; dir<4; dir++) {
    			int nx = x + dx[dir];
    			int ny = y + dy[dir];
    			if(nx < 0 || ny < 0 || nx >= n || ny >= n) continue;
    			if(visited[nx][ny] || blocked[nx][ny]) continue;
    			
    			visited[nx][ny] = true;
    			q.offer(new int[] {nx, ny, d+1});
    		}
    	}
    	persons.offer(new int[] {time, bx, by});
        reservationBlock.add(new int[] {bx, by});
    }
    static void processReservation() {
        for(int[] a : reservationBlock) {
            int x = a[0];
            int y = a[1];
            blocked[x][y] = true;
        }
    }
}
