package jun.week5.codetree;

import java.util.*;
import java.io.*;

/**
 * AI 사용 여부 X
 */
public class _2048게임_박재환 {
    public static void main(String[] args) throws IOException{
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        init(br);
        br.close();
    }

    static int n;
    static int[][] board;
    static void init(BufferedReader br) throws IOException {
        StringTokenizer st;

        n = Integer.parseInt(br.readLine().trim());
        board = new int[n][n];
        for(int x = 0; x < n; x++) {
            st = new StringTokenizer(br.readLine().trim());
            for(int y = 0; y < n; y++) board[x][y] = Integer.parseInt(st.nextToken());
        }

        System.out.println(solution());
    }

    static int maxScore;
    static int solution() {
        maxScore = 0;
        findMaxScore(0, board);
        return maxScore;
    }

    static void findMaxScore(int turn, int[][] arr) {
        if(turn == 5) {
            maxScore = Math.max(maxScore, getMaxScore(arr));
            return;
        }

        findMaxScore(turn + 1, pushDown(arr));
        findMaxScore(turn + 1, pushUp(arr));
        findMaxScore(turn + 1, pushLeft(arr));
        findMaxScore(turn + 1, pushRight(arr));
    }

    static int getMaxScore(int[][] arr) {
        int max = 0;
        for(int[] a : arr) {
            for(int i : a) max = Math.max(max, i);
        }
        return max;
    }

    static class Block {
        int num;
        boolean canMerge;

        public Block(int num) {
            this.num = num;
            this.canMerge = true;
        }
    }

    static int[][] pushDown(int[][] arr) {
        Deque<Block> dq = new ArrayDeque<>();
        int[][] temp = new int[n][n];
        for(int y = 0; y < n; y++) {
            for(int x = n - 1; x >= 0; x--) {
                if(arr[x][y] == 0) continue;

                if(!dq.isEmpty() && dq.peekLast().canMerge && dq.peekLast().num == arr[x][y]) {
                    Block b = dq.pollLast();
                    b.num *= 2;
                    b.canMerge = false;
                    dq.offerLast(b);
                } else {
                    dq.offerLast(new Block(arr[x][y]));
                }
            }

            for(int x = n - 1; x >= 0; x--) {
                if(dq.isEmpty()) break;
                temp[x][y] = dq.pollFirst().num;
            }

            dq.clear();
        }
        return temp;
    }

    static int[][] pushUp(int[][] arr) {
        Deque<Block> dq = new ArrayDeque<>();
        int[][] temp = new int[n][n];
        for(int y = 0; y < n; y++) {
            for(int x = 0; x < n; x++) {
                if(arr[x][y] == 0) continue;

                if(!dq.isEmpty() && dq.peekLast().canMerge && dq.peekLast().num == arr[x][y]) {
                    Block b = dq.pollLast();
                    b.num *= 2;
                    b.canMerge = false;
                    dq.offerLast(b);
                } else {
                    dq.offerLast(new Block(arr[x][y]));
                }
            }

            for(int x = 0; x < n; x++) {
                if(dq.isEmpty()) break;
                temp[x][y] = dq.pollFirst().num;
            }

            dq.clear();
        }
        return temp;
    }

    static int[][] pushLeft(int[][] arr) {
        Deque<Block> dq = new ArrayDeque<>();
        int[][] temp = new int[n][n];

        for (int x = 0; x < n; x++) {
            for (int y = 0; y < n; y++) {
                if (arr[x][y] == 0) continue;

                if (!dq.isEmpty() && dq.peekLast().canMerge && dq.peekLast().num == arr[x][y]) {
                    Block b = dq.pollLast();
                    b.num *= 2;
                    b.canMerge = false;
                    dq.offerLast(b);
                } else {
                    dq.offerLast(new Block(arr[x][y]));
                }
            }

            for (int y = 0; y < n; y++) {
                if (dq.isEmpty()) break;
                temp[x][y] = dq.pollFirst().num;
            }

            dq.clear();
        }

        return temp;
    }

    static int[][] pushRight(int[][] arr) {
        Deque<Block> dq = new ArrayDeque<>();
        int[][] temp = new int[n][n];

        for (int x = 0; x < n; x++) {
            for (int y = n - 1; y >= 0; y--) {
                if (arr[x][y] == 0) continue;

                if (!dq.isEmpty() && dq.peekLast().canMerge && dq.peekLast().num == arr[x][y]) {
                    Block b = dq.pollLast();
                    b.num *= 2;
                    b.canMerge = false;
                    dq.offerLast(b);
                } else {
                    dq.offerLast(new Block(arr[x][y]));
                }
            }

            for (int y = n - 1; y >= 0; y--) {
                if (dq.isEmpty()) break;
                temp[x][y] = dq.pollFirst().num;
            }

            dq.clear();
        }

        return temp;
    }
}
