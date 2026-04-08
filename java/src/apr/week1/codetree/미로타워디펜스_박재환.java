package apr.week1.codetree;

import java.util.*;
import java.io.*;

public class 미로타워디펜스_박재환 {
	static BufferedReader br;

	public static void main(String[] args) throws IOException {
		br = new BufferedReader(new InputStreamReader(System.in));
		init();
		br.close();
	}

	static final int[] dx = { 0, 1, 0, -1 };
	static final int[] dy = { 1, 0, -1, 0 };

	static class Attack {
		int d;
		int p;

		Attack(int d, int p) {
			this.d = d;
			this.p = p;
		}
	}

	static StringTokenizer st;
	static int n, m;
	static int[][] board;
	static Queue<Attack> attacks;

	static void init() throws IOException {
		st = new StringTokenizer(br.readLine().trim());
		n = Integer.parseInt(st.nextToken());
		m = Integer.parseInt(st.nextToken());

		board = new int[n][n];
		for (int x = 0; x < n; x++) {
			st = new StringTokenizer(br.readLine().trim());
			for (int y = 0; y < n; y++) {
				board[x][y] = Integer.parseInt(st.nextToken());
			}
		}

		attacks = new ArrayDeque<>();
		for (int i = 0; i < m; i++) {
			st = new StringTokenizer(br.readLine().trim());
			int d = Integer.parseInt(st.nextToken());
			int p = Integer.parseInt(st.nextToken());
			attacks.offer(new Attack(d, p));
		}
		System.out.println(solution());
	}

	static List<int[]> searchSeq;
	static int roundScore;

	static int solution() {
		int totalScore = 0;

		searchSeq = makeDirGuide();
		while (!attacks.isEmpty()) {
			roundScore = 0;
			Attack attack = attacks.poll();
			// 1. 연속적으로 존재해서 사라지는 몬스터가 있는지 확인
//			findSeqMonster();
			// 2. 플레이어 공격
			attackMonster(attack);

			// 3. 빈칸 채우기
			fillBlank();

			// 4. 연속적으로 존재해서 사라지는 몬스터가 있는지 확인
			findSeqMonster();
			// 5. 몬스터 재배치
			compress();
			totalScore += roundScore;
		}
		return totalScore;
	}

	static void attackMonster(Attack attack) {
		int x = n / 2, y = n / 2; // 타워는 항상 중심
		while (attack.p-- > 0) {
			int nx = x + dx[attack.d];
			int ny = y + dy[attack.d];
			if (isNotBoard(nx, ny))
				break;
			if (board[nx][ny] > 0)
				roundScore += board[nx][ny];
			board[nx][ny] = 0;
			x = nx;
			y = ny;
		}
	}

	static void findSeqMonster() {
		List<int[]> removed = new ArrayList<>();

		for (int i = 0; i < searchSeq.size(); i++) {
			int[] cur = searchSeq.get(i);
			if (board[cur[0]][cur[1]] == 0)
				continue;
			List<int[]> cand = new ArrayList<int[]>();
			for (int j = i; j < searchSeq.size(); j++) {
				int[] next = searchSeq.get(j);
				if (board[cur[0]][cur[1]] == board[next[0]][next[1]]) {
					cand.add(next);
					continue;
				}
				break;
			}
			// 4번 이상 연속된다면 삭제
			// 연속되는 위치 점프
			if (cand.size() >= 4) {
				removed.addAll(cand);
				i = i + cand.size() - 1;
			}
		}
		removeMonster(removed);
		fillBlank();

		if (!removed.isEmpty())
			findSeqMonster();
	}

	static void removeMonster(List<int[]> removed) {
		for (int[] loc : removed) {
			roundScore += board[loc[0]][loc[1]];
			board[loc[0]][loc[1]] = 0;
		}
	}

	static void fillBlank() {
		for (int i = 0; i < searchSeq.size(); i++) {
			int[] cur = searchSeq.get(i);
			if (board[cur[0]][cur[1]] > 0)
				continue; // 비어있지 않은 칸이라면
			boolean isLast = true;
			for (int j = i + 1; j < searchSeq.size(); j++) {
				int[] next = searchSeq.get(j);
				if (board[next[0]][next[1]] == 0)
					continue;
				int temp = board[next[0]][next[1]];
				board[cur[0]][cur[1]] = temp;
				board[next[0]][next[1]] = 0;
				isLast = false;
				break;
			}
			if (isLast)
				break;
		}
	}

	static class Group {
		int size;
		int num;

		Group(int num) {
			this.size = 0;
			this.num = num;
		}
	}

	static void compress() {
		Queue<Group> groups = new ArrayDeque<>();
		for (int i = 0; i < searchSeq.size(); i++) {
			int[] cur = searchSeq.get(i);
			if (board[cur[0]][cur[1]] == 0)
				continue;
			Group g = new Group(board[cur[0]][cur[1]]);
			int j = i;
			for (; j < searchSeq.size(); j++) {
				int[] next = searchSeq.get(j);
				if (board[cur[0]][cur[1]] == board[next[0]][next[1]]) {
					g.size++;
					continue;
				}
				break;
			}
			groups.offer(g);
			i = j - 1;
		}

		for (int x = 0; x < n; x++)
			Arrays.fill(board[x], 0);
		int id = 0;
		while (!groups.isEmpty()) {
			Group g = groups.poll();
			int size = g.size;
			int num = g.num;

			if (id >= searchSeq.size())
				break;
			int[] first = searchSeq.get(id++);
			board[first[0]][first[1]] = size;
			if (id >= searchSeq.size())
				break;
			int[] second = searchSeq.get(id++);
			board[second[0]][second[1]] = num;
		}
	}

	// ===
	static boolean isNotBoard(int x, int y) {
		return x < 0 || y < 0 || x >= n || y >= n;
	}

	static List<int[]> makeDirGuide() {
		List<int[]> list = new ArrayList<int[]>();
		boolean[][] visited = new boolean[n][n];
		int dir = 0;
		int x = 0, y = 0;
		while (true) {
			if (x == n / 2 && y == n / 2)
				break;
			list.add(new int[] { x, y });
			visited[x][y] = true;
			int nx = x + dx[dir];
			int ny = y + dy[dir];
			if (isNotBoard(nx, ny) || visited[nx][ny]) {
				dir = (dir + 1) % 4;
				nx = x + dx[dir];
				ny = y + dy[dir];
			}
			x = nx;
			y = ny;
		}
		/**
		 * 타워 위치를 제외 타워에서 가장 근접한 몬스터부터 순차적으로 위치 저장
		 */
		Collections.reverse(list);

		// ===
		// visibleVisitSeq(list);
		// ===
		return list;
	}
}
