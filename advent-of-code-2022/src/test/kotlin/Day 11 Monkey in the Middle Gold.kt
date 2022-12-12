import `Day 11 Monkey in the Middle Gold`.RNS.Companion.toRNS
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test

/** 15:15 */
class `Day 11 Monkey in the Middle Gold` {
  data class RNS(val primes: List<Int>, val values: List<Int>) {
    fun divisibleBy(testDivisible: Int): Boolean {
      val primeIndex = primes.indexOf(testDivisible)
      return values[primeIndex] == 0
    }

    operator fun plus(right: Int): RNS {
      val added =
          values.mapIndexed { index, left ->
            val prime = primes[index]
            (left.plus(right.rem(prime))).rem(prime)
          }
      return copy(values = added)
    }

    operator fun times(right: RNS): RNS {
      val added =
          primes.mapIndexed { index, prime -> values[index].times(right.values[index]).rem(prime) }
      return copy(values = added)
    }

    operator fun times(right: Int): RNS = times(right.toRNS(primes))

    companion object {
      fun String.toRNS(primes: List<Int>): RNS = toInt().toRNS(primes)
      fun Int.toRNS(primes: List<Int>) = RNS(primes = primes, values = primes.map { rem(it) })
    }
  }

  sealed class Operation : (RNS) -> RNS

  data class Multiply(val value: Int) : Operation() {
    override fun invoke(rns: RNS): RNS = rns * value
  }

  data class Add(val value: Int) : Operation() {
    override fun invoke(rns: RNS): RNS = rns + value
  }

  object Square : Operation() {
    override fun invoke(rns: RNS): RNS = rns * rns
  }

  data class Monkey(
      val index: Int = 0,
      val items: List<RNS>,
      val operation: Operation,
      val testDivisible: Int,
      val pass: Int,
      val fail: Int,
      val inspections: Long = 0
  )

  @Test
  fun goldTest() {
    monkeyBusiness(parse(testInput), 10000) shouldBe 2713310158L
  }

  @Test
  fun gold() {
    monkeyBusiness(parse(loadResource("day11")), 10000) shouldBe 39109444654
  }

  private fun monkeyBusiness(monkeys: List<Monkey>, rounds: Int): Long =
      generateSequence(monkeys) { round(it) }
          .drop(1)
          .take(rounds)
          .last()
          .asSequence()
          .map { it.inspections }
          .onEachIndexed { index, i -> println("Monkey $index inspected items $i times.") }
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

  data class Pass(val index: Int, val value: RNS)

  private fun monkeyRound(monkey: Monkey): Pair<Monkey, List<Pass>> {
    val passes =
        monkey.items
            .map { item -> monkey.operation(item) }
            .map { item ->
              Pass(
                  value = item,
                  index = if (item.divisibleBy(monkey.testDivisible)) monkey.pass else monkey.fail)
            }
    check(passes.size == monkey.items.size)
    check(passes.none { it.index == monkey.index })
    return monkey.copy(
        items = emptyList(),
        inspections = monkey.inspections + monkey.items.size,
    ) to passes
  }

  private fun parse(testInput: String): List<Monkey> {
    val primes =
        testInput
            .lines()
            .filter { it.contains("  Test: divisible by ") }
            .map { it.substringAfterLast(" ").toInt() }
            .sorted()

    return testInput.split("\n\n").mapIndexed { index, string ->
      val lines = string.lines()
      val starting = lines[1].substringAfter(": ").split(", ").map { it.toRNS(primes) }

      val operation =
          lines[2].substringAfter("new = old ").let {
            when {
              it == "* old" -> Square
              it.startsWith("*") -> Multiply(it.substringAfter("* ").toInt())
              it.startsWith("+") -> Add(it.substringAfter("+ ").toInt())
              else -> error("")
            }
          }

      val test = lines[3].substringAfterLast(" ").toInt()
      val pass = lines[4].substringAfter(" monkey ").toInt()
      val fail = lines[5].substringAfter(" monkey ").toInt()
      Monkey(index, starting, operation, test, pass, fail)
    }
  }

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
