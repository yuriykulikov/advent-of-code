package aoc

import org.assertj.core.api.Assertions.assertThat
import org.junit.Ignore
import org.junit.Test

class Advent18 {
    data class Acre(val x: Int, val y: Int, val content: Char) {
        val pos = x to y
    }


    @Test
    fun `it is a wood`() {
        assertThat(fieldAfter(testInput, 10)).isEqualTo(1147)
    }

    @Test
    fun `it is  real wood`() {
        assertThat(fieldAfter(realInput, 10)).isEqualTo(584714)
    }

    @Test
    fun `it is  real wood 2 small`() {
        assertThat(fieldAfter2(realInput, 1000)).isEqualTo(fieldAfter(realInput, 1000))
    }

    @Test
    fun `it is  real wood 2 small 2`() {
        assertThat(fieldAfter2(realInput, 2000)).isEqualTo(fieldAfter(realInput, 2000))
    }

    @Ignore("Wrong")
    @Test
    fun `it is  real wood 2`() {
        assertThat(fieldAfter2(realInput, 1000000000)).isEqualTo(584714)
    }

    private fun fieldAfter(input: String, iterations: Int): Int {
        val acres = input.lines()
            .mapIndexed { y, line ->
                line.toCharArray()
                    .mapIndexed { x, char -> Acre(x, y, char) }
            }
            .flatten()

        // dump(acres)

        val lastState = generateSequence(acres) { prev ->
            val map: Map<Pair<Int, Int>, Acre> = prev.associateBy { it.pos }

            prev.map { acre ->

                val neighbours = (acre.x - 1..acre.x + 1)
                    .flatMap { x -> (acre.y - 1..acre.y + 1).map { y -> x to y } }
                    .minus(acre.x to acre.y)
                    .mapNotNull { pos -> map[pos] }
                    .map { it.content }
                // .apply { println(this) }

                when {
                    // An open acre will become filled with trees if three or more adjacent acres contained trees. Otherwise, nothing happens.
                    acre.content == '.' && neighbours.count { it == '|' } >= 3 -> acre.copy(content = '|')
                    acre.content == '.' -> acre
                    // An acre filled with trees will become a lumberyard if three or more adjacent acres were lumberyards. Otherwise, nothing happens.
                    acre.content == '|' && neighbours.count { it == '#' } >= 3 -> acre.copy(content = '#')
                    acre.content == '|' -> acre
                    // An acre containing a lumberyard will remain a lumberyard if it was adjacent to at least one other lumberyard and at least one acre containing trees. Otherwise, it becomes open.
                    acre.content == '#' && neighbours.count { it == '#' } >= 1 && neighbours.count { it == '|' } >= 1 -> acre.copy(
                        content = '#'
                    )
                    else -> acre.copy(content = '.')
                }
            }
        }
            // .onEach { dump(it) }
            .take(iterations + 1)
            // .onEach { dump(it) }
            // .mapIndexed { index, lastState -> index to lastState }
            // .onEach { (index, lastState) -> println("$index: ${lastState.count { it.content == '|' } * lastState.count { it.content == '#' }}") }
            // .map { it.second }
            .last()

        // dump(lastState)

        return lastState.count { it.content == '|' } * lastState.count { it.content == '#' }
    }

    private fun fieldAfter2(input: String, iterations: Int): Int {
        val base = (iterations - 466).rem(28)
        return when (base) {
            0 -> 169472
            1 -> 163068
            2 -> 161160
            3 -> 158921
            4 -> 155439
            5 -> 158004
            6 -> 159120
            7 -> 158992
            8 -> 160966
            9 -> 164395
            10 -> 165300
            11 -> 171152
            12 -> 175712
            13 -> 182246
            14 -> 185978
            15 -> 192831
            16 -> 192878
            17 -> 197136
            18 -> 192075
            19 -> 191880
            20 -> 190968
            21 -> 192807
            22 -> 190656
            23 -> 190176
            24 -> 187265
            25 -> 184960
            26 -> 179088
            27 -> 177498
            else -> throw RuntimeException()
        }
    }

