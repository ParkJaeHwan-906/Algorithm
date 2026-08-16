package aug.week2.codetree;

import java.util.*;
import java.io.*;

public class 코드트리빵_박재환 {
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

    static class BaseCamp extends Loc {
        BaseCamp(int x, int y) {
            super(x, y);
        }
    }

    static class Store extends Loc {
        Store(int x, int y) {
            super(x, y);
        }
    }

    static class Person extends Loc {
        Store store;
        boolean exit;
        Person(int x, int y, Store store) {
            super(x, y);
            this.store = store;
            this.exit = false;
        }
    }

    static int n, m;
    static int[][] board;
    static Map<Integer, BaseCamp> baseCamps;
    static Store[] stores;
    static void init(BufferedReader br) throws IOException {
        StringTokenizer st;

        st = new StringTokenizer(br.readLine().trim());
        n = Integer.parseInt(st.nextToken());               // 격자 크기
        m = Integer.parseInt(st.nextToken());               // 편의점 수

        board = new int[n][n];
        baseCamps = new HashMap<Integer, BaseCamp>();
        for(int x = 0; x < n; x++) {
            st = new StringTokenizer(br.readLine().trim());
            /**
             * 0 : 빈 공간
             * 1 : 베이스캠프
             */
            for(int y = 0; y < n; y++) {
                board[x][y] = Integer.parseInt(st.nextToken());
                if(board[x][y] == 1) baseCamps.put(getKey(x, y), new BaseCamp(x, y));
            }
        }

        stores = new Store[m];
        for(int i = 0; i < m; i++) {
            st = new StringTokenizer(br.readLine().trim());
            int x = Integer.parseInt(st.nextToken()) - 1;
            int y = Integer.parseInt(st.nextToken()) - 1;
            stores[i] = new Store(x, y);
        }

        System.out.println(solution());
    }

    static int exitPersons;
    static List<Person> persons;
    static boolean[][] blocked;
    static int solution() {
        int time = 0;
        exitPersons = 0;
        persons = new ArrayList<>();
        blocked = new boolean[n][n];
        while(true) {
            // 1. 격자 내 사람 이동
            movePersons();
            if(exitPersons == m) break;
            if(time >= m) {
                time++;             // 시간 증가
                continue;
            }
            // 2. 새로운 사람 투입
            int pId = time;     // 현재 투입되는 사람의 ID
            putPerson(pId);
            time++;             // 시간 증가
        }
        return time + 1;
    }

    static final int[] dx = {-1, 0, 0, 1};
    static final int[] dy = {0, -1, 1, 0};
    static final int INF = Integer.MAX_VALUE;
    static void movePersons() {
        /**
         * 격자 내 인물들을 이동시킵니다.
         * - 본인이 가고자하는 편의점 방향을 향해 1칸 움직입니다.
         * - 편의점에 도착하면 멈추고, 이번 턴 이후 해당 위치를 아무도 지나갈 수 없습니다.
         * - 이동 중 동일 칸에 둘 이상의 사람이 위치할 수 있습니다.
         */
        List<Person> exit = new ArrayList<>();              // 나간 사람들 -> 영구적으로 해당 부분 이동 불가
        for(Person p : persons) {
            if(p.exit) continue;
            int[][] dists = getDists(p.store.x, p.store.y);
            int minDist = INF;
            int minX = p.x;
            int minY = p.y;
            for(int dir = 0; dir < 4; dir++) {
                int nx = p.x + dx[dir];
                int ny = p.y + dy[dir];
                if(isNotBoard(nx, ny)) continue;
                if(blocked[nx][ny]) continue;

                if(minDist > dists[nx][ny]) {
                    minDist = dists[nx][ny];
                    minX = nx;
                    minY = ny;
                }
            }
            p.x = minX;
            p.y = minY;

            // 도착한 위치가 편의점인지 확인
            if(p.x == p.store.x && p.y == p.store.y) {
                exit.add(p);
            }
        }

        // 더 이상 움직이지 않아도 되는 인물 처리
        for(Person p : exit) {
            exitPersons++;
            p.exit = true;
            blocked[p.x][p.y] = true;
        }
    }

    static void putPerson(int pId) {
        BaseCamp baseCamp = selectBaseCamp(pId);
        Person person = new Person(baseCamp.x, baseCamp.y, stores[pId]);
        persons.add(person);
        blocked[person.x][person.y] = true;
    }

    static final BaseCamp DUMMY = new BaseCamp(Integer.MAX_VALUE, Integer.MAX_VALUE);
    static BaseCamp selectBaseCamp(int pId) {
        Store targetStore = stores[pId];            // pId 사람이 가고자 하는 편의점
        /**
         * 편의점과 가장 가까운 베이스캠프를 찾는다.
         * - 거리가 같다면 행 > 열 순으로 작은 항목이 우선순위를 갖는다.
         */
        int bestDist = Integer.MAX_VALUE;
        BaseCamp candBaseCamp = DUMMY;
        int[][] dists = getDists(targetStore.x, targetStore.y);
        for(BaseCamp baseCamp : baseCamps.values()) {
            if(blocked[baseCamp.x][baseCamp.y]) continue;
            int dist = dists[baseCamp.x][baseCamp.y];
            if(bestDist > dist ||
                    (bestDist == dist && candBaseCamp.x > baseCamp.x ||
                            ((bestDist == dist && candBaseCamp.x == baseCamp.x && candBaseCamp.y > baseCamp.y)))) {
                candBaseCamp = baseCamp;
                bestDist = dist;
            }
        }
        return candBaseCamp;
    }

    static boolean isNotBoard(int x, int y) {
        return x < 0 || y < 0 || x >= n || y >= n;
    }

    static int[][] getDists(int targetX, int targetY) {
        int[][] dists = new int[n][n];
        for(int x = 0; x < n; x++) Arrays.fill(dists[x], INF);

        Queue<Loc> queue = new ArrayDeque<>();
        queue.add(new Loc(targetX, targetY));
        dists[targetX][targetY] = 0;

        while(!queue.isEmpty()) {
            Loc cur = queue.poll();
            for(int dir = 0; dir < 4; dir++) {
                int nx = cur.x + dx[dir];
                int ny = cur.y + dy[dir];
                if(isNotBoard(nx, ny)) continue;
                if(blocked[nx][ny]) continue;
                if(dists[nx][ny] != INF) continue;

                dists[nx][ny] = dists[cur.x][cur.y] + 1;
                queue.add(new Loc(nx, ny));
            }
        }
        return dists;
    }

    static int getKey(int x, int y) {
        return x * (n + 7) + y;
    }
}
