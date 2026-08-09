package aug.week1.programmers.무지의먹방라이브_박재환;

import java.util.ArrayList;
import java.util.List;
import java.util.PriorityQueue;

public class 무지의먹방라이브_박재환 {
    public static void main(String[] args) {
        int[] food_times = {3, 1, 2};
        int k = 5;
        Solution sol = new Solution();
        System.out.println(sol.solution(food_times, k));
    }
}

class Solution {
    public int solution(int[] food_times, long k) {
        // 가지치기
        // 모든 음식을 먹는데 걸리는 시간이 k 이하일 경우
        if(getAllTime(food_times) <= k) return -1;

        PriorityQueue<int[]> pq = new PriorityQueue<>((a, b) -> Integer.compare(a[1], b[1]));
        for(int i = 0; i < food_times.length; i++){
            pq.offer(new int[]{i, food_times[i]});      // 음식을 먹는데 걸리는 시간이 적은 순으로 정렬
        }

        long accTime = 0;
        long prev = 0;
        long len = food_times.length;
        while(!pq.isEmpty() && accTime + (pq.peek()[1] - prev) * len <= k) {
            // 현재 음식을 다 먹는데 필요한 시간 -> 만큼 회전해야함
            int[] cur = pq.poll();
            accTime += (cur[1] - prev) * len;
            len--;
            prev = cur[1];
        }

        int remain = (int) ((k - accTime) % len);
        List<int[]> foods = new ArrayList<>();
        while(!pq.isEmpty()) foods.add(pq.poll());
        foods.sort((a, b) -> Integer.compare(a[0], b[0]));
        return foods.get(remain)[0] + 1;
    }

    long getAllTime(int[] food_times) {
        long sum = 0;
        for(int i : food_times) sum += i;
        return sum;
    }
}