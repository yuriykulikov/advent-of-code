package year2025

import io.kotest.matchers.collections.shouldContainExactly
import io.kotest.matchers.shouldBe
import loadResource
import org.junit.jupiter.api.Test
import kotlin.math.max
import kotlinx.coroutines.runBlocking

class Day3Test {
    private val example = """
987654321111111
811111111111119
234234234234278
818181911112111
    """.trimIndent()

    @Test
    fun `Silver example - find max joltages`() {
        val joltages = example.lines().map { joltage(it) }
        joltages.shouldContainExactly(
                98, 89, 78, 92
            )
        joltages.sum().shouldBe(357)
    }

    @Test
    fun `Silver - find max joltages`() {
        val joltages = loadResource("Day3").lines().map { joltage(it) }
        joltages.sum().shouldBe(17493)
    }


    @Test
    fun `Gold example - find max joltages with overdrive`() {
        overjolt("987654321111111") shouldBe 987654321111L
        overjolt("811111111111119") shouldBe 811111111119L
        overjolt("234234234234278") shouldBe 434234234278L
        overjolt("818181911112111") shouldBe 888911112111L


        listOf(
            overjolt("987654321111111"),
            overjolt("811111111111119"),
            overjolt("234234234234278"),
            overjolt("818181911112111"),
        ).sum() shouldBe 3121910778619L
    }

    @Test
    fun `Gold - find max joltages with overdrive`() {
        loadResource("Day3").lines().sumOf { overjolt(it) }.shouldBe(173685428989126L)

    }

    private fun overjolt(battery: String): Long = runBlocking {
        joltageOverride(battery, 12)
    }

    private fun joltage(battery: String): Int {
        // straightforward maxOf
        return (0..battery.lastIndex - 1).maxOf { firstIndex ->
            val firstInt = battery[firstIndex].digitToInt()
            val tail = battery.drop(firstIndex + 1)
            val secondInt = tail.maxOf { it.digitToInt() }
            firstInt * 10 + secondInt
        }
    }

    private val cache = mutableMapOf<Pair<String, Int>, Long>()
    private fun joltageOverride(battery: String, howMany: Int): Long {
        if (howMany == 0) return 0
        if (battery.length < howMany) return 0
        return cache.getOrPut(battery to howMany) {
            // first digit can be on or off, which generates 2 branches
            // and then we pick the max
            val firstInt = battery.first().digitToInt()
            val tail = battery.drop(1)
            val hyp1 = 10.pow(howMany - 1) * firstInt + joltageOverride(tail, howMany - 1)
            val hyp2 = joltageOverride(tail, howMany)
            max(hyp1, hyp2)
        }
    }
}

