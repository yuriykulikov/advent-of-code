data class Point(val x: Int, val y: Int)

fun <T> printMap(
    tiles: Map<Point, T>,
    invertedY: Boolean = true,
    prefixFun: (Int) -> String = { "" },
    func: (T?) -> String = { "${it ?: "n"}" }
) {
  val maxX = tiles.keys.maxOf { it.x }
  val minX = tiles.keys.minOf { it.x }
  val maxY = tiles.keys.maxOf { it.y }
  val minY = tiles.keys.minOf { it.y }

  (minY..maxY)
      .run { if (invertedY) sorted() else sortedDescending() }
      .mapIndexed { index, y ->
        (minX..maxX)
            .map { x -> tiles[Point(x, y)] }
            .joinToString(prefix = prefixFun(index), separator = "", transform = func)
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
