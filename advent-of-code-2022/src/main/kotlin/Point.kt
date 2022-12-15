import kotlin.math.abs

data class Point(val x: Int, val y: Int)

fun printMap(
    tiles: Map<Point, String>,
) {
  val maxX = tiles.keys.maxOf { it.x }
  val minX = tiles.keys.minOf { it.x }
  val maxY = tiles.keys.maxOf { it.y }
  val minY = tiles.keys.minOf { it.y }

  (minY..maxY)
      .map { y -> (minX..maxX).joinToString(separator = "") { x -> tiles[Point(x, y)] ?: " " } }
      .forEach { println(it) }
}

fun <T> printMap(
    tiles: Map<Point, T>,
    invertedY: Boolean = true,
    prefixFun: (Int) -> String = { "" },
    func: (T?) -> String = { "${it ?: " "}" }
) {
  val maxX = tiles.keys.maxOf { it.x }
  val minX = tiles.keys.minOf { it.x }
  val maxY = tiles.keys.maxOf { it.y }
  val minY = tiles.keys.minOf { it.y }

  (minY..maxY)
      .run { if (invertedY) sorted() else sortedDescending() }
      .map { y ->
        (minX..maxX)
            .map { x -> tiles[Point(x, y)] }
            .joinToString(prefix = prefixFun(y), separator = "", transform = func)
      }
      .forEach { println(it) }
}

fun parseMap(mapString: String): Map<Point, Char> {
  return mapString
      .lines()
      .mapIndexed { y, line -> line.mapIndexed { x, c -> Point(x, y) to c } }
      .flatten()
      .toMap()
}

operator fun Point.rangeTo(other: Point): List<Point> =
    when {
      x == other.x -> (y..other.y).map { Point(x, it) }
      y == other.y -> (x..other.x).map { Point(it, y) }
      else -> error("Cannot go from $this to $other")
    }

fun Point.left(inc: Int = 1) = copy(x = x - inc)

fun Point.right(inc: Int = 1) = copy(x = x + inc)

fun Point.up(inc: Int = 1) = copy(y = y - inc)

fun Point.down(inc: Int = 1) = copy(y = y + inc)

operator fun Point.minus(other: Point) = Point(x - other.x, y - other.y)

operator fun Point.plus(other: Point) = Point(x + other.x, y + other.y)

fun Point.direction() = Point(x = x.compareTo(0), y = y.compareTo(0))

fun Point.manhattan(): Int = abs(x) + abs(y)
