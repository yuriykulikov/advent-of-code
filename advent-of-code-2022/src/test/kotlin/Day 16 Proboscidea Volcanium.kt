import io.kotest.matchers.shouldBe
import kotlin.math.min
import kotlinx.collections.immutable.PersistentMap
import kotlinx.collections.immutable.persistentMapOf
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Timeout

@Timeout(240)
class `Day 16 Proboscidea Volcanium` {
  @Test
  fun testParsing() {
    parse(testInput).forEach { println(it) }
  }
  data class Valve(val name: String, val rate: Int, val children: List<String>)
  fun parse(input: String): Map<String, Valve> {
    return input
        .lines()
        .filter { it.isNotBlank() }
        .associate { line ->
          val (_, name, rate, children) =
              line.split(
                  "Valve ",
                  " has flow rate=",
                  "; tunnels lead to valves ",
                  "; tunnel leads to valve ",
                  "; tunnel lead to valves ",
                  "; tunnels lead to valve ",
              )

          name to Valve(name, rate.toInt(), children.split(", "))
        }
  }

  @Test
  fun silverTest() {
    val map = parse(testInput)
    maxPressureIn30Minutes(map).totalPressure(map) shouldBe 1651
  }

  @Test
  fun silver() {
    val map = parse(loadResource("day16"))
    maxPressureIn30Minutes(map).totalPressure(map) shouldBe 1376
  }

  @Test
  fun goldTest() {
    maxPressureIn26MinutesDP(testInput) shouldBe 1707
  }
  @Test
  fun gold() {
    maxPressureIn26MinutesDP(loadResource("day16")) shouldBe 1933
  }
  private fun checkInterrupted() {
    check(!Thread.currentThread().isInterrupted) { "Interrupted!" }
  }
  data class Path(
      val position: String,
      val elapsed: Int,
      val opened: Map<String, Int>,
  ) {
    fun totalPressure(map: Map<String, Valve>): Int {
      return opened.map { (name, instant) -> (30 - instant) * map.getValue(name).rate }.sum()
    }

    override fun toString(): String {
      return opened.toString()
    }
  }

  private fun maxPressureIn30Minutes(map: Map<String, Valve>, maxElapsed: Int = 30): Path {
    val reach: Map<String, List<Pair<String, Int>>> = reachesOf(map)

    fun Path.getTunnelOptions() =
        reach
            .getValue(position)
            .filter { (name, _) -> name !in opened }
            .filter { (_, timeToReach) -> elapsed + timeToReach + 1 <= maxElapsed }
            .map { (name, timeToReach) ->
              copy(
                  elapsed = timeToReach + elapsed + 1,
                  position = name,
                  opened = opened.plus(name to timeToReach + elapsed + 1))
            }

    fun Path.deeper(): List<Path> {
      checkInterrupted()
      check(elapsed <= maxElapsed)
      return getTunnelOptions().takeIf { it.isNotEmpty() }?.flatMap { it.deeper() } ?: listOf(this)
    }

    return Path("AA", 0, emptyMap()).deeper().maxBy { it.totalPressure(map) }
  }

  data class Path2(
      val position1: String,
      val position2: String,
      val freeAt1: Int,
      val freeAt2: Int,
      val elapsed: Int,
      val opened: PersistentMap<String, Int>,
  ) {
    fun totalPressure(map: Map<String, Valve>): Int {
      return opened.map { (name, instant) -> (26 - instant) * map.getValue(name).rate }.sum()
    }
  }

