package day

import allPairs
import asInts
import kotlin.math.abs
import kotlin.math.max
import kotlin.math.min
import kotlin.streams.asStream

object Day09 : Day {

    private data class Tile(val x: Int, val y: Int)

    override fun part1(input: List<String>): Result {
        val tiles = input.map { it.split(",").asInts() }.map { (x, y) -> Tile(x, y) }

        return tiles.allPairs()
            .maxOf { (first, last) -> (abs(first.x - last.x) + 1) * (abs(first.y - last.y) + 1).toLong() }
            .asSuccess()
    }

    override fun part2(input: List<String>): Result {
        val tiles = input.map { it.split(",").asInts() }.map { (x, y) -> Tile(x, y) }

        // any 2 adjacent tiles in the list are connected, as well as the first and last.
        val connections = (tiles.zipWithNext { a, b -> Line(a, b) } + Line(tiles.first(), tiles.last()))

        // Use a java parallel stream to get a 10x speedup on my cpu.
        val allRects = tiles.allPairs()
            .asStream().parallel()
            .map { (first, last) -> Rect(first, last) }
            .filter { !hasTileInCenter(it, tiles) }
            .filter { rectIsInsideArea(it, connections) }
            .toList()

        val biggestRect = allRects.maxBy { it.size }
        return biggestRect.size.asSuccess()
    }

    private data class Rect(val corner1: Tile, val corner2: Tile) {
        val size get() = (abs(corner1.x - corner2.x) + 1) * (abs(corner1.y - corner2.y) + 1).toLong()

        val maxX get() = max(corner1.x, corner2.x)
        val minX get() = min(corner1.x, corner2.x)
        val maxY get() = max(corner1.y, corner2.y)
        val minY get() = min(corner1.y, corner2.y)
    }

    // returns true iff any of tiles is inside rect, not including the edges
    private fun hasTileInCenter(rect: Rect, tiles: List<Tile>): Boolean {
        return tiles.any { tile ->
            tile.x in (rect.minX + 1)..<rect.maxX
                    && tile.y in (rect.minY + 1)..<rect.maxY
        }
    }

    private data class Line(val start: Tile, val end: Tile) {
        val isVertical get() = start.x == end.x
        val isHorizontal get() = start.y == end.y
    }

    private fun rectIsInsideArea(rect: Rect, connections: List<Line>): Boolean {
        var isInside = false

        rayCast(rect.minX..<rect.maxX, connections, true) { tile ->
            if (!isInside && tile.y - 1 in rect.minY..<rect.maxY) return false
            isInside = !isInside
            if (!isInside && tile.y in rect.minY..<rect.maxY) return false
        }
        rayCast(rect.minY..<rect.maxY, connections, false) { tile ->
            if (!isInside && tile.x - 1 in rect.minX..<rect.maxX) return false
            isInside = !isInside
            if (!isInside && tile.x in rect.minX..<rect.maxX) return false
        }

        return true
    }

    private inline fun rayCast(
        range: IntRange,
        connections: List<Line>,
        rayDirectionIsVertical: Boolean,
        onCross: (tile: Tile) -> Unit
    ) {
        val connectionsToCheck = connections.filter { it.isVertical != rayDirectionIsVertical }
            .sortedBy { if (rayDirectionIsVertical) it.start.y else it.start.x }

        if (rayDirectionIsVertical) {
            for (x in range) {
                val ray = Line(Tile(x, Int.MIN_VALUE), Tile(x, Int.MAX_VALUE))
                connectionsToCheck.forEach { line -> if (rayIntersectsLine(ray, line)) onCross(Tile(x, line.start.y)) }
            }
        } else {
            for (y in range) {
                val ray = Line(Tile(Int.MIN_VALUE, y), Tile(Int.MAX_VALUE, y))
                connectionsToCheck.forEach { line -> if (rayIntersectsLine(ray, line)) onCross(Tile(line.start.x, y)) }
            }
        }
    }

    private fun rayIntersectsLine(ray: Line, line: Line): Boolean {
        // Check if lines are horizontal or vertical and intersect
        if (ray.isVertical && line.isVertical) return false // Both vertical, same orientation
        if (ray.isHorizontal && line.isHorizontal) return false // Both horizontal, same orientation
        if (ray.isVertical) {
            // Offset ray by half to make checking corners easier
            val x = ray.start.x + 0.5
            if (line.isHorizontal) {
                val y = line.start.y
                return x in (min(line.start.x, line.end.x).toDouble())..max(line.start.x, line.end.x).toDouble()
                        && y in min(ray.start.y, ray.end.y)..max(ray.start.y, ray.end.y)
            }
        } else if (ray.isHorizontal) {
            val y = ray.start.y + 0.5
            if (line.isVertical) {
                val x = line.start.x
                return x in (min(ray.start.x, ray.end.x) + 1)..<max(ray.start.x, ray.end.x)
                        && y in min(line.start.y, line.end.y).toDouble()..<max(line.start.y, line.end.y).toDouble()
            }
        }

        return false // No intersection or lines are of the same orientation

    }

    override fun testData(): Day.TestData {
        return Day.TestData(
            50,
            24,
            """
                7,1
                11,1
                11,7
                9,7
                9,5
                2,5
                2,3
                7,3
            """.trimIndent()
                .lines()
        )
    }
}