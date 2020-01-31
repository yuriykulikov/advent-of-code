import kotlinx.collections.immutable.*
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test

class Day6MemoryReallocations {

    @Test
    fun test() {
        val distinctDistributions = countDistinctDistributions(persistentListOf(0, 2, 6, 0)).count()
        assertThat(distinctDistributions).isEqualTo(5)
    }

    @Test
    fun star1() {
        val input = """10	3	15	10	5	15	5	15	9	2	5	8	5	2	3	6"""
            .split(" ", "\t")
            .map { it.toInt() }
            .toPersistentList()
        val distinctDistributions = countDistinctDistributions(input).count()
        assertThat(distinctDistributions).isEqualTo(14029)
    }

    @Test
    fun star2test() {
        val (seen, prev, end) = countDistinctDistributions(persistentListOf(0, 2, 6, 0)).last()
        val distinctDistributions = countDistinctDistributions(prev).count()
        assertThat(distinctDistributions).isEqualTo(4)
    }

    @Test
    fun star2() {
        val input = """10	3	15	10	5	15	5	15	9	2	5	8	5	2	3	6"""
            .split(" ", "\t")
            .map { it.toInt() }
            .toPersistentList()
        val (seen, duplicate, end) = countDistinctDistributions(input).last()
        val distinctDistributions = countDistinctDistributions(duplicate).count()
        assertThat(distinctDistributions).isEqualTo(2765)
    }

    private fun countDistinctDistributions(initial: PersistentList<Int>): Sequence<Triple<PersistentSet<PersistentList<Int>>, PersistentList<Int>, Boolean>> {
        return generateSequence(Triple(persistentSetOf(initial), initial, false)) { (seen, prev, end) ->

            var remainder = prev.max() ?: 0
            var index = prev.indexOf(remainder)
            var banks = prev.set(index, 0)

            // this can be optimized a lot, since we can just divide the amount of elements by the remained
            // and then we only have to distribute the rem
            while (remainder > 0) {
                index = if (index + 1 > prev.lastIndex) 0 else index + 1
                banks = banks.set(index, banks[index] + 1)
                remainder--
            }
            val distribution = banks

            val newSeen = seen.add(distribution)
            Triple(newSeen, distribution, newSeen.size == seen.size)
        }
            .takeWhile { (seen, last, end) ->
                !end
            }

    }
}