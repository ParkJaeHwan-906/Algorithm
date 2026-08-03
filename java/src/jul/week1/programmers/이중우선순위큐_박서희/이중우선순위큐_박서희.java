package jul.week1.programmers.이중우선순위큐_박서희;

import java.util.*;

/*
  문제풀이 시간: 13분 정도
  AI 사용 여부: X
 */
public class 이중우선순위큐_박서희 {
    public static void main(String[] args) {
        String[] operations = {"I 16", "I -5643", "D -1", "D 1", "D 1", "I 123", "D -1"};
        Solution solution = new Solution();
        int[] answer = solution.solution(operations);
        System.out.println(Arrays.toString(answer));
    }
}

/*
I 숫자	큐에 주어진 숫자를 삽입합니다.
D 1	큐에서 최댓값을 삭제합니다.
D -1	큐에서 최솟값을 삭제합니다.
 */
class Solution {
    static PriorityQueue<Integer> pq1 = new PriorityQueue<>();
    static PriorityQueue<Integer> pq2 = new PriorityQueue<>(Collections.reverseOrder());
    static Map<Integer, Integer> countMap = new HashMap<>();

    public int[] solution(String[] operations) {
        int[] answer = {};
        for (int i = 0; i < operations.length; i++) {
            String[] command = operations[i].split(" ");
            if (command[0].equals("I")) {
                int value = Integer.parseInt(command[1]);
                pq1.offer(value);
                pq2.offer(value);
                countMap.put(value, countMap.getOrDefault(value, 0) + 1);
            } else {
                if (command[1].equals("1")) {
                    while (!pq2.isEmpty()) {
                        int now = pq2.poll();
                        if (countMap.getOrDefault(now, 0) <= 0) continue;
                        countMap.put(now, countMap.get(now) - 1);
                        break;
                    }
                } else {
                    while (!pq1.isEmpty()) {
                        int now = pq1.poll();
                        if (countMap.getOrDefault(now, 0) <= 0) continue;
                        countMap.put(now, countMap.get(now) - 1);
                        break;
                    }
                }
            }
        }
        int pqMax = 0;
        while (!pq2.isEmpty()) {
            int now = pq2.poll();
            if (countMap.getOrDefault(now, 0) <= 0) continue;
            pqMax = now;
            break;
        }
        int pqMin = 0;
        while (!pq1.isEmpty()) {
            int now = pq1.poll();
            if (countMap.getOrDefault(now, 0) <= 0) continue;
            pqMin = now;
            break;
        }
        return new int[]{pqMax, pqMin};
    }
}
