object `Binary Tree Maximum Path Sum` {

  private val cache = mutableMapOf<TreeNode, Paths>()

  fun maxPathSum(root: TreeNode?): Int {
    val maxPaths = requireNotNull(root).maxPaths()
    return Math.max(maxPaths.appendable, maxPaths.nonAppendable ?: Int.MIN_VALUE)
  }
  class Paths(val appendable: Int, val nonAppendable: Int?)
  fun TreeNode.maxPaths(): Paths {
    return cache.getOrPut(this) {
      val leftAppendable = left?.maxPaths()?.appendable
      val rightAppendable = right?.maxPaths()?.appendable

      val appendable =
          listOfNotNull(`val`, leftAppendable?.plus(`val`), rightAppendable?.plus(`val`))

      val nonAppendable =
          listOfNotNull(
              `val`,
              left?.maxPaths()?.nonAppendable,
              right?.maxPaths()?.nonAppendable,
              leftAppendable?.plus(`val`),
              rightAppendable?.plus(`val`),
              leftAppendable?.plus(`val`)?.let { rightAppendable?.plus(it) })

      Paths(requireNotNull(appendable.max()), nonAppendable.max())
    }
  }
}
