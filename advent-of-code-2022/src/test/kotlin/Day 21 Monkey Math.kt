import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Timeout

/** 12:35 */
@Timeout(45)
class `Day 21 Monkey Math` {
  sealed class Op
  interface HasDependencies {

    val l: String
    val r: String
  }
  data class Times(override val l: String, override val r: String) : Op(), HasDependencies
  data class Div(override val l: String, override val r: String) : Op(), HasDependencies
  data class Plus(override val l: String, override val r: String) : Op(), HasDependencies
  data class Minus(override val l: String, override val r: String) : Op(), HasDependencies
  data class Generate(val value: Long) : Op()
  @Test
  fun silverTest() {
    rootMonkeyYells(testInput) shouldBe 152
  }

  @Test
  fun silver() {
    rootMonkeyYells(loadResource("day21")) shouldBe 87457751482938
  }

  @Test
  fun goldTest() {
    humanYells(testInput) shouldBe 301
  }

  @Test
  fun gold() {
    humanYells(loadResource("day21")) shouldBe 3221245824363
  }

  /** Seems to be a tree */
  private fun rootMonkeyYells(testInput: String): Long {
    val config = parseConfig(testInput)

    fun Op.evaluate(): Long {
      return when (this) {
        is Generate -> value
        is Div -> config.getValue(l).evaluate() / config.getValue(r).evaluate()
        is Plus -> config.getValue(l).evaluate() + config.getValue(r).evaluate()
        is Minus -> config.getValue(l).evaluate() - config.getValue(r).evaluate()
        is Times -> config.getValue(l).evaluate() * config.getValue(r).evaluate()
      }
    }

    return config.getValue("root").evaluate()
  }
  private fun humanYells(testInput: String): Long {
    val config = parseConfig(testInput)
    val cache = buildCache(config)
    fun String.cached() = cache.getValue(this)

    /** Push down what we need and return the answer in the end. humn is the deepest element. */
    fun backPropagate(name: String, needed: Long): Long {
      return when (val op = config.getValue(name)) {
        is Generate -> {
          check(name == "humn")
          needed
        }
        is Times ->
            if (op.l in cache) {
              backPropagate(name = op.r, needed = needed / op.l.cached())
            } else {
              backPropagate(name = op.l, needed = needed / op.r.cached())
            }
        is Plus ->
            if (op.l in cache) {
              backPropagate(name = op.r, needed = needed - op.l.cached())
            } else {
              backPropagate(name = op.l, needed = needed - op.r.cached())
            }
        is Minus -> {
          if (op.l in cache) {
            backPropagate(name = op.r, needed = op.l.cached() - needed)
          } else {
            backPropagate(name = op.l, needed = needed + op.r.cached())
          }
        }
        is Div -> {
          if (op.l in cache) {
            backPropagate(name = op.r, needed = op.l.cached() / needed)
          } else {
            backPropagate(name = op.l, needed = needed * op.r.cached())
          }
        }
      }
    }

    val root = config.getValue("root") as HasDependencies
    return if (root.r in cache) {
      backPropagate(root.l, root.r.cached())
    } else {
      backPropagate(root.r, root.r.cached())
    }
  }

  /** Build a map of nodes which are independent of "humn" */
  private fun buildCache(config: Map<String, Op>): Map<String, Long> {
    val cache = mutableMapOf<String, Long>()

    fun cachable(name: String, op: Op): Boolean {
      if (name == "humn") return false
      if (op is Generate) return true
      return op is HasDependencies &&
          cachable(op.l, config.getValue(op.l)) &&
          cachable(op.r, config.getValue(op.r))
    }

    fun evaluate(name: String): Long {
      val op = config.getValue(name)
      val value =
          when (op) {
            is Generate -> op.value
            is Div -> evaluate(op.l) / evaluate(op.r)
            is Plus -> evaluate(op.l) + evaluate(op.r)
            is Minus -> evaluate(op.l) - evaluate(op.r)
            is Times -> evaluate(op.l) * evaluate(op.r)
          }
      if (cachable(name, op)) {
        cache[name] = value
      }
      return value
    }

    evaluate("root")

    return cache
  }

  private fun parseConfig(testInput: String) =
      testInput.lines().associate { line ->
        val (name, op) = line.split(": ")
        name to
            when {
              " * " in op -> Times(op.substringBefore(" * "), op.substringAfter(" * "))
              " / " in op -> Div(op.substringBefore(" / "), op.substringAfter(" / "))
              " + " in op -> Plus(op.substringBefore(" + "), op.substringAfter(" + "))
              " - " in op -> Minus(op.substringBefore(" - "), op.substringAfter(" - "))
              else -> Generate(op.toLong())
            }
      }

  private val testInput =
      """
root: pppw + sjmn
dbpl: 5
cczh: sllz + lgvd
zczc: 2
ptdq: humn - dvpt
dvpt: 3
lfqf: 4
humn: 5
ljgn: 2
sjmn: drzm * dbpl
sllz: 4
pppw: cczh / lfqf
lgvd: ljgn * ptdq
drzm: hmdt - zczc
hmdt: 32
    """.trimIndent()
}
