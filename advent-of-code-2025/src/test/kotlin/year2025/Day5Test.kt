package year2025

import io.kotest.matchers.collections.shouldHaveSize
import io.kotest.matchers.shouldBe
import loadResource
import org.junit.jupiter.api.Test
import kotlin.math.max

class Day5Test {
    private val example = """
3-5
10-14
16-20
12-18

1
5
8
11
17
32
    """.trimIndent()


    @Test
    fun `Silver example - How many of the available ingredient IDs are fresh`() {
        fresh(example).shouldHaveSize(3)
    }

    @Test
    fun `Silver - How many of the available ingredient IDs are fresh`() {
        fresh(loadResource("Day5")).shouldHaveSize(726)
    }

    @Test
    fun `Gold example - How many ingredient IDs are considered to be fresh according to the fresh ingredient ID ranges`() {
        allFresh(example).shouldBe(14)
    }

    @Test
    fun `Gold - How many ingredient IDs are considered to be fresh according to the fresh ingredient ID ranges`() {
        allFresh(loadResource("Day5")).shouldBe(354226555270043L)
    }

    private fun fresh(database: String): List<Long> {
        val (rangesStr, idsStr) = database.split("\n\n")
        val ranges = rangesStr.lines().map { line ->
            val (l, r) = line.split("-").map { it.toLong() }
            l..r
        }

        val ids = idsStr.lines().map { it.toLong() }

        return ids.filter { id -> ranges.any { range -> id in range } }

    }

    /**
     * Hello RangeSet,
     * - Day 15 Beacon Exclusion Zone
     * - Day 5: If You Give A Seed A Fertilizer
     */
    private fun allFresh(database: String): Long {
        val (rangesStr, _) = database.split("\n\n")

        val ranges = rangesStr.lines().map { line ->
            val (l, r) = line.split("-").map { it.toLong() }
            l..r
        }

        val joined = ranges
            .sortedBy { it.first }
            // fold overlapping ranges
            .fold(emptyList()) { acc: List<LongRange>, next: LongRange ->
                when {
                    acc.isEmpty() -> acc.plusElement(next)
                    acc.last().last >= next.first -> {
                        val l = acc.last().first
                        val r = max(acc.last().last, next.last)
                        acc.dropLast(1).plusElement(l..r)
                    }
                    else -> acc.plusElement(next)
                }
            }


        return joined.sumOf { it.last - it.first + 1 }
    }
}

