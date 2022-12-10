import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test

class `Day 10 Cathode-Ray Tube` {
  @Test
  fun silverTest() {
    with(signalStrength(parse(testInput))) {
      get(20) shouldBe 420
      get(60) shouldBe 1140
      get(100) shouldBe 1800
      get(140) shouldBe 2940
      get(180) shouldBe 2880
      get(220) shouldBe 3960
    }
    sumOfSignals(signalStrength(parse(testInput))) shouldBe 13140
  }

  @Test
  fun silver() {
    sumOfSignals(signalStrength(parse(loadResource("day10")))) shouldBe 13680
  }

  @Test
  fun goldTest() {
    val operations = parse(testInput)
    val map = draw(operations)
    map.values.filter { it == "#" }.size shouldBe 124
    printMap(map)
  }

  @Test
  fun gold() {
    val operations = parse(loadResource("day10"))
    val map = draw(operations)
    map.values.filter { it == "#" }.size shouldBe 102
    printMap(map)
  }

  private fun draw(operations: Sequence<Int?>): Map<Point, String> {
    val map =
        listOf(
                Point(0, 0)..Point(39, 0),
                Point(0, 1)..Point(39, 1),
                Point(0, 2)..Point(39, 2),
                Point(0, 3)..Point(39, 3),
                Point(0, 4)..Point(39, 4),
                Point(0, 5)..Point(39, 5),
            )
            .flatten()
            .zip(xRegister(operations).drop(1))
            .associate { (ray, pos) ->
              val visible = ray.x in (pos - 1..pos + 1)
              ray to if (visible) "#" else " "
            }
    return map
  }

  private fun sumOfSignals(signal: List<Int>): Int {
    return (20..220 step 40).sumOf { signal[it] }
  }

  private fun signalStrength(operations: Sequence<Int?>): List<Int> {
    return xRegister(operations).mapIndexed { index, x -> index * x }
  }

  private fun xRegister(operations: Sequence<Int?>) =
      operations
          .fold(listOf(1) to 1) { (list, x), operation ->
            if (operation == null) {
              list.plus(x) to x
            } else {
              list.plus(x).plus(x) to x + operation
            }
          }
          .first

  private fun parse(testInput: String): Sequence<Int?> {
    return testInput
        .lines()
        .asSequence()
        .filter { it.isNotBlank() }
        .map { line -> line.takeUnless { it == "noop" }?.substringAfter(" ")?.toInt() }
  }

  private val testInput =
      """
addx 15
addx -11
addx 6
addx -3
addx 5
addx -1
addx -8
addx 13
addx 4
noop
addx -1
addx 5
addx -1
addx 5
addx -1
addx 5
addx -1
addx 5
addx -1
addx -35
addx 1
addx 24
addx -19
addx 1
addx 16
addx -11
noop
noop
addx 21
addx -15
noop
noop
addx -3
addx 9
addx 1
addx -3
addx 8
addx 1
addx 5
noop
noop
noop
noop
noop
addx -36
noop
addx 1
addx 7
noop
noop
noop
addx 2
addx 6
noop
noop
noop
noop
noop
addx 1
noop
noop
addx 7
addx 1
noop
addx -13
addx 13
addx 7
noop
addx 1
addx -33
noop
noop
noop
addx 2
noop
noop
noop
addx 8
noop
addx -1
addx 2
addx 1
noop
addx 17
addx -9
addx 1
addx 1
addx -3
addx 11
noop
noop
addx 1
noop
addx 1
noop
noop
addx -13
addx -19
addx 1
addx 3
addx 26
addx -30
addx 12
addx -1
addx 3
addx 1
noop
noop
noop
addx -9
addx 18
addx 1
addx 2
noop
noop
addx 9
noop
noop
noop
addx -1
addx 2
addx -37
addx 1
addx 3
noop
addx 15
addx -21
addx 22
addx -6
addx 1
noop
addx 2
addx 1
noop
addx -10
noop
noop
addx 20
addx 1
addx 2
addx 2
addx -6
addx -11
noop
noop
noop
    """.trimIndent()
}
