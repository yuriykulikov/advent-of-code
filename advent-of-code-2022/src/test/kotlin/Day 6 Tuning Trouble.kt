import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test

class `Day 6 Tuning Trouble` {
  @Test
  fun silverTest() {
    startPacketMarkerPosition("mjqjpqmgbljsphdztnvjfqwrcgsmlb") shouldBe 7
    startPacketMarkerPosition("bvwbjplbgvbhsrlpgdmjqwftvncz") shouldBe 5
    startPacketMarkerPosition("nppdvjthqldpwncqszvftbrmjlhg") shouldBe 6
    startPacketMarkerPosition("nznrnfrfntjfmvfwmzdfjlvtqnbhcprsg") shouldBe 10
    startPacketMarkerPosition("zcfzfwzzqfrljwzlrfnpqdbhtmscgvjw") shouldBe 11
  }

  @Test
  fun silver() {
    startPacketMarkerPosition(loadResource("day6")) shouldBe 1566
  }

  @Test
  fun goldTest() {
    startPacketMarkerPosition("mjqjpqmgbljsphdztnvjfqwrcgsmlb", packetLength = 14) shouldBe 19
    startPacketMarkerPosition("bvwbjplbgvbhsrlpgdmjqwftvncz", packetLength = 14) shouldBe 23
    startPacketMarkerPosition("nppdvjthqldpwncqszvftbrmjlhg", packetLength = 14) shouldBe 23
    startPacketMarkerPosition("nznrnfrfntjfmvfwmzdfjlvtqnbhcprsg", packetLength = 14) shouldBe 29
    startPacketMarkerPosition("zcfzfwzzqfrljwzlrfnpqdbhtmscgvjw", packetLength = 14) shouldBe 26
  }

  @Test
  fun gold() {
    startPacketMarkerPosition(loadResource("day6"), packetLength = 14) shouldBe 2265
  }

  private fun startPacketMarkerPosition(input: String, packetLength: Int = 4): Int {
    return input
        .toList()
        .windowed(packetLength)
        .mapIndexed { index, chars -> chars to index }
        .first { (chars, _) -> chars.distinct().size == packetLength }
        .second + packetLength
  }
}
