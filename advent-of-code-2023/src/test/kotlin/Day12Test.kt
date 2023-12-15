import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.Test

class Day12Test {
  @Test
  fun `silver example 1`() {
    variationsOfBranchAtGroups("?###???????? 3,2,1")
        .second
        .shouldBe(
            listOf(
                ".###.##.#...",
                ".###.##..#..",
                ".###.##...#.",
                ".###.##....#",
                ".###..##.#..",
                ".###..##..#.",
                ".###..##...#",
                ".###...##.#.",
                ".###...##..#",
                ".###....##.#",
            ))
  }

  @Test
  fun `silver example 2`() {
    """???.### 1,1,3
.??..??...?##. 1,1,3
?#?#?#?#?#?#?#? 1,3,1,6
????.#...#... 4,1,1
????.######..#####. 1,6,5
?###???????? 3,2,1"""
        .lines()
        .sumOf { variationsOfWithDp(it) } shouldBe 21
  }

  @Test
  fun `silver test branch at groups`() {
    loadResource("Day12").lines().sumOf {
      variationsOfBranchAtGroups(it, countOnly = true).first
    } shouldBe 8180
  }

  @Test
  fun `silver test branch at groups count only`() {
    loadResource("Day12").lines().sumOf { variationsOfBranchAtGroupsCount(it) } shouldBe 8180
  }

  @Test
  fun `silver test branch at chars`() {
    loadResource("Day12").lines().sumOf { variationsOfBranchAtChars(it) } shouldBe 8180
  }

  @Test
  fun `silver test dp`() {
    loadResource("Day12").lines().sumOf { variationsOfWithDp(it) } shouldBe 8180
  }

  @Test
  fun `unfolding test`() {
    unfold(".# 1") shouldBe ".#?.#?.#?.#?.# 1,1,1,1,1"
    unfold("???.### 1,1,3") shouldBe
        "???.###????.###????.###????.###????.### 1,1,3,1,1,3,1,1,3,1,1,3,1,1,3"
  }

  private fun unfold(s: String): String {
    val (l, r) = s.split(" ")
    return (0 until 5).joinToString("?") { l } + " " + (0 until 5).joinToString(",") { r }
  }

  @Test
  fun `gold example`() {
    """???.### 1,1,3
.??..??...?##. 1,1,3
?#?#?#?#?#?#?#? 1,3,1,6
????.#...#... 4,1,1
????.######..#####. 1,6,5
?###???????? 3,2,1"""
        .lines()
        .map { unfold(it) }
        .sumOf { variationsOfWithDp(it) } shouldBe 525152
  }

  @Test
  @Disabled("5 secs")
  fun `gold example 2`() {
    """???.### 1,1,3
.??..??...?##. 1,1,3
?#?#?#?#?#?#?#? 1,3,1,6
????.#...#... 4,1,1
????.######..#####. 1,6,5
?###???????? 3,2,1"""
        .lines()
        .map { unfold(it) }
        .sumOf { variationsOfBranchAtGroupsCount(it) } shouldBe 525152
  }

  @Test
  fun `gold test`() {
    loadResource("Day12").lines().map { unfold(it) }.sumOf { variationsOfWithDp(it) } shouldBe
        620189727003627
  }

  private fun String.brokenGroups(): List<Int> {
    val str = this
    var count = 0
    return buildList {
      str.forEach {
        if (it == '#') {
          count++
        } else if (count > 0) {
          add(count)
          count = 0
        }
      }
      if (count > 0) {
        add(count)
      }
    }
  }

  /** This was my initial solution. */
  private fun variationsOfBranchAtGroups(
      input: String,
      countOnly: Boolean = false
  ): Pair<Int, List<String>> {
    val (template, list) = input.split(" ")
    val brokenSizes = list.split(",").map { it.toInt() }
    val totalBroken = brokenSizes.sum()

    // also we can have divide and conquer and multiply
    fun countSubVariants(
        remainingGaps: List<IntRange>,
        hypothesis: List<Int>,
        coveredLength: Int
    ): List<List<Int>> {
      // does not match - continue
      if (!matches(hypothesis, brokenSizes, template)) {
        return emptyList()
      }
      if (remainingGaps.isEmpty()) {
        return if (coveredLength == template.length - totalBroken) listOf(hypothesis)
        else emptyList()
      }
      return remainingGaps.first().flatMap { gapLength ->
        countSubVariants(remainingGaps.drop(1), hypothesis + gapLength, coveredLength + gapLength)
      }
    }

    // actually this has too many options which cannot work,
    // because we have a limited number of working springs (total - broken)
    // we can recalculate the n inside countSubVariants
    val n = template.length - totalBroken - brokenSizes.size + 2
    val gapsList: List<IntRange> =
        buildList<IntRange> {
          add(0..n)
          repeat(brokenSizes.size - 1) { add(1..n) }
          add(0..n)
        }
    val gapPermutations = countSubVariants(gapsList, emptyList(), 0)

    return if (countOnly) {
      gapPermutations.size to emptyList()
    } else {
      gapPermutations.size to gapPermutations.map { buildString(it, brokenSizes) }
    }
  }

