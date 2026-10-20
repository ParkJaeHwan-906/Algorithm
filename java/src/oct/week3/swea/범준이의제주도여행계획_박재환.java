package oct.week3.swea;

import java.util.*;
import java.io.*;
import java.util.stream.Collectors;

public class 범준이의제주도여행계획_박재환 {
    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringBuilder result = new StringBuilder();
        int t = Integer.parseInt(br.readLine().trim());
        for(int i = 1; i <= t; i++) {
            result.append('#').append(i).append(' ').append(init(br)).append('\n');
        }
        System.out.print(result);
        br.close();
    }

    static final int MAX_TIME = 540;

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
    static List<int[]>[] connections;
    static Place[] places;
    static Map<Character, List<Place>> typeToPlace;
    static String init(BufferedReader br) throws IOException {
        StringTokenizer st;
        st = new StringTokenizer(br.readLine().trim());
        n = Integer.parseInt(st.nextToken());           // 장소 개수
        m = Integer.parseInt(st.nextToken());           // 요일
        connections = new List[n];
        for(int i = 0; i < n; i++)
            connections[i] = new ArrayList<>();
        for(int i = 0; i < n - 1; i++) {
            st = new StringTokenizer(br.readLine().trim());
            for(int j = i + 1; j < n; j++) {
                int cost = Integer.parseInt(st.nextToken());
                connections[i].add(new int[] {j, cost});
                connections[j].add(new int[] {i, cost});
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
    static String solution() {
        bestSatisfy = 0;
        bestRoute = new ArrayList<>();
        findBestRoute(1, typeToPlace.get('A').get(0).id, 0, 0, new boolean[n], new ArrayList<>());
        return getResultStr();
    }

    static String getResultStr() {
        StringBuilder sb = new StringBuilder();
        sb.append(bestSatisfy);
        for(int i : bestRoute)
            sb.append(' ').append(i + 1);
        return sb.toString();
    }

    static void findBestRoute(int curDay, int curLoc, int time, int score, boolean[] visited, List<Integer> curRoute) {
        for(int[] conn : connections[curLoc]) {
            if(visited[conn[0]])                    // 이미 방문한 경우
                continue;
            if(time + conn[1] + places[conn[0]].spendTime > MAX_TIME)       // 9시간을 초과하는 경우
                continue;
            if(places[conn[0]].type == 'P') {
                visited[conn[0]] = true;
                curRoute.add(conn[0]);
                findBestRoute(curDay, conn[0], time + conn[1] + places[conn[0]].spendTime, score + places[conn[0]].satisfy, visited, curRoute);
                curRoute.remove(curRoute.size() - 1);
                visited[conn[0]] = false;
            } else if(curDay < m && places[conn[0]].type == 'H') {
                curRoute.add(conn[0]);
                findBestRoute(curDay + 1, conn[0], 0, score + places[conn[0]].satisfy, visited, curRoute);
                curRoute.remove(curRoute.size() - 1);
            } else if(curDay == m && places[conn[0]].type == 'A') {
                curRoute.add(conn[0]);
                if(bestSatisfy < score) {
                    bestSatisfy = score;
                    bestRoute.clear();
                    for(int i : curRoute)
                        bestRoute.add(i);
                }
                curRoute.remove(curRoute.size() - 1);
            }
        }
    }
}
