package nov.week2.programmers.순위검색_박재환;

import java.util.*;

public class 순위검색_박재환 {
    public static void main(String[] args) {
        String[] info = {
                "java backend junior pizza 150",
                "python frontend senior chicken 210",
                "python frontend senior chicken 150",
                "cpp backend senior pizza 260",
                "java backend junior chicken 80",
                "python backend senior chicken 50"
        };
        String[] query = {
                "java and backend and junior and pizza 100",
                "python and frontend and senior and chicken 200",
                "cpp and - and senior and pizza 250",
                "- and backend and senior and - 150",
                "- and - and - and chicken 100",
                "- and - and - and - 150"
        };

        Solution solution = new Solution();
        System.out.println(Arrays.toString(solution.solution(info, query)));
    }
}


class Solution {

    static final int CPP = 1;
    static final int JAVA = 2;
    static final int PYTHON = 3;

    static final int BACKEND = 10;
    static final int FRONTEND = 20;

    static final int JUNIOR = 100;
    static final int SENIOR = 200;

    static final int CHICKEN = 1000;
    static final int PIZZA = 2000;

    static final int WILDCARD = -1;

    class Category {
        int language;
        int occupation;
        int history;
        int food;

        Category(String language, String occupation, String history, String food) {
            this.language = convertLanguage(language);
            this.occupation = convertOccupation(occupation);
            this.history = convertHistory(history);
            this.food = convertFood(food);
        }

        int convertLanguage(String language) {
            if(language.equals("cpp"))
                return CPP;
            if(language.equals("java"))
                return JAVA;
            if(language.equals("python"))
                return PYTHON;
            return WILDCARD;
        }

        int convertOccupation(String occupation) {
            if(occupation.equals("backend"))
                return BACKEND;
            if(occupation.equals("frontend"))
                return FRONTEND;
            return WILDCARD;
        }

        int convertHistory(String history) {
            if(history.equals("junior"))
                return JUNIOR;
            if(history.equals("senior"))
                return SENIOR;
            return WILDCARD;
        }

        int convertFood(String food) {
            if(food.equals("chicken"))
                return CHICKEN;
            if(food.equals("pizza"))
                return PIZZA;
            return WILDCARD;
        }

        @Override
        public boolean equals(Object o) {
            if(this == o)
                return true;
            if(!(o instanceof Category))
                return false;
            Category category = (Category) o;
            return language == category.language
                    && occupation == category.occupation
                    && history == category.history
                    && food == category.food;
        }

        @Override
        public int hashCode() {
            return Objects.hash(language, occupation, history, food);
        }
    }

    Map<Category, List<Integer>> participants;
    public int[] solution(String[] info, String[] query) {
        set(info);
        int id = 0;
        int[] result = new int[query.length];
        for(String line : query) {
            String[] arr = line.split(" and ");
            String language = arr[0];
            String occupation = arr[1];
            String history = arr[2];
            String food = arr[3].split(" ")[0];
            int score = Integer.parseInt(arr[3].split(" ")[1]);
            List<Integer> list = participants.get(new Category(language, occupation, history, food));
            result[id++] = list == null ? 0 : findRange(list, score);
        }
        return result;
    }

    int findRange(List<Integer> list, int score) {
        int l = 0, r = list.size();
        while(l < r) {
            int mid = l + (r - l) / 2;
            if(list.get(mid) >= score)
                r = mid;
            else
                l = mid + 1;
        }
        return list.size() - l;
    }

    void set(String[] info) {
        participants = new HashMap<>();
        for(String line : info) {
            String[] arr = line.split(" ");
            String language = arr[0];
            String occupation = arr[1];
            String history = arr[2];
            String food = arr[3];
            int score = Integer.parseInt(arr[4]);
            participants.computeIfAbsent(
                    new Category(language, occupation, history, food),
                    k -> new ArrayList<>())
                    .add(score);
            participants.computeIfAbsent(
                            new Category("-", occupation, history, food),
                            k -> new ArrayList<>())
                    .add(score);
            participants.computeIfAbsent(
                            new Category("-", "-", history, food),
                            k -> new ArrayList<>())
                    .add(score);
            participants.computeIfAbsent(
                            new Category("-", "-", "-", food),
                            k -> new ArrayList<>())
                    .add(score);
            participants.computeIfAbsent(
                            new Category("-", "-", history, "-"),
                            k -> new ArrayList<>())
                    .add(score);
            participants.computeIfAbsent(
                            new Category("-", occupation, "-", food),
                            k -> new ArrayList<>())
                    .add(score);
            participants.computeIfAbsent(
                            new Category("-", occupation, "-", "-"),
                            k -> new ArrayList<>())
                    .add(score);
            participants.computeIfAbsent(
                            new Category("-", occupation, history, "-"),
                            k -> new ArrayList<>())
                    .add(score);
            participants.computeIfAbsent(
                            new Category(language, "-", history, food),
                            k -> new ArrayList<>())
                    .add(score);
            participants.computeIfAbsent(
                            new Category(language, "-", "-", "-"),
                            k -> new ArrayList<>())
                    .add(score);
            participants.computeIfAbsent(
                            new Category(language, "-", "-", food),
                            k -> new ArrayList<>())
                    .add(score);
            participants.computeIfAbsent(
                            new Category(language, "-", history, "-"),
                            k -> new ArrayList<>())
                    .add(score);
            participants.computeIfAbsent(
                            new Category(language, occupation, "-", food),
                            k -> new ArrayList<>())
                    .add(score);
            participants.computeIfAbsent(
                            new Category(language, occupation, "-", "-"),
                            k -> new ArrayList<>())
                    .add(score);
            participants.computeIfAbsent(
                            new Category(language, occupation, history, "-"),
                            k -> new ArrayList<>())
                    .add(score);
            participants.computeIfAbsent(
                            new Category("-", "-", "-", "-"),
                            k -> new ArrayList<>())
                    .add(score);
        }

        for(List<Integer> list : participants.values())
            list.sort(Integer :: compare);
    }
}
