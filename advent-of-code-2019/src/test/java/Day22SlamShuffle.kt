import extensions.kotlin.scan
import kotlinx.collections.immutable.PersistentList
import kotlinx.collections.immutable.mutate
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toPersistentList
import org.assertj.core.api.Assertions.assertThat
import org.junit.Ignore
import org.junit.Test
import kotlin.math.absoluteValue

/**
 * I did not solve the gold star :(
 */
class Day22SlamShuffle {

    data class Technique(
        val name: String,
        val arg: Int
    )

    private fun parse(input: String): List<Technique> {
        return input.lines()
            .filter { it.isNotEmpty() }
            .map { line ->
                when {
                    line.startsWith("deal with increment") ->
                        Technique(
                            "increment",
                            line.substringAfterLast(" ").toInt()
                        )
                    line.startsWith("cut") ->
                        Technique(
                            "cut",
                            line.substringAfterLast(" ").toInt()
                        )
                    line.startsWith("deal") ->
                        Technique(
                            "deal",
                            0
                        )
                    else -> throw Exception("Dummkopf")
                }
            }
    }

    @Test
    fun test10() {
        with(test0) { assertThat(shuffle(first)).isEqualTo(second.split(" ").map { it.toInt() }) }
        with(test01) { assertThat(shuffle(first)).isEqualTo(second.split(" ").map { it.toInt() }) }
        with(test1) { assertThat(shuffle(first)).isEqualTo(second.split(" ").map { it.toInt() }) }
        with(test2) { assertThat(shuffle(first)).isEqualTo(second.split(" ").map { it.toInt() }) }
        with(test3) { assertThat(shuffle(first)).isEqualTo(second.split(" ").map { it.toInt() }) }
    }

    @Test
    fun silver() {
        val seed = IntArray(10007) { i -> i }.toList().toPersistentList()
        val shuffle = shuffle(puzzle, seed)
        val answer = shuffle
            .indexOf(2019)

        assertThat(answer).isEqualTo(1234)
        println(shuffle)
    }

    @Ignore("Wrong")
    @Test
    fun silver2() {
        val seed = IntArray(10006 / 7 + 3 + 1) { i -> i }.toList().toPersistentList()
        val shuffle = shuffle(puzzle, seed)
        val answer = shuffle
            .indexOf(2019 / 7 + 3)

        assertThat(answer).isEqualTo(1234)
        println(shuffle)
    }

    @Ignore
    @Test
    fun gold() {
        val set = mutableSetOf<Int>()
        val shuffled = generateSequence(
            IntArray(10007) { i -> i }.toList().toPersistentList()
                .toPersistentList()
        ) { acc ->
            shuffle(puzzle, acc)
        }
            .drop(1)
            .onEach { acc ->
                val nextYear = acc[2020]
                println(nextYear)
            }.first {
                val duplicate = it[2020] in set
                set.add(it[2020])
                duplicate
            }
    }

    fun <T : Any> Sequence<T>.findDuplicate(): Sequence<Pair<Set<T>, T?>> {
        return scan<T, Pair<Set<T>, T?>>(setOf<T>() to null) { (collected, _), next ->
            if (next in collected) {
                collected to next
            } else {
                collected.plus(next) to null
            }
        }
    }

    //cards 119315717514047
//times 101741582076661
    @Ignore("Takes too long")
    @Test
    fun testa() {
        val (collected, duplicate) = generateSequence(
            IntArray(10006 + 1) { i -> i }.toList().toPersistentList()
        ) { acc ->
            shuffle(puzzle, acc)
        }
            .take(5003 + 1) // same as 0
            .map { it[2020] }
            .findDuplicate()
            .first { (collected, duplicate) -> duplicate != null }

        println(duplicate)
    }

    val primes =
        listOf(
            1009,
            1013,
            1019,
            1021,
            1031,
            1033,
            1039,
            1049,
            1051,
            1061,
            1063,
            1069,
            1087,
            1091,
            1093,
            1097,
            1103,
            1109,
            1117,
            1123,
            1129,
            1151,
            1153,
            1163,
            1171,
            1181,
            1187,
            1193,
            1201,
            1213,
            1217,
            1223,
            1229,
            1231,
            1237,
            1249,
            1259,
            1277,
            1279,
            1283,
            1289,
            1291,
            1297,
            1301,
            1303,
            1307,
            1319,
            1321,
            1327,
            1361,
            1367,
            1373,
            1381,
            1399,
            1409,
            1423,
            1427,
            1429,
            1433,
            1439,
            1447,
            1451,
            1453,
            1459,
            1471,
            1481,
            1483,
            1487,
            1489,
            1493,
            1499,
            1511,
            1523,
            1531,
            1543,
            1549,
            1553,
            1559,
            1567,
            1571,
            1579,
            1583,
            1597,
            1601,
            1607,
            1609,
            1613,
            1619,
            1621,
            1627,
            1637,
            1657,
            1663,
            1667,
            1669,
            1693,
            1697,
            1699,
            1709,
            1721,
            1723,
            1733,
            1741,
            1747,
            1753,
            1759,
            1777,
            1783,
            1787,
            1789,
            1801,
            1811,
            1823,
            1831,
            1847,
            1861,
            1867,
            1871,
            1873,
            1877,
            1879,
            1889,
            1901,
            1907,
            1913,
            1931,
            1933,
            1949,
            1951,
            1973,
            1979,
            1987,
            1993,
            1997,
            1999,
            2003,
            2011,
            2017,
            2027,
            2029,
            2039,
            2053,
            2063,
            2069,
            2081,
            2083,
            2087,
            2089,
            2099
        )

