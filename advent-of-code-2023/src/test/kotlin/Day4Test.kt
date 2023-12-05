import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test

class Day4Test {
  data class Card(
      val index: Int,
      val left: List<Int>,
      val right: List<Int>,
  ) {
    val winning = left.intersect(right.toSet()).size
    val score: Int = if (winning == 0) 0 else 1.shl(winning - 1)
    val follow: Int = left.intersect(right.toSet()).size

    override fun toString(): String {
      return "$left | $right - $score"
    }
  }

  @Test
  fun `silver test (example)`() {
    val input =
        """
            Card 1: 41 48 83 86 17 | 83 86  6 31 17  9 48 53
            Card 2: 13 32 20 16 61 | 61 30 68 82 17 32 24 19
            Card 3:  1 21 53 59 44 | 69 82 63 72 16 21 14  1
            Card 4: 41 92 73 84 69 | 59 84 76 51 58  5 54 83
            Card 5: 87 83 26 28 32 | 88 30 70 12 93 22 82 36
            Card 6: 31 18 13 56 72 | 74 77 10 23 35 67 36 11
        """
            .trimIndent()

    val cards = parseCards(input)
    cards.forEach { println(it) }
    cards.sumOf { it.score } shouldBe 13
  }

  @Test
  fun `silver test`() {
    val cards = parseCards(loadResource("Day4"))
    cards.sumOf { it.score } shouldBe 21213
  }

  @Test
  fun `gold test (example)`() {
    val input =
        """
            Card 1: 41 48 83 86 17 | 83 86  6 31 17  9 48 53
            Card 2: 13 32 20 16 61 | 61 30 68 82 17 32 24 19
            Card 3:  1 21 53 59 44 | 69 82 63 72 16 21 14  1
            Card 4: 41 92 73 84 69 | 59 84 76 51 58  5 54 83
            Card 5: 87 83 26 28 32 | 88 30 70 12 93 22 82 36
            Card 6: 31 18 13 56 72 | 74 77 10 23 35 67 36 11
        """
            .trimIndent()

    val cards = parseCards(input)
    countAll(cards) shouldBe 30
  }

  @Test
  fun `gold test`() {
    val cards = parseCards(loadResource("Day4"))
    countAll(cards) shouldBe 8549735
  }

  private fun countAll(cards: List<Card>): Int {
    val cache = mutableMapOf<Card, Int>()
    fun expand(card: Card): Int {
      if (card.follow == 0) return 1
      return cache.getOrPut(card) {
        // getOrOut allows local return!
        1 + (card.index + 1..card.index + card.follow).sumOf { expand(cards[it]) }
      }
    }
    return cards.sumOf { card -> expand(card) }
  }

  private fun parseCards(input: String) =
      input
          .lines()
          .filter { it.isNotEmpty() }
          .mapIndexed { index, line ->
            val (left, right) =
                line.substringAfter(":").split("|").map { half ->
                  half.split(" ").mapNotNull { it.trim().toIntOrNull() }
                }

            Card(index, left, right)
          }
}
