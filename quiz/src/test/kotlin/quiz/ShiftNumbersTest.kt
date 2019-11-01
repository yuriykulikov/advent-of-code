package quiz

import org.assertj.core.api.Assertions.assertThat
import org.junit.Test

/**
 *
 */
class ShiftNumbersTest {
    fun List<Int>.shiftZeroes(): List<Int> {
        val moreThanZero: List<Int> = this.filter { it > 0 }
        val zeroes = IntArray(this.size - moreThanZero.size) { 0 }
        return moreThanZero.plus(zeroes.toList())
    }

    @Test
    fun `zeroes should be moved to the right`() {
        assertThat(
            listOf(1, 0, 2, 0, 3, 0, 0, 5, 0, 5, 0).shiftZeroes()
        )
            .containsExactly(1, 2, 3, 5, 5, 0, 0, 0, 0, 0, 0)
    }
}