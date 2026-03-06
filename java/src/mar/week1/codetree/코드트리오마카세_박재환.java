package mar.week1.codetree;

import java.util.*;
import java.io.*;

public class 코드트리오마카세_박재환 {
    static BufferedReader br;
    static StringBuilder sb;
    public static void main(String[] args) throws IOException {
        br = new BufferedReader(new InputStreamReader(System.in));
        sb = new StringBuilder();
        init();
        br.close();
        System.out.println(sb);
    }

    /**
     * 각 초밥에 사람 이름을 적어 회전하는 벨트 위에 올린다.
     *
     * 원형 형태의 벨트와 의자 L개
     * x=0 기준 -> 시계방향 회전
     *
     * 초밥은 각 의자 앞에 여러 개 놓일 수 있다.
     * 1초에 한 칸씩 시계방향으로 회전한다.
     *
     * [초밥 만들기]
     * t 시간에 x 앞에 있는 벨트 위에 name 을 부탁한 초밥을 하나 올려놓는다.
     * t시간에 초밥 회전이 일어난 직후 발생한다. 여러 초밥이 같은 위치에 올라갈 수 잇다.
     *
     * [손님]
     * name인 사람이 t시간에 x에 있는 의자로 가서 앉는다.
     * x앞으로 오는 초밥 중 자신의 초밥을 정확히 n개 먹는다.
     *
     * [사진]
     * t시간의 모습을 찍는다.
     * 1), 2)가 끝난 후 찍는다. -> 오마카세 집에 있는 사람 수와, 초밥 수를 출력한다.
     */

    static final int MAKE = 100;
    static final int COME = 200;
    static final int CAPTURE = 300;

    static StringTokenizer st;
    static int l;
    static Map<String, List<Food>> foods;
    static Map<String, Customer> customers;
    static PriorityQueue<Integer> removeTime;
    static Map<Integer, List<String>> waiting;
    static int customerCnt, foodsCnt;
    static void init() throws IOException {
        foods = new HashMap<>();
        customers = new HashMap<>();
        waiting = new HashMap<>();
        removeTime = new PriorityQueue<>(Integer::compare);

        st = new StringTokenizer(br.readLine().trim());
        l = Integer.parseInt(st.nextToken());

        int q = Integer.parseInt(st.nextToken());
        while(q-- > 0) {
            st = new StringTokenizer(br.readLine().trim());
            int cmd = Integer.parseInt(st.nextToken());
            int t = Integer.parseInt(st.nextToken());
            if(cmd == MAKE) {
                int x = Integer.parseInt(st.nextToken());
                String name = st.nextToken();
                make(t, x, name);
            }
            else if(cmd == COME) {
                int x = Integer.parseInt(st.nextToken());
                String name = st.nextToken();
                int n = Integer.parseInt(st.nextToken());
                come(t, x, name, n);
            }
            else if(cmd == CAPTURE) {
                picture(t);
                sb.append(customerCnt).append(' ').append(foodsCnt).append('\n');
            }
        }
    }
    static class Customer {
        int time;
        int x;
        int remain;

        Customer(int time, int x, int remain) {
             this.time = time;
             this.x = x;
             this.remain = remain;
        }
    }
    static class Food {
        int x;
        int inTime;
        int outTime;

        Food(int x, int inTime, int outTime) {
            this.x = x;
            this.inTime = inTime;
            this.outTime = outTime;
        }
    }
    static void make(int t, int x, String name) {
        // 현재 대기중인 손님의 음식일 경우
        Customer cs = customers.get(name);
        if(cs != null) {
            /**
             * 현재 대기중인 손님의 경우, 음식이 사라지는 시간을 알 수 있음
             */
            int remainTime = getDistance(cs.x, x);
            int eatableTime = remainTime + t;       // 손님이 실제 음식을 먹을 수 있는 시간
            registRemoveFood(eatableTime, name);
        } else {
            foods.computeIfAbsent(name, k -> new ArrayList<>()).add(new Food(x, t, 0));
        }
        foodsCnt++;
    }
    static int getDistance(int cs, int food) {
        /**
         * 손님과 음식간 거리
         */
        int dist = cs - food;
        if(dist < 0) dist += l;
        return dist;
    }
    static void registRemoveFood(int t, String name) {
        /**
         * t 시간에 음식을 먹을 수 있는 name 을 List 형태로 저장
         */
        waiting.computeIfAbsent(t, k -> {
            removeTime.offer(t);
            return new ArrayList<>();
        }).add(name);
    }
    static void come(int t, int x, String name, int n) {
        Customer cs = new Customer(t, x, n);
        customers.put(name, cs);
        customerCnt++;

        List<Food> customerFoods = foods.get(name);
        if(customerFoods != null) {     // 현재 대기중인 음식이 있다면
            for(Food food : customerFoods) {
                int curLoc = (food.x + (t-food.inTime))%l;
                int remainTime = getDistance(cs.x, curLoc);
                int eatableTime = remainTime + t;       // 손님이 실제 음식을 먹을 수 있는 시간
                registRemoveFood(eatableTime, name);
            }
            foods.remove(name);
        }
    }
    static void picture(int t) {
        while(!removeTime.isEmpty() && removeTime.peek() <= t) {
            int time = removeTime.poll();
            for(String name : waiting.get(time)) {
                /**
                 * time 에 음식을 먹을 수 있는 손님의 name
                 */
                Customer cs = customers.get(name);
                if(cs == null) continue;

                cs.remain--;
                foodsCnt--;

                if(cs.remain == 0) {
                    customers.remove(name);
                    customerCnt--;
                }
            }
        }
    }
}
