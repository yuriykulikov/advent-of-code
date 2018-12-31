package aoc

import org.assertj.core.api.Assertions.assertThat
import org.junit.Ignore
import org.junit.Test

class Advent14 {
    @Test
    fun `After 9 recipes, the scores of the next ten would be 5158916779`() {
        assertThat(scoreAfter(9)).isEqualTo("5158916779")
    }

    @Test
    fun `After 5 recipes, the scores of the next ten would be 0124515891`() {
        assertThat(scoreAfter(5)).isEqualTo("0124515891")
    }

    @Test
    fun `After 18 recipes, the scores of the next ten would be 9251071085`() {
        assertThat(scoreAfter(18)).isEqualTo("9251071085")
    }

    @Test
    fun `After 2018 recipes, the scores of the next ten would be 5941429882`() {
        assertThat(scoreAfter(2018)).isEqualTo("5941429882")
    }

    @Test
    fun `After 793061 recipes, the scores of the next ten would be 4138145721`() {
        assertThat(scoreAfter(793061)).isEqualTo("4138145721")
    }

    @Test
    fun `51589 first appears after 9 recipes`() {
        assertThat(recipiesToAchieve("51589")).isEqualTo(9)
    }

    @Test
    fun `01245 first appears after 5 recipes`() {
        assertThat(recipiesToAchieve("01245")).isEqualTo(5)
    }

    @Test
    fun `92510 first appears after 18 recipes`() {
        assertThat(recipiesToAchieve("92510")).isEqualTo(18)
    }

    @Test
    fun `59414 first appears after 2018 recipes`() {
        assertThat(recipiesToAchieve("59414")).isEqualTo(2018)
    }

    @Ignore("Takes 14 seconds")
    @Test
    fun `793061 first appears after 20276284 recipes`() {
        assertThat(recipiesToAchieve("793061")).isEqualTo(20276284)
    }


    private fun recipiesToAchieve(toMatch: String): Int {
        val board = Board()
        val elves: List<Elf> = listOf(Elf(board, 0), Elf(board, 1))

        fun findMatch(board: Board): Int? {
            if (board.tail < toMatch.length) return null

            // println("matching $toMatch against ${board.board.joinToString("") { it.toString() }}")

            val firstSequenceStartingPoint = board.tail - toMatch.lastIndex
            val secondSequenceStartingPoint = firstSequenceStartingPoint - 1


            val toMatchLast = board.board
                .subList(firstSequenceStartingPoint, board.board.size)
                .joinToString("") { it.toString() }

            val toMatchSecondToLast = board.board
                .subList(secondSequenceStartingPoint, board.board.size - 1)
                .joinToString("") { it.toString() }

            // println("toMatchLast $toMatchLast")
            // println("toMatchSecondToLast $toMatchSecondToLast")

            return when (toMatch) {
                toMatchLast -> firstSequenceStartingPoint
                toMatchSecondToLast -> secondSequenceStartingPoint
                else -> null
            }
        }

        return generateSequence(0) { it + 1 }.mapNotNull { iteration ->
            board.dump(elves.map { elf -> elf.currentIndex }, iteration)
            val currentSum = elves.sumBy { elf -> board[elf.currentIndex] }
            if (currentSum < 10) {
                board.append(currentSum)
            } else {
                board.append(currentSum.div(10))
                board.append(currentSum.rem(10))
            }

            elves.forEach { elf -> elf.stepForward() }

            assertThat(elves.map { elf -> elf.currentIndex }.distinct().size).isEqualTo(2)

            findMatch(board)
        }
            .first()
    }


