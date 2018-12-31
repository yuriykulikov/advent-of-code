package aoc

import org.assertj.core.api.Assertions.assertThat
import org.junit.Test
import kotlin.math.absoluteValue

class Advent6 {
    /**
    If we name these coordinates A through F, we can draw them on a grid, putting 0,0 at the top left:

    ..........
    .A........
    ..........
    ........C.
    ...D......
    .....E....
    .B........
    ..........
    ..........
    ........F.
    This view is partial - the actual grid extends infinitely in all directions. Using the Manhattan distance, each
    location's closest coordinate can be determined, shown here in lowercase:

    aaaaa.cccc
    aAaaa.cccc
    aaaddecccc
    aadddeccCc
    ..dDdeeccc
    bb.deEeecc
    bBb.eeee..
    bbb.eeefff
    bbb.eeffff
    bbb.ffffFf
    Locations shown as . are equally far from two or more coordinates, and so they don't count as being closest to any.

    In this example, the areas of coordinates A, B, C, and F are infinite - while not shown here, their areas extend
    forever outside the visible grid. However, the areas of coordinates D and E are finite: D is closest to 9 locations,
    and E is closest to 17 (both including the coordinate's location itself). Therefore, in this example, the size of
    the largest area is 17.
     */

    data class Point(val x: Int, val y: Int, val name: String) {
        fun distanceTo(x1: Int, y1: Int): Int {
            return (x1 - x).absoluteValue + (y1 - y).absoluteValue
        }
    }

    @Test
    fun `Star 2 Test input result is 16`() {
        val result = calculateSafeRegionSize(area = testAreaInput, mixDistance = 32)
        assertThat(result).isEqualTo(16)
    }

    @Test
    fun `Star 2 input result is 16`() {
        val result = calculateSafeRegionSize(area = areaInput, mixDistance = 10000)
        assertThat(result).isEqualTo(39560)
    }

    /**
     * Star 2
     */
    private fun calculateSafeRegionSize(area: String, mixDistance: Int): Int {
        val points = parsePoints(area)

        val (_, _, matrix, matrixPoints) = createMatrix(points)

        points.forEach { point ->
            matrix[point.x][point.y] = point.name
        }

        matrixPoints.forEach { (x, y) ->
            val total = points.map { point -> point.distanceTo(x, y) }.sum()

            if (total < mixDistance) {
                matrix[x][y] = "#"
            }
        }

        matrix.dump()

        return matrix.flatten().count { it == "#" }
    }

    @Test
    fun `Test input result is 17`() {
        val result = findMostIsolatedAreaSize(testAreaInput)
        assertThat(result).isEqualTo(17)
    }

    @Test
    fun `Test result is 17`() {
        val result = findMostIsolatedAreaSize(areaInput)
        assertThat(result).isEqualTo(4398)
    }

    /**
     * Star 1
     */
    private fun findMostIsolatedAreaSize(input: String): Int? {
        val points = parsePoints(input)

        println(points)

        val (maxX, maxY, matrix, matrixPoints) = createMatrix(points)

        points.forEach { point ->
            matrix[point.x][point.y] = point.name.capitalize()
        }

        matrix.dump()

        matrixPoints.forEach { (x, y) ->
            val marker: String = points.sortedBy { point -> point.distanceTo(x, y) }.let { sorted ->
                if (sorted[0].distanceTo(x, y) == sorted[1].distanceTo(x, y)) "." else sorted[0].name
            }

            if (matrix[x][y] == " ") {
                matrix[x][y] = marker
            }
        }

        matrix.dump()

        val farAway = (-100..maxX + 100).map { x -> x to -100 }
            .plus((-100..maxX + 100).map { x -> x to maxY + 100 })
            .plus((-100..maxY + 100).map { y -> maxX + 100 to y })
            .plus((-100..maxY + 100).map { y -> -100 to y })

        val infiniteNames = farAway
            .map { (x, y) ->
                points.minBy { it.distanceTo(x, y) }
            }
            .map { it?.name }
            .distinct()

        matrixPoints
            .filter { (x, y) -> matrix[x][y] in infiniteNames }
            .forEach { (x, y) -> matrix[x][y] = "-" }

        matrix.dump()

        val group = matrix.flatten()
            .filterNot { it == "." || it == "-" }
            .groupBy { it }.mapValues { it.value.size }.maxBy { entry -> entry.value }

        return group?.value
    }

    data class Matrix(val maxX: Int, val maxY: Int, val array: Array<Array<String>>, val points: List<Pair<Int, Int>>)

    private fun createMatrix(points: List<Point>): Matrix {
        val maxX = points.map { it.x }.max() ?: 0
        val maxY = points.map { it.y }.max() ?: 0
        val matrix = Array(maxX + 1) { Array(maxY + 1) { " " } }
        val points = (0..maxX).flatMap { x ->
            (0..maxY).map { y ->
                x to y
            }
        }
        return Matrix(maxX, maxY, matrix, points)
    }

    private fun parsePoints(input: String): List<Point> {
        return input.lines()
            .map { line -> line.split(", ").map { it.toInt() } }
            // .sortedBy { (x, y) -> x + y }
            .mapIndexed { i, (x, y) -> Point(x, y, "$i") }
    }

    fun Array<Array<String>>.dump() {
        forEach { row -> println(row.joinToString(" ")) }
    }

    private val testAreaInput = """
1, 1
1, 6
8, 3
3, 4
5, 5
8, 9
""".trimIndent()
    private val areaInput = """
137, 140
318, 75
205, 290
104, 141
163, 104
169, 164
238, 324
180, 166
260, 198
189, 139
290, 49
51, 350
51, 299
73, 324
220, 171
146, 336
167, 286
51, 254
40, 135
103, 138
100, 271
104, 328
80, 67
199, 180
320, 262
215, 290
96, 142
314, 128
162, 106
214, 326
303, 267
340, 96
211, 278
335, 250
41, 194
229, 291
45, 97
304, 208
198, 214
250, 80
200, 51
287, 50
120, 234
106, 311
41, 116
359, 152
189, 207
300, 167
318, 315
296, 72
""".trimIndent()
}