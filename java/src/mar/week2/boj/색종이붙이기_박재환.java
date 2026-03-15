package mar.week2.boj;

import java.util.*;
import java.io.*;

public class 색종이붙이기_박재환 {
    static BufferedReader br;
    public static void main(String[] args) throws IOException {
        br = new BufferedReader(new InputStreamReader(System.in));
        init();
        br.close();
    }
    /**
     * 색종이를 붙일 때, 격자 밖으로 나가서는 안되고, 색종이끼리 겹쳐서도 안된다.
     * 개수는 각 5개씩이다.
     */
    static StringTokenizer st;
    static int[][] board;
    static int min;
    static void init() throws IOException {
        board = new int[10][10];
        min = 26;
        int toFill = 0;
        for(int x=0; x<10; x++) {
            st = new StringTokenizer(br.readLine().trim());
            for(int y=0; y<10; y++) {
                board[x][y] = Integer.parseInt(st.nextToken());
                if(board[x][y] == 1) toFill++;
            }
        }
        if(toFill == 0) {
            System.out.println(0);
            return;
        }
        fullCover(0, new State(), new boolean[10][10], toFill);
        System.out.println(min == 26 ? -1 : min);
    }
    static class State {
        int one;
        int two;
        int three;
        int four;
        int five;

        State() {
            this.one = 0;
            this.two = 0;
            this.three = 0;
            this.four = 0;
            this.five = 0;
        }

        State(int one, int two, int three, int four, int five) {
            this.one = one;
            this.two = two;
            this.three = three;
            this.four = four;
            this.five = five;
        }

        int getSum() {
            return this.one + this.two + this.three + this.four + this.five;
        }
    }
    static void fullCover(int loc, State state, boolean[][] checked, int toFill) {
        if(toFill == 0) {
            min = Math.min(min, state.getSum());
            return;
        }
        if(loc == 100 || state.getSum() >= min) return;

        int x = loc/10;
        int y = loc%10;

        int one = state.one;
        int two = state.two;
        int three = state.three;
        int four = state.four;
        int five = state.five;

        if(board[x][y] == 0 || checked[x][y]) {
            fullCover(loc+1, new State(one, two, three, four, five), checked, toFill);
            return;
        }

        if(five < 5) {
            if(x + 5 <= 10 && y + 5 <= 10) {
                if(canPut(x, y, 5, checked)) {
                    int sum = size5(x, y, checked, true);
                    fullCover(loc + 1, new State(one, two, three, four, five + 1), checked, toFill - sum);
                    size5(x, y, checked, false);
                }
            }
        }
        if(four < 5) {
            if(x + 4 <= 10 && y + 4 <= 10) {
                if(canPut(x, y, 4, checked)) {
                    int sum = size4(x, y, checked, true);
                    fullCover(loc + 1, new State(one, two, three, four + 1, five), checked, toFill - sum);
                    size4(x, y, checked, false);
                }
            }
        }
        if(three < 5) {
            if(x + 3 <= 10 && y + 3 <= 10) {
                if(canPut(x, y, 3, checked)) {
                    int sum = size3(x, y, checked, true);
                    fullCover(loc + 1, new State(one, two, three + 1, four, five), checked, toFill - sum);
                    size3(x, y, checked, false);
                }
            }
        }
        if(two < 5) {
            if(x + 2 <= 10 && y + 2 <= 10) {
                if(canPut(x, y, 2, checked)) {
                    int sum = size2(x, y, checked, true);
                    fullCover(loc + 1, new State(one, two + 1, three, four, five), checked, toFill - sum);
                    size2(x, y, checked, false);
                }
            }
        }
        if(one < 5) {
            if(x + 1 <= 10 && y + 1 <= 10) {
                if(canPut(x, y, 1, checked)) {
                    int sum = size1(x, y, checked, true);
                    fullCover(loc + 1, new State(one + 1, two, three, four, five), checked, toFill - sum);
                    size1(x, y, checked, false);
                }
            }
        }
    }
    /**
     * x, y좌표를 좌상단으로 기준
     */
    static int size1(int x, int y, boolean[][] checked, boolean f) {
        int sum = 0;
        for(int nx=x; nx<x+1; nx++) {
            for(int ny=y; ny<y+1; ny++) {
                checked[nx][ny] = f;
                if(board[nx][ny] == 1) sum++;
            }
        }
        return sum;
    }
    static int size2(int x, int y, boolean[][] checked, boolean f) {
        int sum = 0;
        for(int nx=x; nx<x+2; nx++) {
            for(int ny=y; ny<y+2; ny++) {
                checked[nx][ny] = f;
                if(board[nx][ny] == 1) sum++;
            }
        }
        return sum;
    }
    static int size3(int x, int y, boolean[][] checked, boolean f) {
        int sum = 0;
        for(int nx=x; nx<x+3; nx++) {
            for(int ny=y; ny<y+3; ny++) {
                checked[nx][ny] = f;
                if(board[nx][ny] == 1) sum++;
            }
        }
        return sum;
    }
    static int size4(int x, int y, boolean[][] checked, boolean f) {
        int sum = 0;
        for(int nx=x; nx<x+4; nx++) {
            for(int ny=y; ny<y+4; ny++) {
                checked[nx][ny] = f;
                if(board[nx][ny] == 1) sum++;
            }
        }
        return sum;
    }
    static int size5(int x, int y, boolean[][] checked, boolean f) {
        int sum = 0;
        for(int nx=x; nx<x+5; nx++) {
            for(int ny=y; ny<y+5; ny++) {
                checked[nx][ny] = f;
                if(board[nx][ny] == 1) sum++;
            }
        }
        return sum;
    }
    static boolean canPut(int x, int y, int size, boolean[][] checked) {
        for(int i=x;i<x+size;i++){
            for(int j=y;j<y+size;j++){
                if(board[i][j] == 0) return false;
                if(checked[i][j]) return false;
            }
        }
        return true;
    }
}
