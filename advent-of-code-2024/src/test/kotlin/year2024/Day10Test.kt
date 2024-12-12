package year2024

import Point
import down
import io.kotest.matchers.shouldBe
import left
import loadResource
import org.junit.jupiter.api.Test
import parseMap
import print
import right
import up

class Day10Test {
    private val example = """
89010123
78121874
87430965
96549874
45678903
32019012
01329801
10456732
    """.trimIndent()

    @Test
    fun `silver example`() {
        trailheadScores(parseMap(example)) shouldBe 36
    }

    @Test
    fun `silver test`() {
        trailheadScores(parseMap(loadResource("Day10"))) shouldBe 776
    }

    @Test
    fun `gold example`() {
        trailheadRatings(parseMap(example)) shouldBe 81
    }

    @Test
    fun `gold test`() {
        trailheadRatings(parseMap(loadResource("Day10"))) shouldBe 1657
    }


    fun trailheadScores(input: Map<Point, Char>): Int {

        fun headsFromHere(point: Point): List<Point> {
            val curr = input.getValue(point).digitToInt()
            if (curr == 9) {
                return listOf(point)
            }

            val neighbors = listOf(
                point.left(), point.right(), point.down(), point.up()
            )
                .filter {
                    curr.plus(1) == input[it]?.digitToInt()
                }

            return neighbors
                .flatMap {
                    headsFromHere(it)
                }
        }

        return input.filterValues { it == '0' }.map { (head, _) -> headsFromHere(head).distinct().size }.sum()
    }

    fun trailheadRatings(input: Map<Point, Char>): Int {

        fun headsFromHere(point: Point): List<Point> {
            val curr = input.getValue(point).digitToInt()
            if (curr == 9) {
                return listOf(point)
            }

            val neighbors = listOf(
                point.left(), point.right(), point.down(), point.up()
            )
                .filter {
                    curr.plus(1) == input[it]?.digitToInt()
                }

            return neighbors
                .flatMap {
                    headsFromHere(it)
                }
        }

        return input.filterValues { it == '0' }.map { (head, _) -> headsFromHere(head).size }.sum()
    }
}