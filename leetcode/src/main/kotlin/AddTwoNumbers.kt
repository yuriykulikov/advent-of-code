/** [LeetCode](https://leetcode.com/explore/featured/card/google/60/linked-list-5/3063/) */
class AddTwoNumbers {
  class ListNode(var `val`: Int) {
    var next: ListNode? = null
  }
  fun ListNode(`val`: Int, next: ListNode?): ListNode {
    return ListNode(`val`).apply { this.next = next }
  }
  // can be done differently but let's reverse
  fun ListNode.reverse(): ListNode? {
    // [1 > 2 > 3 > 4]
    // [4 > 3 > 2 > 1]
    var head: ListNode? = this
    var headOut: ListNode? = null
    while (head != null) {
      headOut = ListNode(head.`val`, headOut)
      head = head.next
    }
    return headOut
  }

  fun addTwoNumbersOriginal(l1: ListNode?, l2: ListNode?): ListNode? {
    var head: ListNode? = null
    var l1head = l1
    var l2head = l2
    var carry = 0
    while (l1head != null || l2head != null || carry > 0) {
      val sum = carry + (l1head?.`val` ?: 0) + (l2head?.`val` ?: 0)
      val value = sum.rem(10)
      carry = sum.div(10)
      head = ListNode(value, head)
      l1head = l1head?.next
      l2head = l2head?.next
    }
    return head?.reverse()
  }

  // no rev
  fun addTwoNumbersNoRev(l1: ListNode?, l2: ListNode?): ListNode? {
    var l1head = l1
    var l2head = l2

    var head: ListNode? = null
    var tail: ListNode? = null
    var carry = 0

    while (l1head != null || l2head != null) {
      val sum = (l1head?.`val` ?: 0) + (l2head?.`val` ?: 0) + carry
      val value = sum.rem(10)
      carry = sum.div(10)
      val nextNode = ListNode(value, null)
      if (head == null) {
        head = nextNode
      }
      tail?.next = nextNode
      tail = nextNode
      l1head = l1head?.next
      l2head = l2head?.next
    }
    return head
  }
}
