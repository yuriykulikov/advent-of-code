import org.junit.Test

class RandomizedSetTest() {
  @Test
  fun test() {
    with(RandomizedSet()) {
      insert(3)
      insert(3)
      getRandom()
      getRandom()
      insert(1)
      remove(3)
      getRandom()
      getRandom()
      insert(0)
      remove(0)
    }
  }
}
