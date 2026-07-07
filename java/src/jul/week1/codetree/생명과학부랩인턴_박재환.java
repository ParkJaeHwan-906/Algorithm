package jul.week1.codetree;

import java.util.*;
import java.io.*;

/**
 * [풀이 시간]
 * 00:54:19
 * AI 사용 여부 X
 */
public class 생명과학부랩인턴_박재환 {
    public static void main(String[] args) throws IOException{
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        init(br);
        br.close();
    }

    static class Mold {
        int id;
        int x, y;
        int s, d;       // s : 속도, d : 방향
        int b;          // b : 크기

        Mold(int id, int x, int y, int s, int d, int b) {
            this.id = id;
            this.x = x;
            this.y = y;
            this.s = s;
            this.d = d;
            this.b = b;
        }

        void oppositeDir() {
            if(this.d == 0) this.d = 1;
            else if(this.d == 1) this.d = 0;
            else if(this.d == 2) this.d = 3;
            else if(this.d == 3) this.d = 2;
        }
    }
    static final int[] dx = {-1, 1, 0, 0};
    static final int[] dy = {0, 0, 1, -1};

    static int mId;
    static int n, m, k;
    static int[][] board;
    static Map<Integer, Mold> molds;
    static void init(BufferedReader br) throws IOException{
        StringTokenizer st;

        st = new StringTokenizer(br.readLine().trim());
        n = Integer.parseInt(st.nextToken());
        m = Integer.parseInt(st.nextToken());
        k = Integer.parseInt(st.nextToken());

        mId = 0;
        board = new int[n][m];
        molds = new HashMap<>();
        for(int i = 0; i < k; i++) {
            st = new StringTokenizer(br.readLine().trim());
            int x = Integer.parseInt(st.nextToken()) - 1;
            int y = Integer.parseInt(st.nextToken()) - 1;
            int s = Integer.parseInt(st.nextToken());
            int d = Integer.parseInt(st.nextToken()) - 1;
            int b = Integer.parseInt(st.nextToken());
            Mold mold = new Mold(++mId, x, y, s, d, b);
            board[mold.x][mold.y] = mold.id;
            molds.put(mold.id, mold);
        }
        System.out.println(solution());
    }

    static int totalGet;
    static int solution() {
        totalGet = 0;
        for(int y = 0; y < m; y++) {
            find(y);
            moveMolds();
        }
        return totalGet;
    }

    static void find(int y) {
        for(int x = 0; x < n; x++) {
            if(board[x][y] == 0) continue;

            // 곰팡이를 찾음
            Mold mold = molds.get(board[x][y]);
            totalGet += mold.b;
            molds.remove(mold.id);
            board[x][y] = 0;
            break;
        }
    }

    static void moveMolds() {
        int[][] temp = new int[n][m];
        List<Integer> removed = new ArrayList<>();
        for(Mold mold : molds.values()) {
            moveMold(mold);

            if(temp[mold.x][mold.y] == 0) {
                temp[mold.x][mold.y] = mold.id;
            } else {
                Mold other = molds.get(temp[mold.x][mold.y]);
                if(other.b > mold.b) removed.add(mold.id);
                else {
                    removed.add(other.id);
                    temp[mold.x][mold.y] = mold.id;
                }
            }
        }
        board = temp;
        for(int mid : removed) molds.remove(mid);
    }

    static void moveMold(Mold mold) {
        int x = mold.x;
        int y = mold.y;
        int s = mold.s;

        if(mold.d == 0 || mold.d == 1) s %= (n - 1) * 2;
        else s %= (m - 1) * 2;

        for(int i = 0; i < s; i++) {
            int nx = x + dx[mold.d];
            int ny = y + dy[mold.d];
            if(isNotBoard(nx, ny)) {
                mold.oppositeDir();
                nx = x + dx[mold.d];
                ny = y + dy[mold.d];
            }
            x = nx;
            y = ny;
        }
        mold.x = x;
        mold.y = y;
    }

    static boolean isNotBoard(int x, int y) {
        return x < 0 || y < 0 || x >= n || y >= m;
    }
}
