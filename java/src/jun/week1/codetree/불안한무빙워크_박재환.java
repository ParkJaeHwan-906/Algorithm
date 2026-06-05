package jun.week1.codetree;

import java.util.*;
import java.io.*;

/**
 * [풀이 시간]
 * 00:19:45
 * AI 사용 여부 X
 */
public class 불안한무빙워크_박재환 {
    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        init(br);
        br.close();
    }
    static int n, k;
    static int[] movingWalk;
    static void init(BufferedReader br) throws IOException {
        StringTokenizer st;

        st = new StringTokenizer(br.readLine().trim());
        n = Integer.parseInt(st.nextToken());
        k = Integer.parseInt(st.nextToken());

        movingWalk = new int[2 * n];
        st = new StringTokenizer(br.readLine().trim());
        for(int i = 0; i < 2 * n; i++) movingWalk[i] = Integer.parseInt(st.nextToken());

        System.out.println(solution());
    }

    static int kCount;
    static boolean[] onboard;
    static int solution() {
        kCount = 0;
        onboard = new boolean[2 * n];
        int turn = 0;
        while(kCount < k) {
            ++turn;
            // 1. 무빙워크 회전
            rotateMovingWalk();
            removeArrivedPeople();
            // 2. 무빙워크 내 이동
            moveOnMovingWalk();
            removeArrivedPeople();
            // 3. 인원 추가
            addPeople();
            removeArrivedPeople();
        }
        return turn;
    }

    static void rotateMovingWalk() {
        int[] tempMovingWalk = new int[2 * n];
        for(int i = 1; i < 2 * n; i++) tempMovingWalk[i] = movingWalk[i - 1];
        tempMovingWalk[0] = movingWalk[2 * n - 1];
        movingWalk = tempMovingWalk;

        boolean[] tempOnboard = new boolean[2 * n];
        for(int i = 1; i < 2 * n; i++) tempOnboard[i] = onboard[i - 1];
        tempOnboard[0] = onboard[2 * n - 1];
        onboard = tempOnboard;
    }

    static void moveOnMovingWalk() {
        for(int i = n - 2; i >= 0; i--) {
            if(!onboard[i]) continue;

            // 다음칸으로 이동 가능한지 확인
            if(onboard[i + 1]) continue;            // 다음 칸에 이미 사람이 있는 경우
            if(movingWalk[i + 1] == 0) continue;    // 안정성이 0인 경우

            // 이동 가능
            onboard[i] = false;
            onboard[i + 1] = true;
            if(--movingWalk[i + 1] == 0) kCount++;
        }
    }

    static void addPeople() {
        if(onboard[0] || movingWalk[0] == 0) return;
        onboard[0] = true;
        if(--movingWalk[0] == 0) kCount++;
    }

    static void removeArrivedPeople() {
        if(onboard[n - 1]) onboard[n - 1] = false;
    }
}
