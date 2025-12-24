import sys
input = sys.stdin.readline
n, m = map(int, input().split())
arr = [list(map(int, input().split())) for _ in range(n)]
cheese_count = sum(row.count(1) for row in arr)

dx = [0,1,0,-1]
dy = [1,0,-1,0]
def solution():
    global arr, cheese_count
    time = 0
    # 1. 외부 영역 체크
    check_outer_area()

    while cheese_count > 0:
        time += 1
        cheese_candidates = []
        for x in range(n):
            for y in range(m):
                if(arr[x][y] != 1):
                    continue
                # 치즈인경우, 4방향을 탐색
                if check_all_way(x, y) > 1:
                    cheese_candidates.append((x, y))
        for x, y in cheese_candidates:
            arr[x][y] = -1
            cheese_count -= 1
            expand_outer_area(x, y)
    return time

def check_all_way(x, y):
    area_count = 0
    for dir in range(4):
        nx = x + dx[dir]
        ny = y + dy[dir]
        if nx < 0 or ny < 0 or nx >= n or ny >= m:
            continue
        if arr[nx][ny] == -1:
            area_count += 1
    return area_count

def check_outer_area():
    global arr, cheese_count
    from collections import deque
    q = deque()
    q.append((0,0))

    while q:
        x, y = q.popleft()
        for dir in range(4):
            nx = x + dx[dir]
            ny = y + dy[dir]
            if nx < 0 or ny < 0 or nx >= n or ny >= m:
                continue
            if arr[nx][ny] == 0:
                arr[nx][ny] = -1
                q.append((nx, ny))

def expand_outer_area(x, y):
    global arr
    from collections import deque
    q = deque()
    q.append((x, y))

    while q:
        x, y = q.popleft()
        for dir in range(4):
            nx = x + dx[dir]
            ny = y + dy[dir]
            if nx < 0 or ny < 0 or nx >= n or ny >= m:
                continue
            if arr[nx][ny] == 0:
                arr[nx][ny] = -1
                q.append((nx, ny))

if __name__ == "__main__":
    print(solution())