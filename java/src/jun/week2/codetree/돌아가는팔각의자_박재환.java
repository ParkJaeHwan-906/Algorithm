package jun.week2.codetree;

import java.util.*;
import java.io.*;

/**
 * [풀이 시간]
 * 00:46:01
 * AI 사용 여부 X
 */
public class 돌아가는팔각의자_박재환 {
    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        init(br);
        br.close();
    }

    static int[][] chairs;
    static void init(BufferedReader br) throws IOException {
        StringTokenizer st;

        chairs = new int[4][8];
        for(int i = 0; i < 4; i++) {
            String line = br.readLine().trim();
            // 0 : S, 1 : N
            for(int j = 0; j < 8; j++) chairs[i][j] = line.charAt(j) - '0';
        }

        int k = Integer.parseInt(br.readLine().trim());
        while(k-- > 0) {
            st = new StringTokenizer(br.readLine().trim());
            int n = Integer.parseInt(st.nextToken()) - 1;       // 0 - based 보정
            int d = Integer.parseInt(st.nextToken());           // 1 : 시계, 2 : 반시계

            rotateCommand(n, d);
        }

//        System.out.println(Arrays.toString(chairs[0]));
//        System.out.println(Arrays.toString(chairs[1]));
//        System.out.println(Arrays.toString(chairs[2]));
//        System.out.println(Arrays.toString(chairs[3]));

        System.out.println(getScore());
    }

    static void rotateCommand(int cid, int d) {
        // 현재 회전시키고지 하는 의자의 좌우를 모두 확인한다.
        // 좌 : 6, 우 : 2
//        System.out.printf("[회전명령] cid : %d, d : %d\n", cid, d);
        // 오른쪽 확인
        if(cid < 3 && chairs[cid][2] != chairs[cid + 1][6]) chainRotateRight(cid + 1, -1 * d);
        // 왼쪽 확인
        if(cid > 0 && chairs[cid][6] != chairs[cid - 1][2]) chainRotateLeft(cid - 1, -1 * d);

        rotate(cid, d);
    }

    static void chainRotateRight(int cid, int d) {
//        System.out.printf("[연쇄 회전 - 오른쪽] cid : %d\n", cid);
        if(cid == 3) {
            rotate(cid, d);
            return;
        }

        // 현 위치의 의자를 회전시키기 전에, 오른쪽에 있는 의자와 비교한다.
        if(chairs[cid][2] != chairs[cid + 1][6]) chainRotateRight(cid + 1, -1 * d);
        rotate(cid, d);
    }

    static void chainRotateLeft(int cid, int d) {
//        System.out.printf("[연쇄 회전 - 왼쪽] cid : %d\n", cid);
        if(cid == 0) {
            rotate(cid, d);
            return;
        }

        // 현 위치의 의자를 회전시키기 전에, 왼쪽에 있는 의자와 비교한다.
        if(chairs[cid][6] != chairs[cid - 1][2]) chainRotateLeft(cid - 1, -1 * d);
        rotate(cid, d);
    }

    static void rotate(int cid, int d) {
//        System.out.printf("[회전] cid : %d, d : %d\n", cid, d);
        if(d == 1) rotateClockWise(cid);
        else rotateCounterClockWise(cid);
    }

    static void rotateClockWise(int cid) {
//        System.out.println("[시계방향회전]");
//        System.out.printf("[prev] %s\n", Arrays.toString(chairs[cid]));
        // 한칸씩 오른쪽으로 슬라이드
        int[] temp = new int[8];

        for(int i = 1; i < 8; i++) {
            temp[i] = chairs[cid][i - 1];
        }
        temp[0] = chairs[cid][7];
        chairs[cid] = temp;
//        System.out.printf("[next] %s\n", Arrays.toString(chairs[cid]));
    }

    static void rotateCounterClockWise(int cid) {
//        System.out.println("[반시계방향회전]");
//        System.out.printf("[prev] %s\n", Arrays.toString(chairs[cid]));
        // 한칸씩 왼쪽으로 슬라이드
        int[] temp = new int[8];

        for(int i = 0; i < 7; i++) {
            temp[i] = chairs[cid][i + 1];
        }
        temp[7] = chairs[cid][0];
        chairs[cid] = temp;
//        System.out.printf("[next] %s\n", Arrays.toString(chairs[cid]));
    }

    static int getScore() {
        return 1 * chairs[0][0] + 2 * chairs[1][0] + 4 * chairs[2][0] + 8 * chairs[3][0];
    }
}
