"""
[풀이 시간]
00:13:27
AI 사용 여부 O
: heapq 사용하는 법 헷갈림
"""
def solution(n, works):
    import heapq
    """
    야근 피로도 : 야근을 시작한 시점에 남은 일의 제곱

    1시간에 1만큼 처리 가능
    n : 퇴근까지 남은 시간
    works : 각 일에 대한 남은 작업량
    
    => 가장 큰 값부터 차례로 -1 ?
    """

    heap = []
    for i in works:
        heapq.heappush(heap, -i)        # 기본 정렬이 오름차순이므로 - 붙여서 음수로 우선 처리

    print(heap)


    while n > 0 and len(heap) > 0:
        target = heapq.heappop(heap)
        target += 1
        n -= 1
        if target == 0:
            continue
        heapq.heappush(heap, target)

    total = 0
    while len(heap) > 0:
        total += ((-heapq.heappop(heap)) ** 2)

    return total

if __name__ == '__main__':
    works = [4, 3, 3]
    n = 4
    print(solution(n, works))