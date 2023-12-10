import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test

class Day10Test {

  @Test
  fun `silver test`() {
    val map = parseMap(loadResource("Day10").pretty())
    val loop = findLoop(map)
    loop.size / 2 shouldBe 6768
  }

  @Test
  fun `gold example 1`() {
    val input =
        """.F----7F7F7F7F-7....
.|F--7||||||||FJ....
.||.FJ||||||||L7....
FJL7L7LJLJ||LJ.L-7..
L--J.L7...LJS7F-7L7.
....F-J..F7FJ|L7L7L7
....L7.F7||L7|.L7L7|
.....|FJLJ|FJ|F7|.LJ
....FJL-7.||.||||...
....L---J.LJ.LJLJ..."""

    val map = scaleUp(parseMap(input.pretty()))
    val fill = floodFill(findLoop(map).toSet())
    countPossibleHidingPlaces(fill, map) shouldBe 8
  }

  @Test
  fun `gold example 2`() {
    val input =
        """FF7FSF7F7F7F7F7F---7
L|LJ||||||||||||F--J
FL-7LJLJ||||||LJL-77
F--JF--7||LJLJ7F7FJ-
L---JF-JLJ.||-FJLJJ7
|F|F-JF---7F7-L7L|7|
|FFJF7L7F-JF7|JL---7
7-L-JL7||F7|L7F-7F7|
L.L7LFJ|||||FJL7||LJ
L7JLJL-JLJLJL--JLJ.L"""

    val map = scaleUp(parseMap(input.pretty()))
    val fill = floodFill(findLoop(map).toSet())
    countPossibleHidingPlaces(fill, map).shouldBe(10)
  }

  @Test
  fun `gold test`() {
    val map = scaleUp(parseMap(loadResource("Day10").pretty()))
    val loop = findLoop(map).toSet()
    val fill = floodFill(loop)
    countPossibleHidingPlaces(fill, map) shouldBe 351
  }

  private fun countPossibleHidingPlaces(fill: Set<Point>, map: Map<Point, Char>): Int {
    return fill
        .count { map[it] == '.' }
        .plus(
            fill.count {
              val fillValue = map[it]
              fillValue == '┌' ||
                  fillValue == '┐' ||
                  fillValue == '└' ||
                  fillValue == '┘' ||
                  fillValue == '─' ||
                  fillValue == '│'
            } / 3)
  }

  /**
   * Of course very inefficient, but it works. Ways to optimise:
   * - DFS to exclude areas which are not within the loops
   * - Or join areas when overlap
   */
  private fun floodFill(loop: Set<Point>): Set<Point> {
    val maxX = loop.maxOf { it.x }
    val maxY = loop.maxOf { it.y }
    val minX = loop.minOf { it.x }
    val minY = loop.minOf { it.y }

    return loop
        .first()
        .surroundings()
        .filter { it !in loop }
        .firstNotNullOf { offStart ->
          generateSequence(emptySet<Point>() to setOf(offStart)) { (flood, prevSpill: Set<Point>) ->
                val spill =
                    prevSpill
                        .flatMap { point ->
                          point.surroundings().filter { it !in flood && it !in loop }
                        }
                        .toSet()
                if (spill.any { it.x > maxX || it.y > maxY || it.x < minX || it.y < minY }) null
                else flood + spill to spill
              }
              .takeWhile { (_, spill) -> spill.isNotEmpty() }
              .lastOrNull()
              ?.first
        }
  }

  private fun Point.surroundings(): List<Point> {
    return listOf(
        up(), down(), left(), right(), up().left(), up().right(), down().left(), down().right())
  }

  private fun scaleUp(map: Map<Point, Char>) =
      map.entries
          .map { (point, char) -> point.copy(x = point.x * 3, y = point.y * 3) to char }
          .flatMap { (point, char) ->
            when (char) {
              'S' ->
                  listOf(
                      // cross
                      point to 'S',
                      point.down() to '│',
                      point.right() to '─',
                      point.left() to '─',
                      point.up() to '│')
              '┌' -> listOf(point to char, point.down() to '│', point.right() to '─')
              '┐' -> listOf(point to char, point.down() to '│', point.left() to '─')
              '└' -> listOf(point to char, point.up() to '│', point.right() to '─')
              '┘' -> listOf(point to char, point.up() to '│', point.left() to '─')
              '─' -> listOf(point to char, point.left() to '─', point.right() to '─')
              '│' -> listOf(point to char, point.up() to '│', point.down() to '│')
              '.' -> listOf(point to char)
              else -> emptyList()
            }
          }
          .toMap()

  /**
   * Just follow the pipes like an animal, doesn't matter which way you go. We also ignore the fact
   * that we go both directions.
   */
  private fun findLoop(map: Map<Point, Char>): List<Point> {
    val start = map.entries.first { it.value == 'S' }.key

    return listOfNotNull(
            start.upOrNull(map),
            start.downOrNull(map),
            start.leftOrNull(map),
            start.rightOrNull(map),
        )
        .map { candidate ->
          generateSequence(start to candidate) { (prev, tail) ->
                val nextStep = nextStep(map, prev, tail)
                nextStep?.let { tail to nextStep }
              }
              .map { it.second }
              .toList()
        }
        .maxBy { it.size }
        .plus(start)
  }

  private fun nextStep(map: Map<Point, Char>, prev: Point, tail: Point): Point? {
    return when (val tailVal = map.getValue(tail)) {
      '┌' -> if (prev == tail.down()) tail.rightOrNull(map) else tail.downOrNull(map)
      '┐' -> if (prev == tail.down()) tail.leftOrNull(map) else tail.downOrNull(map)
      '└' -> if (prev == tail.up()) tail.rightOrNull(map) else tail.upOrNull(map)
      '┘' -> if (prev == tail.up()) tail.leftOrNull(map) else tail.upOrNull(map)
      '─' -> if (prev == tail.left()) tail.rightOrNull(map) else tail.leftOrNull(map)
      '│' -> if (prev == tail.up()) tail.downOrNull(map) else tail.upOrNull(map)
      else -> error("Unexpected value: $tailVal")
    }
  }

  private fun Point.rightOrNull(map: Map<Point, Char>): Point? {
    return right().takeIf { map[it] == '┐' || map[it] == '┘' || map[it] == '─' }
  }

  private fun Point.leftOrNull(map: Map<Point, Char>): Point? {
    return left().takeIf { map[it] == '└' || map[it] == '┌' || map[it] == '─' }
  }

  private fun Point.downOrNull(map: Map<Point, Char>): Point? {
    return down().takeIf { map[it] == '┘' || map[it] == '└' || map[it] == '│' }
  }

  private fun Point.upOrNull(map: Map<Point, Char>): Point? {
    return up().takeIf { map[it] == '┐' || map[it] == '┌' || map[it] == '│' }
  }

  private fun String.pretty(): String {
    return replace('F', '┌')
        .replace('7', '┐')
        .replace('L', '└')
        .replace('J', '┘')
        .replace('-', '─')
        .replace('|', '│')
  }
}
