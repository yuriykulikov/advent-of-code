import extensions.kotlin.scan
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test

class Day24PlanetOfDiscord {
    @Test
    fun silverTest() {
        val mapString = """
....#
#..#.
#..##
..#..
#....""".trimIndent()
        val biodiversity = calculateBiodiversity(mapString)

        assertThat(biodiversity).isEqualTo(2129920)

    }

    @Test
    fun silver() {
        val mapString = """
.....
...#.
.#..#
.#.#.
...##""".trimIndent()
        val biodiversity = calculateBiodiversity(mapString)

        assertThat(biodiversity).isEqualTo(18370591)

    }

    private fun calculateBiodiversity(mapString: String): Int {
        val initial: Map<Point, Char> = parseMap(mapString)

        val firstDuplicate: Map<Point, Char> = generateSequence(initial) { map ->
            fun Point.value(): Char = map.getOrDefault(this, '.')
            map.mapValues { (k, v) ->
                when {
                    // A bug dies (becoming an empty space) unless there is exactly one bug adjacent to it.
                    v == '#' && k.surroundings().count { it.value() == '#' } != 1 -> '.'
                    // An empty space becomes infested with a bug if exactly one or two bugs are adjacent to it.
                    v == '.' && k.surroundings().count { it.value() == '#' } in (1..2) -> '#'
                    // Otherwise, a bug or empty space remains the same.
                    else -> v
                }
            }
        }
            .firstDuplicate()

        return firstDuplicate.keys.sortedWith(compareBy<Point> { it.y }.thenBy { it.x })
            .zip((0..firstDuplicate.keys.size).map { index -> 2.pow(index) })
            .filter { (k, v) -> firstDuplicate[k] == '#' }
            .sumBy { (k, v) -> v }
    }

    private val levelPoints = (0 until 5).flatMap { y -> (0 until 5).map { x -> Point(x, y) } }
        .sortedWith(compareBy<Point> { it.y }.thenBy { it.x })

    private fun point(index: Int): Point {
        return levelPoints[index - 1]
    }

    private val pointToLetter: Map<Point, String> = levelPoints
        .zip("ABCDEFGHIJKLMNOPQRSTUVWXYZ".split("").drop(1)) { p, l -> p to l }
        .toMap()

    @Test
    fun verifyLettersAndIndexes() {
        printMap(levelPoints.map { it to pointToLetter[it] }.toMap())
        printMap(levelPoints.map { it to levelPoints.indexOf(it) + 1 }.toMap()) { " ${it.toString().padStart(3, ' ')}" }
    }

    @Test
    fun goldTest() {
        assertThat(
            gold(
                """
....#
#..#.
#..##
..#..
#....""".trimIndent(), 11
            )
        ).isEqualTo(99)
    }

    @Test
    fun gold() {
        assertThat(
            gold(
                """
.....
...#.
.#..#
.#.#.
...##""".trimIndent(), 201
            )
        ).isEqualTo(2040)
    }

    private fun gold(mapString: String, minutes: Int): Int {

        val last = generateSequence(parseMap(mapString)) { map -> additionalInfestation(map) }
            .take(minutes)
            .last()

        printMap(last.filterKeys { it.z == 0 })

        return last
            .values
            .count { it == '#' }
    }

