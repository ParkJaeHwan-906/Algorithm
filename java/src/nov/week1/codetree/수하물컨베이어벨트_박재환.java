package nov.week1.codetree;

import java.util.*;
import java.io.*;

public class 수하물컨베이어벨트_박재환 {
    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        init(br);
        br.close();
    }

    static final int[] dx = {0, 1, 0, -1, 0};
    static final int[] dy = {0, 0, -1, 0, 1};

    static class Post {
        int id;
        int in;
        int x, y;
        int w;
        int out;
        int count;
        boolean broke;
        Post(int id, int in, int x, int y, int w) {
            this.id = id;
            this.in = in;
            this.x = x;
            this.y = y;
            this.w = w;
            this.out = -1;
            this.count = 0;
            this.broke = false;
        }
    }

    static int n, b;
    static int[][] board;
    static Post[] posts;
    static int[][] postBoard;
    static PriorityQueue<Post> intendedPosts;
    static void init(BufferedReader br) throws IOException {
        StringTokenizer st;

        n = Integer.parseInt(br.readLine().trim());
        board = new int[n][n];
        for(int x = 0; x < n; x++) {
            st = new StringTokenizer(br.readLine().trim());
            /**
             * 0 : 벽
             * 1 ~ 4 : 레일
             * 9 : 적재 장소
             */
            for(int y = 0; y < n; y++)
                board[x][y] = Integer.parseInt(st.nextToken());
        }

        b = Integer.parseInt(br.readLine().trim());
        posts = new Post[b + 1];
        postBoard = new int[n][n];
        intendedPosts = new PriorityQueue<>((a, b) -> Integer.compare(a.in, b.in));
        for(int i = 1; i <= b; i++) {
            st = new StringTokenizer(br.readLine().trim());
            int in = Integer.parseInt(st.nextToken());
            int x = Integer.parseInt(st.nextToken()) - 1;
            int y = Integer.parseInt(st.nextToken()) - 1;
            int w = Integer.parseInt(st.nextToken());
            posts[i] = new Post(i, in, x, y, w);
            intendedPosts.offer(posts[i]);
        }

        System.out.println(solution());
    }

    static int outPostCount;
    static Set<Post> onBoard;
    static String solution() {
        outPostCount = 0;
        onBoard = new HashSet<>();
        int turn = 0;
        while(outPostCount < b && turn < 200) {
            ++turn;
            movePosts(turn);
            putPost(turn);
        }

        StringBuilder sb = new StringBuilder();
        for(int i = 1; i <= b; i++)
            sb.append((posts[i].broke ? -1 : 1) * posts[i].out).append(' ');
        return sb.toString();
    }

    static void movePosts(int turn) {
        Queue<int[]> emptyLocs = findEmptySpace();
        Set<Post> moved = new HashSet<>();
        while(!emptyLocs.isEmpty()) {
            int[] cur = emptyLocs.poll();
            Post move = selectPost(cur, moved);
            if(move == null)
                continue;
            moved.add(move);
            postBoard[move.x][move.y] = 0;
            emptyLocs.offer(new int[] {move.x, move.y});
            move.x = cur[0];
            move.y = cur[1];
            if(board[cur[0]][cur[1]] == 9) {
                move.out = turn;
                outPostCount++;
                onBoard.remove(move);
                continue;
            }
            postBoard[move.x][move.y] = move.id;
        }

        for(Post post : moved) {
            post.count = 0;
        }
        for(int i = 1; i <= b; i++) {
            Post post = posts[i];
            if(!onBoard.contains(post) || moved.contains(post) || post.out != -1)
                continue;
            if(++post.count >= 3)
                post.broke = true;
        }
    }

    static Queue<int[]> findEmptySpace() {
        Queue<int[]> emptyLocs = new ArrayDeque<>();
        for(int x = 0; x < n; x++) {
            for(int y = 0; y < n; y++) {
                if(board[x][y] != 0 && postBoard[x][y] == 0)
                    emptyLocs.add(new int[]{x, y});
            }
        }
        return emptyLocs;
    }

    static Post selectPost(int[] cur, Set<Post> moved) {
        Post cand = null;
        for(int dir = 1; dir <= 4; dir++) {
            int nx = cur[0] + dx[dir];
            int ny = cur[1] + dy[dir];
            if(isNotBoard(nx, ny) || postBoard[nx][ny] == 0)
                continue;
            if(oppositeDir(dir) != board[nx][ny])
                continue;
            Post compare = posts[postBoard[nx][ny]];
            if(moved.contains(compare))
                continue;
            if(cand == null
                || (cand.w < compare.w)
                || (cand.w == compare.w && cand.id > compare.id))
                cand = compare;
        }
        return cand;
    }

    static void putPost(int turn) {
        Queue<Post> waitPosts = new ArrayDeque<>();
        while(!intendedPosts.isEmpty() && intendedPosts.peek().in <= turn) {
            Post post = intendedPosts.poll();
            if(postBoard[post.x][post.y] != 0) {
                waitPosts.offer(post);
                continue;
            }
            postBoard[post.x][post.y] = post.id;
            onBoard.add(post);
        }
        while(!waitPosts.isEmpty())
            intendedPosts.offer(waitPosts.poll());
    }

    static boolean isNotBoard(int x, int y) {
        return x < 0 || y < 0 || x >= n || y >= n;
    }

    static int oppositeDir(int dir) {
        if(dir == 1) return 3;
        if(dir == 2) return 4;
        if(dir == 3) return 1;
        return 2;
    }
}
