import kotlin.random.Random

/** [LeetCode](https://leetcode.com/explore/interview/card/google/65/design-4/3094/) */
class RandomizedSet {
  private val map = mutableMapOf<Int, Int>()
  private val array = mutableListOf<Int>()
  private val random = Random(100500)
  fun insert(`val`: Int): Boolean {
    val prev = map[`val`]
    return if (prev != null) {
      false
    } else {
      // add as the last element and store the index in the map
      array.add(`val`)
      map[`val`] = array.lastIndex
      true
    }
  }
  fun remove(`val`: Int): Boolean {
    // remove from the map
    val indexToRemove = map.remove(`val`)
    return if (indexToRemove == null) {
      false
    } else {
      if (indexToRemove == array.lastIndex) {
        array.removeAt(indexToRemove)
      } else {
        val valueToSwap = array.removeAt(array.lastIndex)
        array[indexToRemove] = valueToSwap
        map[valueToSwap] = indexToRemove
      }
      true
    }
  }

  fun getRandom(): Int {
    return array[random.nextInt(array.size)]
  }
}

/**
 * Your RandomizedSet object will be instantiated and called as such: var obj = RandomizedSet() var
 * param_1 = obj.insert(`val`) var param_2 = obj.remove(`val`) var param_3 = obj.getRandom()
 */
