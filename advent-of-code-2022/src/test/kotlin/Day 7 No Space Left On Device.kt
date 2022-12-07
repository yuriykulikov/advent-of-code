import io.kotest.matchers.shouldBe
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.ValueSource

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
  @ParameterizedTest
  @ValueSource(booleans = [true, false])
  fun silverTest(parseInput2: Boolean) {
    val root = if (parseInput2) parseInput2(testInput) else parseInput(testInput)
    root.walk().filter { it.isDir && it.totalSize < 100000 }.sumOf { it.totalSize } shouldBe 95437
  }
  @ParameterizedTest
  @ValueSource(booleans = [true, false])
  fun silver(parseInput2: Boolean) {
    val root =
        if (parseInput2) parseInput2(loadResource("day7")) else parseInput(loadResource("day7"))
    root.walk().filter { it.isDir && it.totalSize < 100000 }.sumOf { it.totalSize } shouldBe 1334506
  }
  @ParameterizedTest
  @ValueSource(booleans = [true, false])
  fun goldTest(parseInput2: Boolean) {
    val root = if (parseInput2) parseInput2(testInput) else parseInput(testInput)
    val needToFree = root.totalSize + 30000000 - 70000000
    root.walk().filter { it.isDir && it.totalSize > needToFree }.map { it.totalSize }.min() shouldBe
        24933642
  }

  @ParameterizedTest
  @ValueSource(booleans = [true, false])
  fun gold(parseInput2: Boolean) {
    val root =
        if (parseInput2) parseInput2(loadResource("day7")) else parseInput(loadResource("day7"))
    val needToFree = root.totalSize + 30000000 - 70000000
    root.walk().filter { it.isDir && it.totalSize > needToFree }.map { it.totalSize }.min() shouldBe
        7421137
  }

  private fun parseInput(input: String): ElfFile {
    val root = ElfFile("/")
    var current = root
    input.lines().forEach { line ->
      when {
        line.startsWith("\$ ls") -> Unit
        line.startsWith("\$ cd ") -> {
          current =
              when (val target = line.substringAfterLast(" ")) {
                "/" -> root
                ".." -> current.parent ?: current
                else -> current.contents.firstOrNull { it.name == target } ?: current
              }
        }
        else -> {
          // dir or file
          val size = line.substringBefore(" ").toIntOrNull() ?: 0
          current.contents.add(ElfFile(line.substringAfter(" "), parent = current, size = size))
        }
      }
    }

    return root
  }

  /** Use an ArrayDeque to keep track of `cd` dir */
  private fun parseInput2(input: String): ElfFile {
    val root = ElfFile("/")
    val stack = ArrayDeque<ElfFile>()
    input.lines().forEach { line ->
      when {
        line.startsWith("\$ ls") -> Unit
        line.startsWith("\$ cd ") -> {
          when (val target = line.substringAfterLast(" ")) {
            "/" -> stack.add(root)
            ".." -> stack.removeLast()
            else -> stack.add(stack.last().contents.first { it.name == target })
          }
        }
        else -> {
          // dir or file
          val size = line.substringBefore(" ").toIntOrNull() ?: 0
          val elfFile = ElfFile(name = line.substringAfter(" "), size = size)
          stack.last().contents.add(elfFile)
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
