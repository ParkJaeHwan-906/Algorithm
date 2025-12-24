package dec_4.boj;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.IOException;
import java.util.StringTokenizer;

// dfs 로는 안되는듯... 다른 풀이로 해보겠음....ㅠ
public class 서강그라운드_이다예_dfs {

    // N = 지역의 개수
    static int N ;
    // M = 예은이 수색범위 ( 1<= M <= 15)
    static int M ;
    // R = 길의 개수 ( 1<= R <= 100)
    static int R ;
    // 최대 아이템 갯수
    static int maxItem ;
    // 경로를 저장할 2차원 배열 선언
    static int[][] areaDistance;
    // 이미 들린 지역인지 체크
    static boolean[] visited;
    // 각 지역의 아이템 갯수
    static int[] items;
    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer st;

        st = new StringTokenizer(br.readLine());

        N = Integer.parseInt(st.nextToken());
        M = Integer.parseInt(st.nextToken());
        R = Integer.parseInt(st.nextToken());

        st = new StringTokenizer(br.readLine());

        items = new int[N+1];
        for (int i = 1 ; i <= N; i++){
            items[i] = Integer.parseInt(st.nextToken());
        }

        areaDistance = new int[N+1][N+1];

        for (int i=0; i < R; i++){
            st = new StringTokenizer(br.readLine());
            int firstArea = Integer.parseInt(st.nextToken());
            int secondArea = Integer.parseInt(st.nextToken());
            int distance = Integer.parseInt(st.nextToken());

            areaDistance[firstArea][secondArea] = distance;
            areaDistance[secondArea][firstArea] = distance;
        }

        int result = 0;
        for (int i = 1 ; i < N + 1; i ++ ){
            visited = new boolean[N+1];
            maxItem = 0;

            visited[i] = true;
            dfs(i,M,items[i]);
            result = Math.max(result,maxItem);
        }

        System.out.println(result);
    }

    static void dfs(int now, int left, int itemSum){
        if (itemSum > maxItem) maxItem = itemSum;

        for (int i = 1; i < N+1 ; i ++){
            int d = areaDistance[now][i];
            if (d == 0) continue;
            if (d > left) continue;

            if (!visited[i]){
                visited[i] = true;
                dfs(i, left - d , itemSum + items[i]);
                visited[i] = false;
            }
        }

    }
}
