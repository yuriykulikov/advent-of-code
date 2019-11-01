package algorithms

import java.util.*

fun <T : Comparable<T>> List<T>.heapSort(): List<T> {
    return heapSort(comparator = { lhs, rhs -> lhs.compareTo(rhs) })
}

fun <T : Any> List<T>.heapSort(comparator: Comparator<T>): List<T> {
    val heap: PriorityQueue<T> = PriorityQueue(this.size, comparator)
    heap.addAll(this)
    return generateSequence {
        val next: T? = heap.poll()
        next
    }.toList()
}
