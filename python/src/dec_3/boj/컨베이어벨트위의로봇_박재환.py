n, k = map(int,  input().split(" "))
belt = list(map(int, input().split(" ")))

def solution(n, k, belt):
    robots = [False] * (2*n)
    expired = 0
    depth = 0

    while(expired < k):
        depth+=1
        # 1. 벨트 회전
        rotate_belt(belt, robots)
        # 2. 로봇 이동
        expired = move_robots(belt, robots, expired)
        # 3. 로봇 올리기
        expired = put_robot(belt, robots, expired)
    
    print(depth)



def rotate_belt(belt, robots):
    tmp_belt = belt[-1]
    tmp_robot = robots[-1]
    for i in range(2*n-1, 0, -1):
        belt[i] = belt[i-1]
        robots[i] = robots[i-1]
    belt[0] = tmp_belt
    robots[0] = tmp_robot

    remove_robot(robots)

def move_robots(belt, robots, expired):
    for i in range(n-2, -1, -1):
        # 로봇이 없는 경우, 패스
        if(not robots[i]):
            continue

        # 로봇이 있는 경우
        # 조건 : 이동하려는 칸에 로봇이 없고, 내구도가 1 이상 남아있다. 
        if(robots[i+1] or belt[i+1] < 1):
            continue

        # 이동 가능하다
        belt[i+1] -= 1
        robots[i]  = False
        robots[i+1] = True

        if(belt[i+1] == 0):
            expired += 1

    remove_robot(robots)

    return expired

def put_robot(belt, robots, expired):
    if(belt[0] < 1):
        return expired

    belt[0] -= 1
    robots[0] = True

    if(belt[0] == 0):
        expired += 1

    return expired

def remove_robot(robots):
    robots[n-1] = False

if __name__ == "__main__":
    solution(n, k, belt)