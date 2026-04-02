package mar.week5.boj;

import java.util.*;
import java.io.*;

public class 뒤집기3_박재환 {
    static BufferedReader br;
    public static void main(String[] args) throws IOException {
        br = new BufferedReader(new InputStreamReader(System.in));
        init();
        br.close();
    }
    static void init() throws IOException {
        String line = br.readLine().trim();

        String result = "";
        for(char c : line.toCharArray()) {
            String front = c + result;
            String back = result + c;

            if(front.compareTo(back) < 0) result = front;
            else result = back;
        }

        System.out.println(result);
    }
}