    /*
 Only two recipes are on the board: the first recipe got a score of 3, the second, 7. Each of the two Elves has a
 current recipe: the first Elf starts with the first recipe, and the second Elf starts with the second recipe.

 To create new recipes, the two Elves combine their current recipes. This creates new recipes from the digits of
 the sum of the current recipes' scores. With the current recipes' scores of 3 and 7, their sum is 10, and so two
 new recipes would be created: the first with score 1 and the second with score 0. If the current recipes' scores
 were 2 and 3, the sum, 5, would only create one recipe (with a score of 5) with its single digit.

 The new recipes are added to the end of the scoreboard in the order they are created. So, after the first round,
 the scoreboard is 3, 7, 1, 0.

 After all new recipes are added to the scoreboard, each Elf picks a new current recipe. To do this, the Elf steps
 forward through the scoreboard a number of recipes equal to 1 plus the score of their current recipe. So, after the
 first round, the first Elf moves forward 1 + 3 = 4 times, while the second Elf moves forward 1 + 7 = 8 times. If
 they run out of recipes, they loop back around to the beginning. After the first round, both Elves happen to loop
 around until they land on the same recipe that they had in the beginning; in general, they will move to different
 recipes.
 */
    class Board {
        val dbg = false
        val board: MutableList<Int> = mutableListOf()
        var tail: Int = -1

        init {
            append(3)
            append(7)
        }

        operator fun get(index: Int): Int {
            return when {
                index > tail -> throw IllegalStateException("Index $index is not available, tail is $tail")
                else -> board[index]
            }
        }

        fun loopTo(index: Int): Int {
            return when {
                index > tail -> index.rem(tail + 1)
                else -> index
            }
        }

        fun append(recipe: Int) {
            tail++
            board.add(recipe)
        }

        fun dump(current: List<Int>, iteration: Int) {
            if (dbg) {
                println("$iteration: " + board
                    .mapIndexedNotNull { index, value ->
                        when {
                            index == current[0] -> "($value)"
                            index == current[1] -> "[$value]"
                            index <= tail -> "$value"
                            else -> null
                        }
                    }
                    .joinToString(""))
            }
        }
    }

    // 0: (3)[7]
    // 1: (3)[7] 1  0
    // 2:  3  7  1 [0](1) 0
    // 3:  3  7  1  0 [1] 0 (1)
    // 4:  (3) 7  1  0  1  0 [1] 2
    // 5:  3  7  1  0 (1) 0  1  2 [4]
    // 6:  3  7  1 [0] 1  0 (1) 2  4  5
    // 7:  3  7  1  0 [1] 0  1  2 (4) 5  1
    // 8:  3 (7) 1  0  1  0 [1] 2  4  5  1  5
    // 9:  3  7  1  0  1  0  1  2 [4](5) 1  5  8
    // 10: 3 (7) 1  0  1  0  1  2  4  5  1  5  8 [9]
    // 11: 3  7  1  0  1  0  1 [2] 4 (5) 1  5  8  9  1  6
    // 12: 3  7  1  0  1  0  1  2  4  5 [1] 5  8  9  1 (6) 7
    // 13: 3  7  1  0 (1) 0  1  2  4  5  1  5 [8] 9  1  6  7  7
    // 14: 3  7 [1] 0  1  0 (1) 2  4  5  1  5  8  9  1  6  7  7  9
    // 15: 3  7  1  0 [1] 0  1  2 (4) 5  1  5  8  9  1  6  7  7  9  2

    private fun scoreAfter(recipies: Int): String {
        val board = Board()
        val elves: List<Elf> = listOf(Elf(board, 0), Elf(board, 1))
        repeat(recipies + 10 - 3) {
            board.dump(elves.map { elf -> elf.currentIndex }, it)
            val currentSum = elves.sumBy { elf -> board[elf.currentIndex] }
            if (currentSum < 10) {
                board.append(currentSum)
            } else {
                board.append(currentSum.div(10))
                board.append(currentSum.rem(10))
            }
            elves.forEach { elf -> elf.stepForward() }

            assertThat(elves.map { elf -> elf.currentIndex }.distinct().size).isEqualTo(2)
        }

        return (recipies..(recipies + 9)).joinToString("") { board[it].toString() }
    }

    class Elf(private val board: Board, var currentIndex: Int) {
        fun stepForward() {
            val score = board[currentIndex]
            currentIndex = board.loopTo(currentIndex + score + 1)
        }
    }
}