import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test

/** [Day 1: Calibration](https://adventofcode.com/2023/day/1) */
class Day1Test {
  private fun calibrationValue(input: String): Int {
    return input.lines().sumOf { line ->
      line.first { it.isDigit() }.digitToInt() * 10 + line.last { it.isDigit() }.digitToInt()
    }
  }

  private fun calibrationValueWithWords(input: String): Int {
    val wordsAndDigits = words + listOf("0", "1", "2", "3", "4", "5", "6", "7", "8", "9")
    return input.lines().sumOf { line ->
      val (_, firstDigit) = line.findAnyOf(wordsAndDigits) ?: error("No digit found in $line")
      val (_, lastDigit) = line.findLastAnyOf(wordsAndDigits) ?: error("No digit found in $line")
      firstDigit.wordOrDigitToInt() * 10 + lastDigit.wordOrDigitToInt()
    }
  }

  private val words = listOf("one", "two", "three", "four", "five", "six", "seven", "eight", "nine")

  private fun String.wordOrDigitToInt(): Int {
    return toIntOrNull() ?: (words.indexOf(this) + 1)
  }

  @Test
  fun `silver example`() {
    calibrationValue("""1abc2
pqr3stu8vwx
a1b2c3d4e5f
treb7uchet""") shouldBe 142
  }

  @Test
  fun `silver test`() {
    calibrationValue(loadResource("Day1")) shouldBe 56042
  }

  @Test
  fun `gold example`() {
    calibrationValueWithWords(
        """two1nine
eightwothree
abcone2threexyz
xtwone3four
4nineeightseven2
zoneight234
7pqrstsixteen""") shouldBe
        281
  }

  @Test
  fun `gold test`() {
    calibrationValueWithWords(loadResource("Day1")) shouldBe 55358
  }
}
