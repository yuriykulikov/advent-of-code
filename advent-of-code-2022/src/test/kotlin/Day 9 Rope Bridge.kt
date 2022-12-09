import io.kotest.matchers.shouldBe
import kotlin.math.abs
import org.junit.jupiter.api.Test

class `Day 9 Rope Bridge` {
  @Test
  fun silverTest() {
    amountOfVisitedPoints(parseInput(testInput), true) shouldBe 13
  }

  @Test
  fun silver() {
    amountOfVisitedPoints(parseInput(loadResource("day9"))) shouldBe 6197
  }

  @Test
  fun goldTest() {
    amountOfVisitedPointsLongerRope(parseInput(largerInput), debug = true) shouldBe 36
  }

  @Test
  fun gold() {
    amountOfVisitedPointsLongerRope(parseInput(loadResource("day9"))) shouldBe 2562
  }
  private fun amountOfVisitedPoints(input: List<Point>, debug: Boolean = false): Int {
    val visited =
        input
            .scan(Point(0, 0) to Point(0, 0)) { (prevHead, prevTail), move ->
              val head = prevHead + move
              val diff = head - prevTail
              val isTooLong = diff.run { abs(x) > 1 || abs(y) > 1 }
              val tail =
                  when {
                    isTooLong -> prevTail + diff.direction()
                    else -> prevTail
                  }
              head to tail
            }
            .map { it.second }
            .toSet()

    if (debug) printMap(visited.associateWith { "#" })
    return visited.size
  }

  private fun amountOfVisitedPointsLongerRope(input: List<Point>, debug: Boolean = false): Int {
    val visited =
        input
            .scan(buildList { repeat(10) { add(Point(0, 0)) } }) { rope, move ->
              moveRope(rope, move)
            }
            .map { it.last() }
            .toSet()

    if (debug) printMap(visited.associateWith { "#" })
    return visited.size
  }

  /** Pulls the head and then all other segments as needed */
  private fun moveRope(rope: List<Point>, move: Point): List<Point> {
    return buildList {
      val movedRope = this
      add(rope.first() + move)
      rope.drop(1).forEach { point ->
        val diff = movedRope.last() - point
        val isTooLong = diff.run { abs(x) > 1 || abs(y) > 1 }
        when {
          isTooLong -> add(point + diff.direction())
          else -> add(point)
        }
      }
    }
  }
  private fun parseInput(input: String): List<Point> {
    val up = Point(0, 0).up()
    val down = Point(0, 0).down()
    val left = Point(0, 0).left()
    val right = Point(0, 0).right()

    return input.trim().lines().flatMap {
      val (direction, amount) = it.split(" ")
      val vector =
          when (direction) {
            "L" -> left
            "R" -> right
            "U" -> up
            "D" -> down
            else -> error("")
          }
      buildList { repeat(amount.toInt()) { add(vector) } }
    }
  }

  private val testInput = """
R 4
U 4
L 3
D 1
R 4
D 1
L 5
R 2
    """.trimIndent()

  val largerInput = """
R 5
U 8
L 8
D 3
R 17
D 10
L 25
U 20
    """.trimIndent()
}
