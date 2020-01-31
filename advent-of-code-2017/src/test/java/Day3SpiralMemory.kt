import org.assertj.core.api.Assertions.assertThat
import org.junit.Test
import kotlin.math.absoluteValue
import kotlin.math.roundToInt
import kotlin.math.sqrt

class Day3SpiralMemory {
    // 17  16  15  14  13
    // 18   5   4   3  12
    // 19   6   1   2  11
    // 20   7   8   9  10
    // 21  22  23  24  25

    // 37  36  35  34  33  32  31
    // 38  17  16  15  14  13  30
    // 39  18   5   4   3  12  29
    // 40  19   6   1   2  11  28
    // 41  20   7   8   9  10  27
    // 42  21  22  23  24  25  26
    // 43  44  45  46  47  48  49

    data class Point(val x: Int, val y: Int)

    private fun Int.nextOdd() = if (this.rem(2) == 0) this + 1 else this
    private fun spiralAccess(index: Int): Int {
        val maxNormalizedX = sqrt(index.toDouble()).roundToInt().nextOdd()
        val maxAbsX = maxNormalizedX / 2

        val rightRow = (-maxAbsX..maxAbsX).reversed().map { y -> Point(maxAbsX, y) }
        val topRow = (-maxAbsX..maxAbsX).reversed().map { x -> Point(x, -maxAbsX) }
        val leftRow = (-maxAbsX..maxAbsX).map { y -> Point(-maxAbsX, y) }
        val bottomRow = (-maxAbsX..maxAbsX).map { x -> Point(x, maxAbsX) }

        val points = (rightRow + topRow + leftRow + bottomRow).distinctBy { it }
        val (x, y) = points[maxNormalizedX * maxNormalizedX - index]
        return x.absoluteValue + y.absoluteValue
    }

    // Data from square 23 is carried only 2 steps: up twice.
    @Test
    fun `23 2 steps`() {
        assertThat(spiralAccess(23)).isEqualTo(2)
    }

    // Data from square 1024 must be carried 31 steps.
    @Test
    fun `1024 31 steps`() {
        assertThat(spiralAccess(1024)).isEqualTo(31)
    }

    // Your puzzle input is 277678.
    @Test
    fun `277678 X steps`() {
        assertThat(spiralAccess(277678)).isEqualTo(475)
    }
}