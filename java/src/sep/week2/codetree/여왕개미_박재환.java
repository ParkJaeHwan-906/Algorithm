package sep.week2.codetree;

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
    static final int QUERY = 400;

    static final int INF = 1_000_000_007;

    static class Home {
        int x;
        boolean deleted;
        Home(int x) {
            this.x = x;
            this.deleted = false;
        }
        Home(int x, boolean deleted) {
            this.x = x;
            this.deleted = deleted;
        }
    }

    static final Home QUEEN = new Home(0, true);

    static List<Home> homes;
    static void init(BufferedReader br) throws IOException {
        StringTokenizer st;
        StringBuilder sb = new StringBuilder();
        int q = Integer.parseInt(br.readLine().trim());
        while(q-- > 0) {
            st = new StringTokenizer(br.readLine().trim());
            int type = Integer.parseInt(st.nextToken());
            if(type == SET) {
                set(st);
            }
            else if(type == ADD) {
                add(st);
            }
            else if(type == DEL) {
                del(st);
            }
            else if(type == QUERY) {
                int result = query(st);
                sb.append(result).append('\n');
            }
        }
        System.out.println(sb);
    }

    static void set(StringTokenizer st) {
        int n = Integer.parseInt(st.nextToken());
        homes = new ArrayList<>();
        homes.add(QUEEN);
        for(int i = 1; i <= n; i++) {
            int x = Integer.parseInt(st.nextToken());
            Home home = new Home(x);
            homes.add(home);
        }
    }

    static void add(StringTokenizer st) {
        int x = Integer.parseInt(st.nextToken());
        Home home = new Home(x);
        homes.add(home);
    }

    static void del(StringTokenizer st) {
        int id = Integer.parseInt(st.nextToken());
        Home home = homes.get(id);
        home.deleted = true;
    }

    static int query(StringTokenizer st) {
        int limit = Integer.parseInt(st.nextToken());
        int l = 0, r = homes.get(homes.size() - 1).x;
        int result = INF;
        while(l <= r) {
            int mid = l + (r - l) / 2;
            if(isPossible(mid, limit)) {        // 탐색 시간을 더 줄일 수 있음
                result = Math.min(result, mid);
                r = mid - 1;
            } else {
                l = mid + 1;
            }
        }
        return result;
    }

    static boolean isPossible(int mid, int limit) {
        int lastLoc = -INF;
        int needs = 0;
        for(Home home : homes) {
            if(home.deleted) {
                continue;
            }

            if(home.x - lastLoc > mid) {
                lastLoc = home.x;
                needs++;
            }
        }
        return needs <= limit;
    }
}
