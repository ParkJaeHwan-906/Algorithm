package nov.week4.boj;

import java.util.*;
import java.io.*;

public class 전구와스위치_박재환 {
    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        System.out.print(init(br));
        br.close();
    }

    static int n;
    static char[] lights;
    static char[] targets;
    static int init(BufferedReader br) throws IOException {
        n = Integer.parseInt(br.readLine().trim());

        lights = new char[n];
        String line = br.readLine().trim();
        for(int i = 0; i < n; i++)
            lights[i] = line.charAt(i);

        targets = new char[n];
        line = br.readLine().trim();
        for(int i = 0; i < n; i++)
            targets[i] = line.charAt(i);

        return solution();
    }

    static int solution() {
        int minCount = Integer.MAX_VALUE;
        // 1번 스위치 누름
        int count = 1;
        char[] temp = lights.clone();
        temp[0] = onOff(temp[0]);
        temp[1] = onOff(temp[1]);
        for(int i =1 ; i < n; i++) {
            if(temp[i - 1] == targets[i - 1])
                continue;
            if(i < n - 1) {
                temp[i - 1] = onOff(temp[i - 1]);
                temp[i] = onOff(temp[i]);
                temp[i + 1] = onOff(temp[i + 1]);
            } else if(i == n - 1) {
                temp[i - 1] = onOff(temp[i - 1]);
                temp[i] = onOff(temp[i]);
            }
            count++;
        }
        if(isSame(temp))
            minCount = Math.min(minCount, count);
        // 1번 스위치 누르지 않음
        count = 0;
        temp = lights.clone();
        for(int i =1 ; i < n; i++) {
            if(temp[i - 1] == targets[i - 1])
                continue;
            if(i < n - 1) {
                temp[i - 1] = onOff(temp[i - 1]);
                temp[i] = onOff(temp[i]);
                temp[i + 1] = onOff(temp[i + 1]);
            } else if(i == n - 1) {
                temp[i - 1] = onOff(temp[i - 1]);
                temp[i] = onOff(temp[i]);
            }
            count++;
        }
        if(isSame(temp))
            minCount = Math.min(minCount, count);
        return minCount == Integer.MAX_VALUE ? -1 : minCount;
    }

    static char onOff(char c) {
        return c == '0' ? '1' : '0';
    }

    static boolean isSame(char[] arr) {
        for(int i = 0; i < n; i++) {
            if(arr[i] != targets[i])
                return false;
        }
        return true;
    }
}
