package algorithms

fun <T : Comparable<T>> List<T>.quickSort(): List<T> {
    return quickSort(comparator = { lhs, rhs -> lhs.compareTo(rhs) })
}

fun <T> List<T>.quickSort(comparator: Comparator<T>): List<T> {
    return when {
        size < 2 -> this
        else -> {
            val pivot = last()
            val withoutPivot = dropLast(1)

            val (before, after) = withoutPivot
                .partition { comparator.compare(it, pivot) == -1 }

            before.quickSort(comparator) + pivot + after.quickSort(comparator)
        }
    }
}

fun List<Int>.quickSortInt(): List<Int> {
    return when {
        size < 2 -> this
        else -> {
            val pivot = last()
            val withoutPivot = dropLast(1)
            val (before, after) = withoutPivot.partition { it < pivot }
            before.quickSortInt() + pivot + after.quickSortInt()
        }
    }
}