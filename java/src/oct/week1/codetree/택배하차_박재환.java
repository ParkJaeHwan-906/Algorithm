package oct.week1.codetree;

import java.util.*;
import java.io.*;

public class 택배하차_박재환 {
    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        init(br);
        br.close();
    }

    static final int[] dx = {0, 1, 0, -1};
    static final int[] dy = {1, 0, -1, 0};

    static class Post {
        int id;
        int h, w;
        int col;
        Post(int id, int h, int w, int col) {
            this.id = id;
            this.h = h;
            this.w = w;
            this.col = col;
        }
    }

    static int n, m;
    static List<Post> posts;
    static void init(BufferedReader br) throws IOException {
        StringTokenizer st;
        st = new StringTokenizer(br.readLine().trim());
        n = Integer.parseInt(st.nextToken());
        m = Integer.parseInt(st.nextToken());
        posts = new ArrayList<>();
        for(int i = 0; i < m; i++) {
            st = new StringTokenizer(br.readLine().trim());
            posts.add(new Post(
                    Integer.parseInt(st.nextToken()),
                    Integer.parseInt(st.nextToken()),
                    Integer.parseInt(st.nextToken()),
                    Integer.parseInt(st.nextToken()) - 1
            ));
        }

        System.out.print(solution());
    }

    static Set<Integer> removedPosts;
    static String solution() {
        StringBuilder sb = new StringBuilder();
        removedPosts = new HashSet<>();
        while(removedPosts.size() < m) {
            int[][] board = dropPosts();
            pickLeft(board, sb);
            board = dropPosts();
            pickRight(board, sb);
        }
        return sb.toString();
    }

    static int[][] dropPosts() {
        int[][] board = new int[n][n];
        for(Post post : posts) {
            if(removedPosts.contains(post.id)) {
                continue;
            }
            int depth = getDepth(post, board);
            for(int x = depth; x > depth - post.h; x--) {
                for(int y = post.col; y < post.col + post.w; y++) {
                    board[x][y] = post.id;
                }
            }
        }
        return board;
    }

    static int getDepth(Post post, int[][] board) {
        for(int x = 0; x < n; x++) {
            boolean available = true;
            for (int y = post.col; y < post.col + post.w; y++) {
                if(board[x][y] != 0) {
                    available = false;
                    break;
                }
            }
            if(!available) {
                return x - 1;
            }
        }
        return n - 1;
    }

    static void pickLeft(int[][] board, StringBuilder sb) {
        int picked = 107;
        boolean[][] checked = new boolean[n][n];
        for(int x = 0; x < n; x++) {
            for(int y = 0; y < n; y++) {
                if(checked[x][y]) {
                    break;
                }
                if(board[x][y] != 0) {
                    List<int[]> locs = findLocs(board, board[x][y], x, y, checked);
                    if(canPick(2, locs, board, board[x][y])) {
                        picked = Math.min(picked, board[x][y]);
                    }
                }
            }
        }
        if(picked == 107) {
            return;
        }
        removedPosts.add(picked);
        sb.append(picked).append('\n');
    }

    static void pickRight(int[][] board, StringBuilder sb) {
        int picked = 107;
        boolean[][] checked = new boolean[n][n];
        for(int x = 0; x < n; x++) {
            for(int y = n -1; y >= 0; y--) {
                if(checked[x][y]) {
                    break;
                }
                if(board[x][y] != 0) {
                    List<int[]> locs = findLocs(board, board[x][y], x, y, checked);
                    if(canPick(0, locs, board, board[x][y])) {
                        picked = Math.min(picked, board[x][y]);
                    }
                }
            }
        }
        if(picked == 107) {
            return;
        }
        removedPosts.add(picked);
        sb.append(picked).append('\n');
    }


    static List<int[]> findLocs(int[][] board, int target, int x, int y, boolean[][] checked) {
        Queue<int[]> q = new ArrayDeque<>();
        List<int[]> locs = new ArrayList<>();
        boolean[][] visited = new boolean[n][n];
        q.offer(new int[] {x, y});
        visited[x][y] = true;
        checked[x][y] = true;
        while(!q.isEmpty()) {
            int[] cur = q.poll();
            locs.add(cur);
            for(int dir = 0; dir < 4; dir++) {
                int nx = cur[0] + dx[dir];
                int ny = cur[1] + dy[dir];
                if(isNotBoard(nx, ny)) {
                    continue;
                }
                if(visited[nx][ny] || board[nx][ny] != target) {
                    continue;
                }
                visited[nx][ny] = true;
                checked[nx][ny] = true;
                q.offer(new int[] {nx, ny});
            }
        }
        return locs;
    }

    static boolean canPick(int dir, List<int[]> locs, int[][] board, int target) {
        for(int[] loc : locs) {
            int x = loc[0];
            int y = loc[1];
            while(true) {
                int nx = x + dx[dir];
                int ny = y + dy[dir];
                if(isNotBoard(nx, ny)) {
                    break;
                }
                if(board[nx][ny] != target && board[nx][ny] != 0) {
                    return false;
                }
                x = nx;
                y = ny;
            }
        }
        return true;
    }

    static boolean isNotBoard(int x, int y) {
        return x < 0 || y < 0 || x >= n || y >= n;
    }
}
