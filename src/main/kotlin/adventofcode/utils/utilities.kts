val input: List<String> = this::class.java.classLoader.getResource("2023/day7.txt")!!.readText().split(System.lineSeparator()).filter { it.isNotBlank() }

fun <T> List<T>.split(predicate: (T) -> Boolean): List<List<T>> =
    fold(mutableListOf(mutableListOf<T>())) { acc, t ->
        if (predicate(t)) acc.add(mutableListOf())
        else acc.last().add(t)
        acc
    }.filterNot { it.isEmpty() }

fun gcd(a: Long, b: Long): Long =
    if (b == 0L) a else gcd(b, a % b)

fun lcm(a: Long, b: Long): Long =
    (a * b) / gcd(a, b)

fun <T> List<List<T>>.transpose(): List<List<T>> {
    val transposedList = ArrayList<MutableList<T>>(maxOf { it.size })
    for (i in 1..maxOf { it.size }) {
        transposedList.add(ArrayList(size))
    }
    forEachIndexed { rowindex, row ->
        row.forEachIndexed { columnindex, column ->
            transposedList[columnindex].add(rowindex, column)
        }
    }
    return transposedList
}
