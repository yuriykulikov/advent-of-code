import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test

class `Day 5 Supply Stacks` {
  @Test
  fun silverTest() {
    message(testInput) shouldBe "CMZ"
  }
  @Test
  fun silver() {
    message(loadResource("day5")) shouldBe "VRWBSFZWM"
  }

  @Test
  fun goldTest() {
    message9001(testInput) shouldBe "MCD"
  }
  @Test
  fun gold() {
    message9001(loadResource("day5")) shouldBe "RBTWJWMCF"
  }
  private fun message(input: String): String {
    val stacks = initStacks(input)

    parseMoves(input).forEach { move ->
      repeat(move.howMany) {
        val pop = stacks[move.from - 1].removeLast()
        stacks[move.to - 1].add(pop)
      }
    }

    return stacks.mapNotNull { it.lastOrNull() }.joinToString("")
  }

  private fun message9001(input: String): String {
    val stacks = initStacks(input)

    parseMoves(input).forEach { move ->
      val from = stacks[move.from - 1]
      val popped = from.takeLast(move.howMany)
      repeat(move.howMany) { from.removeLast() }
      stacks[move.to - 1].addAll(popped)
    }

    return stacks.mapNotNull { it.lastOrNull() }.joinToString("")
  }
  private fun parseMoves(input: String): List<Move> {
    return input.split("\n\n").last().lines().map {
      // move 1 from 2 to 1
      val (howMany, from, to) = it.split("move ", " from ", " to ").drop(1)
      Move(howMany.toInt(), from.toInt(), to.toInt())
    }
  }
  private fun initStacks(input: String): List<MutableList<Char>> {
    val stacks = (0..10).map { ArrayDeque<Char>() }.toMutableList()
    input
        .split("\n\n")
        .first()
        .lines()
        .dropLast(1)
        .map { line -> (1 until line.lastIndex step 4).map { index -> line[index] } }
        // fill the stacks bottom-up
        .reversed()
        .forEach { line ->
          line.forEachIndexed { index, char ->
            if (char != ' ') {
              stacks[index].add(char)
            }
          }
        }
    return stacks
  }

  data class Move(val howMany: Int, val from: Int, val to: Int)
}

private val testInput =
    """
    [D]    
[N] [C]    
[Z] [M] [P]
 1   2   3 

move 1 from 2 to 1
move 3 from 1 to 3
move 2 from 2 to 1
move 1 from 1 to 2
""".trimIndent()
