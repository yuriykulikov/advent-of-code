package year2025

import Point
import org.junit.jupiter.api.Test
import kotlin.math.abs

class Day8Test {
    private val example =
        """
162,817,812
57,618,57
906,360,560
592,479,940
352,342,300
466,668,158
542,29,236
431,825,988
739,650,466
52,470,668
216,146,977
819,987,18
117,168,530
805,96,715
346,949,466
970,615,88
941,993,340
862,61,35
984,92,344
425,690,689
        """.trimIndent()

    data class Point3D(
        val x: Int,
        val y: Int,
        val z: Int,
    )

    @Test
    fun `Silver example - multiply together the sizes of the three largest circuits`() {
        val junctions = example.lines().map { line ->
            val (x, y, z) = line.split(",").map { it.toInt() }
            Point3D(x, y, z)
        }

        junctions.forEach { junction ->
            junctions.forEach { other ->
                println("$junction -> $other ${distance(junction, other)}")
            }
        }
    }

    private fun distance(a: Point3D, b: Point3D): Long {
        return abs(a.x - b.x).pow(2) + abs(a.x - b.x).pow(2) + abs(a.x - b.x).pow(2)
    }
}