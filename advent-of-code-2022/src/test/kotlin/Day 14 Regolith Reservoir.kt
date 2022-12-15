import io.kotest.matchers.shouldBe
import kotlinx.collections.immutable.toPersistentMap
import org.junit.jupiter.api.Test

class `Day 14 Regolith Reservoir` {
  @Test
  fun silverTest() {
    `How many units of sand come to rest before sand starts flowing into the abyss below`(
        testInput, print = true) shouldBe 24
  }

  @Test
  fun silver() {
    `How many units of sand come to rest before sand starts flowing into the abyss below`(
        loadResource("day14")) shouldBe 808
  }

  @Test
  fun goldTest() {
    `How many units of sand come to rest before sand starts flowing into the abyss below`(
        testInput, floor = true, print = true) shouldBe 93
  }

  @Test
  fun gold() {
    `How many units of sand come to rest before sand starts flowing into the abyss below`(
        loadResource("day14"), floor = true) shouldBe 26625
  }
  private fun parseInput(input: String) =
      input
          .lines()
          .flatMap { line ->
            line
                .split(" -> ")
                .map { pointStr -> pointStr.split(",").map { it.toInt() } }
                .map { (x, y) -> Point(x, y) }
                .windowed(2)
                .flatMap { (l, r) -> if (l.x < r.x || l.y < r.y) l..r else r..l }
          }
          .associateWith { "#" }
  private fun `How many units of sand come to rest before sand starts flowing into the abyss below`(
      input: String,
      floor: Boolean = false,
      print: Boolean = false,
  ): Int {
    val map = parseInput(input)
    val maxY = map.keys.maxOf { it.y }
    val result =
        generateSequence(map.toPersistentMap()) { prev ->
              val next = settle(prev, if (floor) maxY + 2 else maxY, floor)
              if (next != null) prev.put(next, "o") else null
            }
            .last()
    if (print) printMap(result)
    return result.values.count { it == "o" }
  }

  private val downLeft = Point(-1, 1)
  private val downRight = Point(1, 1)
  private val start = Point(500, 0)
  private fun settle(map: Map<Point, String>, maxY: Int, floor: Boolean): Point? {
    var pos: Point = start
    while (true) {
      val nextPos =
          pos.down().takeIf { it !in map }
              ?: pos.plus(downLeft).takeIf { it !in map }
                  ?: pos.plus(downRight).takeIf { it !in map }
      if (nextPos == null || floor && nextPos.y == maxY) return pos.takeIf { it !in map }
      if (nextPos.y == maxY) return null
      pos = nextPos
    }
  }

  private val testInput =
      """
498,4 -> 498,6 -> 496,6
503,4 -> 502,4 -> 502,9 -> 494,9
    """.trimIndent()
}
