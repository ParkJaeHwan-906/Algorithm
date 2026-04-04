package mar.week5.codetree;

import java.util.*;
import java.io.*;

public class Sam의피자학교_박재환 {
	static BufferedReader br;
	public static void main(String[] args) throws IOException {
		br = new BufferedReader(new InputStreamReader(System.in));
		init();
		br.close();
	}
	static StringTokenizer st;
	static int n, k;
	static int[] dow;
	static void init() throws IOException {
		st = new StringTokenizer(br.readLine().trim());
		n = Integer.parseInt(st.nextToken());
		k = Integer.parseInt(st.nextToken());
		
		dow = new int[n];
		st = new StringTokenizer(br.readLine().trim());
		for(int i = 0; i < n; i++) dow[i] = Integer.parseInt(st.nextToken());
		
		System.out.println(solution());
	}
	static int solution() {
		int turn = 0;
		
		while(isOk()) {
			addPowder();		// 최솟값 + 1
			/**
			 * 핵심
			 * roll, fold
			 */
			List<Pair> list = roll();
			push(list);
			list = fold();
			push(list);
			
			turn++;
		}
		
		return turn;
	}
	static void addPowder() {
		int min = Integer.MAX_VALUE;
		for(int i : dow) min = Math.min(i, min);
		
		for(int i = 0; i <n; i++) {
			if(dow[i] == min) dow[i]++;
		}
	}   
	static List<Pair> roll() {
	    List<Pair> list = new ArrayList<>();
	    
	    list.add(new Pair(1, 1));
	    list.add(new Pair(2, 1));
	    int r = 2, c = 1;
	    
	    int sId = 2;			// 지금까지 사용한 뭔소의 수
	    while(sId + r <= n) {
	    	/**
	    	 * 지금까지의 도우를 회전 
	    	 */
	    	rotate(list, r, c);
	    	/**
	    	 * 새로운 도우 추가
	    	 */
	    	for(int i = 1; i <= r; i++) {
	    		list.add(new Pair(c + 1, i));
	    		sId++;
	    	}
	    	
	    	if(r == c + 1) c++;
	    	else r++;
	    	
	    }
	    // 남은 도우
	    int remain = 1;
	    while(sId < n) {
	    	list.add(new Pair(r, c + remain));
	    	sId++;
	    	remain++;
	    }
	    return list;
	}
	static void push(List<Pair> list) {
	    int[] temp = new int[n];
	    for(int i = 0; i < n; i++) temp[i] = dow[i];
	    
	    for(int i = 0; i < n; i++) {
	    	for(int j = i + 1; j < n; j++) {
	    		int x1 = list.get(i).x, y1 = list.get(i).y;
	    		int x2 = list.get(j).x, y2 = list.get(j).y;
	    		if(isAdj(x1, y1, x2, y2)) {
	    			if(dow[i] > dow[j]) {
	    				temp[i] -= (dow[i] - dow[j]) / 5;
	    				temp[j] += (dow[i] - dow[j]) / 5;
	    			} else {
	    				temp[i] += (dow[j] - dow[i]) / 5;
	    				temp[j] -= (dow[j] - dow[i]) / 5;
	    			}
	    		}
	    	}
	    }
	    
	    for(int i = 0; i < n; i++) dow[i] = temp[i];
	    makeArr(list);
	}
	static List<Pair> fold() {
		List<Pair> list = new ArrayList<>();
		for(int i = 0; i < n / 2; i++) list.add(new Pair(1, n / 2 - i));
		for(int i = n / 2; i < n; i++) list.add(new Pair(2, i - (n / 2) + 1));
		
		for(int i = 0; i < n; i++) {
			int x = list.get(i).x, y = list.get(i).y;
			
			if(y <= n / 4) {
				list.set(i, new Pair(3 - x, n / 4 - y + 1));
			} else list.set(i, new Pair(x + 2, y - n / 4));
		}
		return list;
	}
	
	// =====
	static class Pair {
		int x, y;
		
		Pair(int x, int y) {
			this.x = x;
			this.y = y;
		}
	}
	static void rotate(List<Pair> list, int r, int c) {
		for(int i = 0; i < list.size(); i++) {
			int x = list.get(i).x, y = list.get(i).y;
			list.set(i, new Pair(y, r - x + 1));
		}
	}
	static boolean isAdj(int x1, int y1, int x2, int y2) {
		return Math.abs(x1 - x2) + Math.abs(y1 - y2) == 1;
	} 
	static void makeArr(List<Pair> list) {
		int[] temp = new int[n];
		
		List<int[]> newList = new ArrayList<int[]>();
		for(int i = 0; i < n; i++) {
			Pair p = list.get(i);
			newList.add(new int[] {p.x, p.y, i});
		}
		newList.sort((a, b) -> {
            if(a[1] != b[1]) return Integer.compare(a[1], b[1]); // 열이 우선적으로 정렬 기준
            return Integer.compare(b[0], a[0]); // x
        });
        for(int i = 0; i < n; i++) {
            int prev = newList.get(i)[2];
            temp[i] = dow[prev];
        }
        for(int i = 0; i < n; i++) {
            dow[i] = temp[i];
        }
	}
	static boolean isOk() {
		int max = Integer.MIN_VALUE;
		int min = Integer.MAX_VALUE;
		
		for(int i : dow) {
			min = Math.min(min, i);
			max = Math.max(max, i);
		}
		return max - min > k;
	}
} 
