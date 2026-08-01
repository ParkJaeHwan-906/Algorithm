package jun.week1.codetree;

import java.io.*;
import java.util.*;

public class 불안한무빙워크_박서희 {
    static int n, k;

    static LinkedList<Integer> safety = new LinkedList<>();
    static LinkedList<Boolean> hasPerson = new LinkedList<>();

    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer st = new StringTokenizer(br.readLine());

        n = Integer.parseInt(st.nextToken());
        k = Integer.parseInt(st.nextToken());


        st = new StringTokenizer(br.readLine());
        for (int i = 0; i < 2 * n; i++) {
            safety.add(Integer.parseInt(st.nextToken()));
            hasPerson.add(false);
        }

        int turn = 0;

        while (true) {
            turn++;

            rotate(); // 회전하기
            movePeople(); // 기존 사람 이동
            moveIn(); // 새 사람 타기
            if (checkZeroCount() >= k) { // 종료
                break;
            }

        }
        System.out.print(turn);
    }

    static void rotate() {
        safety.addFirst(safety.removeLast());
        hasPerson.addFirst(hasPerson.removeLast());
        hasPerson.set(n - 1, false); // 회전해서 내릴 사람 하차
    }


    static void movePeople() {
        for (int i = n - 2; i >= 0; i--) {
            if (hasPerson.get(i)) {
                // 다음 칸에 사람이 있는지 + 다음 칸의 안전성 조사
                if (!hasPerson.get(i + 1) && safety.get(i + 1) > 0) {
                    hasPerson.set(i, false);
                    hasPerson.set(i + 1, true);
                    safety.set(i + 1, safety.get(i + 1) - 1);
                }
            }
        }
        hasPerson.set(n - 1, false); // 회전해서 내릴 사람 하차
    }


    static void moveIn() {
        if (!hasPerson.get(0) && safety.get(0) > 0) {
            hasPerson.set(0, true);
            safety.set(0, safety.get(0) - 1);
        }
    }

    static int checkZeroCount() {
        int cnt = 0;
        for (int s : safety)
            if (s == 0) cnt++;
        return cnt;
    }
}