import sys
input = sys.stdin.readline
sys.setrecursionlimit(10**6)

def solution():
    n, k = map(int, input().split())
    arr = list(map(int, input().split()))

    tree = [0 for _ in range(4 * n)]

    def build(id, l, r):
        if l == r:
            tree[id] = arr[l]
            return

        mid = (l + r) // 2
        build(2 * id, l, mid)
        build(2 * id + 1, mid + 1, r)
        tree[id] = tree[2 * id] + tree[2 * id + 1]

    def query(id, l, r, s, e):
        if r < s or l > e:
            return 0

        if s <= l and e >= r:
            return tree[id];

        mid = (l + r) // 2
        left = query(2 * id, l, mid, s, e)
        right = query(2 * id + 1, mid + 1, r, s, e)
        return left + right

    build(1, 0, n - 1)

    for _ in range(k):
        s, e = map(int, input().split())
        s -= 1
        e -= 1
        print(f"{query(1, 0, n - 1, s, e) / (e - s + 1):.2f}")

if __name__ == '__main__':
    solution()