package quiz

import kotlinx.collections.immutable.persistentListOf
import org.junit.Test

/**
 * 2019.10.30 Meetup with Volodymyr
 */
class ATMTest {

    data class Coin(val name: String, val value: Int) {
        override fun toString() = name
    }

    class Geldautomat(
        val coins: List<Coin> = listOf(
            Coin("dollar", 100),
            Coin("quarter", 25),
            Coin("dime", 10),
            Coin("nickel", 5),
            Coin("cent", 1)
        )
    ) {
        fun change(sum: Int): List<Pair<Coin, Int>> {
            return coins.fold(persistentListOf<Pair<Coin, Int>>() to sum)
            { (change, remainingMoney), nextCoinType ->
                val coinsOfThisType = remainingMoney.div(nextCoinType.value)
                val smallerThanCoinValue = remainingMoney.rem(nextCoinType.value)
                change.add(nextCoinType to coinsOfThisType) to smallerThanCoinValue
            }.first
        }
    }

    @Test
    fun `change for 163 Cents`() {
        Geldautomat().change(163)
            .also { println(it) }
    }
}