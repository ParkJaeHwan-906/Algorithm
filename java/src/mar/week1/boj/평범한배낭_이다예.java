package mar.week1.boj;

import java.io.*;
import java.sql.SQLOutput;
import java.util.*;

public class 평범한배낭_이다예 {
    static BufferedReader br;
    static StringTokenizer st;
    static int N, K;
    static int[][] dp;
    static List<Product> products;

    public static void main(String[] args) throws IOException {

        br = new BufferedReader(new InputStreamReader(System.in));
        st = new StringTokenizer(br.readLine());

        // 배낭 갯수
        int N = Integer.parseInt(st.nextToken());
        // 최대 무게
        int K = Integer.parseInt(st.nextToken());
        products = new ArrayList<>();
        dp = new int[N+1][K+1];

        for(int i = 0 ; i < N ;i ++){
            st = new StringTokenizer(br.readLine());
            int w = Integer.parseInt(st.nextToken());
            int v = Integer.parseInt(st.nextToken());

            products.add(new Product(w,v));
        }

        for (int i = 1; i < N+1; i++ ){
            for (int j = 1 ; j < K+1 ; j++){
                Product nowProduct = products.get(i-1);
                if (j - nowProduct.weight < 0) {
                    dp[i][j] = dp[i - 1][j];
                    continue;
                }

                dp[i][j] = Math.max(dp[i-1][j], dp[i-1][j- nowProduct.weight] + nowProduct.value);
            }
        }
        System.out.println(dp[N][K]);
    }

    static class Product{
        int weight;
        int value;

        Product(int weight, int value){
            this.weight = weight;
            this.value = value;
        }
    }
}
