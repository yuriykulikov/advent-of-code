import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test

class Day14Test {
  data class Platform(
      val stones: Map<Point, Char>,
      val maxX: Int,
      val maxY: Int,
  )

  private fun platform(parseMap: Map<Point, Char>): Platform {
    return Platform(parseMap, parseMap.maxOf { it.key.x }, parseMap.maxOf { it.key.y })
  }

  private val testInput =
      """
    O....#....
    O.OO#....#
    .....##...
    OO.#O....O
    .O.....O#.
    O.#..O.#.#
    ..O..#O..O
    .......O..
    #....###..
    #OO..#....
            """
          .trimIndent()

  @Test
  fun `move rocks north`() {
    moveNorth(parseMap(testInput))
        .stones
        .lines { "${it?:"."}" }
        .joinToString("\n")
        .shouldBe(
            """
  OOOO.#.O..
  OO..#....#
  OO..O##..O
  O..#.OO...
  ........#.
  ..#....#.#
  ..O..#.O.O
  ..O.......
  #....###..
  #....#....
          """
                .trimIndent())
  }

  @Test
  fun `silver example`() {
    testInput
        .let { string ->
          val map = parseMap(string)
          moveNorth(map) to map.maxOf { it.key.y }
        }
        .let { (map, mY) -> countWeight(map.stones, mY) } shouldBe 136
  }

  @Test
  fun `silver test`() {
    val map = parseMap(loadResource("Day14"))
    val moved = moveNorth(map)
    countWeight(moved.stones, map.maxOf { it.key.y }) shouldBe 107053
  }

  @Test
  fun `gold example`() {
    platform(parseMap(testInput))
        .spinCycle()
        .stones
        .lines { "${it?:"."}" }
        .joinToString("\n")
        .shouldBe(
            """
.....#....
....#...O#
...OO##...
.OO#......
.....OOO#.
.O#...O#.#
....O#....
......OOOO
#...O###..
#..OO#...."""
                .trimIndent())
  }

  @Test
  fun `gold example 2`() {
    platform(parseMap(testInput))
        .spinCycle()
        .spinCycle()
        .stones
        .lines { "${it?:"."}" }
        .joinToString("\n")
        .shouldBe(
            """
.....#....
....#...O#
.....##...
..O#......
.....OOO#.
.O#...O#.#
....O#...O
.......OOO
#..OO###..
#.OOO#...O"""
                .trimIndent())
  }

  @Test
  fun `gold example 3`() {
    platform(parseMap(testInput))
        .spinCycle()
        .spinCycle()
        .spinCycle()
        .stones
        .lines { "${it?:"."}" }
        .joinToString("\n")
        .shouldBe(
            """
.....#....
....#...O#
.....##...
..O#......
.....OOO#.
.O#...O#.#
....O#...O
.......OOO
#...O###.O
#.OOO#...O"""
                .trimIndent())
  }

  data class PeriodAndOffset(val offset: Int, val period: Int)
  /**
   * Copied from Day 17 Pyroclastic Flow
   *
   * Finds period and starting offset in O(n).
   *
   * Assuming that the tail of the list is periodic and head is not and that period should be at
   * most size/2 long.
   *
   * First assume a period of 1. Going back from the end of the list, consider each element. If the
   * element is equal to the expected values in the periodic sequence, proceed to the next one.
   * Otherwise, if the element is in the second part of the list, increase the hypothetical period.
   * If the element is in the first part of the list, we have reached to non-periodic part.
   */
  private fun findCycle(data: List<Int>): PeriodAndOffset {
    val list = data.reversed()
    var period = 1
    list.forEachIndexed { index, value ->
      val indexInPeriod = index % period
      when {
        // match, proceed
        value == list[indexInPeriod] -> Unit
        // no match and too early to be in the head
        index < list.size / 2 -> period = index
        // we must have reached the head
        else -> return PeriodAndOffset(offset = list.lastIndex - index, period = period)
      }
    }
    error("List has no period!")
  }

