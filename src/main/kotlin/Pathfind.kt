import java.util.*
import kotlin.math.sign

data class Path<N>(val length: Double, val nodes: List<N>)
data class PathData<N>(var visitedNodes: Map<N, N> = mapOf(), var distances: Map<N, Double> = mapOf())

fun <N> findPath(
    start: N,
    isEnd: (node: N) -> Boolean,
    pathData: PathData<N> = PathData(),
    heuristic: (node: N) -> Double = { 0.0 },
    edgesForNode: (node: N) -> List<Pair<N, Double>>
): Path<N>? {
    val actualDistances = mutableMapOf(start to 0.0)
    val estimatedDistances = mutableMapOf(start to heuristic(start))
    val nodesToVisit: SortedSet<N> = TreeSet { left, right ->
            if (left == right) return@TreeSet 0

            val comparison = sign(estimatedDistances[left]!! - estimatedDistances[right]!!).toInt()
            if (comparison == 0) {
                if (left.hashCode() > right.hashCode()) 1 else -1
            } else {
                comparison
            }
        }
    nodesToVisit.add(start)
    val previousNodes = mutableMapOf<N, N>()

    pathData.distances = actualDistances
    pathData.visitedNodes = previousNodes

    while (nodesToVisit.isNotEmpty()) {
        val currentNode = nodesToVisit.first()
        if (isEnd(currentNode)) {
            return reconstructPath(currentNode, previousNodes, actualDistances)
        }

        nodesToVisit.remove(currentNode)
        estimatedDistances.remove(currentNode)
        edgesForNode(currentNode).forEach { (neighbour, length) ->
            val tentativeDistance = actualDistances[currentNode]!! + length
            if (tentativeDistance < actualDistances.getOrDefault(neighbour, Double.MAX_VALUE)) {
                previousNodes[neighbour] = currentNode
                actualDistances[neighbour] = tentativeDistance
                if (estimatedDistances.contains(neighbour)) {
                    // Because nodesToVisit is ordered by distancesToNodes, its invariants break when
                    //   a shorter path is found later on. In this case, we have to remove the node and readd it
                    nodesToVisit.remove(neighbour)
                }
                estimatedDistances[neighbour] = tentativeDistance + heuristic(neighbour)
                nodesToVisit.add(neighbour)
            }
        }
    }

    return null
}

fun <N> reconstructPath(current: N, previousNodes: Map<N, N>, distances: Map<N, Double>): Path<N> {
    val nodes = mutableListOf(current)
    while (nodes.last() in previousNodes) {
        val nextNode = previousNodes[nodes.last()]!!
        nodes += nextNode
    }
    return Path(distances[current]!!, nodes.asReversed())
}