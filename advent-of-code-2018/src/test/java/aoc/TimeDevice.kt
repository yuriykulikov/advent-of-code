package aoc

data class Operation(val opcode: String, val a: Int, val b: Int, val c: Int) {
    override fun toString(): String {
        return "$opcode $a $b $c"
    }

    fun mutate(registers: Registers): Registers {
        return when (opcode) {
            //addr (add register) stores into register C the result of adding register A and register B.
            "addr" -> registers.copyIn(c, registers.valueOf(a) + registers.valueOf(b))
            //addi (add immediate) stores into register C the result of adding register A and value B.
            "addi" -> registers.copyIn(c, registers.valueOf(a) + b)
            //mulr (multiply register) stores into register C the result of multiplying register A and register B.
            "mulr" -> registers.copyIn(c, registers.valueOf(a) * registers.valueOf(b))
            //muli (multiply immediate) stores into register C the result of multiplying register A and value B.
            "muli" -> registers.copyIn(c, registers.valueOf(a) * b)
            //banr (bitwise AND register) stores into register C the result of the bitwise AND of register A and register B.
            "banr" -> registers.copyIn(c, registers.valueOf(a) and registers.valueOf(b))
            //        bani (bitwise AND immediate) stores into register C the result of the bitwise AND of register A and value B.
            "bani" -> registers.copyIn(c, registers.valueOf(a) and b)

            //borr (bitwise OR register) stores into register C the result of the bitwise OR of register A and register B.
            "borr" -> registers.copyIn(c, registers.valueOf(a) or registers.valueOf(b))
            //bori (bitwise OR immediate) stores into register C the result of the bitwise OR of register A and value B.
            "bori" -> registers.copyIn(c, registers.valueOf(a) or b)

            //setr (set register) copies the contents of register A into register C. (Input B is ignored.)
            "setr" -> registers.copyIn(c, registers.valueOf(a))
            //seti (set immediate) stores value A into register C. (Input B is ignored.)
            "seti" -> registers.copyIn(c, a)
            //Greater-than testing:
            //gtir (greater-than immediate/register) sets register C to 1 if value A is greater than register B. Otherwise, register C is set to 0.
            "gtir" -> registers.copyIn(c, if (a > registers.valueOf(b)) 1 else 0)
            //gtri (greater-than register/immediate) sets register C to 1 if register A is greater than value B. Otherwise, register C is set to 0.
            "gtri" -> registers.copyIn(c, if (registers.valueOf(a) > b) 1 else 0)
            //gtrr (greater-than register/register) sets register C to 1 if register A is greater than register B. Otherwise, register C is set to 0.
            "gtrr" -> registers.copyIn(c, if (registers.valueOf(a) > registers.valueOf(b)) 1 else 0)
            //Equality testing:
            //eqir (equal immediate/register) sets register C to 1 if value A is equal to register B. Otherwise, register C is set to 0.
            "eqir" -> registers.copyIn(c, if (a == registers.valueOf(b)) 1 else 0)
            //eqri (equal register/immediate) sets register C to 1 if register A is equal to value B. Otherwise, register C is set to 0.
            "eqri" -> registers.copyIn(c, if (registers.valueOf(a) == b) 1 else 0)
            //eqrr (equal register/register) sets register C to 1 if register A is equal to register B. Otherwise, register C is set to 0.
            "eqrr" -> registers.copyIn(c, if (registers.valueOf(a) == registers.valueOf(b)) 1 else 0)
            else -> throw RuntimeException()
        }
    }
}

data class Registers(
    val reg0: Int,
    val reg1: Int,
    val reg2: Int,
    val reg3: Int,
    val reg4: Int = 0,
    val reg5: Int = 0,
    val ip: Int = 0,
    val ipReg: Int = 0,
    val count: Int = 0
) {
    fun valueOf(register: Int): Int {
        return when (register) {
            0 -> reg0
            1 -> reg1
            2 -> reg2
            3 -> reg3
            4 -> reg4
            5 -> reg5
            else -> throw RuntimeException()
        }
    }

    fun copyIn(register: Int, value: Int): Registers {
        return when (register) {
            0 -> copy(reg0 = value)
            1 -> copy(reg1 = value)
            2 -> copy(reg2 = value)
            3 -> copy(reg3 = value)
            4 -> copy(reg4 = value)
            5 -> copy(reg5 = value)
            else -> throw RuntimeException()
        }
    }

    override fun toString(): String {
        return "[$reg0, $reg1, $reg2, $reg3, $reg4, $reg5]"
    }
}

fun executeProgram(
    programCode: String,
    regInitial: Int = 0,
    optimize: Pair<Int, (Registers) -> Registers>? = null,
    debug: Boolean = false,
    cap: Int? = null
): Int {
    val program: List<Operation> =
        programCode.lines()
            .filterNot { it.isBlank() }
            .drop(1)
            .map { it.split(' ') }
            .map { (op, a, b, c) ->
                Operation(op, a.toInt(), b.toInt(), c.toInt())
            }

    val initialIpRegBinding = programCode.lines()
        .filterNot { it.isBlank() }
        .first()
        .split(' ')[1].toInt()

    // When the instruction pointer is bound to a register, its value is written to that register just before each
    // instruction is executed, and the value of that register is written back to the instruction pointer immediately
    // after each instruction finishes execution. Afterward, move to the next instruction by adding one to the instruction
    // pointer, even if the value in the instruction pointer was just updated by an instruction. (Because of this,
    // instructions must effectively set the instruction pointer to the instruction before the one they want executed next.)
    return generateSequence(
        Registers(
            reg0 = regInitial,
            reg1 = 0,
            reg2 = 0,
            reg3 = 0,
            reg4 = 0,
            reg5 = 0,
            ip = 0,
            ipReg = initialIpRegBinding
        )
    ) { beforeCount ->
        val prev = beforeCount.copy(count = beforeCount.count + 1)
        val instr = program.getOrNull(prev.ip)
        // println("${prev.ip} : $instr")
        // println(instr)
        val next = when {
            instr == null -> null
            optimize != null && prev.ip == optimize.first -> optimize.second(prev)
            else ->
                prev
                    .let { it.copyIn(it.ipReg, it.ip) }
                    .let { instr.mutate(it) }
                    .let { it.copy(ip = it.valueOf(it.ipReg) + 1) }
        }
        if (debug) println("ip=${prev.ip} $prev $instr $next")
        next
    }.let { sequence ->
        when {
            // capped
            cap != null -> {
                val last = sequence.take(cap).last()
                if (last.count == cap - 1) 0 else last.count
            }
            // normal path
            else -> sequence.filterNotNull().last().reg0
        }
    }
}
