package day

import allPairs
import asInts
import kotlin.math.abs

object Day09 : Day {

    data class Tile(val x: Int, val y: Int)

    override fun part1(input: List<String>): Result {
        val tiles = input.map { it.split(",").asInts() }.map { (x, y) -> Tile(x, y) }

        return tiles.allPairs()
            .maxOf { (first, last) -> (abs(first.x - last.x) + 1) * (abs(first.y - last.y) + 1).toLong() }
            .asSuccess()
    }

    override fun part2(input: List<String>): Result {
        return NotImplemented
    }

    override fun testData(): Day.TestData {
        return Day.TestData(
            50,
            0,
            """
                7,1
                11,1
                11,7
                9,7
                9,5
                2,5
                2,3
                7,3
            """.trimIndent()
                .lines()
        )
    }
}