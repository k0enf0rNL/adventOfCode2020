package adventofcode.y2023

data class Card(val face: Char)

val possibleCards: List<Card> = listOf(Card('J'), Card('2'), Card('3'), Card('4'), Card('5'), Card('6'), Card('7'), Card('8'), Card('9'), Card('T'), Card('Q'), Card('K'), Card('A'))

fun String.toCards(): List<Card> =
    map { char -> possibleCards.first { it.face == char } }

enum class HandType {
    HIGH_CARD,
    ONE_PAIR,
    TWO_PAIR,
    THREE_OF_A_KIND,
    FULL_HOUSE,
    FOUR_OF_A_KIND,
    FIVE_OF_A_KIND
}

fun List<Card>.getType(): HandType {
    val grouped = groupBy { it }.values
    val handContainsJoker = grouped.any { it.contains(Card('J')) }
    return if (grouped.size == 1) {
        HandType.FIVE_OF_A_KIND
    } else if (grouped.size == 2 && grouped.minBy { it.size }.size == 1) {
        if (handContainsJoker) {
            HandType.FIVE_OF_A_KIND
        } else {
            HandType.FOUR_OF_A_KIND
        }
    } else if (grouped.size == 2) {
        if (handContainsJoker) {
            HandType.FIVE_OF_A_KIND
        } else {
            HandType.FULL_HOUSE
        }
    } else if (grouped.maxBy { it.size }.size == 3) {
        if (handContainsJoker) {
            HandType.FOUR_OF_A_KIND
        } else {
            HandType.THREE_OF_A_KIND
        }
    } else if (grouped.count { it.size == 2 } == 2) {
        if (handContainsJoker) {
            if (count { it == Card('J') } == 2) {
                HandType.FOUR_OF_A_KIND
            } else {
                HandType.FULL_HOUSE
            }
        } else {
            HandType.TWO_PAIR
        }
    } else if (grouped.count { it.size == 2 } == 1) {
        if (handContainsJoker) {
            HandType.THREE_OF_A_KIND
        } else {
            HandType.ONE_PAIR
        }
    } else {
        if (handContainsJoker) {
            HandType.ONE_PAIR
        } else {
            HandType.HIGH_CARD
        }
    }
}

data class Hand(val cards: List<Card>, val bet: Int): Comparable<Hand> {
    override fun compareTo(other: Hand): Int {
        val comparedTypes = cards.getType().compareTo(other.cards.getType())

        return if (comparedTypes == 0) {
            cards.mapIndexed { index, card -> possibleCards.indexOf(card).compareTo(possibleCards.indexOf(other.cards[index])) }.firstOrNull { it != 0 } ?: 0
        } else {
            comparedTypes
        }
    }
}

val input: List<String> = this::class.java.classLoader.getResource("2023/day7.txt")!!.readText().split(System.lineSeparator()).filter { it.isNotBlank() }

val hands: List<Hand> = input.map {
    val splitted = it.split(" ")
    Hand(
        splitted.first().toCards(),
        splitted.last().toInt()
    )
}

hands.sorted().map { it.cards to it.cards.getType() }.forEach { println(it) }

println(hands.sorted().mapIndexed { index, hand -> hand.bet * (index + 1) }.sum())


