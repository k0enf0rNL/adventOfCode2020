package adventofcode.y2023

import adventofcode.utils.gcd
import adventofcode.utils.lcm
import adventofcode.utils.readInput
import java.util.LinkedList

sealed class Pulse(val source: String, val destination: String) {
    abstract override fun toString(): String

    class Low(source: String, destination: String) : Pulse(source, destination) {
        override fun toString(): String = "Low(source=$source,destination=$destination)"
    }
    class High(source: String, destination: String) : Pulse(source, destination) {
        override fun toString(): String = "High(source=$source,destination=$destination)"
    }
}

sealed class ModuleType(val name: String, val destinations: List<String>) {
    abstract fun receivePulse(pulse: Pulse): List<Pulse>
    abstract override fun toString(): String
    abstract fun reset()

    class FLipFlop(name: String, destinations: List<String>) : ModuleType(name, destinations) {
        private var state: Boolean = false

        override fun receivePulse(pulse: Pulse): List<Pulse> =
            when (pulse) {
                is Pulse.High -> emptyList()
                is Pulse.Low -> {
                    state = !state
                    destinations.map { if (state) Pulse.High(name, it) else Pulse.Low(name, it) }
                }
            }

        override fun reset() {
            state = false
        }

        override fun toString(): String = "FLipFlop(name=$name,destinations=$destinations,state=$state)"
    }

    class Conjuction(name: String, destinations: List<String>) : ModuleType(name, destinations) {
        val recentPulses: MutableMap<String, Pulse> = mutableMapOf()
        override fun receivePulse(pulse: Pulse): List<Pulse> {
            recentPulses.replace(pulse.source, pulse)
            return if (recentPulses.all { it.value is Pulse.High }) {
                destinations.map { Pulse.Low(name, it) }
            } else {
                destinations.map { Pulse.High(name, it) }
            }
        }

        override fun reset() {
            recentPulses.clear()
        }

        override fun toString(): String = "Conjuction(name=$name,destinations=$destinations,recentPulses=$recentPulses)"
    }

    class Broadcaster(name: String, destinations: List<String>) : ModuleType(name, destinations) {
        override fun receivePulse(pulse: Pulse): List<Pulse> =
            when(pulse) {
                is Pulse.High -> destinations.map { Pulse.High(name, it) }
                is Pulse.Low -> destinations.map { Pulse.Low(name, it) }
            }

        override fun reset() {}
        override fun toString(): String = "Broadcaster(name=$name,destinations=$destinations)"
    }
}

private fun initConjuctions(modules: HashMap<String, ModuleType>) {
    modules.values
        .filterIsInstance<ModuleType.Conjuction>()
        .forEach { conjuctionModule ->
            conjuctionModule.recentPulses.putAll(modules.values.filter {
                it.destinations.contains(
                    conjuctionModule.name
                )
            }.map { it.name to Pulse.Low(it.name, conjuctionModule.name) })
        }
}

fun main() {
    val input = readInput("2023/day20.txt")
    val modules: HashMap<String, ModuleType> = HashMap(input.associate {
        val name = Regex("(\\w+\\s->)").find(it)!!.value.dropLast(3)
        val destinations = Regex("(\\w+)").findAll(it).drop(1).map { it.value }.toList()
        name to (Regex("[%&]").find(it)
            ?.value
            ?.first()
            ?.let { if (it == '&') ModuleType.Conjuction(name, destinations) else ModuleType.FLipFlop(name, destinations) }
            ?: ModuleType.Broadcaster(name, destinations))
    })
    initConjuctions(modules)

    var countLows: Long = 0
    var countHighs: Long = 0
    // Part 1
    repeat(1000) {
        val queue: LinkedList<Pulse> = LinkedList()
        queue.add(Pulse.Low("button", "broadcaster"))
        while (!queue.isEmpty()) {
            val pulse = queue.pop()
            when (pulse) {
                is Pulse.High -> countHighs++
                is Pulse.Low -> countLows++
            }
            if (modules[pulse.destination] != null) {
                queue.addAll(modules[pulse.destination]!!.receivePulse(pulse))
            }
        }
    }
    println(countLows * countHighs)


    // Part 2
    modules.forEach { it.value.reset() }
    initConjuctions(modules)
    val cycleDetectionMap: MutableMap<String, MutableList<Long>> = mutableMapOf()

    var buttonPressedCount: Long = 0

    while(true) {
        buttonPressedCount++
        val queue: LinkedList<Pulse> = LinkedList()
        queue.add(Pulse.Low("button", "broadcaster"))
        while (!queue.isEmpty()) {
            val pulse = queue.pop()
            if (pulse.destination == "hp" && pulse is Pulse.High) {
                cycleDetectionMap.getOrPut(pulse.source) { mutableListOf() }.add(buttonPressedCount)
            }
            if (modules[pulse.destination] != null) {
                queue.addAll(modules[pulse.destination]!!.receivePulse(pulse))
            }
        }
        if (cycleDetectionMap.size == 4 && cycleDetectionMap.all { it.value.size == 2 })
            break
    }
    println(cycleDetectionMap)
    println(cycleDetectionMap.map { it.value.first() }.reduce { acc, num -> lcm(acc, num) })
}
