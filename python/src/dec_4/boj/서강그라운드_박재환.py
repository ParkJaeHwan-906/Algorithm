import sys
input = sys.stdin.readline

INF = 1_000_000_000

n, m, r = map(int, input().split())
items = [0] + list(map(int, input().split()))

dist = [[INF] * (n+1) for _ in range(n+1)]
for i in range(1, n+1):
    dist[i][i] = 0

for _ in range(r):
    a, b, w = map(int, input().split())
    dist[a][b] = w
    dist[b][a] = w

def get_all_dist():
    for mid in range(1, n+1):
        for start in range(1, n+1):
            if dist[start][mid] == INF:
                continue
            for end in range(1, n+1):
                if dist[start][mid] == INF or dist[mid][end] == INF:
                    continue
                dist[start][end] = min(dist[start][end], dist[start][mid] + dist[mid][end])

def get_max_item():
    get_all_dist()

    max_item = 0
    for start in range(1, n+1):
        item_count = 0
        for end in range(1, n+1):
            if dist[start][end] > m:
                continue
            item_count += items[end]
        max_item = max(max_item, item_count)

    return max_item

if __name__ == "__main__":
    print(get_max_item())
