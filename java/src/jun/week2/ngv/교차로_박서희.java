package jun.week2.ngv;

import java.io.*;
import java.util.*;

/*
  문제풀이 시간: 01:19:49
  AI 사용 여부: O 4개의 큐에서 움직일 수 있는 차들의 경우를 하드코딩하려다가 아닌거 같아서 도움 받음.
                1초마다 움직일 수 있는 차만 움직이는게 포인트였다. 나는 3개의 차가 있으면 curTime, curTime + 1, curTime + 2
                를 부여하려고 해서 복잡하게 생각했다.
 */
public class 교차로_박서희 {
    static int curTime = Integer.MAX_VALUE;

    static Queue<Car> queue0 = new LinkedList<>();
    static Queue<Car> queue1 = new LinkedList<>();
    static Queue<Car> queue2 = new LinkedList<>();
    static Queue<Car> queue3 = new LinkedList<>();


    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        int N = Integer.parseInt(br.readLine());

        ArrayList<Car> cars = new ArrayList<>();

        for (int i = 0; i < N; i++) {
            StringTokenizer st = new StringTokenizer(br.readLine());
            int arrivedTime = Integer.parseInt(st.nextToken());
            int road = st.nextToken().charAt(0) - 'A';
            curTime = Math.min(arrivedTime, curTime);

            Car car = new Car(arrivedTime, road);

            cars.add(car);
            if (road == 0) queue0.add(car);
            if (road == 1) queue1.add(car);
            if (road == 2) queue2.add(car);
            if (road == 3) queue3.add(car);
        }

        solution();

        StringBuilder answer = new StringBuilder();
        for (Car car : cars) {
            answer.append(car.leaveTime).append("\n");
        }
        System.out.println(answer);
    }

    static void solution() {
        Queue<Car>[] queues = new Queue[]{queue0, queue1, queue2, queue3};

        while (!queue0.isEmpty() || !queue1.isEmpty() || !queue2.isEmpty() || !queue3.isEmpty()) {
            boolean[] hasCar = new boolean[4];
            int waitingCount = 0;

            for (int i = 0; i < 4; i++) {
                if (!queues[i].isEmpty() && queues[i].peek().arrivedTime <= curTime) {
                    hasCar[i] = true;
                    waitingCount++;
                }
            }

            if (waitingCount == 0) {
                int nextTime = Integer.MAX_VALUE;
                for (int i = 0; i < 4; i++) {
                    if (!queues[i].isEmpty()) {
                        nextTime = Math.min(nextTime, queues[i].peek().arrivedTime);
                    }
                }
                curTime = nextTime;
                continue;
            }

            if (waitingCount == 4) {
                break;
            }


            boolean[] canGo = new boolean[4];
            for (int i = 0; i < 4; i++) {
                if (hasCar[i]) {
                    int rightRoad = (i + 3) % 4;

                    if (!hasCar[rightRoad]) {
                        canGo[i] = true;
                    }
                }
            }

            for (int i = 0; i < 4; i++) {
                if (canGo[i]) {
                    Car passedCar = queues[i].poll();
                    passedCar.leaveTime = curTime;
                }
            }
            curTime++;
        }
    }

    static class Car {
        int arrivedTime;
        int road;
        int leaveTime;

        public Car(int arrivedTime, int road) {
            this.arrivedTime = arrivedTime;
            this.road = road;
            this.leaveTime = -1;
        }
    }
}
