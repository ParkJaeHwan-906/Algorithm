package jun.week5.programmers.기둥과보설치_박서희;

import java.util.*;

/*
  문제풀이 시간: 00:53:05
  AI 사용 여부: O 좌표를 거꾸로 줘서 디버깅 힘들어서 사용.
 */
public class 기둥과보설치_박서희 {
    public static void main(String[] args) {
        int n = 5;
        int[][] build_frame = {{1, 0, 0, 1},
                {1, 1, 1, 1},
                {2, 1, 0, 1},
                {2, 2, 1, 1},
                {5, 0, 0, 1},
                {5, 1, 0, 1},
                {4, 2, 1, 1},
                {3, 2, 1, 1}};

        Solution solution = new Solution();
        int[][] answer = solution.solution(n, build_frame);
        for (int i = 0; i < answer.length; i++) {
            System.out.println(Arrays.toString(answer[i]));
        }
    }
}

class Solution {
    static int n;
    static int[][] bArr;  // 보
    static int[][] gArr;  // 기둥

    public int[][] solution(int n, int[][] build_frame) {
        this.n = n;
        bArr = new int[n + 1][n + 1];
        gArr = new int[n + 1][n + 1];

        for (int i = 0; i < build_frame.length; i++) {
            int x = n - build_frame[i][1], y = build_frame[i][0];
            int a = build_frame[i][2];
            int cmd = build_frame[i][3];

            if (a == 0 && cmd == 0) {           // 기둥 삭제
                gArr[x][y] = 0;
                if (!isPossible()) gArr[x][y] = 1;
            } else if (a == 0 && cmd == 1) {    // 기둥 추가
                if (x == 0) continue;           // 기둥을 벗어나게 세울 수 없음.
                gArr[x][y] = 1;
                if (!isPossible()) gArr[x][y] = 0;
            } else if (a == 1 && cmd == 0) {    // 보 삭제
                bArr[x][y] = 0;
                if (!isPossible()) bArr[x][y] = 1;
            } else if (a == 1 && cmd == 1) {    // 보 추가
                if (x == n || y == n) continue; // 보는 바닥에 설치 불가하고, 보를 n에 놓을 수 없음.
                bArr[x][y] = 1;
                if (!isPossible()) bArr[x][y] = 0;
            }
        }

        return checkGiAndBo();
    }

    public boolean isPossible() {
        // 보 검사
        // 보는 한쪽 끝 부분이 기둥 위에 있거나, 또는 양쪽 끝 부분이 다른 보와 동시에 연결되어 있어야 합니다.
        for (int i = 0; i < n + 1; i++) {
            for (int j = 0; j < n + 1; j++) {
                if (bArr[i][j] == 0) continue;
                if (j + 1 <= n && bArr[i][j + 1] == 1 && j - 1 >= 0 && bArr[i][j - 1] == 1)
                    continue;    // 한쪽 끝 부분이 다른 보와 연결
                if (i + 1 <= n && j + 1 <= n && gArr[i + 1][j + 1] == 1) continue;    // 한쪽 끝 부분이 기둥 위에 있거나.
                if (i + 1 <= n && gArr[i + 1][j] == 1) continue;
                return false;
            }
        }
        // 기둥 검사
        // 기둥은 바닥 위에 있거나 보의 한쪽 끝 부분 위에 있거나, 또는 다른 기둥 위에 있어야 합니다.
        for (int i = 0; i < n + 1; i++) {
            for (int j = 0; j < n + 1; j++) {
                if (gArr[i][j] == 0) continue;  // 기둥이 없으면 조사 안함.
                if (i == n) continue;   // 기둥은 바닥에 세울 수 있음.
                if (bArr[i][j] == 1 || (j - 1 >= 0 && bArr[i][j - 1] == 1)) continue; // 보의 한쪽 끝 부분 위에 있거나.
                if (i + 1 <= n && gArr[i + 1][j] == 1) continue;    // 기둥 위에 있거나.
                return false;
            }
        }
        return true;
    }

    public int[][] checkGiAndBo() {
        ArrayList<int[]> list = new ArrayList<>();
        for (int i = 0; i <= n; i++) {
            for (int j = 0; j <= n; j++) {
                if (gArr[i][j] == 1) list.add(new int[]{j, n - i, 0});
            }
        }

        for (int i = 0; i <= n; i++) {
            for (int j = 0; j <= n; j++) {
                if (bArr[i][j] == 1) list.add(new int[]{j, n - i, 1});
            }
        }
        list.sort((a, b) -> {
            if (a[0] != b[0]) return Integer.compare(a[0], b[0]);
            if (a[1] != b[1]) return Integer.compare(a[1], b[1]);
            return Integer.compare(a[2], b[2]);
        });

        int[][] answer = new int[list.size()][3];
        int idx = 0;
        for (int[] point : list) {
            answer[idx][0] = point[0];
            answer[idx][1] = point[1];
            answer[idx++][2] = point[2];
        }
        return answer;
    }
}
