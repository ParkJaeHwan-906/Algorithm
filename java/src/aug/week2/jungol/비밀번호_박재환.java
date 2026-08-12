package aug.week2.jungol;

import java.util.*;
import java.io.*;

public class 비밀번호_박재환 {
    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        init(br);
        br.close();
    }

    static void init(BufferedReader br) throws IOException {
        long a = Long.parseLong(br.readLine());
        System.out.println(solution(a));
    }

    static String solution(long a) {
        long min = 0;
        long max = 0;

        for (int i = 0; i < 62; i++) {
            boolean currentBit = (a & (1L << i)) != 0;
            boolean nextBit = (a & (1L << (i + 1))) != 0;

            // 오른쪽부터 처음 나오는 10을 01로 바꾼다.
            if (!currentBit && nextBit) {
                int oneCount = Long.bitCount(a & ((1L << i) - 1));

                min = a & ~((1L << (i + 2)) - 1);
                min |= 1L << i;

                // 남은 1은 가능한 높은 자리에 배치한다.
                if (oneCount > 0) {
                    min |= ((1L << oneCount) - 1) << (i - oneCount);
                }
                break;
            }
        }

        for (int i = 0; i < 62; i++) {
            boolean currentBit = (a & (1L << i)) != 0;
            boolean nextBit = (a & (1L << (i + 1))) != 0;

            // 오른쪽부터 처음 나오는 01을 10으로 바꾼다.
            if (currentBit && !nextBit) {
                int oneCount = Long.bitCount(a & ((1L << i) - 1));

                max = a & ~((1L << (i + 1)) - 1);
                max |= 1L << (i + 1);

                // 남은 1은 가능한 낮은 자리에 배치한다.
                max |= (1L << oneCount) - 1;
                break;
            }
        }

        return String.format("%d %d", min, max);
    }
}
