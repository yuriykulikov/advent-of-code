import io.kotest.matchers.shouldBe
import kotlinx.collections.immutable.PersistentMap
import kotlinx.collections.immutable.persistentMapOf
import org.junit.jupiter.api.Test

class Day15Test {
  @Test
  fun `silver example`() {
    "rn=1,cm-,qp=3,cm=2,qp-,pc=4,ot=9,ab=5,pc-,pc=6,ot=7"
        .split(",")
        .sumOf { hash(it) }
        .shouldBe(1320)
  }

  @Test
  fun `silver test`() {
    loadResource("Day15").split(",").sumOf { hash(it) }.shouldBe(506891)
  }

  private fun hash(string: String): Int {
    return string.toCharArray().fold(0) { acc, next -> (acc + next.code) * 17 % 256 }
  }

  @Test
  fun `gold example test`() {
    focus("rn=1,cm-,qp=3,cm=2,qp-,pc=4,ot=9,ab=5,pc-,pc=6,ot=7") shouldBe 145
  }

  @Test
  fun `gold test`() {
    focus(loadResource("Day15")) shouldBe 230462
  }

  private fun focus(input: String): Int {
    val boxes =
        input.split(",").fold(persistentMapOf<Int, PersistentMap<String, Int>>()) { acc, next ->
          if (next.contains("=")) {
            val (key, value) = next.split("=")
            val hash = hash(key)
            val box = acc.getOrDefault(hash, persistentMapOf())
            acc.put(hash, box.put(key, value.toInt()))
          } else {
            val key = next.substringBefore("-")
            val hash = hash(key)
            val box = acc.getOrDefault(hash, persistentMapOf())
            acc.put(hash, box.remove(key))
          }
        }
    return boxes.entries.sumOf { (box, lenses) ->
      lenses.values.mapIndexed { index, focalLength -> (box + 1) * (index + 1) * focalLength }.sum()
    }
  }
}
