import io.kotest.matchers.shouldBe
import java.util.*
import org.junit.jupiter.api.Test

class `Day 13 Distress Signal` {
  sealed class Packet
  data class ListPacket(val list: List<Packet>) : Packet() {
    override fun toString(): String = list.joinToString(",", "[", "]")
    companion object {
      fun of(vararg packet: Packet): Packet = ListPacket(packet.toList())
      fun of(vararg packets: Int): Packet = ListPacket(packets.map { IntPacket(it) })
    }
  }

  data class IntPacket(val integer: Int) : Packet() {
    override fun toString(): String = integer.toString()
  }
  private fun parse(string: String): Packet {
    val tokens =
        StringTokenizer(string, "[],", true).asSequence().filterIsInstance<String>().iterator()
    return parseSequence(tokens)
        // unwrap the first packet
        .let { it as ListPacket }
        .list
        .first()
  }
  private fun parseSequence(sequence: Iterator<String>): Packet {
    val packets = mutableListOf<Packet>()
    while (sequence.hasNext()) {
      when (val next = sequence.next()) {
        "[" -> packets.add(parseSequence(sequence))
        "]" -> break
        "," -> Unit
        else -> packets.add(IntPacket(next.toInt()))
      }
    }
    return ListPacket(packets)
  }

  private fun inOrder(left: Packet, right: Packet, stack: String? = null): Boolean? {
    if (stack != null) println("$stack- Compare $left vs $right")
    return when {
      left == right -> null
      left is IntPacket && right is IntPacket -> left.integer < right.integer
      left is IntPacket && right is ListPacket -> inOrder(ListPacket.of(left.integer), right, stack)
      left is ListPacket && right is IntPacket -> inOrder(left, ListPacket.of(right.integer), stack)
      left is ListPacket && right is ListPacket -> {
        left.list.zip(right.list).firstNotNullOfOrNull { (l, r) ->
          inOrder(l, r, stack?.let { "$it  " })
        }
            ?: (left.list.size < right.list.size)
      }
      else -> error("")
    }
  }

  private val orderComparator: Comparator<Packet> = Comparator { l, r ->
    when (inOrder(l, r)) {
      true -> -1
      false -> 1
      null -> 0
    }
  }

  private fun distressIndex(sorted: List<Packet>): Int {
    val first = sorted.indexOf(parse("[[2]]")) + 1
    val second = sorted.indexOf(parse("[[6]]")) + 1
    return first * second
  }
  private fun packetSequence(testInput1: String) =
      testInput1.lines().asSequence().filter { it.isNotBlank() }.map { parse(it) }

  @Test
  fun testParsing() {
    parse("[1,[2,[3,[4,[5,6,7]]]],8,9]") shouldBe
        ListPacket.of(
            IntPacket(1),
            ListPacket.of(
                IntPacket(2),
                ListPacket.of(
                    IntPacket(3),
                    ListPacket.of(IntPacket(4), ListPacket.of(5, 6, 7)),
                ),
            ),
            IntPacket(8),
            IntPacket(9),
        )
  }

  @Test
  fun silverUnitTest() {
    inOrder(parse("[1,1,3,1,1]"), parse("[1,1,5,1,1]"), "") shouldBe true
    inOrder(parse("[[1],[2,3,4]]"), parse("[[1],4]"), "") shouldBe true
    inOrder(parse("[9]"), parse("[[8,7,6]]"), "") shouldBe false
    inOrder(parse("[[4,4],4,4]"), parse("[[4,4],4,4,4]"), "") shouldBe true
    inOrder(parse("[7,7,7,7]"), parse("[7,7,7]"), "") shouldBe false
    inOrder(parse("[]"), parse("[3]")) shouldBe true
    inOrder(parse("[[[]]]"), parse("[[]]"), "") shouldBe false
    inOrder(parse("[1,[2,[3,[4,[5,6,7]]]],8,9]"), parse("[1,[2,[3,[4,[5,6,0]]]],8,9]"), "") shouldBe
        false
  }

  @Test
  fun silverTest() {
    packetSequence(testInput)
        .windowed(2, 2)
        .mapIndexedNotNull { index, (l, r) -> if (inOrder(l, r) == true) index + 1 else null }
        .sum() shouldBe 13
  }

  @Test
  fun silver() {
    packetSequence(loadResource("day13"))
        .windowed(2, 2)
        .mapIndexedNotNull { index, (l, r) -> if (inOrder(l, r) == true) index + 1 else null }
        .sum() shouldBe 6623
  }

  @Test
  fun goldTest() {
    val sorted =
        packetSequence(testInput)
            .plus(ListPacket.of(ListPacket.of(2)))
            .plus(ListPacket.of(ListPacket.of(6)))
            .sortedWith(orderComparator)
            .toList()
    sorted.joinToString("\n") shouldBe
        """
[]
[[]]
[[[]]]
[1,1,3,1,1]
[1,1,5,1,1]
[[1],[2,3,4]]
[1,[2,[3,[4,[5,6,0]]]],8,9]
[1,[2,[3,[4,[5,6,7]]]],8,9]
[[1],4]
[[2]]
[3]
[[4,4],4,4]
[[4,4],4,4,4]
[[6]]
[7,7,7]
[7,7,7,7]
[[8,7,6]]
[9]
        """.trimIndent()
    distressIndex(sorted) shouldBe 140
  }

  @Test
  fun gold() {
    val sorted =
        packetSequence(loadResource("day13"))
            .plus(ListPacket.of(ListPacket.of(2)))
            .plus(ListPacket.of(ListPacket.of(6)))
            .sortedWith(orderComparator)
            .toList()
    distressIndex(sorted) shouldBe 23049
  }

  private val testInput =
      """
[1,1,3,1,1]
[1,1,5,1,1]

[[1],[2,3,4]]
[[1],4]

[9]
[[8,7,6]]

[[4,4],4,4]
[[4,4],4,4,4]

[7,7,7,7]
[7,7,7]

[]
[3]

[[[]]]
[[]]

[1,[2,[3,[4,[5,6,7]]]],8,9]
[1,[2,[3,[4,[5,6,0]]]],8,9]
    """.trimIndent()
}
