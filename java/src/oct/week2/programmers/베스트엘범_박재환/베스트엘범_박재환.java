package oct.week2.programmers.베스트엘범_박재환;

import java.util.*;

public class 베스트엘범_박재환 {
    public static void main(String[] args) {
        String[] genres = {"classic", "pop", "classic", "classic", "pop"};
        int[] plays = {500, 600, 150, 800, 2500};
        Solution sol = new Solution();
        System.out.println(Arrays.toString(sol.solution(genres, plays)));
    }
}

class Solution {
    static class Music {
        int id;
        String genre;
        int plays;
        Music(int id, String genre, int plays) {
            this.id = id;
            this.genre = genre;
            this.plays = plays;
        }
    }
    static class Genre {
        String genre;
        int plays;
        Genre(String genre, int plays) {
            this.genre = genre;
            this.plays = plays;
        }
    }

    public int[] solution(String[] genres, int[] plays) {
        Map<String, Genre> genreMap = new HashMap<>();
        Map<String, List<Music>> musicMap = new HashMap<>();
        for(int i = 0; i < genres.length; i++) {
            if(!genreMap.containsKey(genres[i])) {
                genreMap.put(genres[i], genreMap.getOrDefault(genres[i], new Genre(genres[i], plays[i])));
            } else {
                Genre genre = genreMap.get(genres[i]);
                genre.plays += plays[i];
            }
            musicMap.computeIfAbsent(genres[i], k -> new ArrayList<>())
                    .add(new Music(i, genres[i], plays[i]));
        }
        List<Integer> result = new ArrayList<>();
        List<Genre> genreList = new ArrayList<>(genreMap.values());
        genreList.sort((a, b) -> Integer.compare(b.plays, a.plays));
        for(Genre genre : genreList) {
            List<Music> musicList = new ArrayList<>(musicMap.get(genre.genre));
            musicList.sort((a, b) -> {
                if(a.plays != b.plays) {
                    return Integer.compare(b.plays, a.plays);
                }
                return Integer.compare(a.id, b.id);
            });
            for(int i = 0; i < Math.min(2, musicList.size()); i++) {
                result.add(musicList.get(i).id);
            }
        }
        int[] arr = new int[result.size()];
        for(int i = 0; i < result.size(); i++) {
            arr[i] = result.get(i);
        }
        return arr;
    }
}