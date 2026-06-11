package jun.week2.programmers.기지국설치_박재환;

import java.util.*;

/**
 * [풀이 시간]
 * 00:36:32
 * AI 사용 여부 X
 */
public class 기지국설치_박재환 {
    public static void main(String[] args) {
        int n = 11;
        int[] stations = {4, 11};
        int w = 1;

        Solution solution = new Solution();
        System.out.println(solution.solution(n, stations, w));
    }
}

class Solution {
    public int solution(int n, int[] stations, int w) {
        int newStations = 0;
        int last = 1;

        for(int loc : stations) {
            int lCover = loc - w;       // 현재 기지국의 왼쪽 범위
            int disable= lCover - last;
            if(disable > 0) {       // 서비스가 불가능한 지역이 있다면 -> 새로운 단말기 설치
                newStations += (disable % (2 * w + 1) == 0 ? disable / (2 * w + 1) : disable / (2 * w + 1) + 1);
            }
            last = loc + w + 1;     // 새롭게 커버되지 않은 위치
        }

        if(last <= n) {     // 마지막위치까지 커버되지 않는 경우
            int disable = n - last + 1;
            newStations += (disable % (2 * w + 1) == 0 ? disable / (2 * w + 1) : disable / (2 * w + 1) + 1);
        }

        return newStations;
    }
}