package aoc

import org.assertj.core.api.Assertions.assertThat
import org.junit.Test

class Day10AdapterArray {
    @Test
    fun silverTest() {
        val zipped = connectAdapters(testInput)
        assertThat(zipped.count { it == 1 }).isEqualTo(7)
        assertThat(zipped.count { it == 3 }).isEqualTo(5)
    }

    @Test
    fun silverTest2() {
        val zipped = connectAdapters(testInput2)
        assertThat(zipped.count { it == 1 }).isEqualTo(22)
        assertThat(zipped.count { it == 3 }).isEqualTo(10)
    }


    @Test
    fun silver() {
        val zipped = connectAdapters(taskInput)
        val ones = zipped.count { it == 1 }
        val threes = zipped.count { it == 3 }
        assertThat(ones * threes).isEqualTo(2100)
    }

    private fun connectAdapters(input: List<Int>): List<Int> {
        return input
            .plus(0)
            .plus((input.maxOrNull() ?: 0) + 3)
            .sorted()
            .zipWithNext { l, r -> r - l }
            .apply { require(all { it <= 3 }) }
    }

    /**
     * (0), 1, 4, 5, 6, 7, 10, 11, 12, 15, 16, 19, (22)
     * (0), 1, 4, 5, 6, 7, 10, 12, 15, 16, 19, (22)
     * (0), 1, 4, 5, 7, 10, 11, 12, 15, 16, 19, (22)
     * (0), 1, 4, 5, 7, 10, 12, 15, 16, 19, (22)
     * (0), 1, 4, 6, 7, 10, 11, 12, 15, 16, 19, (22)
     * (0), 1, 4, 6, 7, 10, 12, 15, 16, 19, (22)
     * (0), 1, 4, 7, 10, 11, 12, 15, 16, 19, (22)
     * (0), 1, 4, 7, 10, 12, 15, 16, 19, (22)
     */
    @Test
    fun goldTest() {
        // 19,208
        // 2^3 × 7^4
        val combinations = countCombinations(testInput2)
        println("and we have $combinations combinations")
        assertThat(combinations).isEqualTo(19208)
    }

    @Test
    fun gold() {
        val combinations = countCombinations(taskInput)
        println("and we have $combinations combinations")
        assertThat(combinations).isEqualTo(16198260678656L)
    }

    private fun countCombinations(input: List<Int>): Long {
        val counts = input
            .plus(0)
            .sorted()
            .zipWithNext { l, r -> r - l }
            .joinToString(separator = "")
            .split("3")
            .filter { it.isNotEmpty() }
            .map { it.length }

        return counts.map { it.combinations() }.reduce(Long::times)
    }

    private fun Int.combinations(): Long {
        return when (this) {
            1 -> 1
            2 -> 2
            3 -> 4
            4 -> 7
            else -> error("No idea")
        }
    }

    private val testInput = """
        16
        10
        15
        5
        1
        11
        7
        19
        6
        12
        4
    """.trimIndent()
        .lines()
        .map { it.toInt() }

    private val testInput2 = """
28
33
18
42
31
14
46
20
48
47
24
23
49
45
19
38
39
11
1
32
25
35
8
17
7
9
4
2
34
10
3
    """.trimIndent()
        .lines()
        .map { it.toInt() }

    private val taskInput = """
        151
        94
        14
        118
        25
        143
        33
        23
        80
        95
        87
        44
        150
        39
        148
        51
        138
        121
        70
        69
        90
        155
        144
        40
        77
        8
        97
        45
        152
        58
        65
        63
        128
        101
        31
        112
        140
        86
        30
        55
        104
        135
        115
        16
        26
        60
        96
        85
        84
        48
        4
        131
        54
        52
        139
        76
        91
        46
        15
        17
        37
        156
        134
        98
        83
        111
        72
        34
        7
        108
        149
        116
        32
        110
        47
        157
        75
        13
        10
        145
        1
        127
        41
        53
        2
        3
        117
        71
        109
        105
        64
        27
        38
        59
        24
        20
        124
        9
        66
    """.trimIndent()
        .lines()
        .map { it.toInt() }
}