    val pointCache = mutableMapOf<Point, List<Point>>()
    private fun Point.surroundings(recursive: Boolean = false): List<Point> {
        return when {
            !recursive -> listOf(
                up(),
                left(),
                right(),
                down()
            )
            else -> /*pointCache.computeIfAbsent(this) TODO */ run {
                when (pointToLetter[copy(z = 0)]) {
                    "A" -> listOf(
                        point(12).copy(z = z + -1),
                        point(8).copy(z = z + -1),
                        right(),
                        down()
                    )
                    "B", "C", "D" -> listOf(
                        left(),
                        point(8).copy(z = z + -1),
                        right(),
                        down()
                    )
                    "E" -> listOf(
                        left(),
                        point(8).copy(z = z + -1),
                        point(14).copy(z = z + -1),
                        down()
                    )
                    "F", "K", "P" -> listOf(
                        point(12).copy(z = z + -1),
                        up(),
                        right(),
                        down()
                    )
                    "J", "O", "T" -> listOf(
                        left(),
                        up(),
                        point(14).copy(z = z + -1),
                        down()
                    )
                    "V", "W", "X" -> listOf(
                        left(),
                        up(),
                        right(),
                        point(18).copy(z = z + -1)
                    )
                    "G", "I", "Q", "S" -> surroundings(false)
                    "H" -> listOf(
                        left(),
                        up(),
                        right(),
                        point(1).copy(z = z - -1),
                        point(2).copy(z = z - -1),
                        point(3).copy(z = z - -1),
                        point(4).copy(z = z - -1),
                        point(5).copy(z = z - -1)
                    )
                    "L" -> listOf(
                        left(),
                        up(),
                        point(1).copy(z = z - -1),
                        point(6).copy(z = z - -1),
                        point(11).copy(z = z - -1),
                        point(16).copy(z = z - -1),
                        point(21).copy(z = z - -1),
                        down()
                    )
                    "N" -> listOf(
                        point(5).copy(z = z - -1),
                        point(10).copy(z = z - -1),
                        point(15).copy(z = z - -1),
                        point(20).copy(z = z - -1),
                        point(25).copy(z = z - -1),
                        up(),
                        right(),
                        down()
                    )
                    "R" -> listOf(
                        left(),
                        point(21).copy(z = z - -1),
                        point(22).copy(z = z - -1),
                        point(23).copy(z = z - -1),
                        point(24).copy(z = z - -1),
                        point(25).copy(z = z - -1),
                        right(),
                        down()
                    )
                    "U" -> listOf(
                        point(12).copy(z = z + -1),
                        up(),
                        right(),
                        point(18).copy(z = z + -1)
                    )
                    "Y" -> listOf(
                        left(),
                        up(),
                        point(14).copy(z = z + -1),
                        point(18).copy(z = z + -1)
                    )
                    "M" -> emptyList()
                    else -> throw Exception("No no no! $this, ${pointToLetter[copy(z = 0)]}")
                }.also {
                    check(it.size == 4 || it.size == 8 || it.isEmpty()) { "Wrong size for ${pointToLetter[copy(z = 0)]}" }
                }
            }
        }
    }

    private fun additionalInfestation(map: Map<Point, Char>): Map<Point, Char> {
        return giveBugsSomeSpace(map)
            .mapValues { (k, v) ->
                when {
                    // A bug dies (becoming an empty space) unless there is exactly one bug adjacent to it.
                    v == '#' && k.surroundings(true).count { map[it] == '#' } != 1 -> '.'
                    // An empty space becomes infested with a bug if exactly one or two bugs are adjacent to it.
                    v == '.' && k.surroundings(true).count { map[it] == '#' } in (1..2) -> '#'
                    // Otherwise, a bug or empty space remains the same.
                    else -> v
                }
            }
    }

    /** make sure bugs have enough space to infest */
    private fun giveBugsSomeSpace(map: Map<Point, Char>): Map<Point, Char> {
        val beforeBugs = map.values.count { it == '#' }
        val levelsWithBugs = map.entries
            .filter { (k, v) -> v == '#' }
            .map { (k, v) -> k.z }
            .distinct()

        val topWithBugs = levelsWithBugs.max()!!
        val bottomWithBugs = levelsWithBugs.min()!!
        val newMap = map.toMutableMap()
        levelPoints.flatMap { listOf(it.copy(z = topWithBugs + 1), it.copy(z = bottomWithBugs - 1)) }
            .forEach { next ->
                newMap.putIfAbsent(next, '.')
            }
        check(beforeBugs == newMap.values.count { it == '#' })
        return newMap
    }
}

fun <T : Any> Sequence<T>.firstDuplicate(): T {
    return scan<T, Pair<Set<T>, T?>>(setOf<T>() to null) { (collected, _), next ->
        if (next in collected) {
            collected to next
        } else {
            collected.plus(next) to null
        }
    }
        .first { (_, duplicate) -> duplicate != null }
        .second!!
}

fun Point.left() = copy(x = x - 1)
fun Point.right() = copy(x = x + 1)

/** for inverted Y axle */
fun Point.up() = copy(y = y - 1)

/** for inverted Y axle */
fun Point.down() = copy(y = y + 1)
