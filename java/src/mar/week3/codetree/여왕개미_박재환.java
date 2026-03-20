package mar.week3.codetree;

import java.util.*;
import java.io.*;

public class 여왕개미_박재환 {
    static BufferedReader br;
    static StringBuilder sb;
    public static void main(String[] args) throws IOException {
        br = new BufferedReader(new InputStreamReader(System.in));
        sb = new StringBuilder();
        init();
        br.close();
        System.out.println(sb);
    }
    /**
     * 좌표 범위 0 ~ 10**9
     */
    static final int SET = 100;
    static final int ADD = 200;
    static final int DEL = 300;
    static final int QUERY = 400;

    static StringTokenizer st;
    static void init() throws IOException {
        int q = Integer.parseInt(br.readLine().trim());
        while(q-- > 0) {
            st = new StringTokenizer(br.readLine().trim());
            int cmd = Integer.parseInt(st.nextToken());

            if(cmd == SET) { set(); }
            else if(cmd == ADD) { add(); }
            else if(cmd == DEL) { del(); }
            else if(cmd == QUERY) { sb.append(query()).append('\n'); }
        }
    }
    /**
     * [마을 건설]
     * 여왕 개미 집을 x = 0
     * N 개의 집 건설 ( 1 ~ N )
     * i 번째 집 위치는 x[i]
     */
    static class Home {
        int x;
        boolean del;

        Home(int x) {
            this.x = x;
            this.del = false;
        }
    }
    static int n;
    static List<Home> homes;
    static void set() {
        homes = new ArrayList<>();

        n = Integer.parseInt(st.nextToken());
        // 1. 여왕 개미
        homes.add(new Home(0));
        for(int i=0; i<n; i++) {
            int x = Integer.parseInt(st.nextToken());
            homes.add(new Home(x));
        }
    }
    /**
     * [개미집 건설] - 최대 10000번
     * 새로운 개미집 건설
     * x = p 인 위치에 집 건설
     * -> 이제까지 건설된 모든 집의 좌표보다 큰 값으로 주어짐
     */
    static void add() {
        int x = Integer.parseInt(st.nextToken());
        homes.add(new Home(x));
    }
    /**
     * [개미집 철거] - 최대 10000번
     * q 번 개미집을 철거한다.
     * 유효하지 않은 명령은 들어오지 않는다.
     */
    static void del() {
        int id = Integer.parseInt(st.nextToken());
        Home home = homes.get(id);
        home.del = true;
    }
    /**
     * [개미집 정찰] - 최대 100 번
     * r 마리의 개미가 정찰을 간다.
     * 서로 다른 개미집을 선택한다.
     * 1초에 1만큼 이동한다.
     * 시간이 최소가 되도록한다.
     */
    static int query() {
        int limit = Integer.parseInt(st.nextToken());
        int l = 0, r = homes.get(homes.size()-1).x;
        int min = Integer.MAX_VALUE;
        while(l <= r) {
            int mid = l + (r - l)/2;
            if(isPossible(mid, limit)) {
                min = Math.min(min, mid);
                r = mid - 1;
            } else {
                l = mid + 1;
            }
        }
        return min;
    }
    static boolean isPossible(int time, int limit) {
        int lastLoc = -Integer.MAX_VALUE;
        int ant = 0;
        for(int i=1; i<homes.size(); i++) {
            Home home = homes.get(i);
            if(home.del) continue;
            int dist = Math.abs(lastLoc - home.x);
            if(dist > time) {
                ant++;
                lastLoc = home.x;
            }
        }
        return ant <= limit;
    }
}
