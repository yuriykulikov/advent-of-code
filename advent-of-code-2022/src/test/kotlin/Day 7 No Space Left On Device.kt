import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test

class `Day 7 No Space Left On Device` {
  data class ElfFile(
      val name: String,
      val parent: ElfFile? = null,
      val size: Int = 0,
      val contents: MutableList<ElfFile> = mutableListOf(),
  ) {
    val totalSize: Int by lazy { size + contents.sumOf { it.totalSize } }
    val isDir = size == 0
    fun walk(): Sequence<ElfFile> {
      return sequenceOf(this) + contents.flatMap { it.walk() }
    }
  }
  @Test
  fun silverTest() {
    val root = parseInput(testInput)
    root.walk().filter { it.isDir && it.totalSize < 100000 }.sumOf { it.totalSize } shouldBe 95437
  }
  @Test
  fun silver() {
    val root = parseInput(loadResource("day7"))
    root.walk().filter { it.isDir && it.totalSize < 100000 }.sumOf { it.totalSize } shouldBe 1334506
  }
  @Test
  fun goldTest() {
    val root = parseInput(testInput)
    smallestFolderToDelete(root) shouldBe 24933642
  }

  @Test
  fun gold() {
    val root = parseInput(loadResource("day7"))
    smallestFolderToDelete(root) shouldBe 7421137
  }
  private fun smallestFolderToDelete(root: ElfFile): Int {
    val needToFree = root.totalSize + 30000000 - 70000000
    return root.walk().filter { it.isDir && it.totalSize > needToFree }.map { it.totalSize }.min()
  }

  private fun parseInput(input: String): ElfFile {
    val root = ElfFile("/")
    var current = root
    input.trim().lines().forEach { line ->
      val left = line.substringBeforeLast(" ")
      val right = line.substringAfterLast(" ")
      when (left) {
        "\$ ls" -> Unit
        "\$ cd" -> {
          current =
              when (right) {
                "/" -> root
                ".." -> current.parent ?: current
                else -> current.contents.firstOrNull { it.name == right } ?: current
              }
        }
        else -> {
          // dir or file
          val size = left.toIntOrNull() ?: 0
          current.contents.add(ElfFile(right, parent = current, size = size))
        }
      }
    }

    return root
  }
}

private val testInput =
    """
$ cd /
$ ls
dir a
14848514 b.txt
8504156 c.dat
dir d
$ cd a
$ ls
dir e
29116 f
2557 g
62596 h.lst
$ cd e
$ ls
584 i
$ cd ..
$ cd ..
$ cd d
$ ls
4060174 j
8033020 d.log
5626152 d.ext
7214296 k    
""".trimIndent()
