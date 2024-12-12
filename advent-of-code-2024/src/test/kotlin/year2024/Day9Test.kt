package year2024

import io.kotest.matchers.shouldBe
import io.kotest.matchers.string.shouldNotContain
import loadResource
import org.junit.jupiter.api.Test

class Day9Test {
    private val example = "2333133121414131402"

    @Test
    fun `silver example`() {
        val fs = unpackDiscMap(example)

        println(fs.fsToString())

        val fragmented = fragment(fs)
        println(fragmented.fsToString())

        fragmented.fsToString() shouldBe "0099811188827773336446555566.............."

        checksum(fragmented) shouldBe 1928
    }

    @Test
    fun `silver example short`() {
        checksum(fragment(unpackDiscMap(example))) shouldBe 1928
    }

    @Test
    fun `silver test`() {
        // TODO there is a but leading to one single . near the end, which I missed because
        // I was filtering nulls out
        val message = fragment(unpackDiscMap(loadResource("Day9"))).fsToString().trimEnd { it == '.' }
        println(message)
        message.shouldNotContain(".")
        checksum(fragment(unpackDiscMap(loadResource("Day9"))).filterNotNull()) shouldBe 6421128769094L
    }

    @Test
    fun `gold example`() {
        val (fs, pos) = unpackDiscMap2(example)

        println(fs.fsToString())

        val def = defragment(fs, pos)
        println(def.fsToString())

        def.fsToString() shouldBe "00992111777.44.333....5555.6666.....8888.."

        checksum(def) shouldBe 2858
    }


    @Test
    fun `gold`() {
        val (fs, pos) = unpackDiscMap2(loadResource("Day9"))

        val def = defragment(fs, pos)

        checksum(def) shouldBe 6448168620520L
    }

    private fun defragment(fs: List<Int?>, pos: Map<Int, Int>): List<Int?> {
        val mutable = fs.toMutableList()

        pos.entries.reversed().forEach { (position, fileLength) ->
            val freeSpace = (0..position).firstOrNull { pos ->
                (0..<fileLength).all { mutable[pos + it] == null }
            }

            if (freeSpace != null) {
                // move the block from position to i
                repeat(fileLength) { offset ->
                    mutable[freeSpace + offset] = mutable[position + offset]
                    mutable[position + offset] = null
                }
            }
        }

        return mutable
    }

    private fun checksum(blocks: List<Int?>): Long {

        return blocks
            // "If a block contains free space, skip it instead."
            .foldIndexed(0) { index, acc, next ->
                acc + index * (next ?: 0).toLong()
            }
    }

    private fun unpackDiscMap(denseFormat: String): List<Int?> {
        return unpackDiscMap2(denseFormat).first
    }

    private fun unpackDiscMap2(denseFormat: String): Pair<List<Int?>, Map<Int, Int>> {
        val positions = mutableMapOf<Int, Int>()
        return buildList {
            denseFormat.map { it.digitToInt() }
                .windowed(2, 2, partialWindows = true)
                .forEachIndexed { index, ints ->
                    val length = ints.first()
                    positions[size] = length
                    repeat(length) {
                        add(index)
                    }
                    ints.getOrNull(1)?.let {
                        repeat(it) {
                            add(null)
                        }
                    }
                }
        } to positions
    }

    private fun fragment(fs: List<Int?>): List<Int?> {
        val mutable = fs.toMutableList()
        var left = 0
        var right = fs.lastIndex
        while (left <= right) {
            while (mutable[left] != null) {
                left++
            }
            while (mutable[right] == null) {
                right--
            }
            check(mutable[left] == null)
            check(mutable[right] != null)
            mutable[left] = mutable[right]
            mutable[right] = null
            left++
            right--
        }
        return mutable
    }

    private fun List<Number?>.fsToString(): String {
        return joinToString("") { it?.toString() ?: "." }
    }
}

