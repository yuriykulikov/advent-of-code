package aoc

import org.assertj.core.api.Assertions.assertThat
import org.junit.Ignore
import org.junit.Test

class Advent21 {
    @Test
    fun `21 day (no idea how to assert that, but it is correct)`() {
        assertThat(executeProgram(input, 9959629, cap = 10000000)).isEqualTo(1848)
    }

    @Test
    fun `21 day ()`() {
        executeProgram(input, 9959629, debug = true)
    }

    @Ignore("I think this hangs")
    @Test
    fun `21 Star 2 ()`() {
        val firstHalt = (0..9959629)
            .asSequence()
            .filter {
                executeProgram(input, it, debug = false, cap = 10000) != 0
            }
            .forEachIndexed { index, it -> println("index: $index $it") }
//
        println("1: " + executeProgram(input, 1, debug = false, cap = 100000))
        println("9959628:" + executeProgram(input, 9959628, debug = false, cap = 100000))
        println("9959629:" + executeProgram(input, 9959629, debug = false, cap = 100000))
        println("9959630:" + executeProgram(input, 9959630, debug = false, cap = 100000))
        println("9959630:" + executeProgram(input, 9959631, debug = false, cap = 100000))
        println("9959630:" + executeProgram(input, 9959629 * 2, debug = false, cap = 100000))
    }


    val input = """
#ip 4
seti 123 0 3
bani 3 456 3
eqri 3 72 3
addr 3 4 4
seti 0 0 4
seti 0 5 3
bori 3 65536 5
seti 5557974 2 3
bani 5 255 2
addr 3 2 3
bani 3 16777215 3
muli 3 65899 3
bani 3 16777215 3
gtir 256 5 2
addr 2 4 4
addi 4 1 4
seti 27 9 4
seti 0 0 2
addi 2 1 1
muli 1 256 1
gtrr 1 5 1
addr 1 4 4
addi 4 1 4
seti 25 4 4
addi 2 1 2
seti 17 6 4
setr 2 2 5
seti 7 1 4
eqrr 3 0 2
addr 2 4 4
seti 5 7 4
    """.trimIndent()
}