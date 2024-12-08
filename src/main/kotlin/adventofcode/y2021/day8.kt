import adventofcode.utils.readInput

data class Line(
    val digits: List<String>,
    val output: List<String>
)

fun main() {

    fun List<String>.toLine() =
        Line(
            first().split(" "),
            last().split(" ")
        )

    val input: List<Line> = readInput("2021/input.txt")
        .map { it.split(" | ").toLine() }

//Part 1
    input.map { it.output }.map { it.filter { it.length in listOf(2, 3, 4, 7) }.count() }.sum().also {
        println("Part 1: $it")
    }
}
