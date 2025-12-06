package year2025

import Point
import io.kotest.matchers.shouldBe
import loadResource
import org.junit.jupiter.api.Test
import parseMap
import right

class Day6Test {
    private val example = """
123 328  51 64 
 45 64  387 23 
  6 98  215 314
*   +   *   +  
    """.trimIndent()


    @Test
    fun `Silver example - What is the grand total found by adding together all of the answers to the individual problems`() {
        solveCephalopodMath(example).shouldBe(4277556)
    }

    @Test
    fun `Silver - What is the grand total found by adding together all of the answers to the individual problems`() {
        solveCephalopodMath(loadResource("Day6")).shouldBe(5733696195703)
    }

    data class Num(val row: Int, val col: Int, val num: String)

    private fun solveCephalopodMath(example: String): Long {

        return parseInput(example).groupBy { it.col }
            .values.sumOf { col: List<Num> ->
                val operator = col.last().num
                val numbers = col.dropLast(1).map { it.num.toLong() }
                if (operator == "+") {
                    numbers.sum()
                } else {
                    numbers.reduce { a, b -> a * b }
                }
            }
    }

    private fun parseInput(example: String): List<Num> {
        val numbers = example.lines()
            .map { line -> line.split("\\s+".toRegex()).filter { it.isNotBlank() } }
            .flatMapIndexed { row, line ->
                line.mapIndexed { col, value ->
                    Num(row, col, value)
                }
            }
        return numbers
    }

    @Test
    fun `Gold example - right-to-left in columns`() {
        solveCephalopodMathRTL(example).shouldBe(3263827)
    }

    @Test
    fun `Gold - right-to-left in columns`() {
        solveCephalopodMathRTL(loadResource("Day6")).shouldBe(10951882745757)
    }

    /**
     * If I have a hammer, everything looks like a nail.
     * Since I already have these Points and maps, let's use them
     */
    private fun solveCephalopodMathRTL(example: String): Long {
        val map = parseMap(example)

        val maxY = map.keys.maxBy { it.y }.y
        val terminator = map.keys.maxBy { it.x }.right().right()

        val operators = map.keys.filter { it.y == maxY && !map.getValue(it).isWhitespace() }

        return (operators + terminator).zipWithNext { operatorPoint, next ->
            val operator = map.getValue(operatorPoint)
            val ys = 0..<operatorPoint.y
            val xs = (next.x - 2).downTo(operatorPoint.x)
            val columns = xs.mapNotNull { x ->
                ys.mapNotNull { y ->
                    map.getValue(Point(x, y)).digitToIntOrNull()
                }.joinToString("").toLong()
            }
            if (operator == '+') {
                columns.sum()
            } else {
                columns.reduce { a, b -> a * b }
            }
        }
            .sum()
    }

}

