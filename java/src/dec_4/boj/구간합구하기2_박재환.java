package dec_4.boj;

import java.util.*;
import java.io.*;

public class 구간합구하기2_박재환 {
    static BufferedReader br;
    static StringBuilder sb;
    public static void main(String[] args) throws IOException {
        br = new BufferedReader(new InputStreamReader(System.in));
        sb = new StringBuilder();
        init();
        br.close();
        System.out.println(sb);
    }
    static StringTokenizer st;
    static int n, m, k;
    static long[] numArr;
    static long[] tree;
    static long[] lazy;
    static void init() throws IOException {
        st = new StringTokenizer(br.readLine().trim());
        n = Integer.parseInt(st.nextToken());
        numArr = new long[n];
        tree = new long[4*n];
        lazy = new long[4*n];
        m = Integer.parseInt(st.nextToken());
        k = Integer.parseInt(st.nextToken());
        int totalCmd = m+k;
        for(int i=0; i<n; i++) numArr[i] = Long.parseLong(br.readLine().trim());
        // 트리 생성
        initTree(0, n-1, 1);
        while(totalCmd-- > 0) {
            st = new StringTokenizer(br.readLine().trim());
            int commandType = Integer.parseInt(st.nextToken());

            switch(commandType) {
                case 1:{    // 변경
                    int l = Integer.parseInt(st.nextToken())-1;
                    int r = Integer.parseInt(st.nextToken())-1;
                    long addValue = Long.parseLong(st.nextToken());
                    update(0, n-1, 1, l, r, addValue);
                    break;
                }
                case 2:{    // 조회
                    int l = Integer.parseInt(st.nextToken())-1;
                    int r = Integer.parseInt(st.nextToken())-1;
                    sb.append(query(0, n-1, 1, l, r)).append('\n');
                    break;
                }
            }
        }
    }

    static long initTree(int s, int e, int idx) {
        if(s==e) return tree[idx] = numArr[s];
        int mid = s + (e-s)/2;
        long l = initTree(s, mid, idx*2);
        long r = initTree(mid+1, e, idx*2+1);
        return tree[idx] = l + r;
    }

    static void update(int s, int e, int idx, int l, int r, long addValue) {
        if(s > r || e < l) return;  // 하나도 포함되지 않는 경우
        if(s >= l && e <= r) {
            lazy[idx] += addValue;
            return;
        }
        int lMax = Math.max(s, l);
        int rMin = Math.min(e, r);
        tree[idx] += (rMin-lMax+1) * addValue;
        // 일부만 포함되는 경우
        int mid = s + (e-s)/2;
        update(s, mid, idx*2, l, r, addValue);
        update(mid+1, e, idx*2+1, l, r, addValue);
    }

    static long query(int s, int e, int idx, int l, int r) {
        if(s > r || e < l) return 0;

        // lazy 전파 및 반영
        tree[idx] += (e - s + 1) * lazy[idx];
        if(s != e) {
            lazy[idx * 2] += lazy[idx];
            lazy[idx * 2 + 1] += lazy[idx];
        }
        lazy[idx] = 0;

        if(s >= l && e <= r) {
            return tree[idx];
        }
        int mid = s + (e-s)/2;
        long lRange = query(s, mid, idx*2, l, r);
        long rRange = query(mid+1, e, idx*2+1, l, r);
        return lRange + rRange;
    }
}
