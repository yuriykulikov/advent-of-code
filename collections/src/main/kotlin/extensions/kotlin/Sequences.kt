package extensions.kotlin

/**
 * Cycles this sequence indefinitely.
 *
 * The operation is _intermediate_ and _stateless_.
 */
fun <T> Sequence<T>.cycle(): Sequence<T> {
    return when {
        this.none() -> this
        else -> generateSequence(this) { this }.flatten()
    }
}

/**
 * Creates a new sequence by scanning the values this sequence. Starts with the [seed] and applies the
 * [operation] from left to right, passing the accumulated value and each next element as arguments.
 * Every result of the operation is a value of the returned sequence.
 *
 * Sequence is finished after the first null is returned by the operation
 *
 * The operation is _intermediate_ and _stateful_.
 */
fun <T, R : Any> Sequence<T>.scanAndTermintateOnNull(seed: R?, operation: (R, T) -> R?): Sequence<R> {
    val iterator = this.iterator()

    return generateSequence(seed) { acc: R ->
        if (iterator.hasNext()) {
            operation(acc, iterator.next())
        } else {
            null
        }
    }
}

/**
 * Creates a new sequence by scanning the values this sequence. Starts with the [seed] and applies the
 * [operation] from left to right, passing the accumulated value and each next element as arguments.
 * Every result of the operation is a value of the returned sequence.
 *
 * The operation is _intermediate_ and _stateful_.
 */
fun <T, R : Any> Sequence<T>.scan(seed: R, operation: (R, T) -> R): Sequence<R> {
    return scanAndTermintateOnNull(seed, operation)
}

/**
 * Creates a new sequence by scanning the values this sequence. Applies the [operation] from left to right, passing
 * the accumulated value and each next element as arguments. Every result of the operation is a value of
 * the returned sequence.
 *
 * The operation is _intermediate_ and _stateful_.
 */
fun <T : Any> Sequence<T>.scan(operation: (T, T) -> T): Sequence<T> {
    val upstream = this.iterator()

    return when {
        upstream.hasNext() -> {
            val first = upstream.next()
            val seed: T? = if (upstream.hasNext()) operation(first, upstream.next()) else null
            generateSequence(seed) { acc: T ->
                if (upstream.hasNext()) {
                    operation(acc, upstream.next())
                } else {
                    null
                }
            }
        }
        else -> emptySequence()
    }
}
