package nov.week2.codetree;

import java.util.*;
import java.io.*;

public class 팩맨_박재환 {
    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        System.out.print(init(br));
        br.close();
    }

    static final int[] dx = {-1, -1, 0, 1, 1, 1, 0, -1};
    static final int[] dy = {0, -1, -1, -1, 0, 1, 1, 1};

    static final int[] pdx = {-1, 0, 1, 0};
    static final int[] pdy = {0, -1, 0, 1};

    static class Loc {
        int x, y;
        Loc(int x, int y) {
            this.x = x;
            this.y = y;
        }
    }

    static class Monster extends Loc {
        int id;
        int dir;
        boolean dead;
        int remainRemove;
        Monster(int x, int y, int dir) {
            super(x, y);
            this.dir = dir;
            this.dead = false;
            this.remainRemove = 0;
        }
        void rotate45Degree() {
            this.dir = (dir + 1) % 8;
        }
    }

    static final Monster DUMMY = new Monster(-1, -1, -1);

    static int m, t;
    static Loc packMan;
    static Queue<Monster> monsters;
    static int[][] deadMonstersBoard;
    static int init(BufferedReader br) throws IOException {
        StringTokenizer st;
        st = new StringTokenizer(br.readLine().trim());
        m = Integer.parseInt(st.nextToken());           // 몬스터 마리 수
        t = Integer.parseInt(st.nextToken());           // 턴의 수

        st = new StringTokenizer(br.readLine().trim());
        packMan = new Loc(
                Integer.parseInt(st.nextToken()) - 1,
                Integer.parseInt(st.nextToken()) - 1
        );

        monsters = new ArrayDeque<>();
        deadMonstersBoard = new int[4][4];
        for(int i = 0; i < m; i++) {
            st = new StringTokenizer(br.readLine().trim());
            monsters.offer(new Monster(
                    Integer.parseInt(st.nextToken()) - 1,
                    Integer.parseInt(st.nextToken()) - 1,
                    Integer.parseInt(st.nextToken()) - 1
            ));
        }
        return solution();
    }

    static Queue<Monster> cloneQueue;
    static List<Monster> afterDeadMonsters;
    static int solution() {
        afterDeadMonsters = new ArrayList<>();
        cloneQueue = new ArrayDeque<>();
        while(t-- > 0) {
            cloneMonster();
            moveMonsters();
            movePackMan();
            decreaseDeadMonsters();
            putClone();
        }
        return m;
    }

    static void cloneMonster() {
        int extra = 0;
        for(Monster monster : monsters) {
            if(monster.dead)
                continue;
            cloneQueue.offer(new Monster(
                    monster.x,
                    monster.y,
                    monster.dir
            ));
        }
    }

    static List<Monster>[][] monsterBoard;
    static void moveMonsters() {
        monsterBoard = new List[4][4];
        for(int x = 0; x < 4; x++) {
            for(int y = 0; y < 4; y++)
                monsterBoard[x][y] = new ArrayList<>();
        }
        Queue<Monster> temp = new ArrayDeque<>();
        while(!monsters.isEmpty()) {
            Monster monster = monsters.poll();
            if(monster.dead)
                continue;
            moveMonster(monster);
            monsterBoard[monster.x][monster.y].add(monster);
            temp.offer(monster);
        }
        monsters = temp;
    }

    static void moveMonster(Monster monster) {
        int nx = monster.x + dx[monster.dir];
        int ny = monster.y + dy[monster.dir];

        if(isNotBoard(nx, ny)
                || deadMonstersBoard[nx][ny] > 0
                || (packMan.x == nx && packMan.y == ny)) {
            boolean alternativeDir = false;
            for(int dir = 0; dir < 8; dir++) {
                monster.rotate45Degree();
                nx = monster.x + dx[monster.dir];
                ny = monster.y + dy[monster.dir];
                if(isNotBoard(nx, ny)
                        || deadMonstersBoard[nx][ny] > 0
                        || (packMan.x == nx && packMan.y == ny))
                    continue;
                alternativeDir = true;
                break;
            }
            if(!alternativeDir)
                return;
        }
        monster.x = nx;
        monster.y = ny;
    }

    static class Node extends Loc {
        Node prev;
        int moved;
        int count;
        Node(int x, int y, int moved, int count, Node prev) {
            super(x, y);
            this.moved = moved;
            this.count = count;
            this.prev = prev;
        }
    }

    static void movePackMan() {
        Queue<Node> q = new ArrayDeque<>();
        q.offer(new Node(packMan.x, packMan.y, 0, 0, null));

        Node lastNode = null;
        while(!q.isEmpty()) {
            Node cur = q.poll();
            if(cur.moved == 3) {
                if(lastNode == null
                    || lastNode.count < cur.count)
                    lastNode = cur;
                continue;
            }
            for(int dir = 0; dir < 4; dir++) {
                int nx = cur.x + pdx[dir];
                int ny = cur.y + pdy[dir];
                if(isNotBoard(nx, ny))
                    continue;
                int newCount = cur.count + (cur.prev != null && cur.prev.x == nx && cur.prev.y == ny ? 0 : monsterBoard[nx][ny].size());
                q.offer(new Node(nx, ny, cur.moved + 1, newCount, cur));
            }
        }
        recoverRoute(lastNode);
    }

    static void recoverRoute(Node cur) {
        packMan.x = cur.x;
        packMan.y = cur.y;
        while(cur.prev != null) {
            List<Monster> removedTarget = monsterBoard[cur.x][cur.y];
            for(Monster monster : removedTarget) {
                monster.dead = true;
                monster.remainRemove = 3;
                deadMonstersBoard[cur.x][cur.y] = 3;
                m--;
            }
            monsterBoard[cur.x][cur.y].clear();
            cur = cur.prev;
        }
    }

    static void decreaseDeadMonsters() {
        for(int x = 0; x < 4; x++) {
            for(int y = 0; y < 4; y++) {
                if(deadMonstersBoard[x][y] > 0) {
                    deadMonstersBoard[x][y]--;
                }
            }
        }
    }

    static void putClone() {
        while(!cloneQueue.isEmpty()) {
            Monster monster = cloneQueue.poll();
            monsters.offer(monster);
            m++;
        }
    }

    static boolean isNotBoard(int x, int y) {
        return x < 0 || y < 0 || x >= 4 || y >= 4;
    }
}
