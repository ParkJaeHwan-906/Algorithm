package sep.week4.goorm;

public class _2차_경품당첨자를구해주세요_박재환 {
    public int func_a(int n){
        int ret = 1;
        while(n > 0){
            ret *= 10;
            n--;
        }
        return ret;
    }

    int func_b(int n){
        int ret = 0;
        while(n > 0){
            ret++;
            n /= 10;
        }
        return ret;
    }

    int func_c(int n){
        int ret = 0;
        while(n > 0){
            ret += n%10;
            n /= 10;
        }
        return ret;
    }

    public int solution(int num) {
        int nextNum = num;
        while(true){
            nextNum++;
            int length = func_b(nextNum);
            if(length % 2 != 0)
                continue;

            int divisor = func_a(length / 2);
            int front = nextNum / divisor;
            int back = nextNum % divisor;

            int frontSum = func_c(front);
            int backSum = func_c(back);
            if(frontSum == backSum)
                break;
        }
        return nextNum - num;
    }

    public static void main(String[] args) {
        _2차_경품당첨자를구해주세요_박재환 sol = new _2차_경품당첨자를구해주세요_박재환();
        int num1 = 1;
        int ret1 = sol.solution(num1);

        // [실행] 버튼을 누르면 출력 값을 볼 수 있습니다.
        System.out.println("solution 메소드의 반환 값은 " + ret1 + " 입니다.");

        int num2 = 235386;
        int ret2 = sol.solution(num2);

        // [실행] 버튼을 누르면 출력 값을 볼 수 있습니다.
        System.out.println("solution 메소드의 반환 값은 " + ret2 + " 입니다.");
    }
}
