package year2024

import io.kotest.matchers.shouldBe
import loadResource
import org.junit.jupiter.api.Test

class Day11Test {
    @Test
    fun `split numbers in half`() {
        val ball = 100500L
        val half = ball.digits() / 2
        half shouldBe 3
        val tenToPowerOf = tenToPowerOf(half)
        ball.div(tenToPowerOf) shouldBe 100
        ball.rem(tenToPowerOf) shouldBe 500
    }

    @Test
    fun example0() {
        val initial = "125 17".split(" ").map { it.toLong() }
        initial.sumOf {
            blink(it, times = 6)
        } shouldBe 22
    }

    @Test
    fun example1() {
        val initial = "125 17".split(" ").map { it.toLong() }
        initial.sumOf {
            blink(it, times = 25)
        } shouldBe 55312
    }

    @Test
    fun `silver test`() {
        val initial = loadResource("Day11").split(" ").map { it.toLong() }
        initial.sumOf {
            blink(it, times = 25)
        } shouldBe 211306
    }

    @Test
    fun `gold test`() {
        val initial = loadResource("Day11").split(" ").map { it.toLong() }
        initial.sumOf {
            blink(it, times = 75)
        } shouldBe 250783680217283L
        println(cache.size)
    }

    private val cache = mutableMapOf<Pair<Long, Int>, Long>()

    private fun blink(ball: Long, times: Int): Long {
        if (times == 0) return 1
        return cache.getOrPut(ball to times) {

            val remBlinks = times - 1
            when {
                ball == 0L -> blink(1L, remBlinks)
                ball.digits().rem(2) == 0 -> {
                    val half = ball.digits() / 2
                    val tenToPowerOf = tenToPowerOf(half)
                    blink(ball.div(tenToPowerOf), remBlinks) + blink(ball.rem(tenToPowerOf), remBlinks)
                }

                else -> blink(ball * 2024, remBlinks)
            }
        }
    }

}

fun Long.digits(): Int {
    var num = this
    var count = 0

    do {
        count++
        num /= 10
    } while (num != 0L)

    return count
}

private fun tenToPowerOf(digits: Int): Long {
    var num = 1L
    for (i in (digits - 1) downTo 0) {
        num *= 10
    }
    return num
}
