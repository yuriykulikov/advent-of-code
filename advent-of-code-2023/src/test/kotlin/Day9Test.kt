import io.kotest.matchers.collections.shouldContainAll
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test

class Day9Test {
  private val testInput =
      """
            0 3 6 9 12 15
            1 3 6 10 15 21
            10 13 16 21 30 45
            """
          .trimIndent()

  @Test
  fun `silver test (example)`() {
    parseSequences(testInput)
        .map { initialSequence -> foldSequence(initialSequence) }
        .apply { shouldContainAll(18, 28, 68) }
        .sum()
        .shouldBe(114)
  }

  @Test
  fun `silver test`() {
    parseSequences(loadResource("Day9"))
        .sumOf { initialSequence -> foldSequence(initialSequence) }
        .shouldBe(1479011877)
  }

  @Test
  fun `gold test (example)`() {
    parseSequences(testInput)
        .sumOf { initialSequence -> foldSequenceGold(initialSequence) }
        .shouldBe(2)
  }

  @Test
  fun `gold test`() {
    parseSequences(loadResource("Day9"))
        .sumOf { initialSequence -> foldSequenceGold(initialSequence) }
        .shouldBe(973)
  }

  private fun foldSequence(initialSequence: List<Int>): Int {
    return generateSequence(initialSequence) { it.zipWithNext { l, r -> r - l } }
        .takeWhile { seq -> !seq.all { it == 0 } }
        .sumOf { it.last() }
  }

  private fun foldSequenceGold(initialSequence: List<Int>): Int {
    return generateSequence(initialSequence) { it.zipWithNext { l, r -> r - l } }
        .takeWhile { seq -> !seq.all { it == 0 } }
        .map { it.first() }
        .toList()
        .reversed()
        .fold(0) { acc, next -> next - acc }
  }

  private fun parseSequences(input: String) =
      input
          .lines()
          .filterNot { it.isEmpty() }
          .map { line -> line.trim().split(" ").map { it.toInt() } }
}