  @Test
  fun `find cycle test`() {
    val platform = platform(parseMap(loadResource("Day14")))

    val seen =
        generateSequence(platform) { it.spinCycle() }
            .map { countWeight(it.stones, it.maxY) }
            .take(350)
            .toList()

    findCycle(seen) shouldBe PeriodAndOffset(92, 72)
  }

  private fun afterManyNights(data: List<Int>): Int {
    val (start, period) = findCycle(data)
    val periodicLength = 1000000000 - start - period
    return data[start + periodicLength.rem(period)]
  }

  @Test
  fun `gold example test`() {
    val platform = platform(parseMap(testInput))

    val seen =
        generateSequence(platform) { it.spinCycle() }
            .map { countWeight(it.stones, it.maxY) }
            .take(100)
            .toList()

    afterManyNights(seen) shouldBe 64
  }

  /**
   * Would be nice to find periods from the front, but I do not know how to do that, since the head
   * can have small periods as well.
   */
  @Test
  fun `gold test`() {
    val platform = platform(parseMap(loadResource("Day14")))

    val seen =
        generateSequence(platform) { it.spinCycle() }
            .map { countWeight(it.stones, it.maxY) }
            .take(350)
            .toList()

    afterManyNights(seen).shouldBe(88371)
  }

  private fun countWeight(it: Map<Point, Char>, mY: Int): Int {
    return it.filterValues { it == 'O' }.map { it.key.y }.sumOf { y -> mY - y + 1 }
  }

  private fun Platform.spinCycle(): Platform {
    return moveNorth().west().south().east()
  }

  private fun moveNorth(map: Map<Point, Char>): Platform {
    val platform = Platform(map, map.maxOf { it.key.x }, map.maxOf { it.key.y })
    return platform.moveNorth()
  }

  /** Can be simplified, generalized and sped up, but mir egal. */
  private fun Platform.moveNorth(): Platform {
    return copy(
        stones =
            (0..maxX)
                .flatMap<Int, Pair<Point, Char>> { x ->
                  (0..maxY).fold(emptyList()) { acc, y ->
                    val point = Point(x, y)
                    when (stones[point]) {
                      // position remains the same
                      '#' -> acc + (point to '#')
                      'O' -> acc + ((acc.lastOrNull()?.first?.down() ?: Point(x, 0)) to 'O')
                      // ignore .
                      else -> acc
                    }
                  }
                }
                .toMap())
  }

  private fun Platform.south(): Platform {
    return copy(
        stones =
            (0..maxX)
                .flatMap<Int, Pair<Point, Char>> { x ->
                  (0..maxY).reversed().fold(emptyList()) { acc, y ->
                    val point = Point(x, y)
                    when (stones[point]) {
                      // position remains the same
                      '#' -> acc + (point to '#')
                      'O' -> acc + ((acc.lastOrNull()?.first?.up() ?: Point(x, maxY)) to 'O')
                      // ignore .
                      else -> acc
                    }
                  }
                }
                .toMap())
  }

  private fun Platform.west(): Platform {
    return copy(
        stones =
            (0..maxY)
                .flatMap<Int, Pair<Point, Char>> { y ->
                  (0..maxX).fold(emptyList()) { acc, x ->
                    val point = Point(x, y)
                    when (stones[point]) {
                      // position remains the same
                      '#' -> acc + (point to '#')
                      'O' -> acc + ((acc.lastOrNull()?.first?.right() ?: Point(0, y)) to 'O')
                      // ignore .
                      else -> acc
                    }
                  }
                }
                .toMap())
  }

  private fun Platform.east(): Platform {
    return copy(
        stones =
            (0..maxY)
                .flatMap<Int, Pair<Point, Char>> { y ->
                  (0..maxX).reversed().fold(emptyList()) { acc, x ->
                    val point = Point(x, y)
                    when (stones[point]) {
                      // position remains the same
                      '#' -> acc + (point to '#')
                      'O' -> acc + ((acc.lastOrNull()?.first?.left() ?: Point(maxX, y)) to 'O')
                      // ignore .
                      else -> acc
                    }
                  }
                }
                .toMap())
  }
}
