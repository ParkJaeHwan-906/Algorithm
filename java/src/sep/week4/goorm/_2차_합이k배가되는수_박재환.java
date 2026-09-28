package sep.week4.goorm;

public class _2차_합이k배가되는수_박재환 {
    int count;
    public int solution(int[] arr, int K) {
        count = 0;
        find(0, arr, 0, 0, K);
        return count;
    }

    void find(int id, int[] arr, int sum, int selected, int k) {
        if(sum % k == 0 && selected == 3) {
            count++;
            return;
        }
        if(id == arr.length || selected == 3) {
            return;
        }
        find(id + 1, arr, sum + arr[id], selected + 1, k);
        find(id + 1, arr, sum, selected, k);
    }

    public static void main(String[] args) {
        _2차_합이k배가되는수_박재환 sol = new _2차_합이k배가되는수_박재환();
        int[] arr = {1, 2, 3, 4, 5};
        int K = 3;
        int ret = sol.solution(arr, K);

        // [실행] 버튼을 누르면 출력 값을 볼 수 있습니다.
        System.out.println("solution 메소드의 반환 값은 " + ret + " 입니다.");
    }
}
