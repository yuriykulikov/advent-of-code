import io.kotest.matchers.collections.shouldStartWith
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test

/** 15:15 */
class `Day 11 Monkey in the Middle` {
  @Test
  fun parsing() {
    parse(testInput) shouldStartWith
        Monkey(
            items = listOf(79.toLong(), 98.toLong()),
            operation = Multiply(19.toLong()),
            testDivisible = 23,
            pass = 2,
            fail = 3,
        )
  }

  @Test
  fun silverTestOneRound() {
    generateSequence(parse(testInput)) { round(it) }
        .onEach { monkeys ->
          println("--------------")
          monkeys.forEach { println("Monkey ${it.index}: ${it.items.joinToString()}") }
        }
        .drop(1)
        .take(1)
        .last()
        .map { it.items } shouldBe
        listOf(
            listOf(20, 23, 27, 26).map { it.toLong() },
            listOf(2080, 25, 167, 207, 401, 1046),
            emptyList(),
            emptyList(),
        )
  }

  @Test
  fun silverTestTwoRounds() {
    generateSequence(parse(testInput)) { round(it) }
        .onEach { monkeys ->
          println("--------------")
          monkeys.forEach { println("Monkey ${it.index}: ${it.items.joinToString()}") }
        }
        .drop(1)
        .take(2)
        .last()
        .map { it.items } shouldBe
        listOf(
            listOf(695, 10, 71, 135, 350),
            listOf(43, 49, 58, 55, 362),
            emptyList(),
            emptyList(),
        )
  }

  @Test
  fun silverTest() {
    monkeyBusiness(parse(testInput)) shouldBe 10605
  }
  @Test
  fun silver() {
    monkeyBusiness(parse(loadResource("day11"))) shouldBe 120756
  }
  private fun monkeyBusiness(monkeys: List<Monkey>, rounds: Int = 20): Long =
      generateSequence(monkeys) { round(it) }
          .drop(1)
          .take(rounds)
          .last()
          .asSequence()
          .map { it.inspections }
          .sortedDescending()
          .take(2)
          .reduce { acc, inspections -> acc * inspections }

  private fun round(monkeys: List<Monkey>): List<Monkey> {
    return monkeys.fold(monkeys) { acc, monkey ->
      val (mutatedMonkey, passes) = monkeyRound(acc[monkey.index])

      acc.map { otherMonkey ->
            if (otherMonkey.index == mutatedMonkey.index) mutatedMonkey
            else {
              val passedToThisMonkey =
                  passes.filter { it.index == otherMonkey.index }.map { it.value }
              otherMonkey.copy(items = otherMonkey.items + passedToThisMonkey)
            }
          }
          .also { check(acc.sumOf { it.items.size } == monkeys.sumOf { it.items.size }) }
    }
  }

  data class Pass(val index: Int, val value: Long)
  private fun monkeyRound(monkey: Monkey): Pair<Monkey, List<Pass>> {
    val passes =
        monkey.items
            .map { item -> monkey.operation(item) / 3 }
            .map { item ->
              Pass(
                  value = item,
                  index = if (item.rem(monkey.testDivisible) == 0L) monkey.pass else monkey.fail)
            }
    check(passes.size == monkey.items.size)
    check(passes.none { it.index == monkey.index })
    return monkey.copy(
        items = emptyList(),
        inspections = monkey.inspections + monkey.items.size,
    ) to passes
  }

  private fun parse(testInput: String): List<Monkey> {
    return testInput.split("\n\n").mapIndexed { index, string ->
      val lines = string.lines()
      val starting = lines[1].substringAfter(": ").split(", ").map { it.toLong() }

      val operation =
          lines[2].substringAfter("new = old ").let {
            when {
              it == "* old" -> Square
              it.startsWith("*") -> Multiply(it.substringAfter("* ").toLong())
              it.startsWith("+") -> Add(it.substringAfter("+ ").toLong())
              else -> error("")
            }
          }

      val test = lines[3].substringAfterLast(" ").toInt()
      val pass = lines[4].substringAfter(" monkey ").toInt()
      val fail = lines[5].substringAfter(" monkey ").toInt()
      Monkey(index, starting, operation, test, pass, fail)
    }
  }

  sealed class Operation : (Long) -> Long
  data class Multiply(val value: Long) : Operation() {
    override fun invoke(p1: Long): Long = p1 * value
  }
  data class Add(val value: Long) : Operation() {
    override fun invoke(p1: Long): Long = p1 + value
  }
  object Square : Operation() {
    override fun invoke(p1: Long): Long = p1 * p1
  }
  data class Monkey(
      val index: Int = 0,
      val items: List<Long>,
      val operation: Operation,
      val testDivisible: Int,
      val pass: Int,
      val fail: Int,
      val inspections: Long = 0
  )

  private val testInput =
      """
Monkey 0:
  Starting items: 79, 98
  Operation: new = old * 19
  Test: divisible by 23
    If true: throw to monkey 2
    If false: throw to monkey 3

Monkey 1:
  Starting items: 54, 65, 75, 74
  Operation: new = old + 6
  Test: divisible by 19
    If true: throw to monkey 2
    If false: throw to monkey 0

Monkey 2:
  Starting items: 79, 60, 97
  Operation: new = old * old
  Test: divisible by 13
    If true: throw to monkey 1
    If false: throw to monkey 3

Monkey 3:
  Starting items: 74
  Operation: new = old + 3
  Test: divisible by 17
    If true: throw to monkey 0
    If false: throw to monkey 1
    """.trimIndent()
}
