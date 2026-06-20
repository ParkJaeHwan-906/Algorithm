package jun.week2.programmers.셔틀버스_박서희;

import java.util.Arrays;

/*
  문제풀이 시간: 1시간+
  AI 사용 여부: O binarySearch를 써야 하나.. 등 복잡하게 생각하다가 AI한테 핵심 로직 물어봄.
 */
public class 셔틀버스_박서희 {
    public static void main(String[] args) {
        int n = 2;
        int t = 10;
        int m = 2;
        String[] timetable = {"09:10", "09:09", "08:00"};

        Solution solution = new Solution();
        String answer = solution.solution(n, t, m, timetable);
        System.out.println(answer);
    }
}


class Solution {
    static int[] timeToMin;
    static int crews;

    public String solution(int n, int t, int m, String[] timetable) {
        String answer = "";
        crews = timetable.length;
        timeToMin = new int[crews];
        for (int i = 0; i < crews; i++) {
            int hour = Integer.parseInt(timetable[i].substring(0, 2));
            int minute = Integer.parseInt(timetable[i].substring(3, 5));
            timeToMin[i] = 60 * hour + minute;
        }
        Arrays.sort(timeToMin);

        int getOnIdx = 0;
        for (int i = 0; i < n; i++) {
            int curBus = 540 + i * t;

            int cnt = 0;
            for (int j = 0; j < m; j++) {
                if (getOnIdx < crews && timeToMin[getOnIdx] <= curBus) {
                    getOnIdx++;
                    cnt++;
                }
            }
            if (i == n - 1 && cnt == m) {
                int con = timeToMin[getOnIdx - 1] - 1;
                int hour = con / 60;
                int minute = con % 60;

                answer = String.format("%02d", hour) + ":" + String.format("%02d", minute);
            } else if (i == n - 1 && cnt < m) {
                int hour = curBus / 60;
                int minute = curBus % 60;

                answer = String.format("%02d", hour) + ":" + String.format("%02d", minute);
            }
        }

        return answer;
    }
}
