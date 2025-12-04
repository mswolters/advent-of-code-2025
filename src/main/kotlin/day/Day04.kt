package day

import MutableRectangle
import Rectangle
import neighbours

object Day04 : Day {
    override fun part1(input: List<String>): Result {
        val rect = Rectangle(input.first().length, input.size) { x, y -> input[y][x] }
        return rect.indexedIterator().asSequence().filter { (_, c) -> c == '@' }
            .count { (coordinate, _) ->
                rect.neighbours(coordinate, true)
                    .map { (_, it) -> it }
                    .count { (_, c) -> c == '@' } < 4
            }
            .asSuccess()
    }

    override fun part2(input: List<String>): Result {
        val rect = MutableRectangle(input.first().length, input.size) { x, y -> input[y][x] }
        var result = 0
        do {
            val rollsToRemove = rect.indexedIterator().asSequence().filter { (_, c) -> c == '@' }
                .filter { (coordinate, _) ->
                    rect.neighbours(coordinate, true)
                        .map { (_, it) -> it }
                        .count { (_, c) -> c == '@' } < 4
                }
            val removedRolls = rollsToRemove.toList()
            result += removedRolls.count()
            removedRolls.forEach { (coordinate, _) -> rect[coordinate] = '.' }
        } while (removedRolls.isNotEmpty())
        return result.asSuccess()
    }

    override fun testData(): Day.TestData {
        return Day.TestData(
            13,
            43,
            """
                ..@@.@@@@.
                @@@.@.@.@@
                @@@@@.@.@@
                @.@@@@..@.
                @@.@@@@.@@
                .@@@@@@@.@
                .@.@.@.@@@
                @.@@@.@@@@
                .@@@@@@@@.
                @.@.@@@.@.
            """.trimIndent()
                .lines()
        )
    }
}