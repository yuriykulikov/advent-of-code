import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test

class Day6Test {
  @Test
  fun `silver test (example)`() {
    val input = """Time:      7  15   30
Distance:  9  40  200"""
    silver(input).shouldBe(288)
  }

  @Test
  fun `silver test`() {
    silver(loadResource("Day6")).shouldBe(219849)
  }

  private fun Day6Test.silver(input: String): Int {
    val times =
        input.lines().first().substringAfter(":").trim().split(" ").mapNotNull { it.toIntOrNull() }
    val distances =
        input.lines().last().substringAfter(":").trim().split(" ").mapNotNull { it.toIntOrNull() }

    return times
        .zip(distances) { time, distance ->
          (0..time).asSequence().map { hold -> distance(time, hold) }.count { it > distance }
        }
        .reduce { acc, i -> acc * i }
  }

  @Test
  fun `gold test (example)`() {
    val input = """Time:      7  15   30
Distance:  9  40  200"""
    gold(input).shouldBe(71503)
  }

  @Test
  fun `gold test`() {
    gold(loadResource("Day6")).shouldBe(29432455)
  }

  /** Too lazy to think, let's keep the Brute Force */
  private fun Day6Test.gold(input: String): Int {
    val time = input.lines().first().substringAfter(":").replace(" ", "").toLong()
    val distance = input.lines().last().substringAfter(":").replace(" ", "").toLong()
    return (0..time).count { hold -> distance(time, hold) > distance }
  }

  private fun distance(time: Long, hold: Long): Long {
    check(hold <= time)
    val speed = hold
    return speed * (time - hold)
  }

  private fun distance(time: Int, hold: Int): Int {
    check(hold <= time)
    val speed = hold
    return speed * (time - hold)
  }
}
