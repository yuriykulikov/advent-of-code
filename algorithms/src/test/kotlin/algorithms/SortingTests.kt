package algorithms

import org.assertj.core.api.Assertions.assertThat
import org.junit.Test

class SortingTests {
  @Test
  fun `shuffled int array is sorted with quicksort`() {
    val shuffled = (1..100).shuffled() + 100 + 100 + 100
    assertThat(shuffled.quickSort(inPlace = false)).isEqualTo(shuffled.sorted())
  }

  @Test
  fun `shuffled int array is sorted with quicksort in place`() {
    val shuffled = (1..100).shuffled() + 100 + 100 + 100

    assertThat(shuffled.quickSort(inPlace = true)).isEqualTo(shuffled.sorted())
  }

  @Test
  fun `shuffled int array is sorted with mergesort`() {
    val shuffled = (1..100).shuffled() + 100 + 100 + 100

    assertThat(shuffled.mergesort()).isEqualTo(shuffled.sorted())
  }
  @Test
  fun `shuffled int array is sorted with heapsort`() {
    val shuffled = (1..100).shuffled()

    assertThat(shuffled.heapSort()).isEqualTo(shuffled.sorted())
  }
}
