package may.week4.codetree;

import java.io.*;
import java.util.*;

/*
  문제풀이 시간: 4:09:52 (잘못된 설명과 잘못된 코드 구현의 대환장 파티..)
  AI 사용 여부: X (사실 예제 돌리고 틀리고 나서 사용했는데 내가 준 설명(예제2번)이 틀려서 AI가 별 도움이 안 됐음. 결국 재환님께 help)
  생각의 흐름: 행에 +3을 더해서 더 편하게 계산하고 했다. 그리고 틀렸던 이유는 1) 동쪽과 서쪽 회전 방향이 같다고 문제를 읽었다.(눈을 제대로 떠!)
            2) 내려가는 걸 반복해야 하는데 반복 안함.(남쪽, 서쪽, 동쪽으로 가고 다시 남쪽, 서쪽, 동쪽으로 이동했어야 함.)
            따로 골렘 객체를 만들지 않고, 각 골렘의 숫자를 매겨서 1 ~ K로 두고, 출구는 따로 k * 1001로 표시했다.(K가 최대 1000)이라서
            그래서 bfs 돌 때 같은 숫자만 조사하거나 현재 출구에 있다면(1001보다 크거나 같으면) 다른 k로도 갈 수 있도록 했다.
 */
public class 마법의숲탐색_박서희 {

    static int R, C;
    static int k;
    static int[][] grid;
    static int answer = 0;

    static int[] dx = {-1, 0, 1, 0};
    static int[] dy = {0, 1, 0, -1};

    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

        StringTokenizer st = new StringTokenizer(br.readLine());
        R = Integer.parseInt(st.nextToken());
        C = Integer.parseInt(st.nextToken());
        int K = Integer.parseInt(st.nextToken());

        grid = new int[R + 3][C]; // 골렘의 세로 길이 고려

        for (k = 1; k < K + 1; k++) {
            st = new StringTokenizer(br.readLine());
            int c = Integer.parseInt(st.nextToken()) - 1;
            int d = Integer.parseInt(st.nextToken());
            int[] alien = moveGolem(c, d, k);

            if (alien[0] == -1) continue;

            answer = answer + moveAlien(alien) - 2;
        }

        System.out.println(answer);
    }

    static int[] moveGolem(int c, int d, int k) {
        int curH = 1;
        int curC = c;
        int curD = d;

        while (true) {
            // 남쪽으로 내려갈 수 있을 때까지 내려가기
            if (curH <= R) {
                if (grid[curH + 1][curC - 1] == 0 && grid[curH + 2][curC] == 0 && grid[curH + 1][curC + 1] == 0) {
                    curH++;
                    continue;
                }
            }
            // 남쪽으로 내려갈 수 없으면 서쪽으로 내려가기 + 출구 방향은 반시계로
            if (curH <= R && curC >= 2) {
                if (grid[curH - 1][curC - 1] == 0 && grid[curH][curC - 2] == 0 && grid[curH + 1][curC - 1] == 0) {
                    if (grid[curH + 1][curC - 2] == 0 && grid[curH + 2][curC - 1] == 0) {
                        curH++;
                        curC--;
                        curD = ((curD - 1) + 4) % 4;
                        continue;
                    }
                }
            }
            // 서쪽으로 갈 수 없다면 동쪽으로 내려가기 + 출구 방향은 반시계로 -> 시계다! 시계!
            if (curH <= R && curC <= C - 3) {
                if (grid[curH - 1][curC + 1] == 0 && grid[curH][curC + 2] == 0 && grid[curH + 1][curC + 1] == 0) {
                    if (grid[curH + 1][curC + 2] == 0 && grid[curH + 2][curC + 1] == 0) {
                        curH++;
                        curC++;
                        curD = ((curD + 1)) % 4;
                        continue;
                    }
                }
            }
            break;
        }

        // 최종 위치가 grid(추가한 높이 3 제외한 grid)를 벗어나면 grid 다 지우고 return
        if (!inRealRange(curH, curC)) {
            for (int i = 0; i < R + 3; i++) {
                for (int j = 0; j < C; j++) {
                    grid[i][j] = 0;
                }
            }
            return new int[]{-1, -1};
        }

        grid[curH][curC] = k;
        for (int i = 0; i < 4; i++) {
            grid[curH + dx[i]][curC + dy[i]] = k;
        }
        grid[curH + dx[curD]][curC + dy[curD]] = k * 1001;

        return new int[]{curH, curC};
    }

    static int moveAlien(int[] alienPos) {
        int curH = alienPos[0];
        int curC = alienPos[1];
        int maxH = alienPos[0];

        int[][] visited = new int[R + 3][C];
        Queue<int[]> q = new LinkedList<>();

        q.add(new int[]{curH, curC});
        visited[curH][curC] = 1;

        while (!q.isEmpty()) {
            int[] cur = q.poll();
            curH = cur[0];
            curC = cur[1];
            maxH = Math.max(maxH, curH);

            for (int d = 0; d < 4; d++) {
                int nH = curH + dx[d];
                int nC = curC + dy[d];

                if (!inGridRange(nH, nC))
                    continue;
                if (visited[nH][nC] == 1)
                    continue;

                if (grid[nH][nC] == grid[curH][curC] || grid[nH][nC] == grid[curH][curC] * 1001) {
                    q.add(new int[]{nH, nC});
                    visited[nH][nC] = 1;
                } else if (grid[curH][curC] >= 1001 && grid[nH][nC] > 0) {
                    q.add(new int[]{nH, nC});
                    visited[nH][nC] = 1;
                }
            }
        }
        return maxH;
    }

    // 골렘이 실제 높이인 grid를 벗어나는지
    static boolean inRealRange(int x, int y) {
        return 4 <= x && x < R + 2 && 1 <= y && y < C - 1;
    }

    // 해당 칸이 grid를 벗어나는지
    static boolean inGridRange(int x, int y) {
        return 0 <= x && x < R + 3 && 0 <= y && y < C;
    }
}
