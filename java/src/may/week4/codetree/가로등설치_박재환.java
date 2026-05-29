package may.week4.codetree;

import java.util.*;
import java.io.*;

/**
 * AI 사용 여부 O
 * => query() 함수에서 초기에는 이분탐색으로 접근 -> 비교군 특정으로 수정했습니다.
 */
public class 가로등설치_박재환 {
    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        String result = init(br);
        System.out.println(result);
        br.close();
    }

    static class Lamp {
        int x;          // 가로등 위치
        int l, r;       // 좌 / 우 가로등 위치

        Lamp(int x, int l, int r) {
            this.x = x;
            this.l = l;
            this.r = r;
        }

        void delete() {
            this.x = -1;
            this.l = -1;
            this.r = -1;
        }
    }

    final static int SET = 100;
    final static int ADD = 200;
    final static int DELETE = 300;
    final static int QUERY = 400;
    /**
     * 마을 거리는 1 ~ n 으로 표현
     * 가로등은 r 의 범위를 밝힘 ( x - r ~ x + r )
     */
    static int n, m;        // n : 거리, m : 초기 가로등 개수
    static List<Lamp> lamps;
    static int firstId, lastId;
    static PriorityQueue<Between> betweens;

    static String init(BufferedReader br) throws IOException {
        StringTokenizer st;
        StringBuilder sb = new StringBuilder();
        int q = Integer.parseInt(br.readLine().trim());
        while (q-- > 0) {
            st = new StringTokenizer(br.readLine().trim());
            int type = Integer.parseInt(st.nextToken());

            if (type == SET) {
                set(st);
            } else if (type == ADD) {
                add();
            } else if (type == DELETE) {
                delete(st);
            } else if (type == QUERY) {
                sb.append(2 * query()).append('\n');
            }
        }
        return sb.toString();
    }

    static void set(StringTokenizer st) {
        n = Integer.parseInt(st.nextToken());
        m = Integer.parseInt(st.nextToken());

        lamps = new ArrayList<>();
        betweens = new PriorityQueue<>();

        firstId = 1;
        lastId = m;

        lamps.add(new Lamp(-1, -1, -1));       // 1 - based
        for (int i = 1; i < m + 1; i++) {
            int x = Integer.parseInt(st.nextToken());
            Lamp lamp;
            if (i == 1) {
                lamp = new Lamp(x, -1, i + 1);
            } else {
                if (i == m) {
                    lamp = new Lamp(x, i - 1, -1);
                } else {
                    lamp = new Lamp(x, i - 1, i + 1);
                }

                int l = lamps.get(i - 1).x;
                int r = x;
                int dist = r - l;
                Between between = new Between(i - 1, i, dist);
                betweens.offer(between);
            }
            lamps.add(lamp);
        }
    }

    static class Between implements Comparable<Between> {
        int lId, rId;
        int dist;

        Between(int lId, int rId, int dist) {
            this.lId = lId;
            this.rId = rId;
            this.dist = dist;
        }

        public int compareTo(Between o) {
            if(this.dist == o.dist) return Integer.compare(this.lId, o.lId);
            return Integer.compare(o.dist, this.dist);
        }
    }

    static void add() {
        /**
         * 인접한 두 가로등의 거리가 가장 먼 중간 위치에 가로등을 설치합니다.
         */
        while(!betweens.isEmpty()) {
            Between temp = betweens.peek();
            if(isValid(temp)) break;
            betweens.poll();
        }

        // 유효한 거리 선택
        Between between = betweens.poll();
        int lId = between.lId;
        int rId = between.rId;
        Lamp lLamp = lamps.get(lId);
        Lamp rLamp = lamps.get(rId);

        int newId = lamps.size();
        int newX = (lLamp.x + rLamp.x + 1) / 2;     // 새롭게 설치될 위치
        Lamp newLamp = new Lamp(newX, lId, rId);

        // 기존 램프 업데이트
        lLamp.r = newId;
        rLamp.l = newId;

        lamps.add(newLamp);

        // 새로운 Between 업데이트
        Between lBetween = new Between(lId, newId, (newLamp.x - lLamp.x));
        Between rBetween = new Between(newId, rId, (rLamp.x - newLamp.x));

        betweens.offer(lBetween);
        betweens.offer(rBetween);
    }

    static boolean isValid(Between temp) {
        /**
         * 해당 Between 이 유효한지 확인
         * 각 lId, rId를 확인해서 유요한지, 서로 연결되어 있는게 맞는지 확인
         */

        Lamp lLamp = lamps.get(temp.lId);
        Lamp rLamp = lamps.get(temp.rId);

        if(lLamp.x == -1 || rLamp.x == -1) return false;                // 제거된 램프가 포함된 경우
        if(lLamp.r != temp.rId || rLamp.l != temp.lId) return false;    // 서로 연결되어 있지 않은 경우

        return true;
    }

    static void delete(StringTokenizer st) {
        int deleteId = Integer.parseInt(st.nextToken());
        Lamp deleteLamp = lamps.get(deleteId);

        int lId = deleteLamp.l;
        int rId = deleteLamp.r;

        if (deleteId == firstId) firstId = rId;
        if (deleteId == lastId) lastId = lId;

        // 램프 삭제
        deleteLamp.delete();

        // 기존 램프들 연결
        if(lId != -1) lamps.get(lId).r = rId;
        if(rId != -1) lamps.get(rId).l = lId;


        if(lId != -1 && rId != -1) {
            int dist = lamps.get(rId).x - lamps.get(lId).x;
            Between between = new Between(lId, rId, dist);
            betweens.offer(between);
        }
    }

    static int query() {
        int result = 0;

        Lamp first = lamps.get(firstId);
        Lamp last = lamps.get(lastId);

        result = Math.max(result, 2 * (first.x - 1));
        result = Math.max(result, 2 * (n - last.x));

        while(!betweens.isEmpty()) {
            Between temp = betweens.peek();
            if(isValid(temp)) break;
            betweens.poll();
        }

        int maxDist = betweens.isEmpty() ? 0 : betweens.peek().dist;
        result = Math.max(result, maxDist);

        return result;
    }
}

