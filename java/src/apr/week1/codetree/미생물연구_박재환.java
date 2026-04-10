package apr.week1.codetree;

import java.util.*;
import java.io.*;

public class 미생물연구_박재환 {
	static BufferedReader br;
	static StringBuilder sb;

	public static void main(String[] args) throws IOException {
		br = new BufferedReader(new InputStreamReader(System.in));
		sb = new StringBuilder();
		init();
		br.close();
		System.out.println(sb);
	}

	static class Command {
		int x1, y1; // 좌하단
		int x2, y2; // 우상단

		Command(int x1, int y1, int x2, int y2) {
			this.x1 = x1;
			this.y1 = y1;
			this.x2 = x2;
			this.y2 = y2;
		}
	}

	static StringTokenizer st;
	static int n, q;
	static Queue<Command> cmds;

	static void init() throws IOException {
		st = new StringTokenizer(br.readLine().trim());
		n = Integer.parseInt(st.nextToken());
		q = Integer.parseInt(st.nextToken());

		cmds = new ArrayDeque<>();
		for (int i = 0; i < q; i++) {
			st = new StringTokenizer(br.readLine().trim());
			// 0 - based
			int x1 = Integer.parseInt(st.nextToken());
			int y1 = Integer.parseInt(st.nextToken());
			int x2 = Integer.parseInt(st.nextToken());
			int y2 = Integer.parseInt(st.nextToken());

			Command cmd = new Command(x1, y1, x2, y2);
			cmds.offer(cmd);
		}

		solution();
	}

	static int id;
	static int[][] board;
	static int[] groups;
	static PriorityQueue<Group> groupLocs;
	static Map<Integer, List<int[]>> afterMoved;

	static void solution() {
		id = 0;
		board = new int[n][n];
		while (!cmds.isEmpty()) {
			Command cmd = cmds.poll();
			input(cmd);
			removeDivided();
			move();
			long score = getScore();
			sb.append(score).append('\n');
		}
	}

	static void input(Command cmd) {
		/**
		 * board 에 미생물을 투입한다.
		 */
		++id; // 현재 투입될 id 번호
		for (int x = cmd.x1; x < cmd.x2; x++) {
			for (int y = cmd.y1; y < cmd.y2; y++) {
				board[x][y] = id;
			}
		}
	}

	static class Group implements Comparable<Group> {
		int gId;
		List<int[]> list;

		Group(int gId, List<int[]> list) {
			this.gId = gId;
			this.list = list;
		}

		public int compareTo(Group o) {
			if (this.list.size() != o.list.size())
				return Integer.compare(o.list.size(), this.list.size());
			return Integer.compare(this.gId, o.gId);
		}
	}

	static void removeDivided() {
		/**
		 * 둘 이상으로 나누어진 매생물 그룹을 제거
		 */
		groups = new int[id + 1];
		groupLocs = new PriorityQueue<>();
		boolean[][] visited = new boolean[n][n];

		for (int x = 0; x < n; x++) {
			for (int y = 0; y < n; y++) {
				if (board[x][y] == 0 || visited[x][y])
					continue;

				int key = board[x][y];
				List<int[]> locs = findSameGroup(x, y, visited);
				groups[key]++;
				groupLocs.offer(new Group(key, locs));
			}
		}
	}

	static final int[] dx = { 0, 1, 0, -1 };
	static final int[] dy = { 1, 0, -1, 0 };

	static List<int[]> findSameGroup(int x, int y, boolean[][] visited) {
		Queue<int[]> q = new ArrayDeque<>();
		List<int[]> list = new ArrayList<>();
		q.offer(new int[] { x, y });
		visited[x][y] = true;

		int origin = board[x][y];
		while (!q.isEmpty()) {
			int[] cur = q.poll();
			list.add(cur);
			for (int dir = 0; dir < 4; dir++) {
				int nx = cur[0] + dx[dir];
				int ny = cur[1] + dy[dir];
				if (isNotBoard(nx, ny))
					continue;
				if (visited[nx][ny])
					continue;
				if (board[nx][ny] != origin)
					continue;

				visited[nx][ny] = true;
				q.offer(new int[] { nx, ny });
			}
		}
		return list;
	}

