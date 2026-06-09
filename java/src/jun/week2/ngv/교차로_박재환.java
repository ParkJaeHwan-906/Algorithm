package jun.week2.ngv;

import java.util.*;
import java.io.*;

/**
 * [풀이 시간]
 * 00:40:38
 * AI 사용 여부 X
 */
public class 교차로_박재환 {
    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        init(br);
        br.close();
    }
    static class Car {
        int id;
        int inTime;
        int road;

        Car(int id, int inTime, int road) {
            this.id = id;
            this.inTime = inTime;
            this.road = road;
        }
    }

    static int n;       // 차량 수
    static Queue<Car>[] roads;      // 교차로
    static Car[] cars;
    static void init(BufferedReader br) throws IOException {
        StringTokenizer st;

        n = Integer.parseInt(br.readLine().trim());
        cars = new Car[n + 1];
        for(int i = 1; i < n + 1; i++) {
            st = new StringTokenizer(br.readLine().trim());
            int inTime = Integer.parseInt(st.nextToken());
            int road = st.nextToken().charAt(0) - 'A';      // A : 0, B : 1, C : 2, D : 3s
            Car car = new Car(i, inTime, road);
            cars[i] = car;
        }

        System.out.println(solution());
    }

    static String solution() {
        set();

        int time = 0;
        int[] result = new int[n + 1];
        Arrays.fill(result, -1);
        // 교차로 내에 모든 차량이 나갈때까지
        while(!roads[0].isEmpty() || !roads[1].isEmpty() || !roads[2].isEmpty() || !roads[3].isEmpty()) {
//            System.out.println("시간 : " + time);
            // 현재 시간에 대기중인 차량 뽑기
            Car carA = roads[0].isEmpty() || roads[0].peek().inTime > time ? null : roads[0].peek();
            Car carB = roads[1].isEmpty() || roads[1].peek().inTime > time ? null : roads[1].peek();
            Car carC = roads[2].isEmpty() || roads[2].peek().inTime > time ? null : roads[2].peek();
            Car carD = roads[3].isEmpty() || roads[3].peek().inTime > time ? null : roads[3].peek();

            // 교착상태인지 확인 -> 바로 종료
            if(carA != null && carB != null && carC != null && carD != null) break;
            // 현재 시간에서 차가 한 대도 없는 경우 -> 시간 점프 -> time 이 10**9 라 순차적으로 접근하면 안될듯
            if(carA == null && carB == null && carC == null && carD == null) {
                int nextTime = Integer.MAX_VALUE;
                for(Queue<Car> q : roads) {
                    if(q.isEmpty()) continue;
                    nextTime = Math.min(q.peek().inTime, nextTime);
                }
                time = nextTime;
                continue;
            }
            // null 이 아닌 차량들의 우측 도로 확인..?
            if(carA != null && carD == null) {
                roads[0].poll();
                result[carA.id] = time;
//                System.out.println("A 탈출");
            }

            if(carB != null && carA == null) {
                roads[1].poll();
                result[carB.id] = time;
//                System.out.println("B 탈출");
            }

            if(carC != null && carB == null) {
                roads[2].poll();
                result[carC.id] = time;
//                System.out.println("C 탈출");
            }

            if(carD != null && carC == null) {
                roads[3].poll();
                result[carD.id] = time;
//                System.out.println("D 탈출");
            }

            time++;
        }

//        System.out.println(Arrays.toString(result));

        return makeString(result);
    }

    static void set() {
        roads = new Queue[4];
        for(int i = 0; i < 4; i++) roads[i] = new ArrayDeque<>();

        for(int i = 1; i < n + 1; i++) {
            Car car = cars[i];
            roads[car.road].offer(car);
        }
    }

    static String makeString(int[] arr) {
        StringBuilder sb = new StringBuilder();
        for(int i = 1; i < n + 1; i++) {
            sb.append(String.format("%d\n", arr[i]));
        }
        return sb.toString();
    }
}
