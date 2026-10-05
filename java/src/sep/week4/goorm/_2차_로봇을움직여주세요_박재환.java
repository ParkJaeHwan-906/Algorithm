package sep.week4.goorm;

import java.util.*;

public class _2차_로봇을움직여주세요_박재환 {
    class Robot {
        int x, y;
        Robot(int x, int y) {
            this.x = x;
            this.y = y;
        }

        void L() {
            this.x -= 1;
        }

        void R() {
            this.x += 1;
        }

        void U() {
            this.y += 1;
        }

        void D() {
            this.y -= 1;
        }
    }

    public int[] solution(String commands) {
        Robot robot = new Robot(0, 0);
        for(char c : commands.toCharArray()) {
            if(c == 'U') {
                robot.U();
            } else if(c == 'D') {
                robot.D();
            } else if(c == 'L') {
                robot.L();
            } else {
                robot.R();
            }
        }
        return new int[] {robot.x, robot.y};
    }

    public static void main(String[] args) {
        _2차_로봇을움직여주세요_박재환 sol = new _2차_로봇을움직여주세요_박재환();
        String commands = "URDDL";
        int[] ret = sol.solution(commands);

        // [실행] 버튼을 누르면 출력 값을 볼 수 있습니다.
        System.out.println("solution 메소드의 반환 값은 " + Arrays.toString(ret) + " 입니다.");
    }
}
