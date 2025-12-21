n, m, h = map(int, input().split())
conn_status = [list([0]*(n+1)) for _ in range(h+1)]  # 초기 0으로 초기화 
for i in range(m):
    row, col = map(int, input().split())
    # 좌 -> 우 연결은 1로 표기
    # 우 -> 좌 연결은 2로 표기
    conn_status[row][col] = 1
    conn_status[row][col+1] = 2
min_count = 4  # 새로 놓을 수 있는 가로선의 개수는 최대 3개S

def solution():
    # 새로 놓을 수 있는 가로선의 개수는 최대 3개
    for i in range(4):
        find_new_location(i, 1, 0)
        if(min_count != 4) :
            break

    print(-1 if min_count == 4 else min_count)

def find_new_location(target_count, row_idx, new_count):
    if new_count == target_count:
        # 요구하는 사다리 조건을 만족하는지
        if is_correct():
            global min_count
            min_count = min(min_count, new_count)
        return
    
    for row in range(row_idx, h+1):
        for col in range(1, n):
            if conn_status[row][col] == 0 and conn_status[row][col+1] == 0:
                # 가로선 설치
                conn_status[row][col] = 1
                conn_status[row][col+1] = 2

                find_new_location(target_count, row, new_count + 1)

                # 가로선 제거 (백트래킹)
                conn_status[row][col] = 0
                conn_status[row][col+1] = 0

def is_correct():
    for col in range(1, n+1):
        cur_col = col
        for row in range(1, h+1):
            if conn_status[row][cur_col] == 1:
                cur_col += 1
            elif conn_status[row][cur_col] == 2:
                cur_col -= 1
        if cur_col != col:
            return False
    return True

if __name__ == "__main__":
    solution()
