# Generates the sequence [1, -2, 3, -4, 5, -6, 7, -8, 9, ...]
def alternating_sign_increasing(n):
  sign = 1
  for i in range(1, n):
    yield sign * i
    sign = sign * -1

def manhattan_distance((x, y)):
  return abs(x) + abs(y)

# Walks the spiral by taking steps equal to length of the spiral legs until we've reached the nth spiral cell
def naive(n):
  sequence = alternating_sign_increasing(n/2)
  counter = 1
  x = 0
  y = 0

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

# print([naive(x) for x in range(1,100)])
n = 1000000
print(naive(n))
print(manhattan_distance(naive(n)))