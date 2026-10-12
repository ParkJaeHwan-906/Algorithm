package oct.week1.programmers.징검다리_박재환;

import java.util.Arrays;

public class 징검다리_박재환 {
    public static void main(String[] args) {
        int distance = 25;
        int[] rocks = {2, 14, 11, 21, 17};
        int n = 2;
        Solution sol = new Solution();
        System.out.print(sol.solution(distance, rocks, n));
    }
}

class Solution {
    public int solution(int distance, int[] rocks, int n) {
        Arrays.sort(rocks);
        int l = 0, r = distance;
        while(l < r) {
            int mid = l + (r - l + 1) / 2;
            if(isPossible(mid, rocks, n, distance)) {
                l = mid;
            } else {
                r = mid - 1;
            }
        }
        return l;
    }
    boolean isPossible(int d, int[] rocks, int n, int distance) {
        int count = 0;
        int lastLoc = 0;
        for(int i : rocks) {
            if(i - lastLoc < d) {
                if(++count > n) {
                    return false;
                }
                continue;
            }
            lastLoc = i;
        }
        if(distance - lastLoc < d) {
            if(++count > n) {
                return false;
            }
        }
        return true;
    }
}