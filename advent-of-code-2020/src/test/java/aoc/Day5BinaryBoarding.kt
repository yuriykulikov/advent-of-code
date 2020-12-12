package aoc

import org.assertj.core.api.KotlinAssertions.assertThat
import org.junit.Test

/**
 * From https://adventofcode.com/2020/day/5
 *
 * --- Day 5: Binary Boarding ---
 *
 * The first 7 characters will either be F or B; these specify exactly one of the 128 rows on the plane (numbered 0 through 127).
 * Each letter tells you which half of a region the given seat is in.
 * Start with the whole list of rows;
 * the first letter indicates whether the seat is in the front (0 through 63) or the back (64 through 127).
 * The next letter indicates which half of that region the seat is in, and so on until you're left with exactly one row.
 *
 */
class Day5BinaryBoarding {
    data class Ticket(
        val code: String
    ) {
        val row: Int = code.take(7)
            .replace('F', '0')
            .replace('B', '1')
            .toInt(2)

        val column: Int = code.drop(7)
            .replace('L', '0')
            .replace('R', '1')
            .toInt(2)

        val seatId: Int = row * 8 + column
    }

    /**
     * For example, consider just the first seven characters of FBFBBFFRLR:
     *
     * Start by considering the whole range, rows 0 through 127.
     * F means to take the lower half, keeping rows 0 through 63.
     * B means to take the upper half, keeping rows 32 through 63.
     * F means to take the lower half, keeping rows 32 through 47.
     * B means to take the upper half, keeping rows 40 through 47.
     * B keeps rows 44 through 47.
     * F keeps rows 44 through 45.
     * The final F keeps the lower of the two, row 44.
     * The last three characters will be either L or R; these specify exactly one of the 8 columns of seats on the plane (numbered 0 through 7). The same process as above proceeds again, this time with only three steps. L means to keep the lower half, while R means to keep the upper half.
     *
     * For example, consider just the last 3 characters of FBFBBFFRLR:
     *
     * Start by considering the whole range, columns 0 through 7.
     * R means to take the upper half, keeping columns 4 through 7.
     * L means to take the lower half, keeping columns 4 through 5.
     * The final R keeps the upper of the two, column 5.
     * So, decoding FBFBBFFRLR reveals that it is the seat at row 44, column 5.
     *
     * Every seat also has a unique seat ID: multiply the row by 8, then add the column. In this example, the seat has ID 44 * 8 + 5 = 357.
     */
    @Test
    fun silverExample() {
        assertThat(Ticket("FBFBBFFRLR").row).isEqualTo(44)
        assertThat(Ticket("FBFBBFFRLR").column).isEqualTo(5)
        assertThat(Ticket("FBFBBFFRLR").seatId).isEqualTo(357)
    }

    @Test
    fun silverParsingTest() {
        // BFFFBBFRRR: row 70, column 7, seat ID 567.
        assertThat(Ticket("BFFFBBFRRR").row).isEqualTo(70)
        assertThat(Ticket("BFFFBBFRRR").column).isEqualTo(7)
        assertThat(Ticket("BFFFBBFRRR").seatId).isEqualTo(567)

        // FFFBBBFRRR: row 14, column 7, seat ID 119.
        assertThat(Ticket("FFFBBBFRRR").row).isEqualTo(14)
        assertThat(Ticket("FFFBBBFRRR").column).isEqualTo(7)
        assertThat(Ticket("FFFBBBFRRR").seatId).isEqualTo(119)

        // BBFFBBFRLL: row 102, column 4, seat ID 820.
        assertThat(Ticket("BBFFBBFRLL").row).isEqualTo(102)
        assertThat(Ticket("BBFFBBFRLL").column).isEqualTo(4)
        assertThat(Ticket("BBFFBBFRLL").seatId).isEqualTo(820)
    }

    /**
     * As a sanity check, look through your list of boarding passes. What is the highest seat ID on a boarding pass?
     */
    @Test
    fun silver() {
        val maxId = input.lines()
            .map { Ticket(it).seatId }
            .max()

        assertThat(maxId).isEqualTo(894)
    }

