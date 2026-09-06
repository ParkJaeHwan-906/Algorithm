package aug.week5.ngv;

import java.util.*;
import java.io.*;

public class 성적평가_박재환 {
    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        init(br);
        br.close();
    }

    static class Person implements Comparable<Person> {
        int id;
        int score;
        Person (int id, int score) {
            this.id = id;
            this.score = score;
        }
        @Override
        public int compareTo(Person o) {
            return Integer.compare(o.score, this.score);
        }
    }

    static int n;
    static void init(BufferedReader br) throws IOException {
        StringTokenizer st;
        StringBuilder sb = new StringBuilder();

        n = Integer.parseInt(br.readLine().trim());

        Person[] finalPersons = new Person[n];

        for (int i = 0; i < n; i++) {
            finalPersons[i] = new Person(i, 0);
        }

        // 3개의 대회
        for (int i = 0; i < 3; i++) {
            st = new StringTokenizer(br.readLine().trim());
            Person[] persons = new Person[n];
            for (int j = 0; j < n; j++) {
                int score = Integer.parseInt(st.nextToken());
                persons[j] = new Person(j, score);
                // 최종 점수 누적
                finalPersons[j].score += score;
            }
            solution(sb, persons);
        }

        // 최종 점수 순위 계산
        Arrays.sort(finalPersons);
        int[] rank = new int[n];
        for (int i = 0; i < n; i++) {
            if (i == 0) {
                rank[finalPersons[i].id] = 1;
                continue;
            }
            if (finalPersons[i - 1].score == finalPersons[i].score) {
                rank[finalPersons[i].id]
                        = rank[finalPersons[i - 1].id];
            } else {
                rank[finalPersons[i].id] = i + 1;
            }
        }

        for (int i = 0; i < n; i++) {
            sb.append(rank[i]).append(' ');
        }
        System.out.println(sb);
    }

    static void solution(StringBuilder sb, Person[] persons) {
        Arrays.sort(persons);
        int[] rank = new int[n];
        for (int i = 0; i < n; i++) {
            if (i == 0) {
                rank[persons[i].id] = 1;
                continue;
            }
            if (persons[i - 1].score == persons[i].score) {
                rank[persons[i].id]
                        = rank[persons[i - 1].id];
            } else {
                rank[persons[i].id] = i + 1;
            }
        }

        for (int i = 0; i < n; i++) {
            sb.append(rank[i]).append(' ');
        }
        sb.append('\n');
    }
}
