package algorithms

fun <T : Comparable<T>> List<T>.mergesort(): List<T> {
  if (size < 2) return this
  return merge(
      subList(0, size / 2).mergesort().toMutableList(),
      subList(size / 2, size).mergesort().toMutableList())
}

fun <T : Comparable<T>> merge(firstList: MutableList<T>, secondList: MutableList<T>): List<T> {
  val list = mutableListOf<T>()
  while (firstList.isNotEmpty() || secondList.isNotEmpty()) {
    val toAdd =
        when {
          firstList.isEmpty() -> secondList.removeAt(0)
          secondList.isEmpty() -> firstList.removeAt(0)
          firstList.first() < secondList.first() -> firstList.removeAt(0)
          else -> secondList.removeAt(0)
        }
    list.add(toAdd)
  }
  return list
}
