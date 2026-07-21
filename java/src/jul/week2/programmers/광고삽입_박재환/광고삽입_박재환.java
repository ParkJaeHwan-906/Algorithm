package jul.week2.programmers.광고삽입_박재환;

import java.util.Arrays;
import java.util.StringTokenizer;

public class 광고삽입_박재환 {
    public static void main(String[] args) {
        String play_time = "02:03:55";
        String adv_time = "00:14:15";
        String[] logs = {
                "01:20:15-01:45:14",
                "00:40:31-01:00:00",
                "00:25:50-00:48:29",
                "01:30:59-01:53:29",
                "01:37:44-02:02:30"
        };

        Solution sol = new Solution();
        System.out.println(sol.solution(play_time, adv_time, logs));
    }
}

class Solution {
    long[] playTime;
    long[] acc;
    public String solution(String play_time, String adv_time, String[] logs) {
        set(play_time);
        for(String log : logs) {
            StringTokenizer st = new StringTokenizer(log, "-");
            int sTime = convertInt(st.nextToken());
            int eTime = convertInt(st.nextToken());
            playTime[sTime]++;
            playTime[eTime]--;
        }

        for(int i = 1; i < playTime.length; i++) playTime[i] += playTime[i - 1];

        acc = new long[playTime.length];
        for(int i = 1; i < playTime.length; i++) acc[i] = acc[i - 1] + playTime[i - 1];

        long max = -1;
        int sTime = -1;
        int avdTime = convertInt(adv_time);
        for(int i = 0; i + avdTime < playTime.length; i++) {
            long t = acc[i + avdTime] - acc[i];
            if(max < t) {
                max = t;
                sTime = i;
            }
        }
        return convertString(sTime);
    }

    void set(String play_time) {
        int i = convertInt(play_time);
        playTime = new long[i + 1];
    }

    int convertInt(String time) {
        StringTokenizer st = new StringTokenizer(time, ":");
        int hours = Integer.parseInt(st.nextToken());
        int minutes = Integer.parseInt(st.nextToken());
        int seconds = Integer.parseInt(st.nextToken());
        return seconds + (60 * minutes) + (60 * 60 * hours);
    }

    String convertString(long time) {
        long hours = time / 3600;
        time %= 3600;
        long minutes = time / 60;
        long seconds = time % 60;

        return String.format("%02d:%02d:%02d", hours, minutes, seconds);
    }
}