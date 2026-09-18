package sep.week2.goorm;

public class _1차_소용돌이수_박재환 {
    public int solution(int n) {
        int[][] board = makeBoard(n);
        int answer = 0;
        int x = 0, y = 0;
        while(!isNotBoard(x, y, n)) {
            answer += board[x][y];
            x++;
            y++;
        }
        return answer;
    }

    public int[][] makeBoard(int n) {
        int[] dx = {0, 1, 0 ,-1};
        int[] dy = {1, 0, -1, 0};
        int[][] board = new int[n][n];
        boolean[][] visited = new boolean[n][n];
        int num = 0, dir = 0;
        int x = 0, y = 0;
        while(num < n * n) {
            board[x][y] = ++num;
            visited[x][y] = true;
            int nx = x + dx[dir];
            int ny = y + dy[dir];
            if(isNotBoard(nx, ny, n) || visited[nx][ny]) {
                dir = (dir + 1) % 4;
                nx = x + dx[dir];
                ny = y + dy[dir];
            }
            x = nx;
            y = ny;
        }
        return board;
    }

    public boolean isNotBoard(int x, int y, int n) {
        return x < 0 || y < 0 || x >= n || y >= n;
    }

    public static void main(String[] args) {
        _1차_소용돌이수_박재환 sol = new _1차_소용돌이수_박재환();
        int n1 = 3;
        int ret1 = sol.solution(n1);
        // [실행] 버튼을 누르면 출력 값을 볼 수 있습니다.
        System.out.println("solution 메소드의 반환 값은 " + ret1 + " 입니다.");

        int n2 = 2;
        int ret2 = sol.solution(n2);
        // [실행] 버튼을 누르면 출력 값을 볼 수 있습니다.
        System.out.println("solution 메소드의 반환 값은 " + ret2 + " 입니다.");
    }
}
