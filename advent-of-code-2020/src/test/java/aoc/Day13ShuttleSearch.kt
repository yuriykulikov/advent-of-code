package aoc

import org.assertj.core.api.Assertions.assertThat
import org.junit.Test
import kotlin.math.abs

class Day13ShuttleSearch {
    @Test
    fun silverTest() {
        assertThat(calculateWaitTime(testInput)).isEqualTo(295)
    }

    @Test
    fun silver() {
        assertThat(calculateWaitTime(taskInput)).isEqualTo(259)
    }

    private fun calculateWaitTime(input: String): Int {
        val now = input.lines().first().toInt()

        val busses = input.lines()
            .last()
            .split(',')
            .filterNot { it == "x" }
            .map { it.toInt() }

        // What is the ID of the earliest bus you can take to the airport
        // multiplied by the number of minutes
        // you'll need to wait for that bus?
        return busses.map { id -> id to abs(now - ((now.div(id) + 1) * id)) }
            .minByOrNull { (id, wait) -> wait }!!
            .let { it.first * it.second }
    }

    //  inputs are primes
    @Test
    fun checkUsingMaxStep() {
        val busses = testInput.lines()
            .last()
            .split(',')
            .map { it.toIntOrNull() ?: 0 }

        // first bus is an exact match
        // 1x, 2x, 3x

        // second is off by one, so... timestamps are:
        // 1y + 1, 2y + 1, 3y + 1
        val (maxPer, index) = busses.mapIndexed { index, period -> period to index }
            .maxByOrNull { (period, index) -> period }!!

        val periods: List<Pair<Int, Int>> = busses.mapIndexedNotNull { index, period ->
            if (period == 0) null else period to index
        }

        val first = generateSequence(0L) { it + 1 }
            // this can be optimized, period can be increased every time next bus is added
            // but i don't really want to do that :)
            .map { iter -> (iter * maxPer - index) }
            .filter { ts ->
                periods.all { (period, index) ->
                    (ts + index).rem(period.toLong()) == 0L
                }
            }
            .first { stamp -> isMagicTimestamp(stamp, busses) }

        assertThat(first).isEqualTo(1068781L)
    }

    @Test
    fun gold() {
        val busses = taskInput.lines()
            .last()
            .split(',')
            .map { it.toIntOrNull() ?: 0 }

        val periods: List<Pair<Int, Int>> = busses.mapIndexedNotNull { index, period ->
            if (period == 0) null else period to index
        }

        periods.forEach { println(it) }
    }

    @Test
    fun generateEquationsForWolframAlpha() {
        val letters = ('a'..'z').minus('e').iterator()

        val equations = taskInput.lines()
            .last()
            .split(',')
            .mapIndexedNotNull { index, period ->
                if (period == "x") null else "$period${letters.next()}-$index=t"
            }
            .joinToString()

        assertThat(equations).isEqualTo("19a-0=t, 41b-9=t, 523c-19=t, 17d-36=t, 13f-37=t, 29g-48=t, 853h-50=t, 37i-56=t, 23j-73=t")
        // we need the first integer solution for t
        // 210612924879242 well done wolfram
    }

    private fun isMagicTimestamp(timestamp: Long, busses: List<Int>): Boolean {
        return busses.mapIndexedNotNull { index, period ->
            if (period == 0) {
                null
            } else {
                (timestamp + index).rem(period)
            }
        }.all { it == 0L }
    }

    private val testInput = """
        939
        7,13,x,x,59,x,31,19
    """.trimIndent()

    private val taskInput = """
        1000510
        19,x,x,x,x,x,x,x,x,41,x,x,x,x,x,x,x,x,x,523,x,x,x,x,x,x,x,x,x,x,x,x,x,x,x,x,17,13,x,x,x,x,x,x,x,x,x,x,29,x,853,x,x,x,x,x,37,x,x,x,x,x,x,x,x,x,x,x,x,x,x,x,x,23
    """.trimIndent()
}