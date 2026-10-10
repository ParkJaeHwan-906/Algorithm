package oct.week1.programmers.징검다리건너기_박재환;

public class 징검다리건너기_박재환 {
    public static void main(String[] args) {
        int[] stones = {2, 4, 5, 3, 2, 1, 4, 2, 5, 1};
        int k = 3;
        Solution sol = new Solution();
        System.out.print(sol.solution(stones, k));
    }
}

class Solution {
    static final int MAX = 200_000_000;
    public int solution(int[] stones, int k) {
        int l = 0, r = MAX;
        while(l < r) {
            int mid = l + (r - l + 1) / 2;
            if(isPossible(stones, k, mid)) {
                l = mid;
            } else {
                r = mid - 1;
            }
        }
        return l;
    }
    boolean isPossible(int[] stones, int k, int mid) {
        int between = 0;
        for(int i : stones) {
            if(i - mid >= 0) {
                between = 0;
                continue;
            }
            if(++between == k) {
                return false;
            }
        }
        return between < k;
    }
}