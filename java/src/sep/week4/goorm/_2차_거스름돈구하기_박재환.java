package sep.week4.goorm;

public class _2차_거스름돈구하기_박재환 {
    public int solution(int money) {
        int coin[] = {10, 50, 100, 500, 1000, 5000, 10000, 50000};
        int counter = 0;
        int idx = coin.length - 1;
        while (money > 0){
            counter += (money / coin[idx]);
            money %= coin[idx];
            idx -= 1;
        }
        return counter;
    }

    public static void main(String[] args) {
        _2차_거스름돈구하기_박재환 sol = new _2차_거스름돈구하기_박재환();
        int money = 2760;
        int ret = sol.solution(money);

        // [실행] 버튼을 누르면 출력 값을 볼 수 있습니다.
        System.out.println("solution 메소드의 반환 값은 " + ret + " 입니다.");
    }
}
