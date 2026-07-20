package jul.week3.codetree;

import java.util.*;
import java.io.*;

public class 연산자배치하기_박재환 {
    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        init(br);
        br.close();
    }
    static int n;
    static int[] numArr;
    static int[] operLimit;
    static void init(BufferedReader br) throws IOException {
        StringTokenizer st;
        n = Integer.parseInt(br.readLine().trim());
        numArr = new int[n];
        st = new StringTokenizer(br.readLine().trim());
        for(int i = 0; i < n; i++) {
            numArr[i] = Integer.parseInt(st.nextToken());
        }
        operLimit = new int[3];
        st = new StringTokenizer(br.readLine().trim());
        for(int i = 0; i < 3; i++) {
            operLimit[i] = Integer.parseInt(st.nextToken());
        }
        System.out.println(solution());
    }
    static long max, min;
    static String solution() {
        max = Integer.MIN_VALUE;
        min = Integer.MAX_VALUE;
        char[] operArr = new char[n - 1];
        findAllComb(0, operArr);
        return String.format("%d %d", min, max);
    }

    static void findAllComb(int seq, char[] operArr) {
        if(seq == n - 1) {
            long result = makeOperResult(operArr);
            max = Math.max(result, max);
            min = Math.min(result, min);
            return;
        }
        if(operLimit[0] > 0) {
            operArr[seq] = '+';
            operLimit[0]--;
            findAllComb(seq + 1, operArr);
            operLimit[0]++;
        }
        if(operLimit[1] > 0) {
            operArr[seq] = '-';
            operLimit[1]--;
            findAllComb(seq + 1, operArr);
            operLimit[1]++;
        }
        if(operLimit[2] > 0) {
            operArr[seq] = '*';
            operLimit[2]--;
            findAllComb(seq + 1, operArr);
            operLimit[2]++;
        }
    }

    static long makeOperResult(char[] operArr) {
        long cur = numArr[0];
        for(int i = 1; i < n; i++) {
            long next = numArr[i];
            if(operArr[i - 1] == '+') cur += next;
            else if(operArr[i - 1] == '-') cur -= next;
            else if(operArr[i - 1] == '*') cur *= next;
        }
        return cur;
    }
}
