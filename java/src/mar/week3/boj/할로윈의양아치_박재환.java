package mar.week3.boj;

import java.util.*;
import java.io.*;

public class 할로윈의양아치_박재환 {
    static BufferedReader br;
    public static void main(String[] args) throws IOException {
        br = new BufferedReader(new InputStreamReader(System.in));
        init();
        br.close();
    }
    static StringTokenizer st;
    static int n, m, k;
    static int[] candies;
    static void init() throws IOException {
        st = new StringTokenizer(br.readLine().trim());
        n = Integer.parseInt(st.nextToken());
        m = Integer.parseInt(st.nextToken());
        k = Integer.parseInt(st.nextToken());

        candies = new int[n+1];
        st = new StringTokenizer(br.readLine().trim());
        for(int i=1; i<n+1;) candies[i++] = Integer.parseInt(st.nextToken());

        make(n);
        for(int i=0; i<m; i++) {
            st = new StringTokenizer(br.readLine().trim());
            int a = Integer.parseInt(st.nextToken());
            int b = Integer.parseInt(st.nextToken());
            union(a, b);
        }
        makeGroupArr();
        System.out.println(solution());
    }
    static int[][] groups;
    static void makeGroupArr() {
        groups = new int[n+1][2];
        for(int i=1; i<n+1; i++) {
            int gId = find(i);
            int candy = candies[i];
            groups[gId][0]++;
            groups[gId][1]+=candy;
        }
    }
    static int solution() {
        int[] dp = new int[k];
        for(int[] arr : groups) {
            if(arr[1] == 0) continue;
            for(int v=k-1; v>=arr[0]; v--) {
                dp[v] = Math.max(dp[v-arr[0]] + arr[1], dp[v]);
            }
        }
        return dp[k-1];
    }
    /**
     * Union Find 를 이용해, 집합 단위로 묶어준다.
     */
    static int[] parents;
    static int[] ranks;
    static void make(int n) {
        parents = new int[n+1];
        ranks = new int[n+1];
        for(int i=1; i<n+1; i++) parents[i] = i;
    }
    static int find(int a) {
        if(parents[a] == a) return a;
        return parents[a] = find(parents[a]);
    }
    static boolean union(int a, int b) {
        int rootA = find(a);
        int rootB = find(b);
        if(rootA == rootB) return false;

        if(ranks[rootA] > ranks[rootB]) parents[rootB] = rootA;
        else if (ranks[rootA] < ranks[rootB]) parents[rootA] = rootB;
        else {
            parents[rootA] = rootB;
            ranks[rootB]++;
        }
        return true;
    }
}
