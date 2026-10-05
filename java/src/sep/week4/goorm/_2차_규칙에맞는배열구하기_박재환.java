package sep.week4.goorm;

import java.util.*;

public class _2차_규칙에맞는배열구하기_박재환 {
    public int[] solution(int[] arr) {
        int left = 0, right = arr.length - 1;
        int idx = 0;
        int[] answer = new int[arr.length];
        while(left <= right){
            if(idx % 2 == 0){
                answer[idx] = arr[left];
                left += 1;
            }
            else{
                answer[idx] = arr[right];
                right -= 1;
            }
            idx += 1;
        }
        return answer;
    }

    public static void main(String[] args) {
        _2차_규칙에맞는배열구하기_박재환 sol = new _2차_규칙에맞는배열구하기_박재환();
        int[] arr = {1, 2, 3, 4, 5, 6};
        int[] ret = sol.solution(arr);

        // [실행] 버튼을 누르면 출력 값을 볼 수 있습니다.
        System.out.println("solution 메소드의 반환 값은 " + Arrays.toString(ret) + " 입니다.");
    }
}
