import org.assertj.core.api.Assertions.assertThat
import org.junit.Test

class AtoiSolution {
    fun myAtoi(str: String): Int {
        val numberStr = str.trim()
            .dropWhile { !it.isDigit() }
            .takeWhile { it.isDigit() }
            .dropWhile { it == '0' }
        if (numberStr.isEmpty()) return 0
        val prefix = str.subSequence(0, str.indexOf(numberStr)).filter { it != '0' }
        if (prefix.count { !it.isWhitespace() } > 1) return 0
        if (prefix.any { it.isLetter() || it == '.' }) return 0
        if (prefix.isNotBlank() && prefix.last() == ' ') return 0

        val negative = prefix.lastOrNull() == '-'

        return when {
            numberStr.length > 18 && negative -> Int.MIN_VALUE
            numberStr.length > 18 -> Int.MAX_VALUE
            negative -> Math.max(-numberStr.parse(), Int.MIN_VALUE.toLong()).toInt()
            else -> Math.min(numberStr.parse(), Int.MAX_VALUE.toLong()).toInt()
        }
    }

    private fun String.parse(): Long {
        return this.reversed().foldIndexed(0L) { index, acc, char ->
            acc + (char - 48).toLong() * 10.pow(index)
        }
    }

    fun String.atoi(): Int = myAtoi(this)

    private fun Int.pow(index: Int): Long {
        var ret = 1L
        repeat(index) { ret *= this }
        return ret
    }

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