	static void move() {
		afterMoved = new HashMap<>();
		int[][] temp = new int[n][n];

		while (!groupLocs.isEmpty()) {
			Group group = groupLocs.poll();
			if (groups[group.gId] != 1)
				continue;

			int[] points = getStandardLoc(group); // 좌하단좌표
			int minX = points[0];
			int minY = points[1];

			int[] moveLoc = moveLoc(group, minX, minY, temp); // 이동할 범위
			if (moveLoc == null)
				continue; // 이동할 수 없는 구룹
			moveGroup(group, moveLoc[0], moveLoc[1], temp);
		}
		board = temp; // 새로운 보드로 갱신
	}

	static int[] getStandardLoc(Group group) {
		/**
		 * 기준점이 될 좌표 값을 찾음
		 */
		int minX = n + 1;
		int minY = n + 1;
		for (int[] loc : group.list) {
			int x = loc[0];
			int y = loc[1];

			minX = Math.min(x, minX);
			minY = Math.min(y, minY);

		}
		return new int[] { minX, minY };
	}

	static int[] moveLoc(Group group, int minX, int minY, int[][] temp) {
		for (int x = 0; x < n; x++) {
			for (int y = 0; y < n; y++) {
				// 현재 좌표를 기준으로 놓을 수 있는지 확인
				int diffX = minX - x;
				int diffY = minY - y;
				if (!canPut(group, diffX, diffY, temp))
					continue;

				// 이동 가능
				return new int[] { diffX, diffY };
			}
		}
		return null;
	}

	static void moveGroup(Group group, int diffX, int diffY, int[][] temp) {
		List<int[]> moved = new ArrayList<>();
		for (int[] loc : group.list) {
			int x = loc[0];
			int y = loc[1];

			int nx = x - diffX;
			int ny = y - diffY;
			temp[nx][ny] = group.gId;
			moved.add(new int[] { nx, ny });
		}
		afterMoved.put(group.gId, moved);
	}

	static long getScore() {
		long score = 0;
		boolean[][] checked = new boolean[id + 1][id + 1];

		for (Map.Entry<Integer, List<int[]>> entry : afterMoved.entrySet()) {
			int originGid = entry.getKey();
			List<int[]> list = entry.getValue();
			for (int[] loc : list) {
				for (int dir = 0; dir < 4; dir++) {
					int nx = loc[0] + dx[dir];
					int ny = loc[1] + dy[dir];
					if (isNotBoard(nx, ny))
						continue;
					if (board[nx][ny] == 0)
						continue;
					if (board[nx][ny] == originGid)
						continue;

					int otherGid = board[nx][ny];
					if (checked[originGid][otherGid] || checked[otherGid][originGid])
						continue;
					checked[originGid][otherGid] = true;
					checked[otherGid][originGid] = true;
					score += (afterMoved.get(originGid).size() * afterMoved.get(otherGid).size());
				}
			}
		}
		return score;
	}

	// ===
	static boolean isNotBoard(int x, int y) {
		return x < 0 || y < 0 || x >= n || y >= n;
	}

	static boolean canPut(Group group, int diffX, int diffY, int[][] temp) {
		for (int[] loc : group.list) {
			int x = loc[0];
			int y = loc[1];

			int nx = x - diffX;
			int ny = y - diffY;
			if (isNotBoard(nx, ny))
				return false;
			if (temp[nx][ny] != 0)
				return false;
		}
		return true;
	}

	// ===
	static void printBoard() {
		for (int[] arr : board)
			System.out.println(Arrays.toString(arr));
		System.out.println();
	}
}
