import org.assertj.core.api.Assertions.assertThat
import org.junit.Test

class Day4SecureContainer {
    @Test
    fun spl() {
        assertThat(585159.digits()).containsSequence(5, 8, 5, 1, 5, 9)
    }

    fun Int.digits(): List<Int> {
        val number = this
        val first = number / 100000
        val second = number / 10000 % 10
        val third = number / 1000 % 10
        val fourth = number / 100 % 10
        val fifth = number / 10 % 10
        val sixths = number / 1 % 10
        return listOf(
            first,
            second,
            third,
            fourth,
            fifth,
            sixths
        )
    }

    @Test
    fun silver() {
        val count = (134564..585159)
            .count { number ->
                isValidPassword(number)
            }
        assertThat(count).isEqualTo(1929)
    }

    @Test
    fun gold() {
        val count = (134564..585159)
            .count { number ->
                isValidPasswordGold(number)
            }
        assertThat(count).isEqualTo(1306)
    }


    private fun isValidPassword(number: Int): Boolean {
        val digits = number.digits()
        val consequent = (0 until 5).any { i ->
            digits[i] == digits[i + 1]
        }
        val ascending = (0 until 5).all { i ->
            digits[i] <= digits[i + 1]
        }
        return consequent && ascending
    }

    private fun isValidPasswordGold(number: Int): Boolean {
        val digits = number.digits()
        val consequent = (0 until 5).any { i ->
            digits[i] == digits[i + 1]
                    && digits.getOrNull(i + 2) != digits[i + 1]
                    && digits.getOrNull(i - 1) != digits[i]
        }
        val ascending = (0 until 5).all { i ->
            digits[i] <= digits[i + 1]
        }
        return consequent && ascending
    }

    @Test
    fun silverTest() {
        assertThat(isValidPassword(111111)).isTrue()
        assertThat(isValidPassword(223450)).isFalse()
        assertThat(isValidPassword(123789)).isFalse()
    }

    @Test
    fun goldTest() {
        assertThat(isValidPasswordGold(112233)).isTrue()
        assertThat(isValidPasswordGold(123444)).isFalse()
        assertThat(isValidPasswordGold(111122)).isTrue()
    }


}