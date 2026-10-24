package oct.week3.programmers.여행경로_박재환;

import java.util.*;

public class 여행경로_박재환 {
    public static void main(String[] args) {
        Solution solution = new Solution();
        String[][][] tickets = {
            {{"ICN", "JFK"}, {"HND", "IAD"}, {"JFK", "HND"}},
            {{"ICN", "SFO"}, {"ICN", "ATL"}, {"SFO", "ATL"}, {"ATL", "ICN"}, {"ATL", "SFO"}},
            {{"ICN", "BBB"}, {"BBB", "ICN"}, {"ICN", "AAA"}}
        };
        String[][] expected = {
            {"ICN", "JFK", "HND", "IAD"},
            {"ICN", "ATL", "ICN", "SFO", "ATL", "SFO"},
            {"ICN", "BBB", "ICN", "AAA"}
        };

        for (int i = 0; i < tickets.length; i++) {
            String[] result = solution.solution(tickets[i]);
            System.out.println("테스트 " + (i + 1));
            System.out.println("expected: " + Arrays.toString(expected[i]));
            System.out.println("result:   " + Arrays.toString(result));
            System.out.println("일치 여부: " + Arrays.equals(expected[i], result));
            System.out.println();
        }
    }
}

class Solution {
    Map<String, PriorityQueue<String>> dests;
    public String[] solution(String[][] tickets) {
        set(tickets);
        Deque<String> resultList = new ArrayDeque<>();
        String cur = "ICN";
        trip(resultList, cur);
        String[] result = new String[resultList.size()];
        for(int i = 0; i < result.length; i++)
            result[i] = resultList.pollFirst();
        return result;
    }

    void trip(Deque<String> result, String cur) {
        PriorityQueue<String> pq = dests.get(cur);
        while(pq != null && !pq.isEmpty()) {
            trip(result, pq.poll());
        }
        result.offerFirst(cur);
    }

    void set(String[][] tickets) {
        dests = new HashMap<>();
        for(String[] ticket : tickets) {
            String from = ticket[0];
            String to = ticket[1];
            dests.computeIfAbsent(from, k -> new PriorityQueue<>()).offer(to);
        }
    }
}
