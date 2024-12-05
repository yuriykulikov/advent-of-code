package year2024

import io.kotest.matchers.shouldBe
import loadResource
import org.junit.jupiter.api.Test

class Day2Test {
  private val example =
      """
        7 6 4 2 1
        1 2 7 8 9
        9 7 6 2 1
        1 3 2 4 5
        8 6 4 4 1
        1 3 6 7 9
    """
          .trimIndent()

  @Test
  fun `safe reports are descending and ascending with max difference of 3 example`() {
    example.lines().count { isSafeReport(it) } shouldBe 2
  }

  @Test
  fun `safe reports are descending and ascending with max difference of 3`() {
    loadResource("Day2").lines().count { isSafeReport(it) } shouldBe 516
  }

  @Test
  fun `safe reports are descending and ascending with dampener with max difference of 3 example`() {
    example.lines().count { isSafeReportWithDampener(it) } shouldBe 4
  }

  @Test
  fun `safe reports are descending and ascending with dampener with max difference of 3`() {
    loadResource("Day2").lines().count { isSafeReportWithDampener(it) } shouldBe 561
  }

  private fun isSafeReport(ints: List<Int>): Boolean {
    return ints.zipWithNext { a, b -> a - b }.all { it in 1..3 } ||
        ints.zipWithNext { a, b -> b - a }.all { it in 1..3 }
  }

  private fun isSafeReport(it: String): Boolean {
    return isSafeReport(it.split(" ").map { it.toInt() })
  }

  private fun isSafeReportWithDampener(it: String): Boolean {
    val ints = it.split(" ").map { it.toInt() }
    val permutations = List(ints.size) { i -> ints.toMutableList().apply { removeAt(i) } }
    return permutations.firstOrNull { isSafeReport(it) } != null
  }
}
