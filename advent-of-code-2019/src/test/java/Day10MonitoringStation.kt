import org.assertj.core.api.AbstractFloatAssert
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.data.Percentage
import org.junit.Test
import kotlin.math.PI
import kotlin.math.atan2
import kotlin.math.sqrt

class Day10MonitoringStation {
    data class Asteroid(val x: Int, val y: Int) {
        override fun toString(): String = "($x, $y)"
    }

    private fun parseInput(input: String): List<Asteroid> {
        return input
            .lines()
            .mapIndexed { y, line -> line to y }
            .flatMap { (line, y): Pair<String, Int> ->
                line.mapIndexedNotNull { x, char: Char ->
                    when (char) {
                        '#' -> Asteroid(x, y)
                        else -> null
                    }
                }
            }
    }

    private fun Asteroid.angleTo(center: Asteroid): Float {
        // mind the inverted y axle
        return atan2(-(y - center.y).toDouble(), (x - center.x).toDouble()).toFloat()
    }

    private fun mostVisibleAsteroids(input: String): Pair<Asteroid, Int> {
        val asteroids: List<Asteroid> = parseInput(input)

        val asteroidToDistinctAngles = asteroids.map { center ->
            val distinctAngles = asteroids.minus(center)
                .map { asteroid -> asteroid.angleTo(center) }
                .distinct()
                .size

            center to distinctAngles
        }

        return asteroidToDistinctAngles.maxBy { (asteroid, distinctAngles) -> distinctAngles }!!
    }

    @Test
    fun atan() {
        fun AbstractFloatAssert<*>.isCloseTo(other: Double) {
            isCloseTo(other.toFloat(), Percentage.withPercentage(10.0))
        }
        assertThat(Asteroid(0, -10).angleTo(Asteroid(0, 0))).isCloseTo(PI / 2)
        assertThat(Asteroid(10, 0).angleTo(Asteroid(0, 0))).isCloseTo(0.0)
        assertThat(Asteroid(-10, 0).angleTo(Asteroid(0, 0))).isCloseTo(-PI)
        assertThat(Asteroid(-10, 1).angleTo(Asteroid(0, 0))).isCloseTo(-PI)
        assertThat(Asteroid(0, 10).angleTo(Asteroid(0, 0))).isCloseTo(-PI / 2)
    }

    @Test
    fun goldStar() {
        val vaporised = vaporisations(parseInput(testInput), Asteroid(27, 19))
        assertThat(vaporised[-1 + 200]).isEqualTo(Asteroid(15, 13))
        assertThat(vaporised[-1 + 200].x * 100 + vaporised[-1 + 200].y).isEqualTo(1513)
    }

    fun vaporisations(asteroids: List<Asteroid>, center: Asteroid): List<Asteroid> {
        fun Asteroid.degAngle(): Float {
            val rad = atan2(/* inverted Y axle */-(y - center.y).toDouble(), (x - center.x).toDouble())

            val grad = Math.toDegrees(rad)
            return (-grad + 360 + 90).toFloat() % 360
        }

        /** Angle in parts of PI */
        fun Asteroid.piAngle(): Float {
            val rad = atan2(/* inverted Y axle */-(y - center.y).toDouble(), (x - center.x).toDouble())
            val pgrad = rad / PI
            return (-pgrad + 2.5f).toFloat() % 2
        }

        fun Asteroid.distance(): Double {
            return sqrt(((x - center.x).square() + (y - center.y).square()))
        }

        val shootings = asteroids
            .minus(center)
            .sortedBy { it.piAngle() }
            .groupBy { it.piAngle() }
            .mapValues { (key, value) -> value.sortedBy { it.distance() } }

        shootings.forEach {
            // println(it)
        }

        val bingo = generateSequence(0 to emptyList<Asteroid>()) { (index, _) ->
            index + 1 to shootings.values.mapNotNull { it.getOrNull(index) }
        }
            .take(10)
            .flatMap { (index, seq) -> seq.asSequence() }
            .take(333)
            .toList()

        bingo.forEachIndexed { index, it ->
            val (x, y) = it
            // println("$index grad: ${it.piAngle()} (${x - center.x}, ${y - center.y}), ($x, $y)")
        }

        return bingo
    }

