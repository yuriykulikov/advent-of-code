import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test

class `Day 8 Treetop Tree House` {
  @Test
  fun silverTest() {
    countTreesVisibleFromOutside(forest(testInput)) shouldBe 21
  }
  @Test
  fun silver() {
    countTreesVisibleFromOutside(forest(loadResource("day8"))) shouldBe 1684
  }
  private fun forest(testInput: String): Map<Point, Int> {
    return parseMap(testInput).mapValues { (k, char) -> "$char".toInt() }
  }

  /** Count trees which are higher than previous highest tree */
  fun visibleFromDistance(forest: Map<Point, Int>, points: List<Point>): Set<Point> {
    var maxHeight = Int.MIN_VALUE
    val visible = mutableSetOf<Point>()
    for (point in points) {
      val height = forest.getValue(point)
      if (height > maxHeight) {
        visible.add(point)
        maxHeight = height
      }
    }
    return visible
  }
  private fun countTreesVisibleFromOutside(forest: Map<Point, Int>): Int {
    val max = forest.keys.last()

    val scans =
        (0..max.y).map { y -> (Point(0, y)..Point(max.x, y)) } +
            (0..max.y).map { y -> (Point(0, y)..Point(max.x, y)).reversed() } +
            (0..max.x).map { x -> (Point(x, 0)..Point(x, max.y)) } +
            (0..max.x).map { x -> (Point(x, 0)..Point(x, max.y)).reversed() }

    val visible = scans.flatMap { scan -> visibleFromDistance(forest, scan) }.toSet()

    return visible.size
  }

  @Test
  fun goldTest() {
    val forest = forest(testInput)
    forest.keys.maxOf { point -> scenicScore(forest, point) } shouldBe 8
  }
  @Test
  fun gold() {
    val forest = forest(loadResource("day8"))
    forest.keys.maxOf { point -> scenicScore(forest, point) } shouldBe 486540
  }

  @Test
  fun goldSmallTest() {
    scenicScore(forest(testInput), Point(2, 1)) shouldBe 4
    scenicScore(forest(testInput), Point(2, 3)) shouldBe 8
    scenicScore(forest(testInput), Point(3, 2)) shouldBe 2
  }
  fun visibleFromTreeHouse(
      forest: Map<Point, Int>,
      points: List<Point>,
      treeHouseHeight: Int
  ): Int {
    var seen = 0
    for (point in points) {
      seen++
      if (forest.getValue(point) >= treeHouseHeight) {
        break
      }
    }
    return seen
  }
  fun scenicScore(forest: Map<Point, Int>, point: Point, debug: Boolean = false): Int {
    val max = forest.keys.last()
    if (point.x == 0 || point.x == max.x || point.y == 0 || point.y == max.y) return 0
    val height = forest.getValue(point)
    val scans =
        listOf(
            (point.copy(x = 0)..point).reversed().minus(point),
            (point..point.copy(x = max.x)).minus(point),
            (point.copy(y = 0)..point).reversed().minus(point),
            (point..point.copy(y = max.y)).minus(point),
        )
    if (debug) {
      println("----")
      printMap(scans.flatten().plus(point).associateWith { forest.getValue(it) })
    }
    return scans
        .map { scan -> visibleFromTreeHouse(forest, scan, height) }
        .reduce { acc, next -> acc * next }
        .also { if (debug) println("res: $it") }
  }

  private val testInput =
      """
        30373
        25512
        65332
        33549
        35390
    """
          .trimIndent()

  fun printMap(tiles: Map<Point, Int>) {
    printMap(tiles) {
      when (it) {
        null -> " "
        else -> it.toString()
      }
    }
  }
}
