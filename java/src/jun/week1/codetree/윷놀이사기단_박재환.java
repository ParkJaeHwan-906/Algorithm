package jun.week1.codetree;

import java.util.*;
import java.io.*;

/**
 * [풀이 시간]
 * 01:07:13
 * AI 사용 여부 O
 * => next 매핑에서 실수가 있었는데 찾지 못했음
 */
public class 윷놀이사기단_박재환 {
    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        init(br);
        br.close();
    }
    static int[] turn;
    static int[] next;
    static int[] score;
    static int[] horse;
    static int answer;
    static void init(BufferedReader br) throws IOException {
        turn = new int[10];
        StringTokenizer st = new StringTokenizer(br.readLine().trim());
        for(int i = 0; i < 10; i++) turn[i] = Integer.parseInt(st.nextToken());

        next = new int[33];
        score = new int[33];
        horse = new int[4];

        answer = 0;
        set();
        allCombi(0, 0);
        System.out.println(answer);
    }
    /**
     * 말판에서 윷놀이를 진행
     * -> 원하는 때에 어떤 이동 칸 수가 나올지 예상할 수 있다.
     * -> 윷을 던질 수 있는 횟수는 10회 밖에 없다.
     * => 주어진 이동 횟구에 나갈 말의 종류를 잘 조합하여 얻을 수 있는 점수의 최댓값
     *
     * 1. 시작 칸에 말 4개가 주어진다.
     * 2. 말은 게임판에 그려진 화살표를 따라서만 이동가능하다.
     *      - 파란칸이 아닌 곳에서 이동하면 검정 화살표
     *      - 파란칸에서 이동하면 빨간 화살표
     *      - 도착 칸에 도착하면 바로 이동 종료
     * 3. 도착칸에 도착하지 않은 말들만 골라 이동
     * 4. 시작 도착 칸을 제외하면 말들은 겹쳐있을 수 없음
     *      - 도달하게 되는 위치에 다른 말이 이미 있다면 불가능한 이동
     * 5. 말이 한 번 이동할 때마다 칸에 있는 수가 점수에 추가
     */
    static void set() {
        for(int i = 1; i <= 20; i++) score[i] = i * 2;      // 가장자리 점수판
        // 가로지르는 경로
        score[21] = 13;
        score[22] = 16;
        score[23] = 19;

        score[24] = 22;
        score[25] = 24;

        score[26] = 28;
        score[27] = 27;
        score[28] = 26;

        score[29] = 25;
        score[30] = 30;
        score[31] = 35;

        // next
        for(int i = 0; i < 20; i++) next[i] = i + 1;
        next[20] = 32;      // 도착
        next[32] = 32;

        next[21] = 22;
        next[22] = 23;
        next[23] = 29;

        next[24] = 25;
        next[25] = 29;

        next[26] = 27;
        next[27] = 28;
        next[28] = 29;

        next[29] = 30;
        next[30] = 31;
        next[31] = 20;
    }

    static void allCombi(int turnId, int totalScore) {
        if(turnId == 10) {
            answer = Math.max(answer, totalScore);
            return;
        }

        int move = turn[turnId];

        for(int i = 0; i < 4; i++) {
            int cur = horse[i];

            if(cur == 32) continue;     // 도착 위치에 있는 말은 움직이지 않음

            int nextLoc = moveHorse(cur, move);

            if(nextLoc != 32 && cantMove(nextLoc)) continue;

            horse[i] = nextLoc;
            allCombi(turnId + 1, totalScore + score[nextLoc]);
            horse[i] = cur;
        }
    }

    static int moveHorse(int cur, int move) {
        /**
         * 파란 위치
         * - 5 ( 10 )
         * - 10 ( 20 )
         * - 15 ( 30 )
         */

        if(cur == 5) {
            cur = 21;
            move--;
        }
        else if(cur == 10) {
            cur = 24;
            move--;
        }
        else if(cur == 15) {
            cur = 26;
            move--;
        }

        while(move > 0 && cur != 32) {
            cur = next[cur];
            move--;
        }
        return cur;
    }

    static boolean cantMove(int cur) {
        for(int i : horse) {
            if(i == cur) return true;
        }
        return false;
    }
}
