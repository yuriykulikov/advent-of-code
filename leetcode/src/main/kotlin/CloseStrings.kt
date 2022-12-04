/**
 * [LeetCode](https://leetcode.com/problems/determine-if-two-strings-are-close/)
 */
class CloseStrings {
    fun closeStrings(word1: String, word2: String): Boolean {
        val chars1 = word1.toCharArray().groupBy { it }.mapValues { (k,count) -> count.size}
        val chars2 = word1.toCharArray().groupBy { it }.mapValues { (k,count) -> count.size}
        return chars1.keys == chars2.keys && chars1.values.sorted() == chars2.values.sorted()
    }
}