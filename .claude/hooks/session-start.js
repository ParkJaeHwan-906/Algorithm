// SessionStart hook: 세션 시작 시 작업 분류를 먼저 묻도록 Claude에 지시를 주입한다.
const context = `[문제 추천 / 커밋 세션 규칙]
이 세션은 알고리즘 스터디 작업으로 시작한다. 사용자의 첫 메시지를 받으면 아래 순서로 진행한다.
(단, 첫 메시지가 명확히 다른 작업 요청이면 그 작업을 우선하고 이 흐름은 건너뛴다.)

1. 분류 선택: AskUserQuestion 도구로 "어떤 작업을 할까요?"를 묻는다.
   - header: "작업 분류", multiSelect: false
   - 선택지: "SQL" (SQL 문제 추천), "알고리즘" (알고리즘 문제 추천), "커밋 작업" (풀이 파일 커밋 및 push)

[SQL / 알고리즘을 고른 경우]
2. 재풀이 선택: AskUserQuestion 도구로 "이미 푼 문제도 다시 풀까요?"를 묻는다.
   - header: "재풀이", multiSelect: false
   - 선택지: "새 문제만" (저장소에 풀이가 있는 문제는 제외), "푼 문제 포함" (새 문제와 함께 보여주고 '이미 풀이'로 표시), "푼 문제만" (복습용으로 이미 푼 문제만)
3. 유형 질문: 선택 도구를 쓰지 말고 일반 대화로 어떤 유형을 풀고 싶은지 묻는다.
   - 알고리즘이면 예시: BFS/DFS, DP, 그리디, 이분 탐색, 투 포인터, 구현/시뮬레이션, 최단 경로 등
   - SQL이면 예시: SELECT, GROUP BY, JOIN, 서브쿼리, 윈도우 함수, 문자열/날짜 함수 등
   - 원하는 난이도나 개수가 있으면 함께 말해 달라고 덧붙인다.
4. 문제 찾기: 사용자가 유형을 말하면 problem-finder 에이전트에 분류·유형·재풀이 선택·추가 조건을 넘겨 문제를 찾게 한다.
5. 결과 전달: 에이전트가 돌려준 사이트별 문제 표를 사용자에게 그대로 보여준다.

[커밋 작업을 고른 경우]
2. 범위 선택: AskUserQuestion 도구로 "README도 함께 기록할까요?"를 묻는다.
   - header: "커밋 범위", multiSelect: false
   - 선택지: "파일만" (풀이 파일마다 커밋 후 push), "파일 + README" (README 이번 주차 표에 문제를 추가해 함께 커밋 후 push)
3. 커밋: solution-committer 에이전트에 선택한 모드를 넘겨 커밋과 push를 맡긴다. 사용자가 이 흐름에서 커밋 작업을 고른 것 자체가 커밋·push 승인이다.
4. 결과 전달: 에이전트가 돌려준 커밋 목록, push 결과, 제외·미확인 파일을 사용자에게 보여준다.`;

process.stdout.write(JSON.stringify({
  systemMessage: "알고리즘 스터디 세션입니다. 아무 메시지나 입력하면 작업 분류(SQL / 알고리즘 / 커밋 작업)부터 물어봅니다.",
  hookSpecificOutput: {
    hookEventName: "SessionStart",
    additionalContext: context,
  },
}));
