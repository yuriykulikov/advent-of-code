import org.assertj.core.api.Assertions
import org.junit.Test

class HappyNumbersTest {
  @Test
  fun happyVeryHappy() {
    Assertions.assertThat(HappyNumbers().isHappy(19)).isTrue
  }
}
