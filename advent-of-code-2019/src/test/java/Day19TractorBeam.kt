import kotlinx.collections.immutable.toPersistentMap
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test
import java.lang.Boolean.TRUE

/**
 * Almost straightforward bruteforce. See [computeGold]
 */
class Day19TractorBeam {
    @Test
    fun silver() {
        val map = (0 until 50).flatMap { y -> (0 until 50).map { x -> Point(x, y) } }
            .map { point ->
                point to point.isTracking()
            }.toMap()

        printMap(map, invertedY = true) { "${if (it == true) '#' else '.'}" }

        assertThat(map.values.count { TRUE == it }).isEqualTo(158)
    }

    @Test
    fun goldTest() {

        val side = 2

        val (x, y) = computeGold(side)

        check(Point(x, y).isTracking())
        check(Point(x + side, y + side).isTracking())
        println("Start $x, $y")
        println("Answer is ${x * 10000 + y} ")
        printPart(Point(x, y), side)
        assertThat(x).isEqualTo(7)
        assertThat(y).isEqualTo(13)
        assertThat(x * 10000 + y).isEqualTo(70013)
    }

    @Test
    fun goldTest2() {

        val side = 25

        val (x, y) = computeGold(side)

        check(Point(x, y).isTracking())
        check(Point(x + side, y + side).isTracking())
        println("Start $x, $y")
        println("Answer is ${x * 10000 + y} ")
        printPart(Point(x, y), side)
        assertThat(x).isEqualTo(151)
        assertThat(y).isEqualTo(284)
        assertThat(x * 10000 + y).isEqualTo(1510284)
    }

    @Test
    fun gold() {

        val side = 100

        val (x, y) = computeGold(side)

        check(Point(x, y).isTracking())
        check(Point(x + side, y + side).isTracking())
        println("Start $x, $y")
        println("Answer is ${x * 10000 + y} ")
        printPart(Point(x, y), side)
        assertThat(x).isEqualTo(619)
        assertThat(y).isEqualTo(1165)
        assertThat(x * 10000 + y).isEqualTo(6191165)
    }

    /**
     * Go down on the Y axle, for each y value check if there are enough #
     * If yes, check [side] points below, there must also be enough #
     *
     * Since X always grows, we can use seedX to optimize.
     */
    private fun computeGold(side: Int): Point {
        var seedX = 0

        return generateSequence(5) { it + 1 }
            .mapNotNull { y ->
                // println("y: $y")
                val line: Sequence<Point> = tracktorLine(y, seedX)
                val below: Sequence<Point> = line.map { (x, y) -> Point(x, y + side - 1) }

                seedX = line.firstOrNull { it.isTracking() }?.x ?: 0

                val good = (line.count { it.isTracking() } >= side
                        && below.count { it.isTracking() } >= side)

                if (good) Point(below.first { it.isTracking() }.x, y) else null
            }
            .first()
    }

    private fun tracktorLine(y: Int, seedX: Int = 0): Sequence<Point> {
        return generateSequence(seedX) { it + 1 }
            .map { x -> Point(x, y) to Point(x, y).isTracking() }
            .dropWhile { (point, isTracking) -> !isTracking }
            .takeWhile { (point, isTracking) -> isTracking }
            .map { (point, isTracking) -> point }
    }

    private fun printPart(start: Point, side: Int) {
        println("Print $start, side: $side")
        val map = (start.y - 1 until (start.y + side + 1)).flatMap { y ->
            (start.x - 1 until start.x + side + 1).map { x ->
                Point(
                    x,
                    y
                )
            }
        }
            .map { point ->
                point to when {
                    !point.isTracking() -> "."
                    point.x >= start.x && point.y >= start.y && point.y < (start.y + side) && point.x < (start.x + side) -> 'o'
                    point.isTracking() -> '#'
                    else -> "*"
                }
            }.toMap()
            .toPersistentMap()
            .put(start, "X")

        printMap(map, invertedY = true, prefixFun = { index -> "${start.y + index}   " }) { "${it}" }
    }

    private val cache: MutableMap<Point, Boolean> = mutableMapOf()
    private fun Point.isTracking(): Boolean {
        return cache.computeIfAbsent(this) {
            intCodeComputer(
                program = program.split(",").map { it.toInt() },
                input = *intArrayOf(x, y)
            ).runToHalt()
                .out
                .first()
                .let { it == 1L }
        }
    }

    private val program =
        "109,424,203,1,21101,0,11,0,1105,1,282,21101,0,18,0,1106,0,259,2102,1,1,221,203,1,21102,31,1,0,1105,1,282,21101,38,0,0,1105,1,259,21001,23,0,2,21201,1,0,3,21101,0,1,1,21101,0,57,0,1106,0,303,1202,1,1,222,20102,1,221,3,21002,221,1,2,21101,259,0,1,21102,80,1,0,1106,0,225,21101,0,189,2,21102,91,1,0,1105,1,303,2102,1,1,223,20101,0,222,4,21102,259,1,3,21101,225,0,2,21102,225,1,1,21102,1,118,0,1105,1,225,21001,222,0,3,21102,1,57,2,21102,1,133,0,1106,0,303,21202,1,-1,1,22001,223,1,1,21102,148,1,0,1106,0,259,1202,1,1,223,21001,221,0,4,20101,0,222,3,21101,0,24,2,1001,132,-2,224,1002,224,2,224,1001,224,3,224,1002,132,-1,132,1,224,132,224,21001,224,1,1,21101,195,0,0,106,0,108,20207,1,223,2,20102,1,23,1,21102,-1,1,3,21101,0,214,0,1106,0,303,22101,1,1,1,204,1,99,0,0,0,0,109,5,1201,-4,0,249,22101,0,-3,1,22101,0,-2,2,22102,1,-1,3,21102,250,1,0,1106,0,225,22101,0,1,-4,109,-5,2106,0,0,109,3,22107,0,-2,-1,21202,-1,2,-1,21201,-1,-1,-1,22202,-1,-2,-2,109,-3,2106,0,0,109,3,21207,-2,0,-1,1206,-1,294,104,0,99,21201,-2,0,-2,109,-3,2105,1,0,109,5,22207,-3,-4,-1,1206,-1,346,22201,-4,-3,-4,21202,-3,-1,-1,22201,-4,-1,2,21202,2,-1,-1,22201,-4,-1,1,21201,-2,0,3,21102,343,1,0,1105,1,303,1105,1,415,22207,-2,-3,-1,1206,-1,387,22201,-3,-2,-3,21202,-2,-1,-1,22201,-3,-1,3,21202,3,-1,-1,22201,-3,-1,2,21201,-4,0,1,21101,384,0,0,1106,0,303,1106,0,415,21202,-4,-1,-4,22201,-4,-3,-4,22202,-3,-2,-2,22202,-2,-4,-4,22202,-3,-2,-3,21202,-4,-1,-2,22201,-3,-2,1,22102,1,1,-4,109,-5,2105,1,0"
}