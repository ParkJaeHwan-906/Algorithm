package oct.week1.codetree;

import java.util.*;
import java.io.*;

public class 산타의선물공장2_박재환 {
	public static void main(String[] args) throws IOException {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		init(br);
		br.close();
	}
	
	static final int SET = 100;
	static final int MOVE= 200;
	static final int SWAP= 300;
	static final int DIVIDE = 400;
	static final int GIFT_QRY = 500;
	static final int BELT_QRY = 600;
	
	static class Gift {
		int id;
		Gift prev;
		Gift next;
		Gift(int id) {
			this.id = id;
			this.prev = null;
			this.next = null;
		}
		void reset() {
			prev = null;
			next = null;
		}
	}
	
	static class Belt {
		Gift head;
		Gift tail;
		int count;
		Belt() {
			this.head = null;
			this.tail = null;
			this.count = 0;
		}
		
		void offerLast(Gift gift) {
			if(head == null) {
				head = gift;
			} else {
				tail.next = gift;
				gift.prev = tail;
			}
			tail = gift;			
			count++;
		}
		
		void offerFirst(Gift gift) {
			if(head == null) {
				tail = gift;
			} else {
				head.prev = gift;
				gift.next = head;
			}
			head = gift;
			count++;
		}
		
		Gift pollFirst() {
			if(head == null) {
				return null;
			}
			Gift gift = head;
			if(head == tail) {
				reset();
			} else {
				head.next.prev = null;
				head = head.next;
				count--;
			}
			gift.reset();
			return gift;
		}
		
		void reset() {
			head = null;
			tail = null;
			count = 0;
		}
	}
	
	static int n, m;
	static Belt[] belts;
	static Gift[] gifts;
	static void init(BufferedReader br) throws IOException {
		StringTokenizer st;
		StringBuilder sb = new StringBuilder();
		int q = Integer.parseInt(br.readLine().trim());
		while(q-- > 0) {
			st = new StringTokenizer(br.readLine().trim());
			int type = Integer.parseInt(st.nextToken());
			if(type == SET) {
				set(st);
			}
			else if(type == MOVE) {
				sb.append(move(st)).append('\n');
			}
			else if(type == SWAP) {
				sb.append(swap(st)).append('\n');
			}
			else if(type == DIVIDE) {
				sb.append(divide(st)).append('\n');
			}
			else if(type == GIFT_QRY) {
				sb.append(giftQry(st)).append('\n');
			}
			else if(type == BELT_QRY) {
				sb.append(beltQry(st)).append('\n');
			}
		}
		System.out.print(sb);
	}
	
	static void set(StringTokenizer st) {
		n = Integer.parseInt(st.nextToken());
		m = Integer.parseInt(st.nextToken());
		belts = new Belt[n + 1];
		for(int i = 1; i <= n; i++) {
			belts[i] = new Belt();
		}
		gifts = new Gift[m + 1];
		for(int i = 1; i <= m; i++) {
			gifts[i] = new Gift(i);
		}
		for(int i = 1; i <= m; i++) {
			int beltId = Integer.parseInt(st.nextToken());
			belts[beltId].offerLast(gifts[i]);
		}
	}
	
	static int move(StringTokenizer st) {
		int from = Integer.parseInt(st.nextToken());
		int to = Integer.parseInt(st.nextToken());
		if(belts[from].count > 0) {
			Gift fromHead = belts[from].head; 
			Gift fromTail= belts[from].tail;
			if(belts[to].count > 0) {
				fromTail.next = belts[to].head;
				belts[to].head.prev = fromTail;
				belts[to].head = fromHead;
			} else {
				belts[to].head = fromHead;
				belts[to].tail = fromTail;
			}
			belts[to].count += belts[from].count;
			belts[from].reset();
		}
		return belts[to].count;
	}
	
	static int swap(StringTokenizer st) {
		int from = Integer.parseInt(st.nextToken());
		int to = Integer.parseInt(st.nextToken());
		Gift fromGift = belts[from].pollFirst();
		Gift toGift = belts[to].pollFirst();
		if(fromGift != null) {
			belts[to].offerFirst(fromGift);
		}
		if(toGift != null) {
			belts[from].offerFirst(toGift);
		}
		return belts[to].count;
	}
	
	static int divide(StringTokenizer st) {
		int from = Integer.parseInt(st.nextToken());
		int to = Integer.parseInt(st.nextToken());
		if(belts[from].count > 1) {
			int size = belts[from].count / 2;
			belts[from].count -= size;
			belts[to].count += size;
			Gift head = belts[from].head;
			Gift tail = belts[from].head;
			size--;
			while(size-- > 0) {
				tail = tail.next;
			}
			Gift fromNewHead = tail.next;
			fromNewHead.prev = null;
			belts[from].head = fromNewHead;
			
			if(belts[to].head != null) {
				belts[to].head.prev = tail;
				tail.next = belts[to].head;
				belts[to].head = head;
			} else {
				belts[to].head = head;
				belts[to].tail= tail;
				tail.next = null;
			}
		}
		return belts[to].count;
	}
	
	static int giftQry(StringTokenizer st) {
		int giftId = Integer.parseInt(st.nextToken());
		Gift gift = gifts[giftId];
		int a = gift.prev == null ? -1 : gift.prev.id;
		int b = gift.next == null ? -1 : gift.next.id;
		return a + (2 * b);
	}
	
	static int beltQry(StringTokenizer st) {
		int beltId = Integer.parseInt(st.nextToken());
		Belt belt = belts[beltId];
		int a = belt.head == null ? -1 : belt.head.id;
		int b = belt.tail == null ? -1 : belt.tail.id;
		int c = belt.count;
		return a + (2 * b) + (3 * c);
	}

	static void printState() {
		for(int i = 1; i <= n; i++) {
			System.out.printf("[BELT %d] loadCount : %d\n", i, belts[i].count);
			Gift cur = belts[i].head;
			while(cur != null) {
				System.out.printf("%d ", cur.id);
				cur = cur.next;
			}
			System.out.println();
		}
		System.out.println();
	}
}

