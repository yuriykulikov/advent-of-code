package year2025

import io.kotest.matchers.collections.shouldContainExactly
import io.kotest.matchers.shouldBe
import loadResource
import org.junit.jupiter.api.Test
import kotlin.math.absoluteValue

class Day1Test {
    private val testInput = """
L68
L30
R48
L5
R60
L55
L1
L99
R14
L82
    """.trimIndent()

    @Test
    fun `silver example`() {
        val input = testInput
        runSequence(input).shouldContainExactly(
            50,
            82,
            52,
            0,
            95,
            55,
            0,
            99,
            0,
            14,
            32,
        )
    }

    @Test
    fun `silver`() {
        runSequence(loadResource("Day1"))
            .count { it == 0 }
            .shouldBe(1055)
    }

    @Test
    fun `gold example`() {
        val input = testInput

        inputToInt(input)
            .scan(50 to 0) { acc, value -> rotateSafeLock(acc.first, value) }.shouldContainExactly(
                50 to 0,
                82 to 1,
                52 to 0,
                0 to 1,
                95 to 0,
                55 to 1,
                0 to 1,
                99 to 0,
                0 to 1,
                14 to 0,
                32 to 1,
            )
    }

    @Test
    fun `gold`() {

        inputToInt(loadResource("Day1"))
            .scan(50 to 0) { acc, value -> rotateSafeLock(acc.first, value) }
            .sumOf { (_, clicks )-> clicks }
            .shouldBe(6386)
    }

    private fun runSequence(input: String): List<Int> {
        return inputToInt(input)
            .scan(50) { acc, value -> rotateSafeLock(acc, value).first }
    }

    private fun inputToInt(input: String): List<Int> {
        return input.lines().filterNot { it.isEmpty() }
            .map {
                val sign = if (it.startsWith("R")) 1 else -1
                it.drop(1).toInt().times(sign)
            }
    }


    private fun rotateSafeLock(acc: Int, value: Int): Pair<Int, Int> {
        val num = acc + value
        val ret = when {
            num == 0 -> 0 to 1
            num in 1..99 -> num to 0
            num > 99 -> {
                num.rem(100) to num.div(100)
            }
            else -> {
                val next = Math.floorMod(num, 100)
                val clicks = if (acc == 0) num.div(100).absoluteValue else num.div(100).absoluteValue + 1
                next to clicks
            }
        }
        check(ret.first in 0..99) {
            "Failed for $num: $ret"
        }
        return ret
    }


}