package sep.week4.goorm;

import java.util.ArrayDeque;

public class _3차_전광판문구출력_박재환 {
    public String solution(String phrases, int second) {
        ArrayDeque<Character> dq = new ArrayDeque<>();
        for(int i = 0; i < 14; i++) {
            dq.offerLast('_');
        }

        int id = 0;
        while(second-- > 0) {
            dq.offerLast(phrases.charAt(id++));
            dq.pollFirst();
            id %= phrases.length();
        }
        StringBuilder sb = new StringBuilder();
        while(!dq.isEmpty()) {
            sb.append(dq.pollFirst());
        }
        return sb.toString();
    }

    public static void main(String[] args) {
        _3차_전광판문구출력_박재환 sol = new _3차_전광판문구출력_박재환();
        String phrases = new String("happy-birthday");
        int second = 3;
        String ret = sol.solution(phrases, second);
        // [실행] 버튼을 누르면 출력 값을 볼 수 있습니다.
        System.out.println("solution 메소드의 반환 값은 \"" + ret + "\" 입니다.");
    }
}
