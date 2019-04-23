def solution(input):
  position = 0 + 0j
  direction = 0 + 1j

  visited = {position}
  visited_twice = None

  for step_direction in input:
    if step_direction[0] == 'L':
      direction *= 1j
    else: 
      direction *= -1j

    step_length = int(step_direction[1:])

    for i in range(step_length):
      position += direction

      if position in visited and visited_twice is None:
        visited_twice = position

      visited.add(position)
  return (position, visited_twice)

def main():
  with open("input.txt") as input:
    contents = input.read().replace('\n', '').split(", ")

  final_position, visited_twice = solution(contents)
  distance = int(abs(final_position.real)) + int(final_position.imag)

  print(distance)
  print(int(abs(visited_twice.real)) + int(abs(visited_twice.imag)))

if __name__ == "__main__":main()
