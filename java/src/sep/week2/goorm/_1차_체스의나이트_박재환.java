package sep.week2.goorm;

public class _1차_체스의나이트_박재환 {
    class Loc {
        int x, y;
        Loc(int x, int y) {
            this.x = x;
            this.y = y;
        }
    }

    boolean isNotBoard(int x, int y) {
        return x < 0 || y < 0 || x >= 8 || y>= 8;
    }

    Loc convertLoc(String pos) {
        int x = (pos.charAt(1) - '0') - 1;
        int y = (pos.charAt(0) - 'A');
        return new Loc(x, y);
    }
    public int solution(String pos) {
        Loc knight = convertLoc(pos);

        int[] dx = {1, 2, 1, 2, -1, -2, -1, -2};
        int[] dy = {-2, -1, 1, 2, -2, -1, 2, 1};

        int count = 0;
        for(int dir = 0; dir < 8; dir++) {
            int nx = knight.x + dx[dir];
            int ny = knight.y + dy[dir];
            if(isNotBoard(nx, ny)) {
                continue;
            }
            count++;
        }
        return count;
    }

    // 아래는 테스트케이스 출력을 해보기 위한 main 메소드입니다.
    public static void main(String[] args) {
        _1차_체스의나이트_박재환 sol = new _1차_체스의나이트_박재환();
        String pos = "A7";
        int ret = sol.solution(pos);

        // [실행] 버튼을 누르면 출력 값을 볼 수 있습니다.
        System.out.println("solution 메소드의 반환 값은 " + ret + " 입니다.");
    }
}
