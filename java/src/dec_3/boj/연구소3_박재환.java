package dec_3.boj;

import java.util.*;
import java.io.*;

public class 연구소3_박재환 {
    static BufferedReader br;
    public static void main(String[] args) throws IOException {
        br = new BufferedReader(new InputStreamReader(System.in));
        init();
        br.close();
    }

    /**
     * 바이러스는 활성 상태와 비활성 상태가 있다.
     * 초기에는 모두 비활성 상태이다.
     *
     * 활성 상태의 바이러스는 상하좌우로 인접한 모든 빈 칸으로 동시에 복제된다.-> 1초가 걸린다.
     *
     * 승원이는 바이러스 M개를 활성 상태로 변경하려한다.
     *
     * N x N
     * - 빈 칸( 0 )
     * - 벽 ( 1 )
     * - 바이러스 ( 2 )
     * 로 이루어져 있다.
     *
     * => M 개의 바이러스를 선택해서, 최단 시간 내에 바이러스를 모두 퍼뜨린다.
     */
    static StringTokenizer st;
    static int n, m;
    static int[][] board;
    static List<int[]> viruses;
    static int emptySpace;
    static void init() throws IOException {
        viruses = new ArrayList<>();
        emptySpace = 0;
        st = new StringTokenizer(br.readLine().trim());
        n = Integer.parseInt(st.nextToken());
        board = new int[n][n];
        m = Integer.parseInt(st.nextToken());

        for(int x=0; x<n; x++) {
            st = new StringTokenizer(br.readLine().trim());
            for(int y=0; y<n; y++) {
                board[x][y] = Integer.parseInt(st.nextToken());
                if(board[x][y] == 2) viruses.add(new int[] {x, y});
                else if(board[x][y] == 0) emptySpace++;
            }
        }
        // 바로 끝낼 수 있는 경우
        if(emptySpace == 0) {
            System.out.println(0);
            return;
        }

        solution();
    }

    static int minTime;
    static void solution() {
        minTime = Integer.MAX_VALUE;
        getVirusCombination(0,0, new int[m]);
        // 채울 수 없는 경우는 -1
        System.out.println(minTime == Integer.MAX_VALUE ? -1 : minTime);
    }

    static void getVirusCombination(int idx, int selectedIdx, int[] virusCandidate) {
        if(selectedIdx == m) {  // 조합 완성
            // 해당 조합으로, 바이러스를 퍼뜨릴 수 있는 최단 시간 구하기
            getMinTime(virusCandidate);
            return;
        }

        if(idx >= viruses.size()) return;   // 더 이상 조합을 만들 수 없음
        if(selectedIdx + (viruses.size()-idx) < m) return;  // 가지치기 => 조합을 완성할 수 없음

        virusCandidate[selectedIdx] = idx;
        getVirusCombination(idx+1, selectedIdx+1, virusCandidate);
        getVirusCombination(idx+1, selectedIdx, virusCandidate);
    }

    static int[] dx = {0,1,0,-1};
    static int[] dy = {1,0,-1,0};
    static void getMinTime(int[] virusCandidate){
        Queue<int[]> q = new ArrayDeque<>();

        int[][] timeBoard = new int[n][n];      // 도착 시간을 기록
        for(int[] arr : timeBoard) Arrays.fill(arr, -1);        // 배열 초기화
        for(int idx : virusCandidate) {
            int[] point = viruses.get(idx);
            timeBoard[point[0]][point[1]] = 0;      // 활성화된 바이러스 표시 ( 0초 부터 시작 )
            q.offer(point);
        }

        int maxTime = Integer.MIN_VALUE;
        int spreadAreaCnt = 0;

        while(!q.isEmpty()) {
            int[] cur = q.poll();
            int curX = cur[0];
            int curY = cur[1];
            int curTime = timeBoard[curX][curY];

            for(int dir=0; dir<4; dir++) {
                int nx = curX + dx[dir];
                int ny = curY + dy[dir];

                if(nx < 0 || ny < 0 || nx >= n || ny >= n) continue;
                if(timeBoard[nx][ny] != -1 || board[nx][ny] == 1) continue;

                // 현재 이동하려는 칸이 빈칸인지, 다른 바이러스가 있는 칸인지 확인
                timeBoard[nx][ny] = curTime+1;

                if(board[nx][ny] == 0) {    // 빈 칸이라면
                    spreadAreaCnt++;
                    maxTime = Math.max(maxTime, timeBoard[nx][ny]);

                    if(maxTime >= minTime) return;      // 이미 이전의 최적해를 넘어서는 경우

                    if(spreadAreaCnt == emptySpace) {
                        minTime = Math.min(minTime, maxTime);
                        return;
                    }
                }

                q.offer(new int[] {nx, ny});
            }
        }
    }
}
