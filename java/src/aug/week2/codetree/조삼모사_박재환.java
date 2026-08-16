package aug.week2.codetree;

import java.util.*;
import java.io.*;

public class 조삼모사_박재환 {
    public static void main(String[] args) throws IOException {
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
    static int minDiff;
    static int solution() {
        minDiff = Integer.MAX_VALUE;
        divideTasks(0, 0, new int[n / 2], 0, new int[n / 2]);
        return minDiff;
    }

    static void divideTasks(int id, int aId, int[] a, int bId, int[] b) {
        if(id == n) {
            int diff = getDiff(a, b);
            minDiff = Math.min(minDiff, diff);
            return;
        }
        if(aId < n / 2) {
            a[aId] = id;
            divideTasks(id + 1, aId + 1, a, bId, b);
        }
        if(bId < n / 2) {
            b[bId] = id;
            divideTasks(id + 1, aId, a, bId + 1, b);
        }
    }

    static int getDiff(int[] a, int[] b) {
        int aTotal = 0;
        int bTotal = 0;
        for (int i = 0; i < n / 2; i++) {
            for (int j = i + 1; j < n / 2; j++) {
                aTotal += board[a[i]][a[j]] + board[a[j]][a[i]];
                bTotal += board[b[i]][b[j]] + board[b[j]][b[i]];
            }
        }
        return Math.abs(aTotal - bTotal);
    }
}
