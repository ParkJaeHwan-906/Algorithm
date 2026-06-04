package jun.week1.programmers.보석쇼핑_박재환;

import java.util.*;

public class 보석쇼핑_박재환 {
    public static void main(String[] args) {
        String[] gems = {"DIA", "RUBY", "RUBY", "DIA", "DIA", "EMERALD", "SAPPHIRE", "DIA"};
        Solution solution = new Solution();
        System.out.println(Arrays.toString(solution.solution(gems)));
    }
}

class Solution {
    Set<String> jewelry;
    public int[] solution(String[] gems) {
        set(gems);

        int l = 0, r = 0;
        int bestL = 0, bestR = gems.length;
        int minLen = Integer.MAX_VALUE;
        Map<String, Integer> map = new HashMap<>();
        while(true) {
            if(map.size() == jewelry.size()) {
                if(minLen > r - l) {
                    minLen = r - l;
                    bestL = l;
                    bestR = r - 1;
                }

                // 하나 감소
                String lGem = gems[l];
                map.put(lGem, map.get(lGem) - 1);

                if(map.get(lGem) == 0) map.remove(lGem);
                l++;
            } else {
                if(r == gems.length) break;
                map.put(gems[r], map.getOrDefault(gems[r], 0) + 1);
                r++;
            }
        }
        return new int[] {bestL + 1, bestR + 1};
    }

    void set(String[] gems) {
        jewelry = new HashSet<>();
        for(String s : gems) jewelry.add(s);
    }
}