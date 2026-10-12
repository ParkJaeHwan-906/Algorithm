package oct.week2.codetree;

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

    static class Loc {
        int x, y;
        Loc(int x, int y) {
            this.x = x;
            this.y = y;
        }
        public boolean equals(Object o) {
            if(o == this) {
                return true;
            }
            if(!(o instanceof Loc)) {
                return false;
            }
            Loc loc = (Loc) o;
            return loc.x == x && loc.y == y;
        }
        public int hashCode() {
            return Objects.hash(x, y);
        }
    }

    static class Box extends Loc {
        int id;
        int inTime;
        int weight;
        int outTime;
        int count;
        boolean broken;
        Box(int id, int inTime, int x, int y, int weight) {
            super(x, y);
            this.id = id;
            this.inTime = inTime;
            this.weight = weight;
            this.outTime = -1;
            this.count = 0;
            this.broken = false;
        }
    }

    static int n, b;
    static int[][] board;
    static Box[] boxes;
    static PriorityQueue<Box> waitBoxes;
    static void init(BufferedReader br) throws IOException {
        StringTokenizer st;

        n = Integer.parseInt(br.readLine().trim());
        board = new int[n][n];
        for(int x = 0; x < n; x++) {
            st = new StringTokenizer(br.readLine().trim());
            for(int y = 0; y < n; y++) {
                /**
                 * 0 : 벽
                 * 1-4 : 컨베이어벨트
                 * 9: 적재장소
                 */
                board[x][y] = Integer.parseInt(st.nextToken());
            }
        }

        b = Integer.parseInt(br.readLine().trim());
        boxes = new Box[b + 1];
        waitBoxes = new PriorityQueue<>((a, b) -> Integer.compare(a.inTime, b.inTime));
        for(int i = 1; i <= b; i++) {
            st = new StringTokenizer(br.readLine().trim());
            int inTime = Integer.parseInt(st.nextToken());
            int x = Integer.parseInt(st.nextToken()) - 1;
            int y = Integer.parseInt(st.nextToken()) - 1;
            int weight = Integer.parseInt(st.nextToken());
            Box box = new Box(i, inTime, x, y, weight);
            boxes[i] = box;
            waitBoxes.add(box);
        }

        solution();
    }

    static int outBoxes;
    static Map<Loc, Box> onBeltBoxes;
    static void solution() {
        outBoxes = 0;
        onBeltBoxes = new HashMap<>();

        int turn = 0;
        while(++turn <= 200 && outBoxes < b) {
            // 1. 수하물 이동
            moveBoxes(turn);
            // 2. 적재 및 파손
            put(turn);
        }
        System.out.print(getResult());
    }

    static void moveBoxes(int turn) {
        Queue<Loc> emptyLocs = new ArrayDeque<>();
        for(int x = 0; x < n; x++) {
            for(int y = 0; y < n; y++) {
                if(onBeltBoxes.containsKey(new Loc(x, y))
                    || board[x][y] == 0) {
                    continue;
                }
                emptyLocs.offer(new Loc(x, y));
            }
        }
        Set<Box> afterMovedBoxes = new HashSet<>();
        while(!emptyLocs.isEmpty()) {
            Loc cur = emptyLocs.poll();
            Box candBox = null;
            for(int dir = 1; dir <= 4; dir++) {
                int nx = cur.x + dx[dir];
                int ny = cur.y + dy[dir];
                if(isNotBoard(nx, ny)
                        || board[nx][ny] == 0
                        || !onBeltBoxes.containsKey(new Loc(nx, ny))) {
                    continue;
                }
                if(board[nx][ny] != oppositeDir(dir)) {
                    continue;
                }
                Box newCandBox = onBeltBoxes.get(new Loc(nx, ny));
                if(afterMovedBoxes.contains(newCandBox)) {
                    continue;
                }
                if(candBox == null) {
                    candBox = newCandBox;
                } else {
                    candBox = moveCompare(candBox, newCandBox);
                }
            }
            if(candBox != null) {
                onBeltBoxes.remove(new Loc(candBox.x, candBox.y));
                emptyLocs.offer(new Loc(candBox.x, candBox.y));
                candBox.count = 0;
                candBox.x = cur.x;
                candBox.y = cur.y;
                if(board[candBox.x][candBox.y] == 9) {
                    outBoxes++;
                    candBox.outTime = turn;
                    continue;
                }
                onBeltBoxes.put(new Loc(cur.x, cur.y), candBox);
                afterMovedBoxes.add(candBox);
            }
        }
        for(Box box : onBeltBoxes.values()) {
            if(afterMovedBoxes.contains(box)) {
                box.count = 0;
                continue;
            }
            if(++box.count >= 3) {
                box.broken = true;
            }
        }
    }

    static void put(int turn) {
        Queue<Box> returnedBoxes = new ArrayDeque<>();
        Map<Loc, Box> candBoxes = new HashMap<>();
        while(!waitBoxes.isEmpty() && waitBoxes.peek().inTime <= turn) {
            Box box = waitBoxes.poll();
            if(onBeltBoxes.containsKey(new Loc(box.x, box.y))) {
                returnedBoxes.offer(box);
                continue;
            }

            if(candBoxes.containsKey(new Loc(box.x, box.y))) {
                Box origin = candBoxes.get(new Loc(box.x, box.y));
                Box newBox = putCompare(origin, box);
                if(origin == newBox) {
                    returnedBoxes.offer(box);
                } else {
                    returnedBoxes.offer(origin);
                    candBoxes.put(new Loc(box.x, box.y), box);
                }
            } else {
                candBoxes.put(new Loc(box.x, box.y), box);
            }
        }

        for(Box box : candBoxes.values()) {
            if(board[box.x][box.y] == 9) {
                box.outTime = turn;
                outBoxes++;
                continue;
            }
            onBeltBoxes.put(new Loc(box.x, box.y), box);
        }

        while(!returnedBoxes.isEmpty()) {
            waitBoxes.offer(returnedBoxes.poll());
        }
    }

    static String getResult() {
        StringBuilder sb = new StringBuilder();
        for(int i = 1; i <= b; i++) {
            sb.append((boxes[i].broken ? -1 : 1) * boxes[i].outTime).append(' ');
        }
        return sb.toString();
    }

    static Box moveCompare(Box a, Box b) {
        if(a.weight != b.weight) {
            return a.weight > b.weight ? a : b;
        }
        return a.id < b.id ? a : b;
    }

    static Box putCompare(Box a, Box b) {
        return a.inTime < b.inTime ? a : b;
    }

    static int oppositeDir(int dir) {
        if(dir == 1) {
            return 3;
        }
        if(dir == 2) {
            return 4;
        }
        if(dir == 3) {
            return 1;
        }
        return 2;
    }

    static boolean isNotBoard(int x, int y) {
        return x < 0 || y < 0 || x >= n || y >= n;
    }

    static void printState(int turn) {
        System.out.printf("[Turn] %d\n", turn);
        System.out.println("[Box Board]");
        int[][] boxBoard = new int[n][n];
        for(Box box : onBeltBoxes.values()) boxBoard[box.x][box.y] = box.id;
        for(int[] arr : boxBoard) System.out.println(Arrays.toString(arr));
        System.out.println();
    }
}
