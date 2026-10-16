package oct.week2.swea;

import java.util.*;
import java.io.*;

public class 보호필름_박재환 {
    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringBuilder sb = new StringBuilder();
        int t = Integer.parseInt(br.readLine().trim());
        for(int tc = 1; tc <= t; tc++) {
            sb.append('#').append(tc).append(' ').append(init(br)).append('\n');
        }
        System.out.print(sb);
        br.close();
    }

    static int d, w, k;
    static int[][] films;
    static int init(BufferedReader br) throws IOException {
        StringTokenizer st;
        st = new StringTokenizer(br.readLine().trim());
        d = Integer.parseInt(st.nextToken());
        w = Integer.parseInt(st.nextToken());
        k = Integer.parseInt(st.nextToken());
        films = new int[d][w];
        for(int x = 0; x < d; x++) {
            st = new StringTokenizer(br.readLine().trim());
            for(int y = 0; y < w; y++)
                films[x][y] = Integer.parseInt(st.nextToken());
        }
        return solution();
    }

    static boolean isPass;
    static int solution() {
        if(pass())
            return 0;
        int min = d;
        for(int i = 1; i <= d; i++) {
            isPass = false;
            findCase(i, 0);
            if(isPass) {
                min = i;
                break;
            }
        }
        return min;
    }

    static void findCase(int remain, int row) {
        if(isPass)
            return;

        if(remain == 0) {
            if(pass())
                isPass = true;
            return;
        }
        if(row == d)
            return;

        int[] origin = films[row];
        int[] a = new int[w];
        Arrays.fill(a, 1);
        films[row] = a;
        findCase(remain - 1, row + 1);
        int[] b = new int[w];
        films[row] = b;
        findCase(remain - 1, row + 1);
        films[row] = origin;
        findCase(remain, row + 1);
    }

    static boolean pass() {
        for(int y = 0; y < w; y++) {
            boolean isPass1 = false;
            for(int x = 0; x <= d - k; x++) {
                boolean isPass2 = true;
                for(int z = x; z < x + k; z++) {
                    if(films[x][y] == films[z][y])
                        continue;
                    isPass2 = false;
                    break;
                }
                if(isPass2) {
                    isPass1 = true;
                    break;
                }
            }
            if(!isPass1)
                return false;
        }
        return true;
    }
}
