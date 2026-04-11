package apr.week1.etc;

import java.util.Arrays;
import java.util.Scanner;

public class 직사각형배열회전_박재환 {
	static int n, m;
	public static void main(String[] args) {
		Scanner sc = new Scanner(System.in);
		n = sc.nextInt();
		m = sc.nextInt();
		int[][] arr = new int[n][m];
		for(int x = 0; x < n; x++) {
			for(int y = 0; y < m; y++) {
				arr[x][y] = sc.nextInt();
			}
		}
		
		System.out.println("[원본]");
		printBoard(arr);
		
		System.out.println("[90도]");
		rotate90(arr);
		
		System.out.println("[180도]");
		rotate180(arr);
		
		System.out.println("[270도]");
		rotate270(arr);
	}
	
	/**
	 * 90도 회전
	 */
	static void rotate90(int[][] arr) {
		int[][] temp = new int[m][n];
		for(int x = 0; x < n; x++) {
			for(int y = 0; y < m; y++) {
				temp[y][n - x - 1] = arr[x][y];
			}
		}
		printBoard(temp);
	}
	/**
	 * 180도 회전
	 */
	static void rotate180(int[][] arr) {
		int[][] temp = new int[n][m];
		for(int x = 0; x < n; x++) {
			for(int y = 0; y < m; y++) {
				temp[n - x - 1][m - y - 1] = arr[x][y];
			}
		}
		printBoard(temp);
	}
	/**
	 * 270도 회전
	 */
	static void rotate270(int[][] arr) {
		int[][] temp = new int[m][n];
		for(int x = 0; x < n; x++) {
			for(int y = 0; y < m; y++) {
				temp[m - y - 1][x] = arr[x][y];
			}
		}
		printBoard(temp);
	}
	
	// ===
	static void printBoard(int[][] arr) {
		for(int[] a : arr) System.out.println(Arrays.toString(a));
		System.out.println();
	}
}
