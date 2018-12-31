package aoc

import org.assertj.core.api.KotlinAssertions.assertThat
import org.junit.Test

class Advent19 {
    // ip=0 [0, 0, 0, 0, 0, 0] seti 5 0 1 [0, 5, 0, 0, 0, 0]
    // ip=1 [1, 5, 0, 0, 0, 0] seti 6 0 2 [1, 5, 6, 0, 0, 0]
    // ip=2 [2, 5, 6, 0, 0, 0] addi 0 1 0 [3, 5, 6, 0, 0, 0]
    // ip=4 [4, 5, 6, 0, 0, 0] setr 1 0 0 [5, 5, 6, 0, 0, 0]
    // ip=6 [6, 5, 6, 0, 0, 0] seti 9 0 5 [6, 5, 6, 0, 0, 9]
    @Test
    fun `Star 1 Test`() {
        val reg0 = executeProgram(testProgram)
        assertThat(reg0).isEqualTo(6)
    }

    @Test
    fun `Star 1`() {
        val reg0 = executeProgram(program)
        assertThat(reg0).isEqualTo(1824)
    }


    @Test
    fun `Star 1 optimized`() {
        val reg0 = executeProgram(program, optimize = 3 to ::wtfIstDas2)
        assertThat(reg0).isEqualTo(1824)
    }

    @Test
    fun `Star 2`() {
        val reg0 = executeProgram(program, regInitial = 1, optimize = 3 to ::wtfIstDas2)
        assertThat(reg0).isEqualTo(21340800)
    }

    fun exec2() {
        // 0 : addi 1 16 1
        // 17 : addi 4 2 4
        // 18 : mulr 4 4 4
        // 19 : mulr 1 4 4
        // 20 : muli 4 11 4
        // 21 : addi 3 3 3
        // 22 : mulr 3 1 3
        // 23 : addi 3 4 3
        // 24 : addr 4 3 4
        // 25 : addr 1 0 1
        // 26 : seti 0 0 1
        // 1 : seti 1 2 5
        // 2 : seti 1 2 2
        // 3 : mulr 5 2 3
        // 4 : eqrr 3 4 3
        // 5 : addr 3 1 1
        // 6 : addi 1 1 1
        // 8 : addi 2 1 2
        // 9 : gtrr 2 4 3
        // 10 : addr 1 3 1
        // 11 : seti 2 8 1
        // 3 : mulr 5 2 3
        // 4 : eqrr 3 4 3
        // 5 : addr 3 1 1
        // 6 : addi 1 1 1
        // 8 : addi 2 1 2
        // 9 : gtrr 2 4 3
        // 10 : addr 1 3 1
        // 11 : seti 2 8 1
        // 3 : mulr 5 2 3
        // 4 : eqrr 3 4 3
        // 5 : addr 3 1 1
        // 6 : addi 1 1 1
        // 8 : addi 2 1 2
        // 9 : gtrr 2 4 3
        // 10 : addr 1 3 1
        // 11 : seti 2 8 1
        // 3 : mulr 5 2 3
        // 4 : eqrr 3 4 3
        // 5 : addr 3 1 1
        // 6 : addi 1 1 1
        // 8 : addi 2 1 2
        // 9 : gtrr 2 4 3
        // 10 : addr 1 3 1
        // 11 : seti 2 8 1
        // 3 : mulr 5 2 3
        // 4 : eqrr 3 4 3
        // 5 : addr 3 1 1
        // 6 : addi 1 1 1
        // 8 : addi 2 1 2
        // 9 : gtrr 2 4 3
        // 10 : addr 1 3 1
        // 11 : seti 2 8 1

        var reg0: Int = 0
        var ip: Int = 0
        var reg2: Int = 0
        var reg3: Int = 0
        var reg4: Int = 0
        var reg5: Int = 0

        // 3 : mulr 5 2 3
        // 4 : eqrr 3 4 3
        // 5 : addr 3 1 1
        // 6 : addi 1 1 1
        // 8 : addi 2 1 2
        // 9 : gtrr 2 4 3
        // 10 : addr 1 3 1
        // 11 : seti 2 8 1

        //  0  1   2  3    4  5
        // [0, 2, 10, 0, 906, 1]

        // ASM
        // 3 : mulr 5 2 3
        reg3 = reg5 * reg2
        // 4 : eqrr 3 4 3
        reg3 = if (reg3 == reg4) 1 else 0
        // 5 : addr 3 1 1
        ip = reg3 + ip // jump to 7 or to 6
        // 6 : addi 1 1 1
        ip = ip + 1 // jump over 7 to 8
        // 7 addr 5 0 0
        reg0 = reg0 + reg5
        // 8 : addi 2 1 2
        reg2 = reg2 + 1
        // 9 : gtrr 2 4 3
        reg3 = if (reg2 > reg4) 1 else 0
        // 10 : addr 1 3 1
        ip = reg3 + ip // exits the loop
        // 11 : seti 2 8 1
        ip = 2

        // reg5 and reg4 are inputs, reg0 is an accumulator and reg1 is the ip
    }

    /**
     * reg4 does not seem to change at all (constant)
     * reg5 is an input for the subroutine
     * reg0 is the output
     */
    fun wtfIstDas(registers: Registers): Registers {
        val (reg0, _, _, _, reg4, reg5) = registers
        // reg2 starts with 1 and goes until reg4 (906)
        repeat(reg4 + 1) { reg2 ->
            if (reg5 * reg2 == reg4) {
                // add to the accumulator and return from the subroutine
                return registers.copy(reg0 = reg0 + reg5, ip = 12)
            }
        }
        return registers.copy(ip = 12)
    }

    /**
     * reg4 does not seem to change at all (constant)
     * reg5 is an input for the subroutine
     * reg0 is the output
     *
     * Basically, if exists a value 1..reg4, which gives reg5 * reg2 == reg4, add reg5 to reg0
     * if reg4.rem(reg5) == 0 ?
     */
    fun wtfIstDas2(registers: Registers): Registers {
        val (reg0, _, _, _, reg4, reg5) = registers
        // reg2 starts with 1 and goes until reg4 (906)
        return when {
            // add to the accumulator and return from the subroutine
            reg4.rem(reg5) == 0 -> registers.copy(reg0 = reg0 + reg5, ip = 12)
            else -> registers.copy(ip = 12)
        }
    }

    val testProgram = """
#ip 0
seti 5 0 1
seti 6 0 2
addi 0 1 0
addr 1 2 3
setr 1 0 0
seti 8 0 4
seti 9 0 5
""".trimIndent()

    val program = """
#ip 1
addi 1 16 1
seti 1 2 5
seti 1 2 2
mulr 5 2 3
eqrr 3 4 3
addr 3 1 1
addi 1 1 1
addr 5 0 0
addi 2 1 2
gtrr 2 4 3
addr 1 3 1
seti 2 8 1
addi 5 1 5
gtrr 5 4 3
addr 3 1 1
seti 1 1 1
mulr 1 1 1
addi 4 2 4
mulr 4 4 4
mulr 1 4 4
muli 4 11 4
addi 3 3 3
mulr 3 1 3
addi 3 4 3
addr 4 3 4
addr 1 0 1
seti 0 0 1
setr 1 5 3
mulr 3 1 3
addr 1 3 3
mulr 1 3 3
muli 3 14 3
mulr 3 1 3
addr 4 3 4
seti 0 0 0
seti 0 1 1
""".trimIndent()
}