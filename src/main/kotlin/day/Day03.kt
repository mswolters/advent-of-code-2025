package day

object Day03 : Day {
    override fun part1(input: List<String>): Result {
        return input.map { line -> line.toCharArray().map { c -> c.digitToInt() } }
            .sumOf { findMaxJoltage(it) }
            .asSuccess()
    }

    private fun findMaxJoltage(bank: List<Int>): Int {
        var max = bank.max()
        var indexMax = bank.indexOf(max)
        if (indexMax == bank.size - 1) {
           max = bank.filter { it != max }.max()
           indexMax = bank.indexOf(max)
        }
        val secondMax = bank.drop(indexMax + 1).max()
        return max * 10 + secondMax
    }

    override fun part2(input: List<String>): Result {
        return NotImplemented
    }

    override fun testData(): Day.TestData {
        return Day.TestData(
            357,
            0,
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