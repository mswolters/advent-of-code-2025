package day

import asInts
import findPath
import kotlin.math.sqrt

object Day10 : Day {

    data class Machine1(val lights: Int, val buttons: List<Int>)

    override fun part1(input: List<String>): Result {
        val machines = input.map { line ->
            val splitput = line.split(" ")
            val rawLights = splitput.first().drop(1).dropLast(1).map { it == '#' }
            val lights = rawLights.fold(0) { acc, b -> (acc shl 1) + if (b) 1 else 0 }
            val buttons = splitput.drop(1).dropLast(1)
                .map { it.drop(1).dropLast(1).split(",").asInts().fold(0) { acc, bit -> acc or (1 shl (rawLights.size - bit - 1)) } }
            Machine1(lights, buttons)
        }

        val fewestButtonsPerMachine = machines.map { machine ->
            val path = findPath(0, isEnd = { it == machine.lights }) { lightsState ->
                machine.buttons.map { button -> lightsState xor button to 1.0 }
            }
            if (path == null) throw IllegalStateException("No solution found")
            path
        }

        return fewestButtonsPerMachine.sumOf { it.length }.toInt().asSuccess()
    }

    data class Machine2(val buttons: List<Set<Int>>, val joltageRequirements: List<Int>)

    override fun part2(input: List<String>): Result {
        return NotImplemented
        /*val machines = input.map { line ->
            val splitput = line.split(" ")
            val buttons = splitput.drop(1).dropLast(1)
                .map { it.drop(1).dropLast(1).split(",").asInts().toSet() }
            val joltageRequirements = splitput.last().drop(1).dropLast(1).split(",").asInts()
            Machine2(buttons, joltageRequirements)
        }

        val fewestButtonsPerMachine = machines.mapIndexed { index, machine ->
            val path = findPath(
                List(machine.joltageRequirements.size) { 0 },
                isEnd = { it == machine.joltageRequirements },
                heuristic = { joltageState ->
                    nDimensionalDistance(joltageState, machine.joltageRequirements)
                }
            ) { joltageState ->
                machine.buttons.flatMap { button ->
                    listOf(
                        joltageState.mapIndexed { index, i -> i + if (button.contains(index)) 1 else 0 } to 1.0,
                        joltageState.mapIndexed { index, i -> i + if (button.contains(index)) 10 else 0 } to 10.0
                    )
                }
                    // Joltage states can never reduce, so when any is too high, the path can be thrown out
                    .filter { (joltageState, _) ->
                        joltageState.zip(machine.joltageRequirements).none { it.first > it.second }
                    }
            }
            println("Machine $index, path found: ${path?.length}")
            if (path == null)
                throw IllegalStateException("No solution found")
            path
        }

        return fewestButtonsPerMachine.sumOf { it.length }.toLong().asSuccess()*/
    }

    private fun nDimensionalDistance(a: List<Int>, b: List<Int>): Double {
        return sqrt(a.zip(b).map { it.first - it.second }.sumOf { it * it.toDouble() })
    }

    override fun testData(): Day.TestData {
        return Day.TestData(
            7,
            33,
            """
                [.##.] (3) (1,3) (2) (2,3) (0,2) (0,1) {3,5,4,7}
                [...#.] (0,2,3,4) (2,3) (0,4) (0,1,2) (1,2,3,4) {7,5,12,7,2}
                [.###.#] (0,1,2,3,4) (0,3,4) (0,1,2,4,5) (1,2) {10,11,11,5,10,5}
            """.trimIndent()
                .lines()
        )
    }
}