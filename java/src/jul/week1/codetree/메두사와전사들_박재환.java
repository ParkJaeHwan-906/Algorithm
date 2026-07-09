package jul.week1.codetree;

import java.util.*;
import java.io.*;

/**
 * [풀이 시간]
 * 02:10:54
 * AI 사용 여부 O
 * => 전사로부터 가려지는 시야 로직이 계속 틀려서 사용..
 *
 */
public class 메두사와전사들_박재환 {
    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        init(br);
        br.close();
    }

    static class Loc {
        int x, y;

        Loc(int x, int y) {
            this.x = x;
            this.y = y;
        }
    }

    static class Worrier extends Loc {
        boolean stone;

        Worrier(int x, int y) {
            super(x, y);
            this.stone = false;
        }

        void reset() {          // 돌인 경우, 초기화
            if(!stone) return;
            stone = false;
        }
    }

    static int n, m;
    static Loc medusa;
    static Loc park;
    static int[][] board;
    static Map<Integer, Worrier> worriers;
    static List<Integer>[][] worrierBoard;
    static void init(BufferedReader br) throws IOException {
        StringTokenizer st;

        st = new StringTokenizer(br.readLine().trim());
        n = Integer.parseInt(st.nextToken());       // 격자 크기
        m = Integer.parseInt(st.nextToken());       // 전사 수

        st = new StringTokenizer(br.readLine().trim());
        int mx = Integer.parseInt(st.nextToken());
        int my = Integer.parseInt(st.nextToken());
        medusa = new Loc(mx, my);                   // 메두사 위치
        int px = Integer.parseInt(st.nextToken());
        int py = Integer.parseInt(st.nextToken());
        park = new Loc(px, py);                     // 공원 위치

        int wId = 0;                                // 전사 id
        worriers = new HashMap<>();                 // id, Worrier
        worrierBoard = new List[n][n];              // 동일한 위치에 전사들이 중복으로 존재할 수 있음
        for(int x = 0; x < n; x++) {
            for(int y = 0; y < n; y++) worrierBoard[x][y] = new ArrayList<>();
        }
        st = new StringTokenizer(br.readLine().trim());
        for(int i = 0; i < m; i++) {
            int x = Integer.parseInt(st.nextToken());
            int y = Integer.parseInt(st.nextToken());
            Worrier w = new Worrier(x, y);
            worriers.put(++wId, w);
            worrierBoard[x][y].add(wId);
        }

        board = new int[n][n];
        for(int x = 0; x < n; x++) {
            st = new StringTokenizer(br.readLine().trim());
            for(int y = 0; y < n; y++) board[x][y] = Integer.parseInt(st.nextToken());
        }

        System.out.println(solution());
    }

    static String solution() {
        Deque<Loc> medusaLocs = new ArrayDeque<>();
        findRouteToPark(medusaLocs, medusa);            // 메두사가 이동할 경로를 초반에 구함

        if(medusaLocs.isEmpty()) return "-1";           // 공원까지 이동하는 경로가 존재하지 않는 경우
        StringBuilder sb = new StringBuilder();
        while(!medusaLocs.isEmpty()) {
            Loc cur = medusaLocs.pollFirst();
            if(cur.x == park.x && cur.y == park.y) {
                sb.append(0);
                break;
            }
            sameLocMedusa(cur);

            Sight sight = findMaxSight(cur);
            makeStone(sight.stoneCand);

            int[] moveResult = moveWorriers(sight.sight, cur);
            sb.append(moveResult[0]).append(' ')
              .append(sight.stoneCand.size()).append(' ')
              .append(moveResult[1]).append('\n');
            resetWorriers();

        }
        return sb.toString();
    }

    // ============= 전사 이동 ===============
    static int[] moveWorriers(int[][] sight, Loc medusa) {
        int moveCnt = 0;
        int attackCnt = 0;
        List<Integer> removed = new ArrayList<>();

        for(Map.Entry<Integer, Worrier> entry : worriers.entrySet()) {
            int wId = entry.getKey();
            Worrier w = entry.getValue();
            if(w.stone) continue;               // 돌이 된 전사 패스
            if(sight[w.x][w.y] > 0) continue;   // 메두사의 시야에 있는 전사 패스

            Loc first = moveFirst(w.x, w.y, sight, medusa);
            if(first != null) {
                w.x = first.x;
                w.y = first.y;
                moveCnt++;
                if(w.x == medusa.x && w.y == medusa.y) {
                    removed.add(wId);
                    attackCnt++;
                    continue;
                }
            }

            Loc second = moveSecond(w.x, w.y, sight, medusa);
            if(second != null) {
                w.x = second.x;
                w.y = second.y;
                moveCnt++;
                if(w.x == medusa.x && w.y == medusa.y) {
                    removed.add(wId);
                    attackCnt++;
                }
            }
        }

        for(int wId : removed) worriers.remove(wId);
        rebuildWorrierBoard();
        return new int[] {moveCnt, attackCnt};
    }

    static Loc moveFirst(int x, int y, int[][] sight, Loc medusa) {
        int[] dx = {-1, 1, 0, 0};
        int[] dy = {0, 0, -1, 1};
        int originDist = getDist(medusa.x, medusa.y, x, y);

        for(int dir = 0; dir < 4; dir++) {
            int nx = x + dx[dir];
            int ny = y + dy[dir];
            if(isNotBoard(nx, ny)) continue;
            if(sight[nx][ny] > 0) continue;

            int dist = getDist(medusa.x, medusa.y, nx, ny);
            if(originDist > dist) return new Loc(nx, ny);
        }
        return null;
    }

    static Loc moveSecond(int x, int y, int[][] sight, Loc medusa) {
        int[] dx = {0, 0, -1, 1};
        int[] dy = {-1, 1, 0, 0};
        int originDist = getDist(medusa.x, medusa.y, x, y);

        for(int dir = 0; dir < 4; dir++) {
            int nx = x + dx[dir];
            int ny = y + dy[dir];
            if(isNotBoard(nx, ny)) continue;
            if(sight[nx][ny] > 0) continue;

            int dist = getDist(medusa.x, medusa.y, nx, ny);
            if(originDist > dist) return new Loc(nx, ny);
        }
        return null;
    }

    // ============= 메두사의 공격을 받고 사라짐 =============
    static void sameLocMedusa(Loc medusa) {
        for(int wId : worrierBoard[medusa.x][medusa.y]) {
            worriers.remove(wId);
        }
        worrierBoard[medusa.x][medusa.y].clear();
    }

    // ============= 메두사 시야 ====================

    static void makeStone(List<Integer> stoneCand) {
        for(int wId : stoneCand) {
            Worrier w = worriers.get(wId);
            if(w != null) w.stone = true;
        }
    }

    static void resetWorriers() {
        for(Worrier w : worriers.values()) {
            w.reset();
        }
    }

    static void rebuildWorrierBoard() {
        for(int x = 0; x < n; x++) {
            for(int y = 0; y < n; y++) {
                worrierBoard[x][y].clear();
            }
        }

        for(Map.Entry<Integer, Worrier> entry : worriers.entrySet()) {
            Worrier w = entry.getValue();
            worrierBoard[w.x][w.y].add(entry.getKey());
        }
    }

    static class Sight {
        int[][] sight;
        List<Integer> stoneCand;

        Sight(int[][] sight, List<Integer> stoneCand) {
            this.sight = sight;
            this.stoneCand = stoneCand;
        }
    }

    static Sight findMaxSight(Loc cur) {
        Sight best = null;
        for(int dir = 0; dir < 4; dir++) {
            Sight cand = evalSight(dir, cur);
            if(best == null || cand.stoneCand.size() > best.stoneCand.size()) {
                best = cand;
            }
        }
        return best;
    }

    static Sight evalSight(int dir, Loc medusa) {
        boolean[][] canSee = new boolean[n][n];
        markTriangle(canSee, dir, medusa);

        List<Integer> stoneCand = new ArrayList<>();

        if(dir == 0 || dir == 1) {
            int step = (dir == 0) ? -1 : 1;
            for(int depth = 1; depth < n; depth++) {
                int x = medusa.x + step * depth;
                if(isNotBoard(x, medusa.y)) break;

                processCellAndShadow(canSee, stoneCand, dir, x, medusa.y, medusa);

                for(int off = 1; off <= depth; off++) {
                    int rightY = medusa.y + off;
                    if(!isNotBoard(x, rightY)) processCellAndShadow(canSee, stoneCand, dir, x, rightY, medusa);
                    int leftY = medusa.y - off;
                    if(!isNotBoard(x, leftY)) processCellAndShadow(canSee, stoneCand, dir, x, leftY, medusa);
                }
            }
        } else {
            int step = (dir == 2) ? -1 : 1;
            for(int depth = 1; depth < n; depth++) {
                int y = medusa.y + step * depth;
                if(isNotBoard(medusa.x, y)) break;

                processCellAndShadow(canSee, stoneCand, dir, medusa.x, y, medusa);

                for(int off = 1; off <= depth; off++) {
                    int downX = medusa.x + off;
                    if(!isNotBoard(downX, y)) processCellAndShadow(canSee, stoneCand, dir, downX, y, medusa);
                    int upX = medusa.x - off;
                    if(!isNotBoard(upX, y)) processCellAndShadow(canSee, stoneCand, dir, upX, y, medusa);
                }
            }
        }

        return new Sight(toSightArray(canSee), stoneCand);
    }

    static void processCellAndShadow(boolean[][] canSee, List<Integer> stoneCand, int dir, int x, int y, Loc medusa) {
        if(!canSee[x][y]) return;
        if(worrierBoard[x][y].isEmpty()) return;

        for(int wId : worrierBoard[x][y]) {
            if(!worriers.containsKey(wId)) continue;
            stoneCand.add(wId);
        }

        applyShadow(canSee, dir, x, y, medusa);
    }

    static void applyShadow(boolean[][] canSee, int dir, int x, int y, Loc medusa) {
        if(dir == 0 || dir == 1) {
            int stepX = (dir == 0) ? -1 : 1;
            int side = y - medusa.y;

            if(side == 0) {
                for(int nx = x + stepX; !isNotBoard(nx, y); nx += stepX) {
                    canSee[nx][y] = false;
                }
                return;
            }

            int nx = x + stepX;
            int depth = 1;
            if(side > 0) {
                while(!isNotBoard(nx, y)) {
                    for(int k = 0; k <= depth; k++) {
                        int ny = y + k;
                        if(isNotBoard(nx, ny)) break;
                        canSee[nx][ny] = false;
                    }
                    nx += stepX;
                    depth++;
                }
            } else {
                while(!isNotBoard(nx, y)) {
                    for(int k = 0; k <= depth; k++) {
                        int ny = y - k;
                        if(isNotBoard(nx, ny)) break;
                        canSee[nx][ny] = false;
                    }
                    nx += stepX;
                    depth++;
                }
            }
            return;
        }

        int stepY = (dir == 2) ? -1 : 1;
        int side = x - medusa.x;

        if(side == 0) {
            for(int ny = y + stepY; !isNotBoard(x, ny); ny += stepY) {
                canSee[x][ny] = false;
            }
            return;
        }

        int ny = y + stepY;
        int depth = 1;
        if(side > 0) {
            while(!isNotBoard(x, ny)) {
                for(int k = 0; k <= depth; k++) {
                    int nx = x + k;
                    if(isNotBoard(nx, ny)) break;
                    canSee[nx][ny] = false;
                }
                ny += stepY;
                depth++;
            }
        } else {
            while(!isNotBoard(x, ny)) {
                for(int k = 0; k <= depth; k++) {
                    int nx = x - k;
                    if(isNotBoard(nx, ny)) break;
                    canSee[nx][ny] = false;
                }
                ny += stepY;
                depth++;
            }
        }
    }

    static void markTriangle(boolean[][] canSee, int dir, Loc medusa) {
        if(dir == 0) {
            for(int d = 1; d < n; d++) {
                int x = medusa.x - d;
                if(x < 0) break;
                int left = Math.max(0, medusa.y - d);
                int right = Math.min(n - 1, medusa.y + d);
                for(int y = left; y <= right; y++) canSee[x][y] = true;
            }
        } else if(dir == 1) {
            for(int d = 1; d < n; d++) {
                int x = medusa.x + d;
                if(x >= n) break;
                int left = Math.max(0, medusa.y - d);
                int right = Math.min(n - 1, medusa.y + d);
                for(int y = left; y <= right; y++) canSee[x][y] = true;
            }
        } else if(dir == 2) {
            for(int d = 1; d < n; d++) {
                int y = medusa.y - d;
                if(y < 0) break;
                int top = Math.max(0, medusa.x - d);
                int bottom = Math.min(n - 1, medusa.x + d);
                for(int x = top; x <= bottom; x++) canSee[x][y] = true;
            }
        } else {
            for(int d = 1; d < n; d++) {
                int y = medusa.y + d;
                if(y >= n) break;
                int top = Math.max(0, medusa.x - d);
                int bottom = Math.min(n - 1, medusa.x + d);
                for(int x = top; x <= bottom; x++) canSee[x][y] = true;
            }
        }
    }

    static int[][] toSightArray(boolean[][] canSee) {
        int[][] sight = new int[n][n];
        for(int x = 0; x < n; x++) {
            for(int y = 0; y < n; y++) {
                if(canSee[x][y]) sight[x][y] = 1;
            }
        }
        return sight;
    }

    static Sight topSight(int[][] sight, Loc cur) {
        for(int dist = 1; dist < n && cur.x - dist >= 0; dist++) {                          // 메두사 위치로 부터 한 칸씩 전진
            int x = cur.x - dist;
            sight[x][cur.y]++;
            for(int y = 1; y <= dist && cur.y + y < n; y++) sight[x][cur.y + y]++;         // 오른쪽
            for(int y = 1; y <= dist && cur.y - y >= 0; y++) sight[x][cur.y - y]++;        // 왼쪽
        }

        for(int dist = 1; dist < n && cur.x - dist >= 0; dist++) {
            int x = cur.x - dist;

            if(sight[x][cur.y] > 0 && !worrierBoard[x][cur.y].isEmpty()) {
                hideTopSight(sight, cur, x, cur.y);
            }

            for(int offset = 1; offset <= dist; offset++) {
                int rightY = cur.y + offset;
                if(rightY < n && sight[x][rightY] > 0 && !worrierBoard[x][rightY].isEmpty()) {
                    hideTopSight(sight, cur, x, rightY);
                }

                int leftY = cur.y - offset;
                if(leftY >= 0 && sight[x][leftY] > 0 && !worrierBoard[x][leftY].isEmpty()) {
                    hideTopSight(sight, cur, x, leftY);
                }
            }
        }
        return new Sight(sight, findVisibleWorriers(sight));
    }

    static Sight bottomSight(int[][] sight, Loc cur) {
        for(int dist = 1; dist < n && cur.x + dist < n; dist++) {
            int x = cur.x + dist;
            sight[x][cur.y]++;
            for(int y = 1; y <= dist && cur.y + y < n; y++) sight[x][cur.y + y]++;
            for(int y = 1; y <= dist && cur.y - y >= 0; y++) sight[x][cur.y - y]++;
        }

        for(int dist = 1; dist < n && cur.x + dist < n; dist++) {
            int x = cur.x + dist;

            if(sight[x][cur.y] > 0 && !worrierBoard[x][cur.y].isEmpty()) {
                hideBottomSight(sight, cur, x, cur.y);
            }

            for(int offset = 1; offset <= dist; offset++) {
                int rightY = cur.y + offset;
                if(rightY < n && sight[x][rightY] > 0 && !worrierBoard[x][rightY].isEmpty()) {
                    hideBottomSight(sight, cur, x, rightY);
                }

                int leftY = cur.y - offset;
                if(leftY >= 0 && sight[x][leftY] > 0 && !worrierBoard[x][leftY].isEmpty()) {
                    hideBottomSight(sight, cur, x, leftY);
                }
            }
        }
        return new Sight(sight, findVisibleWorriers(sight));
    }

    static Sight leftSight(int[][] sight, Loc cur) {
        for(int dist = 1; dist < n && cur.y - dist >= 0; dist++) {
            int y = cur.y - dist;
            sight[cur.x][y]++;
            for(int x = 1; x <= dist && cur.x + x < n; x++) sight[cur.x + x][y]++;
            for(int x = 1; x <= dist && cur.x - x >= 0; x++) sight[cur.x - x][y]++;
        }

        for(int dist = 1; dist < n && cur.y - dist >= 0; dist++) {
            int y = cur.y - dist;

            if(sight[cur.x][y] > 0 && !worrierBoard[cur.x][y].isEmpty()) {
                hideLeftSight(sight, cur, cur.x, y);
            }

            for(int offset = 1; offset <= dist; offset++) {
                int downX = cur.x + offset;
                if(downX < n && sight[downX][y] > 0 && !worrierBoard[downX][y].isEmpty()) {
                    hideLeftSight(sight, cur, downX, y);
                }

                int upX = cur.x - offset;
                if(upX >= 0 && sight[upX][y] > 0 && !worrierBoard[upX][y].isEmpty()) {
                    hideLeftSight(sight, cur, upX, y);
                }
            }
        }
        return new Sight(sight, findVisibleWorriers(sight));
    }

    static Sight rightSight(int[][] sight, Loc cur) {
        for(int dist = 1; dist < n && cur.y + dist < n; dist++) {
            int y = cur.y + dist;
            sight[cur.x][y]++;
            for(int x = 1; x <= dist && cur.x + x < n; x++) sight[cur.x + x][y]++;
            for(int x = 1; x <= dist && cur.x - x >= 0; x++) sight[cur.x - x][y]++;
        }

        for(int dist = 1; dist < n && cur.y + dist < n; dist++) {
            int y = cur.y + dist;

            if(sight[cur.x][y] > 0 && !worrierBoard[cur.x][y].isEmpty()) {
                hideRightSight(sight, cur, cur.x, y);
            }

            for(int offset = 1; offset <= dist; offset++) {
                int downX = cur.x + offset;
                if(downX < n && sight[downX][y] > 0 && !worrierBoard[downX][y].isEmpty()) {
                    hideRightSight(sight, cur, downX, y);
                }

                int upX = cur.x - offset;
                if(upX >= 0 && sight[upX][y] > 0 && !worrierBoard[upX][y].isEmpty()) {
                    hideRightSight(sight, cur, upX, y);
                }
            }
        }
        return new Sight(sight, findVisibleWorriers(sight));
    }

    static void hideTopSight(int[][] sight, Loc medusa, int wx, int wy) {
        int nextX = wx - 1;
        if(nextX < 0) return;

        if(wy == medusa.y) {
            for(int x = nextX; x >= 0; x--) sight[x][wy]--;
            return;
        }

        int left = wy;
        int right = wy;

        if(wy < medusa.y) {                                                                  // 왼쪽 위 전사
            for(int x = nextX; x >= 0; x--) {
                left--;
                for(int y = left; y <= right; y++) {
                    if(y < 0 || y >= n) continue;
                    sight[x][y]--;
                }
            }
            return;
        }

        for(int x = nextX; x >= 0; x--) {                                                    // 오른쪽 위 전사
            right++;
            for(int y = left; y <= right; y++) {
                if(y < 0 || y >= n) continue;
                sight[x][y]--;
            }
        }
    }

    static void hideBottomSight(int[][] sight, Loc medusa, int wx, int wy) {
        int nextX = wx + 1;
        if(nextX >= n) return;

        if(wy == medusa.y) {
            for(int x = nextX; x < n; x++) {
                sight[x][wy]--;
            }
            return;
        }

        int left = wy;
        int right = wy;

        if(wy < medusa.y) {
            for(int x = nextX; x < n; x++) {
                left--;
                for(int y = left; y <= right; y++) {
                    if(y < 0 || y >= n) continue;
                    sight[x][y]--;
                }
            }
            return;
        }

        for(int x = nextX; x < n; x++) {
            right++;
            for(int y = left; y <= right; y++) {
                if(y < 0 || y >= n) continue;
                sight[x][y]--;
            }
        }
    }

    static void hideLeftSight(int[][] sight, Loc medusa, int wx, int wy) {
        int nextY = wy - 1;
        if(nextY < 0) return;

        if(wx == medusa.x) {
            for(int y = nextY; y >= 0; y--) {
                sight[wx][y]--;
            }
            return;
        }

        int top = wx;
        int bottom = wx;

        if(wx < medusa.x) {
            for(int y = nextY; y >= 0; y--) {
                top--;
                for(int x = top; x <= bottom; x++) {
                    if(x < 0 || x >= n) continue;
                    sight[x][y]--;
                }
            }
            return;
        }

        for(int y = nextY; y >= 0; y--) {
            bottom++;
            for(int x = top; x <= bottom; x++) {
                if(x < 0 || x >= n) continue;
                sight[x][y]--;
            }
        }
    }

    static void hideRightSight(int[][] sight, Loc medusa, int wx, int wy) {
        int nextY = wy + 1;
        if(nextY >= n) return;

        if(wx == medusa.x) {
            for(int y = nextY; y < n; y++) {
                sight[wx][y]--;
            }
            return;
        }

        int top = wx;
        int bottom = wx;

        if(wx < medusa.x) {
            for(int y = nextY; y < n; y++) {
                top--;
                for(int x = top; x <= bottom; x++) {
                    if(x < 0 || x >= n) continue;
                    sight[x][y]--;
                }
            }
            return;
        }

        for(int y = nextY; y < n; y++) {
            bottom++;
            for(int x = top; x <= bottom; x++) {
                if(x < 0 || x >= n) continue;
                sight[x][y]--;
            }
        }
    }


    // ============= 메두사 이동 경로 ================

    static List<Integer> findVisibleWorriers(int[][] sight) {
        List<Integer> stoneIds = new ArrayList<>();

        for(int x = 0; x < n; x++) {
            for(int y = 0; y < n; y++) {
                if(sight[x][y] <= 0) continue;

                for(int wId : worrierBoard[x][y]) {
                    if(!worriers.containsKey(wId)) continue;
                    stoneIds.add(wId);
                }
            }
        }

        return stoneIds;
    }

    static class Node extends Loc {
        Node prev;

        Node(int x, int y, Node prev) {
            super(x, y);
            this.prev = prev;
        }
    }

    static void findRouteToPark(Deque<Loc> medusaLocs, Loc medusa) {
        // 메두사 이동은 {상 하 좌 우} 우선순위
        int[] dx = {-1, 1, 0, 0};
        int[] dy = {0, 0, -1, 1};

        Queue<Node> q = new ArrayDeque<>();
        boolean[][] visited = new boolean[n][n];

        q.offer(new Node(medusa.x, medusa.y, null));
        visited[medusa.x][medusa.y] = true;

        while(!q.isEmpty()) {
            Node cur = q.poll();

            if(cur.x == park.x && cur.y == park.y) {        // 공원에 도착한 경우
                traceRouteToPark(medusaLocs, cur);
                return;
            }

            for(int dir = 0; dir < 4; dir++) {
                int nx = cur.x + dx[dir];
                int ny = cur.y + dy[dir];
                if(isNotBoard(nx, ny)) continue;            // 격자를 벗어나는 경우
                if(visited[nx][ny]) continue;               // 이미 방문한 경우
                if(board[nx][ny] == 1) continue;            // 격자 내에서 이동할 수 없는 경우

                visited[nx][ny] = true;
                q.offer(new Node(nx, ny, cur));
            }
        }
    }

    static void traceRouteToPark(Deque<Loc> medusaLocs, Node cur) {
        while(cur.prev != null) {                           // 시작 위치를 제외한 이동 경로 전체
            medusaLocs.offerFirst(new Loc(cur.x, cur.y));
            cur = cur.prev;
        }
    }

    // ================= 공통 모듈 ==============================

    static boolean isNotBoard(int x, int y) {
        return x < 0 || y < 0 || x >= n || y >= n;
    }

    static int getDist(int x1, int y1, int x2, int y2) {
        return Math.abs(x1 - x2) + Math.abs(y1 - y2);
    }
}
