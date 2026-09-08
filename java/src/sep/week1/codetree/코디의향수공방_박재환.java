package sep.week1.codetree;

import java.util.*;
import java.io.*;

public class 코디의향수공방_박재환 {

	public static void main(String[] args) throws IOException {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		init(br);
		br.close();
	}
	
	static final int INF = Integer.MAX_VALUE;
	
	static final int SET = 1;
	static final int ADD = 2;
	static final int DEL = 3;
	static final int BLAND = 4;	
	static final int COMPOSE = 5;	
	
	static class Spices {
		int s;		// 향도
		boolean removed;
		Spices(int s) {
			this.s = s;
			this.removed = false;
		}
		Spices(int s, boolean removed) {
			this.s = s;
			this.removed = removed;
		}
	}
	
	static final Spices DUMMY = new Spices(-1, true);
	
	static int n;
	static List<Spices> spiceses;
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
			else if(type == ADD) { 
				add(st); 
			}
			else if(type == DEL) { 
				int result = del(st);
				sb.append(result).append('\n');
			}
			else if(type == BLAND) {
				int result = bland(st);
				sb.append(result).append('\n');
			}
			else if(type == COMPOSE) {
				int result = compose(st);
				sb.append(result).append('\n');
			}
		} 
		System.out.print(sb);
	}
	
	// =================================================
	// SET
	// =================================================
	static void set(StringTokenizer st) {
		n = Integer.parseInt(st.nextToken());
		spiceses = new ArrayList<>();
		spiceses.add(DUMMY);			// 1-based 로 시작
		for(int i = 1; i <= n; i++) {
			int s = Integer.parseInt(st.nextToken());
			spiceses.add(new Spices(s));
		}
	}
	// =================================================
	// ADD
	// =================================================
	static void add(StringTokenizer st) {
		int s = Integer.parseInt(st.nextToken());
		spiceses.add(new Spices(s));
		
	}
	// =================================================
	// DEL
	// =================================================
	static int del(StringTokenizer st) {
		int idx = Integer.parseInt(st.nextToken());
		if(idx >= spiceses.size()) {
			return -1;
		}
		Spices removedSpices = spiceses.get(idx);
		if(removedSpices.removed) {
			return -1;
		}
		removedSpices.removed = true;
		return removedSpices.s;
	}
	// =================================================
	// BLAND
	// =================================================
	static int bland(StringTokenizer st) {
		int k = Integer.parseInt(st.nextToken());
		int[] dp = new int[k + 1];
		
		Arrays.fill(dp, INF);
		dp[0] = 0;
		
		for(Spices spices : spiceses) {
			if(spices.removed) {		// 삭제된 향료는 사용할 수 없다. 
				continue;
			}
			for(int i = spices.s; i <= k; i++) {
				if(dp[i - spices.s] == INF) {
					continue;
				}
				dp[i] = Math.min(dp[i], dp[i - spices.s] + 1);
			}
		}
		return dp[k] == INF 
				? -1 : dp[k];
	}
	// =================================================
	// COMPOSE
	// =================================================
	static int compose(StringTokenizer st) {
		int k = Integer.parseInt(st.nextToken());
		int totalCase = 0;
		List<Integer> temp = new ArrayList<Integer>();
		for(Spices spices : spiceses) {
			if(spices.removed) {
				continue;
			}
			temp.add(spices.s);
		}
		Collections.sort(temp);		// 향도를 기준으로 오름차순 정렬
		for(int i = 0; i < temp.size(); i++) {
			for(int j = 0; j < temp.size(); j++) {
				int sum = temp.get(i) + temp.get(j);
				int remain = k - sum;
				int findIdx = findIndex(temp, remain);
				if(findIdx >= temp.size()) {		// 만들 수 없는 조합
					continue;
				}
				totalCase += (temp.size() - findIdx);
			}
		}
		
		return totalCase;
	}
	static int findIndex(List<Integer> temp, int target) {
		/**
		 * target 이상의 첫 위치 찾기
		 */
		int l = 0, r = temp.size();
		while(l < r) {
			int mid = l + (r - l) / 2;
			if(temp.get(mid) >= target) {
				// 조건을 만족하는 부분 target 이상 -> 더 작은 만족하는 값이 있는지 탐색 
				r = mid;
			} else {
				l = mid + 1;
			}
		}
		return l;
	}
}
