import sys
input = sys.stdin.readline

def solution():
    max_sub_arr = max(sequence_arr[i]["max_sub_arr"] for i in range(n))
    best_sequence = get_best_sequence()
    return max(max_sub_arr, best_sequence)
    
def get_best_sequence():
    best_sequence_sum = 0
    for i in range(n):
        for j in range(n):
            if i==j:
                continue
            total = sequence_arr[i]["suffix"] + sequence_arr[j]["prefix"]
            total += sum(
                sequence_arr[z]["total"] 
                for z in range(n)
                if sequence_arr[z]["total"] >= 0 and z != i and z != j
                )
            best_sequence_sum = max(best_sequence_sum, total)
    return best_sequence_sum
            

def get_prefix(arr):
    arr_sum = 0
    max_prefix = arr_sum
    for idx in range(len(arr)):
        arr_sum += arr[idx]
        max_prefix = max(max_prefix, arr_sum)
    return max_prefix

def get_suffix(arr):
    arr_sum = 0
    max_suffix = arr_sum
    for idx in range(len(arr)-1, -1, -1):
        arr_sum += arr[idx]
        max_suffix = max(max_suffix, arr_sum)
    return max_suffix

def get_max_sub_arr(arr):
    local_max = 0
    global_max = local_max
    for idx in range(len(arr)):
        local_max = max(0, local_max + arr[idx])
        global_max = max(global_max, local_max)
    return global_max

n, m = map(int, input().split())
sequence_arr = []
for _ in range(n):
    arr = list(map(int, input().split()))
    sequence_arr.append({
        "prefix": get_prefix(arr),
        "suffix": get_suffix(arr),
        "total": sum(arr),
        "max_sub_arr": get_max_sub_arr(arr)
    })

if __name__ == "__main__":
    print(solution())
