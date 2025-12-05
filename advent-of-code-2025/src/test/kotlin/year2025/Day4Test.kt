package year2025

import Point
import down
import io.kotest.matchers.collections.shouldHaveSize
import io.kotest.matchers.shouldBe
import loadResource
import org.junit.jupiter.api.Test
import left
import parseMap
import right
import up

class Day4Test {
    private val example = """
..@@.@@@@.
@@@.@.@.@@
@@@@@.@.@@
@.@@@@..@.
@@.@@@@.@@
.@@@@@@@.@
.@.@.@.@@@
@.@@@.@@@@
.@@@@@@@@.
@.@.@@@.@.
    """.trimIndent()


    @Test
    fun `Silver example - accessible rolls`() {
        accessibleRolls(example).shouldHaveSize(13)
    }

    @Test
    fun `Silver - accessible rolls`() {
        accessibleRolls(loadResource("Day4")).shouldHaveSize(1489)
    }

    @Test
    fun `Gold example - remove accessible rolls`() {
        val initial = parseMap(example)
        val cleanedUpRolls = runCleanuSequence(initial)
        cleanedUpRolls
            .size
            .shouldBe(43)
    }


    @Test
    fun `Gold - remove accessible rolls`() {
        val initial = parseMap(loadResource("Day4"))
        val cleanedUpRolls = runCleanuSequence(initial)
        cleanedUpRolls
            .size
            .shouldBe(8890)
    }

    private fun Day4Test.runCleanuSequence(initial: Map<Point, Char>): Set<Point> {
        val cleanedUp = generateSequence(initial) { map ->
            val accessibleRolls = accessibleRolls(map)
            if (accessibleRolls.isEmpty()) null else map.minus(accessibleRolls)
        }.last()

        val cleanedUpRolls = initial.filterValues { it == '@' }.keys - cleanedUp.filterValues { it == '@' }.keys
        return cleanedUpRolls
    }

    private fun accessibleRolls(example: String): List<Point> {
        val map = parseMap(example)
        return accessibleRolls(map)
    }

    private fun accessibleRolls(map: Map<Point, Char>): List<Point> {
        return map
            .filterValues { it == '@' }
            .keys
            .filter { point -> point.surroundings().mapNotNull { map[it] }.count { it == '@' } < 4 }
    }

    private fun Point.surroundings(): List<Point> {
        return listOf(
            up(), down(), left(), right(), up().left(), up().right(), down().left(), down().right()
        )
    }
}

