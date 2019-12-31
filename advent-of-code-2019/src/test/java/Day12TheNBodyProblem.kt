import org.assertj.core.api.Assertions.assertThat
import org.junit.Test
import kotlin.math.absoluteValue

class Day12TheNBodyProblem {
    data class Vector(val x: Int, val y: Int, val z: Int) {
        operator fun plus(vel: Vector): Vector {
            return Vector(x + vel.x, y + vel.y, z + vel.z)
        }
    }

    data class Body(val pos: Vector, val vel: Vector)

    fun parse(input: String): List<Body> {
        return input.lines()
            .map {
                val split = it.split("pos=<x=", "=", ",", ">")
                Body(
                    Vector(
                        split[1].trim().toInt(),
                        split[3].trim().toInt(),
                        split[5].trim().toInt()
                    ),
                    Vector(
                        split.getOrNull(9)?.trim()?.toInt() ?: 0,
                        split.getOrNull(11)?.trim()?.toInt() ?: 0,
                        split.getOrNull(13)?.trim()?.toInt() ?: 0
                    )
                )
            }
    }

    @Test
    fun verifySilverTestInput() {
        runSimulation(parse(testInput))
            .take(11)
            .toList()
            .also {
                val first = parse(
                    """pos=<x= 2, y=-1, z= 1>, vel=<x= 3, y=-1, z=-1>
        pos=<x= 3, y=-7, z=-4>, vel=<x= 1, y= 3, z= 3>
        pos=<x= 1, y=-7, z= 5>, vel=<x=-3, y= 1, z=-3>
        pos=<x= 2, y= 2, z= 0>, vel=<x=-1, y=-3, z= 1>"""
                )
                assertThat(it[1]).isEqualTo(first)
            }
            .also {
                val first = parse(
                    """pos=<x= 5, y=-3, z=-1>, vel=<x= 3, y=-2, z=-2>
pos=<x= 1, y=-2, z= 2>, vel=<x=-2, y= 5, z= 6>
pos=<x= 1, y=-4, z=-1>, vel=<x= 0, y= 3, z=-6>
pos=<x= 1, y=-4, z= 2>, vel=<x=-1, y=-6, z= 2>"""
                )
                assertThat(it[2]).isEqualTo(first)
            }
            .also {
                val first = parse(
                    """pos=<x= 2, y= 1, z=-3>, vel=<x=-3, y=-2, z= 1>
pos=<x= 1, y=-8, z= 0>, vel=<x=-1, y= 1, z= 3>
pos=<x= 3, y=-6, z= 1>, vel=<x= 3, y= 2, z=-3>
pos=<x= 2, y= 0, z= 4>, vel=<x= 1, y=-1, z=-1>"""
                )
                assertThat(it[10]).isEqualTo(first)
            }
            .also {
                assertThat(it.last().sumBy { it.energy() }).isEqualTo(179)
            }
    }

    fun Body.energy(): Int = (pos.x.absoluteValue + pos.y.absoluteValue + pos.z.absoluteValue) *
            (vel.x.absoluteValue + vel.y.absoluteValue + vel.z.absoluteValue)

    @Test
    fun silverTest() {
        val initial = parse(testInput)
        println(initial)

        runSimulation(initial)
            .take(11)
            .last()
            .sumBy { it.energy() }
            .let { assertThat(it).isEqualTo(179) }
    }

    @Test
    fun silverTest2() {
        val initial = parse(testInput.trimIndent())
        val answer = runSimulation(initial)
            .drop(1)
            //.takeWhile { !set.contains(it) }
            .takeWhile { it != initial }
            //.onEach { set.add(it) }
            .count() + 1
        assertThat(answer).isEqualTo(2772)
    }

    @Test
    fun silver() {
        val initial = parse(realInput)
        println(initial)
        runSimulation(initial)
            .take(1001)
            .last()
            .sumBy { it.energy() }
            .let { assertThat(it).isEqualTo(7758) }
    }

