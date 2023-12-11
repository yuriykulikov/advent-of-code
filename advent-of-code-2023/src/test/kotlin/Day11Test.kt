import io.kotest.matchers.shouldBe
import kotlin.math.absoluteValue
import org.junit.jupiter.api.Test

class Day11Test {

  @Test
  fun `expand the universe`() {
    val input =
        """
...#......
.......#..
#.........
..........
......#...
.#........
.........#
..........
.......#..
#...#.....
    """
            .trimIndent()

    expandUniverse(parseMap(input)).lines { "${it ?: "."}" }.joinToString("\n") shouldBe
        """
....#........
.........#...
#............
.............
.............
........#....
.#...........
............#
.............
.............
.........#...
#....#.......
        """
            .trimIndent()
  }

  private fun expandUniverse(sky: Map<Point, Char>, expanse: Int = 2): Map<Point, Char> {
    val galaxies = sky.filterValues { it == '#' }.keys
    val occupiedX = galaxies.map { it.x }.toSet()
    val occupiedY = galaxies.map { it.y }.toSet()

    fun expand(coordinate: Int, occupied: Set<Int>): Int {
      val notExpanded = (0..coordinate).count { it in occupied }
      val expanded = coordinate - notExpanded
      return notExpanded + expanded * expanse
    }

    return galaxies
        .map { point -> Point(expand(point.x, occupiedX), expand(point.y, occupiedY)) }
        .associateWith { '#' }
  }

  @Test
  fun `silver example`() {
    val input =
        """
...#......
.......#..
#.........
..........
......#...
.#........
.........#
..........
.......#..
#...#.....
    """
            .trimIndent()

    sumOfDistances(expandUniverse(parseMap(input)).keys) shouldBe 374
  }

  private fun sumOfDistances(galaxies: Set<Point>): Long {
    return galaxies.cartesianProduct().sumOf { (l, r) ->
      (l.x - r.x).absoluteValue.toLong() + (l.y - r.y).absoluteValue
    }
  }

  @Test
  fun `silver test`() {
    sumOfDistances(expandUniverse(parseMap(loadResource("Day11"))).keys) shouldBe 9543156
  }

  @Test
  fun `gold example 1`() {
    val input =
        """
...#......
.......#..
#.........
..........
......#...
.#........
.........#
..........
.......#..
#...#.....
    """
            .trimIndent()
    sumOfDistances(expandUniverse(parseMap(input), expanse = 10).keys) shouldBe 1030
  }

  @Test
  fun `gold example 2`() {
    val input =
        """
...#......
.......#..
#.........
..........
......#...
.#........
.........#
..........
.......#..
#...#.....
    """
            .trimIndent()
    sumOfDistances(expandUniverse(parseMap(input), expanse = 100).keys) shouldBe 8410
  }

  @Test
  fun `gold test`() {
    sumOfDistances(
        expandUniverse(parseMap(loadResource("Day11")), expanse = 1000_000).keys) shouldBe
        625243292686
  }
}

/**
 * Cartesian product, but excludes pairs of the same element and pairs which reference same
 * elements.
 */
private fun Set<Point>.cartesianProduct(): List<Pair<Point, Point>> {
  return flatMapIndexed { index: Int, left: Point ->
    drop(index + 1).map { right -> left to right }
  }
}
