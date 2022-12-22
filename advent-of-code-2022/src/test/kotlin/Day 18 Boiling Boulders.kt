import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test

class `Day 18 Boiling Boulders` {
  @Test
  fun silverTest() {
    surfaceOf(testData) shouldBe 64
  }

  @Test
  fun silver() {
    surfaceOf(loadResource("day18")) shouldBe 3448
  }
  @Test
  fun goldTest() {
    surfaceOf(testData, excludePockets = true) shouldBe 58
  }

  @Test
  fun gold() {
    surfaceOf(loadResource("day18"), excludePockets = true) shouldBe 2052
  }
  @Test
  fun surfaceOf(data: String, excludePockets: Boolean = false): Int {
    val points =
        data
            .lines()
            .map { line ->
              val (x, y, z) = line.split(",").map { it.toInt() }
              Point3D(x, y, z)
            }
            .toSet()

    val maxX = points.maxOf { it.x }
    val maxY = points.maxOf { it.y }
    val maxZ = points.maxOf { it.z }
    val minX = points.minOf { it.x }
    val minY = points.minOf { it.y }
    val minZ = points.minOf { it.z }

    val surfaceArea = points.sumOf { point -> point.neighbors().count { it !in points } }

    val cache = mutableMapOf<Point3D, Boolean>()
    fun Point3D.isInternal(seen: MutableSet<Point3D> = mutableSetOf(this)): Boolean {
      seen.add(this)
      return when {
        x > maxX || y > maxY || z > maxZ || x < minX || y < minY || z < minZ -> false
        else -> neighbors().minus(seen).all { it in points || it.isInternal(seen) }
      }.also { cache[this] = it }
    }

    val externalOnlySurface =
        points.sumOf { point -> point.neighbors().count { it !in points && !it.isInternal() } }

    return if (excludePockets) externalOnlySurface else surfaceArea
  }

  data class Point3D(
      val x: Int,
      val y: Int,
      val z: Int,
  ) {
    fun neighbors(): List<Point3D> =
        listOf(
            copy(x = x + 1),
            copy(x = x - 1),
            copy(y = y + 1),
            copy(y = y - 1),
            copy(z = z + 1),
            copy(z = z - 1),
        )
  }

  val testData =
      """
2,2,2
1,2,2
3,2,2
2,1,2
2,3,2
2,2,1
2,2,3
2,2,4
2,2,6
1,2,5
3,2,5
2,1,5
2,3,5
    """.trimIndent()
}
