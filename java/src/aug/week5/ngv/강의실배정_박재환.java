package aug.week5.ngv;

import java.util.*;
import java.io.*;

public class 강의실배정_박재환 {
    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        init(br);
        br.close();
    }

    static class Course implements Comparable<Course> {
        int start, end;
        Course (int start, int end) {
            this.start = start;
            this.end = end;
        }
        @Override
        public int compareTo(Course o) {
            return Integer.compare(this.end, o.end);
        }
    }
    static int n;
    static Course[] courses;
    static void init(BufferedReader br) throws IOException {
        StringTokenizer st;

        n = Integer.parseInt(br.readLine().trim());
        courses = new Course[n];
        for(int i = 0; i < n; i++) {
            st = new StringTokenizer(br.readLine().trim());
            int start = Integer.parseInt(st.nextToken());
            int end = Integer.parseInt(st.nextToken());
            courses[i] = new Course(start, end);
        }

        System.out.println(solution());
    }

    static int solution() {
        Arrays.sort(courses);

        int count = 1;
        int lastTime = courses[0].end;
        for(int i = 1; i < n; i++) {
            if(lastTime <= courses[i].start) {
                count++;
                lastTime = courses[i].end;
            }
        }
        return count;
    }
}
