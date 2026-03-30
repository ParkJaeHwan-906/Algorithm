package mar.week4.codetree;

import java.util.*;
import java.io.*;

public class 해적선장코디_박재환 {
	static BufferedReader br;
	static StringBuilder sb;
	public static void main(String[] args) throws IOException {
		br = new BufferedReader(new InputStreamReader(System.in));
		sb = new StringBuilder();
		init();
		br.close();
		System.out.println(sb);
	}
	static final int SET = 100;
	static final int HELP = 200;
	static final int CHANGE = 300;
	static final int ATTACK = 400;
	
	static StringTokenizer st;
	static void init() throws IOException {
		int t = Integer.parseInt(br.readLine().trim());
		for(int time = 1; time < t + 1; time++) {
			st = new StringTokenizer(br.readLine().trim());
			int cmd = Integer.parseInt(st.nextToken());
			
			if(cmd == SET) { set(); }
			else if(cmd == HELP) { help(); }
			else if(cmd == CHANGE) { change(); }
			else if(cmd == ATTACK) { attack(time); }
		}
	}
	static class Ship {
		int id;
		int power;
		int reload;
		int ableAttackTime;
		
		Ship(int id, int power, int reload, int ablaAttackTime) {
			this.id = id;
			this.power = power;
			this.reload = reload;
			this.ableAttackTime = ablaAttackTime;
		}
	}
	static int n;
	static Map<Integer, Ship> ships;
	static PriorityQueue<Ship> able;	
	static PriorityQueue<Ship> wait;
	static void set() {
		ships = new HashMap<>();
		able = new PriorityQueue<>((a, b) -> {
			/**
			 * 공격력이 가장 높은 선박 -> id 가 작은 선박
			 */
			if(a.power == b.power) return Integer.compare(a.id, b.id);
			return Integer.compare(b.power, a.power);
		});		
		wait = new PriorityQueue<>((a, b) -> Integer.compare(a.ableAttackTime , b.ableAttackTime));
		
		n = Integer.parseInt(st.nextToken());
		for(int i = 0; i < n; i++) {
			int id = Integer.parseInt(st.nextToken());
			int power = Integer.parseInt(st.nextToken());
			int reload = Integer.parseInt(st.nextToken());
			
			Ship ship = new Ship(id, power, reload, -1);
			ships.put(id, ship);
			able.offer(ship);
		}
	}
	static void help() {
		int id = Integer.parseInt(st.nextToken());
		int power = Integer.parseInt(st.nextToken());
		int reload = Integer.parseInt(st.nextToken());
		
		Ship ship = new Ship(id, power, reload, -1);
		ships.put(id, ship);
		able.offer(ship);
	}
	static void change() {
		/**
		 * 지연 갱신 필요
		 */
		int id = Integer.parseInt(st.nextToken());
		int power = Integer.parseInt(st.nextToken());
		
		Ship origin = ships.get(id);
		Ship newShip = new Ship(id, power, origin.reload, origin.ableAttackTime);
		
		ships.put(id, newShip);
		wait.offer(newShip);
	}
	static void attack(int time) {
		/**
		 * 현재 시간에 포를 쏠 수 있는 선박 업데이트
		 */
		while(!wait.isEmpty()) {
			Ship ship = wait.peek();
			if(ship.ableAttackTime > time) break;
			
			able.offer(wait.poll());
		}
		/**
		 * 사격할 선박을 5척 고른다.
		 */
		int totalDamage = 0;
		int attackedShipCount = 0;
		int[] attackedShip = new int[5];
		while(!able.isEmpty() && attackedShipCount < 5) {
			Ship ship = able.poll();
			if(ships.get(ship.id) != ship) continue;		// 갱신된 함선이라면 패스
			
			// 공격이 가능하다면
			totalDamage += ship.power;
			attackedShip[attackedShipCount++] = ship.id;
			
			// 해당 함선 재장전 상태로
			ship.ableAttackTime = time + ship.reload;
			wait.offer(ship);
		}
		
		sb.append(totalDamage).append(' ').append(attackedShipCount).append(' ');
		for(int i : attackedShip) {
			if(i == 0) break;
			sb.append(i).append(' ');
		}
		sb.append('\n');
	}
}
