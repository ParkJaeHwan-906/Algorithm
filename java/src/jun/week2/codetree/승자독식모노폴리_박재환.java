package jun.week2.codetree;

import java.util.*;
import java.io.*;

public class 승자독식모노폴리_박재환 {
    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        init(br);
        br.close();
    }
    static class Player {
        int x, y;           // 플레이어 위치
        int dir;            // 플레이어가 바라보고 있는 방향
        boolean removed;    // 사라짐 여부

        Player(int x, int y, int dir) {
            this.x = x;
            this.y = y;
            this.dir = dir;
        }

        void setDir(int dir) {
            this.dir = dir;
        }
    }

    // 위 아래 왼쪽 오른쪽
    static final int[] dx = {-1, 1, 0, 0};
    static final int[] dy = {0, 0, -1, 1};

    static int n, m, k;
    static int[][] board;
    static Player[] players;
    static int[][][] playerPriorityDir;
    static void init(BufferedReader br) throws IOException {
        StringTokenizer st;

        st = new StringTokenizer(br.readLine().trim());
        n = Integer.parseInt(st.nextToken());
        m = Integer.parseInt(st.nextToken());
        k = Integer.parseInt(st.nextToken());

        board = new int[n][n];
        players = new Player[m + 1];
        for(int x = 0; x < n; x++) {
            st = new StringTokenizer(br.readLine().trim());
            // 0 : 빈 칸
            // n : n번 플레이어
            for(int y = 0; y < n; y++) {
                board[x][y] = Integer.parseInt(st.nextToken());

                if(board[x][y] != 0) {
                    players[board[x][y]] = new Player(x, y, -1);
                    board[x][y] = 0;
                }
            }
        }

        // 플레이어 초기 방향
        st = new StringTokenizer(br.readLine().trim());
        for(int i = 1; i < m + 1; i++) {
            int dir = Integer.parseInt(st.nextToken()) - 1;
            players[i].setDir(dir);
        }

        // 플레이어 우선순위 정하기
        playerPriorityDir = new int[m + 1][4][4];
        for(int i = 1; i < m + 1; i++) {
            for(int dir = 0; dir < 4; dir++) {
                st = new StringTokenizer(br.readLine().trim());
                for (int j = 0; j < 4; j++) {
                    playerPriorityDir[i][dir][j] = Integer.parseInt(st.nextToken()) - 1;
                }
            }
        }

        System.out.println(solution());
    }

    static class Contract {
        int time;
        int x, y;
        int pId;
        int expired;

        Contract(int time, int x, int y, int pId, int expired) {
            this.time = time;
            this.x = x;
            this.y = y;
            this.pId = pId;
            this.expired = expired;
        }
    }

    static int time;
    static int remainPlayer;
    static Queue<Contract> contracts;
    static int[][] contractTime;
    static int solution() {
        time = 0;
        remainPlayer = m;
        contractTime = new int[n][n];       // 계약 시간을 기록
        contracts = new ArrayDeque<>();

        contract(time);

        // 1번 플레이어만 살아남을때까지 반복
        while(remainPlayer > 1 && ++time <= 1000) {
            // 1. 플레이어 이동
            moves();
            refund(time);
            // 2. 독점
            contract(time);
        }
        return remainPlayer == 1 ? time : -1;
    }

    static void refund(int time) {
        while(!contracts.isEmpty() && contracts.peek().expired <= time) {
            Contract contract = contracts.poll();

            if(board[contract.x][contract.y] != contract.pId) continue;
            if(contractTime[contract.x][contract.y] != contract.time) continue;

            board[contract.x][contract.y] = 0;
            contractTime[contract.x][contract.y] = 0;
        }
    }

    static void contract(int time) {
        for(int i = 1; i < m + 1; i++) {
            Player player = players[i];
            if(player.removed) continue;

            // 현 위치에 독점
            if(board[player.x][player.y] != 0 && board[player.x][player.y] < i) {
                player.removed = true;
                remainPlayer--;
                continue;
            }

            board[player.x][player.y] = i;
            contractTime[player.x][player.y] = time;
            contracts.offer(new Contract(time, player.x, player.y, i, time + k));
        }
    }

    static void moves() {
        /**
         * 같은 위치로 돌아가는 경우 버전 관리?
         */
        for(int i = 1; i < m + 1; i++) {
            Player player = players[i];
            if(player.removed) continue;

            int[] nextLoc = move(i, player);
            player.x = nextLoc[0];
            player.y = nextLoc[1];
            player.dir = nextLoc[2];
        }
    }

    static int[] move(int pid, Player player) {
        int[] dirPriority = playerPriorityDir[pid][player.dir];
        int[] nextLoc = null;
        // dirPriority 별로 탐색 -> 빈 칸 > 내 칸
        for(int dir : dirPriority) {
            int nx = player.x + dx[dir];
            int ny = player.y + dy[dir];

            if(isNotBoard(nx, ny)) continue;

            if(board[nx][ny] == 0) return new int[] {nx, ny, dir};
            if(board[nx][ny] == pid && nextLoc == null) nextLoc = new int[] {nx, ny, dir};
        }
        return nextLoc;
    }

    static boolean isNotBoard(int x, int y) {
        return x < 0 || y < 0 || x >= n || y >= n;
    }
}
