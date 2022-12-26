import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test

class `Day 24 Blizzard Basin` {
  private fun Point.neighbors(): List<Point> = listOf(up(), down(), left(), right())
  data class State(
      val steps: Int,
      val blizzards: List<Pair<Point, Char>>,
      val pos: Set<Point>,
  )
  @Test
  fun silverTest() {
    findWayOutOnce(testInput2) shouldBe 18
  }

  @Test
  fun silver() {
    findWayOutOnce(loadResource("day24")) shouldBe 277
  }

  @Test
  fun goldTest() {
    findWayOutReturnAndGoBack(testInput2) shouldBe 54
  }

  @Test
  fun gold() {
    findWayOutReturnAndGoBack(loadResource("day24")) shouldBe 877
  }
  private fun findWayOutOnce(input: String): Int {
    val map = parseMap(input)
    val (start, end) =
        map.keys.filter { point ->
          map[point] == '.' && point.neighbors().count { map[it] == '#' } == 2
        }
    val blizzards =
        map.filterValues { it == '^' || it == '>' || it == 'v' || it == '<' }
            .map { (k, v) -> k to v }
    return findWay(map.filterValues { it == '#' }.keys, blizzards, start, end).steps + 1
  }

  private fun findWayOutReturnAndGoBack(input: String): Int {
    val map = parseMap(input)
    val (start, end) =
        map.keys.filter { point ->
          map[point] == '.' && point.neighbors().count { map[it] == '#' } == 2
        }
    val blizzards =
        map.filterValues { it == '^' || it == '>' || it == 'v' || it == '<' }
            .map { (k, v) -> k to v }

    val bounds = map.filterValues { it == '#' }.keys
    val firstRun = findWay(bounds, blizzards, start, end)
    val secondRun = findWay(bounds, firstRun.blizzards, end, start)
    val thirdRun = findWay(bounds, secondRun.blizzards, start, end)
    return firstRun.steps + secondRun.steps + thirdRun.steps + 1
  }

  private fun findWay(
      bounds: Set<Point>,
      blizzards: List<Pair<Point, Char>>,
      start: Point,
      end: Point
  ): State {
    val maxX = bounds.maxOf { it.x }
    val minX = bounds.minOf { it.x }
    val maxY = bounds.maxOf { it.y }
    val minY = bounds.minOf { it.y }

    return generateSequence(State(0, blizzards, setOf(start))) { state ->
          val nextBlizzards =
              state.blizzards.map { (point, direction) ->
                when (direction) {
                  '^' -> point.up().takeIf { it.y > minY } ?: point.copy(y = maxY - 1)
                  '>' -> point.right().takeIf { it.x < maxX } ?: point.copy(x = minX + 1)
                  'v' -> point.down().takeIf { it.y < maxY } ?: point.copy(y = minY + 1)
                  '<' -> point.left().takeIf { it.x > minX } ?: point.copy(x = maxX - 1)
                  else -> error("")
                } to direction
              }

          val blizzardMap = nextBlizzards.associate { it }

          val possibleMoves =
              state.pos
                  .flatMap { it.neighbors() + it }
                  .filter { it !in blizzardMap && it !in bounds && it.y in minY..maxY }
                  .toSet()

          State(state.steps + 1, nextBlizzards, possibleMoves)
        }
        .takeWhile { state -> end !in state.pos }
        .last()
  }

  private val testInput1 =
      """
#.#####
#.....#
#>....#
#.....#
#...v.#
#.....#
#####.#
    """.trimIndent()

  private val testInput2 =
      """
#.######
#>>.<^<#
#.<..<<#
#>v.><>#
#<^v^^>#
######.#
    """.trimIndent()
}
