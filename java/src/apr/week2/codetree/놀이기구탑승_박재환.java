package apr.week2.codetree;

import java.util.*;
import java.io.*;

public class 놀이기구탑승_박재환 {
    static BufferedReader br;
    public static void main(String[] args) throws IOException {
        br = new BufferedReader(new InputStreamReader(System.in));
        init();
        br.close();
    }
    static StringTokenizer st;
    static int n;
    static int studentsCount;
    static int[][] board;
    static int[] seq;
    static boolean[][] likes;
    static void init() throws IOException {
        n = Integer.parseInt(br.readLine().trim());
        studentsCount = n * n;

        seq = new int[studentsCount];
        likes = new boolean[studentsCount + 1][studentsCount + 1];
        for(int i = 0; i < studentsCount; i++) {
            st = new StringTokenizer(br.readLine().trim());
            int id = Integer.parseInt(st.nextToken());
            seq[i] = id;
            for(int j = 0; j < 4; j++) {
                int like = Integer.parseInt(st.nextToken());
                likes[id][like] = true;
            }
        }

        System.out.println(solution());
    }

    static long solution() {
        board = new int[n][n];

        for(int id : seq) {
            // 순서대로 학생들을 앉힘
            arrange(id);
        }

        // 점수 계산
        return getScore();
    }
    static final int[] dx = {0, 1, 0, -1};
    static final int[] dy = {1, 0, -1, 0};
    static void arrange(int id) {
        /**
         * 1. 인접한 칸에 좋아하는 친구 수가 가장 많은 위치
         * 2. 인접한 칸 중 빈 칸이 가장 많은 위치
         * 3. 행 번호 작은 위치
         * 4. 열 번호 작은 위치
         */
        int bestX = Integer.MAX_VALUE;
        int bestY = Integer.MAX_VALUE;
        int bestBlank = -1;
        int bestLike = -1;

        for(int x = 0; x < n; x++) {
            for(int y = 0; y < n; y++) {
                if(board[x][y] != 0) continue;      // 이미 누군가가 앉아 있음

                int like = 0;
                int blank = 0;
                for(int dir = 0; dir < 4; dir++) {
                    int nx = x + dx[dir];
                    int ny = y + dy[dir];
                    if(isNotBoard(nx, ny)) continue;

                    if(board[nx][ny] == 0) blank++;
                    else if(likes[id][board[nx][ny]]) like++;
                }

                if(bestLike < like) {
                    bestLike = like;
                    bestBlank = blank;
                    bestX = x;
                    bestY = y;
                } else if(bestLike == like && bestBlank < blank) {
                    bestBlank = blank;
                    bestX = x;
                    bestY = y;
                } else if(bestLike == like && bestBlank == blank &&
                        (bestX > x || (bestX == x && bestY > y))) {
                    bestX = x;
                    bestY = y;
                }
            }
        }

        board[bestX][bestY] = id;
    }
    static long getScore() {
        long totalScore = 0;
        for(int x = 0; x < n; x++) {
            for (int y = 0; y < n; y++) {
                int id = board[x][y];

                int like = 0;
                for (int dir = 0; dir < 4; dir++) {
                    int nx = x + dx[dir];
                    int ny = y + dy[dir];
                    if (isNotBoard(nx, ny)) continue;
                    if (likes[id][board[nx][ny]]) like++;
                }
                if(like == 0) continue;
                int score = 1;
                for(int i = 1; i < like; i++) {
                    score *= 10;
                }
                totalScore += score;
            }
        }
        return totalScore;
    }
    static boolean isNotBoard(int x, int y) {
        return x < 0 || y < 0 || x >= n || y >= n;
    }
}
