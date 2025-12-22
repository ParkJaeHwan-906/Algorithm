import sys
input = sys.stdin.readline

n, m = map(int, input().split())
arr = list(int(input()) for _ in range(n))
tree = list([None]*(4*n))

def solution():
    init(0, n-1, 1)
    for _ in range(m):
        a, b = map(int, input().split())
        a-=1
        b-=1
        result = query(0, n-1, 1, a, b)
        print(result["min"], result["max"])

def init(start, end, idx):
    if start == end:
        tree[idx] = {
            "min": arr[start],
            "max": arr[end]
        }
        return tree[idx]
    mid = start + (end-start)//2
    l = init(start, mid, idx*2)
    r = init(mid+1, end, idx*2+1)
    tree[idx] = {
        "min": min(l["min"], r["min"]),
        "max": max(l["max"], r["max"])
    }
    return tree[idx]

def query(start, end, idx, l, r):
    if start > r or end < l:
        return {
            "min": 1_000_000_001,
            "max": -1
        }

    if start >= l and r >= end:
        return tree[idx]

    mid = start + (end - start) // 2
    left = query(start, mid, idx * 2, l, r)
    right = query(mid + 1, end, idx * 2 + 1, l, r)
    return {
        "min": min(left["min"], right["min"]),
        "max": max(left["max"], right["max"])
    }

if __name__ == "__main__":
    solution()