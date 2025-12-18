package dec_3.boj;

import java.util.*;
import java.io.*;

public class 컨베이어벨트위의로봇_박재환 {
    static BufferedReader br;
    public static void main(String[] args) throws IOException {
        br = new BufferedReader(new InputStreamReader(System.in));
        init();
        br.close();
    }

    /**
     * 길이가 N인 컨베이어벨트를, 길이가 2N인 벨트가 위아래로 감싸며 돌고 있다.
     * 각 칸은 1 ~ 2N의 번호가 있다.
     *
     * [올리는 위치 : 1, 내리는 위치 : N]
     * i번 칸의 내구도는 A[i]이다.
     * 로봇은 올리는 위치에만 올릴 수 있고, 언제든 로봇이 내리는 위치에 도달하면 즉시 내린다.
     *
     * 로봇은 컨베이어벨트 위에서 움직일 수 있다.
     * 로봇을 올리는 위치에 올리거나, 어떤 칸으로 이동하면 그 칸의 내구도는 즉시 1만큼 감소한다.
     *
     * 컨베이어벨트를 이용해 로봇들을 건너편으로 옮기려고 한다.
     * 1. 벨트가 각 칸 위에 있는 로봇과 함께 한 칸 회전한다.
     * 2. 가장 먼저 벨트에 올라간 로봇부터, 벨트가 회전하는 방향으로 한 칸 이동할 수 있다면 이동한다.
     *      a. 로봇이 이동하기 위해서는, 이동하려는 칸에 로봇이 없어야하고, 내구도가 1이상 남아있어야한다.
     * 3. 올리는 위치에 있는 칸의 내구도가 0이 아니면 올리는 위치에 로봇을 올린다.
     * 4. 내구도가 0인 칸의 개수가 K개 이상이라면 과정을 종료한다. -> 아니면 반복한다.
     */
    static StringTokenizer st;
    static int n, k;
    static int[] belt;
    static boolean[] robots;
    static int expired;
    static void init() throws IOException {
        expired = 0;
        st = new StringTokenizer(br.readLine().trim());
        n = Integer.parseInt(st.nextToken());
        belt = new int[2*n];
        robots = new boolean[2*n];
        k = Integer.parseInt(st.nextToken());

        st = new StringTokenizer(br.readLine().trim());
        for(int i=0; i<belt.length; i++) {
            belt[i] = Integer.parseInt(st.nextToken());
            if(belt[i] == 0) expired++;
        }

        System.out.println(solution());
    }

    static int solution() {
        int depth = 0;

        while(expired < k) {
            depth++;
            rotateBelt();
//            printStatus();
            moveRobots();
//            printStatus();
            putRobot();
//            printStatus();
        }

        return depth;
    }

    static void rotateBelt() {
        // 1. 벨트의 내구도 회전
        int tmp = belt[2*n-1];  // 벨트의 가장 마지막 위치 값
        for(int i=2*n-1; i > 0; i--) {
            belt[i] = belt[i-1];
        }
        belt[0] = tmp;
        // 2. 로봇의 위치 회전
        boolean tmpR = robots[2*n-1];
        for(int i=2*n-1; i > 0; i--) {
            robots[i] = robots[i-1];
        }
        robots[0] = tmpR;

        // 3. 도착한 로봇 제거
        removeRobot();
    }

    static void moveRobots() {
        // 가장 먼저 벨트에 올라간 로봇부터 움직인다.
        // 이거 순서가 중요한가? -> 뒤에서부터 적용하면 괜찮을 듯
        for(int i=n-2; i > -1; i--) {
            if(!robots[i]) continue;

            // 로봇이 있다면 이동 가능한지 확인

            // 1. 이동하려는 위치에 로봇이 있는지 확인
            if(robots[i+1] || belt[i+1] < 1) continue;
            // 이동 가능
            robots[i] = false;
            robots[i+1] = true;
            // 내구도 감소
            belt[i+1]--;
            if(belt[i+1] == 0) expired++;
        }

        removeRobot();
    }

    static void putRobot() {
        if(robots[0] || belt[0] < 1) return;
        robots[0] = true;
        belt[0]--;
        if(belt[0] == 0) expired++;
    }

    static void removeRobot() {
        robots[n-1] = false;
    }

//    static void printStatus() {
//        System.out.println(Arrays.toString(belt));
//        System.out.println(Arrays.toString(robots));
//    }
}


/*
1 2 1 2 1 2

1.
2 1 2 1 2 1

2.
X X X X X X
2 1 2 1 2 1

3.
O X X X X X
1 1 2 1 2 1

1.
X O X X X X
1 1 1 2 1 2

2.
X X O X X X
1 1 0 2 1 2

3.
O X O X X X
0 1 0 2 1 2

4. END
 */