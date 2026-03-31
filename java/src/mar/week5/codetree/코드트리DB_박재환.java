package mar.week5.codetree;

import java.util.*;
import java.io.*;

public class 코드트리DB_박재환 {
	static BufferedReader br;
	static StringBuilder sb;
	public static void main(String[] args) throws IOException {
		br = new BufferedReader(new InputStreamReader(System.in));
		sb = new StringBuilder();
		init();
		br.close();
		System.out.println(sb);
	}
	static final int SET = 100;
	static final int ADD = 200;
	static final int DEL = 300;
	static final int RANK = 400;
	static final int SUM = 500;
	
	static class Command {
		int type;
		
		String name;
		
		int value;
		int rank;
		
		int sum;
		
		Command(int type) {		// SET
			this.type = type;
		}
		Command(int type, String name, int value) {		// ADD
			this.type = type;
			this.name = name;
			this.value = value;
		}
		Command(int type, String name) {		// DEL
			this.type = type;
			this.name = name;
		}
		Command(int type, int n) {			// RANK, SUM
			this.type = type;
			if(type == RANK) this.rank = n;
			else this.sum = n;
		}
	}
	
	static class Data {
		String name;
		int value;
		
		Data(String name, int value) {
			this.name = name;
			this.value = value;
		}
	}
	
	static StringTokenizer st;
	static Queue<Command> cmds;
	static List<Data> datas;
	static void init() throws IOException {
		cmds = new ArrayDeque<>();
		datas = new ArrayList<>();
		
		int q = Integer.parseInt(br.readLine().trim());
		while(q-- > 0) {
			st = new StringTokenizer(br.readLine().trim());
			String type = st.nextToken();
			
			if(type.equals("init")) {
				// 이전까지 쌓인 명령어 실행
				processCmds();
				// 초기화
				reset();
			}
			else if(type.equals("insert")) {
				String name = st.nextToken();
				int value = Integer.parseInt(st.nextToken());
				datas.add(new Data(name, value));
				cmds.offer(new Command(ADD, name, value));
			}
			else if(type.equals("delete")) {
				String name = st.nextToken();
				cmds.offer(new Command(DEL, name));
			}
			else if(type.equals("rank")) {
				int n = Integer.parseInt(st.nextToken());
				cmds.offer(new Command(RANK, n));
			}
			else if(type.equals("sum")) {
				int n = Integer.parseInt(st.nextToken());
				cmds.offer(new Command(SUM, n));
			}
		}
		
		processCmds();
	}	
	
	static void reset() {
		cmds.clear();
		datas.clear();
	}
	
	static int[] rankTree;
	static long[] sumTree;
	static Map<String, Integer> curNames;
	static Map<Integer, String> curIdToName;
	static Map<Integer, String> curValues;
	static void processCmds() {
		curNames = new HashMap<>();
		curIdToName = new HashMap<>();
		curValues = new HashMap<>();

		// 1. 배열 평탄화 
		flatData();
		
		rankTree = new int[4 * order];		// 순위 기록
		sumTree = new long[4 * order];		// 구간 합 기록
		
		while(!cmds.isEmpty()) {
			Command cmd = cmds.poll();
			if(cmd.type == ADD) {
				int result = add(cmd.name, cmd.value);
				sb.append(result).append('\n');
			}
			else if(cmd.type == DEL) {
				int result = del(cmd.name);
				sb.append(result).append('\n');
			}
			else if(cmd.type == RANK) {
				String result = rank(cmd.rank);
				sb.append(result).append('\n');
			}
			else if(cmd.type == SUM) {
				long result = sum(cmd.sum);
				sb.append(result).append('\n');
			}
//			System.out.println(Arrays.toString(rankTree));
//			System.out.println(Arrays.toString(sumTree));
		}
		
	}
	static int order;
	static Map<Integer, Integer> valueToId;
	static Map<Integer, Integer> idToValue;
	static void flatData() {
		order = 0;
		valueToId = new HashMap<Integer, Integer>();
		idToValue = new HashMap<Integer, Integer>();
	
		datas.sort((a, b) -> Integer.compare(a.value, b.value));		// 가격 순 오름차순
		
		for(Data d : datas) {
			if(valueToId.get(d.value) == null) {
				valueToId.put(d.value, ++order);
				idToValue.put(order, d.value);
			} else {
				int id = valueToId.get(d.value);
				valueToId.put(d.value, id);
				idToValue.put(id, d.value);
			}
		}
	}
	
