package nov.week3.programmers.카드유효성검사_박재환;

import java.util.*;

public class 카드유효성검사_박재환 {
    public static void main(String[] args) {
        Solution solution = new Solution();

        // TC1: 모든 조건 만족
        int[][] cardinfos1 = {
                {100100800, 625, 0},
                {100200700, 1226, 0}
        };
        System.out.println("TC1: " +
                Arrays.toString(solution.solution(cardinfos1)));
        // 예상: [1, 1]


        // TC2: 카드번호 각 자리 합이 10의 배수가 아님
        int[][] cardinfos2 = {
                {123456789, 625, 368},
                {111111118, 1227, 229}
        };
        System.out.println("TC2: " +
                Arrays.toString(solution.solution(cardinfos2)));
        // 예상: [0, 0]


        // TC3: 유효기간이 기준보다 이전
        int[][] cardinfos3 = {
                {100100800, 525, 0},
                {100200700, 1224, 0}
        };
        System.out.println("TC3: " +
                Arrays.toString(solution.solution(cardinfos3)));
        // 예상: [0, 0]


        // TC4: 유효기간 경계값
        int[][] cardinfos4 = {
                {100100800, 625, 0},
                {100200700, 725, 0},
                {100300600, 125, 0}
        };
        System.out.println("TC4: " +
                Arrays.toString(solution.solution(cardinfos4)));
        // 예상: [1, 1, 0]


        // TC5: CVC 일치 여부
        int[][] cardinfos5 = {
                {100100800, 1226, 0},   // 1000 % 1000 = 0
                {100200700, 1226, 123}, // CVC 불일치
                {100300600, 1226, 0}    // 1000 % 1000 = 0
        };
        System.out.println("TC5: " +
                Arrays.toString(solution.solution(cardinfos5)));
        // 예상: [1, 0, 1]


        // TC6: 카드번호 길이 검증
        int[][] cardinfos6 = {
                {12345678, 625, 702},
                {1234567, 625, 579}
        };
        System.out.println("TC6: " +
                Arrays.toString(solution.solution(cardinfos6)));
        // 예상: [0, 0]


        // TC7: CVC 경계값
        int[][] cardinfos7 = {
                {100100800, 625, 0},
                {100100800, 625, 1000},
                {100100800, 625, 999}
        };
        System.out.println("TC7: " +
                Arrays.toString(solution.solution(cardinfos7)));
        // 예상: [1, 0, 0]


        // TC8: 잘못된 유효기간
        int[][] cardinfos8 = {
                {100100800, 1326, 0},
                {100200700, 26, 0},
                {100300600, 1226, 0}
        };
        System.out.println("TC8: " +
                Arrays.toString(solution.solution(cardinfos8)));
        // 예상: [0, 0, 1]
    }
}

class Solution {
    public int[] solution(int[][] cardinfos) {
        int[] result = new int[cardinfos.length];
        for(int i = 0; i < cardinfos.length; i++) {
            if(checkNumber(cardinfos[i][0]) && checkDate(cardinfos[i][1]) && checkCvc(cardinfos[i][0], cardinfos[i][2]))
                result[i] = 1;
            else
                result[i] = 0;
        }
        return result;
    }

    boolean checkNumber(int numbers) {
        String str = String.valueOf(numbers);
        if(str.length() != 9)
            return false;
        int sum = 0;
        for(char c : str.toCharArray())
            sum += (c - '0');
        return sum % 10 == 0;
    }

    boolean checkDate(int date) {
        String str = String.valueOf(date);
        int month = -1;
        int year = -1;
        if(str.length() == 3) {
            month = Integer.parseInt(str.substring(0, 1));
            year = Integer.parseInt(str.substring(1));
        } else if(str.length() == 4) {
            month = Integer.parseInt(str.substring(0, 2));
            year = Integer.parseInt(str.substring(2));
        }

        if(year > 25 && (1 <= month && month <= 12))
            return true;
        return year > 25 || (year == 25 && month >= 6);
    }

    boolean checkCvc(int number, int cvc) {
        String str = String.valueOf(number);
        int sum = Integer.parseInt(str.substring(0, 3))
                + Integer.parseInt(str.substring(3, 6))
                + Integer.parseInt(str.substring(6));
        return (sum % 1000) == cvc;
    }
}