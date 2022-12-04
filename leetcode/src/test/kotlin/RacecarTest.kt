import org.assertj.core.api.Assertions.assertThat
import org.junit.Test

class RacecarTest {
  private fun racecar(i: Int): Int {
    return Racecar().racecar(i)
  }
  @Test
  fun test3() {
    assertThat(racecar(3)).isEqualTo(2)
  }

  @Test
  fun test6() {
    assertThat(racecar(6)).isEqualTo(5)
  }
  @Test
  fun test330() {
    assertThat(racecar(330)).isEqualTo(24)
  }

  @Test
  fun test5617() {
    assertThat(racecar(5617)).isEqualTo(41)
  }
  @Test
  fun test5478() {
    assertThat(racecar(5478)).isEqualTo(50)
  }
}
