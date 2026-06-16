package jun.week1.programmers.야근지수_박서희;

import java.util.*;

/*
  문제풀이 시간: 0:14:08
  AI 사용 여부: X
 */
public class 야근지수_박서희 {
    public static void main(String[] args) {
        int[] works = {4, 3, 3};
        int n = 4;

        Solution solution = new Solution();
        long result = solution.solution(n, works);
        System.out.println(result);
    }
}

class Solution {

    public long solution(int n, int[] works) {
        int total = Arrays.stream(works).sum();
        if (total <= n) return 0;

        long answer = 0;

        PriorityQueue<Integer> pq = new PriorityQueue<>(Comparator.reverseOrder());
        for (int work: works) pq.add(work);
        while (n > 0 && !pq.isEmpty()) {
            int maxWork = pq.poll();
            pq.add(maxWork -1);
            n--;
        }

        while(!pq.isEmpty()) {
            int reminder = pq.poll();
            answer += (reminder * reminder);
        }

        return answer;
    }
}
