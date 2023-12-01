import io.kotest.matchers.shouldBe
import kotlinx.collections.immutable.PersistentMap
import kotlinx.collections.immutable.toPersistentMap
import org.junit.jupiter.api.Test

class `Day 17 Pyroclastic Flow` {

  private val rocksString =
      """
|..####..

|...#.
|..###
|...#.

|....#
|....#
|..###

|..#
|..#
|..#
|..#

|..##
|..##
    """.trimIndent()

  private val rocks =
      rocksString.split("\n\n").map { lines -> parseMap(lines).filterValues { it == '#' } }
  private fun Int.next(): Int {
    return if (this == rocks.lastIndex) 0 else this + 1
  }
  private fun checkInterrupted() {
    check(!Thread.currentThread().isInterrupted) { "Interrupted!" }
  }

  private val expected =
      """
|..@@@@.|
|.......|
|.......|
|.......|
|....#..|
|....#..|
|....##.|
|##..##.|
|######.|
|.###...|
|..#....|
|.####..|
|....##.|
|....##.|
|....#..|
|..#.#..|
|..#.#..|
|#####..|
|..###..|
|...#...|
|..####.|
+-------+
"""

  private fun List<Int>.cycleIterator(): Iterator<Int> {
    var i = 0
    return object : Iterator<Int> {
      override fun hasNext(): Boolean {
        return true
      }

      override fun next(): Int {
        val index = i.rem(size)
        i++
        return get(index)
      }
    }
  }
  @Test
  fun silverTest() {
    val moves = testInput.map { if (it == '>') 1 else -1 }.cycleIterator()
    fallingRocks(moves).take(2023).last().first.keys.minOf(Point::y) shouldBe -3068
  }
  @Test
  fun silver() {
    val moves = loadResource("day17").map { if (it == '>') 1 else -1 }.cycleIterator()
    fallingRocks(moves).take(2023).last().first.keys.minOf(Point::y) shouldBe -3215
  }
  @Test
  fun goldTest() {
    val moves = testInput.map { if (it == '>') 1 else -1 }.cycleIterator()
    val result = heightOfVeryTallTower(heightIncrements(moves))
    result shouldBe 1514285714288
  }

  @Test
  fun gold() {
    val moves = loadResource("day17").map { if (it == '>') 1 else -1 }.cycleIterator()
    val result = heightOfVeryTallTower(heightIncrements(moves))
    result shouldBe 1575811209487
  }

  private fun heightOfVeryTallTower(data: List<Int>): Long {
    val (start, period) = findCycle(data)
    val prefixValues = data.take(start)
    val periodValues = data.drop(start).take(period)
    val periodicLength = 1000000000000L - start
    // incomplete period
    val suffixValues = periodValues.take(periodicLength.rem(period).toInt())
    return prefixValues.sum().toLong() +
        (periodicLength.div(period)) * periodValues.sum().toLong() +
        suffixValues.sum().toLong()
  }

  private fun heightIncrements(moves: Iterator<Int>, size: Int = 5023): List<Int> {
    val map = parseMap("+-------+").toMutableMap()
    var rock = 0
    return (0..size)
        .map {
          // one rock falls
          val startingPos = map.keys.minBy { it.y }.y - 4
          val fallenRock = fallenRock(rock, startingPos, map, moves)
          map.putAll(fallenRock)
          rock = rock.next()
          fallenRock.keys.minOf(Point::y)
        }
        .windowed(2) { (prev, next) -> prev - next }
  }

  private fun fallingRocks(moves: Iterator<Int>): Sequence<Pair<PersistentMap<Point, Char>, Int>> {
    return generateSequence(parseMap("+-------+").toPersistentMap() to 0) { (map, rock) ->
      // one rock falls
      val startingPos = map.keys.minBy { it.y }.y - 4
      map.putAll(fallenRock(rock, startingPos, map, moves)) to rock.next()
    }
  }

  data class PeriodAndOffset(val offset: Int, val period: Int)

  /**
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
    val middle = list.size / 2
    var periodHypothesis = 1
    var period = 0
    list.forEachIndexed { index, value ->
      val indexInPeriod = index % periodHypothesis
      if (value != list[indexInPeriod]) {
        if (index < middle) {
          periodHypothesis = index
        } else {
          return PeriodAndOffset(offset = list.size % period, period = period)
        }
      } else {
        if (indexInPeriod == 0) {
          period = periodHypothesis
        }
      }
    }
    error("List has no period!")
  }
  private fun fallenRock(
      rockIndex: Int,
      startingPos: Int,
      map: Map<Point, Char>,
      moves: Iterator<Int>,
  ): Map<Point, Char> {
    checkInterrupted()
    val rock = rocks[rockIndex]
    val maxY = rock.keys.maxBy { it.y }.y
    val rockInitialPos = rock.mapKeys { (p, v) -> p.up(maxY - startingPos) }

    fun Map<Point, Char>.push(where: Int): Map<Point, Char> {
      val pushed = mapKeys { (k, v) -> k.right(where) }
      return if (pushed.keys.all { it.x in 1..7 && it !in map }) {
        pushed
      } else {
        this
      }
    }

    var pos = rockInitialPos
    while (!Thread.currentThread().isInterrupted) {
      pos = pos.push(moves.next())
      val down = pos.mapKeys { (k, v) -> k.down() }
      if (down.keys.none { it in map }) pos = down else return pos
    }
    error("WTF")
  }

  private val testInput = ">>><<><>><<<>><>>><<<>>><<<><<<>><>><<>>"
}
