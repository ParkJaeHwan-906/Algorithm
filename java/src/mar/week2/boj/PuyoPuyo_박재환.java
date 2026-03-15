package mar.week2.boj;

import java.util.*;
import java.io.*;

public class PuyoPuyo_박재환 {
    static BufferedReader br;
    public static void main(String[] args) throws IOException {
        br = new BufferedReader(new InputStreamReader(System.in));
        init();
        br.close();
    }
    static StringTokenizer st;
    static char[][] board;
    static void init() throws IOException {
        board = new char[12][6];
        for(int x=0; x<12; x++) {
            String line = br.readLine().trim();
            for(int y=0; y<6; y++) board[x][y] = line.charAt(y);
        }
        System.out.println(playGame());
    }
    static int playGame() {
        int turn = 0;
        while(true) {
            findGroup();
            if(!isGroup) break;
            applyGravity();
            turn++;
        }
        return turn;
    }
    static boolean isGroup;
    static void findGroup() {
        isGroup = false;
        boolean[][] checked = new boolean[12][6];
        for(int x=0; x<12; x++) {
            for(int y=0; y<6; y++) {
                if(board[x][y] == '.') continue;
                if(checked[x][y]) continue;
                checkGroup(x, y, checked);
            }
        }
    }
    static final int[] dx = {0,1,0,-1};
    static final int[] dy = {1,0,-1,0};
    static void checkGroup(int x, int y, boolean[][] checked) {
        char color = board[x][y];
        Queue<int[]> q = new ArrayDeque<>();
        Queue<int[]> q2 = new ArrayDeque<>();
        q.offer(new int[] {x, y});
        checked[x][y] = true;
        while(!q.isEmpty()) {
            int[] cur = q.poll();
            q2.offer(cur);
            for(int dir=0; dir<4; dir++) {
                int nx = cur[0] + dx[dir];
                int ny = cur[1] + dy[dir];
                if(nx < 0 || ny < 0 || nx >= 12 || ny >= 6) continue;
                if(board[nx][ny] != color) continue;
                if(checked[nx][ny]) continue;
                checked[nx][ny] =  true;
                q.offer(new int[] {nx, ny});
            }
        }
        if(q2.size() >= 4) removeBlock(q2);
    }
    static void removeBlock(Queue<int[]> q) {
        isGroup = true;
        while(!q.isEmpty()) {
            int[] cur = q.poll();
            int x = cur[0];
            int y = cur[1];
            board[x][y] = '.';
        }
    }
    static void applyGravity() {
        for(int y=0; y<6; y++) {
            int bottom = 11;
            for (int x=11; x>-1; x--) {
                if(board[x][y] != '.') {
                    char temp = board[x][y];
                    if(x != bottom) {
                        board[bottom][y] = temp;
                        board[x][y] = '.';
                    }
                    bottom--;
                }
            }
        }
    }
}
