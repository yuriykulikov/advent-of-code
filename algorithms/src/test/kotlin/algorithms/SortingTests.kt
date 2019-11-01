package algorithms

import org.assertj.core.api.Assertions.assertThat
import org.junit.Test

class SortingTests {
    @Test
    fun `shuffled int array is sorted with quicksort`() {
        val shuffled = (1..100).shuffled() + 100 + 100 + 100

        assertThat(shuffled.quickSort()).isEqualTo(shuffled.sorted())
    }

    @Test
    fun `shuffled int array is sorted with heapsort`() {
        val shuffled = (1..100).shuffled()

        assertThat(shuffled.heapSort()).isEqualTo(shuffled.sorted())
    }

    @Test
    fun `shuffled int array is sorted with intQuickSort`() {
        val shuffled = (1..100).shuffled()

        assertThat(shuffled.quickSortInt()).isEqualTo(shuffled.sorted())
    }
}