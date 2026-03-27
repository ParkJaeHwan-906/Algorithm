package mar.week4.boj;

import java.util.*;
import java.io.*;

public class 디스크트리_박재환 {
    static BufferedReader br;
    static StringBuilder sb;
    public static void main(String[] args) throws IOException {
        br = new BufferedReader(new InputStreamReader(System.in));
        sb = new StringBuilder();
        init();
        br.close();
        System.out.println(sb);
    }
    static StringTokenizer st;
    static int n;
    static void init() throws IOException {
        n = Integer.parseInt(br.readLine().trim());

        Trie trie = new Trie();
        while(n-- > 0) {
            String line = br.readLine().trim();
            trie.add(line);
        }

        query(trie.root, 0);
    }
    static class Trie {
        Node root;

        Trie() {
            root = new Node();
        }

        void add(String s) {
            String[] arr = s.split("\\\\");

            Node cur = this.root;
            for(String path : arr) {
                if(cur.childs.get(path) == null) cur.childs.put(path, new Node());
                cur = cur.childs.get(path);
            }
        }
    }
    static class Node {
        TreeMap<String, Node> childs;

        Node() {
            childs = new TreeMap<>(String::compareTo);
        }
    }

    static void query(Node cur, int depth) {
        for(String s : cur.childs.keySet()) {
            for(int i=0; i<depth; i++) {
                sb.append(' ');
            }
            sb.append(s).append('\n');

            query(cur.childs.get(s), depth+1);
        }
    }
}
