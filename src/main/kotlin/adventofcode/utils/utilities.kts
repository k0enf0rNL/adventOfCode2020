val input: List<String> = this::class.java.classLoader.getResource("2023/day7.txt")!!.readText().split(System.lineSeparator()).filter { it.isNotBlank() }

fun <T> List<T>.split(predicate: (T) -> Boolean): List<List<T>> =
    fold(mutableListOf(mutableListOf<T>())) { acc, t ->
        if (predicate(t)) acc.add(mutableListOf())
        else acc.last().add(t)
        acc
    }.filterNot { it.isEmpty() }

private fun gcd(a: Long, b: Long): Long =
    if (b == 0L) a else gcd(b, a % b)

private fun lcm(a: Long, b: Long): Long =
    (a * b) / gcd(a, b)
