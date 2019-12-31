data class Point(val x: Int, val y: Int, val z: Int = 0) {
    operator fun minus(other: Point) = Point(x - other.x, y - other.y)
    operator fun plus(other: Point) = Point(x + other.x, y + other.y)
}

fun Point.left() = copy(x = x - 1)
fun Point.right() = copy(x = x + 1)
fun Point.up() = copy(y = y + 1)
fun Point.down() = copy(y = y - 1)

fun printMap(tiles: Map<Point, Int>) {
    printMap(tiles) {
        when (it) {
            null -> " "
            //0 is an empty tile. No game object appears in this tile.
            0 -> " "
            //1 is a wall tile. Walls are indestructible barriers.
            1 -> "|"
            //2 is a block tile. Blocks can be broken by the ball.
            2 -> "X"
            //3 is a horizontal paddle tile. The paddle is indestructible.
            3 -> "-"
            //4 is a ball tile. The ball moves diagonally and bounces off objects.
            else -> "*"
        }
    }
}

fun <T> printMap(
    tiles: Map<Point, T>, invertedY: Boolean = true, prefixFun: (Int) -> String = { "" }, func: (T?) -> String = {
        "${it ?: "n"}"
    }
) {

    val maxX = tiles.keys.map { it.x }.max() ?: 0
    val minX = tiles.keys.map { it.x }.min() ?: 0
    val maxY = tiles.keys.map { it.y }.max() ?: 0
    val minY = tiles.keys.map { it.y }.min() ?: 0

    val code = (minY..maxY)
        .run {
            if (invertedY) sorted() else sortedDescending()
        }
        .mapIndexed { index, y ->
            (minX..maxX).map { x -> tiles[Point(x, y)] }
                .joinToString(prefix = prefixFun(index), separator = "") { func(it) }
            // .trim()
        }
    code.forEach { println(it) }
}

fun parseMap(mapString: String): Map<Point, Char> {
    return mapString.lines()
        .mapIndexed { y, line ->
            line.mapIndexed { x, c ->
                Point(x, y) to c
            }
        }.flatten()
        .toMap()
}