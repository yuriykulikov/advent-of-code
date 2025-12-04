package year2024

import io.kotest.matchers.collections.shouldBeEmpty
import io.kotest.matchers.collections.shouldContain
import io.kotest.matchers.collections.shouldContainExactlyInAnyOrder
import io.kotest.matchers.longs.shouldBePositive
import io.kotest.matchers.shouldBe
import loadResource
import org.junit.jupiter.api.Test

class Day2Test {
    private val example = """
11-22,95-115,998-1012,1188511880-1188511890,222220-222224,
1698522-1698528,446443-446449,38593856-38593862,565653-565659,
824824821-824824827,2121212118-2121212124
    """.trim().replace("\n", "")

    @Test
    fun `Silver example - find invalid IDs in each range`() {
        "11-22".invalidIds().shouldContainExactlyInAnyOrder(11, 22)
        "95-115".invalidIds().shouldContain(99)
        "998-1012".invalidIds().shouldContain(1010)
        "1188511880-1188511890".invalidIds().shouldContain(1188511885)
        "222220-222224".invalidIds().shouldContain(222222)
        "1698522-1698528".invalidIds().shouldBeEmpty()
        "446443-446449".invalidIds().shouldContain(446446)
        "38593856-38593862".invalidIds().shouldContain(38593859)
    }

    @Test
    fun `Silver example - Adding up all the invalid IDs`() {
        example.split(",").flatMap { it.invalidIds() }.sum().shouldBe(1227775554)
    }

    @Test
    fun `Silver - Adding up all the invalid IDs`() {
        loadResource("Day2").split(",").flatMap { it.invalidIds() }.sum().shouldBe(15873079081)
    }

    fun String.invalidIds(): List<Long> {
        val (l, r) = split("-").map { it.toLong() }
        val range = l..r
        val maxLength = r.digits()
        val minL = 10.pow(l.digits() / 2 - 1)
        val maxR = 9L.repeat(maxLength / 2)
        return (minL..maxR).map { number ->
            10.pow(number.digits()) * number + number
        }.filter { it in range }
    }

    fun String.invalidIdsGold(): List<Long> {
        val (l, r) = split("-").map { it.toLong() }
        val range = l..r
        val maxLength = r.digits()
        // maxR remains because we need at least one repetition
        val maxR = 9L.repeat(maxLength / 2)
        return (1..maxR).flatMap { number ->
            number.shouldBePositive()
            (2..maxLength)
                .asSequence()
                .filter { number.digits() * it <= maxLength }
                .map { times -> number.repeat(times) }
        }
            .filter { it in range }.distinct()
    }

    @Test
    fun `Gold example - find invalid IDs in each range`() {
        "11-22".invalidIdsGold().shouldContainExactlyInAnyOrder(11, 22)
        "95-115".invalidIdsGold().shouldContainExactlyInAnyOrder(99, 111)
        "998-1012".invalidIdsGold().shouldContainExactlyInAnyOrder(999, 1010)
        "1188511880-1188511890".invalidIdsGold().shouldContainExactlyInAnyOrder(1188511885)
        "222220-222224".invalidIdsGold().shouldContainExactlyInAnyOrder(222222)
        "1698522-1698528".invalidIdsGold().shouldBeEmpty()
        "446443-446449".invalidIdsGold().shouldContainExactlyInAnyOrder(446446)
        "38593856-38593862".invalidIdsGold().shouldContainExactlyInAnyOrder(38593859)
        "565653-565659".invalidIdsGold().shouldContainExactlyInAnyOrder(565656)
        "824824821-824824827".invalidIdsGold().shouldContainExactlyInAnyOrder(824824824)
        "2121212118-2121212124".invalidIdsGold().shouldContainExactlyInAnyOrder(2121212121)
    }

    @Test
    fun `Gold example - Adding up all the invalid IDs`() {
        example.split(",").flatMap { it.invalidIdsGold() }.sum().shouldBe(4174379265)
    }


    @Test
    fun `Gold - Adding up all the invalid IDs`() {
        loadResource("Day2").split(",").flatMap { it.invalidIdsGold() }.distinct().sum().shouldBe(22617871034)
    }
}
