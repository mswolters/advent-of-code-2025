package day

import pow

object Day03 : Day {
    override fun part1(input: List<String>): Result {
        return input.map { line -> line.toCharArray().map { c -> c.digitToInt() } }
            .sumOf { findMaxJoltage(it) }
            .asSuccess()
    }

    private fun findMaxJoltage(bank: List<Int>, batteriesToUse: Int = 2): Long {
        val max = bank.take(bank.size - batteriesToUse + 1).max()
        if (batteriesToUse == 1) return max.toLong()
        val indexOfMax = bank.indexOf(max)
        return max * 10L.pow(batteriesToUse - 1) + findMaxJoltage(bank.drop(indexOfMax + 1), batteriesToUse - 1)
    }

    override fun part2(input: List<String>): Result {
        return input.map { line -> line.toCharArray().map { c -> c.digitToInt() } }
            .sumOf { findMaxJoltage(it, 12) }
            .asSuccess()
    }

    override fun testData(): Day.TestData {
        return Day.TestData(
            357,
            3121910778619,
            """
                987654321111111
                811111111111119
                234234234234278
                818181911112111
            """.trimIndent()
                .lines()
        )
    }
}