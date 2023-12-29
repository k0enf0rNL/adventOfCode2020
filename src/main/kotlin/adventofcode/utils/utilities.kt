package adventofcode.utils

import java.math.BigInteger
import java.security.MessageDigest
import kotlin.io.path.Path
import kotlin.io.path.readLines
import kotlin.math.abs

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
    }

fun <T> List<List<T>>.transpose(): List<List<T>> {
    if (this.isEmpty()) return this

    val width = this[0].size
    val height = this.size

    val transposed = MutableList(width) { MutableList(height) { this[0][0] } }

    for (i in 0 until width) {
        for (j in 0 until height) {
            transposed[i][j] = this[j][i]
        }
    }

    return transposed
}

fun <T : Any> Iterable<T>.distinctPairs(): Sequence<Pair<T, T>> = sequence {
    val iter = this@distinctPairs.iterator()
    if (!iter.hasNext()) return@sequence
    val previous = mutableListOf(iter.next())
    while (iter.hasNext()) {
        val second = iter.next()
        for (first in previous) yield(first to second)
        previous.add(second)
    }
}

fun gcd(a: Long, b: Long): Long =
    if (b == 0L) a else gcd(b, a % b)

// Least Common Multiple
fun lcm(a: Long, b: Long): Long =
    (a * b) / gcd(a, b)

// Picks theorem
fun calculateAreaOfPolygonByPicksTheorem(a: Long, b: Long): Long {
    return a - b / 2 + 1
}

// Shoelace formula
fun calculateAreaOfConvexHullUsingShoelace(points: List<Point>): Long {
    var sum1 = 0.0 // Sum of (x[i]*y[i+1]) for all i
    var sum2 = 0.0 // Sum of (y[i]*x[i+1]) for all i

    for (i in points.indices) {
        val nextIndex = if (i == points.size - 1) 0 else i + 1
        sum1 += points[i].columnIndex * points[nextIndex].rowIndex
        sum2 += points[i].rowIndex * points[nextIndex].columnIndex
    }

    return (abs(sum1 - sum2) / 2.0).toLong() // area by Shoelace formula
}
