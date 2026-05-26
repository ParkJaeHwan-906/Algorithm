import java.io.*;
import java.util.*;

public class 징검다리_박서희 {

    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

        int N = Integer.parseInt(br.readLine());

        StringTokenizer st = new StringTokenizer(br.readLine());
        int[] arr = new int[N];
        for (int i = 0; i < N; i++) {
            arr[i] = Integer.parseInt(st.nextToken());
        }

        System.out.println(getLisLength(arr));
    }

    static int getLisLength(int[] arr) {
        ArrayList<Integer> lis = new ArrayList<>();
        lis.add(arr[0]);

        for (int i = 1; i < arr.length; i++) {
            int cur = arr[i];

            if (cur > lis.get(lis.size() - 1)) {
                lis.add(cur);
            } else {
                int insertIdx = Collections.binarySearch(lis, cur);

                if (insertIdx < 0) {
                    insertIdx = -insertIdx - 1;
                }
                lis.set(insertIdx, cur);
            }
        }

        return lis.size();
    }
}
