package jun.week5.codetree;

import java.util.*;
import java.io.*;

public class 격자숫자놀이_박재환 {
    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        init(br);
        br.close();
    }
    static int r, c, k;
    static int[][] board;
    static int curR, curC;
    static void init(BufferedReader br) throws IOException {
        StringTokenizer st;

        st = new StringTokenizer(br.readLine().trim());
        r = Integer.parseInt(st.nextToken()) - 1;
        c = Integer.parseInt(st.nextToken()) - 1;
        k = Integer.parseInt(st.nextToken());

        board = new int[100][100];      // 격자의 최대 크기는 100 x 100
        for(int x = 0; x < 3; x++) {
            st = new StringTokenizer(br.readLine().trim());
            for(int y = 0; y < 3;) board[x][y++] = Integer.parseInt(st.nextToken());
        }
        curR = curC = 3;
        System.out.println(solution());
    }

    static int solution() {
        int time = 0;

        if(board[r][c] == k) return time;

        while(board[r][c] != k) {
            if(++time > 100) break;
            // 행의 개수와 열의 개수 카운트
            if(curR >= curC) { expandRow(); }
            else { expandCol(); }
        }

        return time > 100 ? -1 : time;
    }

    /**
     * 숫자 압축
     * ex)
     * 1 1 2
     * v
     * 2 1 1 2
     */

    static class Node implements Comparable<Node> {
        int num;
        int count;

        Node(int num, int count) {
            this.num = num;
            this.count = count;
        }

        void add() {
            this.count++;
        }

        public int compareTo(Node o) {
            if(this.count != o.count) return Integer.compare(this.count, o.count);
            return Integer.compare(this.num, o.num);
        }
    }

    static void expandRow() {
        /**
         * 행의 개수가 열의 개수보다 크거나 같을 때
         */
        int newC = 0;

        List<Integer>[] temp = new List[curR];
        for(int i = 0; i < curR;) temp[i++] = new ArrayList<>();

        for(int x = 0; x < curR; x++) {
            Map<Integer, Node> map = new HashMap<>();
            for(int y = 0; y < curC; y++) {
                if(board[x][y] == 0) continue;

                if(map.containsKey(board[x][y])) map.get(board[x][y]).add();
                else map.put(board[x][y], new Node(board[x][y], 1));
            }

            List<Node> nodes = new ArrayList<>(map.values());
            Collections.sort(nodes);
            for(Node n : nodes) {
                temp[x].add(n.num);
                temp[x].add(n.count);
            }

            newC = Math.max(newC, Math.min(100, temp[x].size()));
        }

        int[][] newBoard = new int[100][100];
        for (int x = 0; x < curR; x++) {
            int len = Math.min(temp[x].size(), 100);
            for (int y = 0; y < len; y++) newBoard[x][y] = temp[x].get(y);
        }
        board = newBoard;
        curC = newC;
    }

    static void expandCol() {
        /**
         * 행의 개수가 열의 개수보다 작은 경우
         */
        int newR = 0;

        List<Integer>[] temp = new List[curC];
        for(int i = 0; i < curC;) temp[i++] = new ArrayList<>();

        for(int y = 0; y < curC; y++) {
            Map<Integer, Node> map = new HashMap<>();
            for(int x = 0; x < curR; x++) {
                if(board[x][y] == 0) continue;

                if(map.containsKey(board[x][y])) map.get(board[x][y]).add();
                else map.put(board[x][y], new Node(board[x][y], 1));
            }

            List<Node> nodes = new ArrayList<>(map.values());
            Collections.sort(nodes);
            for(Node n : nodes) {
                temp[y].add(n.num);
                temp[y].add(n.count);
            }

            newR = Math.max(newR, Math.min(100, temp[y].size()));
        }

        int[][] newBoard = new int[100][100];
        for (int y = 0; y < curC; y++) {
            int len = Math.min(temp[y].size(), 100);
            for (int x = 0; x < len; x++) newBoard[x][y] = temp[y].get(x);
        }
        board = newBoard;
        curR = newR;
    }
}
