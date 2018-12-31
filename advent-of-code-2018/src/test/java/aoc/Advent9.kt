package aoc

import extensions.kotlin.cycle
import io.vavr.collection.Map
import io.vavr.kotlin.hashMap
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test

class Advent9 {
    // 9 players; last marble is worth 25 points: high score is 32
    @Test
    fun `Marble Mania result is 32`() {
        assertThat(playMarbleMania(9, 25)).isEqualTo(32)
    }

    // 10 players; last marble is worth 1618 points: high score is 8317
    @Test
    fun `Marble Mania result is 8317`() {
        assertThat(playMarbleMania(10, 1618)).isEqualTo(8317)
    }

    // 13 players; last marble is worth 7999 points: high score is 146373

    @Test
    fun `Marble Mania result is 146373`() {
        assertThat(playMarbleMania(13, 7999)).isEqualTo(146373)
    }

    // 17 players; last marble is worth 1104 points: high score is 2764
    @Test
    fun `Marble Mania result is 2764`() {
        assertThat(playMarbleMania(17, 1104)).isEqualTo(2764)
    }

    // 21 players; last marble is worth 6111 points: high score is 54718
    @Test
    fun `Marble Mania result is 54718`() {
        assertThat(playMarbleMania(21, 6111)).isEqualTo(54718)
    }

    // 30 players; last marble is worth 5807 points: high score is 37305
    @Test
    fun `Marble Mania result is 37305`() {
        assertThat(playMarbleMania(30, 5807)).isEqualTo(37305)
    }

    // 400 players; last marble is worth 71864 points
    @Test
    fun `Star 1 - Marble Mania result is 437654`() {
        assertThat(playMarbleMania(400, 71864)).isEqualTo(437654)
    }

    // 400 players; last marble is worth 71864 points
    @Test
    fun `Star 2 - Marble Mania result is 437654`() {
        assertThat(playMarbleMania(400, 7186400)).isEqualTo(3689913905L)
    }


    // [-] (0)
    // [1]  0 (1)
    // [2]  0 (2) 1
    // [3]  0  2  1 (3)
    // [4]  0 (4) 2  1  3
    // [5]  0  4  2 (5) 1  3
    // [6]  0  4  2  5  1 (6) 3
    // [7]  0  4  2  5  1  6  3 (7)
    // [8]  0 (8) 4  2  5  1  6  3  7
    // [9]  0  8  4 (9) 2  5  1  6  3  7
    // [1]  0  8  4  9  2(10) 5  1  6  3  7
    // [2]  0  8  4  9  2 10  5(11) 1  6  3  7
    // [3]  0  8  4  9  2 10  5 11  1(12) 6  3  7
    // [4]  0  8  4  9  2 10  5 11  1 12  6(13) 3  7
    // [5]  0  8  4  9  2 10  5 11  1 12  6 13  3(14) 7
    // [6]  0  8  4  9  2 10  5 11  1 12  6 13  3 14  7(15)
    // [7]  0(16) 8  4  9  2 10  5 11  1 12  6 13  3 14  7 15
    // [8]  0 16  8(17) 4  9  2 10  5 11  1 12  6 13  3 14  7 15
    // [9]  0 16  8 17  4(18) 9  2 10  5 11  1 12  6 13  3 14  7 15
    // [1]  0 16  8 17  4 18  9(19) 2 10  5 11  1 12  6 13  3 14  7 15
    // [2]  0 16  8 17  4 18  9 19  2(20)10  5 11  1 12  6 13  3 14  7 15
    // [3]  0 16  8 17  4 18  9 19  2 20 10(21) 5 11  1 12  6 13  3 14  7 15
    // [4]  0 16  8 17  4 18  9 19  2 20 10 21  5(22)11  1 12  6 13  3 14  7 15
    // [5]  0 16  8 17  4 18(19) 2 20 10 21  5 22 11  1 12  6 13  3 14  7 15
    // [6]  0 16  8 17  4 18 19  2(24)20 10 21  5 22 11  1 12  6 13  3 14  7 15
    // [7]  0 16  8 17  4 18 19  2 24 20(25)10 21  5 22 11  1 12  6 13  3 14  7 15
    private fun playMarbleMania(players: Int, lastMarble: Int): Long {
        val playersSeq: Sequence<Int> = (1..players).asSequence().cycle()
        val marbles = (1..lastMarble).asSequence()

        val game = playersSeq.zip(marbles)
            .fold(Game()) { game, (player, marble) -> game.next(player, marble) }

        return game.scores.values().max().getOrElse(0L)
    }

    data class Node(val marble: Int) {
        lateinit var prev: Node
        lateinit var next: Node
    }

    /**
     * Stateful folder. Accepts new stuff in [next]
     */
    class Game {
        var scores: Map<Int, Long> = hashMap<Int, Long>()

        private val startingNode = Node(0).apply {
            next = this
            prev = this
        }

        private var currentNode = startingNode

        fun next(player: Int, marble: Int): Game {

            when {
                marble.rem(23) == 0 -> winningMarble(player, marble)
                else -> justNextMarble(marble)
            }

            if (marble <= 25) {
                val dump = generateSequence(startingNode) { prev -> if (prev.next == startingNode) null else prev.next }
                    .joinToString("") { " ${it.marble} " }
                    .replace(" ${currentNode.marble} ", " (${currentNode.marble}) ")

                println("[$player] $dump")
            }

            return this
        }

        private fun justNextMarble(marble: Int) {
            val left = currentNode.next
            val right = currentNode.next.next

            currentNode = Node(marble = marble).apply {
                prev = left
                next = right
                left.next = this
                right.prev = this
            }
        }

        private fun winningMarble(player: Int, marble: Int) {
            val nodeToRemove = currentNode.prev.prev.prev.prev.prev.prev.prev

            // update the scores
            scores = scores.getOrElse(player, 0L)
                .let { it + marble + nodeToRemove.marble }
                .let { scores.put(player, it) }

            // throw away the node and link it's next and prev nodes
            nodeToRemove.prev.next = nodeToRemove.next
            nodeToRemove.next.prev = nodeToRemove.prev

            // clockwise from removed node is the next current node
            currentNode = nodeToRemove.next
        }
    }
}