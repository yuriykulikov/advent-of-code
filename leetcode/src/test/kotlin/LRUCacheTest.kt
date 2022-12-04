import org.assertj.core.api.Assertions.assertThat
import org.junit.Test

class LRUCacheTest {
  @Test
  fun test1() {
    val cache = LRUCache(2)
    with(cache) {
      put(1, 1)
      put(2, 2)
      assertThat(get(1)).isEqualTo(1)
      put(3, 3)
      assertThat(get(2)).isEqualTo(-1)
      put(4, 4)
      assertThat(get(1)).isEqualTo(-1)
      assertThat(get(3)).isEqualTo(3)
      assertThat(get(4)).isEqualTo(4)
    }
  }
}
