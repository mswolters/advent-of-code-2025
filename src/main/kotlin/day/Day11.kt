package day

object Day11 : Day {

    override fun part1(input: List<String>): Result {
        val devices = input.associate { line ->
            val splitput = line.split(": ")
            val node = splitput.first()
            val edges = splitput.last().split(" ")
            node to edges
        }

        return countPaths(devices, "you", "out").asSuccess()
    }

    private fun countPaths(
        devices: Map<String, List<String>>,
        from: String,
        to: String,
        memo: MutableMap<Pair<String, String>, Long> = mutableMapOf()
    ): Long = memo.getOrPut(from to to) {
        if (from == to) return@getOrPut 1
        if (from !in devices) return@getOrPut 0
        val connections = devices[from]!!

        return@getOrPut connections.sumOf { countPaths(devices, it, to, memo) }
    }

    override fun part2(input: List<String>): Result {
        val devices = input.associate { line ->
            val splitput = line.split(": ")
            val node = splitput.first()
            val edges = splitput.last().split(" ")
            node to edges
        }

        return (
                countPaths(devices, "svr", "fft")
                        * countPaths(devices, "fft", "dac")
                        * countPaths(devices, "dac", "out")
                ).asSuccess()

    }

    override fun testData(): Day.TestData {
        return Day.TestData(
            5,
            2,
            """
                aaa: you hhh
                you: bbb ccc
                bbb: ddd eee
                ccc: ddd eee fff
                ddd: ggg
                eee: out
                fff: out
                ggg: out
                hhh: ccc fff iii
                iii: out
            """.trimIndent()
                .lines(),
            """
                svr: aaa bbb
                aaa: fft
                fft: ccc
                bbb: tty
                tty: ccc
                ccc: ddd eee
                ddd: hub
                hub: fff
                eee: dac
                dac: fff
                fff: ggg hhh
                ggg: out
                hhh: out
            """.trimIndent()
                .lines()
        )
    }
}