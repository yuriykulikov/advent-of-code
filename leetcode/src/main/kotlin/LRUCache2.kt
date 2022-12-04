/** `val head` and `val tail`. A lot simpler than [LRUCache].
 *
 * [LeetCode](https://leetcode.com/explore/interview/card/google/65/design-4/3090/)
 * */
class LRUCache2(private val capacity: Int) {
  class Node(val key: Int, var value: Int, var prev: Node?, var next: Node?) {
    override fun toString(): String {
      return "[$key=$value]"
    }
  }

  private val cache = mutableMapOf<Int, Node>()
  private val head: Node = Node(0, 0, null, null)
  private val tail: Node = Node(0, 0, null, null)

  init {
    link(head, tail)
  }

  private fun link(left: Node?, right: Node?) {
    left?.next = right
    right?.prev = left
  }

  /**
   * * 1 - 2 - 3 -> moveToHead(1) -> 2 - 1 - 3
   * * 1 - 2 - 3 -> moveToHead(2) -> 2 - 1 - 3
   * * 1 - 2 - 3 -> moveToHead(3) -> 1 - 2 - 3
   */
  private fun moveToHead(node: Node) {
    if (head.next?.key != node.key) {
      link(node.prev, node.next)
      link(node, head.next)
      link(head, node)
    }
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
    val evicted = tail.prev
    link(evicted?.prev, tail)
    evicted?.let { cache.remove(it.key) }
  }

  private fun add(key: Int, value: Int) {
    val node = Node(key, value, null, null)
    cache[key] = node
    moveToHead(node)
  }
}
