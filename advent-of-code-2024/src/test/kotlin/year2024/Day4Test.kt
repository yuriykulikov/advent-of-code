package year2024

import down
import io.kotest.matchers.shouldBe
import left
import loadResource
import org.junit.jupiter.api.Test
import parseMap
import right
import up

class Day4Test {
    private val example = """MMMSXXMASM
MSAMXMSMSA
AMXSXMAAMM
MSAMASMSMX
XMASAMXAMM
XXAMMXXAMA
SMSMSASXSS
SAXAMASAAA
MAMMMXMMMM
MXMXAXMASX
"""

    @Test
    fun `silver test`() {
        countXmas(example) shouldBe 18
    }

    @Test
    fun `silver`() {
        countXmas(loadResource("Day4")) shouldBe 2397
    }

    @Test
    fun `gold test`() {
        countXMas(example) shouldBe 9
    }

    @Test
    fun `gold`() {
        countXMas(loadResource("Day4")) shouldBe 1824
    }

    /**
     * Check for XMAS around each X in all directions
     */
    private fun countXmas(example: String): Int {
        val map = parseMap(example)

        return map.entries.filter { it.value == 'X' }
            .map { (point, x) ->
                // need to count from point in all directions
                listOf(
                    listOf(point, point.left(), point.left(2), point.left(3)),
                    listOf(point, point.right(), point.right(2), point.right(3)),
                    listOf(point, point.up(), point.up(2), point.up(3)),
                    listOf(point, point.down(), point.down(2), point.down(3)),
                    listOf(point, point.left().up(), point.left(2).up(2), point.left(3).up(3)),
                    listOf(point, point.right().up(), point.right(2).up(2), point.right(3).up(3)),
                    listOf(point, point.left().down(), point.left(2).down(2), point.left(3).down(3)),
                    listOf(point, point.right().down(), point.right(2).down(2), point.right(3).down(3))
                )
                    .count { line ->
                        line.map { map[it] }.joinToString("") == "XMAS"
                    }
            }.sum()
    }

    /**
     * Check for X around each A
     */
    private fun countXMas(example: String): Int {
        val map = parseMap(example)

        return map.entries.filter { it.value == 'A' }
            .count { (center, a) ->

                val lu = center.left().up()
                val ru = center.right().up()
                val ld = center.left().down()
                val rd = center.right().down()

                val diag1 = (map[lu] == 'M' && map[rd] == 'S'
                        || map[lu] == 'S' && map[rd] == 'M')

                val diag2 = (map[ld] == 'M' && map[ru] == 'S'
                        || map[ld] == 'S' && map[ru] == 'M')

                diag1 && diag2
            }
    }
}