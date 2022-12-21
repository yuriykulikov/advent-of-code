import io.kotest.matchers.shouldBe
import kotlin.math.min
import kotlinx.collections.immutable.persistentMapOf
import kotlinx.collections.immutable.toPersistentMap
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.TestInfo
import org.junit.jupiter.api.Timeout
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.EnumSource

enum class Algorithm {
  FLOOD,
  DIJKSTRA,
  BFS,
}

@Timeout(5)
class `Day 12 Hill Climbing Algorithm` {
  private var calls: Int = 0
  @AfterEach
  fun print(testInfo: TestInfo) {
    println("${testInfo.displayName}: $calls")
    calls = 0
  }
  @ParameterizedTest
  @EnumSource(Algorithm::class)
  fun silverTest(alg: Algorithm) {
    alg.compute(parseMap(testInput), true) shouldBe 31
  }
  @ParameterizedTest
  @EnumSource(Algorithm::class)
  fun silver(alg: Algorithm) {
    alg.compute(parseMap(loadResource("day12")), true) shouldBe 339
  }
  @ParameterizedTest
  @EnumSource(Algorithm::class)
  fun goldTest(alg: Algorithm) {
    val map = parseMap(testInput).mapValues { (_, v) -> if (v == 'S') 'a' else v }
    map.filterValues { it == 'a' }
        .entries
        .mapNotNull { (start, _) -> alg.compute(map.plus(start to 'S')) }
        .min() shouldBe 29
  }
  @ParameterizedTest
  @EnumSource(Algorithm::class)
  fun gold(alg: Algorithm) = runBlocking {
    val map =
        parseMap(loadResource("day12"))
            .mapValues { (_, v) -> if (v == 'S') 'a' else v }
            .toPersistentMap()
    map.filterValues { it == 'a' }
        .entries
        .map { (start, _) ->
          async { withContext(Dispatchers.Default) { alg.compute(map.put(start, 'S')) } }
        }
        .mapNotNull { it.await() }
        .min() shouldBe 332
  }

  private fun Algorithm.compute(map: Map<Point, Char>, printMap: Boolean = false) =
      when (this) {
        Algorithm.DIJKSTRA -> minStepsToArriveDijkstra(map)
        Algorithm.FLOOD -> minStepsToArriveFlood(map)
        Algorithm.BFS -> minStepsToArriveBFS(map, printMap)
      }

  private fun Point.possibleSteps(map: Map<Point, Char>): List<Point> {
    checkInterrupted()
    calls++
    return sequenceOf(left(), up(), right(), down())
        .filter { it in map.keys }
        .filter { point ->
          val current = map.getValue(this)
          val possibleStep = map.getValue(point)
          when {
            current == 'S' -> possibleStep == 'a' || possibleStep == 'b'
            possibleStep == 'E' -> current == 'z' || current == 'y'
            possibleStep == current -> true
            possibleStep.code < current.code -> true
            else -> possibleStep.code - current.code == 1
          }
        }
        .toList()
        .also { check(it.size <= 4) }
  }

  private fun checkInterrupted() {
    check(!Thread.currentThread().isInterrupted) { "Interrupted!" }
  }

  private fun minStepsToArriveBFS(map: Map<Point, Char>, printMap: Boolean = false): Int? {
    val start = map.entries.first { (_, v) -> v == 'S' }.key
    val end = map.entries.first { (_, v) -> v == 'E' }.key
    val queue = ArrayDeque<Pair<Point, Int>>()
    val visited = mutableMapOf<Point, Int>()
    queue.add(start to 0)
    visited[start] = 0
    while (queue.isNotEmpty()) {
      checkInterrupted()
      val (next, distance) = queue.removeFirst()
      if (next == end) {
        if (printMap) printPath(map, end, visited)
        return distance
      }
      val possibleSteps = next.possibleSteps(map)
      possibleSteps.minus(visited.keys).forEach { point ->
        visited[point] = distance + 1
        queue.add(point to distance + 1)
      }
    }
    return null
  }
  private fun minStepsToArriveDijkstra(map: Map<Point, Char>): Int? {
    val start = map.entries.first { (_, v) -> v == 'S' }.key
    val end = map.entries.first { (_, v) -> v == 'E' }.key

    val unvisited = map.keys.toMutableSet()
    val distances = HashMap<Point, Int>()
    distances[start] = 0
    var current = start
    while (end in unvisited) {
      val currentDistance = distances.getValue(current)
      current
          .possibleSteps(map)
          .filter { it in unvisited }
          .forEach { neighbor ->
            val newPath = currentDistance.plus(1)
            val existingPath: Int? = distances[neighbor]
            distances[neighbor] = existingPath?.let { min(it, newPath) } ?: newPath
          }

      unvisited.remove(current)
      if (unvisited.isEmpty()) break
      current = unvisited.filter { distances[it] != null }.minByOrNull { distances[it]!! } ?: break
    }

    return distances[end]
  }

  private fun minStepsToArriveFlood(map: Map<Point, Char>): Int? {
    data class Round(val front: Set<Point>, val distance: Int, val seen: Map<Point, Int>)
    val start = map.entries.first { (_, v) -> v == 'S' }.key
    val end = map.entries.first { (_, v) -> v == 'E' }.key

    return generateSequence(Round(setOf(start), 0, persistentMapOf(start to 0))) {
            (front, distance, visited) ->
          val newFront = buildSet {
            front.forEach { point ->
              point.possibleSteps(map).forEach { if (it !in visited) add(it) }
            }
          }
          Round(newFront, distance + 1, visited.plus(front.associateWith { distance + 1 }))
        }
        .takeWhile { it.front.isNotEmpty() }
        .firstOrNull { (front, _, _) -> end in front }
        ?.distance
  }

  private fun printPath(map: Map<Point, Char>, end: Point, distances: Map<Point, Int>) {
    printMap(
        distances.keys.associateWith { map.getValue(it) } +
            generateSequence(end) { next ->
                  next.surroundings().first { point ->
                    distances[point] == distances[next]?.let { it - 1 }
                  }
                }
                .takeWhile { distances.getValue(it) > 0 }
                .associateWith { map.getValue(it).uppercase() })
  }

  private fun Point.surroundings() = listOf(left(), up(), right(), down())

  private val testInput = """
Sabqponm
abcryxxl
accszExk
acctuvwj
abdefghi
    """.trimIndent()
}
