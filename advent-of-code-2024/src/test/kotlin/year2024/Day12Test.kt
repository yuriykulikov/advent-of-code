package year2024

import Point
import down
import io.kotest.matchers.collections.shouldHaveSize
import io.kotest.matchers.shouldBe
import left
import loadResource
import org.junit.jupiter.api.Test
import parseMap
import right
import up

class Day12Test {
    private val example = """
        RRRRIICCFF
        RRRRIICCCF
        VVRRRCCFFF
        VVRCCCJFFF
        VVVVCJJCFE
        VVIVCCJJEE
        VVIIICJJEE
        MIIIIIJJEE
        MIIISIJEEE
        MMMISSJEEE
    """.trimIndent()

    @Test
    fun `silver example`() {
        findRegions(parseMap(example)).shouldHaveSize(11).sumOf { region ->
            region.size * region.perimeter()
        }.shouldBe(1930)
    }

    @Test
    fun `silver test`() {
        val map = parseMap(loadResource("Day12"))
        val regions = findRegions(map)

        regions.sumOf { region ->
            region.size * region.perimeter()
        }.shouldBe(1424472)
    }

    @Test
    fun `gold example`() {
        scoreOf(parseMap(example)).shouldBe(1206)
    }

    @Test
    fun `gold test`() {
        scoreOf(parseMap(loadResource("Day12"))).shouldBe(870202)
    }

    private fun scoreOf(map: Map<Point, Char>) = findRegions(map).map { region -> scale(region) }
        .map { region -> region to region.perimiterWithCorners().corners() }.sumOf { (region, corners) ->
            // (region.associateWith { "." } + corners.associateWith { "*" }).print()
            // descale the size
            region.size / 9 * corners.size
        }

    private fun Set<Point>.corners(): Set<Point> {

        return filter {
            val intersect = it.neighbors().intersect(this)
            intersect.size == 2 && intersect.first().x != intersect.last().x && intersect.first().y != intersect.last().y
        }.toSet()
    }

    private fun scale(region: Set<Point>): Set<Point> {
        return region.flatMap {
            val center = it.copy(it.x * 3, it.y * 3)
            center.surroundings() + center
        }.toSet()
    }

    fun Set<Point>.perimiterWithCorners(): Set<Point> {
        val region = this
        return region.flatMap { point ->
            point.surroundings().minus(region)
        }.toSet()
    }

    /**
     * Perimeter as expected for silver - no corners
     */
    fun Set<Point>.perimeter(): Int {
        val region = this
        return region.sumOf { point ->
            point.neighbors().minus(region).size
        }
    }

    /**
     * Finds all regions in the map by simple floodfill.
     */
    private fun findRegions(map: Map<Point, Char>): List<Set<Point>> {
        val acc = mutableSetOf<Set<Point>>()
        return map.keys.asSequence().filter { candidate -> acc.none { candidate in it } }
            .map { k -> floodFillFrom(map, k) }.onEach { acc.add(it) }.toList().distinct()
    }

    private fun floodFillFrom(map: Map<Point, Char>, origin: Point): Set<Point> {
        val fill = mutableSetOf(origin)
        generateSequence(listOf(origin)) { front ->
            front.asSequence()
                .flatMap { it.neighbors() }
                .filter { map[it] == map[origin] }
                .filter { it !in fill }
                .onEach { fill += it }
                .toList()
        }.first { it.isEmpty() }

        return fill
    }

    private fun Point.neighbors(): List<Point> = listOf(up(), down(), left(), right())

    private fun Point.surroundings(): List<Point> {
        return listOf(
            up(), down(), left(), right(), up().left(), up().right(), down().left(), down().right()
        )
    }

}