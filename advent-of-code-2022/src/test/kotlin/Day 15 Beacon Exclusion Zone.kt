import io.kotest.matchers.shouldBe
import kotlin.math.abs
import kotlin.math.max
import kotlinx.collections.immutable.persistentListOf
import org.junit.jupiter.api.Test

class `Day 15 Beacon Exclusion Zone` {

  @Test
  fun silverTest() {
    amountOfDefinitelyNoBeaconPositions(testInput, 10, true) shouldBe 26
  }
  @Test
  fun silver() {
    amountOfDefinitelyNoBeaconPositions(loadResource("day15"), 2000000) shouldBe 5870800
  }
  private fun Point.tuningFrequency(): Long {
    return x.toLong() * 4000000 + y
  }
  @Test
  fun goldTest() {
    possibleBeacon(testInput).tuningFrequency() shouldBe 56000011
  }

  @Test
  fun gold() {
    possibleBeacon(loadResource("day15")).tuningFrequency() shouldBe 10908230916597
  }

  private fun possibleBeacon(input: String): Point {
    val sensorToBeacon = parse(input)
    val start = sensorToBeacon.keys.map { it.y }.average().toInt()
    println(start)
    val (y, range) =
        (0..Int.MAX_VALUE)
            .asSequence()
            .flatMap { listOf(start + it, start - it) }
            .map { row ->
              row to
                  sensorToBeacon
                      .mapNotNull { (sensor, beacon) ->
                        val toRow = sensor.distanceTo(y = row)
                        val toBeacon = sensor.distanceTo(other = beacon)
                        val onRow = toBeacon - toRow
                        if (onRow > 0) {
                          (sensor.x - (onRow))..(sensor.x + onRow)
                        } else {
                          null
                        }
                      }
                      .fold(IntRangeSet()) { acc, next -> acc.plus(next) }
            }
            .first { (_, range) -> range.disjoint }

    val x = range.ranges.first().last + 1
    return Point(x, y).also {
      println("Result: " + it)
      sensorToBeacon.forEach { println(it) }
    }
  }

  class IntRangeSet {
    val disjoint
      get() = ranges.size != 1
    var ranges = persistentListOf<IntRange>()

    fun plus(other: IntRange): IntRangeSet = apply {
      val lastOrNull = ranges.lastOrNull()
      if (lastOrNull != null && lastOrNull.first <= other.first && lastOrNull.last >= other.last)
          return@apply

      ranges =
          ranges
              .add(other)
              .sortedBy { it.first }
              .fold(persistentListOf()) { acc, next ->
                when {
                  acc.isNotEmpty() && acc.last().last >= next.first -> {
                    val union = acc.last().first..max(next.last, acc.last().last)
                    acc.removeAt(acc.lastIndex).add(union)
                  }
                  else -> acc.add(next)
                }
              }
    }
    val size: Int
      get() = ranges.sumOf { it.last - it.first + 1 }
  }
  private fun amountOfDefinitelyNoBeaconPositions(
      input: String,
      row: Int,
      print: Boolean = false
  ): Int {
    val sensorToBeacon = parse(input)

    val sensorsInRange =
        sensorToBeacon.filter { (s, b) -> s.distanceTo(y = row) <= s.distanceTo(b) }

    if (print) {
      printBeacons(sensorToBeacon, sensorsInRange)
    }

    val fold =
        sensorsInRange
            .map { (s, b) ->
              val toRow = s.distanceTo(y = row)
              val toBeacon = s.distanceTo(b)
              val onRow = abs(toRow - toBeacon)
              (s.x - (onRow))..(s.x + onRow)
            }
            .fold(IntRangeSet()) { acc, next -> acc.plus(next) }

    val amountOfSensorsOnRow =
        sensorToBeacon
            .flatMap { (k, v) -> listOf(k, v) }
            .filter { it.y == row }
            .map { it.x }
            .toSet()
            .size

    return (fold.size - amountOfSensorsOnRow)
  }

  private fun parse(input: String) =
      input
          .lines()
          .filter { it.isNotBlank() }
          .associate { line ->
            val (sx, sy, bx, by) = line.split("=", ",", ":").mapNotNull { it.toIntOrNull() }
            Point(sx, sy) to Point(bx, by)
          }

  private fun Point.manhattanCircle(manhattan: Int): Set<Point> {
    return (x - manhattan..x + manhattan)
        .flatMap { xx -> (y - manhattan..y + manhattan).map { yy -> Point(xx, yy) } }
        .filter { (this - it).manhattan() <= manhattan }
        .toSet()
  }
  private fun printBeacons(sensorToBeacon: Map<Point, Point>, sensorsInRange: Map<Point, Point>) {
    val sensorToBeaconMap =
        sensorToBeacon.flatMap { (sensor, beacon) -> listOf(sensor to "S", beacon to "B") }.toMap()

    val inRangeMap =
        sensorsInRange
            .flatMap { (s, b) -> s.manhattanCircle((s - b).manhattan()) }
            .associateWith { "#" }

    val outOfRangeMap =
        sensorToBeacon
            .flatMap { (s, b) -> s.manhattanCircle((s - b).manhattan()) }
            .associateWith { "." }

    printMap(outOfRangeMap + inRangeMap + sensorToBeaconMap, prefixFun = { "$it".padEnd(3, ' ') })
  }

  private val testInput =
      """
Sensor at x=2, y=18: closest beacon is at x=-2, y=15
Sensor at x=9, y=16: closest beacon is at x=10, y=16
Sensor at x=13, y=2: closest beacon is at x=15, y=3
Sensor at x=12, y=14: closest beacon is at x=10, y=16
Sensor at x=10, y=20: closest beacon is at x=10, y=16
Sensor at x=14, y=17: closest beacon is at x=10, y=16
Sensor at x=8, y=7: closest beacon is at x=2, y=10
Sensor at x=2, y=0: closest beacon is at x=2, y=10
Sensor at x=0, y=11: closest beacon is at x=2, y=10
Sensor at x=20, y=14: closest beacon is at x=25, y=17
Sensor at x=17, y=20: closest beacon is at x=21, y=22
Sensor at x=16, y=7: closest beacon is at x=15, y=3
Sensor at x=14, y=3: closest beacon is at x=15, y=3
Sensor at x=20, y=1: closest beacon is at x=15, y=3
""".trimIndent()
}

private fun Point.distanceTo(x: Int = this.x, y: Int = this.y): Int {
  return abs(this.y - y) + abs(this.x - x)
}

private fun Point.distanceTo(other: Point): Int {
  return minus(other).manhattan()
}
