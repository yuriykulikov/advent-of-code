package year2025

import Point
import down
import io.kotest.matchers.shouldBe
import left
import lines
import loadResource
import org.junit.jupiter.api.Test
import parseMap
import right

class Day7Test {
    private val example = """
.......S.......
...............
.......^.......
...............
......^.^......
...............
.....^.^.^.....
...............
....^.^...^....
...............
...^.^...^.^...
...............
..^...^.....^..
...............
.^.^.^.^.^...^.
...............
    """.trimIndent()


    @Test
    fun `Silver example - How many times will the beam be split`() {
        val map = parseMap(example)
        val sequence = modelBeamSplit(map)
        (map + sequence.flatten().associateWith { "|" }).lines().joinToString("\n").shouldBe(
            """
.......|.......
.......|.......
......|^|......
......|.|......
.....|^|^|.....
.....|.|.|.....
....|^|^|^|....
....|.|.|.|....
...|^|^|||^|...
...|.|.|||.|...
..|^|^|||^|^|..
..|.|.|||.|.|..
.|^|||^||.||^|.
.|.|||.||.||.|.
|^|^|^|^|^|||^|
|.|.|.|.|.|||.|
""".trimIndent()
        )
        sequence.sumOf { beams -> beams.count { beam -> map[beam.down()] == '^' } }.shouldBe(21)
    }

    @Test
    fun `Silver - How many times will the beam be split`() {
        val map = parseMap(loadResource("Day7"))
        val sequence = modelBeamSplit(map)
        sequence.sumOf { beams -> beams.count { beam -> map[beam.down()] == '^' } }.shouldBe(1609)
    }

    private fun modelBeamSplit(map: Map<Point, Char>): List<List<Point>> {
        val start = map.filterValues { it == 'S' }.keys.first()

        val sequence = generateSequence(listOf(start)) { beams ->
            beams.flatMap { beam ->
                val down = beam.down()
                when (map[down]) {
                    '^' -> listOf(down.left(), down.right())
                    '.' -> listOf(down)
                    else -> emptyList()
                }
            }
                .distinct()
        }
            .takeWhile { beams -> beams.isNotEmpty() }
            .toList()
        return sequence
    }

    @Test
    fun `Gold example - How many timelines would a single tachyon particle end up on`() {
        val map = parseMap(example)
        manyWorldsInterpretation(map).shouldBe(40)
    }

    @Test
    fun `Gold - How many timelines would a single tachyon particle end up on`() {
        val map = parseMap(loadResource("Day7"))
        manyWorldsInterpretation(map).shouldBe(12472142047197)
    }

    fun manyWorldsInterpretation(map: Map<Point, Char>): Long {
        val start = map.filterValues { it == 'S' }.keys.first()
        val cache = mutableMapOf<Point, Long>()

        fun timelines(start: Point): Long {
            return cache.getOrPut(start) {
                val down = start.down()
                when (map[down]) {
                    '^' -> timelines(down.left()) + timelines(down.right())
                    '.' -> timelines(down)
                    else -> 1
                }
            }
        }
        return timelines(start)
    }
}

