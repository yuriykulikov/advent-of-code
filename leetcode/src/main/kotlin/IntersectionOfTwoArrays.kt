import kotlin.math.min

/**
 * [LeetCode](https://leetcode.com/problems/intersection-of-two-arrays-ii/)
 */
class Solution {
  fun intersect(nums1: IntArray, nums2: IntArray): IntArray {
    val map1 = nums1.groupBy { it }.mapValues { (k, v) -> v.size }
    val map2 = nums2.groupBy { it }.mapValues { (k, v) -> v.size }
    val out = mutableListOf<Int>()
    map1
        .mapValues { (k, v) -> min(v, map2[k] ?: 0) }
        .forEach { (value, count) -> repeat(count) { out.add(value) } }
    return out.toIntArray()
  }
}
