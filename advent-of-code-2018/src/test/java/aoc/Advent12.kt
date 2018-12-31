package aoc

import org.assertj.core.api.Assertions.assertThat
import org.junit.Test

class Advent12 {
    data class Pattern(private val input: String, val pad: Int) {
        private val split = input.split(" => ")
        val lookFor = split[0]
        val result = split[1].first()
        fun matchHash(lookIn: String): List<Int> {
            // println(lookIn)
            return lookIn.toCharArray()
                .asSequence()
                .windowed(size = 5, step = 1)
                .mapIndexedNotNull { index, window ->
                    val windowToString = window.joinToString("").padStart(index + pad + 2, ' ')
                    when (window) {
                        lookFor.toList() -> {
                            // println("$windowToString!")
                            index + 2
                        }
                        else -> {
                            // println(windowToString)
                            null
                        }
                    }
                }.toList()
        }
    }

    @Test
    fun `example`() {
        val pad = 15
        val list = generations(pad, testInitial, testRecord.lines().map { Pattern(it, pad) })

        assertThat(list[0]).contains("...#..#.#..##......###...###...........")
        assertThat(list[1]).contains("...#...#....#.....#..#..#..#...........")
        assertThat(list[2]).contains("...##..##...##....#..#..#..##..........")
        assertThat(list[3]).contains("..#.#...#..#.#....#..#..#...#..........")
        assertThat(list[4]).contains("...#.#..#...#.#...#..#..##..##.........")
        assertThat(list[5]).contains("....#...##...#.#..#..#...#...#.........")
        assertThat(list[6]).contains("....##.#.#....#...#..##..##..##........")
        assertThat(list[7]).contains("...#..###.#...##..#...#...#...#........")
        assertThat(list[8]).contains("...#....##.#.#.#..##..##..##..##.......")
        assertThat(list[9]).contains("...##..#..#####....#...#...#...#.......")
        assertThat(list[10]).contains("..#.#..#...#.##....##..##..##..##......")
        assertThat(list[11]).contains("...#...##...#.#...#.#...#...#...#......")
        assertThat(list[12]).contains("...##.#.#....#.#...#.#..##..##..##.....")
        assertThat(list[13]).contains("..#..###.#....#.#...#....#...#...#.....")
        assertThat(list[14]).contains("..#....##.#....#.#..##...##..##..##....")
        assertThat(list[15]).contains("..##..#..#.#....#....#..#.#...#...#....")
        assertThat(list[16]).contains(".#.#..#...#.#...##...#...#.#..##..##...")
        assertThat(list[17]).contains("..#...##...#.#.#.#...##...#....#...#...")
        assertThat(list[18]).contains("..##.#.#....#####.#.#.#...##...##..##..")
        assertThat(list[19]).contains(".#..###.#..#.#.#######.#.#.#..#.#...#..")
        assertThat(list[20]).contains(".#....##....#####...#######....#.#..##.")

        val sum = lastGenerationSum(list[20], pad)

        assertThat(sum).isEqualTo(325)
    }

    private fun lastGenerationSum(getStr: String, pad: Int): Long {
        return getStr
            .drop(pad)
            .mapIndexedNotNull { index, char ->
                when (char) {
                    '#' -> -3L + index
                    else -> null
                }
            }.sum()
    }

    private fun generations(
        pad: Int,
        initialString: String,
        patterns: List<Pattern>,
        infinite: Boolean = false
    ): List<String> {
        val padStr = ".".repeat(pad)

        val seq = generateSequence("$padStr...$initialString.........$padStr") { previosGeneration ->
            // println(previosGeneration)
            val hashes: List<Int> = patterns
                .flatMap { pattern ->
                    pattern.matchHash(previosGeneration)
                }

            val result = previosGeneration
                .mapIndexed { index, prev ->
                    if (hashes.contains(index)) '#' else '.'
                }
                .joinToString("")
            // println(result)
            result
        }
        return if (infinite) {
            seq.take(126 + 1)
                .toList()
            // val sameGenerations = 10
            // seq.windowed(sameGenerations + 1)
            //         .takeWhile { it[0].trim('.') != it[sameGenerations].trim('.') }
            //         .map { it[0] }
            //         .toList()
            // .dropLast(sameGenerations)
        } else {
            seq.take(20 + 1)
                .toList()
        }
    }

    @Test
    fun `star 1`() {
        val pad = 120
        val patterns = record.lines()
            .map { Pattern(it, pad) }
            .filter { it.result == '#' }

        val list = generations(pad, initial, patterns)

        list.forEachIndexed { index, s ->
            val sum = lastGenerationSum(s, pad)
            println("$index, sum=$sum $s")
        }

        val sum = lastGenerationSum(list[20], pad)

        assertThat(sum).isEqualTo(3725)
    }

    @Test
    fun `star 2`() {
        val pad = 120
        val patterns = record.lines()
            .map { Pattern(it, pad) }
            .filter { it.result == '#' }

        val list = generations(pad, initial, patterns, true)

        list.forEachIndexed { index, s ->
            val sum = lastGenerationSum(s, pad)
            println("$index, sum=$sum $s")
        }

        val guessWork = { generation: Long ->
            val baseline = list[100]
            val calcSum100 = lastGenerationSum(baseline, pad)
            calcSum100 + baseline.count { it == '#' } * (generation - 100)
        }

        (90L..125L).forEach { gen ->
            assertThat(lastGenerationSum(list[gen.toInt()], pad)).isEqualTo(guessWork(gen))
        }

        assertThat(guessWork(50000000000L)).isEqualTo(3100000000293)
    }

    private val testInitial = "#..#.#..##......###...###"
    private val testRecord = """
...## => #
..#.. => #
.#... => #
.#.#. => #
.#.## => #
.##.. => #
.#### => #
#.#.# => #
#.### => #
##.#. => #
##.## => #
###.. => #
###.# => #
####. => #
""".trimIndent()

    val initial = ".#####.##.#.##...#.#.###..#.#..#..#.....#..####.#.##.#######..#...##.#..#.#######...#.#.#..##..#.#.#"
    val record = """
#..#. => .
##... => #
#.... => .
#...# => #
...#. => .
.#..# => #
#.#.# => .
..... => .
##.## => #
##.#. => #
###.. => #
#.##. => .
#.#.. => #
##..# => #
..#.# => #
..#.. => .
.##.. => .
...## => #
....# => .
#.### => #
#..## => #
..### => #
####. => #
.#.#. => #
.#### => .
###.# => #
##### => #
.#.## => .
.##.# => .
.###. => .
..##. => .
.#... => #
""".trimIndent()
}