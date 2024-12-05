package year2024

import io.kotest.matchers.shouldBe
import loadResource
import org.junit.jupiter.api.Test

class Day5Test {
    private val example = """
        47|53
        97|13
        97|61
        97|47
        75|29
        61|13
        75|53
        29|13
        97|29
        53|29
        61|53
        97|53
        61|29
        47|13
        75|47
        97|75
        47|61
        75|61
        47|29
        75|13
        53|13

        75,47,61,53,29
        97,61,53,29,13
        75,29,13
        75,97,47,61,53
        61,13,29
        97,13,75,29,47
    """.trimIndent()

    @Test
    fun `silver example`() {
        val sumOf = sumOfMiddleNumbersOfValidInstructionSets(example)
        sumOf shouldBe 143
    }

    @Test
    fun `silver`() {
        val sumOf = sumOfMiddleNumbersOfValidInstructionSets(loadResource("Day5"))
        sumOf shouldBe 6505
    }

    @Test
    fun `gold example`() {
        val sumOf = sumOfMiddleNumbersOfCorrectedSets(example)
        sumOf shouldBe 123
    }

    @Test
    fun `gold`() {
        val sumOf = sumOfMiddleNumbersOfCorrectedSets(loadResource("Day5"))
        sumOf shouldBe 6897
    }

    private fun sumOfMiddleNumbersOfValidInstructionSets(input: String): Int {
        val (rulesSection, instructionsSection) = input.split("\n\n")
        val instructions = instructionsSection.lines().map { line -> line.trim().split(",").map { it.toInt() } }
        val rules = rulesSection.lines().map { it.substringBefore("|").toInt() to it.substringAfter("|").toInt() }

        val sumOf = instructions.filter { instruction ->
            val indexed = instruction.mapIndexed { index, i -> i to index }.toMap()
            rules.all { (l, r) ->
                // rule is passed when either numbers are not present or pass
                indexed[l] == null || indexed[r] == null || indexed[l]!! < indexed[r]!!
            }
        }
            .sumOf { it[it.size / 2] }
        return sumOf
    }

    private fun sumOfMiddleNumbersOfCorrectedSets(input: String): Int {
        val (rulesSection, instructionsSection) = input.split("\n\n")
        val instructions = instructionsSection.lines().map { line -> line.trim().split(",").map { it.toInt() } }
        val rules = rulesSection.lines().map { it.substringBefore("|").toInt() to it.substringAfter("|").toInt() }.toSet()

        val sumOf = instructions.filterNot { instruction ->
            val indexed = instruction.mapIndexed { index, i -> i to index }.toMap()
            rules.all { (l, r) ->
                // rule is passed when either numbers are not present or pass
                indexed[l] == null || indexed[r] == null || indexed[l]!! < indexed[r]!!
            }
        }

            .map {
                it.sortedWith { o1, o2 ->
                    when {
                    rules.contains(o1 to o2) -> 1
                    rules.contains(o2 to o1) -> -1
                    else -> 0
                    }
                }
            }
            .sumOf { it[it.size / 2] }
        return sumOf
    }
}