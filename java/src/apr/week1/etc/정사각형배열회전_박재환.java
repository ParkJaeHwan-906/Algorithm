package apr.week1.etc;

import java.util.*;

public class 정사각형배열회전_박재환 {
	public static void main(String[] args) {
		Scanner sc = new Scanner(System.in);
		int n = sc.nextInt();
		int[][] arr = new int[n][n];
		for(int x = 0; x < n; x++) {
			for(int y = 0; y < n; y++) {
				arr[x][y] = sc.nextInt();
			}
		}
		
		System.out.println("[원본]");
		printBoard(arr);
		
		System.out.println("[90도]");
		rotate90(n, arr);
		
		System.out.println("[180도]");
		rotate180(n, arr);
		
		System.out.println("[270도]");
		rotate270(n, arr);
	}
	
	/**
	 * 90도 회전
	 */
	static void rotate90(int n, int[][] arr) {
		int[][] temp = new int[n][n];
		for(int x = 0; x < n; x++) {
			for(int y = 0; y < n; y++) {
				temp[y][n - x - 1] = arr[x][y];
			}
		}
		printBoard(temp);
	}
	/**
	 * 180도 회전
	 */
	static void rotate180(int n, int[][] arr) {
		int[][] temp = new int[n][n];
		for(int x = 0; x < n; x++) {
			for(int y = 0; y < n; y++) {
				temp[n - x - 1][n - y - 1] = arr[x][y];
			}
		}
		printBoard(temp);
	}
	/**
	 * 270도 회전
	 */
	static void rotate270(int n, int[][] arr) {
		int[][] temp = new int[n][n];
		for(int x = 0; x < n; x++) {
			for(int y = 0; y < n; y++) {
				temp[n - y - 1][x] = arr[x][y];
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
