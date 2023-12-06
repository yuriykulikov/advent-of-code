import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test

/** --- Day 7: Camel Cards --- */
class Day7Test {
  private val testInput =
      """
        32T3K 765
        T55J5 684
        KK677 28
        KTJJT 220
        QQQJA 483
    """
          .trimIndent()

  private val silverComparator =
      compareBy<CardAndBet> { (card, bet) -> card.handType() }
          .thenByDescending { (card, bet) -> card.secondary() }

  private val comparatorWithJokers =
      compareBy<CardAndBet> { (card, bet) -> card.handTypeWithJoker() }
          .thenByDescending { (card, bet) -> card.secondaryWithJoker() }

  data class CardAndBet(val card: String, val bet: Int, val rank: Int = 0)

  @Test
  fun `silver test (example)`() {
    val rankCards = rankCards(testInput)

    "32T3K".handType() shouldBe 2

    rankCards.shouldBe(
        listOf(
            CardAndBet(card = "32T3K", bet = 765, rank = 1),
            CardAndBet(card = "KTJJT", bet = 220, rank = 2),
            CardAndBet(card = "KK677", bet = 28, rank = 3),
            CardAndBet(card = "T55J5", bet = 684, rank = 4),
            CardAndBet(card = "QQQJA", bet = 483, rank = 5),
        ))

    rankCards.sumOf { it.bet * it.rank } shouldBe 6440
  }

  @Test
  fun `silver test`() {
    val cards = rankCards(loadResource("Day7"))
    cards.sumOf { it.bet * it.rank } shouldBe 250951660
  }

  @Test
  fun `gold test (example)`() {
    val rankCards = rankCards(testInput, comparatorWithJokers)

    rankCards.shouldBe(
        listOf(
            CardAndBet(card = "32T3K", bet = 765, rank = 1),
            CardAndBet(card = "KK677", bet = 28, rank = 2),
            CardAndBet(card = "T55J5", bet = 684, rank = 3),
            CardAndBet(card = "QQQJA", bet = 483, rank = 4),
            CardAndBet(card = "KTJJT", bet = 220, rank = 5),
        ))

    rankCards.sumOf { it.bet * it.rank } shouldBe 5905
  }

  @Test
  fun `gold test`() {
    val cards = rankCards(loadResource("Day7"), comparatorWithJokers)
    cards.sumOf { it.bet * it.rank } shouldBe 251481660
  }

  private fun rankCards(input: String, comparator: Comparator<CardAndBet> = silverComparator) =
      parse(input)
          .sortedWith(comparator)
          .mapIndexed { index, cardAndBet -> cardAndBet.copy(rank = index + 1) }
          .toList()

  private fun parse(input: String): Sequence<CardAndBet> =
      input
          .lines()
          .asSequence()
          .filter { it.isNotEmpty() }
          .map { it.trim().split(" ") }
          .map { (card, bet) -> CardAndBet(card, bet.toInt()) }

  private fun String.handType(): Int {
    val sizes: List<Int> = toCharArray().groupBy { it }.map { it.value.size }.sortedDescending()
    return when (sizes) {
      // Five of a kind
      listOf(5) -> 7
      // Four of a kind
      listOf(4, 1) -> 6
      // Full house
      listOf(3, 2) -> 5
      // Three of a kind
      listOf(3, 1, 1) -> 4
      // Two pair
      listOf(2, 2, 1) -> 3
      // One pair
      listOf(2, 1, 1, 1) -> 2
      // High card
      else -> 1
    }
  }

  private fun String.secondary(): String {
    return toCharArray().joinToString("") { char ->
      when (char) {
        'A' -> "a"
        'K' -> "b"
        'Q' -> "c"
        'J' -> "d"
        'T' -> "e"
        '9' -> "f"
        '8' -> "g"
        '7' -> "h"
        '6' -> "i"
        '5' -> "j"
        '4' -> "k"
        '3' -> "l"
        '2' -> "m"
        else -> error("Unexpected char: $char")
      }
    }
  }

  enum class HandType(val rank: Int) {
    FiveOfAKind(7),
    FourOfAKind(6),
    FullHouse(5),
    ThreeOfAKind(4),
    TwoPair(3),
    OnePair(2),
    HighCard(1),
    ;

    fun adjustForJokers(jokers: Int): HandType {
      return when (this) {
        FiveOfAKind -> FiveOfAKind
        FourOfAKind ->
            when (jokers) {
              0 -> FourOfAKind
              1 -> FiveOfAKind
              else -> error("Unexpected jokers: $jokers")
            }
        FullHouse ->
            when (jokers) {
              0 -> FullHouse
              1 -> FourOfAKind
              2 -> FiveOfAKind
              else -> error("Unexpected jokers: $jokers")
            }
        ThreeOfAKind ->
            when (jokers) {
              0 -> ThreeOfAKind
              1 -> FourOfAKind
              2 -> FiveOfAKind
              else -> error("Unexpected jokers: $jokers")
            }
        TwoPair ->
            when (jokers) {
              0 -> TwoPair
              1 -> FullHouse
              2 -> FourOfAKind
              3 -> FiveOfAKind
              else -> error("Unexpected jokers: $jokers")
            }
        OnePair ->
            when (jokers) {
              0 -> OnePair
              1 -> ThreeOfAKind
              2 -> FourOfAKind
              3 -> FiveOfAKind
              else -> error("Unexpected jokers: $jokers")
            }
        HighCard ->
            when (jokers) {
              0 -> HighCard
              1 -> OnePair
              2 -> ThreeOfAKind
              3 -> FourOfAKind
              4 -> FiveOfAKind
              5 -> FiveOfAKind
              else -> error("Unexpected jokers: $jokers")
            }
      }
    }
  }

  private fun String.handTypeWithJoker(): Int {
    val toCharArray = toCharArray()
    val jokers = toCharArray.count { it == 'J' }
    val sizes: List<Int> =
        toCharArray
            .filterNot { it == 'J' }
            .groupBy { it }
            .map { it.value.size }
            .filter { it > 1 }
            .sortedDescending()
    return when (sizes) {
          listOf(5) -> HandType.FiveOfAKind
          listOf(4) -> HandType.FourOfAKind
          listOf(3, 2) -> HandType.FullHouse
          listOf(3) -> HandType.ThreeOfAKind
          listOf(2, 2) -> HandType.TwoPair
          listOf(2) -> HandType.OnePair
          else -> HandType.HighCard
        }
        .adjustForJokers(jokers)
        .rank
  }

  private fun String.secondaryWithJoker(): String {
    return toCharArray().joinToString("") { char ->
      when (char) {
        'A' -> "a"
        'K' -> "b"
        'Q' -> "c"
        'T' -> "e"
        '9' -> "f"
        '8' -> "g"
        '7' -> "h"
        '6' -> "i"
        '5' -> "j"
        '4' -> "k"
        '3' -> "l"
        '2' -> "m"
        'J' -> "o"
        else -> error("Unexpected char: $char")
      }
    }
  }
}
