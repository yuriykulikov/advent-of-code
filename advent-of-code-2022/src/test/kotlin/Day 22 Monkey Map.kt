import io.kotest.matchers.shouldBe
import java.util.*
import kotlinx.collections.immutable.persistentMapOf
import org.junit.jupiter.api.Test

class `Day 22 Monkey Map` {
  private fun parse(input: String): Pair<Map<Point, Char>, List<String>> {
    val (map, path) = input.split("\n\n")
    return parseMap(map).filterValues { it != ' ' } to
        StringTokenizer(path, "LR", true).toList().map { it as String }
  }

  private data class Scan(val direction: Point, val position: Point, val steps: Map<Point, Char>)

  @Test
  fun silverTest() {
    val (map, path) = parse(testInput)
    goTheMonkeyPath(
            path = path,
            map = map,
            wrapFunction = { direction, position, _ -> wrapFlat(direction, map, position) },
        )
        .last()
        .also { scan -> printMap(map + scan.steps) }
        .let { scan -> password(scan) } shouldBe 6032
  }

  @Test
  fun silver() {
    val (map, path) = parse(loadResource("day22"))
    goTheMonkeyPath(
            path = path,
            map = map,
            wrapFunction = { direction, position, _ -> wrapFlat(direction, map, position) },
        )
        .last()
        .let { scan -> password(scan) } shouldBe 95358
  }

  private fun Point.neighbors(): List<Point> = listOf(up(), down(), left(), right())
  private fun Point.diagNeighbors(): List<Point> =
      listOf(up().left(), down().left(), up().right(), down().right())

  private fun Point.dirChar() =
      when (this) {
        Point(0, 1) -> 'v'
        Point(1, 0) -> '>'
        Point(0, -1) -> '^'
        Point(-1, 0) -> '<'
        else -> error("Fail $this")
      }
  @Test
  fun goldTest() {
    val (map, path) = parse(testInput)
    val wraps: Map<Point, Point> =
        corners(map).map { corner -> foldFrom(corner, map) }.reduce(Map<Point, Point>::plus)

    goTheMonkeyPath(
            path = path,
            map = map,
            wrapFunction = { direction, position, steps ->
              cubeWrap(position, direction, wraps, map)
            },
        )
        .last()
        .let { scan ->
          printMap(map + scan.steps)
          password(scan)
        } shouldBe 5031
  }

  @Test
  fun gold() {
    val (map, path) = parse(loadResource("day22"))
    val wraps: Map<Point, Point> =
        corners(map).map { corner -> foldFrom(corner, map) }.reduce(Map<Point, Point>::plus)

    goTheMonkeyPath(
            path = path,
            map = map,
            wrapFunction = { direction, position, steps ->
              cubeWrap(position, direction, wraps, map)
            },
        )
        .last()
        .let { scan -> password(scan) } shouldBe 144361
  }

  private fun cubeWrap(
      position: Point,
      direction: Point,
      wraps: Map<Point, Point>,
      map: Map<Point, Char>
  ): Pair<Point, Point>? {
    val teleportIn = position.plus(direction)
    val teleportOut = wraps.getValue(teleportIn)
    // corners link to themselves so exclude position
    val target = teleportOut.neighbors().first { it in map && it != position }

    return when {
      map[target] == '#' -> null
      // facing from the teleport
      else -> target to target.minus(teleportOut).direction()
    }
  }

  /**
   * Finds corners in the map, fold from here
   *
   * ```
   *    ....
   *    ....
   * ......
   * .......
   * ```
   */
  private fun corners(map: Map<Point, Char>) =
      map.keys
          .filter {
            it.neighbors().all { n -> n in map } && it.diagNeighbors().count { n -> n in map } == 3
          }
          .flatMap { it.diagNeighbors() }
          .filter { it !in map }

  /** we fold until encounter 2 angles. One angle and straight is OK to fold */
  private fun foldFrom(start: Point, map: Map<Point, Char>): Map<Point, Point> {
    fun Point.offEdge(): Boolean {
      return this !in map && neighbors().count { n -> n in map } == 1
    }

    val seed = start.neighbors().filter { it.offEdge() }
    return generateSequence(seed to seed.toSet()) { (prev, seen) ->
          val next =
              prev.map { point ->
                (point.neighbors() + point.diagNeighbors()).first { it !in seen && it.offEdge() }
              }

          val reached2Corners =
              next.zip(prev).count { (n, p) ->
                val isCorner = p !in n.neighbors()
                isCorner
              } == 2

          if (reached2Corners) null else next to seen.plus(next)
        }
        .map { it.first }
        // works both ways
        .flatMap { (one, another) -> listOf(one to another, another to one) }
        .toMap()
        .plus(start to start)
  }

  private fun password(scan: Scan) =
      1000 * (scan.position.y + 1) +
          4 * (scan.position.x + 1) +
          when (scan.steps[scan.position]) {
            '>' -> 0
            'v' -> 1
            '<' -> 2
            '^' -> 3
            else -> error("")
          }

  private fun goTheMonkeyPath(
      path: List<String>,
      map: Map<Point, Char>,
      wrapFunction:
          (direction: Point, position: Point, path: Map<Point, Char>) -> Pair<Point, Point>?
  ): List<Scan> {
    return path.scan(
        Scan(
            direction = Point(x = 1, y = 0),
            position = map.keys.minWith(compareBy<Point> { it.y }.thenBy { it.x }),
            steps = persistentMapOf(),
        )) { scan, next: String ->
          when (next) {
            "L" -> scan.copy(direction = scan.direction.rotateCW())
            "R" -> scan.copy(direction = scan.direction.rotateCCW())
            else -> {
              val pointSequence =
                  generateSequence(scan.position to scan.direction) { (pos, direction) ->
                        if (pos + direction !in map) {
                          wrapFunction(direction, pos, scan.steps)
                        } else {
                          pos + direction to direction
                        }
                      }
                      .takeWhile { (pos, dir) -> map[pos] != '#' }
                      .take(next.toInt() + 1)
                      .toList()

              scan.copy(
                  position = pointSequence.lastOrNull()?.first ?: scan.position,
                  direction = pointSequence.lastOrNull()?.second ?: scan.direction,
                  steps =
                      scan.steps + pointSequence.associate { (pos, dir) -> pos to dir.dirChar() })
            }
          }
        }
  }
  private fun wrapFlat(direction: Point, map: Map<Point, Char>, pos: Point) =
      when (direction) {
        Point(0, 1) -> /*v*/ map.keys.filter { it.x == pos.x }.minBy { it.y }
        Point(0, -1) -> /*^*/ map.keys.filter { it.x == pos.x }.maxBy { it.y }
        Point(1, 0) -> /*>*/ map.keys.filter { it.y == pos.y }.minBy { it.x }
        Point(-1, 0) -> /*<*/ map.keys.filter { it.y == pos.y }.maxBy { it.x }
        else -> error("Fail")
      } to direction

  private val testInput =
      """
        ...#
        .#..
        #...
        ....
...#.......#
........#...
..#....#....
..........#.
        ...#....
        .....#..
        .#......
        ......#.

10R5L5R10L4R5L5
    """.trimIndent()
}
