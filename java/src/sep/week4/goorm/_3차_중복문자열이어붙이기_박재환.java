package sep.week4.goorm;

import java.util.*;

public class _3차_중복문자열이어붙이기_박재환 {
    public int solution(String s1, String s2) {
        int overlap1 = getOverlap(s1, s2);
        int overlap2 = getOverlap(s2, s1);

        return s1.length() + s2.length()
                - Math.max(overlap1, overlap2);
    }

    int getOverlap(String first, String second) {
        int maxLength = Math.min(first.length(), second.length());

        for (int length = maxLength; length > 0; length--) {
            String suffix = first.substring(first.length() - length);
            String prefix = second.substring(0, length);

            if (suffix.equals(prefix)) {
                return length;
            }
        }

        return 0;
    }

    public static void main(String[] args) {
        _3차_중복문자열이어붙이기_박재환 sol = new _3차_중복문자열이어붙이기_박재환();
        String s1 = new String("ababc");
        String s2 = new String("abcdab");
        int ret = sol.solution(s1, s2);

        // [실행] 버튼을 누르면 출력 값을 볼 수 있습니다.
        System.out.println("solution 메소드의 반환 값은 " + ret + " 입니다.");
    }
}
