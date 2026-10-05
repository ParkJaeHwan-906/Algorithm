package sep.week4.goorm;


public class _3차_비숍으로부터도망쳐_박재환 {
    boolean[][] board;
    public int solution(String[] bishops) {
        putBishops(bishops);
        int count = 0;
        for(boolean[] arr : board) {
            for(boolean b : arr) {
                if(!b) {
                    count++;
                }
            }
        }
        return count;
    }

    void putBishops(String[] bishops) {
        board = new boolean[8][8];
        for(String loc : bishops) {
            int x = loc.charAt(1) - '1';
            int y = loc.charAt(0) - 'A';
            board[x][y] = true;
            check(x, y);
        }
    }

    void check(int x, int y) {
        int[] dx = {1, 1, -1, -1};
        int[] dy = {-1, 1, -1, 1};
        for(int dir = 0; dir < 4; dir++) {
            int nx = x;
            int ny = y;
            while(!(nx < 0 || ny < 0 || nx >= 8 || ny >= 8)) {
                board[nx][ny] = true;
                nx += dx[dir];
                ny += dy[dir];
            }
        }
    }

    public static void main(String[] args) {
        _3차_비숍으로부터도망쳐_박재환 sol = new _3차_비숍으로부터도망쳐_박재환();
        String[] bishops1 = {new String("D5")};
        int ret1 = sol.solution(bishops1);

        // [실행] 버튼을 누르면 출력 값을 볼 수 있습니다.
        System.out.println("solution 메소드의 반환 값은 " + ret1 + " 입니다.");

        String[] bishops2 = {new String("D5"), new String("E8"), new String("G2")};
        int ret2 = sol.solution(bishops2);

        // [실행] 버튼을 누르면 출력 값을 볼 수 있습니다.
        System.out.println("solution 메소드의 반환 값은 " + ret2 + " 입니다.");
    }
}
