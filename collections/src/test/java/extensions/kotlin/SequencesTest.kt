package extensions.kotlin

import org.assertj.core.api.Assertions.assertThat
import org.junit.Test

/**
 * Test for extension functions in Sequences.kt
 */
class SequencesTest {

    @Test
    fun `cycle cycles indefinitely`() {
        sequenceOf("a", "b", "c")
                .cycle()
                .take(10)
                .toList()
                .let { assertThat(it) }
                .containsExactly("a", "b", "c", "a", "b", "c", "a", "b", "c", "a")
    }

    @Test
    fun `empty sequence does not cycle`() {
        emptySequence<String>()
                .cycle()
                .toList()
                .let { assertThat(it) }
                .isEmpty()
    }


    @Test
    fun `Scan emits all values returned by the operation`() {
        sequenceOf("a", "b", "c")
                .cycle()
                .take(10)
                .scan { acc, next -> acc + next }
                .toList()
                .let { assertThat(it) }
                .containsExactly("ab", "abc", "abca", "abcab", "abcabc", "abcabca", "abcabcab", "abcabcabc", "abcabcabca")
                .hasSize(9)
    }

    @Test
    fun `Scan with seed emits all values returned by the operation`() {
        sequenceOf("a", "b", "c")
                .cycle()
                .take(10)
                .scan("") { acc, next -> acc + next }
                .toList()
                .let { assertThat(it) }
                .containsExactly("", "a", "ab", "abc", "abca", "abcab", "abcabc", "abcabca", "abcabcab", "abcabcabc", "abcabcabca")
                .hasSize(11)
    }

    @Test
    fun `scanWithNull terminates after the first null is emitted`() {
        sequenceOf("a", "b", "c")
                .cycle()
                .scanAndTermintateOnNull("") { acc, next -> if (acc.length == 5) null else acc + next }
                .toList()
                .let { assertThat(it) }
                .containsExactly("", "a", "ab", "abc", "abca", "abcab")
                .hasSize(6)
    }

    @Test
    fun `scanWithNull terminates with null seed`() {
        sequenceOf("a", "b", "c")
                .cycle()
                .scanAndTermintateOnNull(null) { acc, _ -> acc }
                .toList()
                .let { assertThat(it) }
                .isEmpty()
    }

    @Test
    fun `scan on empty sequence with seed is seed`() {
        emptySequence<String>()
                .scan("") { acc, _ -> acc }
                .toList()
                .let { assertThat(it) }
                .containsOnly("")
    }

    @Test
    fun `scan on empty sequence is emtpy`() {
        emptySequence<String>()
                .scan { acc, _ -> acc }
                .toList()
                .let { assertThat(it) }
                .isEmpty()
    }

    @Test
    fun `scan on empty sequence with null seed is empty`() {
        emptySequence<String>()
                .scanAndTermintateOnNull(null) { acc, _ -> acc }
                .toList()
                .let { assertThat(it) }
                .isEmpty()
    }
}
