package aug.week2.codetree;

import java.util.*;
import java.io.*;

public class 병원거리최소화하기_박재환 {
    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        init(br);
        br.close();
    }

    static int n, m;
    static int[][] board;
    static void init(BufferedReader br) throws IOException {
        StringTokenizer st;

        st = new StringTokenizer(br.readLine().trim());
        n = Integer.parseInt(st.nextToken());
        m = Integer.parseInt(st.nextToken());

        board = new int[n][n];
        for(int x = 0; x < n; x++) {
            st = new StringTokenizer(br.readLine().trim());
            for(int y = 0; y < n; y++) board[x][y] = Integer.parseInt(st.nextToken());
        }

        System.out.print(solution());
    }

    static class Loc {
        int x, y;
        Loc(int x, int y) {
            this.x = x;
            this.y = y;
        }
    }

    static int minDist;
    static List<Loc> persons;
    static List<Loc> hospitals;
    static int solution() {
        minDist = Integer.MAX_VALUE;
        findObjects();
        makeHospitalCombi(0, 0, new Loc[m]);
        return minDist;
    }

    static void findObjects() {
        persons = new ArrayList<>();
        hospitals = new ArrayList<>();
        for(int x = 0; x < n; x++) {
            for(int y = 0; y < n; y++) {
                if(board[x][y] == 1) persons.add(new Loc(x, y));
                else if(board[x][y] == 2) hospitals.add(new Loc(x, y));
            }
        }
    }

    static void makeHospitalCombi(int id, int arrId, Loc[] locs) {
        if(arrId == m) {
            minDist = Math.min(getTotalDist(locs), minDist);
            return;
        }

        if(id >= hospitals.size()) return;

        locs[arrId] = hospitals.get(id);
        makeHospitalCombi(id + 1, arrId + 1, locs);
        makeHospitalCombi(id + 1, arrId, locs);
    }

    static int getTotalDist(Loc[] locs) {
        int totalDist = 0;
        for(Loc p : persons) {
            int min = Integer.MAX_VALUE;
            for(Loc h : locs) {
                min = Math.min(min, getDist(p, h));
            }
            totalDist += min;
        }
        return totalDist;
    }

    static int getDist(Loc loc1, Loc loc2) {
        return Math.abs(loc1.x - loc2.x) + Math.abs(loc1.y - loc2.y);
    }
}
