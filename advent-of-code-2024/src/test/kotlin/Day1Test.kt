import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test
import kotlin.math.absoluteValue

class Day1Test {
  private val input = """
3   4
4   3
2   5
1   3
3   9
3   3
  """.trimIndent()

  @Test
  fun `silver example`() {
    distanceBetweenLists(input) shouldBe 11
  }

  @Test
  fun `silver test`() {
    distanceBetweenLists(loadResource("Day1")) shouldBe 2057374
  }

  private fun distanceBetweenLists(input: String): Int {
    val split = input.lines().filter { it.isNotEmpty() }.map { it.split("   ") }
    val l = split.map { (l, r) -> l.toInt() }
    val r = split.map { (l, r) -> r.toInt() }
    val result = l.sorted().zip(r.sorted()).sumOf { (l, r) -> (r - l).absoluteValue }
    return result
  }

  @Test
  fun `gold example`() {
    similarityScore(input) shouldBe 31
  }


  @Test
  fun `gold test`() {
    similarityScore(loadResource("Day1")) shouldBe 23177084
  }


  private fun similarityScore(input: String): Long {
    val split = input.lines().filter { it.isNotEmpty() }.map { it.split("   ") }
    val l = split.map { (l, r) -> l.toLong() }
    val r = split.map { (l, r) -> r.toLong() }
    val rcounts = r.groupingBy { it }.eachCount()
    return l.map { it * (rcounts[it] ?: 0) }.sum()
  }

}
