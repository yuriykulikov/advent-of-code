import io.kotest.matchers.collections.shouldContain
import io.kotest.matchers.shouldBe
import kotlin.math.max
import kotlinx.collections.immutable.persistentListOf
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.Test

/**
 * --- Day 5: If You Give A Seed A Fertilizer ---
 *
 * This is a fun one.
 *
 * TODO An elegant solution would be to check only the points of interest, for example, without
 * transformations we can only check range starts. Each transformation layer splits the ranges, but
 * it is still a small amount of ranges to check. But for now I am too lazy to implement it.
 */
class Day5Test {
  data class Almanac(
      val seeds: List<Long>,
      val mappings: List<Mapping>,
  )

  data class Mapping(
      val title: String,
      val ranges: List<ElvRange>,
  ) {
    fun convert(source: Long): Long {
      return ranges.firstNotNullOfOrNull { range -> range.convert(source) } ?: source
    }

    fun inverseConvert(target: Long): Long {
      return ranges.firstNotNullOfOrNull { range -> range.inverseConvert(target) } ?: target
    }
  }

  data class ElvRange(
      val destination: Long,
      val source: Long,
      val length: Long,
  ) {
    fun convert(sourceValue: Long): Long? {
      return if (sourceValue in source until source + length) {
        val offset = destination - source
        sourceValue + offset
      } else {
        null
      }
    }

    fun inverseConvert(targetValue: Long): Long? {
      return if (targetValue in destination until destination + length) {
        val offset = source - destination
        targetValue + offset
      } else {
        null
      }
    }
  }

  @Test
  fun `Test input parsing test`() {
    parse(testInput)
        .mappings
        .shouldContain(
            Mapping("seed-to-soil map", listOf(ElvRange(50, 98, 2), ElvRange(52, 50, 48))))
  }

  @Test
  fun `silver test (example)`() {
    val almanac = parse(testInput)
    almanac.seeds.minOfOrNull { seed ->
      almanac.mappings.fold(seed) { prev, mapping -> mapping.convert(prev) }
    } shouldBe 35
  }

  @Test
  fun `silver test`() {
    val almanac = parse(loadResource("Day5"))
    almanac.seeds.minOfOrNull { seed ->
      almanac.mappings.fold(seed) { prev, mapping -> mapping.convert(prev) }
    } shouldBe 157211394
  }

  fun Almanac.seedRanges(): List<LongRange> =
      seeds
          .windowed(2, 2)
          .asSequence()
          .map { (l, r) -> l..l + r }
          .fold(LongRangeSet()) { acc, next -> acc + next }
          .ranges

  @Test
  fun `gold test (example)`() {
    val almanac = parse(testInput)
    almanac.seedRanges().flatten().minOfOrNull { seed ->
      almanac.mappings.fold(seed) { prev, mapping -> mapping.convert(prev) }
    } shouldBe 46
  }

  @Test
  @Disabled("3:35 on mac")
  fun `gold test`() = runBlocking {
    val almanac = parse(loadResource("Day5"))

    almanac
        .seedRanges()
        .map { range ->
          async {
            withContext(Dispatchers.IO) {
              range.minOf { seed ->
                almanac.mappings.fold(seed) { prev, mapping -> mapping.convert(prev) }
              }
            }
          }
        }
        .awaitAll()
        .min() shouldBe 50855035
  }

  @Test
  fun `gold test inverse (example)`() {
    val almanac = parse(testInput)
    val ranges = almanac.seedRanges()
    val reversed = almanac.mappings.reversed()

    (0..Long.MAX_VALUE).first { location ->
      val inverse = reversed.fold(location) { prev, mapping -> mapping.inverseConvert(prev) }
      ranges.any { range -> inverse in range }
    } shouldBe 46
  }

  @Test
  fun `gold test inverse`() = runBlocking {
    val almanac = parse(loadResource("Day5"))
    val ranges = almanac.seedRanges()
    val reversed = almanac.mappings.reversed()

    (0..Long.MAX_VALUE).first { location ->
      val inverse = reversed.fold(location) { prev, mapping -> mapping.inverseConvert(prev) }
      ranges.any { range -> inverse in range }
    } shouldBe 50855035
  }

  private fun parse(input: String): Almanac {
    val split = input.trim().split("\n\n")
    val seeds = split.first().substringAfter(": ").split(" ").map { it.toLong() }
    val mappings =
        split.drop(1).map { mapStr ->
          val title = mapStr.substringBefore(":")
          val ranges =
              mapStr.substringAfter(":\n").lines().map { line ->
                val (dest, source, length) = line.split(" ").map { it.toLong() }
                ElvRange(dest, source, length)
              }
          Mapping(title, ranges)
        }
    return Almanac(seeds, mappings)
  }

  class LongRangeSet {
    val disjoint
      get() = ranges.size != 1

    var ranges = persistentListOf<LongRange>()

    operator fun plus(other: LongRange): LongRangeSet = apply {
      val lastOrNull = ranges.lastOrNull()
      if (lastOrNull != null && lastOrNull.first <= other.first && lastOrNull.last >= other.last)
          return@apply

      ranges =
          ranges
              .add(other)
              .sortedBy { it.first }
              .fold(persistentListOf()) { acc, next ->
                when {
                  acc.isNotEmpty() && acc.last().last >= next.first -> {
                    val union = acc.last().first..max(next.last, acc.last().last)
                    acc.removeAt(acc.lastIndex).add(union)
                  }
                  else -> acc.add(next)
                }
              }
    }

    val size: Long
      get() = ranges.sumOf { it.last - it.first + 1 }
  }

  private val testInput =
      """
seeds: 79 14 55 13

seed-to-soil map:
50 98 2
52 50 48

soil-to-fertilizer map:
0 15 37
37 52 2
39 0 15

fertilizer-to-water map:
49 53 8
0 11 42
42 0 7
57 7 4

water-to-light map:
88 18 7
18 25 70

light-to-temperature map:
45 77 23
81 45 19
68 64 13

temperature-to-humidity map:
0 69 1
1 0 69

humidity-to-location map:
60 56 37
56 93 4
    """
          .trimIndent()
}
