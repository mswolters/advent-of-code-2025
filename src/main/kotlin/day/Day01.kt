package day

import kotlin.math.absoluteValue

object Day01 : Day {
    override fun part1(input: List<String>): Result {
        val rotations = input.map { it.take(1) to it.substring(1).toInt() }

        var current = 50
        val seenValues = mutableListOf<Int>()
        rotations.forEach { (direction, steps) ->
            current = when (direction) {
                "R" -> (current + steps).mod(100)
                "L" -> (current - steps).mod(100)
                else -> throw IllegalArgumentException()
            }
            seenValues += current
        }

        return seenValues.count { it == 0 }.asSuccess()
    }

    override fun part2(input: List<String>): Result {
        val rotations = input.map { it.take(1) to it.substring(1).toInt() }

        var current = 50
        val seenValues = mutableListOf<Pair<Int, Int>>()
        rotations.forEach { (direction, steps) ->
            val previous = current
            current = when (direction) {
                "R" -> (current + steps)
                "L" -> (current - steps)
                else -> throw IllegalArgumentException()
            }
            val crossedZero = current.floorDiv(100).absoluteValue
            current = current.mod(100)
            var correction = 0
            if (direction == "L") {
                // Correct for landing on/crossing 0
                if (current == 0) {
                    correction += 1
                }
                if (previous == 0) {
                    correction -= 1
                }
            }
            seenValues += current to crossedZero + correction
        }

        return seenValues.sumOf { (_, crossedZero) -> crossedZero }.asSuccess()
    }

    override fun testData(): Day.TestData {
        return Day.TestData(
            3,
            6,
            """
                L68
                L30
                R48
                L5
                R60
                L55
                L1
                L99
                R14
                L82
            """.trimIndent()
                .lines()
        )
    }
}