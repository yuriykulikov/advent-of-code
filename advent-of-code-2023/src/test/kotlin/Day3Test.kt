import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test

/** [Day 3: Gear Ratios](https://adventofcode.com/2023/day/3) */
class Day3Test {
  data class EnginePart(
      private val start: Point,
      val value: Char,
      val strings: List<String>,
  ) {
    val numbers: List<Int>
      get() = strings.map { it.toInt() }
  }

  private val testInput =
      """467..114..
...*......
..35..633.
......#...
617*......
.....+.58.
..592.....
......755.
...$.*....
.664.598..
"""
          .trimIndent()

  @Test
  fun `silver test (example)`() {
    val map = parseMap(testInput)
    val parts = findParts(map)
    parts.sumOf { label -> label.numbers.sum() } shouldBe 4361
  }

  @Test
  fun `silver test`() {
    val map = parseMap(loadResource("Day3"))
    val parts = findParts(map)
    parts.sumOf { label -> label.numbers.sum() } shouldBe 544433
  }

  @Test
  fun `gold test (example)`() {
    val map = parseMap(testInput)
    val parts = findParts(map)
    parts
        .filter { it.value == '*' && it.numbers.size == 2 }
        .sumOf { it.numbers.first() * it.numbers.last() }
        .shouldBe(467835)
  }

  @Test
  fun `gold test`() {
    val map = parseMap(loadResource("Day3"))
    val parts = findParts(map)
    parts
        .filter { it.value == '*' && it.numbers.size == 2 }
        .sumOf { it.numbers.first() * it.numbers.last() }
        .shouldBe(76314915)
  }

  private fun findParts(map: Map<Point, Char>): List<EnginePart> {
    val special: Map<Point, Char> = map.filter { (key, value) -> value != '.' && !value.isDigit() }

    val engineParts =
        special.map { (key, value) ->
          val starts: List<Point> =
              key.surroundings()
                  .mapNotNull { surroundingPoint ->
                    generateSequence(surroundingPoint) { it.left() }
                        .takeWhile { map[it]?.isDigit() == true }
                        .lastOrNull()
                  }
                  .distinct()

          val labels =
              starts.map { start ->
                generateSequence(start) { it.right() }
                    .takeWhile { map[it]?.isDigit() == true }
                    .joinToString("") { "${map.getValue(it)}" }
              }

          EnginePart(key, value, labels)
        }
    return engineParts
  }

  private fun Point.surroundings(): List<Point> {
    return listOf(
        up(), down(), left(), right(), up().left(), up().right(), down().left(), down().right())
  }
}
