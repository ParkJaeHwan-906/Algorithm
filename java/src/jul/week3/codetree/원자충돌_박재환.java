package jul.week3.codetree;

import java.util.*;
import java.io.*;

public class 원자충돌_박재환 {
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
        public boolean equals(Object o) {
            if(this == o) return true;
            if(!(o instanceof Loc)) return false;
            Loc other = (Loc)o;
            return this.x == other.x && this.y == other.y;
        }
        public int hashCode() {
            return Objects.hash(this.x, this.y);
        }
    }

    static class Atom extends Loc {
        /**
         *  원자(질량, 방향, 속력, 초기 위치)
         *  초기 위치 (1 - based)
         *  방향(상하좌우대각선)
         */
        int m;      // 질량
        int s;      // 속도
        int d;      // 방향

        Atom(int x, int y, int m, int s, int d) {
            super(x, y);
            this.m = m;
            this.s = s;
            this.d = d;
        }
    }

    static int n, m, k;
    static Map<Loc, List<Atom>> atoms;
    static void init(BufferedReader br) throws IOException {
        StringTokenizer st;

        st = new StringTokenizer(br.readLine().trim());
        n = Integer.parseInt(st.nextToken());       // 격자 크기
        m = Integer.parseInt(st.nextToken());       // 원자 개수
        k = Integer.parseInt(st.nextToken());       // 턴 수

        // 초기에 주어지는 원자의 위치는 겹치지 않는다.
        atoms = new HashMap<>();
        for(int i = 0; i < m; i++) {
            st = new StringTokenizer(br.readLine().trim());
            int x = Integer.parseInt(st.nextToken()) - 1;
            int y = Integer.parseInt(st.nextToken()) - 1;
            int m = Integer.parseInt(st.nextToken());
            int s = Integer.parseInt(st.nextToken());
            int d = Integer.parseInt(st.nextToken());
            Atom atom = new Atom(x, y, m, s, d);
            Loc loc = new Loc(x, y);
            atoms.computeIfAbsent(loc, k -> new ArrayList<>()).add(atom);
        }

        System.out.println(solution());
    }

    static final int[] dx = {-1, -1, 0, 1, 1, 1, 0, -1};
    static final int[] dy = {0, 1, 1, 1, 0, -1, -1, -1};

    static long solution() {
        while(k-- > 0) {        // k 초 동안 시뮬레이션 진행
            // 1. 원자 이동
            Map<Loc, List<Atom>> temp = new HashMap<>();
            moveAtoms(temp);
            // 2. 충돌한 원자 확인
            collisionAtoms(temp);
            atoms = temp;
        }
//        printState();
        return getTotalM();
    }

    static void moveAtoms(Map<Loc, List<Atom>> temp) {
        // 1. 모든 원자는 1초가 지날 때마다 자신의 방향으로 자신의 속력만큼 이동
        // atoms 에 존재하는 원자를 순차적으로 이동
        for(List<Atom> atomList : atoms.values()) {
            for(Atom atom : atomList) {
                moveAtom(atom);
                Loc newLoc = new Loc(atom.x, atom.y);
                temp.computeIfAbsent(newLoc, k -> new ArrayList<>()).add(atom);
            }
        }
    }

    static void moveAtom(Atom atom) {
        int x = atom.x;
        int y = atom.y;

        // 격자의 모든 행, 열을 각 끝이 연결되어 있음 -> 사이클이 존재
        int nx = (x + dx[atom.d] * (atom.s % n) + n) % n;
        int ny = (y + dy[atom.d] * (atom.s % n) + n) % n;

        atom.x = nx;
        atom.y = ny;
    }

    static void collisionAtoms(Map<Loc, List<Atom>> temp) {
        for(Loc loc : temp.keySet()) {
            if(temp.get(loc).size() < 2) continue;
            // 2. 이동이 끝난 뒤에 하나의 칸에 2개 이상의 원자가 있으면 합성이 일어남
            List<Atom> atomList = temp.get(loc);
            collisionAtom(loc, atomList);
        }
    }

    static void collisionAtom(Loc loc, List<Atom> atomList) {
        int totalM = 0;
        int totalS = 0;

        boolean even = false;       // 상하좌우
        boolean odd = false;        // 대각선

        for(Atom atom : atomList) {
            // 2-1. 각 질량과 속력을 모두 합한다.
            totalM += atom.m;
            totalS += atom.s;

            if(atom.d % 2 == 0) even = true;
            else odd = true;
        }
        // 2-3-1. 질량은 합쳐진 원자의 질량 / 5
        // 2-3-2. 속력은 합쳐진 원자의 속력 / 합쳐진 원자의 개수
        int newM = totalM / 5;
        int newS = totalS / atomList.size();
        atomList.clear();

        // 2-4. 질량이 0인 원소는 소멸
        if(newM == 0) return;

        // 2-3-3. 방향은 (상하좌우 / 대각선) 의 경우 각 상하좌우 방향을 갖고, 혼합시 대각선 내 방향의 값을 가짐
        if(even && odd) {
            for(int i = 1; i < 8; i+=2) {
                Atom atom = new Atom(loc.x, loc.y, newM, newS, i);
                atomList.add(atom);
            }
        } else {
            for(int i = 0; i < 8; i+=2) {
                Atom atom = new Atom(loc.x, loc.y, newM, newS, i);
                atomList.add(atom);
            }
        }
    }

    static long getTotalM() {
        long totalM = 0;
        for(List<Atom> atomList : atoms.values()) {
            for(Atom atom : atomList) totalM += atom.m;
        }
        return totalM;
    }

    static void printState() {
        for(Loc loc : atoms.keySet()) {
            System.out.printf("Loc : {%d, %d}\n", loc.x, loc.y);
            for(Atom atom : atoms.get(loc)) {
                System.out.printf("\tAtom : {%d, %d, %d}\n", atom.m, atom.s, atom.d);
            }
        }
    }
}
