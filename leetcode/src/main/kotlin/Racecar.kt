/** [LeetCode](https://leetcode.com/problems/race-car/) */
class Racecar {
  data class State(val speed: Int, val pos: Int) {
    fun a(): State {
      return copy(pos = pos + speed, speed = speed * 2)
    }

    fun r(): State {
      return copy(speed = if (speed > 0) -1 else 1)
    }
  }
  fun racecar(target: Int): Int {
    // Naive
    // At each point we have 2 options: A and R
    // We can to build a tree
    //                 0
    //             /      \
    //            A         R
    //         /    \     /    \
    //        AA     AR  RA     RR
    // It will take 2^n time to get the result with BFS
    // We can optimize by excluding nodes which already have been seen
    // And, since we need the amount of steps and not exact steps
    // we can assume that it only makes sense to brake and reverse
    // when we overshot or are about to overshoot

    // val queue = LinkedList<Pair<State, Int>>()
    // ArrayDeque is a bit faster
    val queue = ArrayDeque<Pair<State, Int>>()
    queue.add(State(1, 0) to 0)
    val seen = HashSet<State>()
    while (queue.isNotEmpty()) {
      val (next, level) = queue.removeFirst()
      if (next.pos == target) {
        return level
      }
      if (next in seen) {
        continue
      }
      if (next.pos >= target * 2 || next.pos < -target) {
        continue
      }
      seen.add(next)
      val a = next.a()
      // always add the accelerated state
      queue.add(a to level + 1)
      val overshoot = a.pos > target && a.speed > 0
      val undershoot = a.pos < target && a.speed < 0
      if (undershoot || overshoot) {
        queue.add(next.r() to level + 1)
      }
    }
    error("not found")
  }
}