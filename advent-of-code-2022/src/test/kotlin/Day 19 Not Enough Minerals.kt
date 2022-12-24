import io.kotest.matchers.shouldBe
import java.util.*
import kotlin.math.min
import org.junit.jupiter.api.Test

class `Day 19 Not Enough Minerals` {
  private fun buildStuff(blueprint: BluePrint, minutes: Int = 24): Int {
    val botBuildTimes = mutableMapOf<Int, Int>()
    // cache does not help much
    // val cache = mutableMapOf<State, State?>()
    fun State.build(): State? {
      if (elapsed == minutes) return this
      // return cache.getOrPut(this) {
      val geoCrackingRobot = geoCrackingRobot(blueprint)
      // let's try greedy here
      // since our production is capped at 1
      // it makes sense to start producing geo cracking robots early and not wait for economy to
      // kick in
      return if (geoCrackingRobot != null) {
        botBuildTimes.merge(geoCrackingRobots + 1, elapsed) { p, n -> min(p, n) }
        geoCrackingRobot.build()
      } else if ((botBuildTimes[geoCrackingRobots + 1] ?: 100) < elapsed) {
        null
      } else {
        // since production is capped at 1, gathering additional resources
        // makes no sense once we can build anything from 1 minute yield
        listOfNotNull(
                takeIf { blueprint.maxOre > oreRobots }?.oreRobot(blueprint)?.build(),
                takeIf { blueprint.maxClay > clayRobots }?.clayRobot(blueprint)?.build(),
                takeIf { blueprint.maxObsidian > obsidianRobots }
                    ?.obsidianRobot(blueprint)
                    ?.build(),
                mine().build(),
            )
            .maxByOrNull { it.cracks }
      }
    }

    return State().build()!!.cracks
  }

  @Test
  fun silverTest() {
    bluePrints(testInput)
        .mapIndexed { index, blueprint -> buildStuff(blueprint) * (index + 1) }
        .sum() shouldBe 33
  }

  @Test
  fun silver() {
    bluePrints(loadResource("day19"))
        .mapIndexed { index, blueprint -> buildStuff(blueprint) * (index + 1) }
        .sum() shouldBe 1681
  }

  @Test
  fun gold() {
    bluePrints(loadResource("day19"))
        .take(3)
        .map { blueprint -> buildStuff(blueprint, 32).toLong() }
        .reduce { l, r -> l * r } shouldBe 5394
  }

  data class State(
      val elapsed: Int = 0,
      val oreRobots: Int = 1,
      val clayRobots: Int = 0,
      val obsidianRobots: Int = 0,
      val geoCrackingRobots: Int = 0,
      val ore: Int = 0,
      val clay: Int = 0,
      val obsidian: Int = 0,
      val cracks: Int = 0,
  ) {
    fun mine(): State {
      return copy(
          ore = ore + oreRobots,
          clay = clay + clayRobots,
          obsidian = obsidian + obsidianRobots,
          cracks = cracks + geoCrackingRobots,
          elapsed = elapsed + 1)
    }

    fun geoCrackingRobot(blueprint: BluePrint): State? =
        takeIf {
              ore >= blueprint.geoCrackingRobotOre && obsidian >= blueprint.geoCrackingRobotObsidian
            }
            ?.mine()
            ?.run {
              copy(
                  ore = ore - blueprint.geoCrackingRobotOre,
                  obsidian = obsidian - blueprint.geoCrackingRobotObsidian,
                  geoCrackingRobots = geoCrackingRobots + 1)
            }

    fun obsidianRobot(blueprint: BluePrint): State? =
        takeIf { ore >= blueprint.obsidianRobotOre && clay >= blueprint.obsidianRobotClay }
            ?.mine()
            ?.run {
              copy(
                  ore = ore - blueprint.obsidianRobotOre,
                  clay = clay - blueprint.obsidianRobotClay,
                  obsidianRobots = obsidianRobots + 1)
            }

    fun clayRobot(blueprint: BluePrint): State? =
        takeIf { ore >= blueprint.clayRobotOre }
            ?.mine()
            ?.run { copy(ore = ore - blueprint.clayRobotOre, clayRobots = clayRobots + 1) }

    fun oreRobot(blueprint: BluePrint): State? =
        takeIf { ore >= blueprint.oreRobotOre }
            ?.mine()
            ?.run { copy(ore = ore - blueprint.oreRobotOre, oreRobots = oreRobots + 1) }
  }
  data class BluePrint(
      val oreRobotOre: Int,
      val clayRobotOre: Int,
      val obsidianRobotOre: Int,
      val obsidianRobotClay: Int,
      val geoCrackingRobotOre: Int,
      val geoCrackingRobotObsidian: Int,
  ) {
    val maxOre = maxOf(oreRobotOre, clayRobotOre, obsidianRobotOre, geoCrackingRobotOre)
    val maxClay = maxOf(obsidianRobotClay)
    val maxObsidian = maxOf(geoCrackingRobotObsidian)
  }

  private fun bluePrints(input: String) =
      input.lines().map { line ->
        val prices =
            StringTokenizer(line, " ")
                .asSequence()
                .mapNotNull { it as? String }
                .mapNotNull { it.toIntOrNull() }
                .toList()
                .iterator()

        BluePrint(
            oreRobotOre = prices.next(),
            clayRobotOre = prices.next(),
            obsidianRobotOre = prices.next(),
            obsidianRobotClay = prices.next(),
            geoCrackingRobotOre = prices.next(),
            geoCrackingRobotObsidian = prices.next(),
        )
      }

  private val testInput =
      """
Blueprint 1:
  Each ore robot costs 4 ore.
  Each clay robot costs 2 ore.
  Each obsidian robot costs 3 ore and 14 clay.
  Each geode robot costs 2 ore and 7 obsidian.

Blueprint 2:
  Each ore robot costs 2 ore.
  Each clay robot costs 3 ore.
  Each obsidian robot costs 3 ore and 8 clay.
  Each geode robot costs 3 ore and 12 obsidian.
    """
          .trimIndent()
          .split("\n\n")
          .joinToString("\n") { it.lines().joinToString("") }
}
