import kotlinx.collections.immutable.PersistentList
import kotlinx.collections.immutable.toPersistentList

fun intCodeComputer(
    program: List<Number>,
    mutable: Boolean = true,
    vararg input: Int
): IntCodeComputer {
    val executable = when {
        mutable -> MutableProgram(program.map { it.toLong() }.toMutableList())
        else -> PersistentProgram(program.map { it.toLong() }.toPersistentList())
    }

    return IntCodeComputer(
        program = executable,
        input = input.map { it.toLong() }.toList()
    )
}

fun intCodeComputer(
    program: String,
    mutable: Boolean = true
): IntCodeComputer {
    return intCodeComputer(
        mutable = mutable,
        program = program.split(",").map { it.toLong() }
    )
}

data class IntCodeComputer(
    val input: List<Long>,
    val pc: Int = 0,
    val program: Program,
    val out: List<Long> = emptyList(),
    val relativeBase: Long = 0
)

interface Program {
    operator fun get(index: Int): Long
    fun getOrElse(index: Int, function: () -> Number): Long
    fun growAndSet(index: Int, value: Long): Program
    fun asList(): List<Long>
}

class MutableProgram(private val program: MutableList<Long>) : Program {
    override fun asList(): List<Long> {
        return program.toList()
    }

    override fun get(index: Int): Long {
        return program[index]
    }

    override fun getOrElse(index: Int, function: () -> Number): Long {
        return program.getOrElse(index) { function().toLong() }
    }

    override fun growAndSet(index: Int, value: Long): Program {
        if (index > program.lastIndex) {
            program.addAll(LongArray(index - program.lastIndex) { 0 }.toList())
        }
        program[index] = value
        return this
    }
}

class PersistentProgram(private val program: PersistentList<Long>) : Program {
    override fun asList(): List<Long> {
        return program
    }

    override fun get(index: Int): Long {
        return program[index]
    }

    override fun getOrElse(index: Int, function: () -> Number): Long {
        return program.getOrElse(index) { function().toLong() }
    }

    override fun growAndSet(index: Int, value: Long): Program {
        return PersistentProgram(program.growAndSet(index, value))
    }
}

fun IntCodeComputer.addInput(mutator: (List<Number>) -> List<Number>): IntCodeComputer {
    return copy(input = mutator(input).map { it.toLong() })
}

fun IntCodeComputer.runToHalt(): IntCodeComputer {
    return generateSequence(this) { memory -> memory.executeInstruction() }
        .takeWhile { memory -> memory.pc >= 0 }
        .last()
}

/** Executes to first output */
fun IntCodeComputer.runToOutputOrHalt(outputSize: Int = 1): IntCodeComputer {
    val computer = generateSequence(this.copy(out = emptyList())) { memory -> memory.executeInstruction() }
        .filter { memory -> memory.out.size == outputSize || memory.pc == -1 }
        .first()
    if (computer.pc == -1) {
        throw Exception("halted")
    } else {
        return computer
    }
}

fun IntCodeComputer.runToOutputOrNull(outputSize: Int = 1): IntCodeComputer? {
    val computer = generateSequence(this.copy(out = emptyList())) { memory -> memory.executeInstruction() }
        .filter { memory -> memory.out.size == outputSize || memory.pc == -1 }
        .first()
    return when {
        computer.pc == -1 -> null
        else -> computer
    }
}

/** Executes to first output */
fun IntCodeComputer.runToInputOrHalt(): IntCodeComputer {
    return generateSequence(this) { memory -> memory.executeInstruction() }
        // first input
        .filter { computer ->
            computer.pc == -1 || (
                    computer.input.isEmpty() &&
                            computer.program[computer.pc].toInt() % 100 == 3)
        }
        .first()
}

fun Int.pow(n: Int): Int = when (n) {
    0 -> 1
    1 -> this
    else -> this * pow(n - 1)
}

private fun PersistentList<Long>.growAndSet(pos: Int, value: Long): PersistentList<Long> {
    return if (pos > lastIndex) {
        this.builder().apply { repeat(pos - lastIndex) { add(0) } }.build()
    } else {
        this
    }.set(pos, value)
}

fun IntCodeComputer.executeInstruction(): IntCodeComputer {
    check(pc >= 0) { "Program has finished" }
    val instruction = program[pc].toInt()

    fun rs(pos: Int = 3): Long {
        val mode = instruction / 10.pow(pos + 1) % 10
        return when (mode) {
            // position mode
            0 -> program.getOrElse(program[pc + pos].toInt()) { 0 }
            // relative mode
            2 -> program[(relativeBase + program[pc + pos]).toInt()]
            // immediate mode
            else -> program[pc + pos]
        }
    }

    fun rs1(): Long = rs(1)
    fun rs2(): Long = rs(2)
    fun rd(pos: Int = 3): Int {
        val mode = instruction / 10.pow(pos + 1) % 10
        return when (mode) {
            2 -> (program.getOrElse(pc + pos) { 0 } + relativeBase.toInt()).toInt()
            else -> program.getOrElse(pc + pos) { 0 }.toInt()
        }
    }

    return when (instruction % 100) {
        1 -> copy(
            pc = pc + 4,
            program = program.growAndSet(rd(), rs1() + rs2())
        )
        2 -> copy(
            pc = pc + 4,
            program = program.growAndSet(rd(), rs1() * rs2())
        )
        3 -> {
            // println("Input: ${input.first()}")
            copy(
                pc = pc + 2,
                program = program.growAndSet(rd(pos = 1), input.first()),
                input = input.drop(1)
            )
        }
        4 -> {
            // println("Output: ${rs1()}")
            copy(
                pc = pc + 2,
                program = program,
                out = out.plus(rs1())
            )
        }
        //Opcode 5 is jump-if-true: if the first parameter is non-zero, it sets the instruction pointer to the value from the second parameter. Otherwise, it does nothing.
        5 -> copy(pc = if (rs1().toInt() != 0) rs2().toInt() else pc + 3)
        //Opcode 6 is jump-if-false: if the first parameter is zero, it sets the instruction pointer to the value from the second parameter. Otherwise, it does nothing.
        6 -> copy(pc = if (rs1().toInt() == 0) rs2().toInt() else pc + 3)
        //Opcode 7 is less than: if the first parameter is less than the second parameter, it stores 1 in the position given by the third parameter. Otherwise, it stores 0.
        7 -> copy(
            pc = pc + 4,
            program = program.growAndSet(rd(), if (rs1() < rs2()) 1 else 0)
        )
        //Opcode 8 is equals: if the first parameter is equal to the second parameter, it stores 1 in the position given by the third parameter. Otherwise, it stores 0.
        8 -> copy(
            pc = pc + 4,
            program = program.growAndSet(rd(), if (rs1() == rs2()) 1 else 0)
        )
        9 -> copy(
            pc = pc + 2,
            relativeBase = relativeBase + rs1().toInt()
        )
        99 -> copy(
            pc = -1,
            program = program
        )
        else -> throw Exception("Went south with $instruction")
    }
}