package may.week3.ngv;

import java.util.*;
import java.io.*;

public class 징검다리_박재환 {
    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        init(br);
        br.close();
    }

    static int n;
    static int[] stones;
    static void init(BufferedReader br) throws IOException {
        n = Integer.parseInt(br.readLine().trim());

        StringTokenizer st;
        stones = new int[n];
        st = new StringTokenizer(br.readLine().trim());
        for(int i = 0; i < n;) stones[i++] = Integer.parseInt(st.nextToken());

        System.out.println(solution());
    }

    static int solution() {
        /**
         * 서 -> 동으로 이동
         * 높이가 점점 높은 돌을 밟으며 개울을 지남 -> LIS
         * - DP             : O(n**2)
         * - Binary Search  : O(logN)
         * - Segment Tree   : O(logN)
         */
        List<Integer> list = new ArrayList<>();

        for(int target : stones) {
            int insertId = findInsertId(list, target);
            if(insertId == list.size()) list.add(target);
            else list.set(insertId, target);
        }

        return list.size();
    }

    static int findInsertId(List<Integer> list, int target) {
        int l = 0, r = list.size();
        /**
         * Lower Bound
         * -> target 이상인 값이 처음으로 나오는 위치
         */
        while(l < r) {
            int mid = l + (r - l) / 2;
            if(list.get(mid) < target) l = mid + 1;
            else r = mid;
        }

        return l;
    }
}