    @Ignore("Takes too long")
    @Test
    fun test509() {
        (1000..10006).forEach { kak ->
            val (collected, duplicate) = generateSequence(
                IntArray(kak + 1) { i -> i }.toList().toPersistentList()
            ) { acc ->
                shuffle(puzzle, acc)
            }
                .take(kak + 1) // same as 0
                .map { it[1] }
                .findDuplicate()
                .first { (collected, duplicate) -> duplicate != null }

            println("$kak (${kak in primes}): $duplicate after ${collected.size}")
        }
    }


    private fun shuffle(
        operators: String,
        seed: PersistentList<Int> = persistentListOf(0, 1, 2, 3, 4, 5, 6, 7, 8, 9)
    ): PersistentList<Int> {

        return parse(operators).fold(seed) { acc, op ->
            when {
                op.name == "increment" -> {
                    val offset = op.arg
                    acc.mutate { mutator ->
                        mutator[0] = acc[0]
                        // 0 1 2 3 4 5 6 7 8 9
                        // 0 7 4 1 8 5 2 9 6 3
                        acc.forEachIndexed { index, card ->
                            var mutatorIndex = offset * index
                            while (mutatorIndex > acc.lastIndex) {
                                mutatorIndex -= acc.size
                            }
                            // println("index in accumulator is $index Index in mutator is $mutatorIndex")
                            mutator[mutatorIndex] = card
                        }
                    }
                }
                op.name == "cut" && op.arg < 0 -> acc.takeLast(op.arg.absoluteValue) + acc.dropLast(op.arg.absoluteValue)
                op.name == "cut" -> acc.drop(op.arg) + acc.take(op.arg)
                else -> acc.asReversed()
            }.toPersistentList()
            //.also { println("Out: $it") }
        }
    }

    private val test0 = """
deal into new stack
deal into new stack
""" to "0 1 2 3 4 5 6 7 8 9"

    private val test01 = """
deal with increment 3
""" to "0 7 4 1 8 5 2 9 6 3"

    private val test1 = """
deal with increment 7
deal into new stack
deal into new stack
""" to "0 3 6 9 2 5 8 1 4 7"

    private val test2 = """
cut 6
deal with increment 7
deal into new stack
""" to "3 0 7 4 1 8 5 2 9 6"

    private val test3 = """
deal with increment 7
deal with increment 9
cut -2
""" to "6 3 0 7 4 1 8 5 2 9"

    private val puzzle = """
deal with increment 55
cut -6984
deal into new stack
cut -2833
deal with increment 75
cut 2488
deal with increment 54
cut 9056
deal with increment 52
cut -2717
deal with increment 4
deal into new stack
cut -852
deal with increment 21
cut -3041
deal with increment 38
cut -6871
deal into new stack
deal with increment 32
cut 988
deal with increment 29
deal into new stack
deal with increment 68
cut 5695
deal with increment 36
cut -27
deal with increment 33
deal into new stack
cut -1306
deal with increment 30
cut -4033
deal with increment 28
cut -442
deal into new stack
deal with increment 30
cut -6295
deal with increment 56
cut -4065
deal into new stack
cut 5275
deal with increment 64
cut 9747
deal into new stack
deal with increment 63
cut -3772
deal with increment 61
deal into new stack
cut 1021
deal with increment 73
deal into new stack
deal with increment 7
cut -1232
deal with increment 52
cut -3439
deal with increment 31
cut 1128
deal into new stack
deal with increment 55
deal into new stack
deal with increment 39
cut -3424
deal with increment 11
deal into new stack
cut 4139
deal with increment 15
deal into new stack
cut 5333
deal with increment 16
cut -6787
deal with increment 39
cut -5817
deal into new stack
deal with increment 62
cut -2704
deal with increment 64
deal into new stack
deal with increment 70
cut 3436
deal with increment 65
cut -8686
deal with increment 22
cut -6190
deal with increment 13
cut -100
deal into new stack
cut -619
deal into new stack
cut 3079
deal with increment 53
cut 1725
deal with increment 19
cut 3440
deal with increment 64
cut 8578
deal with increment 5
cut 2341
deal with increment 45
cut 2217
deal with increment 13
deal into new stack
    """.trimIndent()
}
