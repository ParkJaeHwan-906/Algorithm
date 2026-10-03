package sep.week4.programmers.셔틀버스_박재환;

import java.util.*;

public class 셔틀버스_박재환 {
    public static void main(String[] args) {
        int[][] shuttleInfo = {
            {1, 1, 5},
            {2, 10, 2},
            {2, 1, 2},
            {1, 1, 5},
            {1, 1, 1},
            {10, 60, 45}
        };
        String[][] timetables = {
            {"08:00", "08:01", "08:02", "08:03"},
            {"09:10", "09:09", "08:00"},
            {"09:00", "09:00", "09:00", "09:00"},
            {"00:01", "00:01", "00:01", "00:01", "00:01"},
            {"23:59"},
            {"23:59", "23:59", "23:59", "23:59", "23:59", "23:59", "23:59", "23:59",
             "23:59", "23:59", "23:59", "23:59", "23:59", "23:59", "23:59", "23:59"}
        };
        String[] answers = {"09:00", "09:09", "08:59", "00:00", "09:00", "18:00"};

        Solution solution = new Solution();
        for(int i = 0; i < shuttleInfo.length; i++) {
            int[] info = shuttleInfo[i];
            String result = solution.solution(info[0], info[1], info[2], timetables[i]);
            System.out.printf("예제 %d: 결과=%s, 기대값=%s, 일치=%b%n",
                i + 1, result, answers[i], result.equals(answers[i]));
        }
    }
}

class Solution {
    public String solution(int n, int t, int m, String[] timetable) {
        PriorityQueue<Integer> crews = new PriorityQueue<>();
        set(crews, timetable);
        int time = convertTime("09:00");        // 첫 셔틀 시간
        while(n-- > 1) {
            // 현재 시간 셔틀을 탈 수 있는 크루원들
            int crewCount = 0;
            while(!crews.isEmpty() && crews.peek() <= time) {
                crews.poll();
                if(++crewCount == m) {
                    break;
                }
            }
            time += t;
        }
        int crewCount = 0;
        while(!crews.isEmpty() && crews.peek() <= time && crewCount <= m - 2) {
            crews.poll();
            crewCount++;
        }

        if(crews.isEmpty()) {
            return convertInteger(time);
        }
        return convertInteger(Math.min(time, crews.peek() - 1));
    }
    void set(PriorityQueue<Integer> crews, String[] timetable) {
        for(String time : timetable) {
            crews.offer(convertTime(time));
        }
    }
    int convertTime(String time) {
        String[] arr = time.split(":");
        int hour = Integer.parseInt(arr[0]) * 60;
        int minute = Integer.parseInt(arr[1]);
        return hour + minute;
    }
    String convertInteger(int time) {
        return String.format("%02d:%02d", time / 60, time % 60);
    }
}