    @Test
    fun gold() {
        val takenSeats = input.lines()
            .map { Ticket(it) }
            .map { ticket -> Point(x = ticket.row, y = ticket.column) }
            .toSet()

        val allSeats = (0..127).flatMap { x ->
            (0..7).map { y -> Point(x, y) }
        }

        val mySeat = allSeats.filter { point -> point !in takenSeats }
            .first { point ->
                listOf(
                    point.up(),
                    point.down(),
                    point.left(),
                    point.right()
                ).all { it in takenSeats }
            }

        assertThat(mySeat.x * 8 + mySeat.y).isEqualTo(579)
    }

    private val input = """
        BFBFBFFRRL
        FFFFBBBLLL
        FFFBBFBRRR
        FFFFBBBRLR
        FFBFFBBRRL
        BFBBBBBRRR
        BBFFFFFRLR
        FFFFBBFRLR
        BBFFFBBLLL
        FBBBBFBLRL
        FFBBFFBRLR
        BBFFBBFRLL
        BBFFFFFLRL
        BBFFFFBRLR
        BFFFFBFLLL
        FFFBFFFRRL
        FBBBBBBLLR
        FFBFBBFRLL
        FBBFFBFLRL
        BFBFFBBRLR
        BFFBFBBLRR
        FFFBFFBRRL
        FFBFFBFRLL
        FBBBBBFLRL
        FFBBFFFRRL
        FFBFBFBLRR
        BFFFFFBRRL
        FFBFBBBRLR
        FFFBFFFLRR
        BBFFBFFLRR
        BFFFBBBRLL
        FFBBFFBLRL
        BFBBFBFRRR
        BFFBBBBLRL
        FFBFBFBRLL
        FBFFBBBRRR
        FBFBBBFLLL
        FBBFFBFRLR
        FBBFBBFLRR
        BFFBFFBLLR
        FFFBBBFLLL
        FBBFBBFLLR
        BFBFFFBRRL
        FFFFBFBRLL
        BBFFBFFRRR
        BFBBFBFLRL
        FFBBFFBRRL
        FFFBBBFLRR
        BFFBFFBLLL
        BFFBBFBRLL
        FBFBBFBRRL
        FFFFFFBRLR
        BBFBFFFRRR
        BBFFBBFLLL
        BFFBFFFLLR
        BFBFFBFLRL
        FFBBFFFLLR
        BFFFFBBRRL
        FFBBFFBRRR
        FFFFBFFLLR
        BFBBFFFLRR
        BBFFFBBLRL
        BFFFBBBLLR
        FFFBBFFRLR
        FFBBBBBRLL
        BBFFFBFRLR
        BBFBFFBRRR
        FBBBFFFRLL
        FBBBBBBRLR
        BFBBFFBRRR
        FBFBFFBRRL
        FFFBFBBRRL
        FFBFFFFLLL
        FBFFBFBLRR
        BFFBBFFLLR
        FFBBBFBRLL
        BFFBFFBRRR
        FBBFFBFRRR
        FBFFBBFRLR
        FBBBFFBLRL
        BFBBBFBLRL
        FFFFBFFRRR
        FBBFFFFLLR
        FFBFBBFRRR
        FBFBBBFLRL
        BFFFFFFRLL
        FFFFFBFRRR
        BFBFFBBRLL
        FFBFFFFRLL
        FFBFFBFLLL
        FFBFFBBRLL
        BFBFFBFLLL
        BFFBFBBLRL
        BFFFBFFRLL
        BFFBFBBLLL
        BFFFFFFLLR
        FBBFBBFRRR
        FFBBBBBRLR
        BFFBBBBRRR
        FFBBFBFRRL
        BBFBFBFRRR
        FBFFBFFLRR
        FBFFBFBLLL
        FFFBFBBLRR
        BBFBFFFLLL
        FFFBBFBLRR
        FFBBFFBLLR
        BFBBFFFRLL
        BFBBFBFLLR
        FBFFFFBLLL
        BFBBBBFRRR
        BBFFFBFRLL
        FBBFBFBLRL
        FBBBFFFRRR
        BBFBBBBLRL
        BBFFBFBRRL
        BFFFBBBRRL
        FBFBBBFRLL
        BFBBBFBLLL
        BFFBFFFLRL
        FFFBBBBRRL
        BBFFBFBLRL
        FBBFFFFLLL
        FBFFBBBLRL
        BBFBFBBRLL
        FBBBFBFLRL
        FBFFBFBLRL
        BFBBFFBRRL
        FBBFFFFLRR
        FFBBBBFRRL
        FFBFBFBLLR
        FBFFBFFRLR
        FFFBBBFRRR
        BFFFBFBLLR
        FBBBFBFLLL
        FBBBBBFRLL
        FFBFFFBRLR
        FBFFFBBRRL
        BBFFBBBLRR
        FBFBFFFLRR
        BFFFFFFLRL
        BBFBFBBLLL
        FBFBBFFLLL
        FBFFFBBLLL
        BFFBBFBLLL
        BBFFBBFLRR
        BBFFFFFRLL
        FFBFFFBLRR
        FFBFFFFRRR
        FBFFFBFLLL
        BFFFBFFRRR
        FBBFFFBLRR
        FBBBFBBRLR
        FFFFFFBRLL
        BFBFFBFLLR
        BFFFBBFLRR
        FBFFBBBLLR
        BBFFFFFRRR
        FBFBFBBLRL
        FBBFBFFLLL
        BFFBFBBRLL
        BFFBBBFLRR
        BFBBFBFRLR
        FBBFBFBRRL
        FFFFFBBLLR
        FFBFFBBRRR
        BFFBFBFLLL
        BFBBBBBLLL
        BBFBBBFRLR
        FFBFFBBLLL
        BBFFFFBLLR
        BBFFFFBLLL
        FFBFBFFLLL
        BFBFBBBRRR
        BBFBBBFLRL
        BFBBFFFRRR
        FFBFBBFLRL
        BBFBBBBLLR
        FBFFBFFLLR
        FBBFFFFLRL
        FFBBFBBLRR
        BBFFFFFRRL
        BFBFBFBLRR
        FFFBBFFLLR
        FFFFFBFRLR
        BFBFBBFRRL
        FFFFFBBRLR
        FFFFBFBLLR
        FFFFBBFRRR
        FFBFBFBLLL
        BBFBFBFLLR
        BFBFBFFLLL
        BFFBBBFRLL
        FFBBBBFRLR
        BFBBFFFRRL
        FFFFFFFRRL
        FFFBFBBRLL
        FBFFFFFRLR
        BBFFBBFRRR
        FBBFFBBLLL
        BFFFBBFRLR
        BFBBFFFLRL
        BFFBBFFLLL
        BFFFBFFLRL
        FBFBBFFRRR
        FFFBBBFRRL
        FFFFBBFLLL
        FBBFBBBRLL
        BFBBFBFRLL
        FBBBBFBRRL
        FFBFFFBLRL
        BFBBFBBRRL
        BFBBFFFLLR
        FBBBBFBRLR
        BBFBFFFRLR
        FBBFBFFLRL
        FFBFBFFRRR
        FFBFBFBLRL
        BBFBFBBLRR
        BBFBBFBRLR
        BFBBBFBLRR
        FFBBBFBLRR
        BBFFFBFLRR
        FFFBBFFRRL
        BBFFBBBRLR
        FFFBFBFLLL
        BFBFBBBLRL
        FBFBFFBRLL
        FBFFBBBRRL
        FFFFBBFLRR
        FBFFBBFLLR
        FFBFFBFLLR
        BFBFFBBRRR
        BFBBFFBLLR
        BBFBBBBRRL
        FFBFBFFRLL
        FBBFFFBLLR
        FFBBBBFLLR
        FBFBFBFLRL
        BFFBBFBLRL
        BFBFFBFRLR
        FFFFFFFRRR
        BFBBBFFRRR
        FFBBFFFRLR
        FBFBBFBLRR
        BFBBBBFRRL
        FFFFBBFRLL
        FBBBBFFRLL
        FBFFFFFRRL
        FBFFBFFRRL
        BBFBBFBLLL
        BFFFBFBRLR
        FBFBFBBRRL
        FFFBFFBRLR
        BBFFBFFLRL
        FFBFFBFLRR
        FFBBBBFRLL
        BFBBFBFLRR
        FFBFFFBLLL
        BBFBBFFLLL
        FFFBFBBLRL
        BFFFBFFRLR
        FFFBFFFRRR
        FBBFFBBRRL
        BFFBFFBRLL
        BFFBBBFLLL
        FBFBBBBRLR
        FBFFBFBRLL
        FBFBBBFLLR
        FFFBFFFLLL
        FFFBFFBLLR
        FBBFFBBLLR
        FBBFBBBLLL
        BFBBBFFLRR
        BFBFFBBLLL
        FFBFBFBRRL
        FFBFBFFLLR
        FBFBFFFRRL
        FFBBFBBRLL
        BBFFFBBLRR
        FBFBFBBLRR
        BFFFBFBLRR
        BFBFFFFRRL
        BFFFBBBLRL
        FFFFFBBRRR
        BBFBBFBRRR
        FFBFBFBRRR
        FFFFBFFLLL
        BFBFFBBLLR
        FBBBFBBRRL
        FBBBFFFRLR
        FFFBBFBLRL
        FFBBBFFRLL
        FFFFFBBLRL
        BFBFFBFRRL
        FBBFFBFLRR
        BFFBFFFLLL
        FBBFBFBLLL
        BFBBFFBLRL
        FFBBBBBLLL
        FBFBFBFRLR
        BBFBBFBLRR
        BFBBBBBLRR
        FBFBBFFLLR
        FFBFBBFRRL
        FFFBBFBRRL
        BFFFFFFLLL
        BBFBBBFLLR
        FBFFBBFLLL
        FBFBBFBLLR
        BFBBBFBRLR
        BFBFFFFRLR
        FBBFBFFRLR
        BBFFFFFLRR
        BBFBFBBLRL
        BBFBFBBRRR
        FFBBFBFLLL
        FBFBBFFLRL
        BFBFBBFLLR
        BBFBBBBLLL
        FFBBFFBRLL
        FFFFBBFLRL
        FFBBFBFLLR
        BFBBBFBRLL
        FFFBFFBRRR
        FBBBBFFLRL
        FBFFBBBLRR
        FBBBFBBLRR
        FBBBFFBRRL
        BFFBFBFRRR
        BBFBFBBRRL
        FBBFBBBLLR
        FBBFBFBLRR
        FBFFFFBLRR
        FBFBFBBRLL
        FBFFFBFLRR
        BBFBBBFLRR
        BFBFBBBLLL
        FFBFFBFRRR
        BBFFFBBRLR
        BFFBBFBLLR
        FBFBBFBRLL
        FFBFBBBRRR
        FBFFFFBRLL
        FBBBFFFLRL
        BFBFBFBRLR
        FBBBFBBLRL
        FBFFBBBLLL
        FFFBBBFLLR
        BFFFBBFRLL
        FBFFFBFRLR
        BFFFBBFRRL
        BFFFFBFRRL
        BBFFBBBLLR
        FBBFFFFRLR
        FFBFBFFRLR
        BFBFBBBLRR
        FFFFBBBLRR
        FFBFFFBRLL
        FFFFBBBLRL
        FFBBBBFLRL
        BBFFFFBRRL
        FFFFFBFLRR
        BFFFBFBRRR
        BBFBBFFLRR
        FFBFBBFLRR
        FFFFFBBRRL
        BFBFFFFLRL
        BFFBBFBRLR
        FBFFFFBRRL
        BFBBFBBLLL
        BFBBBFBRRL
        BFBBBBBRLL
        FFFBBBBLRR
        FBBFBBFLLL
        BFFFFBBLLR
        FFBFFFBLLR
        FBBFFBFRRL
        FFBBBFFLRR
        BFFFBBBRRR
        FFFFBBBRRR
        FFFBFFBLLL
        BFFFFBBRLL
        FBBBFBFRRL
        FFBBBBFLLL
        BBFBFBBRLR
        FBFFBBFLRL
        FBBFBFFRRR
        BBFFFFBLRR
        FBBBFBBLLR
        FFFBFFBLRL
        BFFBFBFRLL
        FBFFFFBRLR
        FFFFBFFRLR
        BFBFBFBRRL
        FBBFBBFRRL
        BBFFBFBLLR
        FFBFFFFLRR
        BFFFFFBRLL
        FFFFBBFRRL
        FBFFFBFLRL
        FFBBFBBLRL
        BFFBBFBRRR
        BBFBBBFLLL
        FBBFBBBRRR
        BFFBFBBRLR
        FBBBFFBRLL
        FBBFBFFLLR
        BFFBFBBLLR
        FFBFFFFLRL
        BFFBFBFLLR
        BBFBBBBRLR
        FFBBBFFLRL
        FBBFFFBRLR
        BFFFFFBRLR
        FBFFFFFLLR
        FBFFFBFRRR
        FFBBFBFRLL
        FBFBFFFRLR
        BFBFBBFRRR
        BBFFFBBRRR
        FBBFBFBLLR
        BFFBFBFLRL
        FFFBFFFRLR
        FBBBFBFLLR
        FFBBBFBRRR
        BFBFFBBLRL
        FBBBFFBLRR
        BFFFFFFRRR
        FFBBFFFLRR
        BFBFFFBRLR
        BFBFBFBRRR
        BBFBFBFLLL
        FBFFBFBRRL
        BBFBBFFLRL
        FBBBBBBLLL
        FBFFBBFRRR
        FFFFBFFRRL
        FFFBBFBLLL
        FBFFFBBLLR
        FFBBBBBLRR
        FBFBFFFRLL
        BFFBBBBRRL
        FFFBFBBLLL
        FFFFFFBLLR
        BBFFBFBRLL
        FBBBBBFRRR
        BBFBFFFRLL
        FFFFBFBLRR
        BFBFFFFRLL
        BFBBBBFRLR
        FBFBFBFRRL
        FBFBFFFRRR
        FBBFFFBRRR
        FBBFBFFRLL
        FFFBFFFLLR
        FBFBBBFRLR
        FFBFFFBRRL
        FBBBBBFRRL
        FBBFFBFLLR
        FBFBBFBRRR
        FBFFFFFLLL
        FBFBBBFLRR
        FFFBBBBLLR
        FBBBFBBRRR
        BBFBBFFRLL
        FFFFBFBLLL
        BBFBBFBRLL
        BFFBBBBLLL
        BBFBBFBLLR
        BBFFFBBRRL
        FFBBBFFRRR
        BBFFBFFRLL
        FBBBBBFLLR
        FBBFFFFRRL
        FBBFFBFRLL
        FBFBFBFLLR
        BBFBBBFRRR
        FFFBBFBRLL
        BFBBBBFLRR
        BFBFFBFRRR
        FFBBBFBRRL
        BFBFFBBRRL
        BBFFBFBRRR
        FFBFBBBLLR
        FBBFFBBRLR
        BFBBBFFRLL
        FFFBBBFRLL
        FBFBFBFLLL
        FFFBBFBRLR
        BFBBFFBLLL
        FFBBBFFRLR
        BFBBFBBRLL
        BFFBFFBRRL
        BFFFFFBLRL
        FBBFFBBLRR
        BBFFFFBRLL
        FBFFBFFLLL
        BBFFBBBRRL
        BBFBFBFRLL
        BBFBFFFLLR
        BFBBFBFLLL
        FFBBFFBLRR
        BBFBFFBRLL
        FBFFFBBRLR
        FFFFFBFRLL
        BFFBBFBRRL
        BFFBBBFRRL
        FBFBFFBLLL
        BFBFBBFLRR
        FFBFBBFLLR
        FFBFBBBRLL
        FBFFBFFLRL
        FBBBBBBRRL
        FFFBFBFRRL
        FFBFBFFRRL
        BFFFBBFRRR
        BFBFBBBRLR
        BFFBBBFLRL
        FFFBBBBLRL
        BFFBBFFRRL
        FBFFBBBRLL
        BFFBBBFLLR
        BBFFBFFLLL
        BBFFBBFLRL
        BFFFFBFRRR
        FFFFFBBRLL
        FBFFBBFRRL
        FBFBBBBRRR
        BBFBBFFRRL
        FFBBFBFRRR
        FFBFBBBLRL
        BFBBFBBRRR
        FBFFFFBRRR
        FFFBFBBRRR
        FFFBBBFRLR
        BFBFBFBLLR
        BFBFBBFLLL
        BFBBFBBRLR
        BFBFFFBLRR
        FBFBBFFRLR
        BFBBFFFRLR
        BBFFFBFRRR
        FBBFFFFRRR
        BFFFBBFLLL
        BBFFFFBLRL
        FBBFBBBLRR
        BFBBFFBRLR
        FFBFFFFRRL
        BBFFBFFRLR
        FBFBFBBLLL
        BFFBBFFRLL
        FBBFBFBRLR
        FBBBBFFLLR
        FBFFFBBLRR
        FBBBBFBRRR
        BBFBBFBLRL
        FFFBBBBRLR
        BBFFBBBRLL
        FBFBFBFLRR
        FBBFBFFRRL
        FBFBBFBLLL
        FBFBFBBLLR
        BBFFBFFRRL
        FBFFBBBRLR
        FFBBFBBRRL
        FBBFFFBLLL
        FFFBBBFLRL
        FBBBFBFRLL
        FFBFBFFLRR
        FFBBFBBLLR
        FBBFBBFRLL
        FFFBBFBLLR
        FFBBBBBRRL
        FBFFBFFRLL
        FBFBFBBRRR
        BFFBBFFRLR
        FBFFFBBRRR
        FFBBBFFLLR
        FFFBBFFRLL
        BBFFBBBLRL
        FBBFBBBLRL
        BFFFFBBLLL
        FFFBBFFRRR
        FFFFBBBRRL
        BFBBBFFLLL
        FFFBBBBRRR
        BFFFBBBRLR
        FFFBBFFLRL
        BFFBBBBRLR
        BFBBBFFRRL
        BBFFFBFLLR
        BFFFFBBRRR
        FFBBFFFLRL
        FFFBFFFRLL
        FFBBFBBRRR
        BFBFBFFRLL
        FBBFBBBRLR
        BFBBBBBLLR
        FFBBBFFLLL
        BFBFBBBLLR
        FBBBBFBRLL
        BFBBFBFRRL
        FFBFFBBLRR
        BFBBFFBRLL
        FFBBBFBLLR
        BFFFBFFLRR
        FFFBBBBRLL
        FFBBBBFLRR
        FFFBFBFLRL
        FBBBFFFLLR
        BFFFFFBLLL
        FBBFBBBRRL
        FFFFBFBRLR
        BFBBBBFLLR
        FFFFBBBLLR
        FBFFFBBLRL
        BFBBBFFLRL
        FBFFBFFRRR
        BFBBBBBLRL
        FBBBFFFLLL
        BFFFFFBLLR
        BFBFBFBRLL
        BFBBFBBLRR
        BFBFBFBLRL
        FBFFBFBRRR
        FBFFFFFLRL
        BFFFFBFLRR
        BFFFBFBRRL
        BBFBFFFLRR
        BFBBFFFLLL
        BBFFBBFRLR
        FBFBBBBLLL
        FBFBBBBLLR
        FFFFFBFRRL
        BFFBBFFLRR
        FBFBFFBLLR
        FBBBFFBRRR
        FFFFBFBLRL
        BFBFBBFLRL
        BFFBFFBLRR
        FBBBBBFLLL
        BBFFBFBLLL
        BBFBFFBLLR
        FFBFFBBRLR
        BFFFFBBLRR
        FBFFFBBRLL
        BBFBFBFLRL
        BFFBBBFRLR
        BFBBFBBLRL
        FFFFBFFLRR
        FFFBBFFLRR
        FFFFBBFLLR
        FFFFBFBRRL
        BFBFBFFLLR
        FFFFFFBLRR
        FBBBFFBRLR
        FBBBFFBLLR
        BFFBBFBLRR
        FBFBBFFRLL
        FBFBBFBRLR
        BFBFBFBLLL
        FFFBBFFLLL
        FBFBBBFRRR
        FFFBFBFRRR
        FFBFBBFRLR
        FBFFBFBRLR
        FFFFFFBRRL
        FBBFBFBRRR
        BFBFFFBLLR
        FBBFFBBLRL
        FFFBFBFRLR
        BFFFFFBRRR
        FFBBBFBRLR
        FFFFFFBLRL
        BFBBFFBLRR
        BBFFFFFLLR
        BFBBBBFLLL
        FBBBFFFRRL
        FFBBFBFLRR
        BBFBBFFRLR
        BFFFBFFRRL
        BBFBFFBLLL
        BBFBFFFLRL
        BFFFFBFRLL
        BFFFFBFRLR
        BFFBFBFLRR
        BFFBBBFRRR
        FBFBBBBLRR
        BBFBFFBRLR
        BBFBFFBLRL
        BFFFFFFLRR
        BFBFBFFRRR
        FFBFFFFLLR
        FFBFBBFLLL
        BFBFFBFLRR
        FBBFFBBRLL
        BFFFFBFLLR
        FBBFFBFLLL
        FBBFFFBRRL
        FBFBBFBLRL
        BBFFBBFRRL
        BBFFFFBRRR
        BFBFBFFRLR
        FBFBFFBLRL
        FBFBFFBRRR
        BBFFFBFRRL
        FBFBFBFRRR
        BBFBFFBLRR
        FBFBBFFRRL
        BBFFBBBLLL
        FBFBFFFLLR
        BBFBBBBLRR
        FBFBBBBRRL
        BFFFBBBLRR
        BBFFBBFLLR
        FBBBBBBRRR
        FFFBFFFLRL
        FFFFBFFLRL
        BFFFBBFLRL
        FFBFFBFRRL
        FFBBFBFRLR
        FBBBBBBRLL
        FBBFFFBRLL
        BBFBFFBRRL
        FFFBFFBLRR
        FFBFBFFLRL
        FBBFFBBRRR
        BFBFFFBRRR
        FBBBBFBLRR
        FBBBFBBLLL
        BFBBBBFRLL
        BFBBBBFLRL
        FFBBBBBLRL
        FFBBBBFRRR
        FFBFFBFLRL
        FFBFFBBLLR
        FFBBFFFLLL
        BFBBBFBRRR
        BFFFFFFRLR
        FFBFBFBRLR
        FFBFBBBLRR
        BBFFBFBLRR
        FBFFBBFLRR
        BFBFFFBLRL
        FBFBFBFRLL
        FFBBBFFRRL
        BFFBFFBRLR
        BFFFFFFRRL
        BFBFBBBRLL
        BBFBFFFRRL
        BFFFFBBLRL
        FBBBFBFLRR
        BFBFBFFLRR
        FFFFFBFLLR
        FBBBBBFRLR
        FBFFFBFLLR
        BBFBFBBLLR
        FBFBFFBLRR
        BBFFFBBLLR
        BFBFBBBRRL
        BFFFFFBLRR
        BFFBFFFRLL
        BFFFBFBRLL
        FBBFBBFLRL
        FBBBFBFRRR
        FFBBBBBRRR
        BBFBBFFLLR
        BFFBBFFLRL
        BFBBFBBLLR
        FFBBFFBLLL
        BBFBFBFRRL
        BFFBFBBRRR
        BFFBBBBLRR
        FFBFFBFRLR
        FBFBFFFLRL
        FFFFBBBRLL
        BFFBFFFRLR
        FFBFFFFRLR
        FFBBFBFLRL
        BFFBFBFRRL
        FBFBFFBRLR
        FBBFBFFLRR
        BFBFFFFLLR
        BFFFBFBLLL
        FFFFBFFRLL
        FBFBFBBRLR
        FBBBBFFRRR
        BFFFBBFLLR
        BBFFFFFLLL
        BFFBFFFRRR
        FBBBBFFRLR
        BBFFFBBRLL
        FFBFBBBLLL
        FFFBFBFRLL
        BFBFFFBRLL
        FFFFFBBLLL
        BFFBBFFRRR
        BBFBBBFRRL
        BFBFFBBLRR
        FFFFFBFLRL
        BFFBFBFRLR
        BFFBFFBLRL
        BBFFFBFLRL
        FBBBBFBLLL
        BFBFBFFLRL
        FFFBFBFLLR
        BFBFBBFRLL
        BBFFBFBRLR
        BFBFFBFRLL
        FFFFFBBLRR
        FBFFBFBLLR
        BFBFFFBLLL
        FBFFBBFRLL
        FBBBBFFLLL
        FBFFFFFRLL
        BFFBFFFRRL
        FBBBBBBLRL
        BFFBFBBRRL
        BBFBFBFRLR
        FBFBBBBRLL
        BBFFBBBRRR
        BFFFFBBRLR
        BFBBBBBRRL
        FFBBFBBRLR
        FFFBFBFLRR
        FFFFFFBLLL
        FBFBBBBLRL
        FBFFFFFLRR
        FBBBFFBLLL
        BBFBBFFRRR
        FFFBFFBRLL
        FBFFFBFRRL
        FBFBBBFRRL
        BFBFFFFRRR
        BFBBBFFLLR
        FBBBBFFLRR
        FFFFBFBRRR
        FBBBBBFLRR
        FFBFFBBLRL
        FFFBFBBLLR
        BFFFBFFLLR
        FBBBBFFRRL
        BBFFBFFLLR
        BFBFFFFLLL
        FBFFFBFRLL
        BFBFFFFLRR
        BFFFBFFLLL
        FBBFBFBRLL
        BFFFFBFLRL
        BFFBBBBLLR
        BFFBBBBRLL
        FBBBBFBLLR
        FBFBFFFLLL
        FFBBFFFRRR
        BFBBBBBRLR
        FFBBFBBLLL
        FBFBBFFLRR
        FFBBFFFRLL
        FBFFFFFRRR
        BBFBBBFRLL
        FBBFBBFRLR
        FFBFFFBRRR
        FBBFFFBLRL
        FFFBFBBRLR
        FBFFFFBLRL
        BFBFBBFRLR
        FBBBFBFRLR
        BFFFBFBLRL
        FFFBBBBLLL
        BBFBFBFLRR
        FBFFFFBLLR
        BBFBBBBRLL
        FFBBBBBLLR
        BBFBBFBRRL
        FBBFFFFRLL
        FFBBBFBLRL
        BFFFBBBLLL
        FFFFFBFLLL
        BFBBBFBLLR
        FFBBBFBLLL
        FFFFFFBRRR
        BFBBBFFRLR
        FBBBFBBRLL
        FFBFBBBRRL
        FBBBFFFLRR
        BBFFFBFLLL
        FBBBBBBLRR
    """.trimIndent()
}