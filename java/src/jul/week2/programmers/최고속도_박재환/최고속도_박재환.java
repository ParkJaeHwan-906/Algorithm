package jul.week2.programmers.최고속도_박재환;

import java.util.*;

/**
 * AI 사용 여부 O
 * => 할말하않..;;
 */
public class 최고속도_박재환 {
    public static void main(String[] args) {
        int[][] city = {
                {-1, 3},
                {7, 3},
                {1, -1},
                {-2, 6}
        };

        int[][] road = {
                {-1, 7, 7, 7, 80},
                {-3, 3, 9, 3, 45},
                {-2, -4, -2, 6, 60},
                {1, -4, 1, 8, 50},
                {5, 1, 5, 7, 70}
        };

        Solution solution = new Solution();
        System.out.println(Arrays.toString(solution.solution(city, road)));
    }
}

class Solution {
    static final int INF = Integer.MAX_VALUE;

    class Loc {
        int x, y;

        Loc(int x, int y) {
            this.x = x;
            this.y = y;
        }

        public boolean equals(Object obj) {
            if (this == obj) return true;
            if (!(obj instanceof Loc)) return false;

            Loc other = (Loc) obj;
            return this.x == other.x && this.y == other.y;
        }

        public int hashCode() {
            return Objects.hash(this.x, this.y);
        }
    }

    class Road {
        int x1, y1;
        int x2, y2;
        int speed;

        Set<Loc> locs;      // 양 끝점, 카메라, 도시, 교차점

        Road(int x1, int y1, int x2, int y2, int speed) {
            this.x1 = x1;
            this.y1 = y1;
            this.x2 = x2;
            this.y2 = y2;
            this.speed = speed;

            this.locs = new HashSet<>();
        }

        boolean isHorizon() {       // 수평인지 확인
            return this.y1 == this.y2;
        }

        Loc getCamera() {
            return new Loc(
                    x1 + (x2 - x1) / 2,
                    y1 + (y2 - y1) / 2
            );
        }

        boolean isContain(Loc loc) {
            return loc.x >= this.x1 && loc.x <= this.x2
                    && loc.y >= this.y1 && loc.y <= this.y2;
        }
    }

    class Edge {
        int to;
        int speed;

        Edge(int to, int speed) {
            this.to = to;
            this.speed = speed;
        }
    }

    class State {
        int node;
        int speed;

        State(int node, int speed) {
            this.node = node;
            this.speed = speed;
        }
    }

    /**
     * [도로 구성요소]
     * - 교차점
     * - 끝점
     * - 카메라
     * - 도시
     */
    int cityCount;
    int roadCount;
    int nodeCount;
    Road[] roads;
    Loc[] cities;
    List<Edge>[] graph;
    Map<Loc, Integer> speedLimit;
    Map<Loc, Integer> nodeToId;
    public int[] solution(int[][] city, int[][] road) {
        set(city, road);

        int[] speeds = findMaxSpeed(nodeToId.get(cities[0]));
        int[] result = new int[cityCount - 1];
        for(int i = 1; i < cityCount; i++) {
            int cid = nodeToId.get(cities[i]);
            if(speeds[cid] == INF) result[i - 1] = 0;
            else result[i - 1] = speeds[cid];
        }
        return result;
    }

    int[] findMaxSpeed(int s) {
        int[] speeds = new int[graph.length];
        Arrays.fill(speeds, -1);
        PriorityQueue<State> pq = new PriorityQueue<>((a, b) -> Integer.compare(b.speed, a.speed));
        speeds[s] = INF;
        pq.offer(new State(s, INF));
        while(!pq.isEmpty()) {
            State cur = pq.poll();
            if(cur.speed < speeds[cur.node]) continue;
            for(Edge e : graph[cur.node]) {
                int nSpeed = Math.min(e.speed, cur.speed);
                if(speeds[e.to] < nSpeed) {
                    speeds[e.to] = nSpeed;
                    pq.offer(new State(e.to, speeds[e.to]));
                }
            }
        }
        return speeds;
    }

