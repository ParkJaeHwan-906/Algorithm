package dataStructure;

import java.io.*;

public class 싱글링크드리스트_박재환 {

    static final int ADD = 0;
    static final int DELETE = 1;
    static final int QUERY = 2;

    static class Node {
        int value;
        Node next;
        Node(int value) {
            this.value = value;
            this.next = null;
        }
    }

    static class SingleLinkedList {
        Node head;
        Node tail;
        int size;
        SingleLinkedList() {
            this.head = null;
            this.tail = null;
            this.size = 0;
        }

        void add(int value) {
            Node node = new Node(value);
            if(this.head == null && this.tail == null) {
                this.head = node;
                this.tail = node;
            } else {
                this.tail.next = node;
                this.tail = node;
            }
            this.size++;
        }

        void delete(int id) {
            if (this.size == 0) {
                System.out.println("List is empty");
                return;
            }
            if (id < 0 || id >= this.size) {
                System.out.println("Index Out of Bound");
                return;
            }

            Node deleted;
            if (id == 0) {
                deleted = this.head;
                this.head = this.head.next;

                if (this.head == null) {
                    this.tail = null;
                }
            } else {
                Node prev = this.head;
                for (int i = 0; i < id - 1; i++) {
                    prev = prev.next;
                }

                deleted = prev.next;
                prev.next = deleted.next;

                if (deleted == this.tail) {
                    this.tail = prev;
                }
            }

            deleted.next = null;
            this.size--;
            System.out.println(deleted.value);
        }

        void query(int id) {
            if (this.size == 0) {
                System.out.println("List is empty");
                return;
            }
            if (id < 0 || id >= this.size) {
                System.out.println("Index Out of Bound");
                return;
            }
            Node node = this.head;
            for (int i = 0; i < id; i++) {
                node = node.next;
            }
            System.out.println(node.value);
        }
    }

    static SingleLinkedList singleLinkedList = new SingleLinkedList();
    public static void main(String[] args) {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        System.out.println("SingleLinked List");
        while (true) {
            try {
                System.out.print("[TYPE] 0: ADD, 1: DELETE, 2: QUERY (빈 줄: 종료) : ");
                String typeInput = br.readLine();
                if (typeInput == null || typeInput.isBlank()) {
                    break;
                }

                int type = Integer.parseInt(typeInput.trim());
                if(type == ADD) {
                    add(br);
                } else if(type == DELETE) {
                    delete(br);
                } else if(type == QUERY) {
                    query(br);
                }
            } catch (IOException | NumberFormatException e) {
                break;
            }
        }
    }

    static void add(BufferedReader br) throws IOException {
        System.out.print("[ADD] : ");
        int value = Integer.parseInt(br.readLine().trim());
        singleLinkedList.add(value);
    }

    static void delete(BufferedReader br) throws IOException {
        System.out.print("[DELETE] : ");
        int id = Integer.parseInt(br.readLine().trim());
        singleLinkedList.delete(id);
    }

    static void query(BufferedReader br) throws IOException {
        System.out.print("[QUERY] : ");
        int id = Integer.parseInt(br.readLine().trim());
        singleLinkedList.query(id);
    }
}
