import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test

/** [Day 2: Day 2: Cube Conundrum](https://adventofcode.com/2023/day/2) */
class Day2Test {

  data class Game(
      val gameId: Int,
      val sets: List<CubeSet>,
  ) {
    fun max(color: String): Int {
      return sets.maxOf { it.cubes[color] ?: 0 }
    }

    val power: Int
      get() {
        return listOf("red", "green", "blue")
            .map { color -> sets.maxOf { it.cubes[color] ?: 0 } }
            .reduce { acc, next -> acc * next }
      }
  }

  data class CubeSet(
      val cubes: Map<String, Int>,
  )

  @Test
  fun `Silver - ids of possible games (example)`() {
    val input =
        """Game 1: 3 blue, 4 red; 1 red, 2 green, 6 blue; 2 green
Game 2: 1 blue, 2 green; 3 green, 4 blue, 1 red; 1 green, 1 blue
Game 3: 8 green, 6 blue, 20 red; 5 blue, 4 red, 13 green; 5 green, 1 red
Game 4: 1 green, 3 red, 6 blue; 3 green, 6 red; 3 green, 15 blue, 14 red
Game 5: 6 red, 1 blue, 3 green; 2 blue, 1 red, 2 green
         """

    val games = parseGames(input)
    // only 12 red cubes, 13 green cubes, and 14 blue cubes?

    val possibleCode =
        games
            .filter { game ->
              game.max("red") <= 12 && game.max("green") <= 13 && game.max("blue") <= 14
            }
            .sumOf { it.gameId }

    possibleCode shouldBe 8
  }

  @Test
  fun `Silver - ids of possible games`() {

    val games = parseGames(loadResource("Day2"))
    // only 12 red cubes, 13 green cubes, and 14 blue cubes?

    val possibleCode =
        games
            .filter { game ->
              game.max("red") <= 12 && game.max("green") <= 13 && game.max("blue") <= 14
            }
            .sumOf { it.gameId }

    possibleCode shouldBe 2061
  }

  @Test
  fun `Gold - sum of set powers (example)`() {
    val input =
        """
            Game 1: 3 blue, 4 red; 1 red, 2 green, 6 blue; 2 green
            Game 2: 1 blue, 2 green; 3 green, 4 blue, 1 red; 1 green, 1 blue
            Game 3: 8 green, 6 blue, 20 red; 5 blue, 4 red, 13 green; 5 green, 1 red
            Game 4: 1 green, 3 red, 6 blue; 3 green, 6 red; 3 green, 15 blue, 14 red
            Game 5: 6 red, 1 blue, 3 green; 2 blue, 1 red, 2 green
        """
            .trimIndent()

    val games = parseGames(input)

    games.sumOf { it.power } shouldBe 2286
  }

  @Test
  fun `Gold - sum of set powers`() {
    val games = parseGames(loadResource("Day2"))

    games.sumOf { it.power } shouldBe 72596
  }

  private fun parseGames(input: String): List<Game> {
    return input
        .trim()
        .lines()
        .map { it.substringAfter("Game ").split(": ", limit = 2) }
        .map { (gameId, gameStr) ->
          val sets =
              gameStr.split("; ").map { setStr ->
                val colorToCount =
                    setStr
                        .split(", ")
                        .map { it.split(" ") }
                        .associate { (count, color) -> color to count.toInt() }
                CubeSet(colorToCount)
              }
          Game(gameId.toInt(), sets)
        }
  }
}
