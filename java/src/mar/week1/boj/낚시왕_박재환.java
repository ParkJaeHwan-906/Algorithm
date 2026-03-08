package mar.week1.boj;

import java.util.*;
import java.io.*;

public class 낚시왕_박재환 {
    static BufferedReader br;
    public static void main(String[] args) throws IOException {
        br = new BufferedReader(new InputStreamReader(System.in));
        init();
        br.close();
    }

    /**
     * 초기 : 1번 열의 한 칸 왼쪽에 있다.
     * 종료 : 가장 오른쪽 열의 오른쪽 칸에 이동하면 멈춘다.
     *
     * [낚시왕 이동]
     * 낚시왕이 오른쪽으로 한 칸 이동한다.
     * [낚시]
     * 낚시왕이 있는 열에 있는 상어 중 땅과 제일 가까운 상어를 잡는다. -> 그 상어는 사라진다.
     * [상어 이동]
     * 상어는 입력으로 주어진 속도로 이동한다. (초당 움직이는 칸 수)
     * 이동하고자 하는 칸이 격자 경계를 넘는 경우 방향을 반대로 바꾼다.
     * 이동 후 같은 칸에 상어가 여러마리 있을 수 있다. -> 크기가 가장 큰 상어가 나머지 상어를 모두 잡아먹는다.
     */
    static class Shark implements Comparable<Shark> {
        int x, y, size;
        int d, move;

        Shark(int x, int y, int size, int d, int move) {
            this.x = x;
            this.y = y;
            this.size = size;
            this.d = d;
            this.move = move;
        }

        public int compareTo(Shark o) {
            return Integer.compare(this.x, o.x);
        }
    }
    static class FisherMan {
        int y;      // 가로로만 이동
        int accSize;

        FisherMan() {
            this.y = 0;
            this.accSize = 0;
        }
    }
    static StringTokenizer st;
    static int r, c, m;
    static Map<Integer, PriorityQueue<Shark>> sharks;
    static void init() throws IOException {
        st = new StringTokenizer(br.readLine().trim());
        r = Integer.parseInt(st.nextToken());
        c = Integer.parseInt(st.nextToken());
        m = Integer.parseInt(st.nextToken());

        sharks = new HashMap<>();
        for(int i=0; i<m; i++) {
            st = new StringTokenizer(br.readLine().trim());
            int x = Integer.parseInt(st.nextToken());
            int y = Integer.parseInt(st.nextToken());
            int move = Integer.parseInt(st.nextToken());
            int d = Integer.parseInt(st.nextToken())-1;
            int size = Integer.parseInt(st.nextToken());

            Shark shark = new Shark(x, y, size, d, move);
            sharks.computeIfAbsent(y, k -> new PriorityQueue<>()).offer(shark);
        }

        solution();
    }
    static void solution() {
        FisherMan fisherMan = new FisherMan();

        while(fisherMan.y < c) {
            // 1. 낚시왕 이동
            fisherMan.y++;
            // 2. 낚시
            PriorityQueue<Shark> pq = sharks.get(fisherMan.y);
            if(pq != null && !pq.isEmpty()) {
                Shark shark = pq.poll();
                fisherMan.accSize += shark.size;
            }
            // 3. 상어 이동
            setShark();
        }

        System.out.println(fisherMan.accSize);
    }
    static final int[] dx = {-1,1,0,0};
    static final int[] dy = {0,0,1,-1};
    static void setShark() {
        Map<Integer, Shark> king = new HashMap<>();

        for(PriorityQueue<Shark> pq : sharks.values()) {
            while(!pq.isEmpty()) {
                Shark shark = pq.poll();
                // 상어의 이동을 1칸씩이 아닌, 점프로 처리해야함
                moveShark(shark);

                int key = shark.x * 207 + shark.y;
                if(king.get(key) != null) {
                    Shark shark1 = king.get(key);
                    if(shark1.size > shark.size) continue;
                }
                king.put(key, shark);
            }
        }

        sharks.clear();
        for(Shark s : king.values()) {
            sharks.computeIfAbsent(s.y, k -> new PriorityQueue<>()).offer(s);
        }
    }

    static void moveShark(Shark shark) {
        boolean vertical = (shark.d == 0 || shark.d == 1);
        int len = vertical ? r : c;
        if(len == 1) return;            // 길이가 1칸이면 어차피 이동 불가
        int cycle = 2 * (len-1);        // ex) 1 ~ r => r-1칸 이동 -> 왕복 운동 -> 이동 변화 X
        int s = shark.move % cycle;
        if(s == 0) return;

        int loc = (vertical ? shark.x : shark.y);
        int dir = (shark.d == 1 || shark.d == 2) ? 1 : -1; // 하/우:+1, 상/좌:-1
        while(s > 0) {
            int toEnd = (dir == 1) ? (len - loc) : (loc - 1);       // 벽 까지 거리
            if(s <= toEnd) {
                loc += (dir * s);
                s = 0;
            } else {
                loc += (dir * toEnd);
                s -= toEnd;
                dir *= -1;
            }
        }

        if (vertical) {
            shark.x = loc;
            shark.d = (dir == 1) ? 1 : 0; // 하 / 상
        } else {
            shark.y = loc;
            shark.d = (dir == 1) ? 2 : 3; // 우 / 좌
        }
    }
}
