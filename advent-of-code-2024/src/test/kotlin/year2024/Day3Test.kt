package year2024.year2024

import io.kotest.matchers.shouldBe
import loadResource
import org.junit.jupiter.api.Test

class Day3Test {
    private val example = "xmul(2,4)%&mul[3,7]!@^do_not_mul(5,5)+mul(32,64]then(mul(11,8)mul(8,5))"

    @Test
    fun silverTest() {
        multiply(example) shouldBe 161
    }


    @Test
    fun silver() {
        multiply(loadResource("Day3")) shouldBe 182619815L
    }

    @Test
    fun goldTest() {
        multiplyDoDont("xmul(2,4)&mul[3,7]!^don't()_mul(5,5)+mul(32,64](mul(11,8)undo()?mul(8,5))") shouldBe 48
    }

    @Test
    fun gold() {
        multiplyDoDont(loadResource("Day3")) shouldBe 80747545L
    }


    private fun multiply(input: String): Long {
        return input.split("mul(")
            .map { it.substringBefore(")") }
            .filter { args -> args.all { it.isDigit() || it == ',' } }
            .mapNotNull {
                val split = it.split(",")
                if (split.size != 2) return@mapNotNull null
                val l = split.first().toLongOrNull()
                val r = split.last().toLongOrNull()
                if (l == null || r == null) return@mapNotNull null
                l * r
            }
            .sum()

    }

    private fun multiplyDoDont(input: String): Long {
        return sequenceOf(0 to true) //
            .plus(input.indexesOf("do()").map { it to true })
            .plus(input.indexesOf("don't()").map { it to false })
            .plus(sequenceOf(input.length to false))
            .sortedBy { it.first }
            .windowed(2, 1, true)
            .mapNotNull { window ->
                val (index, isDo) = window.first()
                // find next index or length if last segment
                val indexNext = window.lastOrNull()?.first ?: input.length
                if (isDo) {
                    input.substring(index, indexNext)
                } else {
                    null
                }
            }
            .map { multiply(it) }
            .sum()
    }

    private fun String.indexesOf(sub: String): Sequence<Int> {
        return sequence {
            var index = indexOf(sub)
            while (index >= 0) {
                yield(index)
                index = indexOf(sub, index + 1)
            }
        }
    }
}

