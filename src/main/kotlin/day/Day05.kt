package day

import asLongs
import split

object Day05 : Day {
    override fun part1(input: List<String>): Result {
        val (rangeLines, idLines) = input.split("")
        val ranges = rangeLines.map { it.split("-") }
            .map { (first, last) -> LongRange(first.toLong(), last.toLong()) }

        return idLines.asLongs().count { id -> ranges.any { id in it } }.asSuccess()
    }

    override fun part2(input: List<String>): Result {
        return NotImplemented
    }

    override fun testData(): Day.TestData {
        return Day.TestData(
            3,
            0,
            """
                3-5
                10-14
                16-20
                12-18

                1
                5
                8
                11
                17
                32
            """.trimIndent()
                .lines()
        )
    }
}