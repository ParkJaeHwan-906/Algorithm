package oct.week1.codetree;

import java.util.*;
import java.io.*;

public class 코디의보석공방_박재환 {
    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        init(br);
        br.close();
    }
    
    static final int READY = 1;
    static final int ADD = 2;
    static final int SELL = 3;
    static final int DISPLAY = 4;
    static final int COMPOSE = 5;
    
    static int n;
    static List<Jewel> jewels;
    static void init(BufferedReader br) throws IOException {
    	StringTokenizer st;
    	StringBuilder sb = new StringBuilder();
    	int q = Integer.parseInt(br.readLine().trim());
    	while(q-- > 0) {
    		st = new StringTokenizer(br.readLine().trim());
    		int type = Integer.parseInt(st.nextToken());
    		if(type == READY) {
    			ready(st);
    		}
    		else if(type == ADD) {
    			add(st);
    		}
    		else if(type == SELL) {
    			sb.append(sell(st)).append('\n');
    		}
    		else if(type == DISPLAY) {
    			sb.append(display(st)).append('\n');
    		}
    		else if(type == COMPOSE) {
    			sb.append(compose(st)).append('\n');
    		}
    	}
    	System.out.print(sb);
    }
    
    static class Jewel {
    	int w;
    	int v;
    	boolean soldout;
    	Jewel(int w, int v) {
    		this.w = w;
    		this.v = v;
    		this.soldout = false;
    	}
    }
    
    static final Jewel DUMMY = new Jewel(-1, -1);
    
    static void ready(StringTokenizer st) {
    	n = Integer.parseInt(st.nextToken());
    	jewels = new ArrayList<>();
    	jewels.add(DUMMY);
    	for(int i = 1; i <= n; i++) {
    		int w = Integer.parseInt(st.nextToken());
    		int v = Integer.parseInt(st.nextToken());
    		jewels.add(new Jewel(w, v));
    	}
    }
    
    static void add(StringTokenizer st) {
    	int w = Integer.parseInt(st.nextToken());
		int v = Integer.parseInt(st.nextToken());
		jewels.add(new Jewel(w, v));
    }
    
    static int sell(StringTokenizer st) {
    	int id = Integer.parseInt(st.nextToken());
    	if(id > jewels.size() - 1 || jewels.get(id).soldout) {
    		return -1;
    	}
    	jewels.get(id).soldout = true;
    	return jewels.get(id).v;
    }
    
    static int display(StringTokenizer st) {
    	int limit = Integer.parseInt(st.nextToken());
    	int[] maxVal = new int[limit + 1];
    	for(Jewel j : jewels) {
    		if(j == DUMMY || j.soldout) {
    			continue;
    		}
    		for(int w = limit; w >= j.w; w--) {
    			maxVal[w] = Math.max(maxVal[w], maxVal[w - j.w] + j.v);
    		}
    	}
    	return maxVal[limit];
    }
    
    static int compose(StringTokenizer st) {
    	int d = Integer.parseInt(st.nextToken());
    	List<Jewel> temp = new ArrayList<>();
    	for(Jewel j : jewels) {
    		if(j == DUMMY || j.soldout) {
    			continue;
    		}
    		temp.add(j);
    	}
    	Collections.sort(temp, (a, b) -> Integer.compare(a.w, b.w));		
    	int count = 0;
    	for(int i = 0; i < temp.size(); i++) {
    		Jewel j = temp.get(i);
    		int atLeast = j.w - d;
    		int idx = findIdx(temp, atLeast);
    		count += i - idx;
    	}
    	return count;
    } 
    
    static int findIdx(List<Jewel> temp, int target) {
    	int l = 0, r = temp.size();
    	while(l < r) {
    		int mid = l + (r - l) / 2;
    		if(temp.get(mid).w >= target) {
    			r = mid;
    		} else {
    			l = mid + 1;
    		}
    	}
    	return l;
    }
}
