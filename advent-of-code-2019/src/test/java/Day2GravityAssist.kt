import kotlinx.collections.immutable.toPersistentList
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test

class Day2GravityAssist {
    val testValues = listOf(
        listOf(1, 0, 0, 0, 99) to listOf(2, 0, 0, 0, 99),
        listOf(2, 3, 0, 3, 99) to listOf(2, 3, 0, 6, 99),
        listOf(2, 4, 4, 5, 99, 0) to listOf(2, 4, 4, 5, 99, 9801),
        listOf(1, 1, 1, 4, 99, 5, 6, 0, 99) to listOf(30, 1, 1, 4, 2, 5, 6, 0, 99),
        listOf(1, 9, 10, 3, 2, 3, 11, 0, 99, 30, 40, 50) to listOf(3500, 9, 10, 70, 2, 3, 11, 0, 99, 30, 40, 50)
    )

    fun execute(program: List<Int>): List<Int> {
        return intCodeComputer(program)
            .runToHalt()
            .program
            .asList()
            .map { it.toInt() }
    }

    @Test
    fun verifyTestValues() {
        testValues.forEach { (initial, expected) ->
            assertThat(execute(initial)).isEqualTo(expected)
        }
    }

    @Test
    fun silverStar() {
        assertThat(
            execute(
                input.toPersistentList()
                    .set(1, 12)
                    .set(2, 2)
            )[0]
        ).isEqualTo(2692315)
    }

    @Test
    fun goldStar() {
        val (noun, verb) = (0..99).flatMap { noun -> (0..99).map { verb -> noun to verb } }
            .asSequence()
            .first { (noun, verb) ->
                execute(
                    input.toPersistentList()
                        .set(1, noun)
                        .set(2, verb)
                )
                    .get(0) == 19690720
            }

        assertThat(100 * noun + verb).isEqualTo(9507)
    }

    val input = listOf(
        1,
        0,
        0,
        3,
        1,
        1,
        2,
        3,
        1,
        3,
        4,
        3,
        1,
        5,
        0,
        3,
        2,
        1,
        6,
        19,
        1,
        19,
        5,
        23,
        2,
        13,
        23,
        27,
        1,
        10,
        27,
        31,
        2,
        6,
        31,
        35,
        1,
        9,
        35,
        39,
        2,
        10,
        39,
        43,
        1,
        43,
        9,
        47,
        1,
        47,
        9,
        51,
        2,
        10,
        51,
        55,
        1,
        55,
        9,
        59,
        1,
        59,
        5,
        63,
        1,
        63,
        6,
        67,
        2,
        6,
        67,
        71,
        2,
        10,
        71,
        75,
        1,
        75,
        5,
        79,
        1,
        9,
        79,
        83,
        2,
        83,
        10,
        87,
        1,
        87,
        6,
        91,
        1,
        13,
        91,
        95,
        2,
        10,
        95,
        99,
        1,
        99,
        6,
        103,
        2,
        13,
        103,
        107,
        1,
        107,
        2,
        111,
        1,
        111,
        9,
        0,
        99,
        2,
        14,
        0,
        0
    )
}