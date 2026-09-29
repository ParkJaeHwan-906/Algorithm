package sep.week4.goorm;

public class _3차_배열을회전시켜보세요_박재환 {
    int[] func_a(int[] arr) {
        int length = arr.length;
        int[] ret = new int[length*2];
        for(int i = 0; i < length; i++)
            ret[i + length] = ret[i] = arr[i];
        return ret;
    }

    boolean func_b(int[] first, int[] second){
        int[] counter = new int[1001];
        for(int i = 0; i < first.length; i++){
            counter[first[i]]++;
            counter[second[i]]--;
        }
        for(int i = 0; i < 1001; i++)
            if(counter[i] != 0)
                return false;
        return true;
    }

    boolean func_c(int[] first, int[] second){
        int length = second.length;
        for(int i = 0; i < length; i++){
            int j;
            for(j = 0; j < length; j++)
                if(first[i + j] != second[j])
                    break;
            if(j == length)
                return true;
        }
        return false;
    }

    public boolean solution(int[] arrA, int[] arrB) {
        if(arrA.length != arrB.length)
            return false;
        if(func_b(arrA, arrB)) {
            int[] arrAtemp = func_a(arrA);
            if(func_c(arrAtemp, arrB))
                return true;
        }
        return false;
    }

    public static void main(String[] args) {
        _3차_배열을회전시켜보세요_박재환 sol = new _3차_배열을회전시켜보세요_박재환();
        int[] arrA1 = {1, 2, 3, 4};
        int[] arrB1 = {3, 4, 1, 2};
        boolean ret1 = sol.solution(arrA1, arrB1);

        // [실행] 버튼을 누르면 출력 값을 볼 수 있습니다.
        System.out.println("solution 메소드의 반환 값은 " + ret1 + " 입니다.");

        int[] arrA2 = {1, 2, 3, 4};
        int[] arrB2 = {1, 4, 2, 3};
        boolean ret2 = sol.solution(arrA2, arrB2);

        // [실행] 버튼을 누르면 출력 값을 볼 수 있습니다.
        System.out.println("solution 메소드의 반환 값은 " + ret2 + " 입니다.");
    }
}
