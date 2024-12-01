fun ListNode.append(value: Int): ListNode = apply {
  generateSequence(this) { it.next }.last().next = ListNode(value)
}

data class ListNode(var `val`: Int, var next: ListNode? = null) {
  override fun toString(): String {
    return toStringWithLimit(10)
  }

  private fun toStringWithLimit(limit: Int): String {
    return if (limit == 0) {
      "$..."
    } else {
      return "$`val` -> " + next?.toStringWithLimit(limit - 1)
    }
  }
}

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
