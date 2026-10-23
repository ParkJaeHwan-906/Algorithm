
package oct.week3.swea;

import java.util.*;
import java.io.*;

public class 범준이의제주도여행계획_최적화_박재환 {
    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringBuilder result = new StringBuilder();
        int t = Integer.parseInt(br.readLine().trim());
        for(int i = 1; i <= t; i++) {
            result.append('#').append(i).append(' ')
                    .append(init(br)).append('\n');
        }
        System.out.print(result);
        br.close();
    }

    static final int MAX_TIME = 540;
    static final int INF = 1_000_000;

    static class Place {
        int id;
        char type;
        int spendTime;
        int satisfy;
        public Place(int id, char type, int spendTime, int satisfy) {
            this.id = id;
            this.type = type;
            this.spendTime = spendTime;
            this.satisfy = satisfy;
        }
    }

    static int n, m;
    static int airportId;
    static int[][] moveTime;
    static Place[] places;
    static Map<Character, List<Place>> typeToPlace;
    static int[] minHotelTime;
    static int[] minVisitTime;
    static Integer[] touristOrder;
    static String init(BufferedReader br) throws IOException {
        StringTokenizer st = new StringTokenizer(br.readLine().trim());
        n = Integer.parseInt(st.nextToken());
        m = Integer.parseInt(st.nextToken());
        moveTime = new int[n][n];
        for(int i = 0; i < n - 1; i++) {
            st = new StringTokenizer(br.readLine().trim());
            for(int j = i + 1; j < n; j++) {
                int cost = Integer.parseInt(st.nextToken());
                moveTime[i][j] = cost;
                moveTime[j][i] = cost;
            }
        }
        places = new Place[n];
        typeToPlace = new HashMap<>();
        for(int i = 0; i < n; i++) {
            st = new StringTokenizer(br.readLine().trim());
            char type = st.nextToken().charAt(0);
            Place place;
            if(type == 'P') {
                int spendTime = Integer.parseInt(st.nextToken());
                int satisfy = Integer.parseInt(st.nextToken());
                place = new Place(i, type, spendTime, satisfy);
            } else {
                place = new Place(i, type, 0, 0);
            }
            places[i] = place;
            typeToPlace.computeIfAbsent(type, k -> new ArrayList<>()).add(place);
        }
        return solution();
    }

    static int bestSatisfy;
    static List<Integer> bestRoute;
    static Map<Long, Integer> minTimeByState;
    static String solution() {
        bestSatisfy = 0;
        bestRoute = new ArrayList<>();
        minTimeByState = new HashMap<>();
        airportId = typeToPlace.get('A').get(0).id;
        minHotelTime = new int[n];
        minVisitTime = new int[n];
        Arrays.fill(minHotelTime, INF);
        Arrays.fill(minVisitTime, INF);
        List<Place> hotels = typeToPlace.getOrDefault('H', new ArrayList<>());
        for(int i = 0; i < n; i++) {
            for(Place hotel : hotels) {
                minHotelTime[i] = Math.min(
                        minHotelTime[i],
                        moveTime[i][hotel.id]
                );
            }
            for(int j = 0; j < n; j++) {
                if(i != j) {
                    minVisitTime[i] = Math.min(
                            minVisitTime[i],
                            moveTime[j][i]
                    );
                }
            }
        }
        List<Place> tourists = typeToPlace.getOrDefault('P', new ArrayList<>());
        touristOrder = new Integer[tourists.size()];
        for(int i = 0; i < tourists.size(); i++)
            touristOrder[i] = tourists.get(i).id;
        Arrays.sort(touristOrder, (a, b) -> {
            int costA = places[a].spendTime + minVisitTime[a];
            int costB = places[b].spendTime + minVisitTime[b];
            long left = (long) places[a].satisfy * costB;
            long right = (long) places[b].satisfy * costA;
            if(left != right)
                return Long.compare(right, left);
            return Integer.compare(a, b);
        });
        findBestRoute(
                1, airportId, 0, 0,
                new boolean[n], 0L, new ArrayList<>());
        return getResultStr();
    }

    static String getResultStr() {
        StringBuilder sb = new StringBuilder();
        sb.append(bestSatisfy);
        for(int i : bestRoute)
            sb.append(' ').append(i + 1);
        return sb.toString();
    }

    static void findBestRoute(int curDay, int curLoc, int time, int score, boolean[] visited, long visitedMask, List<Integer> curRoute) {
        int endTime = (curDay == m)
                ? moveTime[curLoc][airportId]
                : minHotelTime[curLoc];
        if(time + endTime > MAX_TIME)
            return;
        long stateKey = visitedMask
                | ((long) curLoc << n)
                | ((long) curDay << (n + 6));
        Integer oldTime = minTimeByState.get(stateKey);
        if(oldTime != null && oldTime <= time)
            return;
        minTimeByState.put(stateKey, time);
        if(cannotBeatBest(curDay, time, score, visited))
            return;
        for(int nextLoc : touristOrder) {
            if(visited[nextLoc])
                continue;
            int nextTime = time
                    + moveTime[curLoc][nextLoc]
                    + places[nextLoc].spendTime;
            int minEndTime = (curDay == m)
                    ? moveTime[nextLoc][airportId]
                    : minHotelTime[nextLoc];
            if(nextTime + minEndTime > MAX_TIME)
                continue;
            visited[nextLoc] = true;
            curRoute.add(nextLoc);
            findBestRoute(curDay, nextLoc, nextTime, score + places[nextLoc].satisfy, visited, visitedMask | (1L << nextLoc), curRoute);
            curRoute.remove(curRoute.size() - 1);
            visited[nextLoc] = false;
        }
        if(curDay < m) {
            for(Place hotel : typeToPlace.getOrDefault('H', new ArrayList<>())) {
                int hotelId = hotel.id;
                if(time + moveTime[curLoc][hotelId] > MAX_TIME)
                    continue;
                curRoute.add(hotelId);
                findBestRoute(curDay + 1, hotelId, 0, score, visited, visitedMask, curRoute);
                curRoute.remove(curRoute.size() - 1);
            }
        } else {
            if(time + moveTime[curLoc][airportId] <= MAX_TIME
                    && score > bestSatisfy) {
                curRoute.add(airportId);
                bestSatisfy = score;
                bestRoute = new ArrayList<>(curRoute);
                curRoute.remove(curRoute.size() - 1);
            }
        }
    }

    static boolean cannotBeatBest(int curDay, int time, int score, boolean[] visited) {
        int remainingTime = MAX_TIME - time + (m - curDay) * MAX_TIME;
        long optimisticScore = score;
        for(int loc : touristOrder) {
            if(visited[loc])
                continue;
            int minCost = places[loc].spendTime
                    + minVisitTime[loc];
            if(minCost <= remainingTime) {
                optimisticScore += places[loc].satisfy;
                remainingTime -= minCost;
            } else {
                if(minCost > 0) {
                    optimisticScore += (
                            (long) places[loc].satisfy * remainingTime
                                    + minCost - 1
                    ) / minCost;
                }
                break;
            }
        }
        return optimisticScore <= bestSatisfy;
    }
}
