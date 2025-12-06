package day

import asLongs
import split
import kotlin.math.max
import kotlin.math.min

object Day05 : Day {
    override fun part1(input: List<String>): Result {
        val (rangeLines, idLines) = input.split("")
        val ranges = rangeLines.map { it.split("-") }
            .map { (first, last) -> LongRange(first.toLong(), last.toLong()) }

        return idLines.asLongs().count { id -> ranges.any { id in it } }.asSuccess()
    }

    override fun part2(input: List<String>): Result {
        val (rangeLines) = input.split("")

        val overlappingRanges = rangeLines.map { it.split("-") }
            .map { (first, last) -> LongRange(first.toLong(), last.toLong()) }

        return stripOverlaps(overlappingRanges, emptyList())
            .sumOf { it.longCount() }.asSuccess()
    }

    private fun stripOverlaps(
        overlappingRanges: List<LongRange>,
        notOverlappingRanges: List<LongRange>
    ): List<LongRange> {
        if (overlappingRanges.isEmpty()) return notOverlappingRanges

        val rangeToCheck = overlappingRanges.first()

        // if the new range is fully inside any other range, continue with the next
        if (notOverlappingRanges.any { rangeToCheck in it }) return stripOverlaps(
            overlappingRanges.drop(1),
            notOverlappingRanges
        )

        // remove any range that is fully inside the new range
        val strippedFullyInside = notOverlappingRanges.filter { it !in rangeToCheck }

        // shrink the new range so it's no longer overlapping with any other
        val toTrimStart = strippedFullyInside.singleOrNull { rangeToCheck.first in it }
        val toTrimEnd = strippedFullyInside.singleOrNull { rangeToCheck.last in it }

        // if the new range falls fully inside 2 other ranges, continue with the next
        if (toTrimStart != null && toTrimEnd != null) {
            if (toTrimStart.last == toTrimEnd.first - 1) {
                if (notOverlappingRanges.any { rangeToCheck in it })
                    return stripOverlaps(overlappingRanges.drop(1), notOverlappingRanges)
            }
        }

        // trim the range so it no longer overlaps with any other
        val trimmedRange = rangeToCheck.run {
            LongRange(max(first, toTrimStart?.last?.plus(1) ?: first), min(last, toTrimEnd?.first?.minus(1) ?: last))
        }
        return stripOverlaps(overlappingRanges.drop(1), strippedFullyInside.plus(element = trimmedRange))
    }

    private infix operator fun LongRange.contains(other: LongRange): Boolean {
        return other.first >= first && other.last <= last
    }

    private fun LongRange.longCount(): Long = last - first + 1

    override fun testData(): Day.TestData {
        return Day.TestData(
            3,
            15,
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
                .lines(),
            """
                3-5
                10-14
                16-20
                9-18

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