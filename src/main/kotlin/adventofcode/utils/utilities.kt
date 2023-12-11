package adventofcode.utils

import java.math.BigInteger
import java.security.MessageDigest
import kotlin.io.path.Path
import kotlin.io.path.readLines

/**
 * Reads lines from the given input txt file.
 */
fun readInput(name: String) = Path("src/main/resources/$name").readLines()

/**
 * Converts string to md5 hash.
 */
fun String.md5() = BigInteger(1, MessageDigest.getInstance("MD5").digest(toByteArray()))
    .toString(16)
    .padStart(32, '0')

/**
 * The cleaner shorthand for printing output.
 */
fun Any?.println() = println(this)

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
