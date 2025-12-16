package day

object Day07 : Day {
    override fun part1(input: List<String>): Result {
        val firstLine = input.first()
        val beams = MutableList(firstLine.length) { index -> firstLine[index] == 'S' }
        var splits = 0

        input.drop(1).forEach { line ->
            line.forEachIndexed { index, ch ->
                if (beams[index] && ch == '^') {
                    // Split the beam
                    beams[index] = false
                    beams[index - 1] = true
                    beams[index + 1] = true
                    splits++
                }
            }
        }
        return splits.asSuccess()
    }

    override fun part2(input: List<String>): Result {
        val firstLine = input.first()
        val beams = MutableList(firstLine.length) { index -> if (firstLine[index] == 'S') 1L else 0L }

        input.drop(1).forEach { line ->
            line.forEachIndexed { index, ch ->
                if (beams[index] != 0L && ch == '^') {
                    // Split the beam
                    beams[index - 1] += beams[index]
                    beams[index + 1] += beams[index]
                    beams[index] = 0L
                }
            }
        }
        return beams.sum().asSuccess()
    }

    override fun testData(): Day.TestData {
        return Day.TestData(
            21,
            40,
            """
                .......S.......
                ...............
                .......^.......
                ...............
                ......^.^......
                ...............
                .....^.^.^.....
                ...............
                ....^.^...^....
                ...............
                ...^.^...^.^...
                ...............
                ..^...^.....^..
                ...............
                .^.^.^.^.^...^.
                ...............
            """.trimIndent()
                .lines()
        )
    }
}