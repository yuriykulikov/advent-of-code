package aoc

import kotlin.math.abs

data class Point(val x: Int, val y: Int) {
    operator fun minus(other: Point) = Point(x - other.x, y - other.y)
    operator fun plus(other: Point) = Point(x + other.x, y + other.y)
    operator fun times(times: Int): Point = copy(x = x * times, y = y * times)
}

fun Point.left(inc: Int = 1) = copy(x = x - inc)
fun Point.right(inc: Int = 1) = copy(x = x + inc)
fun Point.up(inc: Int = 1) = copy(y = y - inc)
fun Point.down(inc: Int = 1) = copy(y = y + inc)
fun Point.manhattan(): Int = abs(x) + abs(y)
fun Point.rotateCCW() = Point(x = -y, y = x)
fun Point.rotateCW() = Point(x = y, y = -x)

fun <T> printMap(
    tiles: Map<Point, T>,
    invertedY: Boolean = true,
    func: (T?) -> String = { "${it ?: " "}" }
) {
    val maxX = tiles.keys.map { it.x }.maxOrNull() ?: 0
    val minX = tiles.keys.map { it.x }.minOrNull() ?: 0
    val maxY = tiles.keys.map { it.y }.maxOrNull() ?: 0
    val minY = tiles.keys.map { it.y }.minOrNull() ?: 0

    val code = (minY..maxY)
        .run {
            if (invertedY) sorted() else sortedDescending()
        }
        .mapIndexed { index, y ->
            (minX..maxX).map { x -> tiles[Point(x, y)] }
                .joinToString(separator = "") { func(it) }
        }
    code.forEach { println(it) }
}

/**
 * Parses a string as a map
 *
 * Y is inverted (first line is y=0)
 */
fun parseMap(mapString: String): Map<Point, Char> {
    return mapString.lines()
        .mapIndexed { y, line ->
            line.mapIndexed { x, c ->
                Point(x, y) to c
            }
        }
        .flatten()
        .toMap()
}