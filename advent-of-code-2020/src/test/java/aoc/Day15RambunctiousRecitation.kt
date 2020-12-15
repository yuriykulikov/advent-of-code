package aoc

import org.assertj.core.api.Assertions.assertThat
import org.junit.Test
import java.util.*

class Day15RambunctiousRecitation {
    @Test
    fun silverTest10() {
        assertThat(play(listOf(0, 3, 6), 10)).isEqualTo(0)
    }

    @Test
    fun silverTest() {
        assertThat(play(listOf(0, 3, 6), 2020)).isEqualTo(436)
    }

    @Test
    fun silver() {
        assertThat(play(listOf(2, 1, 10, 11, 0, 6), 2020)).isEqualTo(232)
    }

    @Test
    fun goldTest() {
        assertThat(play(listOf(0, 3, 6), 30000000)).isEqualTo(175594)
    }

    @Test
    fun gold() {
        assertThat(play(listOf(2, 1, 10, 11, 0, 6), 30000000)).isEqualTo(18929178)
    }

    private fun play(starting: List<Int>, end: Int): Int {
        val spokenTurns = HashMap<Int, Int>().apply {
            putAll(starting.mapIndexed { index, number -> number to index + 1 })
        }

        var lastNumber = starting.last()

        repeat(end - starting.size) { i ->
            val lastTurn = i + starting.size
            val speak = spokenTurns[lastNumber]?.let { lastTurn - it } ?: 0
            spokenTurns[lastNumber] = lastTurn
            lastNumber = speak
        }

        return lastNumber
    }
}