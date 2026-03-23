package jan.week2.boj;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.StringTokenizer;
import java.io.IOException;


public class 가희의고구마먹방_이다예 {
    static BufferedReader br;
    static StringTokenizer st;

    // 맵의 세로 길이
    static int R;

    // 맵의 가로 길이
    static int C;

    // 가희가 이동하는 시간
    static int T;

    // 맵
    static char[][] map;

    // 북동남서
    static int[] dx = {0,1,0,-1};
    static int[] dy = {-1,0,1,0};

    // 결과값
    static int result;

    public static void main(String[] args) throws IOException {
        br = new BufferedReader(new InputStreamReader(System.in));
        st = new StringTokenizer(br.readLine());

        R = Integer.parseInt(st.nextToken());
        C = Integer.parseInt(st.nextToken());
        T = Integer.parseInt(st.nextToken());

        // 맵 크기
        map = new char[R][C];

        // 가희 현재 위치
        int nowX = 0;
        int nowY = 0;

        for (int i = 0; i < R ; i++){
            String line = br.readLine();
            for (int j = 0; j < C; j ++){
                map[i][j] = line.charAt(j);
                if (map[i][j] == 'G') {
                    nowX = i;
                    nowY = j;
                }
            }
        }

        result = 0;


        dfs(nowX,nowY,0, 0);


        System.out.println(result);
    }

    // 규칙
    // 1. 가희는 1초마다 상하좌우 방향 중 한 방향으로 1번 이동하거나, 이동하지 않고 그 자리에 머무를 수 있다.
    // 2. 가희가 이동한 지점에 고구마가 있는 경우에는 , 고구마를 먹음
    // 3. 가희가 고구마를 먹으면 , 고구마가 다시 그 자리에 생기지 않음
    // 최대한 많은 고구마 먹기
    static void dfs(int y, int x, int time, int eat){

        boolean ate = false;
        if (map[y][x] == 'S') {
            eat++;
            ate =true;
            map[y][x] = '.';
        }
        if (time == T) {
            if (eat > result) result = eat;
            // 백트래킹
            if(ate) map[y][x] = 'S';
            return;
        }

        for (int i = 0 ; i < 4 ; i ++){

            int nx = x + dx[i];
            int ny = y + dy[i];
            // x,y가 현재 맵을 벗어나거나 이동한 곳이 막혀있으면 갈 수 없음
            if ((0 > nx) || (nx >= C) ||  (0 > ny)
                    || (ny >= R) || (map[ny][nx] == '#')) continue;
            dfs(ny,nx,time+1,eat);
        }
        //백트래킹
        if(ate) map[y][x] = 'S';

    }

}