    @Test
    fun verifyGoldStar() {
        val vaporised = vaporisations(
            parseInput(
                """
.#..##.###...#######
##.############..##.
.#.######.########.#
.###.#######.####.#.
#####.##.#.##.###.##
..#####..#.#########
####################
#.####....###.#.#.##
##.#################
#####.##.###..####..
..######..##.#######
####.##.####...##..#
.#####..#.######.###
##...#.##########...
#.##########.#######
.####.#.###.###.#.##
....##.##.###..#####
.#.#.###########.###
#.#.#.#####.####.###
###.##.####.##.#..##
        """.trimIndent()
            ), Asteroid(11, 13)
        )

        assertThat(vaporised[-1 + 1]).isEqualTo(Asteroid(11, 12))
        assertThat(vaporised[-1 + 2]).isEqualTo(Asteroid(12, 1))
        assertThat(vaporised[-1 + 3]).isEqualTo(Asteroid(12, 2))
        assertThat(vaporised[-1 + 10]).isEqualTo(Asteroid(12, 8))
        assertThat(vaporised[-1 + 20]).isEqualTo(Asteroid(16, 0))
        assertThat(vaporised[-1 + 50]).isEqualTo(Asteroid(16, 9))
        assertThat(vaporised[-1 + 100]).isEqualTo(Asteroid(10, 16))
        assertThat(vaporised[-1 + 199]).isEqualTo(Asteroid(9, 6))
        assertThat(vaporised[-1 + 200]).isEqualTo(Asteroid(8, 2))
        assertThat(vaporised[-1 + 201]).isEqualTo(Asteroid(10, 9))
        // assertThat(shooted[-1 + 299]).isEqualTo(Asteroid(11, 1))
    }

    @Test
    fun verifySilverStar1() {
        assertThat(
            mostVisibleAsteroids(
                """
.#..#
.....
#####
....#
...##
""".trimIndent()
            )
        ).isEqualTo(Asteroid(3, 4) to 8)
    }

    @Test
    fun verifSilverStar2() {
        assertThat(
            mostVisibleAsteroids(
                """
......#.#.
#..#.#....
..#######.
.#.#.###..
.#..#.....
..#....#.#
#..#....#.
.##.#..###
##...#..#.
.#....####
""".trimIndent()
            )
        ).isEqualTo(Asteroid(5, 8) to 33)
    }

    @Test
    fun verifySilverStar3() {
        assertThat(
            mostVisibleAsteroids(
                """
#.#...#.#.
.###....#.
.#....#...
##.#.#.#.#
....#.#.#.
.##..###.#
..#...##..
..##....##
......#...
.####.###.
""".trimIndent()
            )
        ).isEqualTo(Asteroid(1, 2) to 35)
    }

    @Test
    fun verifySilverStar4() {
        assertThat(
            mostVisibleAsteroids(
                """
.#..#..###
####.###.#
....###.#.
..###.##.#
##.##.#.#.
....###..#
..#.#..#.#
#..#.#.###
.##...##.#
.....#.#..
""".trimIndent()
            )
        ).isEqualTo(Asteroid(6, 3) to 41)
    }

    @Test
    fun verifySilverStar5() {
        assertThat(
            mostVisibleAsteroids(
                """
.#..##.###...#######
##.############..##.
.#.######.########.#
.###.#######.####.#.
#####.##.#.##.###.##
..#####..#.#########
####################
#.####....###.#.#.##
##.#################
#####.##.###..####..
..######..##.#######
####.##.####...##..#
.#####..#.######.###
##...#.##########...
#.##########.#######
.####.#.###.###.#.##
....##.##.###..#####
.#.#.###########.###
#.#.#.#####.####.###
###.##.####.##.#..##
""".trimIndent()
            )
        ).isEqualTo(Asteroid(11, 13) to 210)
    }

    @Test
    fun silverStar() {
        assertThat(mostVisibleAsteroids(testInput)).isEqualTo(Asteroid(27, 19) to 314)
    }

    private val testInput = """
..#..###....#####....###........#
.##.##...#.#.......#......##....#
#..#..##.#..###...##....#......##
..####...#..##...####.#.......#.#
...#.#.....##...#.####.#.###.#..#
#..#..##.#.#.####.#.###.#.##.....
#.##...##.....##.#......#.....##.
.#..##.##.#..#....#...#...#...##.
.#..#.....###.#..##.###.##.......
.##...#..#####.#.#......####.....
..##.#.#.#.###..#...#.#..##.#....
.....#....#....##.####....#......
.#..##.#.........#..#......###..#
#.##....#.#..#.#....#.###...#....
.##...##..#.#.#...###..#.#.#..###
.#..##..##...##...#.#.#...#..#.#.
.#..#..##.##...###.##.#......#...
...#.....###.....#....#..#....#..
.#...###..#......#.##.#...#.####.
....#.##...##.#...#........#.#...
..#.##....#..#.......##.##.....#.
.#.#....###.#.#.#.#.#............
#....####.##....#..###.##.#.#..#.
......##....#.#.#...#...#..#.....
...#.#..####.##.#.........###..##
.......#....#.##.......#.#.###...
...#..#.#.........#...###......#.
.#.##.#.#.#.#........#.#.##..#...
.......#.##.#...........#..#.#...
.####....##..#..##.#.##.##..##...
.#.#..###.#..#...#....#.###.#..#.
............#...#...#.......#.#..
.........###.#.....#..##..#.##...
""".trimIndent()
}

private fun Int.square() = (this * this).toDouble()
