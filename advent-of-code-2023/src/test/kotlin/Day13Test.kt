import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test

class Day13Test {
  @Test
  fun `silver vertical`() {
    val lava =
        parseMap(
            """
#.##..##.
..#.##.#.
##......#
##......#
..#.##.#.
..##..##.
#.#.##.#.
    """
                .trimIndent())

    verticalMirror(lava) shouldBe 5
    horizontalMirror(lava) shouldBe 0
  }

  @Test
  fun `silver horizontal`() {
    val lava =
        parseMap(
            """
#...##..#
#....#..#
..##..###
#####.##.
#####.##.
..##..###
#....#..#
    """
                .trimIndent())
    horizontalMirror(lava) shouldBe 400
    verticalMirror(lava) shouldBe 0
  }

  @Test
  fun `silver example`() {
    """#.##..##.
..#.##.#.
##......#
##......#
..#.##.#.
..##..##.
#.#.##.#.

#...##..#
#....#..#
..##..###
#####.##.
#####.##.
..##..###
#....#..#
"""
        .trim()
        .split("\n\n")
        .map { parseMap(it) }
        .sumOf { lava -> score(lava) } shouldBe 405
  }

  @Test
  fun `silver test`() {
    loadResource("Day13")
        .trim()
        .split("\n\n")
        .map { parseMap(it) }
        .sumOf { lava -> horizontalMirror(lava) + (verticalMirror(lava)) } shouldBe 35691
  }

  @Test
  fun `gold example`() {
    """#.##..##.
..#.##.#.
##......#
##......#
..#.##.#.
..##..##.
#.#.##.#.

#...##..#
#....#..#
..##..###
#####.##.
#####.##.
..##..###
#....#..#
"""
        .trim()
        .split("\n\n")
        .map { parseMap(it) }
        .sumOf { lava -> unsmudgeAndScore(lava) } shouldBe 400
  }

  @Test
  fun `gold test`() {
    loadResource("Day13")
        .split("\n\n")
        .map { parseMap(it) }
        .sumOf { lava -> unsmudgeAndScore(lava) } shouldBe 39037
  }

  private fun unsmudgeAndScore(lava: Map<Point, Char>): Int {
    val initHor = horizontalMirror(lava)
    val initVer = verticalMirror(lava)
    val unsmudged =
        lava.keys
            .map { lava.plus(it to lava.getValue(it).invert()) }
            .first {
              verticalMirrors(it).minus(initVer).isNotEmpty() ||
                  horizontalMirrors(it).minus(initHor).isNotEmpty()
            }

    return (verticalMirrors(unsmudged).minus(initVer).firstOrNull() ?: 0) +
        (horizontalMirrors(unsmudged).minus(initHor).firstOrNull() ?: 0)
  }

  private fun score(lava: Map<Point, Char>): Int {
    return (horizontalMirror(lava) ?: 0) + (verticalMirror(lava) ?: 0)
  }

  private fun verticalMirror(lava: Map<Point, Char>): Int {
    return verticalMirrors(lava).firstOrNull() ?: 0
  }

  private fun verticalMirrors(lava: Map<Point, Char>): List<Int> {
    val maxX = lava.keys.maxOf { it.x }
    val maxY = lava.keys.maxOf { it.y }

    return (1..maxX).filter { xC ->
      (0..maxX).all { x ->
        (0..maxY).all { y ->
          val left = lava[Point(xC - x - 1, y)]
          val right = lava[Point(xC + x, y)]
          left == null || right == null || left == right
        }
      }
    }
  }

  private fun horizontalMirror(lava: Map<Point, Char>): Int {
    return horizontalMirrors(lava).firstOrNull() ?: 0
  }

  private fun horizontalMirrors(lava: Map<Point, Char>): List<Int> {
    val maxX = lava.keys.maxOf { it.x }
    val maxY = lava.keys.maxOf { it.y }

    return (1..maxY)
        .filter { yC ->
          (0..maxX).all { x ->
            (0..maxY).all { y ->
              val top = lava[Point(x, yC - y - 1)]
              val bottom = lava[Point(x, yC + y)]
              top == null || bottom == null || top == bottom
            }
          }
        }
        .map { it.times(100) }
  }
}

private fun Char.invert(): Char {
  return when (this) {
    '#' -> '.'
    '.' -> '#'
    else -> error("Unknown char $this")
  }
}
