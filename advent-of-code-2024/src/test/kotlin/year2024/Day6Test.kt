package year2024

import Point
import io.kotest.matchers.collections.shouldHaveSize
import io.kotest.matchers.shouldBe
import loadResource
import org.junit.jupiter.api.Test
import parseMap
import plus

class Day6Test {
    private val example = """
        ....#.....
        .........#
        ..........
        ..#.......
        .......#..
        ..........
        .#..^.....
        ........#.
        #.........
        ......#...
    """.trimIndent()

    @Test
    fun `silver example`() {
        val map = parseMap(example)
        simulateGuardRun(map).visited.shouldHaveSize(41)
    }

    @Test
    fun silver() {
        val map = parseMap(loadResource("Day6"))
        simulateGuardRun(map).visited.shouldHaveSize(5551)
    }

    @Test
    fun `gold example`() {
        countObstaclePlacements(example).shouldBe(6)
    }

    @Test
    fun gold() {
        countObstaclePlacements(loadResource("Day6")).shouldBe(1939)
    }

    /**
     * Possible optimizations:
     * - Don't start from the beginning each time, start when reached the obstacle
     * - Find next turn faster
     */
    private fun countObstaclePlacements(mapString: String): Long {
        val map = parseMap(mapString)
        val start = map.entries.first { (p, v) -> v == '^' }.key
        val count = simulateGuardRun(map).visited.minus(start).parallelStream().filter { potentialObstacle ->
            simulateGuardRun(map, potentialObstacle).cycled
        }.count()
        return count
    }

    class Run(
        val map: Map<Point, Char>,
        val positions: MutableMap<Point, MutableSet<Point>>,
        var position: Point,
        var direction: Point,
        val obstacle: Point?,
    ) {
        var out: Boolean = false
        var cycled: Boolean = false

        val visited: Set<Point>
            get() = positions.keys

        fun simulate() {
            while (!cycled && !out) {
                val next = nextPos(position, direction, map, obstacle)
                if (next == null) {
                    out = true
                } else {
                    val (nextPos, nextDir) = next
                    if (positions[nextPos]?.contains(nextDir) == true) {
                        cycled = true
                    } else {
                        position = nextPos
                        direction = nextDir
                        positions.getOrPut(nextPos) { mutableSetOf() }.add(nextDir)
                    }
                }
            }
        }
    }

    fun simulateGuardRun(map: Map<Point, Char>, obstacle: Point? = null): Run {
        val start = map.entries.first { (p, v) -> v == '^' }.key
        val direction = Point(0, -1)

        return Run(map, mutableMapOf(start to mutableSetOf(direction)), start, direction, obstacle).apply {
            simulate()
        }
    }

    companion object {
        private fun Point.rotateRight(): Point {
            return when (this) {
                Point(0, -1) -> Point(x = 1, y = 0)
                Point(1, 0) -> Point(x = 0, y = 1)
                Point(-1, 0) -> Point(x = 0, y = -1)
                Point(0, 1) -> Point(x = -1, y = 0)
                else -> error("Invalid vector")
            }
        }

        fun nextPos(
            pos: Point,
            direction: Point,
            map: Map<Point, Char>,
            obstacle: Point?,
        ): Pair<Point, Point>? {
            val supposedNextPosition = pos + direction

            return if (supposedNextPosition == obstacle) {
                nextPos(pos, direction.rotateRight(), map, obstacle)
            } else when (map[supposedNextPosition]) {
                null -> null
                '#' -> nextPos(pos, direction.rotateRight(), map, obstacle)
                else -> supposedNextPosition to direction
            }
        }
    }
}