import sys
input = sys.stdin.readline

n = int(input())
arr = list(map(int, input().split()))

def solution():
    left = get_left_arr()
    right = get_right_arr()
    # print(left)
    # print(right)
    max_combi = -100000000
    for i in range(n-2):
        max_combi = max(max_combi, left[i] + right[i+2] )
    return max_combi

def get_left_arr():
    left = [0] * n
    local_ =  arr[0]
    left[0] = local_
    for i in range(1, n):
        local_ = max(arr[i], local_ + arr[i])
        left[i] = local_
    return left

def get_right_arr():
    right = [0] * n
    local_ = arr[-1]
    global_ = local_
    right[-1] = global_
    for i in range(n-2, -1, -1):
        local_ = max(arr[i], local_ + arr[i])
        global_ = max(global_, local_)
        right[i] = global_
    return right

if __name__ == "__main__":
    print(solution())