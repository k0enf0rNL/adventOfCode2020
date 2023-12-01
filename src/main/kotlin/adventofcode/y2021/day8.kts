data class Line(
    val digits: List<String>,
    val output: List<String>
)

fun List<String>.toLine() =
    Line(
        first().split(" "),
        last().split(" ")
    )

val input: List<Line> = this::class.java.classLoader.getResource("2021/day8.txt")!!.readText()
    .split(System.lineSeparator())
    .filter { it.isNotBlank() }
    .map { it.split(" | ").toLine() }

//Part 1
input.map { it.output }.map { it.filter { it.length in listOf(2,3,4,7) }.count() }.sum().also {
    println("Part 1: $it")
}
