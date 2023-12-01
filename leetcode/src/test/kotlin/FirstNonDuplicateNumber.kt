import io.kotest.matchers.shouldBe
import org.junit.Test

class FirstNonDuplicateNumber {
  @Test
  fun nonDup() {
    // Найти первый неповторяющийся элемент в наборе целых чисел.
    // Например для набора {3,4,9,4,3,8,3} должно вернуться значение 9
    listOf(3, 4, 9, 4, 3, 8, 3)
        .groupBy { it }
        .entries
        .first { (_, duplicates) -> duplicates.size == 1 }
        .key shouldBe 9

    listOf(3, 4, 9, 4, 3, 8, 3).groupBy { it }.firstNotNullOf { (k,v) -> k.takeIf { v.size == 1 } } shouldBe 9
  }
}
