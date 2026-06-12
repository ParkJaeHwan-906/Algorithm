package jun.week2.programmers.셔틀버스_박재환;

import java.util.Arrays;

/**
 * [풀이 시간]
 * 00:34:12
 * AI 사용 여부 O
 * => 문제 이해가 되지 않아 사용
 */
public class 셔틀버스_박재환 {
    public static void main(String[] args) {
        int n = 1;
        int t = 1;
        int m = 5;
        String[] timetable = {"08:00", "08:01", "08:02", "08:03"};

        Solution solution = new Solution();
        System.out.println(solution.solution(n, t, m, timetable));
    }
}

class Solution {
    /**
     * 셔틀은 09:00 시부터 n 회 t 분 간격으로 역에 도착한다.
     *      - 한 셔틀에는 최대 m명의 승객이 탈 수 있다.
     * 처리 순서 : 대기 -> 탑승
     */
    int n, t, m;
    String[] timetable;
    int[] shuttles;
    int[] timetableToInt;
    public String solution(int n, int t, int m, String[] timetable) {
        String answer = "";

        set(n, t, m, timetable);

        int crewId = 0;
        for(int shuttleId = 0; shuttleId < n; shuttleId++) {
            int shuttle = shuttles[shuttleId];
            int board = 0;

            while(crewId < timetableToInt.length && board < m && timetableToInt[crewId] <= shuttle) {
                crewId++;
                board++;
            }

            if(shuttleId == n - 1) {        // 마지막 셔틀인 경우 -> 콘이 탑승해야함
                if(board < m) answer = String.format("%02d:%02d", (shuttle / 60), (shuttle % 60));
                else {
                    int lastBoardTime = timetableToInt[crewId - 1];     // 마지막 탑승 인원
                    lastBoardTime--;
                    answer = String.format("%02d:%02d", (lastBoardTime / 60), (lastBoardTime % 60));
                }
            }
        }

        return answer;
    }

    void set(int n, int t, int m, String[] timetable) {
        this.n = n;
        this.t = t;
        this.m = m;
        this.timetable = timetable;

        shuttles = new int[n];      // 셔틀은 총 n대
        int arriveTime = 60 * 9;    // 첫 셔틀은 9:00 시
        for(int i = 0; i < n; i++) {
            shuttles[i] = arriveTime + (i * t);
        }

        timetableToInt = new int[timetable.length];
        for(int i = 0; i < timetableToInt.length; i++) {
            String[] arr = timetable[i].split(":");
            int hour = 60 * Integer.parseInt(arr[0]);
            int minute = Integer.parseInt(arr[1]);
            timetableToInt[i] = hour + minute;
        }
        Arrays.sort(timetableToInt);        // 도착시간이 빠른 순으로 정렬
    }
}