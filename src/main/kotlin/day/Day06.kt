package day

import asLongs
import multiplied
import toRectangle

object Day06 : Day {
    val anySpaces = Regex("""\s+""")
    override fun part1(input: List<String>): Result {
        return input.map { it.trim().split(anySpaces) }
            .toRectangle()
            .columns()
            .sumOf { calculateProblem(it) }
            .asSuccess()
    }

    private fun calculateProblem(problem: List<String>): Long {
        val op = problem.last()
        val numbers = problem.dropLast(1).asLongs()

        return when (op) {
            "+" -> numbers.sum()
            "*" -> numbers.multiplied()
            else -> throw IllegalArgumentException("unknown op $op")
        }
    }

    override fun part2(input: List<String>): Result {
        return NotImplemented
    }

    override fun testData(): Day.TestData {
        return Day.TestData(
            4277556,
            0,
            """
                123 328  51 64 
                 45 64  387 23 
                  6 98  215 314
                *   +   *   + 
            """.trimIndent()
                .lines()
        )
    }
}