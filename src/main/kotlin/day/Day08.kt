package day

import allPairs
import asInts
import multiplied
import kotlin.math.sqrt

object Day08 : Day {

    data class JunctionBox(val x: Int, val y: Int, val z: Int) {
        fun distanceTo(other: JunctionBox): Double {
            return sqrt(
                (x - other.x).toDouble() * (x - other.x) +
                        (y - other.y).toDouble() * (y - other.y) +
                        (z - other.z).toDouble() * (z - other.z)
            )
        }
    }

    override fun part1(input: List<String>): Result {
        val boxes = input
            .map { it.split(",").asInts() }
            .map { (x, y, z) -> JunctionBox(x, y, z) }

        val distances = boxes
            .allPairs()
            .map { (a, b) -> Triple(a, b, a.distanceTo(b)) }
            .sortedBy { (_, _, distance) -> distance }

        val circuits = boxes.map { setOf(it) }.toMutableList()

        for ((a, b, _) in distances.take(1000)) {
            // Find the circuits that contain a or b.
            val toMerge = circuits.filter { it.contains(a) || it.contains(b) }
            // do nothing if they're already in the same circuit
            if (toMerge.size > 1) {
                circuits.removeAll(toMerge)
                // Merge the circuits together and add to the list of circuits again.
                circuits.add(toMerge.flatten().toMutableSet())
            }
        }

        return circuits.map { it.size }.sortedDescending().take(3).multiplied().asSuccess()
    }

    override fun part2(input: List<String>): Result {
        return NotImplemented
    }

    override fun testData(): Day.TestData {
        return Day.TestData(
            20,
            0,
            """
                162,817,812
                57,618,57
                906,360,560
                592,479,940
                352,342,300
                466,668,158
                542,29,236
                431,825,988
                739,650,466
                52,470,668
                216,146,977
                819,987,18
                117,168,530
                805,96,715
                346,949,466
                970,615,88
                941,993,340
                862,61,35
                984,92,344
                425,690,689
            """.trimIndent()
                .lines()
        )
    }
}