import io.kotest.matchers.shouldBe
import org.junit.Test

class PostfixToInfix {

  data class Expression(val string: String, val prio: Int)
  private fun toInfix(postfix: String): String {
    val iter = postfix.split(" ").reversed().iterator()
    val toInfix = toInfix(iter)
    check(!iter.hasNext()) { "Invalid str, unused: ${iter.asSequence().joinToString()}" }
    return toInfix.string
  }
  private fun toInfix(iter: Iterator<String>): Expression {
    check(iter.hasNext()) { "Invalid input" }
    return when (val next = iter.next()) {
      in "+-*/" -> {
        val operationPrio = if (next in "+-") 0 else 1
        val (right, rightPrio) = toInfix(iter)
        val (left, leftPrio) = toInfix(iter)
        // add braces if operand has lower prio then current operation
        val leftStr = if (leftPrio < operationPrio) "($left)" else left
        val rightStr = if (rightPrio < operationPrio) "($right)" else right
        Expression("$leftStr $next $rightStr", operationPrio)
      }
      else -> {
        check(next.toIntOrNull() != null) { "Not an Int" }
        Expression(next, 1)
      }
    }
  }
  @Test
  fun toInfixSimpleTest() {
    toInfix("1 2 + 3 4 + *") shouldBe "(1 + 2) * (3 + 4)"
    toInfix("1 2 * 3 4 + +") shouldBe "1 * 2 + 3 + 4"
    toInfix("1 2 + 3 4 * +") shouldBe "1 + 2 + 3 * 4"
    toInfix("1 2 + 3 * 4 +") shouldBe "(1 + 2) * 3 + 4"
    toInfix("1 2 + 3 * 4 / ") shouldBe "(1 + 2) * 3 + 4"
  }

  private fun toInfixWithOp(postfix: String): String {
    val iterator = postfix.split(" ").reversed().iterator()
    return toInfixWithOp(iterator).toString()
  }
  sealed class Op {
    abstract val prio: Int
  }
  data class Operation(
      val left: Op,
      val right: Op,
      val code: String,
  ) : Op() {
    override fun toString(): String {
      val leftNeedsBrackets = left.prio < prio
      val rightNeedsBrackets = right.prio < prio
      val leftStr = if (leftNeedsBrackets) "($left)" else left.toString()
      val rightStr = if (rightNeedsBrackets) "($right)" else right.toString()
      return "$leftStr $code $rightStr"
    }

    override val prio: Int
      get() = if (code == "*" || code == "/") 2 else 1
  }
  data class Value(
      val value: String,
  ) : Op() {
    override val prio: Int
      get() = 2

    override fun toString(): String {
      return value
    }
  }
  private fun toInfixWithOp(iter: Iterator<String>): Op {
    return when (val next = iter.next()) {
      "+",
      "-",
      "*",
      "/" -> {
        Operation(
            right = toInfixWithOp(iter),
            left = toInfixWithOp(iter),
            code = next,
        )
      }
      else -> {
        Value(next)
      }
    }
  }

  @Test
  fun toInfixOpTest() {
    toInfixWithOp("1 2 + 3 4 + *") shouldBe "(1 + 2) * (3 + 4)"
    toInfixWithOp("1 2 * 3 4 + +") shouldBe "1 * 2 + 3 + 4"
    toInfixWithOp("1 2 + 3 4 * +") shouldBe "1 + 2 + 3 * 4"
    toInfixWithOp("1 2 + 3 * 4 +") shouldBe "(1 + 2) * 3 + 4"
  }
}
