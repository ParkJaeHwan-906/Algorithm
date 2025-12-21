package dec_3.boj;

import java.util.*;
import java.io.*;

public class 사다리조작_박재환 {
    static BufferedReader br;
    public static void main(String[] args) throws IOException {
        br = new BufferedReader(new InputStreamReader(System.in));
        init();
        br.close();
    }
    /**
     * N개의 세로선과 M개의 가로선
     * 인접한 세로선 사이에는 가로선을 놓을 수 있다.
     * 각 세로선마다 가로선을 놓을 수 있는 위치의 개수는 H이다.
     */
    static StringTokenizer st;
    static int n,m,h;
    static int[][] connectStatus;
    static int minCnt;
    static void init() throws IOException {
        minCnt = Integer.MAX_VALUE;
        st = new StringTokenizer(br.readLine().trim());
        n = Integer.parseInt(st.nextToken());
        m = Integer.parseInt(st.nextToken());
        h = Integer.parseInt(st.nextToken());
        connectStatus = new int[h+1][n +1];      // [h][col]
        for(int i=0; i<m; i++) {
            st = new StringTokenizer(br.readLine());
            int rowIdx = Integer.parseInt(st.nextToken());
            int colIdx = Integer.parseInt(st.nextToken());

            // -> 방향은 1로, <- 방향은 2로 표시
            connectStatus[rowIdx][colIdx] = 1;
            connectStatus[rowIdx][colIdx+1] = 2;
        }
        solution();
    }

    static void solution() {
        for(int targetCnt = 0; targetCnt < 4 && minCnt == Integer.MAX_VALUE; targetCnt++) {
            findNewLocation(targetCnt, 1, 0);
        }

        System.out.println(minCnt == Integer.MAX_VALUE ? -1 : minCnt);
    }

    static void findNewLocation(int targetCnt, int rowIdx, int newCnt) {
        if(newCnt == targetCnt) {
            // 모두 원하는대로 방문 가능한지 검증
            if(isCorrect()) {
                minCnt = newCnt;
            }
            return;
        }

        for(int row=rowIdx; row<h+1; row++) {
            for(int col=1; col<n; col++) {  // 마지막 세로선에서는 새롭게 가로선을 추가할 수 없음
                if(connectStatus[row][col] == 0 && connectStatus[row][col+1] == 0) {
                    connectStatus[row][col] = 1;
                    connectStatus[row][col+1] = 2;
                    findNewLocation(targetCnt, row, newCnt+1);
                    connectStatus[row][col] = 0;
                    connectStatus[row][col+1] = 0;
                }
            }
        }
    }

    static boolean isCorrect() {
        for(int col=1; col<n+1; col++) {
            int curCol = col;
            for(int row=1; row<h+1; row++) {
                if(connectStatus[row][curCol] == 1) curCol++;
                else if(connectStatus[row][curCol] == 2) curCol--;
            }
            if(col != curCol) return false;
        }
        return true;
    }
}
