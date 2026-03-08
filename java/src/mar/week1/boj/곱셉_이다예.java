package mar.week1.boj;

import java.io.*;
import java.util.*;

/*
분할 정복을 이용한 빠른 거듭제곱 문제.
A^B를 그대로 계산하면 O(B)이지만
A^B = (A^(B/2))^2 성질을 이용하면 O(log B)에 계산 가능하다.

또한 (A * B) % C = ((A % C) * (B % C)) % C 성질을 이용하여
곱할 때마다 %C를 적용해 overflow를 방지한다.
*/

public class 곱셉_이다예 {
    static BufferedReader br;
    static StringTokenizer st;
    static int A,B,C;

    public static void main(String[] args) throws IOException {
        br = new BufferedReader(new InputStreamReader(System.in));
        st = new StringTokenizer(br.readLine());
        A = Integer.parseInt(st.nextToken());
        B = Integer.parseInt(st.nextToken());
        C = Integer.parseInt(st.nextToken());

        long result = pow(A,B);
        System.out.println(result);
    }

    static long pow(long a,long b){

        if (b==1) return a % C;

        long half = pow(a,b/2);

        if (b % 2 == 1){
            return (half * half % C) * a % C;
        }
        else {
            return half * half % C;
        }

    }
}

