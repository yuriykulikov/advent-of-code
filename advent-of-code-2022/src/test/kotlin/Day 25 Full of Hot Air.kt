import io.kotest.assertions.assertSoftly
import io.kotest.matchers.shouldBe
import java.math.BigInteger
import org.junit.jupiter.api.Test

class `Day 25 Full of Hot Air` {
  @Test
  fun snafuTest() {
    "1".toSnafuInt() shouldBe 1
    "2".toSnafuInt() shouldBe 2
    "1=".toSnafuInt() shouldBe 3
    "1-".toSnafuInt() shouldBe 4
    "10".toSnafuInt() shouldBe 5
    "11".toSnafuInt() shouldBe 6
    "12".toSnafuInt() shouldBe 7
    "2=".toSnafuInt() shouldBe 8
    "2-".toSnafuInt() shouldBe 9
    "20".toSnafuInt() shouldBe 10
    "1=0".toSnafuInt() shouldBe 15
    "1-0".toSnafuInt() shouldBe 20
    "1=11-2".toSnafuInt() shouldBe 2022
    "1-0---0".toSnafuInt() shouldBe 12345
    "1121-1110-1=0".toSnafuInt() shouldBe 314159265
    "1=-0-2".toSnafuInt() shouldBe 1747
    "12111".toSnafuInt() shouldBe 906
    "2=0=".toSnafuInt() shouldBe 198
    "21".toSnafuInt() shouldBe 11
    "2=01".toSnafuInt() shouldBe 201
    "111".toSnafuInt() shouldBe 31
    "20012".toSnafuInt() shouldBe 1257
    "112".toSnafuInt() shouldBe 32
    "1=-1=".toSnafuInt() shouldBe 353
    "1-12".toSnafuInt() shouldBe 107
    "12".toSnafuInt() shouldBe 7
    "1=".toSnafuInt() shouldBe 3
    "122".toSnafuInt() shouldBe 37
    assertSoftly {
      testInput.lines().forEach { snafu -> snafuOf(snafu.toSnafuInt()) shouldBe snafu }
      snafuOf(1) shouldBe "1"
      snafuOf(2) shouldBe "2"
      snafuOf(3) shouldBe "1="
      snafuOf(3) shouldBe "1="
      snafuOf(4) shouldBe "1-"
      snafuOf(5) shouldBe "10"
      snafuOf(6) shouldBe "11"
      snafuOf(7) shouldBe "12"
      snafuOf(8) shouldBe "2="
      snafuOf(9) shouldBe "2-"
      snafuOf(10) shouldBe "20"
      snafuOf(11) shouldBe "21"
      snafuOf(31) shouldBe "111"
      snafuOf(15) shouldBe "1=0"
      snafuOf(20) shouldBe "1-0"
      snafuOf(32) shouldBe "112"
      snafuOf(37) shouldBe "122"
      snafuOf(107) shouldBe "1-12"
      snafuOf(198) shouldBe "2=0="
      snafuOf(201) shouldBe "2=01"
      snafuOf(353) shouldBe "1=-1="
      snafuOf(906) shouldBe "12111"
      snafuOf(1257) shouldBe "20012"
      snafuOf(1747) shouldBe "1=-0-2"
      snafuOf(2022) shouldBe "1=11-2"
      snafuOf(12345) shouldBe "1-0---0"
      snafuOf(314159265) shouldBe "1121-1110-1=0"
    }
  }
  @Test
  fun silverTest() {
    snafuSum(testInput) shouldBe 4890
    snafuOf(4890) shouldBe "2=-1=0"
  }

  @Test
  fun silver() {
    snafuSum(loadResource("day25")) shouldBe 36067808580527L
    snafuOf(36067808580527L) shouldBe "2-212-2---=00-1--102"
  }

  private fun snafuOf(number: Long): String {
    return generateSequence(number to "") { (num, snafu) ->
          val rem = (num) % 5
          val div = (num) / 5
          check(rem in 0..4)
          val carry = if (rem in 0..2) 0 else 1
          val next = div + carry
          next to
              when (rem) {
                0L -> "0"
                1L -> "1"
                2L -> "2"
                3L -> "="
                4L -> "-"
                else -> error("")
              } + snafu
        }
        .first { (number, _) -> number == 0L }
        .second
        .trimStart('0')
  }

  private fun snafuSum(testInput: String): Long {
    return testInput.lines().map { it.toSnafuInt() }.sum()
  }

  private fun String.toSnafuInt(): Long {
    return toCharArray()
        .reversed()
        .mapIndexed { index, char ->
          5.toBigInteger()
              .pow(index)
              .times(
                  when (char) {
                    '0' -> 0
                    '1' -> 1
                    '2' -> 2
                    '-' -> -1
                    '=' -> -2
                    else -> error("")
                  }.toBigInteger())
        }
        .reduce(BigInteger::add)
        .longValueExact()
  }

  private val testInput =
      """
1=-0-2
12111
2=0=
21
2=01
111
20012
112
1=-1=
1-12
12
1=
122
""".trimIndent()
}
