# Generates the sequence [1, -2, 3, -4, 5, -6, 7, -8, 9, ...]
def alternating_sign_increasing(n, start=1):
  sign = 1
  for i in range(start, n):
    yield sign * i
    sign = sign * -1

def manhattan_distance((x, y)):
  return abs(x) + abs(y)

# Walks the spiral by taking steps equal to length of the spiral legs until we've reached the nth spiral cell
def walk(n, counter=1, x=0, y=0, seq_start=1):
  sequence = alternating_sign_increasing(n / 2, seq_start)

  while counter < n:
    step = sequence.next()

    # if the step would bring us farther than the target n, we need to take a smaller step
    if (counter + abs(step)) > n:
      return (x + (n - counter) * (step / abs(step)), y) # step / abs(step)

    # take the step; update x and counter
    x += step
    counter += abs(step)

    # if we're actually at the target n, return the coordinates!
    if counter == n:
      return (x, y)

    # if the step would bring us farther than the target n, we need to take a smaller step
    if (counter + abs(step)) > n:
      return (x, y + (n - counter) * (step / abs(step)))

    # take the step; update y and counter
    y += step
    counter += abs(step)

  return (x, y)

def fly(n):
  ring = 0
  ring_start = 0
  partial_sum = 0

  while ring_start <= n:
    ring += 1
    partial_sum = partial_sum + 8 * ring
    leg_length = (ring + 1) * 2
    ring_start = partial_sum - leg_length + 3

  prev_sum = partial_sum - 8 * ring
  ring = ring - 1
  leg_length = (ring + 1) * 2
  ring_start = prev_sum - leg_length + 3

  return walk(n, counter=ring_start, x=(-1 * ring), y=(-1 * ring), seq_start=leg_length)

# print([walk(x) for x in range(1,100)])
n = 309430
print(walk(n))
print(fly(n))
print(manhattan_distance(walk(n)))
