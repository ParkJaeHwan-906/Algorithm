package jul.week3.codetree;

import java.util.*;
import java.io.*;

public class 여왕개미_박재환 {
    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        init(br);
        br.close();
    }

    static final int SET = 100;
    static final int ADD = 200;
    static final int DEL = 300;
    static final int QRY = 400;

    static class Home {
        int x;
        boolean deleted;
        Home(int x) {
            this.x = x;
            this.deleted = false;
        }
    }

    static List<Home> homes;
    static void init(BufferedReader br) throws IOException {
        StringTokenizer st;
        StringBuilder sb = new StringBuilder();
        int q = Integer.parseInt(br.readLine().trim());
        while(q-- > 0) {
            st = new StringTokenizer(br.readLine().trim());
            int type = Integer.parseInt(st.nextToken().trim());
            if(type == SET) { set(st); }
            else if(type == ADD) { add(st); }
            else if(type == DEL) { del(st); }
            else if(type == QRY) {
                int result = qry(st);
                sb.append(result).append("\n");
            }
        }
        System.out.println(sb);
    }

    static final Home QUEEN = new Home(0);

    static void set(StringTokenizer st) {
        /**
         * N개의 개미집을 건설합니다.
         * - 여왕개미 집은 x = 0 위치에 건설합니다.
         */
        int n = Integer.parseInt(st.nextToken());
        // 1. 여왕개미
        homes = new ArrayList<>();
        homes.add(QUEEN);
        // 2. 집 추가
        for(int i = 0; i < n; i++) {
            add(st);
        }
    }

    static void add(StringTokenizer st) {
        homes.add(new Home(Integer.parseInt(st.nextToken())));
    }

    static void del(StringTokenizer st) {
        int hid = Integer.parseInt(st.nextToken());
        homes.get(hid).deleted = true;
    }

    static int qry(StringTokenizer st) {
        int r = Integer.parseInt(st.nextToken());
        int left = 0, right = homes.get(homes.size() - 1).x;
        while(left < right) {
            /**
             * BinarySearch를 사용해서 탐색 시간을 정한다.
             */
            int mid = left + (right - left) / 2;
            if(isPossible(mid, r)) {
                right = mid;
            } else {
                left = mid + 1;
            }
        }
        return right;
    }

    static boolean isPossible(int t, int r) {
        int startId = getStartId();
        if(startId == -1) return true;      //  탐색할 집이 없다면 무조건 참

        int needs = 1;
        int lastId = startId;
        for(int i = startId + 1; i < homes.size(); i++) {
            if(homes.get(i).deleted) continue;

            int dist = homes.get(i).x - homes.get(lastId).x;
            if(dist > t) {
                if(++needs > r) return false;
                lastId = i;
            }
        }
        return needs <= r;
    }

    static int getStartId() {
        for(int i = 1; i < homes.size(); i++) {
            if(!homes.get(i).deleted) return i;
        }
        return -1;
    }
}
