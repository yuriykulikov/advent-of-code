package quiz

import io.vavr.kotlin.toVavrList
import kotlinx.collections.immutable.persistentListOf
import org.assertj.core.api.Assertions.assertThat
import org.junit.Ignore
import org.junit.Test

/**
 * 2019.10.18 Meetup with Volodymyr
 */
class PrimeNumbers {
    inline fun Long.isEven() = this and 1 == 0L

    private val maxNumber = 3000000L
    private val expectedSum = 312471072265L

    @Test
    fun `Persistent fold`() {
        val sumOfPrimes = (2..maxNumber)
            .filterNot { it.isEven() } // even numbers are not prime
            .fold(persistentListOf(2L)) { primes, next ->
                when {
                    primes
                        .asSequence() // this makes a big difference, because .any terminates early
                        .takeWhile { it * it <= next }
                        .any { next.rem(it) == 0L } -> primes
                    else -> primes.add(next)// appends to the list
                }
            }.sum()

        assertThat(sumOfPrimes).isEqualTo(expectedSum)
    }

    @Ignore("Slow")
    @Test
    fun `Immutable fold`() {
        val sumOfPrimes = (2..maxNumber)
            .filterNot { it.isEven() } // even numbers are not prime
            .fold(listOf(2L)) { primes, next ->
                when {
                    primes
                        .asSequence()// again, big difference
                        .takeWhile { it * it <= next }
                        .any { next.rem(it) == 0L } -> primes
                    else -> primes.plus(next)// creates a new arrayList
                }
            }.sum()
        assertThat(sumOfPrimes).isEqualTo(expectedSum)
    }

    @Test
    fun `Mutable fold`() {
        val primes = mutableListOf(2L)
        (3..maxNumber)
            .filterNot { it.isEven() } // even numbers are not prime
            .forEach { next ->
                val newPrime = primes
                    .asSequence()
                    .takeWhile { it <= next.div(it) }
                    .none { next.rem(it) == 0L }

                if (newPrime) {
                    primes.add(next)
                }
            }
        assertThat(primes.sum()).isEqualTo(expectedSum)
    }

    @Ignore
    @Test
    fun `Vavr list - very slow`() {
        val sumOfPrimes = (2..maxNumber)
            .fold(listOf(2L).toVavrList()) { primes, next ->
                when {
                    next.isEven() -> primes // actually makes no difference
                    primes.takeWhile { it * it <= next }.any { next.rem(it) == 0L } -> primes
                    else -> primes.append(next)
                }
            }.sum().toInt()
        assertThat(sumOfPrimes).isEqualTo(expectedSum)
    }

    @Ignore("Slow")
    @Test
    fun `Vavr Array`() {
        val sumOfPrimes = (2..maxNumber)
            .fold(listOf(2L).toVavrList().toArray()) { primes, next ->
                when {
                    next.isEven() -> primes // actually makes no difference
                    primes.takeWhile { it * it <= next }.any { next.rem(it) == 0L } -> primes
                    else -> primes.append(next)
                }
            }.sum().toLong()
        assertThat(sumOfPrimes).isEqualTo(expectedSum)
    }

    @Ignore
    @Test
    fun `Straightforward approach - very slow`() {
        fun Long.isPrime() = (2..this / 2).none { this.rem(it) == 0L }

        val sumOfPrimeNumbers = 2 + (3..maxNumber)
            .filterNot { it.isEven() }
            .filter { it.isPrime() }
            .sum()

        assertThat(sumOfPrimeNumbers).isEqualTo(expectedSum)
    }

    @Test
    fun `Straightforward approach with a twist`() {
        fun Long.isPrime(): Boolean {
            // this is slower than the loop because of the sequence overhead
            return generateSequence(3L) { prev -> prev + 2 } // 3, 5, 7, 9, 11, 13, 15, 17..
                .takeWhile { it * it <= this } // the twist: no more than square root
                .none { this.rem(it) == 0L }
        }

        val sumOfPrimeNumbers = 2 + (3..maxNumber)
            .filterNot { it.isEven() }
            .filter { it.isPrime() }
            .sum()

        assertThat(sumOfPrimeNumbers).isEqualTo(expectedSum)
    }

    @Test
    fun `While loop`() {
        fun Long.isPrime(): Boolean {
            var i = 3
            //while (i <= this / i) is less readable in my opinion
            while (i * i <= this) {
                if (this.rem(i) == 0L) return false
                i += 2
            }
            return true
        }

        val sumOfPrimeNumbers = 2 + (3..maxNumber)
            .filterNot { it.isEven() }
            .filter { it.isPrime() }
            .sum()

        assertThat(sumOfPrimeNumbers).isEqualTo(expectedSum)
    }
}