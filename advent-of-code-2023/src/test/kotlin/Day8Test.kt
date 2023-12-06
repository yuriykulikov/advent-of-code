import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test

class Day8Test {
  private val testInput2 =
      """RL

AAA = (BBB, CCC)
BBB = (DDD, EEE)
CCC = (ZZZ, GGG)
DDD = (DDD, DDD)
EEE = (EEE, EEE)
GGG = (GGG, GGG)
ZZZ = (ZZZ, ZZZ)
"""

  private val testInput6 = """LLR

AAA = (BBB, BBB)
BBB = (AAA, ZZZ)
ZZZ = (ZZZ, ZZZ)
"""

  data class Node(val name: String, val left: String, val right: String)

  @Test
  fun `silver test (example)`() {
    countSteps(testInput2).shouldBe(2)
    countSteps(testInput6).shouldBe(6)
  }

  @Test
  fun `silver test`() {
    countSteps(loadResource("Day8")).shouldBe(12361)
  }

  @Test
  fun `gold test (example)`() {
    countStepsGhost(
            """LR

11A = (11B, XXX)
11B = (XXX, 11Z)
11Z = (11B, XXX)
22A = (22B, XXX)
22B = (22C, 22C)
22C = (22Z, 22Z)
22Z = (22B, 22B)
XXX = (XXX, XXX)""")
        .shouldBe(6)
  }

  @Test
  fun `gold test`() {
    countStepsGhost(loadResource("Day8")).shouldBe(18215611419223)
  }

  private fun countSteps(input: String): Int {
    val (rl, map) = parseInput(input)
    return generateSequence { rl.toCharArray().asSequence() }
        .flatten()
        .scan(map.getValue("AAA")) { node, c ->
          if (c == 'L') map.getValue(node.left) else map.getValue(node.right)
        }
        .takeWhile { it.name != "ZZZ" }
        .count()
  }

  private fun countStepsGhost(input: String): Long {
    val (rl, map) = parseInput(input)
    val starts = map.values.filter { it.name.endsWith("A") }

    return starts
        .map { start ->
          generateSequence { rl.toCharArray().asSequence() }
              .flatten()
              .scan(start) { node, c ->
                if (c == 'L') map.getValue(node.left) else map.getValue(node.right)
              }
              .takeWhile { !it.name.endsWith("Z") }
              .count()
              .toLong()
        }
        .fold(1L) { acc, i -> lcm(acc, i) }
  }

  private fun lcm(a: Long, b: Long): Long {
    return a * b / gcd(a, b)
  }

  private fun gcd(a: Long, b: Long): Long {
    return if (b == 0L) a else gcd(b, a % b)
  }

  private fun parseInput(input: String): Pair<String, Map<String, Node>> {
    val rl = input.lines().first()
    val map =
        input
            .lines()
            .asSequence()
            .drop(2)
            .filterNot { it.isEmpty() }
            .map {
              val (name, children) = it.split(" = ")
              val (l, r) = children.replace("(", "").replace(")", "").split(", ")
              Node(name.trim(), l, r)
            }
            .associateBy { it.name }
    return Pair(rl, map)
  }
}
