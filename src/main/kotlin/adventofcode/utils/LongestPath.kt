package adventofcode.utils

import java.util.LinkedList

data class LongestPathNode(val row: Int, val col: Int)

data class JunctionConnection(val fromJunction: LongestPathNode, val toJunction: LongestPathNode, val steps: Int)

fun longestPathUsingOnlyJunctions(
    matrix: List<List<Char>>,
    startNode: LongestPathNode,
    endNode: LongestPathNode,
    allowedToStepOnto: (nextNode: Pair<Char, LongestPathNode>, currentNode: LongestPathNode) -> Boolean
): Set<JunctionConnection> {
    val defaultNeighbors = listOf(Pair(-1, 0), Pair(1, 0), Pair(0, -1), Pair(0, 1))
    val junctions: Set<LongestPathNode> = getJunctions(matrix, startNode, endNode, defaultNeighbors)
    val junctionConnections: Set<JunctionConnection> = getJunctionConnections(matrix, junctions, defaultNeighbors, allowedToStepOnto)
    val junctionConnectionsMap: MutableMap<LongestPathNode, MutableSet<JunctionConnection>> = mutableMapOf()
    junctionConnections.forEach {
        junctionConnectionsMap.getOrPut(it.fromJunction) { mutableSetOf() }.add(it)
    }

    val visited = HashMap<LongestPathNode, HashSet<JunctionConnection>>()
    val stack = LinkedList<Pair<LongestPathNode, HashSet<JunctionConnection>>>()
    stack.push(startNode to HashSet(listOf()))
    visited[startNode] = HashSet(listOf())

    while (stack.isNotEmpty()) {
        val (currentNode, pathOfJunctions) = stack.pop()

        val destinationJunctions = junctionConnectionsMap[currentNode]!!.map { it.toJunction }.toSet()

        destinationJunctions.forEach { destination ->
            if (pathOfJunctions.none { it.fromJunction == destination || it.toJunction == destination }) {
                val nextPath = pathOfJunctions + junctionConnections.first { (it.fromJunction == destination && it.toJunction == currentNode) || (it.fromJunction == currentNode && it.toJunction == destination) }
                if ((visited[destination]?.sumOf { it.steps } ?: 0) < nextPath.sumOf { it.steps }) {
                    visited[destination] = HashSet(nextPath)
                }
                if (destination != endNode) {
                    stack.push(destination to HashSet(nextPath))
                }
            }
        }
    }

    return visited[endNode] ?: emptySet()
}

private fun getJunctions(
    matrix: List<List<Char>>,
    startNode: LongestPathNode,
    endNode: LongestPathNode,
    defaultNeighbors: List<Pair<Int, Int>>
): Set<LongestPathNode> {
    val junctions: MutableSet<LongestPathNode> = mutableSetOf(startNode, endNode)
    val numRows = matrix.size
    val numCols = matrix[0].size
    matrix.forEachIndexed { rowIndex, chars ->
        chars.forEachIndexed { columnIndex, char ->
            if (char != '#') {
                val currentNode = LongestPathNode(rowIndex, columnIndex)
                var validAmountOfOtherDirections = 0
                for (neighborDirection in defaultNeighbors) {
                    val nextRow = rowIndex + neighborDirection.first
                    val nextCol = columnIndex + neighborDirection.second
                    val nextNode = LongestPathNode(nextRow, nextCol)
                    if (nextNode.row >= 0 && nextNode.row < numRows && nextNode.col >= 0 && nextNode.col < numCols && matrix[nextRow][nextCol] != '#'
                    ) {
                        validAmountOfOtherDirections++
                    }
                }
                if (validAmountOfOtherDirections > 2) {
                    junctions.add(currentNode)
                }
            }
        }
    }
    return junctions
}

private fun getJunctionConnections(
    matrix: List<List<Char>>,
    junctions: Set<LongestPathNode>,
    defaultNeighbors: List<Pair<Int, Int>>,
    allowedToStepOnto: (nextNode: Pair<Char, LongestPathNode>, currentNode: LongestPathNode) -> Boolean
): Set<JunctionConnection> {
    val junctionConnections: MutableSet<JunctionConnection> = mutableSetOf()
    val numRows = matrix.size
    val numCols = matrix[0].size
    junctions.forEach { junction ->
        val visited: MutableMap<LongestPathNode, MutableList<LongestPathNode>> = mutableMapOf()
        for (neighborDirection in defaultNeighbors) {
            val nextRow = junction.row + neighborDirection.first
            val nextCol = junction.col + neighborDirection.second
            val nextNode = LongestPathNode(nextRow, nextCol)
            if (nextNode.row >= 0 && nextNode.row < numRows && nextNode.col >= 0 && nextNode.col < numCols && matrix[nextRow][nextCol] != '#') {
                visited[nextNode] = mutableListOf(junction, nextNode)
            }
        }
        visited.keys.forEach {
            while (true) {
                var stop = false
                val currentNode = visited[it]!!.last()
                for (neighborDirection in defaultNeighbors) {
                    val nextRow = currentNode.row + neighborDirection.first
                    val nextCol = currentNode.col + neighborDirection.second
                    val nextNode = LongestPathNode(nextRow, nextCol)
                    if (nextNode.row >= 0 && nextNode.row < numRows && nextNode.col >= 0 && nextNode.col < numCols &&
                        !visited[it]!!.contains(nextNode)
                    ) {
                        if (allowedToStepOnto(matrix[nextRow][nextCol] to nextNode, currentNode)) {
                            visited[it]!!.add(nextNode)
                            if (junctions.contains(nextNode)) {
                                junctionConnections.add(JunctionConnection(junction, nextNode, visited[it]!!.size - 1))
                                stop = true
                            }
                        }
                    }
                }
                if (visited[it]!!.last() == currentNode) {
                    break
                }
                if (stop) {
                    break
                }
            }
        }
    }
    return junctionConnections
}

