package aug.week5.codetree;

import java.util.*;
import java.io.*;

public class 미로타워디펜스_박재환 {
    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        init(br);
        br.close();
    }

    static class Loc {
        int x, y;

        Loc(int x, int y) {
            this.x = x;
            this.y = y;
        }

        boolean isSame(int x, int y) {
            return this.x == x && this.y == y;
        }
    }

    static final int[] dx = {0, 1, 0, -1};
    static final int[] dy = {1, 0, -1, 0};

    static int n, m;
    static int[][] board;

    static long score;
    static Loc player;
    static void init(BufferedReader br) throws IOException {
        StringTokenizer st;

        st = new StringTokenizer(br.readLine().trim());
        n = Integer.parseInt(st.nextToken());
        m = Integer.parseInt(st.nextToken());

        board = new int[n][n];
        player = new Loc(n / 2, n / 2);
        for(int x = 0; x < n; x++) {
            st = new StringTokenizer(br.readLine().trim());
            for(int y = 0; y < n; y++) {
                board[x][y] = Integer.parseInt(st.nextToken());
            }
        }

        score = 0L;
        for(int i = 0; i < m; i++) {
            st = new StringTokenizer(br.readLine().trim());
            int d = Integer.parseInt(st.nextToken());       // 공격 방향
            int p = Integer.parseInt(st.nextToken());       // 공격 칸 수
            attack(d, p);
            List<Integer> monsters = removeChain();
            remakeBoard(monsters);
        }
        System.out.print(score);
    }

    // ======================================
    //  공격
    // ======================================
    static void attack(int d, int p) {
        // d 방향으로 p 칸 공격
        for(int i = 1; i <= p; i++) {
            int nx = player.x + (dx[d] * i);
            int ny = player.y + (dy[d] * i);
            if(isNotBoard(nx, ny)) continue;
            score += board[nx][ny];
            board[nx][ny] = 0;
        }
    }

    // ======================================
    //  삭제
    // ======================================
    static List<Integer> removeChain() {
        List<Integer> monsters = getMonsters();
        fillEmpty(monsters);

        while(true) {
            if(!removed(monsters)) {
                break;
            }
            fillEmpty(monsters);
        }
        return monsters;
    }

    static void fillEmpty(List<Integer> monsters) {
        for(int i = monsters.size() - 2; i >= 0; i--) {
            if(monsters.get(i) != 0) {
                continue;
            }

            for(int j = i - 1; j >= 0; j--) {
                if(monsters.get(j) == 0) {
                    continue;
                }

                int iNum = monsters.get(i);
                int jNum = monsters.get(j);
                monsters.set(i, jNum);
                monsters.set(j, iNum);
                break;
            }
        }
    }

    static boolean removed(List<Integer> monsters) {
        boolean removed = false;
        for(int i = monsters.size() - 2; i >= 0; i--) {
            int count = 1;
            int iNum = monsters.get(i);
            if(iNum == 0) {
                continue;
            }
            for(int j = i - 1; j >= 0; j--) {
                if(monsters.get(j) == iNum) {
                    count++;
                    continue;
                }
                break;
            }

            if(count < 4) {
                continue;
            }
            removed = true;
            // 4번 이상 반복되는 경우
            for(int j = 0; j < count; j++) {
                monsters.set(i - j, 0);
            }
            score += (count * iNum);
        }
        return removed;
    }

    // ======================================
    //  미로 재구성
    // ======================================
    static void remakeBoard(List<Integer> monsters) {
        Deque<Integer> newMonsters = makeNewMonsters(monsters);
        boolean[][] visited = new boolean[n][n];
        board = new int[n][n];
        int dir = 0;
        int x = 0, y = 0;
        board[x][y] = newMonsters.pollFirst();
        visited[x][y] = true;
        while(!player.isSame(x, y)) {
            int nx = x + dx[dir];
            int ny = y + dy[dir];

            if(isNotBoard(nx, ny) || visited[nx][ny]) {        // 격자를 벗어나는 경우 회전
                dir = (dir + 1) % 4;
                nx = x + dx[dir];
                ny = y + dy[dir];
            }
            x = nx; y = ny;
            board[x][y] = newMonsters.pollFirst();
            visited[x][y] = true;
        }
    }

    static Deque<Integer> makeNewMonsters(List<Integer> monsters) {
        Deque<Integer> newMonsters = new ArrayDeque<>();
        newMonsters.offerFirst(0);          // player
        for(int i = monsters.size() - 2; i >= 0; i--) {
            int count = 1;
            int iNum = monsters.get(i);
            if (iNum == 0) {
                continue;
            }
            for(int j = i - 1; j >= 0; j--) {
                if(monsters.get(j) == iNum) {
                    count++;
                    continue;
                }
                break;
            }

            for(int j = 0; j < count; j++) {
                monsters.set(i - j, 0);
            }

            newMonsters.offerFirst(count);
            newMonsters.offerFirst(iNum);
        }

        // 격자에 들어가지 못하는 바깥쪽 몬스터 제거
        while (newMonsters.size() > monsters.size()) {
            newMonsters.pollFirst();
        }
        while(newMonsters.size() < monsters.size()) {
            newMonsters.offerFirst(0);
        }
        return newMonsters;
    }

    // ======================================
    //  공통
    // ======================================
    static boolean isNotBoard(int x, int y) {
        return x < 0 || y < 0 || x >= n || y >= n;
    }
    static void printBoard() {
        for(int[] arr : board) System.out.println(Arrays.toString(arr));
    }
    static List<Integer> getMonsters() {
        List<Integer> monsters = new ArrayList<>();
        boolean[][] visited = new boolean[n][n];

        int dir = 0;
        int x = 0, y = 0;
        monsters.add(board[x][y]);
        visited[x][y] = true;
        while(!player.isSame(x, y)) {
            int nx = x + dx[dir];
            int ny = y + dy[dir];

            if(isNotBoard(nx, ny) || visited[nx][ny]) {        // 격자를 벗어나는 경우 회전
                dir = (dir + 1) % 4;
                nx = x + dx[dir];
                ny = y + dy[dir];
            }
            x = nx; y = ny;
            monsters.add(board[x][y]);
            visited[x][y] = true;
        }

        return monsters;
    }
}
