"""Supply the practice workflow as SessionStart context without interactive stdin."""
import json
from pathlib import Path
import sys


def main():
    event = json.load(sys.stdin)
    if event.get("source") not in ("startup", "clear"):
        return
    root = Path(__file__).resolve().parents[2]
    skill = root / ".agents" / "skills" / "problem-finder" / "SKILL.md"
    context = (
        "이 저장소의 새 세션에서는 다음 스킬을 읽고 "
        "SQL / 알고리즘 / 커밋 작업 선택 UI를 먼저 제공하라: " + skill.as_posix() + ". "
        "SQL과 알고리즘은 원하는 유형을 자유로운 대화로 묻는다. "
        "커밋 작업은 파일만 또는 파일+README 중 원하는 범위를 먼저 묻고 "
        "스킬의 커밋 작업 지침에 따라 파일별 커밋과 push를 진행한다. "
        "사용자가 첫 메시지에서 분류나 유형을 이미 제시하면 해당 질문은 생략한다. "
        "다른 작업을 명시하면 그 요청을 우선한다. "
        "이 훅은 모델에 문맥만 전달하며 직접 선택 UI를 띄우거나 대화를 시작하지 않는다. "
        "첫 사용자 메시지에 응답할 때 질문을 진행한다."
    )
    # SessionStart accepts plain stdout as model context. Avoid JSON output
    # parsing and Windows console code-page conversions in the hook runner.
    sys.stdout.buffer.write((context + "\n").encode("utf-8"))


if __name__ == "__main__":
    main()
