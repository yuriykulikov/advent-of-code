import org.assertj.core.api.Assertions.assertThat
import org.junit.Test

class Day3SpiralMemory2 {
    data class Point(val x: Int, val y: Int)

    val matrix = mutableMapOf<Point, Int>().apply {
        put(Point(0, 0), 1)
        put(Point(1, 0), 1)
    }

    // Your puzzle input is 277678.
    @Test
    fun `Star 2`() {

        // 147  142  133  122   59
        // 304    5    4    2   57
        // 330   10    1    1   54
        // 351   11   23   25   26
        // 362  747  806--->   ...
        generateSequence(Point(1, 0) to 1) { (prevPoint, writtenValue) ->
            val nextPoint = when {
                matrix[prevPoint.up()].notFilled() && matrix[prevPoint.left()].filled() -> prevPoint.up()
                matrix[prevPoint.left()].notFilled() && matrix[prevPoint.down()].filled() -> prevPoint.left()
                matrix[prevPoint.down()].notFilled() && matrix[prevPoint.right()].filled() -> prevPoint.down()
                matrix[prevPoint.right()].notFilled() && matrix[prevPoint.up()].filled() -> prevPoint.right()
                else -> throw Exception("prevPoint: $prevPoint, matrix: $matrix")
            }

            val newValue =
                listOfNotNull(
                    matrix[nextPoint.copy(x = nextPoint.x + 1, y = nextPoint.y + 1)],
                    matrix[nextPoint.copy(x = nextPoint.x + 1, y = nextPoint.y)],
                    matrix[nextPoint.copy(x = nextPoint.x + 1, y = nextPoint.y - 1)],
                    matrix[nextPoint.copy(x = nextPoint.x, y = nextPoint.y + 1)],
                    matrix[nextPoint.copy(x = nextPoint.x, y = nextPoint.y - 1)],
                    matrix[nextPoint.copy(x = nextPoint.x - 1, y = nextPoint.y + 1)],
                    matrix[nextPoint.copy(x = nextPoint.x - 1, y = nextPoint.y)],
                    matrix[nextPoint.copy(x = nextPoint.x - 1, y = nextPoint.y - 1)]
                ).sum()

            matrix[nextPoint] = newValue

            nextPoint to newValue
        }
            .take(100)
            // .onEach { println(it)  }
            .dropWhile { it.second < 277678 }
            .first()
            .second
            .also { println(it) }
            .also { assertThat(it).isEqualTo(279138) }
    }

    private fun Point.left() = copy(x = x - 1)
    private fun Point.right() = copy(x = x + 1)
    private fun Point.up() = copy(y = y + 1)
    private fun Point.down() = copy(y = y - 1)
    private fun Int?.filled() = this != null
    private fun Int?.notFilled() = this == null
}