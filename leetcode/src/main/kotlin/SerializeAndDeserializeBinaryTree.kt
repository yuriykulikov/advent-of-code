class TreeNode(var `val`: Int) {
  var left: TreeNode? = null
  var right: TreeNode? = null
}

fun tree(data: String) = SerializeAndDeserializeBinaryTree().deserialize(data)

/** https://leetcode.com/explore/interview/card/google/65/design-4/3092/ */
class SerializeAndDeserializeBinaryTree {
  fun serialize(root: TreeNode?): String {
    val queue = ArrayDeque<TreeNode>()
    val flat = mutableListOf<Int?>()
    fun push(node: TreeNode?) {
      if (node != null) {
        queue.add(node)
        flat.add(node.`val`)
      } else {
        flat.add(null)
      }
    }

    push(root)

    while (queue.isNotEmpty()) {
      val node = queue.removeFirst()
      push(node.left)
      push(node.right)
    }
    return flat.dropLastWhile { it == null }.joinToString(",", "[", "]")
  }

  // Decodes your encoded data to tree.
  fun deserialize(data: String): TreeNode? {
    val iter = data.drop(1).dropLast(1).split(",").iterator()
    fun nextNode() = iter.takeIf { it.hasNext() }?.next()?.toIntOrNull()?.let { TreeNode(it) }
    val root = nextNode() ?: return null
    val queue = ArrayDeque<TreeNode>()
    queue.add(root)
    while (queue.isNotEmpty()) {
      val node = queue.removeFirst()
      node.left = nextNode()?.also { queue.add(it) }
      node.right = nextNode()?.also { queue.add(it) }
    }
    return root
  }
}
