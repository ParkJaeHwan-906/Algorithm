package jun.week4.codetree;

import java.util.*;
import java.io.*;

/**
 * [풀이 시간]
 * 02:03:37
 * AI 사용 여부 O
 * => 블록을 떨어뜨리는 과정이 기존 상태를 유지하면서 떨어뜨리는 것이었는데, 각 블록을 별개로 떨어뜨리는 줄 이해하고 있었음
 *      => tc2 번 오답
 * => 연한 구역 떨어뜨리는 과정에서 OutOfIndex 오류 발생 => 범위 설정 오류
 */
public class 이차원테트리스ㅡ박재환 {
    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        init(br);
        br.close();
    }

    static int score;
    static int[][] red;
    static int[][] yellow;
    static void init(BufferedReader br) throws IOException {
        StringTokenizer st;

        score = 0;
        // 연한 부분 패딩
        red = new int[4][6];
        yellow = new int[6][4];

        int k = Integer.parseInt(br.readLine().trim());
        while(k-- > 0) {
            /**
             * 1 : []
             *
             * 2 : [][]
             *
             * 3 : []
             *     []
             */
            st = new StringTokenizer(br.readLine().trim());
            int type = Integer.parseInt(st.nextToken());
            int x = Integer.parseInt(st.nextToken());
            int y = Integer.parseInt(st.nextToken());

            // [입력]®
            // 파랑 : (x, y)
            // 노랑 : (0, y)
            // 빨강 : (x, 0)

            // 1. 블록 내리기
            pushRed(x, 0, type);
            pushYellow(0, y, type);

            // 2. 블록 지우기 (노량 : 행, 빨강 : 열)
            remove();

            // 3. 연한 부분 처리
            lightRed();
            lightYellow();
        }

        int remainBlocks = 0;
        for(int[] arr : red) {
            for(int i : arr) remainBlocks += i;
        }
        for(int[] arr : yellow) {
            for(int i : arr) remainBlocks += i;
        }

        System.out.printf("%d\n%d", score, remainBlocks);
    }

    static void pushRed(int x, int y, int type) {
        // y 증가 방향으로 떨어짐
        int maxY = 0;
        if(type == 3) {     // 두 칸 확인
            for(int i = 0; i < 6; i++) {
                boolean canPush = true;
                for(int j = x; j <= x + 1; j++) {
                    if(red[j][i] != 0) {
                        canPush = false;
                        break;
                    }
                }
                if(!canPush) break;
                maxY = Math.max(maxY, i);
            }

            red[x][maxY] = 1;
            red[x + 1][maxY] = 1;
        } else {
            for(int i = 0; i < 6; i++) {
                if(red[x][i] != 0) break;
                maxY = Math.max(maxY, i);
            }

            if(type == 1) {
                red[x][maxY] = 1;
            } else {
                red[x][maxY] = 1;
                red[x][maxY - 1] = 1;
            }
        }
    }

    static void pushYellow(int x, int y, int type) {
        // x 증가 방향으로 떨어짐
        int maxX = 0;
        if(type == 2) {     // 두 칸 확인
            for(int i = 0; i < 6; i++) {
                boolean canPush = true;
                for(int j = y; j <= y + 1; j++) {
                    if(yellow[i][j] != 0) {
                        canPush = false;
                        break;
                    }
                }
                if(!canPush) break;
                maxX = Math.max(maxX, i);
            }

            yellow[maxX][y] = 1;
            yellow[maxX][y + 1] = 1;
        } else {
            for(int i = 0; i < 6; i++) {
                if(yellow[i][y] != 0) break;
                maxX = Math.max(maxX, i);
            }

            if(type == 1) {
                yellow[maxX][y] = 1;
            } else {
                yellow[maxX][y] = 1;
                yellow[maxX - 1][y] = 1;
            }
        }
    }

    static boolean removeRed() {
        boolean[] removedCol = new boolean[6];
        int removedCnt = 0;
        for(int y = 2; y < 6; y++) {
            boolean remove = true;
            for(int x = 0; x < 4; x++) {
                if(red[x][y] == 0) {
                    remove = false;
                    break;
                }
            }
            if(remove) {
                removedCol[y] = true;
                removedCnt++;
            }
        }

        if(removedCnt == 0) return false;
        score += removedCnt;

        int[][] temp = new int[4][6];
        int ny = 5;
        for(int y = 5; y >= 0; y--) {
            if(removedCol[y]) continue;
            for(int x = 0; x < 4; x++) {
                temp[x][ny] = red[x][y];
            }
            ny--;
        }
        red = temp;
        return true;
    }

    static boolean removeYellow() {
        boolean[] removedRow = new boolean[6];
        int removedCnt = 0;
        for(int x = 2; x < 6; x++) {
            boolean remove = true;
            for(int y = 0; y < 4; y++) {
                if(yellow[x][y] == 0) {
                    remove = false;
                    break;
                }
            }
            if(remove) {
                removedRow[x] = true;
                removedCnt++;
            }
        }

        if(removedCnt == 0) return false;
        score += removedCnt;

        int[][] temp = new int[6][4];
        int nx = 5;
        for(int x = 5; x >= 0; x--) {
            if(removedRow[x]) continue;
            for(int y = 0; y < 4; y++) {
                temp[nx][y] = yellow[x][y];
            }
            nx--;
        }
        yellow = temp;
        return true;
    }

    static void remove() {
        // 빨강
        while(removeRed());

        // 노랑
        while(removeYellow());
    }

    static void lightYellow() {
        int newRows = 0;
        for(int x = 0; x < 2; x++) {
            for(int y = 0; y < 4; y++) {
                if(yellow[x][y] == 1) {
                    newRows++;
                    break;
                }
            }
        }

        // 블록 내리기
        int[][] temp = new int[6][4];
        for(int x = 5; x - newRows >= 0; x--) {
            for(int y = 0; y < 4; y++) {
                temp[x][y] = yellow[x - newRows][y];
            }
        }

        yellow = temp;
    }

    static void lightRed() {
        int newCols = 0;
        for(int y = 0; y < 2; y++) {
            for(int x = 0; x < 4; x++) {
                if(red[x][y] == 1) {
                    newCols++;
                    break;
                }
            }
        }

        int[][] temp = new int[4][6];
        for(int y = 5; y - newCols >= 0; y--) {
            for(int x = 0; x < 4; x++) {
                temp[x][y] = red[x][y - newCols];
            }
        }

        red = temp;
    }
}
