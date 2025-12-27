import sys
import heapq
input = sys.stdin.readline


n, m = map(int, input().split())
pq = [];
for _ in range(m):
    from_, to_, cost_ = map(int, input().split())
    heapq.heappush(pq, (cost_, from_, to_))

def get_min_cost():
    parents = make()
    max_cost = 0
    total_cost = 0
    edge = 0
    while pq:
        cost_, from_, to_ = heapq.heappop(pq)

        if union(from_, to_, parents):
            total_cost += cost_
            max_cost = max(max_cost, cost_)
            edge += 1
            if edge == n-1:
                break

    return total_cost - max_cost


def make():
    parents = [0] * (n+1)
    for i in range(n+1):
        parents[i] = i
    return parents

def find(a, parents):
    if parents[a] == a:
        return parents[a]
    parents[a] = find(parents[a], parents)
    return parents[a]

def union(a, b, parents):
    root_a = find(a, parents)
    root_b = find(b, parents)

    if root_a == root_b:
        return False

    parents[root_b] = root_a
    return True

if __name__ == "__main__":
    print(get_min_cost())