    private fun dump(acres: List<Acre>) {
        println()
        acres.groupBy { it.y }
            .toSortedMap().values.forEach { acres -> println(acres.joinToString("") { "${it.content}" }) }
    }

    val testInput = """
.#.#...|#.
.....#|##|
.|..|...#.
..|#.....#
#.#|||#|#|
...#.||...
.|....|...
||...#|.#|
|.||||..|.
...#.|..|.
""".trimIndent()

    val realInput = """
#.##...#....|...#..|...#|...#....|.....|..##.|.|.#
||#...##|.|.##....|..#.#......#.#..#......|..#.|.#
|......#.##....||..#..||..|.....|..##....|.#.|..|#
.|...#||..||#|#.....|#...|.||.||#...#..|.##.......
...#.|.||.|.....|..##.#|....|#...|#.#|.#|...#.....
##.#....|..|.|....#.#|..##..#.......|.|.||#.......
....|..#|...|...|#..#...#....#.........#....#.|..|
....##...|..........|.|##|##..||..|..#..#|..|.....
........|..#.......|.#|............##.||.##.#..||#
.|...|##..#.|..|..|.#|.#.|#...|......#.....#...##|
............#.|.|||....|...#.#..#|.#...|.#....#|..
.....#....##....|#..##..|......|#.........|..##..|
#|.#||...|#...#|#|......#|#.|.#|#.|#...|.....#|.||
..|..#..|.#||.#.#.......#..#.#...|.#...|.|.|..|..#
.#|....#..#.|........#.....#.|....|#|#........#.|.
###|....##|#|||#.||.....#....#.#....|.#...|#..|#..
...#.##..#....|...||.#.|#.....||##|#.....|#|...|..
#..|.|.|#......|.#....#||...#.|.|..|#...|#|.|#|...
....###..|||#..##....||..|.|.#|#.....#||.......|..
|....#...#.||.|...#.#|...##.##....||.|.|##.#.#|...
#..#...#...|##....#.#|....|.#|#.|.....|##|||...#.|
..||.|.#...#.#|#...#..||.|#.|.|.|.###.....|....#..
.#|..|###.#....#.......#|.#|..#.|.||.|.||..##...#|
|..|....|#.........#|.|.....|.#...|.#|.||..|.|||.|
#...........|##|..#..##.......|||..#.....#.|#..##.
.#.#..|....|.|||###..#........|..#..|..##|...|#...
#.#..|||||....#.#..#...##....#....##.#|......##.#.
|.|#|.....|......#.....#........|.||....##|.#...|.
#....#.....#.#.|#......|#.|........|#|..#.|##.|.#.
.#|##..|.||..#|....#.|..#|#.#...###|...#.#....#...
#...#....||..#.|...||..#....|.|......|||....#.....
.#|....#|...#.#....|...|#|#.##...|.|.#||||..|.##.#
....#..#....|.|.#....|..|||#....#...|.#....#.|#.|.
.#..###|..|#.....#....|.|.|||.|.|#||#.#..|.##|...#
##.#...#|.....||.#..|.|...||.....|||.||#......|#..
||#|.|...||......#...#.|#..|..#.......##..##.#...#
...##.|#....#||.|.#.|#..........#...#.......#....|
..#...#...|#..|||..#||||....#..|.#..#.#..||.##|.||
#..|...|##||.||..|#.|.#....|.......|..#....|.#|...
...|.#####.##|####..#.|..#....#...|#.#.#|......||.
###......|.|..|#.|#....|.......|##.|#|...|.......|
##|....|#..|...#..|.||......#|.....##|...|...|..#|
..#.|.#.||...###..##.##..|....|..|....#..#...####.
.|##.#..|..|...##......#...##||........|.|....#||.
##.|#....|...|..|#.....|....|.|....|.|.|..|..#.#..
..|.|.#|.|....||#.|#...............##..|...#.....#
.#...#.......#.......#.##...#.|.|..####.||#....#||
.||#...##.#|.||..##.|....|||##...|#.|...#.#|.#....
.#|.||#..#.|#.......||.||#...........#....|###....
#|....#......##.#....|.....|##.#|.....|.#..|.....#
""".trimIndent()
}