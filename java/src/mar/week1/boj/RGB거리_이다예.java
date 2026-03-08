package mar.week1.boj;

import java.io.*;
import java.util.*;

public class RGB거리_이다예 {
    static BufferedReader br;
    static StringTokenizer st;
    static int N;
    static List<Home> homes;
    static int[][] cost;
    public static void main(String[] args) throws IOException {
        br = new BufferedReader(new InputStreamReader(System.in));
        N = Integer.parseInt(br.readLine());
        homes = new ArrayList<>();
        cost = new int[N+1][3];

        for (int i =0 ; i < N ; i++){
            st= new StringTokenizer(br.readLine());
            int redCost= Integer.parseInt(st.nextToken());
            int greenCost= Integer.parseInt(st.nextToken());
            int blueCost= Integer.parseInt(st.nextToken());

            homes.add(new Home(redCost,greenCost,blueCost));
        }

        dp();

        int result = Integer.MAX_VALUE ;
        for(int c = 0 ; c < 3 ; c++){
            if (result > cost[N][c]) result = cost[N][c];
        }
        System.out.println(result);
    }

    static void dp(){
        for(int i = 1 ; i < N+1; i++){
            Home nowHome = homes.get(i-1);
            for (int j = 0 ; j < 3 ; j++){
                if(j==0){
                    cost[i][j] = Math.min(cost[i-1][1],cost[i-1][2]) + nowHome.red;
                }
                if(j==1){
                    cost[i][j] = Math.min(cost[i-1][0],cost[i-1][2]) + nowHome.green;
                }
                if(j==2){
                    cost[i][j] = Math.min(cost[i-1][1],cost[i-1][0]) + nowHome.blue;
                }
            }
        }
    }

    static class Home{
        int red;
        int green;
        int blue;

        Home(int red, int green, int blue){
           this.red = red;
           this.green = green;
           this.blue = blue;
        }
    }
}
