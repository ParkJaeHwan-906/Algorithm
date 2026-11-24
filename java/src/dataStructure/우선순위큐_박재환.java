package dataStructure;

import java.io.*;

public class 우선순위큐_박재환 {

    static final int ADD = 1;
    static final int POLL = 2;
    static final int PEEK = 3;

    static class Heap {
        int[] elements;
        int size;
        public Heap() {
            this.elements = new int[10];
            this.size = 0;
        }

        void add(int value) {
            grow();
            this.elements[this.size] = value;
            shiftUp(this.size);
            this.size++;
        }

        void poll() {
            if(this.size == 0) {
                System.out.println("Queue is empty");
                return;
            }

            int value = this.elements[0];
            System.out.println(value);
            this.elements[0] = this.elements[--this.size];        // 마지막 원소를 루트로
            if (this.size > 0)
                shiftDown(0);
        }

        void peek() {
            if (this.size == 0) {
                System.out.println("Queue is empty");
                return;
            }
            System.out.println(this.elements[0]);
        }

        private void shiftDown(int i) {
            while(true) {
                int lId = 2 * i + 1;
                int rId = 2 * i + 2;
                int small = i;
                if(lId < this.size && this.elements[lId] < this.elements[small])
                    small = lId;
                if(rId < this.size && this.elements[rId] < this.elements[small])
                    small = rId;
                if(small == i)
                    break;
                swap(small, i);
                i = small;
            }
        }

        private void shiftUp(int i) {
            while (i > 0) {
                int pId = (i - 1) / 2;
                if(this.elements[pId] <= this.elements[i])
                    break;
                this.swap(pId, i);
                i = pId;
            }
        }

        private void swap(int i, int j) {
            int tmp = this.elements[i];
            this.elements[i] = this.elements[j];
            this.elements[j] = tmp;
        }

        private void grow() {
            if(this.size == this.elements.length) {
                int[] newArray = new int[this.elements.length * 2];
                for(int i = 0; i < this.elements.length; i++)
                    newArray[i] = this.elements[i];
                this.elements = newArray;
            }
        }
    }

    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        Heap queue = new Heap();
        System.out.println("PriorityQueue");
        while (true) {
            System.out.print("[TYPE] 1: ADD, 2: POLL, 3: PEEK (빈 줄: 종료) : ");
            String line = br.readLine();
            if (line == null || line.isBlank()) {
                break;
            }

            try {
                int type = Integer.parseInt(line.trim());
                if (type == ADD) {
                    System.out.print("[ADD] : ");
                    String valueLine = br.readLine();
                    if (valueLine == null) {
                        break;
                    }
                    queue.add(Integer.parseInt(valueLine.trim()));
                } else if (type == POLL) {
                    queue.poll();
                } else if (type == PEEK) {
                    queue.peek();
                } else {
                    System.out.println("Invalid type");
                }
            } catch (NumberFormatException e) {
                System.out.println("숫자를 입력해주세요.");
            }
        }
        br.close();
    }
}
