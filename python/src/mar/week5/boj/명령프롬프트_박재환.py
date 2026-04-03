num = int(input())
word = list(input())

for _ in range(num-1):
    word_2 = input()
    for n in range(len(word)):
        if word[n] == word_2[n]:
            continue
        else:
            word[n] = "?"
print(*word, sep = "")