/**
 * `var head` and `var tail`. A bit tedious because we have to keep track of things like moving the
 * tail to head etc.
 *
 * [LeetCode](https://leetcode.com/explore/interview/card/google/65/design-4/3090/)
 */
class LRUCache(private val capacity: Int) {
  class Node(val key: Int, var value: Int, var prev: Node?, var next: Node?) {
    override fun toString(): String {
      return "[$key=$value]"
    }
  }
  val cache = mutableMapOf<Int, Node>()
  var head: Node? = null
  var tail: Node? = null
  fun Node.makeHead() {
    check(next == null)
    check(prev == null)
    val prevHead = head
    this.next = prevHead
    prevHead?.prev = this
    this.prev = null
    head = this
  }

  /** 1 - 2 - 3 -> linkNeighbors(2) -> 1 - 2 */
  private fun linkNeighbors(node: Node) {
    val left = node.prev
    val right = node.next
    left?.next = right
    right?.prev = left
    node.next = null
    node.prev = null
  }

  /**
   * * 1 - 2 - 3 -> moveToHead(1) -> 2 - 1 - 3
   * * 1 - 2 - 3 -> moveToHead(2) -> 2 - 1 - 3
   * * 1 - 2 - 3 -> moveToHead(3) -> 1 - 2 - 3
   */
  private fun moveToHead(node: Node) {
    if (head?.key == node.key) {
      // NOP already head
    } else if (tail?.key == node.key) {
      popTail()?.makeHead()
    } else {
      linkNeighbors(node)
      node.makeHead()
    }
  }
  private fun popTail(): Node? {
    val prevTail = tail
    tail = prevTail?.prev
    tail?.next = null
    prevTail?.prev = null
    return prevTail
  }
  fun get(key: Int): Int {
    val node = cache[key] ?: return -1
    moveToHead(node)
    return node.value
  }

  fun put(key: Int, value: Int) {
    val cached = cache[key]
    if (cached != null) {
      cached.value = value
      moveToHead(cached)
    } else if (cache.size < capacity) {
      add(key, value)
    } else {
      evict()
      add(key, value)
    }
  }
  private fun evict() {
    popTail()?.let { cache.remove(it.key) }
  }
  private fun add(key: Int, value: Int) {
    val node = Node(key, value, null, null)
    moveToHead(node)
    if (tail == null) {
      tail = node
    }
    cache[key] = node
  }
}
