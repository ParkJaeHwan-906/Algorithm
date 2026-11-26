package dataStructure;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.StringTokenizer;

public class 동적배열_박재환 {

    static final int APPEND = 0;
    static final int DELETE = 1;
    static final int LOOKUP = 2;

    static class DynamicArray {
        int[] arr;
        int pointer;
        public DynamicArray() {
            arr = new int[10];
            pointer = 0;
        }

        void append(int id, int value) {
            if (id == -1) id = pointer;
            if (id < 0 || id > pointer) {
                System.out.println("Index Exception");
                return;
            }
            resize();
            for (int i = pointer; i > id; i--) {
                arr[i] = arr[i - 1];
            }
            arr[id] = value;
            pointer++;
        }

        void lookup(int id) {
            if(id < 0 || id >= pointer) {
                System.out.println("Index Out Of Bound");
                return;
            }
            System.out.println(arr[id]);
        }

        void delete(int id) {
            if (pointer == 0) {
                System.out.println("Index Out Of Bound");
                return;
            }
            if (id == -1) id = pointer - 1;
            if (id < 0 || id >= pointer) {
                System.out.println("Index Out Of Bound");
                return;
            }
            for (int i = id; i < pointer - 1; i++) {
                arr[i] = arr[i + 1];
            }
            arr[--pointer] = 0;
        }

        int size() {
            return pointer;
        }

        private void resize() {
            if(pointer == arr.length) {
                int[] newArr = new int[arr.length * 2];
                for(int i = 0; i < pointer; i++) {
                    newArr[i] = arr[i];
                }
                arr = newArr;
            }
        }
    }

    public static void main(String[] args) throws IOException {
        System.out.println("Dynamic Array");
        DynamicArray array = new DynamicArray();
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        while(true) {
            System.out.print("[TYPE] 0: ADD, 1: DELETE, 2: QUERY (빈 줄: 종료) : ");
            String typeInput = br.readLine();
            if (typeInput == null || typeInput.isBlank()) {
                break;
            }

            int type = Integer.parseInt(typeInput.trim());
            if(type == APPEND) {
                System.out.print("삽입 위치 (0~size, 맨 뒤는 -1): ");
                int index = Integer.parseInt(br.readLine().trim());
                System.out.print("값: ");
                int value = Integer.parseInt(br.readLine().trim());
                array.append(index, value);
            }
            else if(type == DELETE) {
                System.out.print("삭제 위치 (맨 뒤는 -1): ");
                array.delete(Integer.parseInt(br.readLine().trim()));
            } else if(type == LOOKUP) {
                System.out.print("조회 위치: ");
                array.lookup(Integer.parseInt(br.readLine().trim()));
            } else {
                System.out.println("지원하지 않는 유형입니다.");
            }
            System.out.println("현재 크기: " + array.size());
        }
    }
}