	static int add(String name, int value) {
		// 1. 삽입이 가능한지 확인
		if(curNames.containsKey(name) || curValues.containsKey(value)) return 0;
		// 삽입이 가능한 상태 -> 트리 갱신 
		int id = valueToId.get(value);      // value 를 기준으로 평탄화 했으므로, value 기준 조회
		// rankTree
		updateRankTree(1, 1, order, id, id, 1);
		// sumTree
		updateSumTree(1, 1, order, id, id, value);
		
		curNames.put(name, value);
		curValues.put(value, name);
		curIdToName.put(id, name);
		return 1;
	}
	
	static int del(String name) {
		if(!curNames.containsKey(name)) return 0;
		
		int value = curNames.get(name);
        int id = valueToId.get(value);

		// rankTree
		updateRankTree(1, 1, order, id, id, 0);
		// sumTree
		updateSumTree(1, 1, order, id, id, 0);
		
		curNames.remove(name);
		curValues.remove(value);
		curIdToName.remove(id);
		return value;
	}
	
	static String rank(int rank) {
		if(order == 0 || rankTree[1] < rank) {
			return "None";
		}
		int tId = queryRankTree(1, 1, order, rank);
		return curIdToName.get(tId);
	}
	
	static long sum(int n) {
        if(order == 0) return 0;
		int e = findUppderId(n);
		return querySumTree(1, 1, order, 1, e - 1);
	}
	static int findUppderId(int n) {
		int l = 1, r = order + 1;
	
		while(l < r) {
			int mid = l + (r - l) / 2;
			if(idToValue.get(mid) > n) r = mid;
			else l = mid + 1;
		}
		return l;
	}
	
	static void updateRankTree(int id, int l, int r, int s, int e, int v) {
		if(r < s || l > e) return;
		if(l >= s && r <= e) {
			rankTree[id] = v;
			return;
		}
		int mid = l + (r - l) / 2;
		updateRankTree(2 * id, l, mid, s, e, v);
		updateRankTree(2 * id + 1, mid + 1, r, s, e, v);
		rankTree[id] = rankTree[2 * id]  + rankTree[2 * id + 1];
	}
	static void updateSumTree(int id, int l, int r, int s, int e, int v) {
		if(r < s || l > e) return;
		if(l >= s && r <= e) {
			sumTree[id] = v;
			return;
		}
		int mid = l + (r - l) / 2;
		updateSumTree(2 * id, l, mid, s, e, v);
		updateSumTree(2 * id + 1, mid + 1, r, s, e, v);
		sumTree[id] = sumTree[2 * id]  + sumTree[2 * id + 1];
	}
	
	static int queryRankTree(int id, int l, int r, int v) {
		if(l == r) return l;
		int mid = l + (r - l) / 2;
		if(rankTree[2 * id] >= v) {
			return queryRankTree(2 * id, l, mid, v);
		} else {
			return queryRankTree(2 * id + 1, mid + 1, r, v - rankTree[2 * id]);
		}
	}
	static long querySumTree(int id, int l, int r, int s, int e) {
		if(r < s || l > e) return 0;
		if(l >= s && r <= e) {
			return sumTree[id];
		}
		int mid = l + (r - l) / 2;
		return querySumTree(2 * id, l, mid, s, e) + querySumTree(2 * id + 1, mid + 1, r, s, e);
	}
}
