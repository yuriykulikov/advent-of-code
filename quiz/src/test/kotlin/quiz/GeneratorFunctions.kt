package quiz

import kotlinx.collections.immutable.toPersistentList
import org.assertj.core.api.Assertions
import org.junit.Test
import kotlin.math.absoluteValue

/**
 * 2019.10.30 Meetup with Volodymyr
 */
class GeneratorFunctions {
    @Test
    fun `task 2`() {
        // i = [0, 1, 2, 3, 4, 5, 6]
        Assertions.assertThat(
            (0..6).toList()
        ).contains(0, 1, 2, 3, 4, 5, 6)
        // a = [1, 2, 3, 4, 5, 6, 7]
        Assertions.assertThat(
            generateSequence(0) { it + 2 }.take(8).toList()
        ).contains(0, 2, 4, 6, 8, 10, 12)
        // b = [0, 2, 4, 6, 8, 10, 12]
        Assertions.assertThat(
            generateSequence(1) { it + 2 }.take(8).toList()
        ).contains(1, 3, 5, 7, 9, 11, 13)
        // c = [1, 3, 5, 7, 9, 11, 13]
        Assertions.assertThat(
            (0..5).map { 2.shl(it) }.toPersistentList().add(0, 1)
        ).contains(1, 2, 4, 8, 16, 32, 64)
        // d = [1, 2, 4, 8, 16, 32, 64]
        Assertions.assertThat(
            (-3..3).map { it.absoluteValue }
        ).contains(3, 2, 1, 0, 1, 2, 3)
        // e = [3, 2, 1, 0, 1, 2, 3]
        Assertions.assertThat(
            (0..6).map { it * it }.toPersistentList()
        ).contains(0, 1, 4, 9, 16, 25, 36)
        // f = [0, 1, 4, 9, 16, 25, 36]
        Assertions.assertThat(
            generateSequence(0) { if (it == 0) 1 else 0 }.take(8).toList()
        ).contains(0, 1, 0, 1, 0, 1, 0)
        // g = [0, 1, 0, 1, 0, 1, 0]

    }

    @Test
    fun `task 2 2`() {
        // i = [0, 1, 2, 3, 4, 5, 6]
        // a = [1, 2, 3, 4, 5, 6, 7]
        Assertions.assertThat(
            IntArray(7) { index -> index }
        ).contains(0, 1, 2, 3, 4, 5, 6)
        // b = [0, 2, 4, 6, 8, 10, 12]
        Assertions.assertThat(
            IntArray(7) { index -> index * 2 }
        ).contains(0, 2, 4, 6, 8, 10, 12)
        // c = [1, 3, 5, 7, 9, 11, 13]
        Assertions.assertThat(
            IntArray(7) { index -> index * 2 + 1 }
        ).contains(1, 3, 5, 7, 9, 11, 13)
        // d = [1, 2, 4, 8, 16, 32, 64]
        Assertions.assertThat(
            IntArray(7) { index -> 1.shl(index) }
        ).contains(1, 2, 4, 8, 16, 32, 64)
        // e = [3, 2, 1, 0, 1, 2, 3]
        Assertions.assertThat(
            IntArray(7) { index -> (index - 3).absoluteValue }
        ).contains(3, 2, 1, 0, 1, 2, 3)
        // f = [0, 1, 4, 9, 16, 25, 36]
        Assertions.assertThat(
            IntArray(7) { index -> index * index }
        ).contains(0, 1, 4, 9, 16, 25, 36)
        // g = [0, 1, 0, 1, 0, 1, 0]
        Assertions.assertThat(
            IntArray(7) { index -> index.div(2) }
        ).contains(0, 1, 0, 1, 0, 1, 0)
    }
}