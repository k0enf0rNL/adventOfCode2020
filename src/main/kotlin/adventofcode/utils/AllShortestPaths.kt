package adventofcode.utils

import java.util.PriorityQueue

/**
 * Implements A* search to find all shortest paths between two vertices using a predicate to determine the ending vertex
 */
fun <K> findAllShortestPathsByPredicate(
    start: K,
    endFunction: (K) -> Boolean,
    neighbours: NeighbourFunction<K>,
    cost: CostFunction<K> = { _, _ -> 1 },
    heuristic: HeuristicFunction<K> = { 0 }
): GraphSearchResultAllPaths<K> {
    val toVisit = PriorityQueue(listOf(ScoredVertexAllPaths(start, 0, heuristic(start))))
    val endVertices: MutableSet<K> = mutableSetOf()
    val seenPoints: MutableMap<K, SeenVertexAllPaths<K>> = mutableMapOf(start to SeenVertexAllPaths(0, emptyList()))

    while (toVisit.isNotEmpty()) {
        val (currentVertex, currentScore) = toVisit.remove()

        if (endFunction(currentVertex)) {
            endVertices.add(currentVertex)
        }

        val nextPoints = neighbours(currentVertex)
            .mapNotNull { next ->
                val newCost = currentScore + cost(currentVertex, next)
                val seen = seenPoints[next]

                when {
                    seen == null || newCost < seen.cost -> {
                        seenPoints[next] = SeenVertexAllPaths(newCost, listOf(currentVertex))
                        ScoredVertexAllPaths(next, newCost, heuristic(next))
                    }
                    newCost == seen.cost -> {
                        seenPoints[next] = seen.copy(prev = seen.prev + currentVertex)
                        null
                    }
                    else -> null
                }
            }

        toVisit.addAll(nextPoints)
    }

    return GraphSearchResultAllPaths(start, endVertices.firstOrNull(), seenPoints)
}

class GraphSearchResultAllPaths<K>(
    val start: K,
    val end: K?,
    private val result: Map<K, SeenVertexAllPaths<K>>
) {
    fun getScore(vertex: K) = result[vertex]?.cost ?: throw IllegalStateException("Result for $vertex not available")
    fun getScore() = end?.let { getScore(it) } ?: throw IllegalStateException("No path found")

    fun getPaths(): List<List<K>> = end?.let { getPaths(it, emptyList()) } ?: throw IllegalStateException("No path found")
    fun seen(): Set<K> = result.keys

    private fun getPaths(endVertex: K, pathEnd: List<K>): List<List<K>> {
        val previous = result[endVertex]?.prev

        if (previous.isNullOrEmpty()) {
            return listOf(listOf(endVertex) + pathEnd)
        }
        return previous.flatMap { prev ->
            getPaths(prev, listOf(endVertex) + pathEnd)
        }
    }
}

data class SeenVertexAllPaths<K>(val cost: Int, val prev: List<K>)

data class ScoredVertexAllPaths<K>(val vertex: K, val score: Int, val heuristic: Int) : Comparable<ScoredVertexAllPaths<K>> {
    override fun compareTo(other: ScoredVertexAllPaths<K>): Int = (score + heuristic).compareTo(other.score + other.heuristic)
}
