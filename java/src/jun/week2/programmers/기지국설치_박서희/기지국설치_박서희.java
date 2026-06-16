package jun.week2.programmers.기지국설치_박서희;

/*
  문제풀이 시간: 00:33:49
  AI 사용 여부: X
 */
public class 기지국설치_박서희 {
    public static void main(String[] args) {
        int n = 16;
        int[] stations = {9};
        int w = 2;

        Solution solution = new Solution();
        long result = solution.solution(n, stations, w);
        System.out.println(result);
    }
}

class Solution {
    public int solution(int n, int[] stations, int w) {
        int answer = 0;

        int start = 0;
        while (start < stations.length) {
            int dis = 0;
            if (start == 0) {
                dis = stations[0] - w - 1;
                if (dis > 0) answer += cal(dis, w);
            } else {
                dis = stations[start] - w - stations[start - 1] - w - 1;
                if (dis > 0) answer += cal(dis, w);
            }
            start++;
        }

        int dis = n - stations[stations.length - 1] - w;
        if (dis > 0) answer += cal(dis, w);

        return answer;
    }

    private int cal(int dis, int w) {
        int cnt = dis / (2 * w + 1);
        if (dis % (2 * w + 1) == 0) {
            return cnt;
        }
        return cnt + 1;
    }
}