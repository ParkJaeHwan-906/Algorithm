package aug.week2.codetree;

import java.util.*;
import java.io.*;

public class 메이즈러너_박재환 {
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
    }

    static class Participant extends Loc {
        boolean exit;
        Participant(int x, int y) {
            super(x, y);
            this.exit = false;
        }
    }

    static class Exit extends Loc{
        Exit(int x, int y) {
            super(x, y);
        }
    }

    static int n, m, k;
    static int[][] board;
    static Participant[] participants;
    static Exit exit;
    static void init(BufferedReader br) throws IOException {
        StringTokenizer st;

        st = new StringTokenizer(br.readLine().trim());
        n = Integer.parseInt(st.nextToken());           // 격자 크기
        m = Integer.parseInt(st.nextToken());           // 참가자 수
        k = Integer.parseInt(st.nextToken());           // 출구 수

        board = new int[n][n];
        for(int x = 0; x < n; x++) {
            st = new StringTokenizer(br.readLine().trim());
            /**
             * 0     : 빈 칸
             * 1 ~ 9 : 벽 내구도
             */
            for(int y = 0; y < n; y++) board[x][y] = Integer.parseInt(st.nextToken());
        }

        participants = new Participant[m + 1];
        for(int i = 1; i < m + 1; i++) {
            st = new StringTokenizer(br.readLine().trim());
            int x = Integer.parseInt(st.nextToken()) - 1;
            int y = Integer.parseInt(st.nextToken()) - 1;
            participants[i] = new Participant(x, y);
        }

        st = new StringTokenizer(br.readLine().trim());
        int x = Integer.parseInt(st.nextToken()) - 1;
        int y = Integer.parseInt(st.nextToken()) - 1;
        exit = new Exit(x, y);
        board[x][y] = -1;

        System.out.print(solution());
    }

    static int exitParticipants;
    static String solution() {
        exitParticipants = 0;

        int totalParticipantsMove = 0;
        while(k-- > 0) {
            // 1. 참가자 이동
            int participantsMove = moveParticipants();
            totalParticipantsMove += participantsMove;
            if(exitParticipants == m) break;
            // 2. 격자 회전
            // 2 - 1. 사각형 추출
            Square square = findMinimumSquare();
            // 2 - 2. 사각형 회전
            rotateClockwise(square);
        }

        return String.format("%d\n%d %d", totalParticipantsMove, exit.x + 1, exit.y + 1);
    }

    static final int[] dx = {-1, 1, 0, 0};
    static final int[] dy = {0, 0, -1, 1};

    static List<Integer>[][] participantsBoard;
    static int moveParticipants() {
        int moveCount = 0;

        participantsBoard = new ArrayList[n][n];
        for (int x = 0; x < n; x++) {
            for (int y = 0; y < n; y++) {
                participantsBoard[x][y] = new ArrayList<>();
            }
        }

        for (int pId = 1; pId <= m; pId++) {
            Participant participant = participants[pId];
            if (participant.exit) continue;

            int originDist = distFromExit(participant.x, participant.y);
            boolean moved = false;
            for (int dir = 0; dir < 4; dir++) {
                int nx = participant.x + dx[dir];
                int ny = participant.y + dy[dir];
                if (isNotBoard(nx, ny)) continue;
                if (board[nx][ny] > 0) continue;

                int newDist = distFromExit(nx, ny);
                if (newDist >= originDist) continue;
                moveCount++;
                moved = true;
                if (newDist == 0) {
                    participant.x = nx;
                    participant.y = ny;
                    participant.exit = true;
                    exitParticipants++;
                } else {
                    participant.x = nx;
                    participant.y = ny;
                    participantsBoard[nx][ny].add(pId);
                }
                break;
            }

            if (!moved) participantsBoard[participant.x][participant.y].add(pId);
        }
        return moveCount;
    }

    static class Square {
        int x, y, size;

        Square(int x, int y, int size) {
            this.x = x;
            this.y = y;
            this.size = size;
        }
    }

    static Square findMinimumSquare() {
        for (int size = 2; size <= n; size++) {         // 최소 한 변의 길이는 2
            for (int x = 0; x + size <= n; x++) {
                for (int y = 0; y + size <= n; y++) {

                    // 출구가 정사각형 내부에 있는지 확인
                    if (!contains(x, y, size, exit.x, exit.y)) continue;

                    // 탈출하지 않은 참가자가 한 명 이상 있는지 확인
                    boolean hasParticipant = false;
                    for (int px = x; px < x + size && !hasParticipant; px++) {
                        for (int py = y; py < y + size; py++) {
                            if (!participantsBoard[px][py].isEmpty()) {
                                hasParticipant = true;
                                break;
                            }
                        }
                    }
                    if (hasParticipant) return new Square(x, y, size);
                }
            }
        }
        return null;
    }

    static void rotateClockwise(Square square) {
        int[][] copy = new int[square.size][square.size];
        List[][] copyP = new List[square.size][square.size];
        for(int x =  0; x < square.size; x++) {
            for(int y = 0; y < square.size; y++) copyP[x][y] = new ArrayList<>();
        }
        for(int x = square.x; x < square.x + square.size; x++) {
            for(int y = square.y; y < square.y + square.size; y++) {
                copy[x - square.x][y - square.y] = board[x][y];
                copyP[x - square.x][y - square.y] = clone(participantsBoard[x][y]);
            }
        }
        int[][] temp = new int[square.size][square.size];
        List[][] tempP = new List[square.size][square.size];
        for(int x = 0; x < square.size; x++) {
            for(int y = 0; y < square.size; y++) {
                temp[y][square.size - 1 - x] = copy[x][y];
                tempP[y][square.size - 1 - x] = copyP[x][y];
            }
        }
        for(int x = square.x; x < square.x + square.size; x++) {
            for(int y = square.y; y < square.y + square.size; y++) {
                board[x][y] = temp[x - square.x][y - square.y] > 0 ? --temp[x - square.x][y - square.y] : temp[x - square.x][y - square.y];
                participantsBoard[x][y] = tempP[x - square.x][y - square.y];
            }
        }

        // 실제 반영
        applyNewVersion();
    }

    static void applyNewVersion() {
        for(int x = 0; x < n; x++) {
            boolean find = false;
            for(int y = 0; y < n; y++) {
                if(board[x][y] == -1) {
                    exit.x = x;
                    exit.y = y;
                    find = true;
                    break;
                }
            }
            if(find) break;
        }

        for(int x = 0; x < n; x++) {
            for(int y = 0; y < n; y++) {
                if(participantsBoard[x][y].isEmpty()) continue;
                for(int pId : participantsBoard[x][y]) {
                    participants[pId].x = x;
                    participants[pId].y = y;
                }
            }
        }
    }

    static boolean contains(int squareX, int squareY, int size, int targetX, int targetY) {
        return squareX <= targetX && targetX < squareX + size && squareY <= targetY && targetY < squareY + size;
    }

    static int distFromExit(int x, int y) {
        return Math.abs(x - exit.x) + Math.abs(y - exit.y);
    }

    static boolean isNotBoard(int x, int y) { return x < 0 || y < 0 || x >= n || y >= n; }

    static List<Integer> clone(List<Integer> list) {
        List<Integer> temp =  new ArrayList<>(list);
        for(int i : list) temp.add(i);
        return temp;
    }
}
