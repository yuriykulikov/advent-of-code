import io.kotest.matchers.shouldBe
import org.junit.Test

class SerializeAndDeserializeBinaryTreeTest {
  @Test
  fun test() {
    val data = "[1,2,3,null,null,4,5]"
    val codec = SerializeAndDeserializeBinaryTree()
    codec.serialize(codec.deserialize(data)) shouldBe data
  }

  @Test
  fun testEmpty() {
    val data = "[]"
    val codec = SerializeAndDeserializeBinaryTree()
    codec.serialize(codec.deserialize(data)) shouldBe data
  }
}
