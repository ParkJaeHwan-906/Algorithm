package jun.week1.programmers.보석쇼핑_박서희;

import java.util.*;

/*
  문제풀이 시간: 0:46:25
  AI 사용 여부: X
 */
public class 보석쇼핑_박서희 {
    public static void main(String[] args) {
        String[] arr = {"DIA", "RUBY", "RUBY", "DIA", "DIA", "EMERALD", "SAPPHIRE", "DIA"};
        Solution solution = new Solution();
        System.out.println(Arrays.toString(solution.solution(arr)));
    }

}

class Solution {

    public int[] solution(String[] gems) {
        int start = -1, end = -1;
        int minL = Integer.MAX_VALUE;

        int totalCnt = new HashSet<>(Arrays.asList(gems)).size();
        Map<String, Integer> countGems = new HashMap<>();

        int left = 0, right = 0; // 투 포인터
        while (right < gems.length) {
            countGems.put(gems[right], countGems.getOrDefault(gems[right], 0) + 1);

            while (countGems.size() == totalCnt) {
                int curL = right - left;
                if (curL < minL) {
                    minL = curL;
                    start = left + 1;
                    end = right + 1;
                }

                String leftGem = gems[left];
                countGems.put(leftGem, countGems.get(leftGem) - 1);

                if (countGems.get(leftGem) == 0) {
                    countGems.remove(leftGem);
                }
                left++;
            }
            right++;
        }

        return new int[]{start, end};
    }
}
