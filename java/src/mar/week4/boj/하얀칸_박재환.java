package mar.week4.boj;

import java.util.*;
import java.io.*;

public class 하얀칸_박재환 {
    static BufferedReader br;
    public static void main(String[] args) throws IOException {
        br = new BufferedReader(new InputStreamReader(System.in));

        int cnt = 0;
        for(int x=0; x<8; x++) {
            String line = br.readLine().trim();
            for(int y=0; y<8; y++) {
                if((x + y) % 2 == 0 && line.charAt(y) == 'F') cnt++;
            }
        }

        br.close();
        System.out.println(cnt);
    }
}
