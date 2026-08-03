package jun.week4.codetree;

import java.io.*;
import java.util.*;

/*
  문제풀이 시간: X
  AI 사용 여부: O 풀이를 고민해봤는데 어떻게 풀어야 할지 모르겠어서 AI를 사용했다.
                코드트리 채점기 문제랑 이 문제가 유독 어렵게 느껴지는 것을 보면 시뮬레이션 문제에 특히 약한 것 같다.
                이 문제도 추후 다시 풀어봐야 할 듯..모든 events를 list에 저장해서 차례차례 파악하는 식으로 풀이.
 */
public class 코드트리오마카세_박서희 {
    static StringBuilder sb = new StringBuilder();
    static ArrayList<Event> events = new ArrayList<>();
    static HashMap<String, Person> people = new HashMap<>();
    static HashMap<String, ArrayList<Sushi>> waitingSushi = new HashMap<>();

    static int L;

    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer st = new StringTokenizer(br.readLine());

        L = Integer.parseInt(st.nextToken());
        int Q = Integer.parseInt(st.nextToken());

        while (Q-- > 0) {
            st = new StringTokenizer(br.readLine());
            int command = Integer.parseInt(st.nextToken());

            if (command == 100) {
                int t = Integer.parseInt(st.nextToken());
                int x = Integer.parseInt(st.nextToken());
                String name = st.nextToken();
                events.add(new Event(t, 100, x, name, 0));
                createSushi(t, x, name);
            }
            if (command == 200) {
                int t = Integer.parseInt(st.nextToken());
                int x = Integer.parseInt(st.nextToken());
                String name = st.nextToken();
                int n = Integer.parseInt(st.nextToken());
                events.add(new Event(t, 200, x, name, n));
                welcome(t, x, name, n);
            }
            if (command == 300) {
                int t = Integer.parseInt(st.nextToken());
                events.add(new Event(t, 300, 0, "", 0));
            }
        }

        Collections.sort(events);

        int currentSushi = 0;
        int currentPeople = 0;
        HashMap<String, Integer> eatenCount = new HashMap<>();

        for (Event e : events) {
            if (e.type == 100) {
                currentSushi++;
            }
            else if (e.type == 200) {
                currentPeople++;
            }
            else if (e.type == 110) { // 초밥 소멸
                currentSushi--;
                eatenCount.put(e.name, eatenCount.getOrDefault(e.name, 0) + 1);

                if (eatenCount.get(e.name) == people.get(e.name).n) {   // n개의 초밥 다 먹음.
                    currentPeople--;
                }
            }
            else if (e.type == 300) {
                sb.append(currentPeople).append(" ").append(currentSushi).append("\n");
            }
        }

        System.out.print(sb);
    }

    static void createSushi(int t, int x, String name) {
        if (people.containsKey(name)) {     // 초밥을 기다리는 손님이 와 있다면
            Person p = people.get(name);

            // 손님이 먼저 와서 기다리고 있던 상황 (t >= p.t)
            int dist = (p.x - x + L) % L;
            int tEat = t + dist;    // 먹힌 시간

            events.add(new Event(tEat, 110, 0, name, 0));
        }
        else { // 주인이 아직 안 왔다면 waitingSushi에 저장
            if (!waitingSushi.containsKey(name)) {
                waitingSushi.put(name, new ArrayList<>());
            }
            waitingSushi.get(name).add(new Sushi(t, x));
        }
    }

    static void welcome(int t, int x, String name, int n) {
        Person p = new Person(t, x, n);
        people.put(name, p);

        // 이 손님을 기다리던 초밥이 있었다면
        if (waitingSushi.containsKey(name)) {
            ArrayList<Sushi> sushis = waitingSushi.get(name);

            for (Sushi s : sushis) {
                int curSushiX = (s.x + (t - s.t)) % L;
                int dist = (x - curSushiX + L) % L;
                int tEat = t + dist;

                events.add(new Event(tEat, 110, 0, name, 0));
            }

            waitingSushi.remove(name);
        }
    }

    static class Event implements Comparable<Event> {
        int time, type, x;
        String name;
        int n;

        public Event(int time, int type, int x, String name, int n) {
            this.time = time;
            this.type = type;
            this.x = x;
            this.name = name;
            this.n = n;
        }

        @Override
        public int compareTo(Event o) {
            if (this.time != o.time) return Integer.compare(this.time, o.time);
            return Integer.compare(this.type, o.type); // 초밥 추가, 입장, 초밥 소멸이 사진 촬영보다 무조건 먼저 와야 함
        }
    }

    static class Person {
        int t, x, n;

        public Person(int t, int x, int n) {
            this.t = t;
            this.x = x;
            this.n = n;
        }
    }

    static class Sushi {
        int t, x;

        public Sushi(int t, int x) {
            this.t = t;
            this.x = x;
        }
    }
}
