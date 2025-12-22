import sys
input = sys.stdin.readline

MOD = 1_000_000_007
n, m, k = map(int, input().split())
num_arr = list(int(input()) for _ in range(n))
segment_tree = list([0]*(4*n))
total_command_count = m+k

def solution():
    global total_command_count

    init_segment_tree(0, n-1, 1)
    for _ in range(total_command_count):
        a, b, c = map(int, input().split())

        match a:
            case 1:
                b-=1
                update_segment_tree(0, n-1, 1, b, c)
            case 2:
                b-=1
                c-=1
                print(query(0, n-1, 1, b, c))


def init_segment_tree(start, end, segment_idx):
    global segment_tree

    if start == end:
        segment_tree[segment_idx] = num_arr[start]
        return segment_tree[segment_idx]

    mid = start + (end-start)//2
    left = init_segment_tree(start, mid, segment_idx*2)
    right = init_segment_tree(mid+1, end, segment_idx*2+1)
    segment_tree[segment_idx] = (left*right)%MOD
    return segment_tree[segment_idx]

def update_segment_tree(start, end, segment_idx, target_idx, value):
    global segment_tree

    # 변경하고자하는 target_idx 가, start ~ end 사이에 속하지 않는 경우
    if start > target_idx or end < target_idx:
        return segment_tree[segment_idx]
    # 리프 노드인 경우
    if start == end:
        segment_tree[segment_idx] = value
        return segment_tree[segment_idx]

    mid = start + (end-start)//2
    left = update_segment_tree(start, mid, segment_idx*2, target_idx, value)
    right = update_segment_tree(mid+1, end, segment_idx*2+1, target_idx, value)
    segment_tree[segment_idx] = (left*right)%MOD
    return segment_tree[segment_idx]

def query(start, end, segment_idx, left, right):
    if start > right or end < left:     # 범위를 완전하게 벗어나는 경우
        return 1
    if left <= start and end <= right:
        return segment_tree[segment_idx]

    mid = start + (end-start)//2
    l = query(start, mid, segment_idx*2, left, right)
    r = query(mid+1, end, segment_idx*2+1, left, right)
    return (l*r)%MOD

if __name__ == "__main__":
    solution()