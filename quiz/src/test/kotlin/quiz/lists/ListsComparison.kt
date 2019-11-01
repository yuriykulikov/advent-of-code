package quiz.lists

import kotlinx.collections.immutable.persistentListOf
import org.junit.FixMethodOrder
import org.junit.Test
import org.junit.runners.MethodSorters

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
class ListsComparison {
    val times = 10000000

    @Test
    fun aaa_warmUp1() {
        arrayList()
        arrayBlist()
        arrayListPers()
    }

    @Test
    fun arrayList() {

        val list = mutableListOf<Int>()

        repeat(times) {
            list.add(it)
        }

    }

    @Test
    fun arrayListPers() {

        var list = persistentListOf<Int>()

        repeat(times) {
            list = list.add(it)
        }

    }

    @Test
    fun arrayBlist() {

        var list = List.from(0)

        repeat(times) {
            list = Cons(it, list)
        }

    }
}


sealed class List<out A> {

    abstract val head: A

    abstract val tail: List<A>

    fun <B> foldLeft(z: B, f: (B, A) -> B): B {
        tailrec fun foldLeft(l: List<A>, z: B, f: (B, A) -> B): B {
            return when (l) {
                is Nil -> z
                is Cons -> foldLeft(l.tail, f(z, l.head), f)
            }
        }
        return foldLeft(this, z, f)
    }

    fun <B> foldRight(z: B, f: (A, B) -> B): B {
        return when (this) {
            is Cons -> f(this.head, tail.foldRight(z, f))
            is Nil -> z
        }
    }

    fun reverse(): List<A> {
        return foldLeft(Nil as List<A>, { b, a ->
            Cons(
                a,
                b
            )
        })
    }

    fun <B> foldRightViaFoldLeft(z: B, f: (A, B) -> B): B {
        return reverse().foldLeft(z, { b, a -> f(a, b) })
    }

    fun <B> map(f: (A) -> B): List<B> {
        tailrec fun map(acc: List<B>, next: List<A>): List<B> =
            when (next) {
                is Nil -> acc
                is Cons -> map(Cons(f(next.head), acc), next.tail)
            }
        return map(Nil as List<B>, this).reverse()
    }

    /**
     * unsafe variance is ok here because List is immutable
     */
    fun <B> flatMap(f: (a: A) -> List<@UnsafeVariance B>): List<B> {
        return flatten(map { a -> f(a) })
    }

    /**
     * unsafe variance is ok here because List is immutable
     */
    fun append(l: List<@UnsafeVariance A>): List<A> {
        tailrec fun append(acc: List<A>, toAppend: List<A>, next: List<A>): List<A> =
            when (next) {
                is Nil -> acc
                is Cons -> {
                    when (toAppend) {
                        is Nil -> append(Cons(next.head, acc), Nil, next.tail)
                        is Cons -> append(Cons(toAppend.head, acc), toAppend.tail, next)
                    }
                }
            }
        return append(Nil, l.reverse(), this.reverse())
    }

    /**
     * unsafe variance is ok here because List is immutable
     */
    fun find(a: @UnsafeVariance A): List<A> {
        tailrec fun find(list: List<A>): List<A> =
            when (list) {
                is Nil -> Nil
                is Cons -> if (list.head == a) list else find(list.tail)
            }
        return find(this)
    }

    /**
     * unsafe variance is ok here because List is immutable
     */
    fun contains(a: @UnsafeVariance A): Boolean {
        return find(a) != Nil
    }

    companion object {
        private fun <A> flatten(l: List<List<A>>): List<A> {
            return l.foldRight(Nil as List<A>, { a, b -> a.append(b) })
        }

        fun <A> from(vararg params: A): List<A> {
            return params.foldRight(Nil as List<A>, { h, t ->
                Cons(h, t)
            })
        }
    }
}

data class Cons<out T>(override val head: T, override val tail: List<T>) : List<T>() {
    override fun toString(): String {
        return "$head :: $tail"
    }
}

object Nil : List<Nothing>() {
    override val head: Nothing
        get() {
            throw NoSuchElementException("head of an empty list")
        }

    override val tail: List<Nothing>
        get() {
            throw NoSuchElementException("tail of an empty list")
        }

    override fun toString(): String {
        return javaClass.simpleName
    }
}

fun main() {
    val x = List.from(1, 2, 3, 4, 5)
    val y = List.from(6, 7, 8, 9, 10)

    println(x.map { it * 2 })
    println(x.append(y))
    println(x.find(3))
}

