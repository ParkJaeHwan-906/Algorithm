package aug.week5.codetree;

import java.util.*;
import java.io.*;

public class 꼬리잡기놀이_박재환 {
    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        init(br);
        br.close();
    }

    static final int[] shootDir = {0, 3, 2, 1};
    static final int[] dx = {0, 1, 0, -1};
    static final int[] dy = {1, 0, -1, 0};

    static int n, m, k;
    static int[][] board;
    static void init(BufferedReader br) throws IOException {
        StringTokenizer st;

        st = new StringTokenizer(br.readLine().trim());
        n = Integer.parseInt(st.nextToken());           // 격자 크기
        m = Integer.parseInt(st.nextToken());           // 팀 수
        k = Integer.parseInt(st.nextToken());           // 라운드 수

        board = new int[n][n];
        for(int x = 0; x < n; x++) {
            st = new StringTokenizer(br.readLine().trim());
            for(int y = 0; y < n; y++) {
                board[x][y] = Integer.parseInt(st.nextToken());
            }
        }
        System.out.println(solution());
    }

    static List<int[]>[] teams;
    static int[][] teamBoard;
    static int solution() {
        set();
        int score = 0;
        for(int i = 0; i < k; i++) {       // 라운드별 진행
            moveTeams();
            score += throwBall(i);
        }
        return score;
    }

    static void set() {
        teams = new List[m];
        for(int i = 0; i < m; i++) {
            teams[i] = new ArrayList<>();
        }

        int id = 0;
        teamBoard = new int[n][n];
        for(int x = 0; x < n; x++) {
            for(int y = 0; y < n; y++) {
                if(board[x][y] != 1) {
                    continue;
                }
                // 머리를 찾은 경우
                findTeam(id++, new int[] {-1, -1}, new int[] {x, y});
            }
        }

        // board 에 teamId 로 기록
        for(int i = 0; i < m; i++) {
            for(int[] loc : teams[i]) {
                teamBoard[loc[0]][loc[1]] = i + 1;
            }
        }
    }

    static void findTeam(int teamId, int[] prev, int[] cur) {
        teams[teamId].add(cur);
        if(board[cur[0]][cur[1]] == 3) {        // 현 위치가 꼬리인 경우 -> 종료
            return;
        }

        // 꼬리가 아니라면 다음 사람을 찾는다.
        boolean find = false;
        for(int dir = 0; dir < 4; dir++) {          // 중간 사람이 있다면 중간 사람을 찾는다.
            int nx = cur[0] + dx[dir];
            int ny = cur[1] + dy[dir];
            if(isNotBoard(nx, ny) || (prev[0] == nx && prev[1] == ny)
                    || board[nx][ny] == 0 || board[nx][ny] == 4) {
                // 격자를 벗어나거나, 이미 지나온 경로거나, 팀원이 아닌 경우
                continue;
            }
            if(board[nx][ny] == 2) {
                find = true;
                findTeam(teamId, cur, new int[]{nx, ny});
            }
        }

        if(find) {
            return;
        }

        for(int dir = 0; dir < 4; dir++) {      // 중간 사람이 없다면 머리 -> 꼬리 구조이다.
            int nx = cur[0] + dx[dir];
            int ny = cur[1] + dy[dir];
            if(isNotBoard(nx, ny) || (prev[0] == nx && prev[1] == ny)
                    || board[nx][ny] == 0 || board[nx][ny] == 4) {
                // 격자를 벗어나거나, 이미 지나온 경로거나, 팀원이 아닌 경우
                continue;
            }
            if(board[nx][ny] == 3) {
                findTeam(teamId, cur, new int[]{nx, ny});
            }
        }
    }

    // ======================================
    // 각 팀 이동
    // ======================================
    static void moveTeams() {
        for(int i = 0; i < m; i++) {
            moveTeam(i + 1, teams[i]);
        }
    }

    static void moveTeam(int teamId, List<int[]> team) {
        // 머리가 다음으로 이동할 위치를 찾아보낸다.
        // 이때 꼬리 위치만 자른다. -> 전체 이동 필요 없음
        int[] head = team.get(0);
        int[] tail = team.get(team.size() - 1);
        for(int dir = 0; dir < 4; dir++) {
            int nx = head[0] + dx[dir];
            int ny = head[1] + dy[dir];

            if(isNotBoard(nx, ny)) {        // 격자 밖인 경우
                continue;
            }

            boolean emptyRoute = board[nx][ny] != 0 && teamBoard[nx][ny] == 0;
            boolean isTail = tail[0] == nx && tail[1] == ny;

            // 머리가 이동할 수 있는 위치는 [이동 경로 or 꼬리]
            if(emptyRoute || isTail) {
                int[] newHead = new int[] {nx, ny};
                team.remove(tail);
                teamBoard[tail[0]][tail[1]] = 0;
                team.add(0, newHead);
                teamBoard[nx][ny] = teamId;
                break;
            }
        }
    }

    // ======================================
    // 공 던지기
    // ======================================
    static int throwBall(int round) {
        int r = round % (4 * n);
        int sx, sy, dir;
        if (r < n) {
            int offset = r;
            sx = offset;
            sy = 0;
            dir = 0;

        } else if (r < 2 * n) {
            int offset = r - n;
            sx = n - 1;
            sy = offset;
            dir = 3;

        } else if (r < 3 * n) {
            int offset = r - 2 * n;
            sx = n - 1 - offset;
            sy = n - 1;
            dir = 2;

        } else {
            int offset = r - 3 * n;
            sx = 0;
            sy = n - 1 - offset;
            dir = 1;
        }
        return findHitPerson(sx, sy, dir);
    }

    static int findHitPerson(int x, int y, int dir) {
        while(!isNotBoard(x, y)) {      // 공이 격자 밖으로 나갈 때 까지
            if(teamBoard[x][y] != 0) {      // 누군가 맞은 경우
                int teamId = teamBoard[x][y] - 1;
                int seq = findHitPersonSeq(teamId, new int[] {x, y});
                Collections.reverse(teams[teamId]);
                return seq * seq;
            }
            x += dx[dir];
            y += dy[dir];
        }
        return 0;
    }

    static int findHitPersonSeq(int teamId, int[] loc) {
        List<int[]> team = teams[teamId];
        for(int i = 0; i < team.size(); i++) {
            int[] teamLoc = team.get(i);
            if(teamLoc[0] == loc[0] && teamLoc[1] == loc[1]) {
                return i + 1;
            }
        }
        return -1;
    }

    // ======================================
    // 공통
    // ======================================
    static boolean isNotBoard(int x, int y) {
        return x < 0 || y < 0 || x >= n || y >= n;
    }
    static void printTeams() {
        for(int i = 0; i < m; i++) {
            System.out.print("team " + (i + 1) + " : ");
            for(int[] a : teams[i]) {
                System.out.print(Arrays.toString(a) + " ");
            }
            System.out.println();
        }

        for(int[] arr : board) {
            System.out.println(Arrays.toString(arr));
        }
        System.out.println();
    }
}
