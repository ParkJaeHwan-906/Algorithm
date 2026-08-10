package aug.week2.programmers.입국심사_박재환;

public class 임국심사_박재환 {
    public static void main(String[] args) {
        int n = 6;
        int[] times = {7, 10};
        Solution sol = new Solution();
        System.out.println(sol.solution(n, times));
    }
}

class Solution {
    /**
     * n : 최대 10억
     * times 각 원소 : 최대 10억
     */
    public long solution(int n, int[] times) {
        /**
         * 1. 초기 모든 심사대는 비어있다.
         * 2. 산 심사대에서는 동시에 한 명만 심사를 할 수 있다.
         * 3. 가장 앞에 서 있는 사람은 비어 있는 심사대로 가서 심사를 받을 수 있다.
         *
         * ---
         *
         * 최대 값이 10억 -> 시뮬레이션으로는 불가
         * 이분탐색 사용
         */
        long l = 0, r = (long) 1_000_000_000 * n;
        while(l < r) {
            long mid = l + (r - l) / 2;
            long availableCount = 0;
            for(int i : times) {
                availableCount += mid / i;          // mid 시간동안 처리할 수 있는 수
                if(availableCount >= n) break;      // 가지지치
            }
            if(availableCount < n) {                // 시간이 부족한 경우 시간을 늘려야함
                l = mid + 1;
            } else {
                r = mid;                            // 충분한 경우 (r 범위에 정답 포함)
            }
        }
        return l;
    }
}