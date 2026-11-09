package nov.week1.programmers.카펫_박재환;

import java.util.Arrays;

public class 카펫_박재환 {
    public static void main(String[] args) {
        Solution solution = new Solution();
        System.out.println(Arrays.toString(solution.solution(10, 2)));
        System.out.println(Arrays.toString(solution.solution(8, 1)));
        System.out.println(Arrays.toString(solution.solution(24, 24)));
    }
}

class Solution {
    public int[] solution(int brown, int yellow) {
        for(int w = 3; w < (brown / 2); w++) {
            for(int h = 3; h <= w; h++) {
                int b = (w + w + h + h) - 4;
                int y = (w - 2) * (h - 2);
                if(b == brown && y == yellow)
                    return new int[] {w, h};
            }
        }
        return null;
    }
}
