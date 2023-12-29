package adventofcode.y2023

import adventofcode.utils.distinctPairs
import adventofcode.utils.println
import adventofcode.utils.readInput
import java.math.BigInteger
import kotlin.math.min
import kotlin.math.sign

/**
 * Stone projected unto some axis (basically eliminating the projected axis, being x, y or z).
 */
data class ProjectedStone(val position: Pair<Long, Long>, val velocity: Pair<Long, Long>) {
    val a: BigInteger = velocity.second.toBigInteger()
    val b: BigInteger = -velocity.first.toBigInteger()
    val c: BigInteger = (velocity.second.toBigInteger() * position.first.toBigInteger() - velocity.first.toBigInteger() * position.second.toBigInteger())

    /**
     * Adds the specified velocity to this projected stone
     */
    fun addingVelocity(delta: Pair<Long, Long>): ProjectedStone {
        return copy(velocity = velocity.first + delta.first to velocity.second + delta.second)
    }

    override fun toString(): String = "$position @ $velocity"
}

data class HailStonePosition(val x: Long, val y: Long, val z: Long)

data class HailStoneVelocity(val x: Long, val y: Long, val z: Long)

data class HailStone(val position: HailStonePosition, val velocity: HailStoneVelocity) {
    fun slope(): Double = velocity.y.toDouble() / velocity.x.toDouble()

    fun projected(component: Int): ProjectedStone {
        // Take all components except the specified component
        return when (component) {
            0 -> ProjectedStone(position = (position.y to position.z), velocity = (velocity.y to velocity.z))
            1 -> ProjectedStone(position = (position.x to position.z), velocity = (velocity.x to velocity.z))
            2 -> ProjectedStone(position = (position.x to position.y), velocity = (velocity.x to velocity.y))
            else -> error("Invalid component: $component")
        }
    }
}

/** Determine where the hailstones [h1] and [h2] will collide _(if ignoring the Z axis)_, if at all. */
private fun calc2DOverlap(h1: HailStone, h2: HailStone): Boolean {
    fun validFuture(h: HailStone, cx: Double, cy: Double): Boolean {
        return !((h.velocity.x < 0 && h.position.x < cx) || (h.velocity.x > 0 && h.position.x > cx) || (h.velocity.y < 0 && h.position.y < cy) || (h.velocity.y > 0 && h.position.y > cy))
    }

    if (h1.slope() == h2.slope()) return false
    val cx = ((h2.slope() * h2.position.x) - (h1.slope() * h1.position.x) + h1.position.y - h2.position.y) / (h2.slope() - h1.slope())
    val cy = (h1.slope() * (cx - h1.position.x)) + h1.position.y
    val valid = validFuture(h1, cx, cy) && validFuture(h2, cx, cy)

    return cx in 200000000000000.0..400000000000000.0 && cy in 200000000000000.0..400000000000000.0 && valid
}

private fun solve(first: ProjectedStone, second: ProjectedStone): Pair<Long, Long>? {
    return solve(first.a, first.b, first.c, second.a, second.b, second.c)
}

// Solve two linear equations for x and y
// Equations of the form: ax + by = c
private fun solve(a1: BigInteger, b1: BigInteger, c1: BigInteger, a2: BigInteger, b2: BigInteger, c2: BigInteger): Pair<Long, Long>? {
    // a1*x + b1*y = c1
    // a2*x + b2*y = c2

    // b2*a1*x + b2*b1*y = b2*c1
    // b1*a2*x + b2*b1*y = b1*c2

    // x(b2*a1 - b1*a2) = b2*c1 - b1*c2

    // x = (b2*c1 - b1*c2) / (b2*a1 - b1*a2)

    // let d = (b2*a1 - b1*a2)

    // if d == 0: lines are parallel and there is no solution

    val d = b2 * a1 - b1 * a2
    if (d == BigInteger.ZERO) return null
    val x = (b2 * c1 - b1 * c2) / d
    val y = (c2 * a1 - c1 * a2) / d
    return (x.toLong() to y.toLong())
}

