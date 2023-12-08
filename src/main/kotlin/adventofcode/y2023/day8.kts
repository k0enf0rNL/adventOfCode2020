package adventofcode.y2023

data class Node(val name: String) {
    lateinit var left: Node
    lateinit var right: Node

    fun next(char: Char) =
        if (char == 'L') {
            left
        } else {
            right
        }

    fun setLeftAndRight(left: Node, right: Node) {
        this.left = left
        this.right = right
    }
}

val input: List<String> = this::class.java.classLoader.getResource("2023/day8.txt")!!.readText().split(System.lineSeparator() + System.lineSeparator()).filter { it.isNotBlank() }

val instructions: CharArray = input.first().toCharArray()

val nodes: List<Node> =
    input.last().split(System.lineSeparator()).filter { it.isNotBlank() }.map { Node(it.split(" = ").first()) to it.split(" = ").last() }.let { nodesWithoutNextElement ->
        nodesWithoutNextElement.map {
            val splitted = Regex("\\w+").findAll(it.second)
            it.first.setLeftAndRight(
                nodesWithoutNextElement.first { it.first.name == splitted.first().value }.first,
                nodesWithoutNextElement.first { it.first.name == splitted.last().value }.first
            )
            it.first
        }
    }

var stepsPartOne: Int = 0

val start: Node = nodes.first { it.name == "AAA" }
var current: Node = start
val finish: Node = nodes.first { it.name == "ZZZ" }

while (true) {
    val char = instructions[stepsPartOne.mod(instructions.size)]
    val next = current.next(char)
    stepsPartOne++
    current = next
    if (next == finish) {
        break
    }
}

println(stepsPartOne)

var stepsPartTwo: Int = 0

val allStartNodes: List<Node> = nodes.filter { it.name.endsWith("A") }

private fun gcd(a: Long, b: Long): Long =
    if (b == 0L) a else gcd(b, a % b)

private fun lcm(a: Long, b: Long): Long =
    (a * b) / gcd(a, b)

private fun numberOfSteps(node: Node): Long {
    var steps = 0
    var currentNode: Node = node
    while (true) {
        val next = currentNode.next(instructions[steps.mod(instructions.size)])
        steps++
        currentNode = next
        if (currentNode.name.endsWith("Z")) {
            break
        }
    }
    return steps.toLong()
}

println(allStartNodes.map { numberOfSteps(it) }.reduce { acc, num -> lcm(acc, num) })
