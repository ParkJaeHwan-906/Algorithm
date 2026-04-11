package apr.week1.codetree;

import java.util.*;
import java.io.*;

public class 코드트리메신저_박재환 {
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
	static final int ON_OFF = 200;
	static final int CHANGE_AUTH = 300;
	static final int CHANGE_P = 400;
	static final int QUERY = 500; 
	
	static StringTokenizer st;
	static int n, q;
	static Room[] rooms;
	static void init() throws IOException {
		st = new StringTokenizer(br.readLine().trim());
		n = Integer.parseInt(st.nextToken());
		q = Integer.parseInt(st.nextToken());
		
		while(q-- > 0) {
			st = new StringTokenizer(br.readLine().trim());
			int cmd = Integer.parseInt(st.nextToken());
			
			if(cmd == SET) { set(); }
			else if(cmd == ON_OFF) {
				int id = Integer.parseInt(st.nextToken());
				onOff(id);
			}
			else if(cmd == CHANGE_AUTH) {
				int id = Integer.parseInt(st.nextToken());
				int auth = Integer.parseInt(st.nextToken());
				chageAuth(id, auth);
			}
			else if(cmd == CHANGE_P) {
				int id1 = Integer.parseInt(st.nextToken());
				int id2 = Integer.parseInt(st.nextToken());
				changeP(id1, id2);
			}
			else if(cmd == QUERY) {
				int id = Integer.parseInt(st.nextToken());
				int result = query(id);
				sb.append(result).append('\n');
			}
		}
	}
	
	static class Room {
		int id;
		int pId;
		int l, r;
		int auth;
		boolean on;
		
		int[] alert;
		
		Room(int id, int pId) {
			this.id = id;
			this.pId = pId;
			
			this.on = true;
			this.l = 0;
			this.r = 0;
			this.alert = new int[21];		// 트리의 최대 높이는 20
		}
		
		void onOff() {
			this.on = (!this.on);
		}
	}
	
	static void set() {
		rooms = new Room[n + 1];
		for(int i = 1; i < n + 1; i++) {
			int pId = Integer.parseInt(st.nextToken());
			Room room = new Room(i, pId);
			rooms[i] = room;
		}
		for(int i = 1; i < n + 1; i++) addChild(rooms[i].pId, i);
		for(int i = 1; i < n + 1; i++) {
			int auth = Integer.parseInt(st.nextToken());
			rooms[i].auth = Math.min(20, auth);		// 최대 전파를 20으로 제한
		}
		
		buildRooms();
	}
	
	static void buildRooms() {
		for(int i = 1; i < n + 1; i++) {
			if(rooms[i].pId == 0) buildRoom(i);
		}
	}
	static void buildRoom(int cur) {
		int l = rooms[cur].l;
		int r = rooms[cur].r;
		if(l != 0) buildRoom(l);
		if(r != 0) buildRoom(r);
		computeRoom(cur);
	}
	static void computeRoom(int cur) {
		Arrays.fill(rooms[cur].alert, 0);
		
		int range = rooms[cur].auth;
		rooms[cur].alert[range] = 1;
		
		int l = rooms[cur].l;
		int r = rooms[cur].r;
		
		if(l != 0 && rooms[l].on) {
			for(int d = 1; d<= 20; d++) {
				rooms[cur].alert[d - 1] += rooms[l].alert[d];
			}
		}
		if(r != 0 && rooms[r].on) {
			for(int d = 1; d<= 20; d++) {
				rooms[cur].alert[d - 1] += rooms[r].alert[d];
			}
		}
	}
	static void addChild(int pId, int id) {
		if(pId == 0) return;
		Room room = rooms[pId];
		if(room.l == 0) room.l = id;
		else room.r = id;
	}
	
	static void onOff(int id) {
		rooms[id].onOff();
		updateParent(id);
	}
	
	static void updateParent(int cur) {
		while(cur != 0) {
			computeRoom(cur);
			cur = rooms[cur].pId;
		}
	}
	
	static void chageAuth(int id, int auth) {
		rooms[id].auth = Math.min(20, auth);
		updateParent(id);
	}
	
	static void changeP(int id1, int id2) {
		int pId1 = rooms[id1].pId;
		int pId2 = rooms[id2].pId;
		swap(pId1, id1, id2);
		swap(pId2, id2, id1);
		
		rooms[id1].pId = pId2;
		rooms[id2].pId = pId1;
		
		updateParent(pId1);
		updateParent(pId2);
	}
	static void swap(int pId, int from, int to) {
		if(pId == 0) return;
		Room room = rooms[pId];
		if(room.l == from) room.l = to;
		else room.r = to;
	}
	static int query(int id) {
		int alert = 0;
		for(int i : rooms[id].alert) alert += i;
		return alert - 1;
	}
}