    void set(int[][] city, int[][] road) {
        this.cityCount = city.length;
        this.roadCount = road.length;

        this.roads = new Road[roadCount];
        this.speedLimit = new HashMap<>();

        for(int i = 0; i < roadCount; i++) {
            roads[i] = new Road(road[i][0], road[i][1], road[i][2], road[i][3], road[i][4]);
            Loc s = new Loc(roads[i].x1, roads[i].y1);
            Loc e = new Loc(roads[i].x2, roads[i].y2);
            Loc cam = roads[i].getCamera();

            roads[i].locs.add(s);
            roads[i].locs.add(e);
            roads[i].locs.add(cam);

            speedLimit.merge(cam, roads[i].speed, Math::min);
        }

        cities = new Loc[cityCount];
        for(int i = 0; i < cityCount; i++) {
            cities[i] = new Loc(city[i][0], city[i][1]);

            for(Road r : roads) {       // 도로에 포함되는 지점이라면
                if(r.isContain(cities[i])) r.locs.add(cities[i]);
            }
        }

        for(int i = 0; i < roadCount; i++) {
            for(int j = i + 1; j < roadCount; j++) {
                Loc cross = isCross(roads[i], roads[j]);
                if(cross == null) continue;
                roads[i].locs.add(cross);
                roads[j].locs.add(cross);
            }
        }

        nodeToId = new HashMap<>();
        List<Loc> allLocs = new ArrayList<>();
        for(Road r : roads) {
            for(Loc loc : r.locs) {
                if(nodeToId.containsKey(loc)) continue;
                int id = allLocs.size();
                nodeToId.put(loc, id);
                allLocs.add(loc);
            }
        }

        nodeCount = allLocs.size();
        graph = new List[nodeCount];
        for(int i = 0; i < nodeCount;) graph[i++] = new ArrayList<>();
        for(Road r : roads) {
            List<Loc> temp = new ArrayList<>(r.locs);
            if (r.isHorizon()) temp.sort((a, b) -> Integer.compare(a.x, b.x));
            else temp.sort((a, b) -> Integer.compare(a.y, b.y));

            for(int i = 0; i + 1 < temp.size(); i++) {
                Loc fromLoc = temp.get(i);
                Loc toLoc = temp.get(i + 1);

                int from = nodeToId.get(fromLoc);
                int to = nodeToId.get(toLoc);

                int fromSpeed = speedLimit.getOrDefault(fromLoc, INF);
                int toSpeed = speedLimit.getOrDefault(toLoc, INF);
                int min = Math.min(fromSpeed, toSpeed);
                graph[from].add(new Edge(to, min));
                graph[to].add(new Edge(from, min));
            }
        }
    }

    Loc isCross(Road a, Road b) {
        boolean isHorizonA = a.isHorizon();
        boolean isHorizonB = b.isHorizon();
        // 수평 도로와 수직 도로
        if (isHorizonA != isHorizonB) {
            Road horizontal = isHorizonA ? a : b;
            Road vertical = isHorizonA ? b : a;

            int x = vertical.x1;
            int y = horizontal.y1;

            if (horizontal.x1 <= x && x <= horizontal.x2
                    && vertical.y1 <= y && y <= vertical.y2) {
                return new Loc(x, y);
            }

            return null;
        }

        // 둘 다 수평
        if (isHorizonA) {
            if (a.y1 != b.y1) {
                return null;
            }

            int left = Math.max(a.x1, b.x1);
            int right = Math.min(a.x2, b.x2);

            if (left == right) {
                return new Loc(left, a.y1);
            }

            return null;
        }

        // 둘 다 수직
        if (a.x1 != b.x1) {
            return null;
        }

        int bottom = Math.max(a.y1, b.y1);
        int top = Math.min(a.y2, b.y2);

        if (bottom == top) {
            return new Loc(a.x1, bottom);
        }

        return null;
    }
}