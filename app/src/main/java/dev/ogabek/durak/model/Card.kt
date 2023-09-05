package dev.ogabek.durak.model

import java.util.UUID

data class Card(
    val id: UUID = UUID.randomUUID(),
    val code: Int,
    val type: CardType
) {
    fun image(): String {
        val image = when(type) {
            CardType.CLUBS -> {
                "${code}c"
            }
            CardType.DIAMONDS -> {
                "${code}d"
            }
            CardType.HEARTS -> {
                "${code}h"
            }
            CardType.SPADES -> {
                "${code}s"
            }
        }

        return "file:///android_asset/${image}.png"
    }
}

enum class CardType {
    CLUBS,
    DIAMONDS,
    HEARTS,
    SPADES
}
