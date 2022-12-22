import io.kotest.matchers.shouldBe
import kotlinx.collections.immutable.toPersistentList
import org.junit.jupiter.api.Test

class `Day 20 Grove Positioning System` {

  @Test
  fun testData() {
    val result = mix(testValues)
    result.joinToString("\n") shouldBe
        """
            [1, 2, -3, 3, -2, 0, 4]
            [2, 1, -3, 3, -2, 0, 4]
            [1, -3, 2, 3, -2, 0, 4]
            [1, 2, 3, -2, -3, 0, 4]
            [1, 2, -2, -3, 0, 3, 4]
            [1, 2, -3, 0, 3, 4, -2]
            [1, 2, -3, 0, 3, 4, -2]
            [1, 2, -3, 4, 0, 3, -2]
        """.trimIndent()
  }
  @Test
  fun silverTest() {
    val result = mix(testValues).last()
    val indexOf0 = result.indexOf(0)
    listOf(1000, 2000, 3000).map { result[wrapIndex(it + indexOf0, result.size)] }.sum() shouldBe 3
  }

  @Test
  fun silver() {
    val result = mix(loadResource("day20")).last()
    val indexOf0 = result.indexOf(0)
    listOf(1000, 2000, 3000).map { result[wrapIndex(it + indexOf0, result.size)] }.sum() shouldBe
        8764
  }
  @Test
  fun goldTest() {
    val result = mixGold(testValues)
    val indexOf0 = result.indexOf(0)
    listOf(1000, 2000, 3000).sumOf { result[wrapIndex(it + indexOf0, result.size)] } shouldBe
        1623178306
  }
  @Test
  fun gold() {
    val result = mixGold(loadResource("day20"))
    val indexOf0 = result.indexOf(0)
    listOf(1000, 2000, 3000).sumOf { result[wrapIndex(it + indexOf0, result.size)] } shouldBe
        535648840980L
  }
  @Test
  fun gold2() {
    val result = mixGold2(loadResource("day20"))
    val indexOf0 = result.indexOf(0)
    listOf(1000, 2000, 3000).sumOf { result[wrapIndex(it + indexOf0, result.size)] } shouldBe
        535648840980L
  }

  fun wrapIndex(index: Int, size: Int): Int = wrapIndex(index.toLong(), size)
  fun wrapIndex(index: Long, size: Int): Int {
    return when {
      index == 0L -> size
      index < 0 -> index % size + size
      index > size -> index % size
      else -> index
    }.toInt()
  }
  private fun mix(values: String): List<List<Long>> {
    val zipped = values.trimIndent().lines().map { it.toLong() }.withIndex().toPersistentList()

    val result =
        zipped
            .scan(zipped) { list, next ->
              val index = list.indexOf(next)
              val withoutElement = list.removeAt(index)
              withoutElement.add(wrapIndex(index + next.value, withoutElement.size), next)
            }
            .map { list -> list.map { it.value } }
    return result
  }

  private fun mixGold(values: String): List<Long> {
    val zipped =
        values.trimIndent().lines().map { it.toLong() * 811589153 }.withIndex().toPersistentList()

    return (0 until 10)
        .fold(zipped) { toZip, _ ->
          zipped
              .scan(toZip) { list, next ->
                val index = list.indexOf(next)
                list.removeAt(index).add(wrapIndex(index + next.value, list.size - 1), next)
              }
              .last()
        }
        .map { it.value }
  }

  /** A bit faster with a mutable list */
  private fun mixGold2(values: String): List<Long> {
    val zipped = values.trimIndent().lines().map { it.toLong() * 811589153 }.withIndex()
    val mixed = zipped.toMutableList()
    repeat(10) {
      zipped.forEach { next ->
        val index = mixed.indexOf(next)
        mixed.removeAt(index)
        mixed.add(wrapIndex(index + next.value, mixed.size), next)
      }
    }
    return mixed.map { it.value }
  }

  private val testValues = """
1
2
-3
3
-2
0
4
"""
}
