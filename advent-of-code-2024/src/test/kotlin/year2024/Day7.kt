package year2024

import io.kotest.matchers.shouldBe
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.runBlocking
import loadResource
import org.junit.jupiter.api.Test

class Day7 {
    private val example = """
190: 10 19
3267: 81 40 27
83: 17 5
156: 15 6
7290: 6 8 6 15
161011: 16 10 13
192: 17 8 14
21037: 9 7 18 13
292: 11 6 16 20
"""

    @Test
    fun `silver test`() {
        solves(3267, listOf(81, 40, 27)).shouldBe(true)
    }

    @Test
    fun `silver example`() {
        example.lines().filter { it.isNotBlank() }.mapNotNull { line ->
            val left = line.substringBefore(":").toLong()
            val right = line.substringAfter(":").trim().split(" ").map { it.toLong() }
            if (solves(left, right)) left else null
        }.sum().shouldBe(3749)
    }

    @Test
    fun `silver`() {
        loadResource("Day7").lines().filter { it.isNotBlank() }.mapNotNull { line ->
            val left = line.substringBefore(":").toLong()
            val right = line.substringAfter(":").trim().split(" ").map { it.toLong() }
            if (solves(left, right)) left else null
        }.sum().shouldBe(21572148763543L)
    }

    @Test
    fun `gold example`() {
        example.lines().filter { it.isNotBlank() }.mapNotNull { line ->
            val left = line.substringBefore(":").toLong()
            val right = line.substringAfter(":").trim().split(" ").map { it.toLong() }
            if (solves2(left, right)) left else null
        }.sum().shouldBe(11387)
    }

    @Test
    fun `gold`() {
        loadResource("Day7").lines().filter { it.isNotBlank() }.mapNotNull { line ->
            val left = line.substringBefore(":").toLong()
            val right = line.substringAfter(":").trim().split(" ").map { it.toLong() }
            if (solves2(left, right)) left else null
        }.sum().shouldBe(581941094529163)
    }

    private fun solves(left: Long, right: List<Long>): Boolean {
        val l = right[0]
        val r = right[1]
        val rem = right.drop(2)
        return solves(left, l + r, rem) || solves(left, l * r, rem)
    }

    fun solves(left: Long, acc: Long, tail: List<Long>): Boolean {
        if (tail.isEmpty()) return left == acc
        val hyp1 = solves(left, acc + tail.first(), tail.drop(1))
        val hyp2 = solves(left, acc * tail.first(), tail.drop(1))
        return hyp1 || hyp2
    }

    private fun solves2(left: Long, right: List<Long>): Boolean {
        val l = right[0]
        val r = right[1]
        val rem = right.drop(2)
        // this is slower because it calculates another useless branch
        // return solves2(left, 0, right)
        return solves2(left, l + r, rem) || solves2(left, l * r, rem) || solves2(left, l.conc(r), rem)
    }

    /**
     * I am pretty sure it can be done right to left which will be faster
     */
    fun solves2(left: Long, acc: Long, tail: List<Long>): Boolean {
        if (acc > left) return false
        if (tail.isEmpty()) return left == acc
        val remTail = tail.drop(1)
        val first = tail.first()
        return solves2(left, acc + first, remTail) || solves2(left, acc * first, remTail) || solves2(
            left,
            acc.conc(first),
            remTail
        )
    }
}

/**
 * The concatenation operator (||) combines the digits from its left and right inputs into a single number.
 */
private fun Long.conc(other: Long): Long {
    var digits = 1
    var rem = other
    while (rem > 0) {
        digits *= 10
        rem /= 10
    }
    return this * digits + other
}
