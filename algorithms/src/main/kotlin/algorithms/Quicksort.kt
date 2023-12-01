package algorithms

import java.util.*

fun <T : Comparable<T>> List<T>.quickSort(inPlace: Boolean = true): List<T> {
  return if (inPlace) {
    toMutableList().apply { quickSortInPlace() }
  } else {
    quickSortNewList()
  }
}

fun <T : Comparable<T>> MutableList<T>.quickSortInPlace() {
  if (size <= 1) return
  val pivotIndex = partition()
  subList(0, pivotIndex).quickSortInPlace()
  subList(pivotIndex + 1, size).quickSortInPlace()
}

fun <T : Comparable<T>> MutableList<T>.partition(): Int {
  val pivot = last()
  var swapPosition = 0
  (0 until lastIndex).forEach { i ->
    if (get(i) <= pivot) {
      Collections.swap(this, swapPosition, i)
      swapPosition++
    }
  }
  Collections.swap(this, swapPosition, lastIndex)
  return swapPosition
}

private fun <T : Comparable<T>> List<T>.quickSortNewList(): List<T> {
  return when {
    size < 2 -> this
    else -> {
      val pivot = last()
      val withoutPivot = dropLast(1)
      val (before, after) = withoutPivot.partition { it < pivot }
      before.quickSortNewList() + pivot + after.quickSortNewList()
    }
  }
}
