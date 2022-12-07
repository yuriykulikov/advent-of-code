fun ListNode.append(value: Int): ListNode = apply {
  generateSequence(this) { it.next }.last().next = ListNode(value)
}

data class ListNode(var `val`: Int, var next: ListNode? = null)
data class LinkedList(var head: ListNode?) {
  private var tail = head
  fun first(): Int = requireNotNull(head).`val`
  fun isNotEmpty(): Boolean = head != null
  fun removeFirst(): Int {
    val ret = requireNotNull(head).`val`
    head = requireNotNull(head).next
    return ret
  }
  fun add(value: Int) {
    val element = ListNode(value)
    tail?.next = element
    tail = element
    if (head == null) {
      head = element
    }
  }
  fun addAll(other: LinkedList) {
    tail?.next = other.head
    tail = other.tail
  }
  fun clear() {
    head = null
    tail = null
  }
}
