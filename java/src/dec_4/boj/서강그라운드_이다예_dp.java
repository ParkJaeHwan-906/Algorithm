package dec_4.boj;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.IOException;
import java.util.StringTokenizer;

public class 서강그라운드_dp {

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
    // 각 지역의 아이템 갯수
    static int[] items;

    static final int INF = 1_000_000_000;

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
        for (int i = 1; i <= N; i++) {
            for (int j = 1; j <= N; j++) {
                areaDistance[i][j] = (i == j) ? 0 : INF;
            }
        }

        for (int i=0; i < R; i++){
            st = new StringTokenizer(br.readLine());
            int firstArea = Integer.parseInt(st.nextToken());
            int secondArea = Integer.parseInt(st.nextToken());
            int distance = Integer.parseInt(st.nextToken());

            areaDistance[firstArea][secondArea] = distance;
            areaDistance[secondArea][firstArea] = distance;
        }

        // 플로이드 - 워셜
        // 여러 지역을 거쳐서 오는 길도 경로 계산할때 고려
        for (int k = 1 ; k <= N; k++){
            for (int i = 1; i <= N; i++){
                for(int j=1; j <= N ; j++){
                    if (areaDistance[i][k] + areaDistance[k][j] < areaDistance[i][j]){
                        areaDistance[i][j]  = areaDistance[i][k] + areaDistance[k][j];
                    }
                }
            }
        }

        int result = 0;
        for (int now = 1; now <= N ; now++){
            int sum = 0;
            for (int j = 1; j<=N; j++){
                if(areaDistance[now][j] <= M) sum += items[j];
            }
            result = Math.max(result,sum);
        }

        System.out.println(result);
    }


}
