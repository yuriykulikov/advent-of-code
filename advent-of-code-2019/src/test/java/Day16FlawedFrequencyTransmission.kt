import extensions.kotlin.cycle
import kotlinx.collections.immutable.persistentListOf
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test
import kotlin.math.absoluteValue
import kotlin.properties.Delegates
import kotlin.system.measureTimeMillis

/**
 * This was a hard one. Spoiler for the part 2: see [prepareSignal] and [reversedFft]
 */
class Day16FlawedFrequencyTransmission {
    @Test
    fun verifyTestInputs() {
        val map = fft("12345678")
            .drop(1)
            .take(4)
            .toList()
            .map { it.joinToString("") }

        assertThat(map).containsSequence(
            "48226158",
            "34040438",
            "03415518",
            "01029498"
        )

        assertThat(
            fft("80871224585914546619083218645595").take(101).last().take(8).joinToString("")
        ).isEqualTo("24176176")
        assertThat(
            fft("19617804207202209144916044189917").take(101).last().take(8).joinToString("")
        ).isEqualTo("73745418")
        assertThat(
            fft("69317163492948606335995924319873").take(101).last().take(8).joinToString("")
        ).isEqualTo("52432133")
    }

    @Test
    fun silver() {
        assertThat(fft(input).take(101).last().take(8).joinToString("")).isEqualTo("28430146")
    }

    private fun cycleBase(position: Int): Sequence<Int> {
        return IntArray(position + 1) { 0 }.toList()
            .plus(IntArray(position + 1) { 1 }.toList())
            .plus(IntArray(position + 1) { 0 }.toList())
            .plus(IntArray(position + 1) { -1 }.toList())
            .asSequence()
            .cycle()
            .drop(1)
    }

    /** This is a brute force sequence which worked for part one */
    private fun fft(initialSignal: String): Sequence<List<Int>> {
        val initial: List<Int> = initialSignal.split("").filter { it.isNotEmpty() }.map { it.toInt() }
        return generateSequence(initial) { signal ->
            signal.mapIndexed { index, _ ->
                signal.asSequence()
                    .zip(cycleBase(index)) { l, r -> l * r }
                    .sum()
                    .absoluteValue
                    .rem(10)
            }
        }
    }


    @Test
    fun goldAssumptions() {

        val index = input.take(7).toInt()
        val length = input.length * 10000L

        println("index:  $index")
        println("length: $length")
        println("tail: ${length - index}")

    }

    @Test
    fun goldTestAssumptions() {
        val index = "03036732577212944063491565474664".take(7).toInt()
        val length = "03036732577212944063491565474664".length * 10000

        println("index:  $index")
        println("length: $length")
        println("tail: ${length - index}")
    }

    @Test
    fun testPrepareSignal() {
        assertThat("1234".prepareSignal(2, 8).joinToString("")).isEqualTo("43214321432143214321")
    }

    @Test
    fun testGold() {
        reversedFft("03036732577212944063491565474664".prepareSignal())
            .reversed()
            .take(8)
            .joinToString("")
            .let { assertThat(it).isEqualTo("84462026") }
    }

    @Test
    fun testGold2() {
        reversedFft("02935109699940807407585447034323".prepareSignal())
            .reversed()
            .take(8)
            .joinToString("")
            .let { assertThat(it).isEqualTo("78725270") }
    }

    @Test
    fun testGold3() {
        reversedFft("03081770884921959731165446850517".prepareSignal())
            .reversed()
            .take(8)
            .joinToString("")
            .let { assertThat(it).isEqualTo("53553731") }
    }

    @Test
    fun gold() {
        val reversedSignal: List<Int> = input.prepareSignal()

        val reversedFft = bench("reversedFft") { reversedFft(reversedSignal) }

        reversedFft
            .reversed()
            .take(8)
            .joinToString("")
            .let { assertThat(it).isEqualTo("12064286") }
    }

    /** Read the [prefix], repeat the signal [repetitions] times and then cut the head since we only need the tail. */
    private fun String.prepareSignal(prefix: Int = 7, repetitions: Int = 10000): List<Int> {
        val parsed = split("").filter { it.isNotEmpty() }.map { it.toInt() }

        val skipAmount = take(prefix).toInt()

        return parsed
            .reversed()
            .asSequence()
            .cycle()
            .take(parsed.size * repetitions - skipAmount)
            .toList()
    }

    private fun reversedFft(reversedSignal: List<Int>): List<Int> {
        return generateSequence(reversedSignal) { signal ->
            // reversedFftIteration(signal)
            reversedFftIterationM(signal)
        }
            .take(101)
            .last()
    }

    @Test
    fun testReversedFftIteration() {
        val parsed = "466474560144310".split("").filter { it.isNotEmpty() }.map { it.toInt() }

        reversedFftIteration(parsed)
            .joinToString("")
            .let { assertThat(it) }
            .startsWith("406071")
    }

    /**
     * So...
     * Numerology goes from tail to head!
     * Consider a reversed sequence: 466474560144310
     * And this next one below it:   406071622371455
     * Next iteration can be produced by folding 466474560144310 like this:
     * Seed is 4 (signal.first())
     *
     * 4      6      6        4       7       4
     *    ┌-  4  ┌-  0   ┌-   6   ┌-  0   ┌-  7
     * 4 -┘   0 -┘   6  -┘    0  -┘   7  -┘   1
     *
     * And it goes around the sausage like that for a while, and we end up with the same result as using [fft],
     * except it works only for part of the tail
     */
    fun reversedFftIteration(signal: List<Int>): List<Int> {
        return signal.drop(1)
            .fold(persistentListOf(signal.first())) { fold, next ->
                fold.add((fold.last() + next).absoluteValue.rem(10))
            }
    }

    /**
     * So...
     * Numerology goes from tail to head!
     * Consider a reversed sequence: 466474560144310
     * And this next one below it:   406071622371455
     * Next iteration can be produced by folding 466474560144310 like this:
     * Seed is 4 (signal.first())
     *
     * 4      6      6        4       7       4
     *    ┌-  4  ┌-  0   ┌-   6   ┌-  0   ┌-  7
     * 4 -┘   0 -┘   6  -┘    0  -┘   7  -┘   1
     *
     * And it goes around the sausage like that for a while, and we end up with the same result as using [fft],
     * except it works only for part of the tail
     *
     * This is twice as fast as [reversedFftIteration]
     */
    fun reversedFftIterationM(signal: List<Int>): List<Int> {
        val acc: MutableList<Int> = arrayListOf(signal.first()).apply {
            // this reduces the costs by 50%
            ensureCapacity(signal.size)
        }

        signal.drop(1).forEach { next ->
            acc.add((acc.last() + next).absoluteValue.rem(10))
        }
        return acc
    }

    private inline fun <T : Any> bench(what: String, block: () -> T): T {
        var ret: T by Delegates.notNull()
        measureTimeMillis {
            ret = block()
        }.also {
            println("$what took ${it}ms")
        }
        return ret
    }

    private val input =
        "59704176224151213770484189932636989396016853707543672704688031159981571127975101449262562108536062222616286393177775420275833561490214618092338108958319534766917790598728831388012618201701341130599267905059417956666371111749252733037090364984971914108277005170417001289652084308389839318318592713462923155468396822247189750655575623017333088246364350280299985979331660143758996484413769438651303748536351772868104792161361952505811489060546839032499706132682563962136170941039904873411038529684473891392104152677551989278815089949043159200373061921992851799948057507078358356630228490883482290389217471790233756775862302710944760078623023456856105493"
}
