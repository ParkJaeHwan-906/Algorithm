package sep.week2.programmers.징검다리_박재환;

import java.util.*;

public class 징검다리_박재환 {
    public static void main(String[] args) {
        int distance = 25;
        int[] rocks = {2, 14, 11, 21, 17};
        int n = 2;
        Solution sol = new Solution();
        System.out.println(sol.solution(distance, rocks, n));
    }
}

class Solution {
    public int solution(int distance, int[] rocks, int n) {
        Arrays.sort(rocks);
        int l = 1, r = distance;
        int result = Integer.MIN_VALUE;
        while(l <= r) {
            int mid = l + (r - l) / 2;
            if(isPossible(mid, rocks, n, distance)) {
                result = Math.max(result, mid);
                l = mid + 1;
            } else {
                r = mid - 1;
            }
        }
        return result;
    }

    boolean isPossible(int mid, int[] rocks, int n, int distance) {
        int removed = 0;
        int last = 0;
        for(int i = 0; i < rocks.length; i++) {
            if(rocks[i] - last < mid) {
                if(++removed > n) {
                    return false;
                }
                continue;
            }
            last = rocks[i];
        }
        if(distance - last < mid) {
            removed++;
        }
        return removed <= n;
    }
}