package aoc

import org.assertj.core.api.Assertions.assertThat
import org.junit.Test

class Advent11 {
    data class Point(val x: Int, val y: Int, val gridSerialNumber: Int) {
        val powerLevel by lazy {
            val rackId = x + 10
            val beginPowerLevel = rackId * y
            val increasedPowerLevel = beginPowerLevel + gridSerialNumber
            val multiplied = increasedPowerLevel * rackId
            multiplied.rem(1000).div(100) - 5
        }
    }

    @Test
    fun `Fuel cell at  122,79, grid serial number 57 power level -5`() {
        assertThat(Point(122, 79, 57).powerLevel).isEqualTo(-5)
    }


    @Test
    fun `Fuel cell at  3,5, grid serial number 8 power level 4`() {
        assertThat(Point(3, 5, 8).powerLevel).isEqualTo(4)
    }

    @Test
    fun `Fuel cell at 217,196, grid serial number 39 power level  0`() {
        assertThat(Point(217, 196, 39).powerLevel).isEqualTo(0)
    }

    @Test
    fun `Fuel cell at 101,153, grid serial number 71 power level  4`() {
        assertThat(Point(101, 153, 71).powerLevel).isEqualTo(4)
    }

    interface Matrix {
        val points: List<Point>

        fun get(x: Int, y: Int): Point
    }

    class ListMatrix(serial: Int) : Matrix {
        private val matrix: List<List<Point>> = (1..300).map { x ->
            (1..300).map { y -> Point(x, y, serial) }
        }

        override val points: List<Point> = matrix.flatten()

        override fun get(x: Int, y: Int): Point {
            return matrix[x - 1][y - 1]
        }
    }

    @Test
    fun `Example values are correct`() {
        val matrix = ListMatrix(42)

        assertThat(matrix.get(21, 61).x).isEqualTo(21)
        assertThat(matrix.get(21, 61).y).isEqualTo(61)
        assertThat(matrix.get(21, 61).powerLevel).isEqualTo(4)
    }

    @Test
    fun `Example grid is propertly filled`() {
        val maxPower = maxPower3(ListMatrix(42)).first

        assertThat("${maxPower.x},${maxPower.y}").isEqualTo("21,61")
    }


    @Test
    fun `Actual grid is propertly filled`() {
        val maxPower = maxPower3(ListMatrix(7803)).first

        assertThat("${maxPower.x},${maxPower.y}").isEqualTo("20,51")
    }

    @Test
    fun `Awesome`() {
        assertThat(ListMatrix(7803).points.size).isEqualTo(300 * 300)
    }

    fun Matrix.getArea(point: Point, size: Int): List<Point> {
        return if (point.x + size in 1..300 && point.y + size in 1..300) {
            val area = (point.x until point.x + size).flatMap { x ->
                (point.y until point.y + size).map { y ->
                    get(x, y)
                }
            }
            assertThat(area.size).isEqualTo(size * size)
            area
        } else {
            emptyList()
        }
    }

    private fun maxPower3(matrix: Matrix): Pair<Point, Int> {
        val points: List<Point> = matrix.points

        fun Point.totalPower() = matrix.getArea(this, 3).sumBy { it.powerLevel }

        val maxPoint: Point = points.maxBy { point -> point.totalPower() }!!

        return maxPoint to maxPoint.totalPower()
    }

    @Test
    fun `Star 2 (cheat)`() {
        val matrix = ListMatrix(42)

        val points: List<Point> = matrix.points

        fun Point.totalPower(size: Int) = matrix.getArea(this, size).sumBy { it.powerLevel }

        val calcs: List<Pair<Point, Int>> =
            (0..20).mapNotNull { size -> points.maxBy { point -> point.totalPower(size) }!! to size }

        val winner = calcs.maxBy { (point, size) -> point.totalPower(size) }!!

        val point = winner.first
        val size = winner.second

        assertThat("${point.x},${point.y},$size").isEqualTo("232,251,12")
    }


    @Test
    fun `Star 2 (cheat non test)`() {
        val matrix = ListMatrix(7803)

        val points: List<Point> = matrix.points

        fun Point.totalPower(size: Int) = matrix.getArea(this, size).sumBy { it.powerLevel }

        val calcs: List<Pair<Point, Int>> =
            (0..20).mapNotNull { size -> points.maxBy { point -> point.totalPower(size) }!! to size }

        val winner = calcs.maxBy { (point, size) -> point.totalPower(size) }!!

        val point = winner.first
        val size = winner.second

        assertThat("${point.x},${point.y},$size").isEqualTo("230,272,17")
    }
}

