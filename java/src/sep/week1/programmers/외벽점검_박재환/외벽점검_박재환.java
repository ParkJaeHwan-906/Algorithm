package sep.week1.programmers.외벽점검_박재환;

import java.util.*;

public class 외벽점검_박재환 {
	public static void main(String[] args) {
		int n = 12;
		int[] weak = {1, 5, 6, 10};
		int[] dist = {1, 2, 3, 4};
		
		Solution sol = new Solution();
		System.out.print(sol.solution(n, weak, dist));
	}
}

class Solution {
	static int[] weak;
	static int[] dist;
	static int[] extendedWeak;
	static int answer;
    public int solution(int n, int[] weak, int[] dist) {
    	set(n, weak, dist);
    	makeFriendSeq(dist, 0, new int[dist.length], new boolean[dist.length]);
        return answer > dist.length
        		? -1 : answer;
    }
    
    static void set(int n, int[] weakInput, int[] distInput) {
    	answer = distInput.length + 1;			// 초기 정답은 모든 친구를 사용하는 경우
    	weak = weakInput;
    	dist = distInput;
    	extendedWeak = new int[weakInput.length * 2];		// 2배로 확장
    	for(int i = 0; i < weakInput.length; i++) {
    		extendedWeak[i] = weakInput[i];
    		extendedWeak[i + weakInput.length] = weakInput[i] + n;  
    	}
    }
    
    // =========================================================
    // 친구 순열 만들기 
    // - 친구의 최대 인원 8명, 8! = 완전탐색 가능
    // =========================================================
    static void makeFriendSeq(int[] dist, int selectedId, int[] selected, boolean[] used) {
    	if(selectedId == selected.length) {
    		check(selected);
    		return;
    	}
    	for(int i = 0; i < dist.length; i++) {
    		if(used[i]) {
    			continue;
    		}
    		selected[selectedId] = dist[i];
    		used[i] = true;
    		makeFriendSeq(dist, selectedId + 1, selected, used);
    		used[i] = false;
    	}
    }
    // =========================================================
    // 탐색 시도
    // =========================================================
    static void check(int[] selected) {
    	for(int start = 0; start < weak.length; start++) {
    		// 각 취약점을 출발점으로 모두 시도
    		int friend = 0;
    		int cover = extendedWeak[start] + selected[friend];
    		for(int idx = start; idx < start + weak.length; idx++) {
    			if(extendedWeak[idx] > cover) {
    				friend++;
    				if(friend == dist.length) {
    					break;
    				}
    				cover = extendedWeak[idx] + selected[friend];
    			}
    		}
    		if(friend < dist.length) {
    			answer = Math.min(answer, friend + 1);
    		}
    	}
    }
}