  /** This was my initial solution. */
  private fun variationsOfBranchAtGroupsCount(
      input: String,
  ): Long {
    val (template, list) = input.split(" ")
    val brokenSizes = list.split(",").map { it.toInt() }
    val totalBroken = brokenSizes.sum()

    // also we can have divide and conquer and multiply
    fun countSubVariants(
        remainingGaps: List<IntRange>,
        hypothesis: List<Int>,
        coveredLength: Int
    ): Long {
      // does not match - continue
      if (!matches(hypothesis, brokenSizes, template)) {
        return 0
      }
      if (remainingGaps.isEmpty()) {
        return if (coveredLength == template.length - totalBroken) 1 else 0
      }
      return remainingGaps.first().sumOf { gapLength ->
        countSubVariants(remainingGaps.drop(1), hypothesis + gapLength, coveredLength + gapLength)
      }
    }

    // actually this has too many options which cannot work,
    // because we have a limited number of working springs (total - broken)
    // we can recalculate the n inside countSubVariants
    val n = template.length - totalBroken - brokenSizes.size + 2
    val gapsList: List<IntRange> =
        buildList<IntRange> {
          add(0..n)
          repeat(brokenSizes.size - 1) { add(1..n) }
          add(0..n)
        }
    return countSubVariants(gapsList, emptyList(), 0)
  }

  /** Ok here branching is done at every ?, and there are fast outs */
  private fun variationsOfBranchAtChars(input: String): Int {
    val (template, list) = input.split(" ")
    val brokenGroups = list.split(",").map { it.toInt() }

    fun matches(hypothesis: String) =
        template.zip(hypothesis) { a, b -> a == '?' || a == b }.all { it }

    fun countSubVariants(hypothesis: String, remainingInput: String): Int {

      hypothesis.brokenGroups().dropLast(1).let {
        if (it != brokenGroups.take(it.size)) {
          //  println("shortcut with groups")
          return 0
        }
      }

      return if (remainingInput.isEmpty()) {
        when {
          hypothesis.brokenGroups() != brokenGroups -> 0
          !matches(hypothesis) -> 0
          else -> 1
        }
      } else {
        when (remainingInput.first()) {
          '.' -> countSubVariants("$hypothesis.", remainingInput.drop(1))
          '#' -> countSubVariants("$hypothesis#", remainingInput.drop(1))
          '?' ->
              countSubVariants("$hypothesis.", remainingInput.drop(1)) +
                  countSubVariants("$hypothesis#", remainingInput.drop(1))
          else -> error("unknown char ${remainingInput.first()}")
        }
      }
    }

    return countSubVariants("", template)
  }

  data class Key(val remainingChars: String, val remainingGroups: List<Int>, val slider: Int)

  /**
   * The trick here is to traverse both the string the broken group simultaneously. This could have
   * been better with [variationsOfBranchAtGroupsCount] but I just can't look at this anymore.
   */
  private fun variationsOfWithDp(input: String): Long {
    val (template, list) = input.split(" ")
    val brokenGroups = list.split(",").map { it.toInt() }
    val cache = mutableMapOf<Key, Long>()

    fun countSubVariants(
        remainingChars: String,
        remainingGroups: List<Int>,
        consequentSharps: Int
    ): Long {
      fun point(): Long {
        // ongoing group of #
        return if (consequentSharps > 0) {
          if (remainingGroups.isNotEmpty() && remainingGroups.first() == consequentSharps) {
            //  now it is completed and loo green
            countSubVariants(remainingChars.drop(1), remainingGroups.drop(1), 0)
          } else {
            // no luck, keep going
            0
          }
        } else {
          // just the next point
          countSubVariants(remainingChars.drop(1), remainingGroups, 0)
        }
      }

      fun sharp(): Long {
        // start or continue group of #
        return countSubVariants(remainingChars.drop(1), remainingGroups, consequentSharps + 1)
      }

      return cache.getOrPut(Key(remainingChars, remainingGroups, consequentSharps)) {
        when (remainingChars.firstOrNull()) {
          null -> if (remainingGroups.isEmpty() && consequentSharps == 0) 1L else 0L
          '#' -> sharp()
          '.' -> point()
          '?' -> point() + sharp()
          '0' -> point()
          else -> error("unknown char ${remainingChars.first()}")
        }
      }
    }

    return countSubVariants(template + "0", brokenGroups, 0)
  }

  private fun matches(
      workingGroups: List<Int>,
      brokenGroups: List<Int>,
      template: String
  ): Boolean {
    if (workingGroups.sum().plus(brokenGroups.sum()) > template.length) return false
    var index = 0
    fun checkAndAdvance(char: Char): Char? {
      return template[index++].takeIf { it == '?' || it == char }
    }
    workingGroups.zip(brokenGroups + 0).forEach { (working, broken) ->
      repeat(working) { checkAndAdvance('.') ?: return false }
      repeat(broken) { checkAndAdvance('#') ?: return false }
    }
    return true
  }

  private fun buildString(workingGroups: List<Int>, brokenGroups: List<Int>): String {
    return buildString {
      workingGroups.zip(brokenGroups + 0).forEach { (working, broken) ->
        repeat(working) { append(".") }
        repeat(broken) { append("#") }
      }
    }
  }
}
