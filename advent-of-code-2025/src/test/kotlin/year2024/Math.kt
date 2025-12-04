package year2024

fun Long.digits(): Int {
    var num = this
    var count = 0

    do {
        count++
        num /= 10
    } while (num != 0L)

    return count
}

fun Int.digits(): Int {
    var num = this
    var count = 0

    do {
        count++
        num /= 10
    } while (num != 0)

    return count
}

fun Int.pow(index: Int): Long {
    var ret = 1L
    repeat(index) { ret *= this }
    return ret
}

fun Long.toIntExact(): Int {
    return Math.toIntExact(this)
}

/**
 * Repeats the number n times:
 *
 * ```
 * 5.repeat(4) -> 5555
 * 12.repeat(3) -> 121212
 * ```
 */
fun Int.repeat(n: Int): Int {
    val d = digits()
    var result = 0

    repeat(n) {
        result = result * 10.pow(d).toInt() + this
    }

    return result
}

/**
 * Repeats the number n times:
 *
 * ```
 * 5.repeat(4) -> 5555
 * 12.repeat(3) -> 121212
 * ```
 */
fun Long.repeat(n: Int): Long {
    val d = digits()
    var result = 0L

    repeat(n) {
        result = result * 10.pow(d) + this
    }
    // if (result < 0) {return 0}
    check(result >= 0L) { "For $result when asked to repeat $this $n times" }
    return result
}