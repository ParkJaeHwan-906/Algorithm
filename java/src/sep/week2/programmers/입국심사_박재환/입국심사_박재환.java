package sep.week2.programmers.입국심사_박재환;

import java.util.Arrays;

public class 입국심사_박재환 {
    public static void main(String[] args) {
        int n = 6;
        int[] times = {7, 10};
        Solution sol = new Solution();
        System.out.println(sol.solution(n, times));
    }
}

class Solution {
    public long solution(int n, int[] times) {
        Arrays.sort(times);
        long l = 1L, r = (long) times[times.length - 1] * n;
        long min = Long.MAX_VALUE;
        while(l <= r) {
            long mid = l + (r - l) / 2;
            if(possible(mid, times, n)) {
                min = Math.min(min, mid);
                r = mid - 1;
            } else {
                l = mid + 1;
            }
        }
        return min;
    }

    boolean possible(long mid, int[] times, int n) {
        long total = 0;
        for(int time : times) {
            total += (mid / time);
            if(total >= n) {
                return true;
            }
        }
        return false;
    }
}