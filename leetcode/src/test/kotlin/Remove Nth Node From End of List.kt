import io.kotest.matchers.shouldBe
import org.junit.Test

class `Remove Nth Node From End of List` {
  @Test
  fun test1() {
    val result = Solution().removeNthFromEnd(ListNode(1).append(2).append(3).append(4).append(5), 2)
    result shouldBe ListNode(1).append(2).append(3).append(5)
  }
  @Test
  fun test2() {
    val result = Solution().removeNthFromEnd(ListNode(1).append(2), 2)
    result?.`val` shouldBe 2
    result?.next shouldBe null
  }

  class Solution {
    fun removeNthFromEnd(head: ListNode?, n: Int): ListNode? {
      val asSequence = generateSequence(head) { it.next }
      val count = asSequence.count()
      if (n > count) return null
      if (n == count) return head?.next
      val beforeRemove = asSequence.take(count - n).last()
      beforeRemove.next = beforeRemove.next?.next
      return head
    }
  }
}
