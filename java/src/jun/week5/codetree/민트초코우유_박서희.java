package jun.week5.codetree;

import java.util.*;
import java.io.*;


public class 민트초코우유_박서희 {

    static final int[] dx = {-1, 1, 0, 0};
    static final int[] dy = {0, 0, -1, 1};

    static int N;
    static ArrayList<Student> representatives = new ArrayList<>();
    static Student[][] students;

    public static void main(String[] args) throws IOException {

        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer st = new StringTokenizer(br.readLine());

        N = Integer.parseInt(st.nextToken());
        int T = Integer.parseInt(st.nextToken());

        students = new Student[N][N];
        for (int i = 0; i < N; i++) {

            String s = br.readLine();
            for (int j = 0; j < N; j++) {

                char favor = s.charAt(j);  // T 민트 C 초코 M 우유
                int idx = 0;
                if (favor == 'C') idx = 0;
                else if (favor == 'M') idx = 1;
                else idx = 2;

                students[i][j] = new Student(idx, i, j);

            }
        }

        for (int i = 0; i < N; i++) {
            st = new StringTokenizer(br.readLine());
            for (int j = 0; j < N; j++) {
                students[i][j].B = Integer.parseInt(st.nextToken());
            }
        }

        while (T-- > 0) {
            representatives.clear();
            morning();
            lunch();
            dinner();
            calculate();
        }
    }

    public static void morning() {
        for (int i = 0; i < N; i++)
            for (int j = 0; j < N; j++)
                students[i][j].B++;
    }


    public static void lunch() {
        int[][] visited = new int[N][N];
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                if (visited[i][j] == 1) continue;
                bfs(i, j, visited);
            }
        }
    }


    public static void dinner() {

        Collections.sort(representatives, favoriteComparator);
        for (Student rep : representatives) {
            if (rep.defense > 0) continue;
            int curB = rep.B;
            int cr = rep.r, cc = rep.c, cf = rep.favorite;
            int x = curB - 1;
            int d = curB % 4;
            rep.B = 1;

            while (x > 0) {
                cr += dx[d];
                cc += dy[d];
                if (!inRange(cr, cc)) break;
                if (cf == students[cr][cc].favorite) continue;
                int y = students[cr][cc].B;
                if (x > y) {
                    x -= (y + 1);
                    students[cr][cc].B++;
                    students[cr][cc].defense = 1;
                    students[cr][cc].favorite = cf;
                    if (x == 0) break;
                } else {
                    students[cr][cc].favorite |= cf;
                    students[cr][cc].B += x;
                    students[cr][cc].defense = 1;
                    break;
                }
            }
        }
    }


    public static void calculate() {
        int[] score = new int[8];
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                score[students[i][j].favorite] += students[i][j].B;
                if (students[i][j].defense > 0) students[i][j].defense--;
            }
        }

        StringBuilder sb = new StringBuilder();
        // 민트초코우유, 민트초코, 민트우유, 초코우유, 우유, 초코, 민트
        sb.append(score[7]).append(" ").append(score[5]).append(" ").append(score[6]).append(" ")
                .append(score[3]).append(" ").append(score[2]).append(" ").append(score[1]).append(" ").append(score[4]);
        System.out.println(sb);
    }


    private static void bfs(int i, int j, int[][] visited) {
        PriorityQueue<Student> group = new PriorityQueue<>();
        Queue<int[]> queue = new LinkedList<>();
        group.add(students[i][j]);
        queue.add(new int[]{i, j});
        visited[i][j] = 1;

        while (!queue.isEmpty()) {
            int[] cur = queue.poll();
            int x = cur[0], y = cur[1];
            int f = students[x][y].favorite;

            for (int d = 0; d < 4; d++) {
                int nx = x + dx[d], ny = y + dy[d];
                if (!inRange(nx, ny)) continue;
                int nf = students[nx][ny].favorite;
                if (visited[nx][ny] == 1) continue;
                if (f != nf) continue;
                queue.add(new int[]{nx, ny});
                visited[nx][ny] = 1;
                group.add(students[nx][ny]);
            }
        }

        Student rep = group.poll();
        rep.B += group.size();
        representatives.add(rep);
        while (!group.isEmpty()) {
            Student temp = group.poll();
            temp.B--;
        }

    }


    private static int score(int favorite) {
        if (favorite == 1 || favorite == 2 || favorite == 4)
            return 0;

        if (favorite == 3 || favorite == 6 || favorite == 5)
            return 1;

        return 2;
    }


    private static boolean inRange(int x, int y) {
        return 0 <= x && x < N && 0 <= y && y < N;
    }


    static class Student implements Comparable<Student> {
        int favorite = 0; // 민트, 초코, 우유 비트마스킹으로 관리
        int B = 0;
        int r, c;
        int defense = 0;

        public Student(int fIdx, int r, int c) {
            favorite = 1 << fIdx;
            this.r = r;
            this.c = c;
        }

        @Override
        public int compareTo(Student o) {
            if (this.B != o.B) return Integer.compare(o.B, this.B);
            if (this.r != o.r) return Integer.compare(this.r, o.r);
            return Integer.compare(this.c, o.c);
        }
    }


    static Comparator<Student> favoriteComparator = (a, b) -> {
        int sa = score(a.favorite);
        int sb = score(b.favorite);

        if (sa != sb) return Integer.compare(sa, sb);
        return a.compareTo(b);
    };

}
