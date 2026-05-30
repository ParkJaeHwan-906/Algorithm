package may.week4.codetree;

import java.util.*;
import java.io.*;

/**
 * [풀이 시간]
 * 00:47:43
 * AI 사용 여부 X
 */
public class 원자충돌_박재환 {
    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        init(br);
        br.close();
    }

    final static int[] dx = {-1, -1, 0, 1, 1, 1, 0, -1};
    final static int[] dy = {0, 1, 1, 1, 0, -1, -1, -1};

    static class Atom {
        int x, y;       // 위치
        int m;          // 질량
        int s;          // 속력
        int d;          // 방향

        Atom(int x, int y, int m, int s, int d) {
            this.x = x;
            this.y = y;
            this.m = m;
            this.s = s;
            this.d = d;
        }
    }

    static int n, m, k;
    static Queue<Atom> atoms;
    static void init(BufferedReader br) throws IOException {
        StringTokenizer st;
        st = new StringTokenizer(br.readLine().trim());
        n = Integer.parseInt(st.nextToken());       // 격자 크기
        m = Integer.parseInt(st.nextToken());       // 원자 개수
        k = Integer.parseInt(st.nextToken());       // 실험 시간

        atoms = new ArrayDeque<>();
        for(int i = 0; i < m; i++) {
            st = new StringTokenizer(br.readLine().trim());
            int x = Integer.parseInt(st.nextToken()) - 1;
            int y = Integer.parseInt(st.nextToken()) - 1;
            int m = Integer.parseInt(st.nextToken());
            int s = Integer.parseInt(st.nextToken());
            int d = Integer.parseInt(st.nextToken());

            Atom atom = new Atom(x, y, m, s, d);
            atoms.offer(atom);
        }

        System.out.println(solution());
    }
    /**
     * n x n 격자
     * m 개의 원자
     *      - 질량
     *      - 방향
     *      - 속력
     *      - 초기 위치
     * 격자의 모든 행 열은 각각의 끝과 연결되어 있음
     *
     * [원자 이동]
     * 1초가 지날때 마다 자신의 방향으로 속력만큼 이동한다.
     * 이동이 모두 끝난 뒤, 하나의 칸에 2개 이상의 원자가 있는 경우 합성이 일어난다.
     *      - 각 질량과 속력을 모두 합한 하나의 원자로 합쳐진다.
     *      - 이후 합쳐진 원자는 4개의 원자로 나뉜다.
     *      - 나누어진 원자들은 모두 해당 칸에 위치한다.
     *              - 질향은 합쳐진 원자 질량에 5를 나눈다.
     *              - 속력은 합쳐진 원자 속력에 합쳐진 원자의 개수를 나눈다.
     *              - 방향은 합쳐지는 원자의 방향이 모두 상하좌우, 대각선이면 각각 상 하 좌우 / 아니라면 각각 대각선
     *              - 질량이 0인 원소는 소멸된다.
     * 이동 중 만나는 경우는 합성으로 고려하지 않는다.
     */
    static Map<Integer, List<Atom>> afterMoves;
    static int solution() {
        afterMoves = new HashMap<>();

        while(k-- > 0) {
            // 1. 원자 이동
            moveAtoms();
            // 2. 원자 합성
            combine();
        }
        return totalM();
    }

    static void moveAtoms() {
        afterMoves.clear();

        while(!atoms.isEmpty()) {
            Atom atom = atoms.poll();
            moveAtom(atom);
//            System.out.printf("(%d, %d)\n", atom.x, atom.y);
            int key = atom.x * (n + 7) + atom.y;
            afterMoves.computeIfAbsent(key, k -> new ArrayList<>()).add(atom);
        }
    }

    static void moveAtom(Atom atom) {
        // 끝과 끝이 이어져 있음
        int nx = ((atom.x + (dx[atom.d] * atom.s)) % n + n) % n;
        int ny = ((atom.y + (dy[atom.d] * atom.s)) % n + n) % n;

        atom.x = nx;
        atom.y = ny;
    }

    static void combine() {
        for(Map.Entry<Integer, List<Atom>> entry : afterMoves.entrySet()) {
            if(entry.getValue().size() < 2) {
                addNextAtoms(entry.getValue());
                continue;
            }

            // 2개 이상의 원자가 함께 존재하는 경우
            List<Atom> newAtomList = newAtomList(entry.getValue());
            addNextAtoms(newAtomList);
        }
    }

    static List<Atom> newAtomList(List<Atom> list) {
        List<Atom> newList = new ArrayList<>();

        int x = list.get(0).x;
        int y = list.get(0).y;
        int totalM = 0;     // 총 질량
        int totalS = 0;     // 총 속력
        boolean straight = false;       // 상하좌우
        boolean cross = false;          // 대각선
        for(Atom atom : list) {
            totalM += atom.m;
            totalS += atom.s;

            if(atom.d % 2 == 0) straight = true;
            else cross = true;
        }

        int newM = totalM / 5;
        int newS = totalS / list.size();

        // 질량이 0
        if(newM == 0) return newList;

        // 모두 상화좌우 또는 대각선
        if((straight && !cross) || (!straight && cross)) {
            newList.add(new Atom(x, y, newM, newS, 0));
            newList.add(new Atom(x, y, newM, newS, 2));
            newList.add(new Atom(x, y, newM, newS, 4));
            newList.add(new Atom(x, y, newM, newS, 6));
        }
        // 섞인 경우
        else {
            newList.add(new Atom(x, y, newM, newS, 1));
            newList.add(new Atom(x, y, newM, newS, 3));
            newList.add(new Atom(x, y, newM, newS, 5));
            newList.add(new Atom(x, y, newM, newS, 7));
        }
        return newList;
    }

    static void addNextAtoms(List<Atom> list) {
        for(Atom atom : list) atoms.offer(atom);
    }

    static int totalM() {
        int totalM = 0;
        while(!atoms.isEmpty()) totalM += atoms.poll().m;
        return totalM;
    }
}

/*
4 4 1
1 2 2 2 4
2 4 5 3 6
4 2 1 1 0
4 3 3 2 5
 */
