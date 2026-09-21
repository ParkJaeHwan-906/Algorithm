package sep.week2.goorm;

public class _1차_타임머신_박재환 {
    public long solution(long num) {
        String tempStr = String.valueOf((num + 1));
        tempStr = tempStr.replace('0', '1');
        return Long.parseLong(tempStr);
    }

    // 아래는 테스트케이스 출력을 해보기 위한 main 메소드입니다.
    public static void main(String[] args) {
        _1차_타임머신_박재환 sol = new _1차_타임머신_박재환();
        long num = 9949999;
        long ret = sol.solution(num);

        // [실행] 버튼을 누르면 출력 값을 볼 수 있습니다.
        System.out.println("solution 메소드의 반환 값은 " + ret + " 입니다.");
    }
}
