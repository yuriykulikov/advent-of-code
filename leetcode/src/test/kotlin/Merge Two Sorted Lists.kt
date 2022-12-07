import io.kotest.matchers.shouldBe
import org.junit.Test

class `Merge Two Sorted Lists` {

  fun mergeTwoLists(list1: ListNode?, list2: ListNode?): ListNode? {
    val left = LinkedList(list1)
    val right = LinkedList(list2)
    val out = LinkedList(null)

    while (left.isNotEmpty() || right.isNotEmpty()) {
      when {
        left.isNotEmpty() && right.isNotEmpty() -> {
          val selectedList = if (left.first() <= right.first()) left else right
          out.add(selectedList.removeFirst())
        }
        left.isNotEmpty() -> {
          out.addAll(left)
          left.clear()
        }
        right.isNotEmpty() -> {
          out.addAll(right)
          right.clear()
        }
        else -> error("")
      }
    }
    return out.head
  }

  @Test
  fun omg() {
    mergeTwoLists(ListNode(1).append(2).append(4), ListNode(1).append(3).append(4)) shouldBe
        ListNode(1).append(1).append(2).append(3).append(4).append(4)
  }
}
