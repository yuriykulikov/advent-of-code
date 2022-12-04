import org.assertj.core.api.Assertions.assertThat
import org.junit.Test



class AtoiTest {
  fun String.atoi(): Int = AtoiSolution().myAtoi(this)
  @Test
  fun test() {
    assertThat("42".atoi()).isEqualTo(42)
    assertThat("   -42".atoi()).isEqualTo(-42)
    assertThat("4193 with words".atoi()).isEqualTo(4193)
    assertThat("words and 987".atoi()).isEqualTo(0)
    assertThat("-91283472332".atoi()).isEqualTo(-2147483648)
    assertThat("3.14".atoi()).isEqualTo(3)
    assertThat("+2".atoi()).isEqualTo(2)
    assertThat("+-2".atoi()).isEqualTo(0)
    assertThat(".2".atoi()).isEqualTo(0)
    assertThat("9223372036854775808".atoi()).isEqualTo(2147483647)
    assertThat("  0000000000012345678".atoi()).isEqualTo(12345678)
    assertThat("-   234".atoi()).isEqualTo(0)
  }
}
