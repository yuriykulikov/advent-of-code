import kotlin.math.abs

data class Point(val x: Int, val y: Int)

fun <T : Any> Map<Point, T>.lines(
    invertedY: Boolean = true,
    prefixFun: (Int) -> String = { "" },
    func: Point.(T?) -> String = { "${it ?: " "}" }
): List<String> {
  val maxX = keys.maxOf { it.x }
  val minX = keys.minOf { it.x }
  val maxY = keys.maxOf { it.y }
  val minY = keys.minOf { it.y }

  return (minY..maxY)
      .let { if (invertedY) it.sorted() else it.sortedDescending() }
      .map { y ->
        (minX..maxX).joinToString(
            prefix = prefixFun(y),
            separator = "",
            transform = { x -> func(Point(x, y), this[Point(x, y)]) })
      }
}

fun <T : Any> Map<Point, T>.print(
    invertedY: Boolean = true,
    prefixFun: (Int) -> String = { "" },
    func: Point.(T?) -> String = { "${it ?: " "}" }
) {
  lines(invertedY, prefixFun, func).print()
}

fun List<String>.print() {
  forEach { println(it) }
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

fun Point.rotateCCW() = Point(x = -y, y = x)

fun Point.rotateCW() = Point(x = y, y = -x)
