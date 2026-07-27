package jul.week4.codetree;

import java.util.*;
import java.io.*;

/**
 * [풀이 시간]
 * 01:21:57
 * AI 사용 여부 O
 * -> 배열 회전 로직에서 바로 격자에 적용하려 했는데 그냥 절댓값으로 계속 하는게 안헷갈리고 나을 듯합니다.
 */
public class 고대문명유적탐사_박재환 {
    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        init(br);
        br.close();
    }
    static int k, m;
    static int[][] board;
    static Queue<Integer> wait;
    static void init(BufferedReader br) throws IOException {
        StringTokenizer st;

        st = new StringTokenizer(br.readLine().trim());
        k = Integer.parseInt(st.nextToken());
        m = Integer.parseInt(st.nextToken());

        board = new int[5][5];
        for(int x = 0; x < 5; x++) {
            st = new StringTokenizer(br.readLine().trim());
            for(int y = 0; y < 5; y++) board[x][y] = Integer.parseInt(st.nextToken());
        }

        wait = new ArrayDeque<>();
        st = new StringTokenizer(br.readLine().trim());
        for(int i = 0; i < m; i++) wait.offer(Integer.parseInt(st.nextToken()));

        System.out.println(solution());
    }

    static String solution() {
        StringBuilder sb = new StringBuilder();
        for(int i = 0; i < k; i++) {
            int value = 0;
            // 탐사 결과
            SearchResult turnResult = search();
            // 점수 및 보드 갱신
            if(turnResult.value == 0) break;
            value += turnResult.value;
            board = turnResult.board;
            // 빈칸 채우기
            value += chainResponse(board);
            sb.append(value).append(' ');
        }
        return sb.toString();
    }

    static class SearchResult {
        int cx, cy;     // 중심축 (x, y)
        int value;      // 얻을 수 있는 가치
        int rotate;     // 회전 횟수
        int[][] board;  // 처리 이후의 보드
        SearchResult(int cx, int cy, int value, int rotate, int[][] board) {
            this.cx = cx;
            this.cy = cy;
            this.value = value;
            this.rotate = rotate;
            this.board = board;
        }
    }

    static SearchResult DUMMY = new SearchResult(10,10,-10, 10, new int[][]{});

    static SearchResult search() {
        SearchResult maxResult = DUMMY;
        for(int x = 0; x < 3; x++) {
            for(int y = 0; y < 3; y++) {
                SearchResult searchResult = getMaxValue(x, y);
                if(maxResult.value < searchResult.value ||
                        (maxResult.value == searchResult.value && (maxResult.rotate > searchResult.rotate)) ||
                        (maxResult.value == searchResult.value && maxResult.rotate == searchResult.rotate && maxResult.cy > searchResult.cy) ||
                        (maxResult.value == searchResult.value && maxResult.rotate == searchResult.rotate && maxResult.cy == searchResult.cy && maxResult.cx > searchResult.cx)) {
                    maxResult = searchResult;
                }
            }
        }
        return maxResult;
    }

    static SearchResult getMaxValue(int x, int y) {
        SearchResult maxResult = DUMMY;
        int[][] temp = copyBoard(board);

        for(int rotate = 1; rotate <= 3; rotate++) {
            temp = rotateBoard(temp, x, y);
            SearchResult searchResult = guessValue(temp, x, y, rotate);
            if(maxResult.value < searchResult.value ||
                    (maxResult.value == searchResult.value && (maxResult.rotate > searchResult.rotate)) ||
                    (maxResult.value == searchResult.value && maxResult.rotate == searchResult.rotate && maxResult.cy > searchResult.cy) ||
                    (maxResult.value == searchResult.value && maxResult.rotate == searchResult.rotate && maxResult.cy == searchResult.cy && maxResult.cx > searchResult.cx)) {
                maxResult = searchResult;
            }
        }
        return maxResult;
    }

    static SearchResult guessValue(int[][] board, int x, int y, int rotate) {
        int value = 0;
        int[][] temp = copyBoard(board);
        boolean[][] visited = new boolean[5][5];
        for(int i = 0; i < 5; i++) {
            for(int j = 0; j < 5; j++) {
                if(temp[i][j] == 0 || visited[i][j]) continue;
                value += bindGroup(temp, i, j, visited);
            }
        }
        return new SearchResult(x + 1, y + 1, value, rotate, temp);
    }

    static final int[] dx = {0, 1, 0, -1};
    static final int[] dy = {1, 0, -1, 0};

    static int bindGroup(int[][] board, int x, int y, boolean[][] visited) {
        Queue<int[]> q = new ArrayDeque<>();
        List<int[]> list = new ArrayList<>();
        visited[x][y] = true;
        q.offer(new int[]{x, y});
        list.add(new int[]{x, y});

        while(!q.isEmpty()) {
            int[] cur = q.poll();
            for(int dir = 0; dir < 4; dir++) {
                int nx = cur[0] + dx[dir];
                int ny = cur[1] + dy[dir];

                if(nx < 0 || ny < 0 || nx >= 5 || ny >= 5) continue;
                if(visited[nx][ny]) continue;
                if(board[nx][ny] != board[cur[0]][cur[1]]) continue;

                visited[nx][ny] = true;
                q.offer(new int[]{nx, ny});
                list.add(new int[]{nx, ny});
            }
        }

        if(list.size() < 3) return 0;           // 3개 미만이라면
        for(int[] loc : list) board[loc[0]][loc[1]] = 0;
        return list.size();
    }

    static int chainResponse(int[][] temp) {
        int value = 0;
        while(true) {
            fillEmpty(temp);
            SearchResult searchResult = guessValue(temp, -1, -1, -1);      // 다른 조건 없이 그룹만 확인
            if(searchResult.value == 0) break;
            value += searchResult.value;
            temp = searchResult.board;
        }
        board = temp;
        return value;
    }

    static void fillEmpty(int[][] board) {
        for(int y = 0; y < 5; y++) {
            for(int x = 4; x >= 0; x--) {
                if(board[x][y] == 0) board[x][y] = wait.poll();     // 조각이 부족한 경우는 X
            }
        }
    }

    static int[][] rotateBoard(int[][] board, int x, int y) {
        int[][] temp = copyBoard(board);
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                temp[x + j][y + 2 - i] = board[x + i][y + j];
            }
        }
        return temp;
    }


    static int[][] copyBoard(int[][] board) {
        int[][] copy = new int[5][5];
        for(int i = 0; i < 5; i++) {
            for(int j = 0; j < 5; j++) copy[i][j] = board[i][j];
        }
        return copy;
    }
}
