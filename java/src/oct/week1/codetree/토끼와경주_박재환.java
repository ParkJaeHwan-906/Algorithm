package oct.week1.codetree;

import java.util.*;
import java.io.*;

public class 토끼와경주_박재환 {
	public static void main(String[] args) throws IOException {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		init(br);
		br.close();
	}
	
	static final int SET = 100;
	static final int RUN = 200;
	static final int CHANGE = 300;
	static final int QUERY = 400;
	
	static final int[] dx = {0, 1, 0, -1};
	static final int[] dy = {1, 0, -1, 0};
	
	static class Rabbit implements Comparable<Rabbit> {
		int id;
		int x, y;
		int d;
		int jumpCount;
		long score;
		Rabbit(int id, int d) {
			this.id = id;
			this.x = 0;
			this.y = 0;
			this.d = d;
			this.jumpCount = 0;
			this.score = 0L;
		}
		@Override
		public int compareTo(Rabbit o) {
			if(this.jumpCount != o.jumpCount) 
				return Integer.compare(this.jumpCount, o.jumpCount);
			if((this.x + this.y) != (o.x + o.y)) 
				return Integer.compare((this.x + this.y), (o.x + o.y));
			if(this.x != o.x) 
				return Integer.compare(this.x, o.x);
			if(this.y != o.y) 
				return Integer.compare(this.y, o.y);
			return Integer.compare(this.id, o.id);
		}
	}
	
	static int n, m, p;
	static Map<Integer, Rabbit> rabbits;
	static PriorityQueue<Rabbit> rabbitPq;
	static long bestScore;
	static void init(BufferedReader br) throws IOException {
		StringTokenizer st;
		StringBuilder sb = new StringBuilder();
		int q = Integer.parseInt(br.readLine().trim());
		while(q-- > 0) {
			st = new StringTokenizer(br.readLine().trim());
			int type = Integer.parseInt(st.nextToken());
			if(type == SET) 
				set(st);
			else if(type == RUN)
				rabbitRun(st);
			else if(type == CHANGE)
				change(st);
			else if(type == QUERY)
				sb.append(query()).append('\n');
		}
		System.out.print(sb);
	}
	
	static void set(StringTokenizer st) {
		bestScore = 0;
		n = Integer.parseInt(st.nextToken());
		m = Integer.parseInt(st.nextToken());
		p = Integer.parseInt(st.nextToken());
		rabbits = new HashMap<>();
		rabbitPq = new PriorityQueue<>();
		for(int i = 0; i < p; i++) {
			int id = Integer.parseInt(st.nextToken());
			int d = Integer.parseInt(st.nextToken());
			Rabbit rabbit = new Rabbit(id, d);
			rabbits.put(id, rabbit);
			rabbitPq.offer(rabbit);
		}
	}
	
	static void rabbitRun(StringTokenizer st) {
		Set<Rabbit> movedRabbit = new HashSet<>();
		int k = Integer.parseInt(st.nextToken());
		int s = Integer.parseInt(st.nextToken());
		int accScore = 0;
		while(k-- > 0) {
			Rabbit rabbit = rabbitPq.poll();
			Loc nextLoc = findNextLoc(rabbit);
			rabbit.x = nextLoc.x;
			rabbit.y = nextLoc.y;
			rabbit.score -= (nextLoc.x + nextLoc.y + 2);
			rabbit.jumpCount++;
			accScore += (nextLoc.x + nextLoc.y + 2);
			rabbitPq.offer(rabbit);
			movedRabbit.add(rabbit);
		}
		
		for(Rabbit r : rabbits.values()) {
			r.score += accScore;
			bestScore = Math.max(bestScore, r.score);
		}
		
		Rabbit bestRabbit = null;
		for(Rabbit rabbit : movedRabbit) {
			if(bestRabbit == null)
				bestRabbit = rabbit;
			else
				bestRabbit = compare(rabbit, bestRabbit);
		}
		bestRabbit.score += s;
		bestScore = Math.max(bestScore, bestRabbit.score);
	}
	
	static Rabbit compare(Rabbit a, Rabbit b) {
		if((a.x + a.y) != (b.x + b.y))
			return (b.x + b.y) > (a.x + a.y)
					? b : a;
		if(a.x != b.x)
			return b.x > a.x
					? b : a;
		if(a.y != b.y)
			return b.y > a.y
					? b : a;
		return b.id > a.id
				? b : a;
	}
	
	static class Loc {
		int x, y;
		Loc(int x, int y) {
			this.x = x;
			this.y = y;
		}
	}
	
	static Loc compare(Loc a, Loc b) {
		if((a.x + a.y) != (b.x + b.y)) {
			return (a.x + a.y) > (b.x + b.y)
					? a : b;
		}
		if(a.x != b.x) {
			return a.x > b.x
					? a : b;
		}
		return a.y > b.y
				? a : b;
	}
	
	static Loc findNextLoc(Rabbit rabbit) {
		Loc nextLoc = null;
		for(int dir = 0; dir < 4; dir++) {
			Loc candLoc = moveRabbit(rabbit, dir);
			if(nextLoc == null)
				nextLoc = candLoc;
			else
				nextLoc = compare(nextLoc, candLoc);
		}
		return nextLoc;
	}
	
	static Loc moveRabbit(Rabbit rabbit, int dir) {
	    int x = rabbit.x;
	    int y = rabbit.y;
	    if (dir == 0)           
	        y = reflect(y, rabbit.d, m, true);
	    else if (dir == 1)    
	        x = reflect(x, rabbit.d, n, true);
	    else if (dir == 2) 
	        y = reflect(y, rabbit.d, m, false);
	    else                 
	        x = reflect(x, rabbit.d, n, false);
	    return new Loc(x, y);
	}

	static int reflect(int start, long distance, int size, boolean positive) {
	    long cycle = 2L * (size - 1);
	    long d = distance % cycle;
	    long pos = positive
	            ? (start + d) % cycle
	            : (start - d + cycle) % cycle;
	    return (int) (pos < size ? pos : cycle - pos);
	}
	 
	static void change(StringTokenizer st) {
		int id = Integer.parseInt(st.nextToken());
		int l = Integer.parseInt(st.nextToken());
		rabbits.get(id).d *= l;
	}
	
	static long query() {
		return bestScore;
	}
}
