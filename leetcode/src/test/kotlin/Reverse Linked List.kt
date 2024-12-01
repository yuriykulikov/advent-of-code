import io.kotest.matchers.shouldBe
import org.junit.Test

/**
 * Example: var li = ListNode(5) var v = li.`val` Definition for singly-linked list. class
 * ListNode(var `val`: Int) { var next: ListNode? = null }
 */
class Solution {
  private fun reverseListIteratively(originalHead: ListNode): ListNode {
    var head: ListNode = originalHead.next ?: return originalHead
    var prev = originalHead
    // this will be the last node
    prev.next = null
    while (true) {
      val next = head.next
      // link this node to prev
      head.next = prev
      if (next == null) {
        // end of list
        return head
      } else {
        // continue to the next node
        prev = head
        head = next
      }
    }
  }

  private fun ListNode.detachHead(): Pair<ListNode, ListNode?> {
    val head = this
    val next = head.next
    head.next = null
    return head to next
  }

  private fun reverseListRecFirstTry(head: ListNode?): ListNode? {
    if (head?.next != null) {
      // 1 - 2 - 3 - 4
      val next = head.next
      head.next = null
      // head: 1 <> next: 2 - 3 - 4

      val newHead = reverseListRecFirstTry(next)
      // newHead: 4 - 3 - 2
      // next is still 2

      // 4 - 3 - 2 -> 1 (head)
      next?.next = head

      return newHead
    } else {
      // reached the last element, it will be the new head
      return head
    }
  }

  /**
   * Lessons learned:
   * - Debug in the head
   */
  private fun reverseListRec(head: ListNode): ListNode {
    // head: 4 5
    // d: 4 rem: 5
    val (detachedHead, headOfRemaining) = head.detachHead()
    return if (headOfRemaining != null) {
      // hor: 5 -> null
      val headOfReversed = reverseListRecWithSecondFunction(headOfRemaining)
      require(headOfRemaining.next == null)
      // rem: 5 d: 4
      headOfRemaining.next = detachedHead
      return headOfReversed
    } else {
      detachedHead
    }
  }

  /**
   * Lessons learned:
   * - Debug in the head
   */
  private fun reverseListRecWithSecondFunction(head: ListNode): ListNode {

    val (prev, next) = head.detachHead()

    if (next == null) return head

    fun invertInternal(prev: ListNode, head: ListNode): ListNode {
      // prev 4 next 5
      val (detachedHead, remaining) = head.detachHead()
      if (remaining == null) {
        detachedHead.next = prev
        return detachedHead
      } else {
        val inverted = invertInternal(detachedHead, remaining)
        // prev: 1
        // next: 2 - 3 - 4
        // inverted before append: 4 - 3 - 2
        head.next = prev
        // inverted after append: 4 - 3 - 2 - [1]
        return inverted
      }
    }

    return invertInternal(prev, next)
  }

  private val list = ListNode(1).append(2).append(3).append(4).append(5)
  private val reversed = ListNode(5).append(4).append(3).append(2).append(1)

  @Test
  fun `reverse list iteratively`() {
    val result = Solution().reverseListIteratively(list)
    result shouldBe reversed
  }

  @Test
  fun `reverse list recursively with 2 functions`() {
    val result = Solution().reverseListRecWithSecondFunction(list)
    result shouldBe reversed
  }

  @Test
  fun `reverse list recursively first try`() {
    val result = Solution().reverseListRecFirstTry(list)
    result shouldBe reversed
  }

  @Test
  fun `reverse list recursively`() {
    val result = Solution().reverseListRec(list)
    result shouldBe reversed
  }
}
