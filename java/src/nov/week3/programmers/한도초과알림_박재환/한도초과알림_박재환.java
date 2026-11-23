package nov.week3.programmers.한도초과알림_박재환;

import java.util.*;

public class 한도초과알림_박재환 {
    public static void main(String[] args) {
        Solution solution = new Solution();

        // TC1: 여러 사용자가 알림 조건 만족
        String[] infos1 = {
                "20250601 1001",
                "20250601 1002",
                "20250602 1001",
                "20250603 1001",
                "20250604 1002",
                "20250605 1002",
                "20250606 1002"
        };

        System.out.println("TC1: " +
                Arrays.toString(solution.solution(infos1, 3, 3)));
        // 예상: [1001, 1002]


        // TC2: 알림 대상 없음
        String[] infos2 = {
                "20250601 1001",
                "20250610 1001",
                "20250601 1002",
                "20250602 1002"
        };

        System.out.println("TC2: " +
                Arrays.toString(solution.solution(infos2, 3, 3)));
        // 예상: [-1]


        // TC3: 월이 변경되는 경우
        String[] infos3 = {
                "20250629 1001",
                "20250630 1001",
                "20250701 1001"
        };

        System.out.println("TC3: " +
                Arrays.toString(solution.solution(infos3, 3, 3)));
        // 예상: [1001]


        // TC4: p일 범위를 초과하는 경우
        String[] infos4 = {
                "20250601 1001",
                "20250602 1001",
                "20250604 1001"
        };

        System.out.println("TC4: " +
                Arrays.toString(solution.solution(infos4, 3, 3)));
        // 예상: [-1]


        // TC5: 입력이 날짜순으로 정렬되지 않은 경우
        String[] infos5 = {
                "20250603 1002",
                "20250601 1001",
                "20250602 1002",
                "20250603 1001",
                "20250601 1002",
                "20250602 1001"
        };

        System.out.println("TC5: " +
                Arrays.toString(solution.solution(infos5, 3, 3)));
        // 예상: [1001, 1002]


        // TC6: 같은 날짜에 여러 번 초과
        String[] infos6 = {
                "20250601 1001",
                "20250601 1001",
                "20250601 1001"
        };

        System.out.println("TC6: " +
                Arrays.toString(solution.solution(infos6, 3, 3)));
        // 예상: [1001] (각 기록을 1회로 계산하는 경우)
    }
}

class Solution {

    class Log {
        String uId;
        String date;
        Log(String uId, String date) {
            this.uId = uId;
            this.date = date;
        }
    }

    public int[] solution(String[] infos, int p, int n) {
        Log[] logs = new Log[infos.length];
        for(int i = 0; i < logs.length; i++) {
            String[] arr = infos[i].split(" ");
            String date = arr[0];
            String uId = arr[1];
            logs[i] = new Log(uId, date);
        }
        Arrays.sort(logs, (a, b) -> a.date.compareTo(b.date));
        Map<String, Queue<String>> last = new HashMap<>();
        Set<String> set = new HashSet<>();
        for(Log log : logs) {
            if(last.containsKey(log.uId)) {
                String prev = last.get(log.uId).peek();
                int diff = dayDiff(prev, log.date);
                if(diff < p) {
                    last.get(log.uId).offer(log.date);
                    if(last.get(log.uId).size() >= n)
                        set.add(log.uId);
                } else {
                    while(!last.get(log.uId).isEmpty() && dayDiff(last.get(log.uId).peek(), log.date) >= p) {
                        last.get(log.uId).poll();
                    }
                    last.get(log.uId).offer(log.date);
                    if(last.get(log.uId).size() >= n)
                        set.add(log.uId);
                }
            } else {
                last.computeIfAbsent(log.uId, k -> new ArrayDeque<>()).offer(log.date);
                if(last.get(log.uId).size() >= n)
                    set.add(log.uId);
            }
        }

        if(set.isEmpty())
            return new int[] {-1};
        int id = 0;
        int[] result = new int[set.size()];
        for(String str : set) {
            result[id++] = Integer.parseInt(str);
        }
        Arrays.sort(result);
        return result;
    }

    int dayDiff(String prev, String next) {
        int y1 = Integer.parseInt(prev.substring(0, 4));
        int y2 = Integer.parseInt(next.substring(0, 4));

        int m1 = Integer.parseInt(prev.substring(4, 6));
        int m2 = Integer.parseInt(next.substring(4, 6));

        int d1 = Integer.parseInt(prev.substring(6));
        int d2 = Integer.parseInt(next.substring(6));

        return (y2 - y1) * 12 * 30 + (m2 - m1) * 30 + (d2 - d1);
    }
}