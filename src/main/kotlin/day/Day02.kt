package day

import asPair
import divisors
import properDivisors

object Day02 : Day {
    override fun part1(input: List<String>): Result {
        val result = input.first().split(",")
            .map { it.split("-").run { Pair(first(), last()) } }
            .flatMap { findInvalids(it) }
            .sum()
        return result.asSuccess()
    }

    fun findInvalids(range: Pair<String, String>): List<Long> {
        val longRange = LongRange(range.first.toLong(), range.second.toLong())
        val rangeToCheck = IntRange((range.first.take((range.first.length) / 2).ifEmpty { "1" }).toInt(), range.second.take((range.second.length + 1) / 2).toInt())

        return rangeToCheck.asSequence().map { it.toString() }
            .map { it + it }
            .map { it.toLong() }
            .filter { it in longRange }
            .toList()
    }

    override fun part2(input: List<String>): Result {
        val result = input.first().split(",")
            .map { it.split("-").run { LongRange(first().toLong(), last().toLong()) } }
            .flatMap { findInvalids2(it) }
            .sum()


        return result.asSuccess()
    }

    fun findInvalids2(range: LongRange): List<Long> {
        return range.filter {
            val asString = it.toString()
            if (asString.length == 1) {
                false
            } else {
                // This time we break up each number in all possible chunks. If every chunk is equal, the number was invalid
                val divisors = asString.length.properDivisors()
                divisors.any { divisor ->
                    val chunked = asString.chunked(divisor)
                    chunked.all { e -> e == chunked.first() }
                }
            }
        }
    }

    override fun testData(): Day.TestData {
        return Day.TestData(
            1227775554,
            4174379265,
            """
                11-22,95-115,998-1012,1188511880-1188511890,222220-222224,1698522-1698528,446443-446449,38593856-38593862,565653-565659,824824821-824824827,2121212118-2121212124
            """.trimIndent()
                .lines()
        )
    }
}