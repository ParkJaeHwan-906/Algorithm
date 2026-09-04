package aug.week5.codetree;

import java.util.*;
import java.io.*;

public class 해적선장코디_박재환 {
    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        init(br);
        br.close();
    }

    static final int READY = 100;
    static final int HELP = 200;
    static final int CHANGE = 300;
    static final int ATTACK = 400;

    static class Ship implements Comparable<Ship> {
        int id;
        int p;
        int r;
        int availableTime;
        Ship(int id, int p, int r, int availableTime) {
            this.id = id;
            this.p = p;
            this.r = r;
            this.availableTime = availableTime;
        }
        @Override
        public int compareTo(Ship o) {
            if(this.p != o.p) return Integer.compare(o.p, this.p);
            return Integer.compare(this.id, o.id);
        }
    }

    static int n;
    static Map<Integer, Ship> ships;
    static PriorityQueue<Ship> availableShips;
    static PriorityQueue<Ship> waitShips;
    static void init(BufferedReader br) throws IOException {
        StringTokenizer st;
        StringBuilder sb = new StringBuilder();
        int t = Integer.parseInt(br.readLine().trim());
        int time = 0;
        while(t-- > 0) {
            st = new StringTokenizer(br.readLine().trim());
            int type = Integer.parseInt(st.nextToken());
            if(type == READY) { ready(st); }
            else if(type == HELP) { help(st); }
            else if(type == CHANGE) { change(st); }
            else if(type == ATTACK) { attack(sb, time); }
            time++;
        }
        System.out.println(sb);
    }

    static void ready(StringTokenizer st) {
        n = Integer.parseInt(st.nextToken());
        ships = new HashMap<>();
        waitShips = new PriorityQueue<>((a, b) -> Integer.compare(a.availableTime, b.availableTime));
        availableShips = new PriorityQueue<>();
        for(int i = 0; i < n; i++) {
            int id = Integer.parseInt(st.nextToken());
            int p = Integer.parseInt(st.nextToken());
            int r = Integer.parseInt(st.nextToken());
            ships.put(id, new Ship(id, p, r, -1));
            availableShips.offer(ships.get(id));
        }
    }

    static void help(StringTokenizer st) {
        int id = Integer.parseInt(st.nextToken());
        int p = Integer.parseInt(st.nextToken());
        int r = Integer.parseInt(st.nextToken());
        ships.put(id, new Ship(id, p, r, -1));
        waitShips.offer(ships.get(id));
    }

    static void change(StringTokenizer st) {
        int id = Integer.parseInt(st.nextToken());
        int p = Integer.parseInt(st.nextToken());
        Ship ship = ships.get(id);
        if(p == ship.p) {
            return;
        }
        Ship newShip = new Ship(id, p, ship.r, ship.availableTime);
        ships.put(id, newShip);
        availableShips.offer(newShip);
    }

    static void attack(StringBuilder sb, int time) {
        // 출력 : (총 피해량, 사격 선박 수, 선박 id 들)
        int totalDamage = 0;
        int shipCount = 0;
        StringBuilder shipSb = new StringBuilder();
        while(!waitShips.isEmpty() && waitShips.peek().availableTime <= time) {
            availableShips.offer(waitShips.poll());
        }
        while(!availableShips.isEmpty() && shipCount < 5) {
            Ship ship = availableShips.poll();
            if(ship != ships.get(ship.id)) {
                continue;
            }
            totalDamage += ship.p;
            shipCount++;
            ship.availableTime = time + ship.r;
            shipSb.append(ship.id).append(' ');
            waitShips.offer(ship);
        }

        sb.append(totalDamage).append(' ').append(shipCount).append(' ').append(shipSb).append('\n');
    }
}