/**
 * Processes all pairs of stones by projecting them unto the specified component (0 == x, 1 == y, 2 == z).
 *
 * Optionally a delta velocity is applied to each stone.
 *
 * If the processing block returns false this function immediately exits
 */
private fun processPairs(stones: List<HailStone>, projectedComponent: Int, deltaSpeed: Pair<Long, Long> = 0L to 0L, process: (Pair<Long, Long>?) -> Boolean) {
    for (i in 0 until stones.size) {
        for (j in i + 1 until stones.size) {
            val firstStone = stones[i].projected(projectedComponent).addingVelocity(deltaSpeed)
            val secondStone = stones[j].projected(projectedComponent).addingVelocity(deltaSpeed)
            val intersection = solve(firstStone, secondStone)?.takeIf { p ->
                listOf(firstStone, secondStone).all { (p.second - it.position.second).sign == it.velocity.second.sign }
            }
            if (!process(intersection)) return
        }
    }
}

/**
 * Searches for multiple intersection position using the specified projected component (x, y or z-axis).
 *
 * Brute forces over combinations of vx, vy to find a possible solution.
 *
 * The key insight is that a minus delta velocity can be applied to any stone and assume the rock to remain stationary (speed zero).
 * Because the rock has to collide with every stone, the stone paths should all have an intersection (which is the position of the rock).
 *
 * Returns a pair of position to velocity of the rock found for the projection, or null if no solution could be found.
 */
private fun findRockPositionAndVelocity(stones: List<HailStone>, component: Int): Pair<Pair<Long, Long>, Pair<Long, Long>>? {
    val maxValue = 400L
    val minResultCount = 5
    for (vx in -maxValue..maxValue) {
        for (vy in -maxValue..maxValue) {
            val deltaV = (vx to vy)
            val matchingPositions = mutableSetOf<Pair<Long, Long>>()
            var resultCount = 0
            processPairs(stones, component, deltaV) { intersection ->
                if (intersection != null) {
                    matchingPositions += intersection
                    resultCount++
                    resultCount < minResultCount
                } else {
                    false
                }
            }
            // We need exactly 1 position with at least minResultCount matches
            if (matchingPositions.size == 1 && resultCount >= min(minResultCount, stones.size / 2)) {
                return matchingPositions.single() to (-deltaV.first to -deltaV.second)
            }
        }
    }
    return null
}

fun main() {
    val input = readInput("2023/day24.txt")
    val hailStones = input.map { line ->
        line.split("@").let {
            HailStone(
                Regex("([-\\d]+)").findAll(it.first()).map { it.value.toLong() }.toList().let { HailStonePosition(it[0], it[1], it[2]) },
                Regex("([-\\d]+)").findAll(it.last()).map { it.value.toLong() }.toList().let { HailStoneVelocity(it[0], it[1], it[2]) }
            )
        }
    }
    hailStones.distinctPairs().count { (h1, h2) -> calc2DOverlap(h1, h2) }.println()

    // Project to z-axis
    val result1 = findRockPositionAndVelocity(stones = hailStones, component = 2) ?: error("Could not find result")

    // Project to x-axis
    val result2 = findRockPositionAndVelocity(stones = hailStones, component = 0) ?: error("Could not find result")

    // Project to y-axis
    val result3 = findRockPositionAndVelocity(stones = hailStones, component = 1) ?: error("Could not find result")

    val (x1, y1) = result1.first
    val (y2, z1) = result2.first
    val (x2, z2) = result3.first
    val (vx1, vy1) = result1.second
    val (vy2, vz1) = result2.second
    val (vx2, vz2) = result3.second

    require(y1 == y2 && x1 == x2 && z1 == z2) {
        "Expected positions to match"
    }
    require(vy1 == vy2 && vx1 == vx2 && vz1 == vz2) {
        "Expected velocities to match"
    }

    println("Found rock position and velocity: $x1,$y1,$z1 @ $vx1,$vy1,$vz1")

    println(x1 + y1 + z1)
}
