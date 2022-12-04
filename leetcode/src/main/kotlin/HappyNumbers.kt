/**
 * [LeetCode](https://leetcode.com/problems/happy-number/)
 */
class HappyNumbers {
  private val seen = mutableSetOf<Int>()
  private fun Int.transform(): Int {
    val first =
        generateSequence(this) { it.div(10) }
            .takeWhile { it > 0 }
            .map { it.rem(10) }
            .map { it * it }
            .sum()

    return when (first) {
      1 -> 1
      in seen -> first
      else -> {
        seen.add(first)
        first.transform()
      }
    }
  }

  fun isHappy(n: Int): Boolean {
    return n.transform() == 1
  }
}
