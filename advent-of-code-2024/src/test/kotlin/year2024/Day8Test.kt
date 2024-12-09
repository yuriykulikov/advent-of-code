package year2024

import Point
import io.kotest.matchers.collections.shouldHaveSize
import io.kotest.matchers.shouldBe
import lines
import loadResource
import minus
import org.junit.jupiter.api.Test
import parseMap
import plus
import print

class Day8Test {
    private val example = """
    ............
    ........0...
    .....0......
    .......0....
    ....0.......
    ......A.....
    ............
    ............
    ........A...
    .........A..
    ............
    ............
""".trimIndent()

    private val example2 = """
..........
...#......
#.........
....a.....
........a.
.....a....
..#.......
......#...
..........
..........
""".trimIndent()

    @Test
    fun `silver example`() {
        val map = parseMap(example)
        val antinodes = findAntiNodes(map)
        (map + antinodes + map.filterValues { it != '.' }).lines().joinToString("\n") shouldBe """
......#....#
...#....0...
....#0....#.
..#....0....
....0....#..
.#....A.....
...#........
#......#....
........A...
.........A..
..........#.
..........#.
        """.trimIndent()
        antinodes.keys.shouldHaveSize(14)
    }

    @Test
    fun `silver test`() {
        val map = parseMap(loadResource("Day8"))
        val antinodes = findAntiNodes(map)
        (map + antinodes + map.filterValues { it != '.' }).print()
        antinodes.keys.shouldHaveSize(299)
    }

    @Test
    fun `gold example`() {
        val map = parseMap(example)
        val antinodes = findAntiNodes(map, harmonics = true)
        (map + antinodes + map.filterValues { it != '.' }).lines().joinToString("\n") shouldBe """
##....#....#
.#.#....0...
..#.#0....#.
..##...0....
....0....#..
.#...#A....#
...#..#.....
#....#.#....
..#.....A...
....#....A..
.#........#.
...#......##
        """.trimIndent()
        antinodes.keys.shouldHaveSize(34)
    }

    @Test
    fun `gold test`() {
        val map = parseMap(loadResource("Day8"))
        val antinodes = findAntiNodes(map, true)
        (map + antinodes + map.filterValues { it != '.' }).print()
        antinodes.keys.shouldHaveSize(1032)
    }

    private fun findAntiNodes(map: Map<Point, Char>, harmonics: Boolean = false): Map<Point, Char> {
        val maxX = map.keys.maxOf { it.x }
        val maxY = map.keys.maxOf { it.y }
        fun inBounds(point: Point): Boolean {
            return point.x <= maxX && point.y <= maxY && point.x >= 0 && point.y >= 0
        }
        return map
            .filterValues { it != '.' }
            .entries
            // group by frequency
            .groupBy { it.value }
            .asSequence()
            .flatMap { (_, entries) ->
                val antennaPositions = entries.map { it.key }
                permutate(antennaPositions)
            }
            .flatMap { (a, b) ->
                if (harmonics) {
                    generateSequence(a) { it + (a - b) }
                        .takeWhile { inBounds(it) }
                        .plus(generateSequence(b) { it + (b - a) }
                            .takeWhile { inBounds(it) })
                } else {
                    sequenceOf(a + (a - b), b + (b - a))
                }
            }
            .filter {
                inBounds(it)
            }
            .associateWith {
                '#'
            }
    }

    private fun permutate(antennaPositions: List<Point>): List<Pair<Point, Point>> {
        return antennaPositions.dropLast(1).flatMapIndexed { index, a ->
            antennaPositions.drop(index + 1).map { b -> Pair(a, b) }
        }
    }
}