package jul.week3.jungol;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class 면적구하기_박재환 {
    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        init(br);
        br.close();
    }

    static void init(BufferedReader br) throws IOException {
        int t = Integer.parseInt(br.readLine().trim());
        while(t-- > 0) {
            String line = br.readLine().trim();
            double result = getArea(line);
            if(result * 10 % 10 == 0) {
                System.out.println((int)result);
            }else {
                System.out.printf("%.1f\n", result);
            }
        }
    }
    /**
     * (8, 2, 6, 4) : 북, 남, 동, 서
     * (9, 7, 3, 1) : 북동, 북서, 남동, 남서
     * 5 : 도형의 끝
     */
    static double getArea(String line) {
        List<int[]> locs = new ArrayList<>();
        // 시작 위치는 항상 (0, 0)
        locs.add(new int[] {0, 0});

        for(char c : line.toCharArray()) {
            int[] lastLoc = locs.get(locs.size()-1);
            if(c == '1') {
                // 남서
                int nx = lastLoc[0] + 1;
                int ny = lastLoc[1] - 1;
                locs.add(new int[] {nx, ny});
            }
            else if(c == '2') {
                // 남
                int nx = lastLoc[0] + 1;
                int ny = lastLoc[1];
                locs.add(new int[] {nx, ny});
            }
            else if(c == '3') {
                // 남동
                int nx = lastLoc[0] + 1;
                int ny = lastLoc[1] + 1;
                locs.add(new int[] {nx, ny});
            }
            else if(c == '4') {
                // 서
                int nx = lastLoc[0];
                int ny = lastLoc[1] - 1;
                locs.add(new int[] {nx, ny});
            }
            else if(c == '5') {     // 도형의 끝
                break;
            }
            else if(c == '6') {
                // 동
                int nx = lastLoc[0];
                int ny = lastLoc[1] + 1;
                locs.add(new int[] {nx, ny});
            }
            else if(c == '7') {
                // 북서
                int nx = lastLoc[0] - 1;
                int ny = lastLoc[1] - 1;
                locs.add(new int[] {nx, ny});
            }
            else if(c == '8') {
                // 북
                int nx = lastLoc[0] - 1;
                int ny = lastLoc[1];
                locs.add(new int[] {nx, ny});
            }
            else if(c == '9') {
                // 북동
                int nx = lastLoc[0] - 1;
                int ny = lastLoc[1] + 1;
                locs.add(new int[] {nx, ny});
            }
        }
        return calcArea(locs);
    }

    static double calcArea(List<int[]> locs) {
        int sum1 = 0;
        int sum2 = 0;
        for(int i = 0; i < locs.size() - 1; i++) {
            int x1 = locs.get(i)[0];
            int y1 = locs.get(i + 1)[1];
            sum1 += (x1 * y1);

            int x2 = locs.get(i + 1)[0];
            int y2 = locs.get(i)[1];
            sum2 += (x2 * y2);
        }

        int diff = sum1 - sum2;
        double div = (diff * 1.0) / 2;
        return Math.abs(div);
    }
}
