import io.kotest.matchers.shouldBe
import kotlinx.collections.immutable.persistentListOf
import org.junit.jupiter.api.Test

class `Day 23 Unstable Diffusion` {
  fun Point.around(): List<Point> =
      listOf(
          up().left(), down().left(), up().right(), down().right(), up(), left(), down(), right())
  fun Point.n() = up()
  fun Point.ne() = up().right()
  fun Point.nw() = up().left()
  fun Point.s() = down()
  fun Point.se() = down().right()
  fun Point.sw() = down().left()
  fun Point.w() = left()
  fun Point.e() = right()

  @Test
  fun silverTest() {
    countEmptySpaces(testInput) shouldBe 110
  }

  @Test
  fun silver() {
    countEmptySpaces(loadResource("day23")) shouldBe 3882
  }

  @Test
  fun goldTest() {
    runSimulation(testInput).count() shouldBe 20
  }

  @Test
  fun gold() {
    runSimulation(loadResource("day23")).count() shouldBe 1116
  }
  private fun countEmptySpaces(input: String): Int {
    val after10 = runSimulation(input).take(11).last()

    val maxX = after10.maxOf { it.x }
    val minX = after10.minOf { it.x }
    val maxY = after10.maxOf { it.y }
    val minY = after10.minOf { it.y }

    return (minY..maxY).sumOf { y -> (minX..maxX).count { x -> Point(x, y) !in after10 } }
  }

  private fun runSimulation(input: String): Sequence<Set<Point>> {
    val evalsSeed =
        persistentListOf<Pair<(Point, Set<Point>) -> Boolean, (Point) -> Point>>(
            { elf: Point, elves: Set<Point> ->
              elf.n() !in elves && elf.ne() !in elves && elf.nw() !in elves
            } to { elf -> elf.n() },
            { elf: Point, elves: Set<Point> ->
              elf.s() !in elves && elf.se() !in elves && elf.sw() !in elves
            } to { elf -> elf.s() },
            { elf: Point, elves: Set<Point> ->
              elf.w() !in elves && elf.nw() !in elves && elf.sw() !in elves
            } to { elf -> elf.w() },
            { elf: Point, elves: Set<Point> ->
              elf.e() !in elves && elf.ne() !in elves && elf.se() !in elves
            } to { elf -> elf.e() },
        )

    return generateSequence(parseMap(input).filterValues { it == '#' }.keys.toSet() to evalsSeed) {
            (elves, evals) ->
          val proposals =
              elves.map { elf ->
                elf to
                    if (elf.around().none { it in elves }) {
                      elf
                    } else {
                      evals.firstNotNullOfOrNull { (predicate, transform) ->
                        if (predicate(elf, elves)) transform(elf) else null
                      }
                          ?: elf
                    }
              }

          val newMap =
              proposals
                  .map { (current, proposed) ->
                    when {
                      current == proposed -> current
                      proposals.count { (_, otherProposal) -> otherProposal == proposed } == 1 ->
                          proposed
                      else -> current
                    }
                  }
                  .toSet()
          if (elves == newMap) null else newMap to evals.removeAt(0).add(evals.first())
        }
        .map { it.first }
  }

  private val testInput =
      """
....#..
..###.#
#...#.#
.#...##
#.###..
##.#.##
.#..#..
    """.trimIndent()
}
