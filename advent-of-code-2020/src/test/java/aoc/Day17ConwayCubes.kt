package aoc

import org.assertj.core.api.Assertions.assertThat
import org.junit.Test

class Day17ConwayCubes {
    private val cache3d = mutableMapOf<Point, List<Point>>()

    fun Point.neighbors3d(): List<Point> {
        return cache3d.getOrPut(this) {
            run {
                (x - 1..x + 1).flatMap { xx ->
                    (y - 1..y + 1).flatMap { yy ->
                        (z - 1..z + 1).map { zz ->
                            Point(xx, yy, zz)
                        }
                    }
                }
                    .minus(this)
            }
        }
    }

    val surr4d = Point(0, 0, 0, 0).run {
        (x - 1..x + 1).flatMap { xx ->
            (y - 1..y + 1).flatMap { yy ->
                (z - 1..z + 1).flatMap { zz ->
                    (w - 1..w + 1).map { ww ->
                        Point(xx, yy, zz, ww)
                    }
                }
            }
        }
            .toSet()
            .minus(this)
            .apply { require(size == 80) }
    }

    fun Point.neighbors4d(): List<Point> {
        return surr4d.map { this + it }
    }

    @Test
    fun silverTest() {
        val activeAfterWarmup = countActiveAfterSequence(parseMap(testInput), 6)
        assertThat(activeAfterWarmup).isEqualTo(112)
    }

    @Test
    fun silver() {
        val activeAfterWarmup = countActiveAfterSequence(parseMap(taskInput), 6)
        assertThat(activeAfterWarmup).isEqualTo(213)
    }

    @Test
    fun goldTest() {
        val activeAfterWarmup = countActiveAfterSequence(parseMap(testInput), 6) { it.neighbors4d() }
        assertThat(activeAfterWarmup).isEqualTo(848)
    }

    @Test
    fun gold() {
        val activeAfterWarmup = countActiveAfterSequence(parseMap(taskInput), 6) { it.neighbors4d() }
        assertThat(activeAfterWarmup).isEqualTo(1624)
    }

    private fun countActiveAfterSequence(
        initial: Map<Point, Char>,
        rounds: Int,
        neighbors: (Point) -> List<Point> = { it.neighbors3d() },
    ): Int {
        return generateSequence(initial) { cubes ->
            cubes.keys.flatMap { neighbors(it) }
                .map { point ->
                    val value = cubes.getOrDefault(point, '.')
                    val activeNeighbors = neighbors(point).mapNotNull { cubes[it] }.count { it == '#' }
                    when {
                        value == '#' && activeNeighbors in (2..3) -> point to '#'
                        value == '.' && activeNeighbors == 3 -> point to '#'
                        else -> point to '.'
                    }
                }
                .toMap()
        }
            .take(rounds + 1)
            .last()
            .values
            .count { it == '#' }
    }

    private val testInput = """
.#.
..#
###
    """.trimIndent()
    private val taskInput = """
..##.#.#
.#####..
#.....##
##.##.#.
..#...#.
.#..##..
.#...#.#
#..##.##
    """.trimIndent()
}