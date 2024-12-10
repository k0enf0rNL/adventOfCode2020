package adventofcode.utils

import java.util.PriorityQueue

/**
 * Implements A* search to find the shortest path between two vertices using a predicate to determine the ending vertex
 */
fun <K> findAllPathsByPredicate(
    start: K,
    endFunction: (K) -> Boolean,
    neighbours: NeighbourFunction<K>,
    cost: CostFunction<K> = { _, _ -> 1 },
    heuristic: HeuristicFunction<K> = { 0 }
): List<GraphSearchResult<K>> {
    val toVisit = PriorityQueue(listOf(ScoredVertex(start, 0, heuristic(start))))
    val endGraphSearchResults: MutableList<GraphSearchResult<K>> = mutableListOf()
    val seenPoints: MutableMap<K, SeenVertex<K>> = mutableMapOf(start to SeenVertex(0, null))

    while (toVisit.isNotEmpty()) {
        val (currentVertex, currentScore) = toVisit.remove()
        if (endFunction(currentVertex)) {
            endGraphSearchResults.add(GraphSearchResult(start, currentVertex, seenPoints))
        }

        val nextPoints = neighbours(currentVertex)
            .map { next -> ScoredVertex(next, currentScore + cost(currentVertex, next), heuristic(next)) }

        toVisit.addAll(nextPoints)
        seenPoints.putAll(nextPoints.associate { it.vertex to SeenVertex(it.score, currentVertex) })
    }

    return endGraphSearchResults
}