  operator fun <T> List<T>.times(other: List<T>): List<List<T>> {
    val prod = mutableListOf<List<T>>()
    for (e in this) {
      for (f in other) {
        prod.add(listOf(e, f))
      }
    }
    return prod
  }
  private fun maxPressureIn26MinutesDP(testInput: String): Int {
    val maxElapsed = 26
    val map = parse(testInput)
    val reach: Map<String, List<Pair<String, Int>>> = reachesOf(map)

    fun Path2.reachableFrom(from: String) =
        reach
            .getValue(from)
            .filter { (name, _) -> name !in opened }
            .filter { (_, timeToReach) -> elapsed + timeToReach + 1 <= maxElapsed }

    fun Path2.branchOut(): List<Path2> {
      checkInterrupted()
      check(elapsed <= maxElapsed)
      check(elapsed <= freeAt1)
      check(elapsed <= freeAt2)
      val branches =
          when {
            elapsed == freeAt1 && elapsed == freeAt2 -> {
              val reachableFrom2 = reachableFrom(position2)
              reachableFrom(position1)
                  .flatMap { from1 -> reachableFrom2.map { from2 -> from1 to from2 } }
                  .map { (first, second) ->
                    val (name1, timeToReach1) = first
                    val (name2, timeToReach2) = second
                    copy(
                        position1 = name1,
                        position2 = name2,
                        elapsed = min(timeToReach1 + elapsed + 1, timeToReach2 + elapsed + 1),
                        freeAt1 = timeToReach1 + elapsed + 1,
                        freeAt2 = timeToReach2 + elapsed + 1,
                        opened =
                            opened
                                .put(name1, timeToReach1 + elapsed + 1)
                                .put(name2, timeToReach2 + elapsed + 1),
                    )
                  }
                  .filterNot { it.position2 == it.position1 }
            }
            elapsed == freeAt1 -> {
              reachableFrom(position1).map { (name, timeToReach) ->
                copy(
                    position1 = name,
                    elapsed = min(timeToReach + elapsed + 1, freeAt2),
                    freeAt1 = timeToReach + elapsed + 1,
                    opened = opened.put(name, timeToReach + elapsed + 1),
                )
              }
            }
            elapsed == freeAt2 -> {
              reachableFrom(position2).map { (name, timeToReach) ->
                copy(
                    position2 = name,
                    elapsed = min(freeAt1, timeToReach + elapsed + 1),
                    freeAt2 = timeToReach + elapsed + 1,
                    opened = opened.put(name, timeToReach + elapsed + 1),
                )
              }
            }
            else -> {
              error("Fail $this")
            }
          }

      return if (branches.isEmpty()) return listOf(this)
      else
          branches
              // I am sure there is something to prune here...
              .take(16)
              .flatMap { it.branchOut() }
    }

    return Path2("AA", "AA", 0, 0, 0, persistentMapOf())
        .branchOut()
        .maxBy { it.totalPressure(map) }
        .totalPressure(map)
  }
  private fun reachesOf(map: Map<String, Valve>): Map<String, List<Pair<String, Int>>> {
    return map.mapValues { (name, _) ->
      dijkstra(name, map).filter { (name, _) -> map.getValue(name).rate > 0 }
    }
  }
  private fun dijkstra(name: String, map: Map<String, Valve>): List<Pair<String, Int>> {
    val distances = mutableMapOf<String, Int>(name to 0)
    val unvisited = map.keys.minus(name).toMutableSet()
    var current = name
    while (unvisited.isNotEmpty()) {
      val distanceToCurrent = distances.getValue(current)
      map.getValue(current).children.forEach { childName ->
        if (childName !in distances) {
          distances[childName] = distanceToCurrent + 1
        } else {
          distances[childName] = min(distances.getValue(childName), distanceToCurrent + 1)
        }
      }
      unvisited.remove(current)
      if (unvisited.isEmpty()) break
      current = unvisited.minBy { distances[it] ?: Int.MAX_VALUE }
    }

    return distances.map { (k, v) -> k to v }
  }

  private val testInput =
      """
Valve AA has flow rate=0; tunnels lead to valves DD, II, BB
Valve BB has flow rate=13; tunnels lead to valves CC, AA
Valve CC has flow rate=2; tunnels lead to valves DD, BB
Valve DD has flow rate=20; tunnels lead to valves CC, AA, EE
Valve EE has flow rate=3; tunnels lead to valves FF, DD
Valve FF has flow rate=0; tunnels lead to valves EE, GG
Valve GG has flow rate=0; tunnels lead to valves FF, HH
Valve HH has flow rate=22; tunnel leads to valve GG
Valve II has flow rate=0; tunnels lead to valves AA, JJ
Valve JJ has flow rate=21; tunnel leads to valve II
""".trimIndent()
}
