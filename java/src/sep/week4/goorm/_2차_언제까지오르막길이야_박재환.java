package sep.week4.goorm;

import java.util.*;

public class _2차_언제까지오르막길이야_박재환 {
    public int solution(int[] arr) {
        List<Integer> lis = new ArrayList<>();
        for(int num : arr) {
            int insertIdx = findInsertIdx(num, lis);
            if(insertIdx == lis.size()) {
                lis.add(num);
            } else {
                lis.set(insertIdx, num);
            }
        }
        return lis.size();
    }

    /**
     * num 보다 크거나 같은 가장 작은 값을 찾는다.
     */
    int findInsertIdx(int num, List<Integer> lis) {
        int l = 0, r = lis.size();
        while(l < r) {
            int mid = l + (r - l) / 2;
            if(lis.get(mid) >= num) {
                r = mid;
            } else {
                l = mid + 1;
            }
        }
        return l;
    }

    public static void main(String[] args) {
        _2차_언제까지오르막길이야_박재환 sol = new _2차_언제까지오르막길이야_박재환();
        int[] arr = {3, 1, 2, 4, 5, 1, 2, 2, 3, 4};
        int ret = sol.solution(arr);

        // [실행] 버튼을 누르면 출력 값을 볼 수 있습니다.
        System.out.println("solution 메소드의 반환 값은 " + ret + " 입니다.");
    }
}
