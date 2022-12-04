/**
 * This was in 2020...
 *
 * [LeetCode](https://leetcode.com/problems/string-to-integer-atoi/)
 */
class AtoiSolution {
  fun myAtoi(str: String): Int {
    val numberStr =
        str.trim().dropWhile { !it.isDigit() }.takeWhile { it.isDigit() }.dropWhile { it == '0' }
    if (numberStr.isEmpty()) return 0
    val prefix = str.subSequence(0, str.indexOf(numberStr)).filter { it != '0' }
    if (prefix.count { !it.isWhitespace() } > 1) return 0
    if (prefix.any { it.isLetter() || it == '.' }) return 0
    if (prefix.isNotBlank() && prefix.last() == ' ') return 0

    val negative = prefix.lastOrNull() == '-'

    return when {
      numberStr.length > 18 && negative -> Int.MIN_VALUE
      numberStr.length > 18 -> Int.MAX_VALUE
      negative -> Math.max(-numberStr.parse(), Int.MIN_VALUE.toLong()).toInt()
      else -> Math.min(numberStr.parse(), Int.MAX_VALUE.toLong()).toInt()
    }
  }

  private fun String.parse(): Long {
    return this.reversed().foldIndexed(0L) { index, acc, char ->
      acc + (char - 48).toLong() * 10.pow(index)
    }
  }

  private fun Int.pow(index: Int): Long {
    var ret = 1L
    repeat(index) { ret *= this }
    return ret
  }
}
