import java.util.*

data class Path<N>(val length: Double, val nodes: List<N>)
data class PathData<N>(var visitedNodes: Map<N, Path<N>> = mapOf())

fun <N> findPath(start: N, isEnd: (node: N) -> Boolean, data: PathData<N> = PathData(mapOf()), edgesForNode: (node: N) -> List<Pair<N, Double>>): Path<N>? {
    val distancesToNodes = mutableMapOf(start to Path(0.0, listOf(start)))
    val nodesToVisit: SortedSet<N> = TreeSet { left, right ->
        if (left == right) {
            return@TreeSet 0
        }

        val comparison = (distancesToNodes[left]!!.length - distancesToNodes[right]!!.length).toInt()
        if (comparison == 0) {
            if (left.hashCode() > right.hashCode()) 1 else -1
        } else {
            comparison
        }
    }
    var (shortestNode, shortestPath) = distancesToNodes.minBy { (_, path) -> path.length }
    while (!(isEnd(shortestNode))) {
        val edges = edgesForNode(shortestNode)
        edges.forEach { (node, length) ->
            val pathForNode = distancesToNodes[node]
            val newLength = shortestPath.length + length
            if (pathForNode == null || pathForNode.length > newLength) {
                if (distancesToNodes.contains(node)) {
                    // Because nodesToVisit is ordered by distancesToNodes, its invariants break when
                    //   a shorter path is found later on. In this case, we have to remove the node and readd it
                    nodesToVisit.remove(node)
                }
                val newPath = Path(shortestPath.length + length, shortestPath.nodes + node)
                distancesToNodes[node] = newPath
                nodesToVisit.add(node)
            } else {
                //println("Skipping")
            }
        }
        if (nodesToVisit.isEmpty()) {
            data.visitedNodes = distancesToNodes
            return null
        }
        shortestNode = nodesToVisit.first()
        nodesToVisit.remove(shortestNode)
        shortestPath = distancesToNodes[shortestNode]!!
    }
    data.visitedNodes = distancesToNodes
    return shortestPath
}

fun <N> findPathAStar(start: N, isEnd: (node: N) -> Boolean, data: PathData<N> = PathData(mapOf()), edgesForNode: (node: N) -> List<Pair<N, Double>>, heuristic: (node: N) -> Double): Path<N>? {
    val nodesToVisit = mutableSetOf(start)
    val previousNodes = mutableMapOf<N, N>()
    val actualDistances = mutableMapOf(start to 0.0)
    val fScores = mutableMapOf(start to heuristic(start))

    var loopcount = 0
    while (nodesToVisit.isNotEmpty()) {
        loopcount++
        val currentNode = nodesToVisit.minBy { fScores[it]!! }
        if (isEnd(currentNode)) {
            return reconstructPath(currentNode, previousNodes, actualDistances)
        }

        nodesToVisit.remove(currentNode)
        edgesForNode(currentNode).forEach { (neighbour, length) ->
            val tentativeDistance = actualDistances[currentNode]!! + length
            if (tentativeDistance < actualDistances.getOrDefault(neighbour, Double.MAX_VALUE)) {
                previousNodes[neighbour] = currentNode
                actualDistances[neighbour] = tentativeDistance
                fScores[neighbour] = tentativeDistance + heuristic(neighbour)
                nodesToVisit.add(neighbour)
            }
        }
        if (loopcount % 10_000 == 0) {
            println("Visited $loopcount nodes")
            println("Current node: $currentNode")
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