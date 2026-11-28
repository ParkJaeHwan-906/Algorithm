package nov.week4.boj;

import java.util.*;
import java.io.*;

public class 감소하는수_박재환 {
    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        System.out.print(init(br));
        br.close();
    }

    static int n;
    static long init(BufferedReader br) throws IOException {
        n = Integer.parseInt(br.readLine().trim());
        return solution();
    }

    static List<Long> numbers;
    static long solution() {
        numbers = new ArrayList<>();
        for(int i = 0; i <= 9; i++)
            findNumber(1, i);
        numbers.sort(Long::compareTo);
        if(numbers.size() <= n)
            return -1;
        return numbers.get(n);
    }

    static void findNumber(int len, long number) {
        numbers.add(number);
        if(len == 10)           // 최대가 9876543210 임
            return;
        long offset = (long) Math.pow(10, len);
        long prev = number / (offset / 10);
        for (int i = 0; i <= 9; i++) {
            if (prev < i) {
                long next = i * offset + number;
                findNumber(len + 1, next);
            }
        }
    }
}