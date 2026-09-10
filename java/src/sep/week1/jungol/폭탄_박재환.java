package sep.week1.jungol;

import java.io.*;

public class 폭탄_박재환 {
    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        init(br);
        br.close();
    }

    static String word;
    static String bomb;
    static void init(BufferedReader br) throws IOException {
        word = br.readLine().trim();
        bomb = br.readLine().trim();
        System.out.print(solution());
    }

    static String solution() {
        char[] stack = new char[word.length()];
        int size = 0;   // 현재 쌓여 있는 문자의 개수
        int bombLength = bomb.length();

        for(int i = 0; i < word.length(); i++) {
            // 1. 문자를 하나 쌓는다.
            stack[size++] = word.charAt(i);
            // 폭탄보다 짧으면 비교할 필요가 없다.
            if(size < bombLength) continue;

            // 2. 끝부분이 폭탄과 같은지 확인한다.
            boolean isBomb = true;
            int start = size - bombLength;
            for(int j = 0; j < bombLength; j++) {
                if(stack[start + j] != bomb.charAt(j)) {
                    isBomb = false;
                    break;
                }
            }
            // 3. 폭탄이면 쌓인 문자의 개수만 줄인다.
            if(isBomb) {
                size -= bombLength;
            }
        }

        return size == 0 ? "FRULA" : new String(stack, 0, size);
    }
}
