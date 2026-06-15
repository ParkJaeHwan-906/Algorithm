package jun.week2.codetree;

import java.util.*;
import java.io.*;

/**
 * [풀이 시간]
 * 00:48:58
 * AI 사용 여부 X
 */
public class 택배하차_박재환 {
    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        init(br);
        br.close();
    }
    static class Drop {
        int no;
        int h, w;
        int col;

        Drop(int no, int h, int w, int col) {
            this.no = no;
            this.h = h;
            this.w = w;
            this.col = col;
        }
    }
    static int n, m;
    static List<Drop> drops;
    static void init(BufferedReader br) throws IOException {
        StringTokenizer st;

        st = new StringTokenizer(br.readLine().trim());
        n = Integer.parseInt(st.nextToken());
        m = Integer.parseInt(st.nextToken());

        drops = new ArrayList<>();
        for(int i = 0; i < m; i++) {
            st = new StringTokenizer(br.readLine().trim());
            int no = Integer.parseInt(st.nextToken());
            int h = Integer.parseInt(st.nextToken());
            int w = Integer.parseInt(st.nextToken());
            int col = Integer.parseInt(st.nextToken()) - 1;

            Drop drop = new Drop(no, h, w, col);
            drops.add(drop);
        }

        System.out.println(solution());
    }

    static int[][] board;
    static Set<Integer> removed;
    static String solution() {
        removed = new HashSet<>();
        StringBuilder sb = new StringBuilder();
        while(!drops.isEmpty()) {       // 모든 택배의 하차가 끝났을 때
            // 1. 택배 쌓기
            dropBoxes();

            // 2. 왼쪽 뽑기
            int leftTargetId = pickFromLeft();
            sb.append(leftTargetId).append("\n");
            removed.add(leftTargetId);
            dropBoxes();

            // 3. 오른쪽 뽑기
            int rightTargetId = pickFromRight();
            sb.append(rightTargetId).append("\n");
            removed.add(rightTargetId);
            dropBoxes();
        }

        return sb.toString();
    }

    static void dropBoxes() {
        board = new int[n][n];
        List<Drop> temp = new ArrayList<>();
        for(Drop drop : drops) {
            if(removed.contains(drop.no)) continue;
            temp.add(drop);
            int row = dropBox(drop);
            fill(row, drop);
        }
        drops = temp;
    }

    static int dropBox(Drop drop) {
        // 밑변만 기준으로 가장 낮게 떨어질 수 있는 높이 찾기
        int maxDepth = n;
        for(int y = drop.col; y < drop.col + drop.w; y++) {
            for(int x = 0; x < n; x++) {
                if(x != n - 1 && board[x][y] == 0) continue;        // 마지막 행이 아니고 비어있다면
                if(x == n - 1 && board[x][y] == 0) maxDepth = Math.min(maxDepth, x);    // 마지막 행이고 비어있다면
                else maxDepth = Math.min(maxDepth, x - 1);                              // 비어있지않다면
            }
        }
        return maxDepth;
    }

    static void fill(int row, Drop drop) {
        for(int y = drop.col; y < drop.col + drop.w; y++) {
            for(int x = row; x > row - drop.h; x--) {
                board[x][y] = drop.no;
            }
        }
    }

    static int pickFromLeft() {
        Set<Integer> checked = new HashSet<>();
        int targetId = Integer.MAX_VALUE;
        for(int x = 0; x < n; x++) {
            for(int y = 0; y < n; y++) {
                if(board[x][y] == 0) continue;
                if(!checked.add(board[x][y])) continue;

                // 이전에 처리되지 않은 택배인 경우
                // 뽑을 수 있는지
                if(!isCanPollLeft(board[x][y], x, y)) continue;

                targetId = Math.min(targetId, board[x][y]);
            }
        }
        return targetId;
    }

    static boolean isCanPollLeft(int no, int row, int col) {
        List<int[]> rows = getRowsLeft(no, row, col);

        for(int[] loc : rows) {
            int x = loc[0];
            int y = loc[1] - 1;

            for(; y >= 0; y--) {
                if(board[x][y] == 0) continue;
                return false;
            }
        }

        return true;
    }

    static List<int[]> getRowsLeft(int no, int row, int col) {
        List<int[]> list = new ArrayList<>();
        for(int x = row; x < n; x++) {
            if(board[x][col] != no) break;
            list.add(new int[] {x, col});
        }
        return list;
    }

    static int pickFromRight() {
        Set<Integer> checked = new HashSet<>();
        int targetId = Integer.MAX_VALUE;
        for(int x = 0; x < n; x++) {
            for(int y = n - 1; y >= 0; y--) {
                if(board[x][y] == 0) continue;
                if(!checked.add(board[x][y])) continue;

                // 이전에 처리되지 않은 택배인 경우
                // 뽑을 수 있는지
                if(!isCanPollRight(board[x][y], x, y)) continue;

                targetId = Math.min(targetId, board[x][y]);
            }
        }
        return targetId;
    }

    static boolean isCanPollRight(int no, int row, int col) {
        List<int[]> rows = getRowsRight(no, row, col);

        for(int[] loc : rows) {
            int x = loc[0];
            int y = loc[1] + 1;

            for(; y < n; y++) {
                if(board[x][y] == 0) continue;
                return false;
            }
        }

        return true;
    }

    static List<int[]> getRowsRight(int no, int row, int col) {
        List<int[]> list = new ArrayList<>();
        for(int x = row; x < n; x++) {
            if(board[x][col] != no) break;
            list.add(new int[] {x, col});
        }
        return list;
    }
}