    @Test
    fun goldTest() {
        val initial = parse(testInput)
        // val initial = parse(testInput.trimIndent())
        (0..3).map { index ->
            val countX = runSimulation(initial)
                .drop(1)
                //.takeWhile { !set.contains(it) }
                .takeWhile { it[index].pos.x != initial[index].pos.x }
                .count() + 1
            val countY = runSimulation(initial)
                .drop(1)
                //.takeWhile { !set.contains(it) }
                .takeWhile { it[index].pos.y != initial[index].pos.y }
                .count() + 1
            val countZ = runSimulation(initial)
                .drop(1)
                //.takeWhile { !set.contains(it) }
                .takeWhile { it[index].pos.z != initial[index].pos.z }
                .count() + 1
            println(countX * countY * countZ)
            countX
        }
            .forEach { println(it) }
    }


    // 4686774924
    @Test
    fun goldTest2() {
        val initial = parse(realInput.trimIndent())
        // val initial = parse(testInput.trimIndent())

        var lastX: List<Pair<Int, Int>>? = null
        var lastY: List<Pair<Int, Int>>? = null
        var lastZ: List<Pair<Int, Int>>? = null
        val setX = mutableSetOf<List<Pair<Int, Int>>>()
        runSimulation(initial)
            .map { moons -> moons.map { moon -> moon.pos.x to moon.vel.x } }
            .takeWhile {
                lastX = it
                it !in setX
            }
            .forEach { setX.add(it) }

        val setY = mutableSetOf<List<Pair<Int, Int>>>()
        runSimulation(initial)
            .map { moons -> moons.map { moon -> moon.pos.y to moon.vel.y } }
            .takeWhile {
                lastY = it

                it !in setY
            }
            .forEach { setY.add(it) }

        val setZ = mutableSetOf<List<Pair<Int, Int>>>()
        runSimulation(initial)
            .map { moons -> moons.map { moon -> moon.pos.z to moon.vel.z } }
            .takeWhile {
                lastZ = it
                it !in setZ
            }
            .forEach { setZ.add(it) }

        val pX = setX.size.toLong()
        val pY = setY.size.toLong()
        val pZ = setZ.size.toLong()

        println("X size: ${pX}")
        println("Y size: ${pY}")
        println("Z size: ${pZ}")

        // 4686774924
        // 2028 2 ∙ 2 ∙ 3 ∙ 13 ∙ 13
        // 5898 /2 ∙ /3 ∙ 983
        // 4702 /2 ∙ 2351

        println(4686774924 / (13L * 13L * 983L * 2351L * 2 * 2 * 3))

        // REAL
        // X size: 113028 /2 ∙ /2 ∙ 3 ∙ 9419
        // Y size: 231614 /2 ∙ 115807
        // Z size: 108344 2 ∙ 2 ∙ 2 ∙ 29 ∙ 467

        println(3L * 9419L * 115807L * 2 * 2 * 2 * 29 * 467)
    }

    private fun runSimulation(initial: List<Body>): Sequence<List<Body>> {
        return generateSequence(initial) { bodies ->
            // apply gravity
            val withVelocity = bodies.map { body ->
                val dX = bodies.minus(body).map { otherBody -> -body.pos.x.compareTo(otherBody.pos.x) }.sum()
                val dY = bodies.minus(body).map { otherBody -> -body.pos.y.compareTo(otherBody.pos.y) }.sum()
                val dZ = bodies.minus(body).map { otherBody -> -body.pos.z.compareTo(otherBody.pos.z) }.sum()
                body.copy(vel = body.vel + Vector(dX, dY, dZ))
            }

            // apply velocity
            withVelocity.map { body ->
                body.copy(pos = body.pos + body.vel)
            }
        }
    }

    val longTest = """
<x=-8, y=-10, z=0>
<x=5, y=5, z=10>
<x=2, y=-7, z=3>
<x=9, y=-8, z=-3>
""".trimIndent()

    val testInput = """
<x=-1, y=0, z=2>
<x=2, y=-10, z=-7>
<x=4, y=-8, z=8>
<x=3, y=5, z=-1>
""".trimIndent()

    val realInput = """
<x=9, y=13, z=-8>
<x=-3, y=16, z=-17>
<x=-4, y=11, z=-10>
<x=0, y=-2, z=-2>
""".trimIndent()
}