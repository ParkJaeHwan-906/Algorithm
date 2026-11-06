package nov.week1.programmers._2개이하로다른비트_박재환;

import java.util.*;

public class _2개이하로다른비트_박재환 {
    public static void main(String[] args) {
        Solution solution = new Solution();
        System.out.println(Arrays.toString(solution.solution(new long[]{2, 7}))); // [3, 11]
    }
}

class Solution {
    public long[] solution(long[] numbers) {
        long[] answer = new long[numbers.length];
        for (int i = 0; i < numbers.length; i++) {
            StringBuilder bits = new StringBuilder(Long.toBinaryString(numbers[i]));
            bits.insert(0, '0');
            for (int j = bits.length() - 1; j >= 0; j--) {
                if (bits.charAt(j) == '0') {
                    bits.setCharAt(j, '1');
                    if (j + 1 < bits.length()) {
                        bits.setCharAt(j + 1, '0');
                    }
                    break;
                }
            }
            answer[i] = Long.parseLong(bits.toString(), 2);
        }
        return answer;
    }
}
