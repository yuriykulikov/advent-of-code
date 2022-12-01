package aoc

import org.assertj.core.api.Assertions.assertThat
import org.junit.Test

class Day22CrabCombat {

    @Test
    fun silverTest() {
        assertThat(playGame(testInput).score()).isEqualTo(306)
    }

    @Test
    fun silver() {
        assertThat(playGame(taskInput).score()).isEqualTo(32272)
    }

    private fun playGame(input: String): List<Int> {
        val players = input.split("\n\n").map { player ->
            player.substringAfter(":").lines().mapNotNull { it.toIntOrNull() }
        }

        return generateSequence(players) { players ->
            val drawed = players.map { it.first() }.sortedDescending()
            players.map {
                it.run {
                    if (first() == drawed.first()) {
                        drop(1).plus(drawed)
                    } else {
                        drop(1)
                    }
                }
            }
        }
            .first { players -> players.any { it.isEmpty() } }
            .first { it.isNotEmpty() }
    }

    private fun List<Int>.score(): Int {
        return reversed().mapIndexed { index, card -> card * (index + 1) }
            .sum()
    }

    private val testInput = """
        Player 1:
        9
        2
        6
        3
        1

        Player 2:
        5
        8
        4
        7
        10
    """.trimIndent()

    private val taskInput = """
        Player 1:
        3
        42
        4
        25
        14
        36
        32
        18
        33
        10
        35
        50
        16
        31
        34
        46
        9
        6
        41
        7
        15
        45
        30
        27
        49

        Player 2:
        8
        11
        47
        21
        17
        39
        29
        43
        23
        28
        13
        22
        5
        20
        44
        38
        26
        37
        2
        24
        48
        12
        19
        1
        40
    """.trimIndent()
}

